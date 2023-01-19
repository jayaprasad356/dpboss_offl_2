package com.betplay.dpboss_off;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class adaptertransaction extends RecyclerView.Adapter<adaptertransaction.ViewHolder> {

    Context context;
    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> remark = new ArrayList<>();
    ArrayList<String> amount = new ArrayList<>();
    ArrayList<String> type = new ArrayList<>();

    public adaptertransaction(Context context, ArrayList<String> date, ArrayList<String> remark, ArrayList<String> amount, ArrayList<String> type) {
        this.context = context;
        this.date = date;
        this.remark = remark;
        this.amount = amount;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.date.setText(date.get(position));
        holder.remark.setText(remark.get(position));
        holder.amount.setText(amount.get(position).replace("-",""));

        if (type.get(position).equals("1")) {
            holder.type.setText("+");
            holder.type.setTextColor(context.getResources().getColor(R.color.figma_green));
            holder.addMoney.setVisibility(View.VISIBLE);
            holder.deductMoney.setVisibility(View.GONE);
        } else {
            holder.type.setText("-");
            holder.type.setTextColor(context.getResources().getColor(R.color.figma_red));
            holder.addMoney.setVisibility(View.GONE);
            holder.deductMoney.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return date.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        CardView addMoney;
        CardView deductMoney;
        latobold remark;
        latonormal date;
        latobold amount;
        LinearLayout amountBlock;
        latobold type;

        public ViewHolder(View view) {
            super(view);

            this.addMoney = itemView.findViewById(R.id.add_money);
            this.deductMoney = itemView.findViewById(R.id.deduct_money);
            this.remark = itemView.findViewById(R.id.remark);
            this.date = itemView.findViewById(R.id.date);
            this.amount = itemView.findViewById(R.id.amount);
            this.date = itemView.findViewById(R.id.date);
            this.amount = itemView.findViewById(R.id.amount);
            this.amountBlock = itemView.findViewById(R.id.amount_block);
            this.remark = itemView.findViewById(R.id.remark);
            this.type = itemView.findViewById(R.id.type);
        }
    }


}
