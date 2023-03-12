package com.project.growwithsunglow.ui.home;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.growwithsunglow.BlockAdapter;
import com.project.growwithsunglow.BlockDetailsAdapter;
import com.project.growwithsunglow.BlockDetailsModel;
import com.project.growwithsunglow.BlockModel;
import com.project.growwithsunglow.R;
import com.project.growwithsunglow.databinding.FragmentDashboardBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    private FragmentDashboardBinding binding;
    Button table, realtime;
    private FloatingActionButton floatingActionButton;
    private BlockDetailsAdapter blockDetailsAdapter;
    private BlockAdapter blockAdapter;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatePickerDialog picker;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private final ArrayList<BlockModel> dataSet = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel dashboardViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

       /* binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();*/
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview2);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        addBlocksToRecycler();

        floatingActionButton = view.findViewById(R.id.add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBlock();

            }
        });

        return view;
    }

    private void addBlock() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myView = inflater.inflate(R.layout.add_block, null);
        myDialog.setView(myView);

        AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);
        dialog.show();

        EditText editBlock = myView.findViewById(R.id.editTextBlock);
        EditText editVariety = myView.findViewById(R.id.editTextVariety);
        EditText editPropagator = myView.findViewById(R.id.editTextPropagator);
        EditText editDate = myView.findViewById(R.id.editTextDate);

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        editDate.setText(dayOfMonth + "/"+ (month + 1)+ "/"+ year);

                    }
                }, year, month, day);
                picker.show();
            }
        });
        Button save = myView.findViewById(R.id.save_button);
        Button cancel = myView.findViewById(R.id.cancel_button);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String block = editBlock.getText().toString().trim();
                String variety = editVariety.getText().toString().trim();
                String propagator = editPropagator.getText().toString().trim();
                String date = editDate.getText().toString().trim();

                if (block.isEmpty()) {
                    editBlock.setError("Block Number is required");
                    editBlock.requestFocus();
                    return;
                }
                if (variety.isEmpty()) {
                    editVariety.setError("Variety is required");
                    editVariety.requestFocus();
                    return;
                }
                if (propagator.isEmpty()) {
                    editPropagator.setError("Propagator is required");
                    editPropagator.requestFocus();
                    return;
                }
                if (date == null) {
                    editDate.setError("Date Planted is required");
                    editDate.requestFocus();
                    return;
                }


                BlockModel blockModel = new BlockModel(block, variety, propagator, date);
                FirebaseDatabase.getInstance().getReference("Blocks").push()
                        .setValue(blockModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(getActivity(), "Event successfully added", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getActivity(), "Event not added", Toast.LENGTH_LONG).show();
                                }

                            }
                        });


            }

        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void addBlocksToRecycler() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                dataSet.clear();


                for (DataSnapshot events : snapshot.child("Blocks").getChildren()) {
                    final String getBlock = events.child("block").getValue(String.class);
                    final String getVariety = events.child("variety").getValue(String.class);
                    final String getPropagator = events.child("propagator").getValue(String.class);
                    final String getDate = events.child("date").getValue(String.class);

                    BlockModel blockModel = new BlockModel(getBlock, getVariety, getPropagator, getDate);
                    blockModel.setKey(events.getKey());

                    dataSet.add(blockModel);

                }
                blockAdapter = new BlockAdapter(getActivity(), dataSet);
                recyclerView.setAdapter(blockAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}