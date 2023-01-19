package com.betplay.dpboss_off;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class played extends AppCompatActivity {

    protected RecyclerView recyclerview;
    ViewDialog progressDialog;
    String url;
    private TextInputEditText fromDate;
    private TextInputEditText toDate;
    private latobold submit;
    private RelativeLayout dateFilter;
    private CardView back;
    private ImageView filter;
    private CardView toolbar;
    final Calendar myCalendar = Calendar.getInstance();
    String myFormat = "dd/MM/yy";
    private latobold description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_played);
        initViews();
        initView();
        url = constant.prefix + "games_news.php";
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabelFrom();
        };
        fromDate.setOnClickListener(view -> new DatePickerDialog(played.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        DatePickerDialog.OnDateSetListener date2 = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabelTo();
        };

        toDate.setOnClickListener(view -> new DatePickerDialog(played.this, date2, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show());
        apicall("", "");
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFilter.setVisibility(View.VISIBLE);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apicall(fromDate.getText().toString(), toDate.getText().toString());
                dateFilter.setVisibility(View.GONE);
                description.setText(fromDate.getText().toString() + " - " + toDate.getText().toString());

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (dateFilter.getVisibility() == View.VISIBLE) {
            return;
        }
        super.onBackPressed();
    }    private void updateLabelFrom() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        fromDate.setText(dateFormat.format(myCalendar.getTime()));
    }



    private void updateLabelTo() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        toDate.setText(dateFormat.format(myCalendar.getTime()));
    }


    private void apicall(String fromDate, String toDate) {

        progressDialog = new ViewDialog(played.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hideDialog();
                        Log.e("my_tag", response);
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);

                            ArrayList<String> market = new ArrayList<>();
                            ArrayList<String> date = new ArrayList<>();
                            ArrayList<String> total = new ArrayList<>();
                            ArrayList<JSONArray> json = new ArrayList<>();

                            ArrayList<ArrayList<String>> game = new ArrayList<>();
                            ArrayList<ArrayList<String>> bet = new ArrayList<>();
                            ArrayList<ArrayList<String>> amount = new ArrayList<>();

                            if (jsonObject1.has("dates")){
                                JSONArray jsonArray = jsonObject1.getJSONArray("dates");
                                for (int a = 0; jsonArray.length() > a; a++) {
                                    String avl_date = jsonArray.getString(a);


                                    JSONObject jsonObject = jsonObject1.getJSONObject(avl_date);

                                    JSONArray jsonArray1 = jsonObject.getJSONArray("markets");

                                    for (int z = 0; z < jsonArray1.length(); z++) {

                                        date.add(avl_date);
                                        String avl_mrkt = jsonArray1.getString(z);

                                        market.add(avl_mrkt.replace("OPEN", "").replace("CLOSE", "").replace("_", ""));

                                        JSONArray jsonArray2 = jsonObject.getJSONArray(avl_mrkt);

                                        int totalAmount = 0;


                                        ArrayList<String> _game = new ArrayList<>();
                                        ArrayList<String> _bet = new ArrayList<>();
                                        ArrayList<String> _amount = new ArrayList<>();

                                        for (int x = 0; x < jsonArray2.length(); x++) {

                                            JSONObject jsonObject2 = jsonArray2.getJSONObject(x);

                                            totalAmount += Integer.parseInt(jsonObject2.getString("amount"));

                                            if (jsonObject2.getString("game").equals("single")) {
                                                if (jsonObject2.getString("bazar").contains("OPEN")) {
                                                    _game.add("Open");
                                                } else {
                                                    _game.add("Close");
                                                }
                                            } else {
                                                _game.add(String.valueOf(jsonObject2.getString("game").charAt(0)).toUpperCase(Locale.ROOT) + jsonObject2.getString("game").substring(1, jsonObject2.getString("game").length()));
                                            }

                                            _bet.add(jsonObject2.getString("number"));
                                            _amount.add(jsonObject2.getString("amount"));

                                        }

                                        game.add(_game);
                                        bet.add(_bet);
                                        amount.add(_amount);

                                        json.add(jsonArray2);
                                        total.add(totalAmount + "");


                                    }

                                }
                            }


                            Log.e("date", date.toString());
                            Log.e("market", market.toString());

                            adapter_my_games rc = new adapter_my_games(played.this, market, date, total, json, game, bet, amount);
                            recyclerview.setLayoutManager(new GridLayoutManager(played.this, 1));
                            recyclerview.setAdapter(rc);

                        } catch (JSONException e) {
                            ArrayList<String> market = new ArrayList<>();
                            ArrayList<String> date = new ArrayList<>();
                            ArrayList<String> total = new ArrayList<>();
                            ArrayList<JSONArray> json = new ArrayList<>();

                            ArrayList<ArrayList<String>> game = new ArrayList<>();
                            ArrayList<ArrayList<String>> bet = new ArrayList<>();
                            ArrayList<ArrayList<String>> amount = new ArrayList<>();

                            adapter_my_games rc = new adapter_my_games(played.this, market, date, total, json, game, bet, amount);
                            recyclerview.setLayoutManager(new GridLayoutManager(played.this, 1));
                            recyclerview.setAdapter(rc);

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
                        Toast.makeText(played.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));

                if (!fromDate.equals("")) {
                    params.put("from_date", fromDate);
                }
                if (!toDate.equals("")) {
                    params.put("to_date", toDate);
                }

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    private void initView() {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
    }

    private void initViews() {
        fromDate = findViewById(R.id.from_date);
        toDate = findViewById(R.id.to_date);
        submit = findViewById(R.id.submit);
        dateFilter = findViewById(R.id.date_filter);
        back = findViewById(R.id.back);
        filter = findViewById(R.id.filter);
        toolbar = findViewById(R.id.toolbar);
        recyclerview = findViewById(R.id.recyclerview);
        description = findViewById(R.id.description);
    }
}
