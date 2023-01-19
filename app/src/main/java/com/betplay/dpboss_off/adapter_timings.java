package com.betplay.dpboss_off;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.iwgang.countdownview.CountdownView;

class adapter_timings extends RecyclerView.Adapter<adapter_timings.ViewHolder> {

    Context context;
    String market;
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> is_open = new ArrayList<>();
    private ArrayList<String> time = new ArrayList<>();
    private ArrayList<String> result = new ArrayList<>();

    public adapter_timings(Context context, String market, ArrayList<String> name, ArrayList<String> is_open, ArrayList<String> time, ArrayList<String> result) {
        this.context = context;
        this.market = market;
        this.name = name;
        this.is_open = is_open;
        this.time = time;
        this.result = result;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_timing_layout, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        holder.name.setText(time.get(position));
        holder.result.setText(result.get(position));

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        String givenDateString = formattedDate+" "+time.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
        try {
            Date mDate = sdf.parse(givenDateString);
            long timeInMilliseconds = mDate.getTime();
            //System.out.println(timeInMilliseconds);
            holder.timer.start(timeInMilliseconds-System.currentTimeMillis());
            //System.out.println("started");
            holder.timer.setOnCountdownIntervalListener(1, new CountdownView.OnCountdownIntervalListener() {
                @Override
                public void onInterval(CountdownView cv, long remainTime) {

                    //System.out.println(name.get(position)+" - "+cv.getRemainTime());
                }
            });
            // System.out.println(timeInMilliseconds-System.currentTimeMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (is_open.get(position).equals("1")) {
            holder.status.setTextColor(context.getResources().getColor(R.color.md_green_800));
            holder.status.setText("OPEN");

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,starline_games.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("market",market).putExtra("timing",time.get(position)));
                }
            });

        } else {
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Market is already closed", Toast.LENGTH_SHORT).show();
                }
            });

            holder.status.setTextColor(context.getResources().getColor(R.color.md_red_600));
            holder.status.setText("CLOSED");
        }

        holder.setIsRecyclable(false);
    }


    @Override
    public int getItemCount() {
        return time.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,result,status;
        LinearLayout layout;
        CountdownView timer;

        public ViewHolder(View view) {
            super(view);
            layout = view.findViewById(R.id.layout);
            name = view.findViewById(R.id.name);
            result = view.findViewById(R.id.result);
            status = view.findViewById(R.id.status);
            timer = view.findViewById(R.id.timer);


        }
    }



}
