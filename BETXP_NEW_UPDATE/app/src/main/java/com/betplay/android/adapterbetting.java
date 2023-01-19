package com.betplay.dpboss_off;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class adapterbetting extends RecyclerView.Adapter<adapterbetting.ViewHolder> {

    Context context;

    ArrayList<String> number = new ArrayList<>();
    private ArrayList<String> list = new ArrayList<>();

    public adapterbetting(Context context,ArrayList<String> number) {
        this.context = context;
        this.number = number;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.betlayout, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        list.add("0");

        holder.number.setText(number.get(position));

        holder.amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().isEmpty() || s == null) {
                    list.set(position,"0");
                } else if (Integer.parseInt(s.toString()) > 10000) {
                    holder.amount.setText("10000");
                    list.set(position,10000+"");
                } else {
                    list.set(position, s.toString());
                }

                BroadcastReceiver mReceiver = new BroadcastReceiver() { @Override public void onReceive(Context context, Intent intent) { }};

                IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");
                context.registerReceiver(mReceiver, intentFilter);

                Intent i = new Intent("android.intent.action.MAIN");
                context.sendBroadcast(i);

                context.unregisterReceiver(mReceiver);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.setIsRecyclable(false);
    }

    public ArrayList<String> getNumber()
    {
        return list;
    }

    @Override
    public int getItemCount() {
        return number.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView number;
        EditText amount;

        public ViewHolder(View view) {
            super(view);
            number = view.findViewById(R.id.number);
            amount = view.findViewById(R.id.amount);


        }
    }



}
