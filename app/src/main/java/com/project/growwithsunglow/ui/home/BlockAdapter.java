package com.project.growwithsunglow.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.growwithsunglow.R;

import java.util.ArrayList;

public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.MyViewHolder> {
    private final Context context;
    private ArrayList<BlockModel> myDataSet;


    public BlockAdapter(Context context, ArrayList<BlockModel> myDataSet) {
        this.context = context;
        this.myDataSet = myDataSet;
    }


    @Override
    public BlockAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieved_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BlockModel eventModel = myDataSet.get(position);

        holder.block.setText(eventModel.getBlock());
        holder.status.setText(eventModel.getStatus());


        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, BlockDetails.class);
                i.putExtra("Block", myDataSet.get(holder.getAdapterPosition()).getBlock());
                i.putExtra("Variety", myDataSet.get(holder.getAdapterPosition()).getVariety());
                i.putExtra("Propagator", myDataSet.get(holder.getAdapterPosition()).getPropagator());
                i.putExtra("Date", myDataSet.get(holder.getAdapterPosition()).getDate());


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
        private final TextView block, status;
        CardView card;

        public MyViewHolder(@NonNull View view){
            super(view);

            block = view.findViewById(R.id.blockCard);
            status = view.findViewById(R.id.statusCard);
            card = view.findViewById(R.id.card);

        }
    }



}
