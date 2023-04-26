package com.project.growwithsunglow.ui.home;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
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
import com.project.growwithsunglow.BlockModel;
import com.project.growwithsunglow.R;
import com.project.growwithsunglow.databinding.FragmentDashboardBinding;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    private FragmentDashboardBinding binding;
    Button table, realtime;
    String status, block, variety, propagator;
    private FloatingActionButton floatingActionButton;
    private BlockDetailsAdapter blockDetailsAdapter;
    private BlockAdapter blockAdapter;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatePickerDialog picker;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private final ArrayList<BlockModel> dataSet = new ArrayList<>();
    private ValueEventListener listener;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerview2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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

       // EditText editBlock = myView.findViewById(R.id.editTextBlock);
        Spinner sBlocks = myView.findViewById(R.id.blockNoSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.blocks, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sBlocks.setAdapter(adapter);
        sBlocks.setEnabled(false);
      //  EditText editVariety = myView.findViewById(R.id.editTextVariety);
        Spinner sVariety = myView.findViewById(R.id.varietySpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.varieties, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sVariety.setAdapter(adapter2);
        sVariety.setEnabled(false);
      //  EditText editPropagator = myView.findViewById(R.id.editTextPropagator);
        Spinner sPropagator = myView.findViewById(R.id.propagatorSpinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getActivity(), R.array.propagators, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sPropagator.setAdapter(adapter3);
        sPropagator.setEnabled(false);

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
        sVariety.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sVariety.setEnabled(true);
                variety = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sBlocks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sBlocks.setEnabled(true);
                block = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sPropagator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sPropagator.setEnabled(true);
                propagator = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                /*String block = editBlock.getText().toString().trim();
                String variety = editVariety.getText().toString().trim();
                String propagator = editPropagator.getText().toString().trim();*/
                String date = editDate.getText().toString().trim();
                Boolean blockNo = false;
                for (BlockModel blockModel : dataSet) {
                    if (blockModel.getBlock().equals(block)) {
                        blockNo = true;
                        break;
                    }
                }
                if (!sBlocks.isEnabled() ) {
                    Toast.makeText(getActivity(), "Block Number is required", Toast.LENGTH_SHORT).show();
                    sBlocks.requestFocus();
                    return;
                }
                if(blockNo.equals(true)){
                    Toast.makeText(getActivity(), "Block Number is in use", Toast.LENGTH_SHORT).show();
                    sBlocks.requestFocus();
                    return;
                }

                if (!sVariety.isEnabled()) {
                    Toast.makeText(getActivity(), "Variety is required", Toast.LENGTH_SHORT).show();
                    sVariety.requestFocus();
                    return;
                }
                if (!sPropagator.isEnabled()) {
                    Toast.makeText(getActivity(), "Propagator is required", Toast.LENGTH_SHORT).show();
                    sPropagator.requestFocus();
                    return;
                }
                if (date == null) {
                    editDate.setError("Date Planted is required");
                    editDate.requestFocus();
                    return;
                }

                status = "pre flower induction";
                BlockModel blockModel2 = new BlockModel(block, variety, propagator, date, status);
                FirebaseDatabase.getInstance().getReference("Blocks").child(block)
                        .setValue(blockModel2).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(getActivity(), "Block successfully added", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getActivity(), "Block not added", Toast.LENGTH_LONG).show();
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
                    final String getStatus = events.child("status").getValue(String.class);
                  //  final String key = events.getKey();

                    BlockModel blockModel = new BlockModel(getBlock, getVariety, getPropagator, getDate, getStatus);

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