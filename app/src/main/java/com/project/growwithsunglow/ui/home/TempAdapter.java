package com.project.growwithsunglow.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.growwithsunglow.GdhModel;
import com.project.growwithsunglow.R;

import java.util.ArrayList;

public class TempAdapter extends RecyclerView.Adapter<TempAdapter.MyViewHolder> {
    private final Context context;
    private ArrayList<AvgTemp> myDataSet;
    private ArrayList<GdhModel> dailyGdhList;


    public TempAdapter(Context context, ArrayList<GdhModel> dailyGdhList){
        this.context = context;
        this.dailyGdhList = dailyGdhList;

    }


    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GdhModel gdhModel = dailyGdhList.get(position);

        holder.date.setText(gdhModel.getDate());
        holder.gdh.setText(gdhModel.getGdh());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    Intent i = new Intent(context, EventActivity.class);
                //    i.putExtra("Event", myDataSet.get(holder.getAdapterPosition()).getEvent());
                //    i.putExtra("Description", myDataSet.get(holder.getAdapterPosition()).getDescription());
                //    i.putExtra("Location", myDataSet.get(holder.getAdapterPosition()).getLocation());
                //    i.putExtra("Time", myDataSet.get(holder.getAdapterPosition()).getTime());
                //    i.putExtra("Key", myDataSet.get(holder.getAdapterPosition()).getKey());


                //   context.startActivity(i);
            }
        });



    }


    @Override
    public int getItemCount() {

        return dailyGdhList.size();
    }

    public void searchDataList(ArrayList<GdhModel> searchList){
        dailyGdhList = searchList;
        notifyDataSetChanged();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView date, gdh;
        CardView card;

        public MyViewHolder(@NonNull View view){
            super(view);

            date = view.findViewById(R.id.date);
            gdh = view.findViewById(R.id.gdh);
            card = view.findViewById(R.id.card);
        }
    }



}