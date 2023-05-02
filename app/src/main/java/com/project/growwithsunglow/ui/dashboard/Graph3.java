package com.project.growwithsunglow.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class Graph3 extends Fragment {
    private LineChart mChart;
    float floatGdh1;
    TextView propagator, variety;
    ArrayList<Entry> gdhData1 = new ArrayList<>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.graph3_layout, container, false);

            mChart = view.findViewById(R.id.chart1);
            propagator = view.findViewById(R.id.propagatorCard);
            variety = view.findViewById(R.id.varietyCard);

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

            getPropAndVariety();

            getBlocksGDHDates();

            return view;
        }
    private void getPropAndVariety() {
        databaseReference.child("Blocks").child(String.valueOf(3)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String getVariety = snapshot.child("variety").getValue(String.class);
                String getPropagator = snapshot.child("propagator").getValue(String.class);
                variety.setText(getVariety);
                propagator.setText(getPropagator);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

        private void getBlocksGDHDates() {

            Query getGDH = databaseReference.orderByChild("date");

            getGDH.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    gdhData1.clear();

                    for (DataSnapshot dataSnapshot : snapshot.child("GDH").child(String.valueOf(3)).getChildren()) {
                        String gdhDate1 = dataSnapshot.child("date").getValue(String.class);
                        String gdh1 = dataSnapshot.child("gdh").getValue(String.class);

                        floatGdh1 = Float.parseFloat(gdh1);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date date1 = null;

                        try{
                            date1 = dateFormat.parse(gdhDate1);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        gdhData1.add(new Entry(date1.getTime(), floatGdh1));
                    }
                    LineDataSet dataSet1 = new LineDataSet(gdhData1, "Block 3 GDH");
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