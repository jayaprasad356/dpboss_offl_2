package com.betplay.dpboss_off;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class adapter_played_bets extends RecyclerView.Adapter<adapter_played_bets.ViewHolder> {

    Context context;
    ArrayList<String> game = new ArrayList<>();
    ArrayList<String> bet = new ArrayList<>();
    ArrayList<String> amount = new ArrayList<>();

    public adapter_played_bets(Context context, ArrayList<String> game, ArrayList<String> bet, ArrayList<String> amount) {
        this.context = context;
        this.game = game;
        this.bet = bet;
        this.amount = amount;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.played_bets, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.game.setText(game.get(position));
        holder.bet.setText(bet.get(position));
        holder.amount.setText(amount.get(position));

    }

    @Override
    public int getItemCount() {
        return bet.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView game,amount,bet;

        public ViewHolder(View view) {
            super(view);

            bet = view.findViewById(R.id.bet);
            amount = view.findViewById(R.id.amount);
            game = view.findViewById(R.id.game);
        }
    }



}
