package com.betplay.dpboss_off;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

import java.util.ArrayList;

class adapter_my_games extends RecyclerView.Adapter<adapter_my_games.ViewHolder> {

    Context context;

    ArrayList<String> market = new ArrayList<>();
    private ArrayList<String> date = new ArrayList<>();
    ArrayList<String> total = new ArrayList<>();
    private ArrayList<JSONArray> json = new ArrayList<>();


    ArrayList<ArrayList<String>> game = new ArrayList<>();
    ArrayList<ArrayList<String>> bet = new ArrayList<>();
    ArrayList<ArrayList<String>> amount = new ArrayList<>();

    public adapter_my_games(Context context, ArrayList<String> market, ArrayList<String> date, ArrayList<String> total, ArrayList<JSONArray> json,ArrayList<ArrayList<String>> game,ArrayList<ArrayList<String>> bet,ArrayList<ArrayList<String>> amount) {
        this.context = context;
        this.date = date;
        this.market = market;
        this.total = total;
        this.json = json;
        this.game = game;
        this.bet = bet;
        this.amount = amount;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.played_new, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.date.setText(date.get(position));
        holder.bazar.setText(market.get(position));
        holder.amount.setText("Total Game Amount - "+total.get(position));


        adapter_played_bets rc = new adapter_played_bets(context,game.get(position),bet.get(position),amount.get(position));
        holder.bet.setLayoutManager(new GridLayoutManager(context, 1));
        holder.bet.setAdapter(rc);


    }

    @Override
    public int getItemCount() {
        return date.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView date,bazar,amount;
        RecyclerView bet;

        public ViewHolder(View view) {
            super(view);

            date = view.findViewById(R.id.date);
            bazar = view.findViewById(R.id.market);
            amount = view.findViewById(R.id.total);
            bet = view.findViewById(R.id.bets);


        }
    }



}
