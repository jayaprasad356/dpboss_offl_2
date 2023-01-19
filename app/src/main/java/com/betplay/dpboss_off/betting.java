package com.betplay.dpboss_off;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Map;

public class betting extends AppCompatActivity {

    protected RecyclerView recyclerview;
    protected latobold submit;
    protected ScrollView scrollView;
    protected EditText totalamount;
    SharedPreferences prefs;
    ArrayList<String> list;
    ArrayList<String> number = new ArrayList<>();
    adapterbetting adapterbetting;
    String market,game;
    ViewDialog progressDialog;
    String url;
    int total = 0;
    ArrayList<String> fillnumber = new ArrayList<>();
    ArrayList<String> fillamount = new ArrayList<>();
    ArrayList<String> fillmarket = new ArrayList<>();
    String numb,amou,types,timing = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_betting);
        initView();
        url = constant.prefix + getString(R.string.bet);

        prefs = getSharedPreferences(constant.prefs,MODE_PRIVATE);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        game = getIntent().getStringExtra("game");
        market = getIntent().getStringExtra("market");
        number = getIntent().getStringArrayListExtra("list");
        if (getIntent().hasExtra("timing")){
            timing = getIntent().getStringExtra("timing");
        }

        adapterbetting = new adapterbetting(betting.this, number);

        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                list = adapterbetting.getNumber();
                total = 0;
                for (int a = 0; a < list.size(); a++) {
                    total = total+Integer.parseInt(list.get(a));
                }
                totalamount.setText(total+"");
            }
        };

        IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");
        registerReceiver(mReceiver, intentFilter);

        recyclerview.setLayoutManager(new GridLayoutManager(betting.this, 4));
        recyclerview.setAdapter(adapterbetting);
        adapterbetting.notifyDataSetChanged();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("list",list.toString());
                if (total < constant.min_total || total > constant.max_total)
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(betting.this);
                    builder1.setMessage("You can only bet between "+constant.min_total+" INR to "+constant.max_total+" INR");
                    builder1.setCancelable(true);
                    builder1.setNegativeButton(
                            "Okay",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

                else if (total <= Integer.parseInt(prefs.getString("wallet",null))) {
                    for (int a = 0; a < list.size(); a++) {
                        if (!list.get(a).equals("0") && Integer.parseInt(list.get(a)) < constant.min_single || Integer.parseInt(list.get(a)) > constant.max_single){
                            fillamount.clear();
                            fillnumber.clear();

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(betting.this);
                            builder1.setMessage("You can only bet between "+constant.min_single+" INR to "+constant.max_single+" INR");
                            builder1.setCancelable(true);
                            builder1.setNegativeButton(
                                    "Okay",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();

                            return;
                        }
                        else if (!list.get(a).equals("0")) {
                            fillnumber.add(number.get(a) + "");
                            fillamount.add(list.get(a));
                            fillmarket.add("");
                        }
                    }

                    numb = TextUtils.join(",", fillnumber);
                    amou = TextUtils.join(",", fillamount);
                    types = TextUtils.join(",", fillmarket);


                    apicall();
                }
                else
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(betting.this);
                    builder1.setMessage("You don't have enough points");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Recharge",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    openWhatsApp();
                                    dialog.dismiss();
                                }
                            });

                    builder1.setNegativeButton(
                            "Cancel",
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

    private void apicall() {

        progressDialog = new ViewDialog(betting.this);
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
                                Toast.makeText(betting.this, "Your account temporarily disabled by admin", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(betting.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
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
                params.put("types",types);
                params.put("mobile", prefs.getString("mobile",null));
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

    private void openWhatsApp() {

        String url = constant.getWhatsapp(getApplicationContext());

        Uri uri = Uri.parse(url);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(sendIntent);
    }

    private void initView() {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        submit = (latobold) findViewById(R.id.submit);
        totalamount = (EditText) findViewById(R.id.totalamount);
    }


}
