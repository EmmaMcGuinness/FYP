package com.project.growwithsunglow.ui.home;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.database.FirebaseDatabase;
import com.project.growwithsunglow.R;

import java.util.Calendar;

public class UpdateBlock extends AppCompatActivity {
    Button updateButton, cancelButton;
    EditText editDate;
    String variety, propagator, status, updateB, updateV, updateProp, updatePlant;
    private DatePickerDialog picker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_block);

       editDate = findViewById(R.id.editTextDate);

        updateButton = findViewById(R.id.update_button);
        cancelButton = findViewById(R.id.cancel_button);

        Intent intent = getIntent();
        updateB= intent.getStringExtra("Block");
        updateV= intent.getStringExtra("Variety");
        updateProp= intent.getStringExtra("Propagator");
        updatePlant= intent.getStringExtra("Planted");
        status= intent.getStringExtra("Status");

        Log.d("Edit", updateB + " " + updateV + " " + updateProp + " " + status + " "+ updatePlant);

        editDate.setText(updatePlant);


        updateData();


    }
    private void updateData() {

        Spinner sVariety = findViewById(R.id.varietySpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(UpdateBlock.this, R.array.varieties, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sVariety.setAdapter(adapter2);
        sVariety.setEnabled(false);

        Spinner sPropagator = findViewById(R.id.propagatorSpinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(UpdateBlock.this, R.array.propagators, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sPropagator.setAdapter(adapter3);
        sPropagator.setEnabled(false);


        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(UpdateBlock.this, new DatePickerDialog.OnDateSetListener() {
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


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = editDate.getText().toString().trim();

                BlockModel blockModel2 = new BlockModel(updateB, variety, propagator, date, status);
                if (!sVariety.isEnabled()) {
                    Toast.makeText(UpdateBlock.this, "Variety is required", Toast.LENGTH_SHORT).show();
                    sVariety.requestFocus();
                    return;
                }
                if (!sPropagator.isEnabled()) {
                    Toast.makeText(UpdateBlock.this, "Propagator is required", Toast.LENGTH_SHORT).show();
                    sPropagator.requestFocus();
                    return;
                }
                if (date == null) {
                    editDate.setError("Date Planted is required");
                    editDate.requestFocus();
                    return;
                }
                FirebaseDatabase.getInstance().getReference("Blocks").child(updateB)
                        .setValue(blockModel2).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(UpdateBlock.this, "Block successfully updated", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(UpdateBlock.this, "Block not successfully updated", Toast.LENGTH_LONG).show();
                                }

                            }
                        });

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }




}

