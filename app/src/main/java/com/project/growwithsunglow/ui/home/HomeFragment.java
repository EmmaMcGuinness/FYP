package com.project.growwithsunglow.ui.home;

import android.content.Context;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.growwithsunglow.BlockAdapter;
import com.project.growwithsunglow.BlockModel;
import com.project.growwithsunglow.R;
import com.project.growwithsunglow.databinding.FragmentHomeBinding;


import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    Context mContext;
    private BlockAdapter blockAdapter;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userID;
    private FragmentHomeBinding binding;
    private final ArrayList<BlockModel> myDataSet = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

      /*  binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();*/
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);




        addBlocksToRecycler();

        mAuth = FirebaseAuth.getInstance();


        floatingActionButton = view.findViewById(R.id.add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBlock();

            }
        });

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
                    editBlock.setError("Event is required");
                    editBlock.requestFocus();
                    return;
                }
                if (variety.isEmpty()) {
                    editVariety.setError("Description is required");
                    editVariety.requestFocus();
                    return;
                }
                if (propagator.isEmpty()) {
                    editPropagator.setError("Deadline is required");
                    editPropagator.requestFocus();
                    return;
                }
                if (date == null) {
                    editDate.setError("Deadline is required");
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
    private void addBlocksToRecycler() {



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                myDataSet.clear();

                for (DataSnapshot events : snapshot.child("Blocks").getChildren()) {
                    final String getBlock = events.child("block").getValue(String.class);
                    final String getVariety = events.child("variety").getValue(String.class);
                    final String getPropagator = events.child("propagator").getValue(String.class);
                    final String getDate = events.child("date").getValue(String.class);

                    BlockModel blockModel = new BlockModel(getBlock, getVariety, getPropagator, getDate);
                    blockModel.setKey(events.getKey());

                    myDataSet.add(blockModel);

                }
                blockAdapter = new BlockAdapter(getActivity(), myDataSet);
                recyclerView.setAdapter(blockAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
