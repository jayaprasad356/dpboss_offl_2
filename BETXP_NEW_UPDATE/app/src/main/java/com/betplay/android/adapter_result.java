package com.betplay.dpboss_off;


import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class adapter_result extends RecyclerView.Adapter<adapter_result.ViewHolder> {

    Context context;
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> result = new ArrayList<>();

    ArrayList<String> is_open = new ArrayList<>();
    ArrayList<String> open_time = new ArrayList<>();
    ArrayList<String> close_time = new ArrayList<>();
    private ArrayList<String> open_av = new ArrayList<>();

    public adapter_result(Context context,  ArrayList<String> name, ArrayList<String> result, ArrayList<String> is_open, ArrayList<String> open_time, ArrayList<String> close_time,ArrayList<String> open_av) {
        this.context = context;
        this.name = name;
        this.result = result;
        this.is_open = is_open;
        this.open_time = open_time;
        this.close_time = close_time;
        this.open_av = open_av;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_layout, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        if (context.getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("verify", "0").equals("1")) {
            holder.play_game.setVisibility(View.VISIBLE);
//            holder.info.setVisibility(View.VISIBLE);
//            holder.top.setVisibility(View.VISIBLE);
        } else {
            holder.play_game.setVisibility(View.GONE);
//            holder.info.setVisibility(View.GONE);
//            holder.top.setVisibility(View.GONE);
        }

        holder.time_.setText("("+open_time.get(position)+" - "+close_time.get(position)+")");

        holder.name.setText(name.get(position));
        holder.result.setText(result.get(position));

        holder.info.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.info.setMarqueeRepeatLimit(-1);
        holder.info.setSingleLine(true);
        holder.info.setSelected(true);

        holder.top.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.top.setMarqueeRepeatLimit(-1);
        holder.top.setSingleLine(true);
        holder.top.setSelected(true);

        holder.top.setText("Open-Bids-"+open_time.get(position)+" | Close-Bids-"+close_time.get(position));

        holder.time_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                LayoutInflater factory = LayoutInflater.from(context);
                View market_time = factory.inflate(R.layout.market_time, null);
                TextView close = market_time.findViewById(R.id.close);
                TextView open_time_view = market_time.findViewById(R.id.open_time);
                TextView close_time_view = market_time.findViewById(R.id.close_time);
                builder1.setView(market_time);
                builder1.setCancelable(true);
                AlertDialog alert11 = builder1.create();
                alert11.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                open_time_view.setText(open_time.get(position));
                close_time_view.setText(close_time.get(position));

                alert11.show();

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert11.dismiss();
                    }
                });

            }
        });

        if (open_av.get(position).equals("1") || is_open.get(position).equals("1")) {
            holder.info.setText("Betting is Running Now");
            holder.info.setTextColor(context.getResources().getColor(R.color.md_green_800));
            holder.play_game.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,games.class)
                            .putExtra("market",name.get(position))
                            .putExtra("is_open",is_open.get(position))
                            .putExtra("is_close",open_av.get(position))
                    );
                }
            });
        } else {
            holder.info.setText("Betting closed for today");
            holder.info.setTextColor(context.getResources().getColor(R.color.md_red_600));

            holder.play_game.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new androidx.appcompat.app.AlertDialog.Builder(context)
                            .setTitle("Market Closed")
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,result, info,top,time_;
        ImageView time_info;
        LinearLayout play_game;
        CardView layout;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            result = view.findViewById(R.id.result);
            info = view.findViewById(R.id.info);
            time_info = view.findViewById(R.id.time_info);
            play_game = view.findViewById(R.id.play_game);
            layout = view.findViewById(R.id.layout);
            top = view.findViewById(R.id.top);
            time_ = view.findViewById(R.id.time_);
        }
    }



}
