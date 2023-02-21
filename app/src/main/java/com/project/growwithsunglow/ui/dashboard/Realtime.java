package com.project.growwithsunglow.ui.dashboard;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.project.growwithsunglow.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class Realtime extends AppCompatActivity {
    RecyclerView recyclerView;
    AsyncHttpClient client;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    Workbook workbook;
    ArrayList<AvgTemp> avgTempList;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realtime_layout);

        url = "https://github.com/EmmaMcGuinness/excelExample/blob/main/Block1Example.xls?raw=true";

        recyclerView = findViewById(R.id.listOfData);

        avgTempList = new ArrayList<>();
        client = new AsyncHttpClient();


    }
    private void downloadData(){
        client.get(url, new FileAsyncHttpResponseHandler(this) {

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Toast.makeText(Realtime.this, "Download Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                Toast.makeText(Realtime.this, "File downloaded", Toast.LENGTH_SHORT).show();
                WorkbookSettings ws = new WorkbookSettings();
                ws.setGCDisabled(true);
                int blockNo = 1;
                if(file != null){
                    try {
                        workbook = workbook.getWorkbook(file);
                        Sheet sheet = workbook.getSheet(0);
                        for(int i = 0; i < sheet.getRows(); i++){
                            Cell[] row = sheet.getRow(i);
                            String date = row[0].getContents();
                            String temp = row[2].getContents();
                            AvgTemp avgTemp = new AvgTemp(temp,date,blockNo);
                            FirebaseDatabase.getInstance().getReference("AvgTemp").push()
                                    .setValue(avgTemp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                //  Toast.makeText(Realtime.this, "Successfully added", Toast.LENGTH_SHORT).show();

                                            }else{
                                                Toast.makeText(Realtime.this, "Not added", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }catch (BiffException e){
                        e.printStackTrace();
                    }
                }

            }
        });
    }
    private void addTempsToRecycler() {
        recyclerView = findViewById(R.id.listOfData);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(Realtime.this));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                avgTempList.clear();

                for (DataSnapshot avgTemps : snapshot.child("AvgTemp").getChildren()) {
                    final String getDate = avgTemps.child("date").getValue(String.class);
                    final String getTemp = avgTemps.child("temp").getValue(String.class);
                    final int getBlockNo = avgTemps.child("blockNo").getValue(Integer.class);


                    AvgTemp avgTemp = new AvgTemp(getDate, getTemp, getBlockNo);
                    avgTempList.add(avgTemp);

                }
                recyclerView.setAdapter(new TempAdapter(Realtime.this, avgTempList));

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
            addTempsToRecycler();
        }

        return true;
    }
}
