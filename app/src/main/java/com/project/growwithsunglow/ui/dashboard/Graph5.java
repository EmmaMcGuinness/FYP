package com.project.growwithsunglow.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.growwithsunglow.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Graph5 extends Fragment {

        private LineChart mChart;
        float floatGdh1, floatGdh2;
        Toolbar toolbar;
        ArrayList<Entry> gdhData1 = new ArrayList<>();
        ArrayList<Entry> gdhData2 = new ArrayList<>();

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.graph2_layout, container, false);

            mChart = view.findViewById(R.id.chart1);


            mChart.setTouchEnabled(true);
            mChart.setDragEnabled(true);
            mChart.setScaleEnabled(true);
            mChart.setPinchZoom(true);
            mChart.getDescription().setEnabled(false);

            XAxis xAxis = mChart.getXAxis();
            xAxis.setValueFormatter(new DateValueFormatter("dd/MM/yyyy"));


            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);
            xAxis.setLabelRotationAngle(-45);


            getBlocksGDHDates();

            return view;
        }

        private void getBlocksGDHDates() {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            Query getGDH = databaseReference.orderByChild("date");

            getGDH.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    gdhData1.clear();

                    for (DataSnapshot dataSnapshot : snapshot.child("Blocks").child(String.valueOf(5)).child("GDH").getChildren()) {
                        String gdhDate1 = dataSnapshot.child("date").getValue(String.class);
                        String gdh1 = dataSnapshot.child("gdh").getValue(String.class);
                        //String gdhDate2 = dataSnapshot.child(String.valueOf(2)).child("date").getValue(String.class);
                        //String gdh2 = dataSnapshot.child(String.valueOf(2)).child("gdh").getValue(String.class);

                        floatGdh1 = Float.parseFloat(gdh1);
                        // floatGdh2 = Float.parseFloat(gdh2);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date date1 = null;
                        //  Date date2 = null;
                        try{
                            date1 = dateFormat.parse(gdhDate1);
                            //  date2 = dateFormat.parse(gdhDate2);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        gdhData1.add(new Entry(date1.getTime(), floatGdh1));
                        //  gdhData2.add(new Entry(date2.getTime(), floatGdh2));
                    }
                    LineDataSet dataSet1 = new LineDataSet(gdhData1, "Block 5 GDH");
                    dataSet1.setColor(Color.RED);
                    dataSet1.setLineWidth(2f);
                    dataSet1.setDrawCircles(false);
                    dataSet1.setDrawValues(false);


                    LineData lineData = new LineData(dataSet1);
                    lineData.setDrawValues(false);

                    mChart.setData(lineData);
                    mChart.invalidate();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


    }