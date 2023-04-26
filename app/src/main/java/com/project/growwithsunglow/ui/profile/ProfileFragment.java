package com.project.growwithsunglow.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import com.project.growwithsunglow.Login;
import com.project.growwithsunglow.R;
import com.project.growwithsunglow.User;

import com.project.growwithsunglow.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment  {

    private FragmentProfileBinding binding;
    TextView profileName, profileRole, profileEmail, profilePassword;
    private FirebaseUser user;
    private String userID;
    EditText editName, editRole, editEmail, editPassword;
    Button editProfile, saveButton, cancelButton, logout;
    String nameUser, roleUser, emailUser, passwordUser;
    DatabaseReference fireDB;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileName = view.findViewById(R.id.name);
        profileRole = view.findViewById(R.id.role);
        profileEmail = view.findViewById(R.id.email);
        profilePassword = view.findViewById(R.id.password);
        editProfile = view.findViewById(R.id.editProfile);
        logout = view.findViewById(R.id.logout);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        fireDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userObj = snapshot.getValue(User.class);
                profileName.setText(userObj.getName());
                profileRole.setText(userObj.getRole());
                profileEmail.setText(userObj.getEmail());
                profilePassword.setText(userObj.getPassword());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              editProfile();
            }

        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
        return view;
    }
    private void editProfile() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myView = inflater.inflate(R.layout.activity_profile_edit, null);
        myDialog.setView(myView);

        AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);
        dialog.show();

        fireDB = FirebaseDatabase.getInstance().getReference("Users").child(userID);

        editName = myView.findViewById(R.id.name);
        editRole =  myView.findViewById(R.id.role);
        editEmail =  myView.findViewById(R.id.email);
        editPassword =  myView.findViewById(R.id.password);
        saveButton =  myView.findViewById(R.id.saveButton);
        cancelButton = myView.findViewById(R.id.cancelButton);

        editName.setText(profileName.getText().toString());
        editRole.setText(profileRole.getText().toString());
        editEmail.setText(profileEmail.getText().toString());
        editPassword.setText(profilePassword.getText().toString());



        nameUser = editName.getText().toString().trim();
        roleUser = editRole.getText().toString().trim();
        emailUser = editEmail.getText().toString().trim();
        passwordUser = editPassword.getText().toString().trim();

        User user = new User(nameUser, roleUser, emailUser, passwordUser);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fireDB.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}