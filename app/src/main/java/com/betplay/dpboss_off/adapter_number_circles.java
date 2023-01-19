package com.betplay.dpboss_off;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class adapter_number_circles extends RecyclerView.Adapter<adapter_number_circles.ViewHolder> {

    Context context;
    ArrayList<String> name = new ArrayList<>();

    public adapter_number_circles(Context context, ArrayList<String> name) {
        this.context = context;
        this.name = name;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.number_circle_item, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.name.setText(name.get(position));


    }


    @Override
    public int getItemCount() {
        return name.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);


        }
    }



}
