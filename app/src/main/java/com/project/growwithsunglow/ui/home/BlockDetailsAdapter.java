package com.project.growwithsunglow.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.growwithsunglow.R;

import java.util.ArrayList;

public class BlockDetailsAdapter extends RecyclerView.Adapter<BlockDetailsAdapter.MyViewHolder> {
    private final Context context;
    private ArrayList<BlockDetailsModel> dataSet;


    public BlockDetailsAdapter(Context context, ArrayList<BlockDetailsModel> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    @Override
    public BlockDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BlockDetailsModel eventModel = dataSet.get(position);

        holder.block.setText(eventModel.getBlock());

       /* holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, EventActivity.class);
                i.putExtra("Event", myDataSet.get(holder.getAdapterPosition()).getEvent());
                i.putExtra("Description", myDataSet.get(holder.getAdapterPosition()).getDescription());
                i.putExtra("Location", myDataSet.get(holder.getAdapterPosition()).getLocation());
                i.putExtra("Time", myDataSet.get(holder.getAdapterPosition()).getTime());
                i.putExtra("Key", myDataSet.get(holder.getAdapterPosition()).getKey());


                context.startActivity(i);
            }
        });*/



    }


    @Override
    public int getItemCount() {

        return dataSet.size();
    }

    public void searchDataList(ArrayList<BlockDetailsModel> searchList){
        dataSet = searchList;
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
