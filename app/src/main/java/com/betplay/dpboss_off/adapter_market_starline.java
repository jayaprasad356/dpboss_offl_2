package com.betplay.dpboss_off;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class adapter_market_starline extends RecyclerView.Adapter<adapter_market_starline.ViewHolder> {

    Context context;
    ArrayList<String> name = new ArrayList<>();

    public adapter_market_starline(Context context, ArrayList<String> name) {
        this.context = context;
        this.name = name;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_starline_layout, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        holder.name.setText(name.get(position));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, starline_timings.class).putExtra("market", name.get(position)));
            }
        });

        holder.setIsRecyclable(false);
    }


    @Override
    public int getItemCount() {
        return name.size();
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
