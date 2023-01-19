package com.betplay.dpboss_off;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class adapter_transaction_new extends RecyclerView.Adapter<adapter_transaction_new.ViewHolder> {

    Context context;
    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> remark = new ArrayList<>();
    ArrayList<String> amount = new ArrayList<>();
    ArrayList<String> type = new ArrayList<>();

    public adapter_transaction_new(Context context, ArrayList<String> date, ArrayList<String> remark, ArrayList<String> amount, ArrayList<String> type) {
        this.context = context;
        this.date = date;
        this.remark = remark;
        this.amount = amount;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_new, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.date.setText(date.get(position));

        if (type.get(position).equals("1")) {
            holder.received.setText(amount.get(position));
        } else {
            holder.paid.setText(amount.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return date.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView date,received,paid;

        public ViewHolder(View view) {
            super(view);

            this.date = itemView.findViewById(R.id.date);
            this.received = itemView.findViewById(R.id.received);
            this.paid = itemView.findViewById(R.id.paid);
        }
    }


}
