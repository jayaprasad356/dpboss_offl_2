package com.betplay.dpboss_off;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class adapter_starline_results extends RecyclerView.Adapter<adapter_starline_results.ViewHolder> {

    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> result = new ArrayList<>();

    public adapter_starline_results(ArrayList<String> name,ArrayList<String> result) {
        this.name = name;
        this.result = result;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.starline_result_layout, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        holder.name.setText(name.get(position));
        holder.result.setText(result.get(position));

    }


    @Override
    public int getItemCount() {
        return name.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,result;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            result = view.findViewById(R.id.result);
        }
    }



}
