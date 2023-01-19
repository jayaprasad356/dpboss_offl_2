package com.betplay.dpboss_off;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;
import android.util.Log;

public class WithdrawDetails extends AppCompatActivity {

    ViewDialog progressDialog;
    SharedPreferences prefs;

    private CardView toolbar;
    private EditText acno,upi;
    private EditText email;
    private EditText mobile;
    private latobold submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_details);
        prefs = getSharedPreferences(constant.prefs,MODE_PRIVATE);
        initViews();
        findViewById(R.id.back).setOnClickListener(v -> finish());
        submit.setOnClickListener(v -> apicall());
    }

    @Override
    protected void onResume() {
        super.onResume();
        apicall2();
    }


    private void apicall() {

        progressDialog = new ViewDialog(WithdrawDetails.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new StringRequest(Request.Method.POST, constant.prefix+"withdraw_mode.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            if (jsonObject1.getString("success").equalsIgnoreCase("1")) {


                                Toast.makeText(WithdrawDetails.this, "Bank details updated successfully", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(WithdrawDetails.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", prefs.getString("mobile",null));
                params.put("ifsc", mobile.getText().toString());
                params.put("name", email.getText().toString());
                params.put("acno", acno.getText().toString());
                params.put("upi", upi.getText().toString());

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void apicall2() {

        progressDialog = new ViewDialog(WithdrawDetails.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new StringRequest(Request.Method.POST, constant.prefix+"get_bank_details.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("with",response);
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            if (jsonObject1.getString("success").equalsIgnoreCase("1")) {

                                acno.setText(jsonObject1.getString("acno"));
                                email.setText(jsonObject1.getString("name"));
                                mobile.setText(jsonObject1.getString("ifsc"));
                                upi.setText(jsonObject1.getString("upi"));


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
                        Toast.makeText(WithdrawDetails.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", prefs.getString("mobile",null));

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        acno = findViewById(R.id.acno);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        submit = findViewById(R.id.submit);
        upi = findViewById(R.id.upi);
    }
}