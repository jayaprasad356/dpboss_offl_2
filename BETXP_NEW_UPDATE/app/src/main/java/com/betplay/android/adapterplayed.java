package com.betplay.dpboss_off;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class adapterplayed extends RecyclerView.Adapter<adapterplayed.ViewHolder> {

    Context context;

    ArrayList<String> date = new ArrayList<>();
    private ArrayList<String> bazar = new ArrayList<>();
    ArrayList<String> amount = new ArrayList<>();
    private ArrayList<String> bet = new ArrayList<>();

    public adapterplayed(Context context,ArrayList<String> date,ArrayList<String> bazar,ArrayList<String> amount,ArrayList<String> bet) {
        this.context = context;
        this.date = date;
        this.bazar = bazar;
        this.amount = amount;
        this.bet = bet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.played, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.date.setText(date.get(position));
        holder.bazar.setText(bazar.get(position));
        holder.amount.setText(amount.get(position)+" 📀");
        holder.bet.setText(bet.get(position));


    }

    @Override
    public int getItemCount() {
        return date.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView date,bazar,amount,bet;

        public ViewHolder(View view) {
            super(view);

            date = view.findViewById(R.id.date);
            bazar = view.findViewById(R.id.bazar);
            amount = view.findViewById(R.id.amount);
            bet = view.findViewById(R.id.bet);


        }
    }



}
