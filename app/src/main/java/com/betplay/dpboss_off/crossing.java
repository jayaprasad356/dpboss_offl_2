package com.betplay.dpboss_off;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
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

public class crossing extends AppCompatActivity {

    protected EditText number;
    protected EditText amount;
    protected EditText totalamount;
    protected latobold submit;
    protected NestedScrollView scrollView;
    protected RecyclerView recyclerview;
    String value = "";
    SharedPreferences prefs;
    Boolean ischange = false;

    String market,game;
    ViewDialog progressDialog;
    String url;

    ArrayList<String> fillnumber = new ArrayList<>();
    ArrayList<String> fillamount = new ArrayList<>();
    String numb,amou;

    ArrayList<String> numbers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_crossing);
        initView();
        url = constant.prefix + getString(R.string.bet);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        prefs = getSharedPreferences(constant.prefs,MODE_PRIVATE);
        game = getIntent().getStringExtra("game");
        market = getIntent().getStringExtra("market");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(numbers.size() == 0){
                    Toast.makeText(crossing.this, "Enter valid bet", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (number.getText().toString().isEmpty())
                {
                    number.setError("Enter numbers");
                }
                else if (amount.getText().toString().isEmpty() || amount.getText().toString().equals("0"))
                {
                    amount.setError("Enter amount");
                }
                else {

                    if (Integer.parseInt(totalamount.getText().toString()) < constant.min_total || Integer.parseInt(totalamount.getText().toString()) > constant.max_total)
                    {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(crossing.this);
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
                    else if (Integer.parseInt(totalamount.getText().toString()) <= Integer.parseInt(prefs.getString("wallet", null))) {

                        for (int a = 0; a < numbers.size(); a++) {
                            fillnumber.add(numbers.get(a) + "");
                            fillamount.add(amount.getText().toString());
                        }

                        numb = TextUtils.join(",", fillnumber);
                        amou = TextUtils.join(",", fillamount);
                        apicall();


                    } else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(crossing.this);
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

            }
        });
    }

    private void apicall() {

        progressDialog = new ViewDialog(crossing.this);
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
                                Toast.makeText(crossing.this, "Your account temporarily disabled by admin", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(crossing.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("number",numb);
                params.put("amount",amou);
                params.put("bazar",market);
                params.put("total",totalamount.getText().toString()+"");
                params.put("game","jodi");
                params.put("mobile", prefs.getString("mobile",null));
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

    public void characterCount(String inputString)
    {
            StringBuilder data = new StringBuilder();
            HashMap<Character, Integer> charCountMap
                    = new HashMap<Character, Integer>();
            char[] strArray = inputString.toCharArray();
            for (char c : strArray) {
                if (charCountMap.containsKey(c)) {
                    charCountMap.put(c, charCountMap.get(c) + 1);
                }
                else {
                    charCountMap.put(c, 1);
                }
            }

            numbers.clear();

            for (Map.Entry entry : charCountMap.entrySet()) {
                data.append(entry.getKey().toString());
            }

            value = data.toString();

            for (int a = 0; a < value.length(); a++)
            {

                for (int i = 0; i < value.length(); i++)
                {
                    String nd = value.charAt(a)+""+value.charAt(i)+"";
                    numbers.add(nd);
                }
            }

            adapter_crossing adapterbetting = new adapter_crossing(crossing.this, numbers);
            recyclerview.setLayoutManager(new GridLayoutManager(crossing.this, 4));
            recyclerview.setAdapter(adapterbetting);
            adapterbetting.notifyDataSetChanged();

            number.setText(value);

            if (!amount.getText().toString().isEmpty())
            {
                totalamount.setText(""+(Integer.parseInt(amount.getText().toString().toString())*(value.length()*value.length())));
            }
        }

        private void initView() {
            number = (EditText) findViewById(R.id.number);
            amount = (EditText) findViewById(R.id.amount);
            totalamount = (EditText) findViewById(R.id.totalamount);
            submit = (latobold) findViewById(R.id.submit);
            scrollView = (NestedScrollView) findViewById(R.id.scrollView);
            recyclerview = (RecyclerView) findViewById(R.id.recyclerview);


            number.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s != null && s.length() > 0 && !value.equals(s.toString()))
                    {
                        ischange = true;
                        characterCount(s.toString());
                    }
                    else if (s.toString().equals(""))
                    {
                        numbers.clear();
                        adapter_crossing adapterbetting = new adapter_crossing(crossing.this, numbers);
                        recyclerview.setLayoutManager(new GridLayoutManager(crossing.this, 4));
                        recyclerview.setAdapter(adapterbetting);
                        adapterbetting.notifyDataSetChanged();

                    }
                }
            });

            amount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s != null && s.length() > 0 && value != null && value.length() > 0)
                    {
                        totalamount.setText(""+(Integer.parseInt(s.toString())*(value.length()*value.length())));
                    }
                    else
                    {
                        totalamount.setText("");
                    }

                }
            });

    }
}
