package com.project.growwithsunglow.ui.home;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.growwithsunglow.BlockModel;
import com.project.growwithsunglow.R;

import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {
    Button updateButton, cancelButton;
    EditText updateBlock, updateVariety, updatePropagator, updatePlanted;
    String block, variety, propagator, planted, status;
    private DatePickerDialog picker;


    DatabaseReference databaseReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_block);


        updateButton = findViewById(R.id.update_button);
        cancelButton = findViewById(R.id.cancel_button);


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            updateBlock.setText(bundle.getString("Block"));
            updateVariety.setText(bundle.getString("Variety"));
            updatePropagator.setText(bundle.getString("Propagator"));
            updatePlanted.setText(bundle.getString("Planted"));
           // key = bundle.getString("Key");
            status = bundle.getString("Status");
        }
        Log.d("update",     String.valueOf(updateBlock) + "" + updateVariety + "" + updatePropagator +"" +  updatePlanted);
        //databaseReference = FirebaseDatabase.getInstance().getReference("Blocks").child(key);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                updateData();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void updateData() {

        // EditText editBlock = myView.findViewById(R.id.editTextBlock);
        Spinner sBlocks = findViewById(R.id.blockNoSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(UpdateActivity.this, R.array.blocks, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sBlocks.setAdapter(adapter);
        sBlocks.setEnabled(false);
        //  EditText editVariety = myView.findViewById(R.id.editTextVariety);
        Spinner sVariety = findViewById(R.id.varietySpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(UpdateActivity.this, R.array.varieties, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sVariety.setAdapter(adapter2);
        sVariety.setEnabled(false);
        //  EditText editPropagator = myView.findViewById(R.id.editTextPropagator);
        Spinner sPropagator = findViewById(R.id.propagatorSpinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(UpdateActivity.this, R.array.propagators, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sPropagator.setAdapter(adapter3);
        sPropagator.setEnabled(false);

        EditText editDate = findViewById(R.id.editTextDate);
        EditText editActualFIDate = findViewById(R.id.actualFIDate);

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(UpdateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        editDate.setText(dayOfMonth + "/"+ (month + 1)+ "/"+ year);

                    }
                }, year, month, day);
                picker.show();
            }
        });
        editActualFIDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(UpdateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        editActualFIDate.setText(dayOfMonth + "/"+ (month + 1)+ "/"+ year);

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


        String date = editDate.getText().toString().trim();
        String actualFI = editActualFIDate.getText().toString().trim();


                if (!sVariety.isEnabled()) {
                    Toast.makeText(UpdateActivity.this, "Variety is required", Toast.LENGTH_SHORT).show();
                    sVariety.requestFocus();
                    return;
                }
                if (!sPropagator.isEnabled()) {
                    Toast.makeText(UpdateActivity.this, "Propagator is required", Toast.LENGTH_SHORT).show();
                    sPropagator.requestFocus();
                    return;
                }
                if (date == null) {
                    editDate.setError("Date Planted is required");
                    editDate.requestFocus();
                    return;
                }

                BlockModel blockModel2 = new BlockModel(block, variety, propagator, date, status, actualFI);
                FirebaseDatabase.getInstance().getReference("Blocks").child(block)
                        .setValue(blockModel2).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(UpdateActivity.this, "Block successfully updated", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(UpdateActivity.this, "Block not successfully updated", Toast.LENGTH_LONG).show();
                                }

                            }
                        });


            }




}

