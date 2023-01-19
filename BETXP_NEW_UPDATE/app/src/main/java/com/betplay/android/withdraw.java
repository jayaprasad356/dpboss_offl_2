package com.betplay.dpboss_off;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

import lib.kingja.switchbutton.SwitchMultiButton;

public class withdraw extends AppCompatActivity {


    ViewDialog progressDialog;
    String url,withdraw_request;
    ArrayList<String> gateways = new ArrayList<>();
    String gateway = "";
    SwitchMultiButton mSwitchMultiButton;
    EditText amount,info;
    Spinner mode;

    ArrayList<String> payment_mode = new ArrayList<>();
    ArrayList<String> payment_info = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        url = constant.prefix + getString(R.string.withdraw_modes);
        withdraw_request = constant.prefix + getString(R.string.withdraw_request);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        info = findViewById(R.id.info);
        amount = findViewById(R.id.amount);
        mode = findViewById(R.id.mode);
        findViewById(R.id.whatsapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = constant.getWhatsapp(getApplicationContext()) + "?text=" + "I Want To Withdraw Money:";

                Uri uri = Uri.parse(url);
                Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(sendIntent);
            }
        });
        Log.e("wall",getSharedPreferences(constant.prefs, Context.MODE_PRIVATE).getString("wallet","0"));

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode.getSelectedItemPosition() == 0){
                    Toast.makeText(withdraw.this, "Select Payment method", Toast.LENGTH_SHORT).show();
                } else if (info.getText().toString().isEmpty()){
                    info.setError("Enter valid withdraw information");
                } else if (amount.getText().toString().isEmpty() || amount.getText().toString().equals("0")){
                    amount.setError("Enter valid amount");
                } else if (Integer.parseInt(amount.getText().toString()) < Integer.parseInt(getSharedPreferences(constant.prefs, Context.MODE_PRIVATE).getString("min_withdraw","1000"))){
                    amount.setError("amount must be more than "+getSharedPreferences(constant.prefs, Context.MODE_PRIVATE).getString("min_withdraw",constant.min_deposit+""));
                } else if(Integer.parseInt(amount.getText().toString()) > Integer.parseInt(getSharedPreferences(constant.prefs, Context.MODE_PRIVATE).getString("wallet","0"))){
                    amount.setError("You don't have enough wallet balance");
                } else {
                    apicall();
                }
            }
        });

        get_apicall();
    }



    private void get_apicall() {

        progressDialog = new ViewDialog(withdraw.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("edsa", "efsdc" + response);
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);

                            payment_mode.add("Select Payment Mode");

                            JSONArray jsonArray = jsonObject1.getJSONArray("data");
                            for (int a= 0; jsonArray.length()>a;a++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(a);

                                payment_info.add(jsonObject.getString("hint"));
                                payment_mode.add(jsonObject.getString("name"));
                            }

                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                    (withdraw.this, R.layout.simple_list_item_1,
                                            payment_mode);

                            mode.setAdapter(spinnerArrayAdapter);

                            mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position != 0) {
                                        info.setHint(payment_info.get(position - 1));
                                    } else {
                                        info.setHint("Select Payment Method");
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });


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
                        Toast.makeText(withdraw.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void apicall() {

        progressDialog = new ViewDialog(withdraw.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new StringRequest(Request.Method.POST, withdraw_request,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hideDialog();
                        Log.e("edsa", "efsdc" + response);
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);

                            if (jsonObject1.getString("active").equals("0")) {
                                Toast.makeText(withdraw.this, "Your account temporarily disabled by admin", Toast.LENGTH_SHORT).show();

                                getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().clear().apply();
                                Intent in = new Intent(getApplicationContext(), login.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                finish();
                            }


                            if (jsonObject1.getString("success").equalsIgnoreCase("1")) {
                                SharedPreferences.Editor editor = getSharedPreferences(constant.prefs,MODE_PRIVATE).edit();
                                editor.putString("wallet", jsonObject1.getString("wallet")).apply();

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(withdraw.this);
                                builder1.setMessage(jsonObject1.getString("msg"));
                                builder1.setCancelable(true);
                                builder1.setNegativeButton(
                                        "Okay",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                finish();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
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
                        Toast.makeText(withdraw.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("mobile",null));
                params.put("session",getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));
                params.put("amount",amount.getText().toString());
                params.put("mode",mode.getSelectedItem().toString());
                params.put("info",info.getText().toString());


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

}