package com.project.growwithsunglow.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.project.growwithsunglow.GdhModel;
import com.project.growwithsunglow.R;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import cz.msebera.android.httpclient.Header;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class BlockDetails extends AppCompatActivity {
    TextView planted, daysAfter, gdhRequired, gdhReached, gdhDateReached,
            numDaysLeft, predicted, tillTarget, chillHrs, gdhACReached, gdhDateACReached,
            predictedHarvest, chillDate, daysLeftAC;
    String block, variety, propagator, current, dateAfter,
            fDateAfter, gdhR, reachedDate, finalDate , finalTemp, dateCalGdh,dateTrackGdh = "", chillD ="",
            gdhAcDate = "", floweringStatus = "";
    LocalDate fDate, thirdDate, reachedGDHDate, date1, date2, date3, reachedChill, loopDateGDHAC;
    double gdhLeft, gdhACLeft, gdh = 0.0, sumTrackGdh = 0, sumGDHSinceChill = 0, sumDaysLeftAC =0,
            totalDaysLeftAC, daysLeft, sumGetDaysLeft =0, totalGetDaysLeft, getDaysLeft;;
    int blockNum, countChill = 0;
    float sum, average;
    float countAvg = 1;
    boolean reachedTheGdh = false;
    Button threeDaysAfter;
    String key = "";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    AsyncHttpClient client;
    SimpleDateFormat sdf, sdf2;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference statusDatabase = FirebaseDatabase.getInstance().getReference("Blocks");
    Workbook workbook;
    ArrayList<AvgTemp> avgTempList;
    ArrayList<String> datesList = new ArrayList<String>();
    ArrayList<String> gdhDatesList = new ArrayList<String>();
    ArrayList<GdhModel> dailyGdhList = new ArrayList<GdhModel>();
    List<LTemp> listTemp = new ArrayList<>();
    String url;
    DateTimeFormatter formatter, formatter2;
    AvgTemp avgTemp = null;
    float loopTemp1, loopTemp2, loopTemp3;
    TempAdapter tempAdapter;
    private String  status = "";
    private boolean isUpdating = false;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.block_layout);

        planted = findViewById(R.id.plantedCard);
        daysAfter = findViewById(R.id.daysAfterCard);
        threeDaysAfter = findViewById(R.id.threeDaysButton);
        tillTarget = findViewById(R.id.leftTillTarget);
        gdhRequired = findViewById(R.id.GDHRequiredCard);
        gdhReached = findViewById(R.id.gdhReached);
        gdhDateReached = findViewById(R.id.gdhReachedDate);
        gdhACReached = findViewById(R.id.gdhACReached);
        gdhDateACReached = findViewById(R.id.gdhACReachedDate);
        numDaysLeft = findViewById(R.id.basedOnGdhDayLeftCard);
        predicted = findViewById(R.id.predictedCard);
        chillHrs = findViewById(R.id.chillCard);
        chillDate = findViewById(R.id.chillDateCard);
        predictedHarvest = findViewById(R.id.predictedHarvestCard);
        daysLeftAC = findViewById(R.id.daysLeftACCard);
        progressBar = findViewById(R.id.progressBar);


        progressBar.setVisibility(View.VISIBLE);



        formatter  = DateTimeFormatter.ofPattern("M/d/yy");
        formatter2  = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            planted.setText(bundle.getString("Date"));
            variety = bundle.getString("Variety");
            propagator = bundle.getString("Propagator");
            block = bundle.getString("Block");
            //key = bundle.getString("key");

        }
        Log.d("Edit ", block + " " +planted.getText().toString() + " " + variety + " " + propagator+ " "+ status);

        blockNum = Integer.parseInt(block);

        getSupportActionBar().setTitle("Block " + block);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String plantedS = planted.getText().toString().trim();
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf2= new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("M/d/yy");

        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(plantedS));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.add(Calendar.DATE, 56);
        dateAfter = sdf.format(cal.getTime());
        daysAfter.setText(dateAfter);
        fDateAfter = sdf1.format(cal.getTime());

        pullTempData();
        pullGDHData();
        gdhR = "14500";
        gdhRequired.setText(gdhR);


        url = "https://github.com/EmmaMcGuinness/excelExample/blob/main/Blocks.xls?raw=true";

        avgTempList = new ArrayList<>();
        client = new AsyncHttpClient();
        client.setEnableRedirects(true, true, true);



        downloadData();


    }






    private void downloadData() {
        avgTempList.clear();
        client.get(url, new FileAsyncHttpResponseHandler(this) {

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Toast.makeText(BlockDetails.this, "Download Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                listTemp.clear();
                Toast.makeText(BlockDetails.this, "File downloaded", Toast.LENGTH_SHORT).show();
                WorkbookSettings ws = new WorkbookSettings();
                ws.setGCDisabled(true);
                int blockNo = 1;
                if (file != null) {
                    try {
                        workbook = workbook.getWorkbook(file);
                        Sheet sheet = workbook.getSheet(blockNum -1);
                        for (int i = 0; i < sheet.getRows(); i++) {
                            Cell[] row = sheet.getRow(i);
                            if (row[1] != null && row[1].getType() != CellType.EMPTY) {
                                String date = row[1].getContents();
                                finalDate = date.substring(0, date.length() - 5);

                            }

                            if (row[2] != null && row[2].getType() != CellType.EMPTY) {
                                String temp = row[2].getContents();
                                finalTemp = temp.substring(0, temp.length() - 2);
                            }


                            LTemp lTemp = new LTemp(finalDate, finalTemp);
                            listTemp.add(lTemp);


                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (BiffException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    getAvgTemp();
                } catch (ParseException e) {
                    e.printStackTrace();
                }



            }
        });
    }

    private void pullTempData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datesList.clear();

                for (DataSnapshot avgTemps : snapshot.child("Blocks").child(block).child("AverageTemp").getChildren()) {
                    final String getDate = avgTemps.child("date").getValue(String.class);
                    //Log.d("dates", getDate);
                    datesList.add(getDate);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void pullGDHData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gdhDatesList.clear();

                for (DataSnapshot gdh : snapshot.child("Blocks").child(block).child("GDH").getChildren()) {
                    final String getDate = gdh.child("date").getValue(String.class);
                    gdhDatesList.add(getDate);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);

        return true;
    }


    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.action_Download) {
            downloadData();
        } else if (id == R.id.action_Edit) {

            Intent i = new Intent(BlockDetails.this, UpdateActivity.class)
                    .putExtra("Block", block)
                    .putExtra("Planted", planted.getText().toString())
                    .putExtra("Variety", variety)
                    .putExtra("Propagator", propagator)
                    .putExtra("Status", status);
            startActivity(i);
        }else if (id == R.id.action_Delete){

            deleteBlock();

        }
        if (id == android.R.id.home) {
            finish();
            onBackPressed();
            return true;
        }

        return true;
    }

    private void deleteBlock() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(BlockDetails.this);
        alertBuilder.setTitle("Delete Block");
        alertBuilder.setMessage("Are you sure you want to delete Block " + block+ "?");
        alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(BlockDetails.this, "Block deleted", Toast.LENGTH_SHORT).show();
                databaseReference.child("Blocks").child(block).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                });
            }
        });
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    private void getAvgTemp() throws ParseException {
        Log.d("getAvgTemp", "here");

        sum = Float.parseFloat(listTemp.get(0).getTemp());
        current = listTemp.get(0).getDate();
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    for (int i = 1; i < listTemp.size(); i += 1) {
                        String loopDate = listTemp.get(i).getDate().trim();
                        float loopTemp = Float.parseFloat(listTemp.get(i).getTemp());
                        if (current.equals(loopDate)) {
                            sum = sum + loopTemp;
                            countAvg += 1;
                            if (i == listTemp.size() - 1) {
                                average = sum / countAvg;
                                // Log.d("Main", "Average of group is " + current + " is " + average);
                                avgTemp = new AvgTemp(current, String.valueOf(average));
                                avgTempList.add(avgTemp);
                                if (!datesList.contains(current)) {
                                    // Log.d("DateList", "Date is not in database");
                                    FirebaseDatabase.getInstance().getReference("Blocks").child(block).child("AverageTemp").push()
                                            .setValue(avgTemp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {


                                                    }

                                                }
                                            });
                                }

                            }
                        } else {
                            average = sum / countAvg;
                            // Log.d("Main", "Average of group is " + current + " is " + average);

                            avgTemp = new AvgTemp(current, String.valueOf(average));
                            avgTempList.add(avgTemp);
                            if (!datesList.contains(current)) {
                                // Log.d("DateList", "Date is not in database");
                                FirebaseDatabase.getInstance().getReference("Blocks").child(block).child("AverageTemp").push()
                                        .setValue(avgTemp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                }

                                            }
                                        });
                            }
                            current = loopDate;
                            sum = loopTemp;
                            countAvg = 1;
                        }


                    }
                });
        future.thenRun(() -> {
            try {
                getFirstDate();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }
    private void getFirstDate() throws ParseException {
        Log.d("getFirstDate", "here");
        String fSep = "9/1/22";
        LocalDate firstSep = LocalDate.parse(fSep, formatter);

        fDate = LocalDate.parse(fDateAfter, formatter);
        if (fDate.isAfter(firstSep)){
            trackThreeDays();

        }else{
            fDate = LocalDate.parse(fSep,formatter);
            trackThreeDays();

     }

    }

    private void trackThreeDays() throws ParseException {
        Log.d("TrackThreeDays", "here");

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            for (int i = 0; i < avgTempList.size() - 1; i += 1) {
                loopTemp1 = Float.parseFloat(avgTempList.get(i).getTemp());
                date1 = LocalDate.parse(avgTempList.get(i).getDate().trim(), formatter);
                if (date1.equals(fDate)) {
                    if (loopTemp1 < 16.0) {
                        //  threeDaysAfter.setBackgroundColor(ContextCompat.getColor(BlockDetails.this, R.color.orange));
                        if (i + 1 < avgTempList.size()) {
                            loopTemp2 = Float.parseFloat(avgTempList.get(i + 1).getTemp());
                            date2 = LocalDate.parse(avgTempList.get(i + 1).getDate().trim(), formatter);

                            if (date1.plusDays(1).equals(date2) && loopTemp2 < 16.0) {
                                // threeDaysAfter.setBackgroundColor(Color.YELLOW);
                                if (i + 2 < avgTempList.size()) {
                                    loopTemp3 = Float.parseFloat(avgTempList.get(i + 2).getTemp());
                                    date3 = LocalDate.parse(avgTempList.get(i + 2).getDate().trim(), formatter);
                                    if (date2.plusDays(1).equals(date3) && loopTemp3 < 16.0) {
                                        //  Log.d("Colour", "Green Temp:" + loopTemp3 + " Date: " + date3);
                                        runOnUiThread(() -> {
                                                    threeDaysAfter.setBackgroundColor(Color.GREEN);
                                                    thirdDate = date3;
                                                    predicted.setText(thirdDate.format(formatter2));
                                                });
                                        calculateGDH();
                                        return;
                                    } else {
                                        runOnUiThread(() -> {
                                                    threeDaysAfter.setBackgroundColor(Color.YELLOW);
                                                });
                                        fDate = fDate.plusDays(1);
                                    }
                                } else {
                                    //  Log.d("Colour", "Green Temp: Not available");
                                    runOnUiThread(() -> {
                                        threeDaysAfter.setBackgroundColor(Color.YELLOW);
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(BlockDetails.this, "Pre-Flower Induction", Toast.LENGTH_LONG).show();
                                    });
                                    return;
                                }
                            } else {
                                threeDaysAfter.setBackgroundColor(ContextCompat.getColor(BlockDetails.this, R.color.orange));
                                fDate = fDate.plusDays(1);

                            }
                        } else {
                            // Log.d("Colour", "Yellow Temp: Not available");
                            runOnUiThread(() -> {
                                threeDaysAfter.setBackgroundColor(ContextCompat.getColor(BlockDetails.this, R.color.orange));
                                progressBar.setVisibility(View.GONE);
                            });
                            return;
                        }
                    } else {
                        //  Log.d("Colour", "Red 1 " + loopTemp1);
                        fDate = fDate.plusDays(1);
                    }
                }

            }
        });

        }

        private void setStatus(String newStatus) {
            Log.d("setStatus", "here");
                statusDatabase.child(block).child("status").setValue(newStatus)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       // Log.d("Status ", "Status updated : " + newStatus);

                    }
                });

        }

    private void calculateGDH() {
        Log.d("calculateGDH", "here");


        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    for (int i = 0; i < avgTempList.size() - 1; i += 1) {
                        LocalDate loopDate = LocalDate.parse(avgTempList.get(i).getDate().trim(), formatter);
                        Double loopTemp = Double.parseDouble(avgTempList.get(i).getTemp());
                        while (loopDate.equals(thirdDate)) {
                            if (loopTemp > 1) {
                                dateCalGdh = loopDate.format(formatter2);
                                gdh = Math.round(((loopTemp - 4.5) * 24) * 100) / 100.0;
                                GdhModel gdh1 = new GdhModel(dateCalGdh, String.valueOf(gdh));
                                if (gdhDatesList.contains(dateCalGdh)) {
                                    // Log.d("gdh ", "already in database");
                                } else if (!gdhDatesList.contains(dateCalGdh)) {
                                    //  Log.d("gdh", "Not in database " + date);
                                    FirebaseDatabase.getInstance().getReference("Blocks").child(block).child("GDH").push()
                                            .setValue(gdh1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                    } else {

                                                    }

                                                }
                                            });
                                }
                                thirdDate = thirdDate.plusDays(1);

                            }
                        }
                    }
                });

        future.thenRun(() -> {
            getGDHDates();

        });

    }
    private void getGDHDates() {
        Log.d("getGDHDates", "here");
        Query getGDH = databaseReference.orderByChild("date");
        getGDH.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                dailyGdhList.clear();

                for (DataSnapshot gdhs : snapshot.child("Blocks").child(block).child("GDH").getChildren()) {
                    final String getDate = gdhs.child("date").getValue(String.class);
                    final String getGdh = gdhs.child("gdh").getValue(String.class);
                    GdhModel gdh = new GdhModel(getDate, getGdh);
                    dailyGdhList.add(gdh);

                }

                try {
                    trackAllGdh();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void trackAllGdh() throws ParseException {
        Log.d("trackAllGdh", "here");
        sumTrackGdh = 0;
        CompletableFuture.runAsync(() -> {
            for (int i = 0; i < dailyGdhList.size(); i += 1) {
                dateTrackGdh = dailyGdhList.get(i).getDate().trim();
                Double loopGdh = Double.parseDouble(dailyGdhList.get(i).getGdh());
                sumTrackGdh = Math.round(sumTrackGdh + loopGdh);
                if (sumTrackGdh >= Double.parseDouble(gdhR)) {
                    reachedDate = String.valueOf(dateTrackGdh);
                    runOnUiThread(() -> {
                    gdhReached.setText(String.valueOf(sumTrackGdh));
                    gdhDateReached.setText(dateTrackGdh);
                    gdhReached.setTextColor(ContextCompat.getColor(BlockDetails.this, R.color.green));
                    tillTarget.setText(sumTrackGdh + "  /");
                    });
                    reachedGDHDate = LocalDate.parse(reachedDate, formatter2);
                    try {
                        calculateChill();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }

            gdhLeft = Math.round((Double.parseDouble(gdhR) - sumTrackGdh)*100.0)/100.0;
            runOnUiThread(() -> {
                tillTarget.setText(sumTrackGdh + "  /");
            });
            if(sumTrackGdh < Double.parseDouble(gdhR)){
                runOnUiThread(() -> {
                    Toast.makeText(BlockDetails.this, "In the flower initiation stage", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                });
                getDaysLeft();

                String newStatus = "flower initiation";
                setStatus(newStatus);
            }
        });

    }

    private void getDaysLeft() {

        List<GdhModel> lastSeven = dailyGdhList.subList(dailyGdhList.size() - 7, dailyGdhList.size());
        CompletableFuture.runAsync(() -> {
            for (int i = 0; i < lastSeven.size(); i += 1) {
                Double loopTemp = Double.parseDouble(lastSeven.get(i).getGdh());
                sumGetDaysLeft = (sumGetDaysLeft + loopTemp);
            }

            totalGetDaysLeft = sumGetDaysLeft / lastSeven.size();
            getDaysLeft = Math.round(gdhLeft / totalGetDaysLeft);

            // Log.d("DaysLeft", "Total: "+ totalGetDaysLeft + " days:" + lastSeven.size() + " sumGetDaysLeft: " + sumGetDaysLeft);
            runOnUiThread(() -> {
                numDaysLeft.setText(String.valueOf(getDaysLeft) + "days");
            });
        });
    }


    private void calculateChill() throws ParseException {
        Log.d("calculateChill", "here");

        countChill = 0;

        CompletableFuture.runAsync(() -> {
                    for (int i = 0; i < listTemp.size() - 1; i += 1) {
                        float loopTemp1 = Float.parseFloat(listTemp.get(i).getTemp());
                        LocalDate date1 = LocalDate.parse(listTemp.get(i).getDate().trim(), formatter);

                        if (date1.isAfter(reachedGDHDate) || date1.equals(reachedGDHDate)) {
                            chillD = date1.format(formatter2);
                            if (loopTemp1 < 8) {
                                countChill++;
                                if (countChill == 800) {
                                    reachedChill = date1;
                                    runOnUiThread(() -> {
                                        chillHrs.setTextColor(ContextCompat.getColor(BlockDetails.this, R.color.green));
                                        chillDate.setText(chillD);
                                            });
                                    calculateGdhSinceChill();
                                    break;
                                }
                            }
                        }
                    }
                    if (countChill < 800) {
                        runOnUiThread(() -> {
                            Toast.makeText(BlockDetails.this, "In the chilling stage", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            chillHrs.setText(String.valueOf(countChill));
                            chillDate.setText(chillD);
                            chillHrs.setTextColor(Color.RED);
                        });
                        String chillStatus = "Chilling";
                        // Log.d("Status", "Chilling");
                        setStatus(chillStatus);
                    }
            runOnUiThread(() -> {
                chillHrs.setText(String.valueOf(countChill));
            });
                });

            // Log.d("Chill" , String.valueOf(countChill) + " "+ String.valueOf(reachedChill));





    }

    private void calculateGdhSinceChill() {
        Log.d("calculateGdhSinceChill", "here");

        sumGDHSinceChill = 0;

        CompletableFuture.runAsync(() -> {
                    for (int i = 0; i < dailyGdhList.size(); i += 1) {
                        loopDateGDHAC = LocalDate.parse(dailyGdhList.get(i).getDate().trim(), formatter2);
                        Double loopGdh = Double.parseDouble(dailyGdhList.get(i).getGdh());
                        if (loopDateGDHAC.isAfter(reachedChill) || loopDateGDHAC.equals(reachedChill)) {
                            sumGDHSinceChill = Math.round(sumGDHSinceChill + loopGdh);
                            runOnUiThread(() -> {
                                gdhACReached.setText(String.valueOf(sumGDHSinceChill));
                                gdhAcDate = loopDateGDHAC.format(formatter2);
                                gdhDateACReached.setText(gdhAcDate);
                                gdhACReached.setTextColor(Color.RED);
                            });
                            if (sumGDHSinceChill >= 16000) {
                                runOnUiThread(() -> {
                                    gdhACReached.setTextColor(ContextCompat.getColor(BlockDetails.this, R.color.green));
                                    predictedHarvest.setText(gdhAcDate);
                                    Toast.makeText(BlockDetails.this, "Ready to Harvest!", Toast.LENGTH_LONG).show();
                                     Log.d("Status", "Harvesting");
                                    progressBar.setVisibility(View.GONE);
                                });
                                    String harvestStatus = "Harvest";
                                    setStatus(harvestStatus);
                                break;
                            }
                        }
                    }
                    gdhACLeft = Math.round((16000 - sumGDHSinceChill) * 100.0) / 100.0;
                    if (sumGDHSinceChill < 16000) {
                        getDaysLeftAC();
                        runOnUiThread(() -> {
                            Toast.makeText(BlockDetails.this, "In the post-chilling stage", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        });
                        floweringStatus = "Post- \nChilling";
                        Log.d("Status", "flowering");
                        setStatus(floweringStatus);

                    }
                });
      //  Log.d("GDH after Chill" , String.valueOf(loopDateGDHAC) + " "+ String.valueOf(sumGDHSinceChill));
    }

    private void getDaysLeftAC() {
        Log.d("getDaysLeftAC", "here");
        List<GdhModel> lastSeven = dailyGdhList.subList(dailyGdhList.size() - 7, dailyGdhList.size());


        CompletableFuture.runAsync(() -> {
        for(int i = 0; i < lastSeven.size(); i+=1){
            Double loopTemp = Double.parseDouble(lastSeven.get(i).getGdh());
            sumDaysLeftAC = (sumDaysLeftAC + loopTemp);
        }

        totalDaysLeftAC = sumDaysLeftAC/lastSeven.size();
        daysLeft =Math.round(gdhACLeft/totalDaysLeftAC);
            runOnUiThread(() -> {
                daysLeftAC.setText(String.valueOf(daysLeft) + "days");
            });
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}


