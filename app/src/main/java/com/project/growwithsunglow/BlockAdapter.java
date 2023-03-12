package com.project.growwithsunglow;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.growwithsunglow.ui.home.BlockDetails;

import java.util.ArrayList;

public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.MyViewHolder> {
    private final Context context;
    private ArrayList<BlockModel> myDataSet;


    //constructor
    public BlockAdapter(Context context, ArrayList<BlockModel> myDataSet) {
        this.context = context;
        this.myDataSet = myDataSet;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public BlockAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieved_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BlockModel eventModel = myDataSet.get(position);

        holder.block.setText(eventModel.getBlock());


        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, BlockDetails.class);
                i.putExtra("Block", myDataSet.get(holder.getAdapterPosition()).getBlock());
                i.putExtra("Variety", myDataSet.get(holder.getAdapterPosition()).getVariety());
                i.putExtra("Propagator", myDataSet.get(holder.getAdapterPosition()).getPropagator());
                i.putExtra("Date", myDataSet.get(holder.getAdapterPosition()).getDate());
                i.putExtra("Key", myDataSet.get(holder.getAdapterPosition()).getKey());


               context.startActivity(i);
            }
        });



    }


    @Override
    public int getItemCount() {

        return myDataSet.size();
    }

    public void searchDataList(ArrayList<BlockModel> searchList){
        myDataSet = searchList;
        notifyDataSetChanged();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView block;
        CardView card;

        public MyViewHolder(@NonNull View view){
            super(view);

            block = view.findViewById(R.id.blockCard);
            card = view.findViewById(R.id.card);
        }
    }



}
