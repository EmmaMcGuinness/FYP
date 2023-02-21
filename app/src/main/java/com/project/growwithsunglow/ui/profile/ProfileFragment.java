package com.project.growwithsunglow.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.growwithsunglow.User;
import com.project.growwithsunglow.ui.profile.ProfileViewModel ;

import com.project.growwithsunglow.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment  {

    private FragmentProfileBinding binding;
    TextView profileName, profileRole, profileEmail, profilePassword;
    Button editProfile;
    private FirebaseUser user;
    private String userID;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        profileName = binding.name;
        profileRole = binding.role;
        profileEmail = binding.email;
        profilePassword = binding.password;
        editProfile = binding.editProfile;

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
                Intent i = new Intent(getActivity(), ProfileEdit.class)
                        .putExtra("Name", profileName.getText().toString())
                        .putExtra("Role", profileRole.getText().toString())
                        .putExtra("Email", profileEmail.getText().toString())
                        .putExtra("Password", profilePassword.getText().toString());
                startActivity(i);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}