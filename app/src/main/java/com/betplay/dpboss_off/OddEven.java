package com.betplay.dpboss_off;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OddEven extends AppCompatActivity {


    private CardView back;
    private EditText amount;
    private latobold submit;
    TextView open_game,close_game;
    TextView title,balance,screenTitle;
    LinearLayout type_container;
    LinearLayout odd_row,even_row;
    TextView odd,even;

    String open_av = "0",timing = "";

    SharedPreferences prefs;
    ArrayList<String> list;
    ArrayList<String> numbers = new ArrayList<>();
    adapterbetting adapterbetting;
    String market,game;
    ViewDialog progressDialog;
    String url;
    int total = 0;
    ArrayList<String> fillnumber = new ArrayList<>();
    ArrayList<String> fillamount = new ArrayList<>();
    ArrayList<String> fillmarket = new ArrayList<>();
    String numb,amou,types;

    // 0 - open, 1 - close
    int selectedType = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odd_even);
        initViews();
        open_av = getIntent().getStringExtra("open_av");
        url = constant.prefix + getString(R.string.bet);

        prefs = getSharedPreferences(constant.prefs,MODE_PRIVATE);
        game = getIntent().getStringExtra("game");
        market = getIntent().getStringExtra("market");
        numbers = getIntent().getStringArrayListExtra("list");
        if (getIntent().hasExtra("timing")){
            timing = getIntent().getStringExtra("timing");
            type_container.setVisibility(View.GONE);
        }
        title.setText(market.replace("_","").toUpperCase(Locale.ROOT)+", "+game.toUpperCase(Locale.ROOT));


        if (!game.equals("jodi") && !getIntent().hasExtra("timing")){
            ArrayList<String> typeof = new ArrayList<>();

            if (open_av.equals("1")){ typeof.add("OPEN");}
            typeof.add("CLOSE");

            type_container.setVisibility(View.VISIBLE);

            if (open_av.equals("0")){
                selectedType = 1;
                close_game.setTextColor(getResources().getColor(R.color.font));
                close_game.setBackgroundColor(getResources().getColor(R.color.primary));
                open_game.setTextColor(getResources().getColor(R.color.font));
                open_game.setBackgroundColor(getResources().getColor(R.color.gray));
            }

        }

        open_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedType = 0;
                open_game.setTextColor(getResources().getColor(R.color.font));
                open_game.setBackgroundColor(getResources().getColor(R.color.primary));
                close_game.setTextColor(getResources().getColor(R.color.font));
                close_game.setBackgroundColor(getResources().getColor(R.color.gray));
                submit.setBackgroundColor(getResources().getColor(R.color.primary));


            }
        });

        close_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedType = 1;
                close_game.setTextColor(getResources().getColor(R.color.font));
                close_game.setBackgroundColor(getResources().getColor(R.color.primary));
                open_game.setTextColor(getResources().getColor(R.color.font));
                open_game.setBackgroundColor(getResources().getColor(R.color.gray));
                submit.setBackgroundColor(getResources().getColor(R.color.primary));
            }
        });

        odd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                odd.setBackgroundColor(getResources().getColor(R.color.primary));
                odd.setTextColor(getResources().getColor(R.color.font));
                even.setBackgroundColor(getResources().getColor(R.color.gray));
                even.setTextColor(getResources().getColor(R.color.font));
                odd_row.setVisibility(View.VISIBLE);
                even_row.setVisibility(View.GONE);
            }
        });


        even.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                even.setBackgroundColor(getResources().getColor(R.color.primary));
                even.setTextColor(getResources().getColor(R.color.font));
                odd.setBackgroundColor(getResources().getColor(R.color.gray));
                odd.setTextColor(getResources().getColor(R.color.font));
                even_row.setVisibility(View.VISIBLE);
                odd_row.setVisibility(View.GONE);
            }
        });

        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty() || s == null) {
                    // DO NOTHING FIELD IS EMPTY
                } else if (Integer.parseInt(s.toString()) > constant.max_single) {
                    amount.setText(constant.max_single+"");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String num = intent.getStringExtra("number");
                fillamount.remove(Integer.parseInt(num));
                fillnumber.remove(Integer.parseInt(num));
                fillmarket.remove(Integer.parseInt(num));

//                if (fillmarket.size() > 0){
//                    digit_header.setVisibility(View.VISIBLE);
//                } else {
//                    digit_header.setVisibility(View.GONE);
//                }

                total = 0;
                for (int a = 0; a < fillamount.size(); a++) {
                    total = total+Integer.parseInt(fillamount.get(a));
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");
        registerReceiver(mReceiver, intentFilter);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (total <= Integer.parseInt(amount.getText().toString())*5) {
                    if (odd_row.getVisibility() == View.VISIBLE) {
                        fillnumber.add("1");
                        fillnumber.add("3");
                        fillnumber.add("5");
                        fillnumber.add("7");
                        fillnumber.add("9");
                    } else {
                        fillnumber.add("0");
                        fillnumber.add("2");
                        fillnumber.add("4");
                        fillnumber.add("6");
                        fillnumber.add("8");
                    }

                    fillamount.add(amount.getText().toString());
                    fillamount.add(amount.getText().toString());
                    fillamount.add(amount.getText().toString());
                    fillamount.add(amount.getText().toString());
                    fillamount.add(amount.getText().toString());

                    if (selectedType == 0) {
                        fillmarket.add("OPEN");
                        fillmarket.add("OPEN");
                        fillmarket.add("OPEN");
                        fillmarket.add("OPEN");
                        fillmarket.add("OPEN");
                    } else {
                        fillmarket.add("CLOSE");
                        fillmarket.add("CLOSE");
                        fillmarket.add("CLOSE");
                        fillmarket.add("CLOSE");
                        fillmarket.add("CLOSE");
                    }

                    numb = TextUtils.join(",", fillnumber);
                    amou = TextUtils.join(",", fillamount);
                    types = TextUtils.join(",", fillmarket);

                    Log.e("amou",amou);

                    apicall();
                }
                else
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(OddEven.this);
                    builder1.setMessage("You don't have enough points");
                    builder1.setCancelable(true);
                    builder1.setNegativeButton(
                            "Close",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        balance.setText(getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("wallet","0"));
        super.onResume();
    }

    private void apicall() {

        progressDialog = new ViewDialog(OddEven.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String response = null;

        final StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("edsa", "efsdc" + response);
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);

                            if (jsonObject1.getString("active").equals("0")) {
                                Toast.makeText(OddEven.this, "Your account temporarily disabled by admin", Toast.LENGTH_SHORT).show();

                                getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().clear().apply();
                                Intent in = new Intent(getApplicationContext(), login.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                finish();
                            }

                            if (!jsonObject1.getString("session").equals(getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null))) {
                                Toast.makeText(OddEven.this, "Session expired ! Please login again", Toast.LENGTH_SHORT).show();

                                getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().clear().apply();
                                Intent in = new Intent(getApplicationContext(), login.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                finish();
                            }

                            if (jsonObject1.getString("success").equalsIgnoreCase("1")) {

                                Intent in = new Intent(getApplicationContext(), thankyou.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                finish();

                            } else {
                                Toast.makeText(getApplicationContext(), jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
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
                        Toast.makeText(OddEven.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("number",numb);
                params.put("amount",amou);
                params.put("bazar",market);
                params.put("total",total+"");
                params.put("game",game);
                params.put("mobile", prefs.getString("mobile",null));
                params.put("types",types);
                if (!timing.equals("")){
                    params.put("timing",timing);
                }
                params.put("session",getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void initViews() {
        back = findViewById(R.id.back);
        amount = findViewById(R.id.amount);
        submit = findViewById(R.id.submit);
        title = findViewById(R.id.title);
        balance = findViewById(R.id.balance);
        screenTitle = findViewById(R.id.title);
        open_game = findViewById(R.id.open_game);
        close_game = findViewById(R.id.close_game);
        type_container = findViewById(R.id.type_container);
    //    digit_header = findViewById(R.id.digit_header);
        odd_row = findViewById(R.id.odd_row);
        even_row = findViewById(R.id.even_row);
        odd = findViewById(R.id.odd);
        even = findViewById(R.id.even);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}