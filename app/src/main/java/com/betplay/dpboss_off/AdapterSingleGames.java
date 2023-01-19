package com.betplay.dpboss_off;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class AdapterSingleGames extends RecyclerView.Adapter<AdapterSingleGames.ViewHolder> {

    Context context;
    ArrayList<String> digit = new ArrayList<>();
    ArrayList<String> amount = new ArrayList<>();
    ArrayList<String> game = new ArrayList<>();

    public AdapterSingleGames(Context context, ArrayList<String> digit, ArrayList<String> amount, ArrayList<String> game) {
        this.context = context;
        this.digit = digit;
        this.amount = amount;
        this.game = game;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_bets, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.date.setText(digit.get(position));
        holder.remark.setText(game.get(position));
        holder.amount.setText(amount.get(position));

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BroadcastReceiver mReceiver = new BroadcastReceiver() { @Override public void onReceive(Context context, Intent intent) { }};

                IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");
                context.registerReceiver(mReceiver, intentFilter);

                Intent i = new Intent("android.intent.action.MAIN");
                i.putExtra("number",position+"");
                context.sendBroadcast(i);

                context.unregisterReceiver(mReceiver);
            }
        });

    }

    @Override
    public int getItemCount() {
        return digit.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView date,amount,remark;
        ImageView delete;

        public ViewHolder(View view) {
            super(view);

            date = view.findViewById(R.id.date);
            amount = view.findViewById(R.id.amount);
            remark = view.findViewById(R.id.remark);
            delete = view.findViewById(R.id.delete);
        }
    }



}
