package com.betplay.dpboss_off;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class adapter_market extends RecyclerView.Adapter<adapter_market.ViewHolder> {

    Context context;
    String game;
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> is_open = new ArrayList<>();
    private ArrayList<String> time = new ArrayList<>();
    private ArrayList<String> numbers = new ArrayList<>();

    public adapter_market(Context context,String game, ArrayList<String> name, ArrayList<String> is_open, ArrayList<String> time,ArrayList<String> numbers) {
        this.context = context;
        this.game = game;
        this.name = name;
        this.is_open = is_open;
        this.time = time;
        this.numbers = numbers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_layout, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        holder.name.setText(name.get(position));
        holder.time.setText(time.get(position));

        if (is_open.get(position).equals("1")) {
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.md_green_800));

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (game) {
                        case "halfsangam":
                            context.startActivity(new Intent(context, halfsangam.class)
                                    .putExtra("market", name.get(position).replace(" ", "_"))
                                    .putExtra("list", numbers)
                                    .putExtra("game", game)
                                    .putExtra("open_av", "1")
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            break;
                        case "fullsangam":
                            context.startActivity(new Intent(context, fullsangam.class)
                                    .putExtra("market", name.get(position)
                                            .replace(" ", "_"))
                                    .putExtra("list", numbers)
                                    .putExtra("open_av", "1")
                                    .putExtra("game", game)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            break;
                        case "crossing":
                            context.startActivity(new Intent(context, crossing.class)
                                    .putExtra("market", name.get(position)
                                            .replace(" ", "_"))
                                    .putExtra("list", numbers)
                                    .putExtra("game", game)
                                    .putExtra("open_av", "1")
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            break;
                        case "red_bracket":
                            context.startActivity(new Intent(context, RedBracket.class)
                                    .putExtra("market", name.get(position)
                                            .replace(" ", "_"))
                                    .putExtra("list", numbers)
                                    .putExtra("game", game)
                                    .putExtra("open_av", "1")
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            break;
                        default:
                            context.startActivity(new Intent(context, betting.class)
                                    .putExtra("market", name.get(position)
                                            .replace(" ", "_"))
                                    .putExtra("list", numbers)
                                    .putExtra("open_av", "1")
                                    .putExtra("game", game)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            break;
                    }
                }
            });

        } else {
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle("Market Close")
                            .setMessage("This game is already closed for this market")
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                }
            });

            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.md_red_600));
        }


        holder.setIsRecyclable(false);
    }


    @Override
    public int getItemCount() {
        return time.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,time;
        RelativeLayout layout;

        public ViewHolder(View view) {
            super(view);
            layout = view.findViewById(R.id.layout);
            name = view.findViewById(R.id.name);
            time = view.findViewById(R.id.time);


        }
    }



}
