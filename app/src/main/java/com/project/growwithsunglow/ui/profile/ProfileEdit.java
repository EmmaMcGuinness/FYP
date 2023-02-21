package com.project.growwithsunglow.ui.profile;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.growwithsunglow.MainActivity;
import com.project.growwithsunglow.R;
import com.project.growwithsunglow.User;

public class ProfileEdit extends AppCompatActivity {

    private FirebaseUser user;
    private String userID;

    EditText editName, editRole, editEmail, editPassword;
    Button saveButton;
    String nameUser, roleUser, emailUser, passwordUser;
    DatabaseReference fireDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        getSupportActionBar().setTitle("Grow With Sunglow");

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();


        fireDB = FirebaseDatabase.getInstance().getReference("Users").child(userID);

        editName = findViewById(R.id.name);
        editRole = findViewById(R.id.role);
        editEmail = findViewById(R.id.email);
        editPassword = findViewById(R.id.password);
        saveButton = findViewById(R.id.saveButton);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            editName.setText(bundle.getString("Name"));
            editRole.setText(bundle.getString("Role"));
            editEmail.setText(bundle.getString("Email"));
            editPassword.setText(bundle.getString("Password"));

        }
        fireDB = FirebaseDatabase.getInstance().getReference("Users").child(userID);





        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileEdit.this, MainActivity.class);
                startActivity(i);
                updateData();

            }
        });
    }
    public void updateData(){
        nameUser = editName.getText().toString().trim();
        roleUser = editRole.getText().toString().trim();
        emailUser = editEmail.getText().toString().trim();
        passwordUser = editPassword.getText().toString().trim();

        User user = new User(nameUser, roleUser, emailUser, passwordUser);

        fireDB.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ProfileEdit.this, "Updated", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileEdit.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
