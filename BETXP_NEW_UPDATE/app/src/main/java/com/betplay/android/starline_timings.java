package com.betplay.dpboss_off;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class starline_timings extends AppCompatActivity {

    RecyclerView recyclerview;
    ViewDialog progressDialog;
    String url2;
    String market = "";
    TextView title, rate;
    private ImageView back;
    private CardView toolbar;
    private latonormal bidHistory;
    private latonormal resultHistory;
    private latonormal chart;
    private ImageView back2;
    private RecyclerView recyclerview2;
    private LinearLayout resultHistoryPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starline_timings);
        initViews();
        market = getIntent().getStringExtra("market");
        title.setText(market);
        url2 = constant.prefix + getResources().getString(R.string.starline_timings);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultHistoryPop.setVisibility(View.GONE);
            }
        });

        resultHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultHistoryPop.setVisibility(View.VISIBLE);
            }
        });

        bidHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(starline_timings.this, played.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(starline_timings.this, charts.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("href",constant.prefix+"chart/getChart.php?market="+market.replace(" ","%20")));
            }
        });

        apicall2();
    }

    @Override
    public void onBackPressed() {
        if (resultHistoryPop.getVisibility() == View.VISIBLE){
            resultHistoryPop.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }

    private void apicall2() {

        progressDialog = new ViewDialog(starline_timings.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new StringRequest(Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);

                            JSONArray jsonArray = jsonObject1.getJSONArray("data");

                            rate.setText("Single Digit : " + jsonObject1.getString("single") + ", Single Pana : " + jsonObject1.getString("singlepatti") + ", Double Pana : " + jsonObject1.getString("doublepatti") + ", Triple Pana : " + jsonObject1.getString("triplepatti"));

                            ArrayList<String> name = new ArrayList<>();
                            ArrayList<String> is_open = new ArrayList<>();
                            ArrayList<String> time = new ArrayList<>();
                            ArrayList<String> result = new ArrayList<>();

                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(a);

                                name.add(jsonObject.getString("name"));
                                is_open.add(jsonObject.getString("is_open"));
                                time.add(jsonObject.getString("time"));
                                result.add(jsonObject.getString("result"));
                            }

                            adapter_timings rc = new adapter_timings(starline_timings.this, market, name, is_open, time, result);
                            recyclerview.setLayoutManager(new GridLayoutManager(starline_timings.this, 1));
                            recyclerview.setAdapter(rc);


                            adapter_starline_results rc2 = new adapter_starline_results(time, result);
                            recyclerview2.setLayoutManager(new GridLayoutManager(starline_timings.this, 1));
                            recyclerview2.setAdapter(rc2);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.hideDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        progressDialog.hideDialog();
                        Toast.makeText(starline_timings.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("session", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));
                params.put("market", market);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    private void initViews() {
        recyclerview = findViewById(R.id.recyclerview);
        title = findViewById(R.id.title);
        rate = findViewById(R.id.rate);
        back = findViewById(R.id.back);
        toolbar = findViewById(R.id.toolbar);
        bidHistory = findViewById(R.id.bid_history);
        resultHistory = findViewById(R.id.result_history);
        chart = findViewById(R.id.chart);
        back2 = findViewById(R.id.back2);
        recyclerview2 = findViewById(R.id.recyclerview2);
        resultHistory = findViewById(R.id.result_history);
        resultHistoryPop = findViewById(R.id.result_history_pop);
    }
}
