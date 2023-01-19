package com.betplay.dpboss_off;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

public class ForgotPassword extends AppCompatActivity {

    private CardView back;
    private EditText mobile;
    private EditText password;
    private latobold submit;
    ActivityResultLauncher<Intent> mStartForResult;
    ViewDialog progressDialog;
    String url = constant.prefix+"forgot.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initViews();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobile.getVisibility() == View.VISIBLE) {
                    if (mobile.getText().toString().isEmpty() || mobile.getText().toString().length() != 10) {
                        mobile.setError("Enter valid mobile number");
                    } else {
                        mStartForResult.launch(new Intent(ForgotPassword.this, MobileVerification.class).putExtra("mobile", mobile.getText().toString()));
                    }
                } else {
                    if (password.getText().toString().isEmpty()) {
                        password.setError("Enter valid password");
                    } else {
                        apicall();
                    }
                }
            }
        });
    }


    private void apicall() {

        progressDialog = new ViewDialog(ForgotPassword.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            Toast.makeText(ForgotPassword.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();

                            if (jsonObject1.getString("success").equalsIgnoreCase("1")) {

                                Intent in = new Intent(getApplicationContext(), login.class);
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
                        Toast.makeText(ForgotPassword.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", mobile.getText().toString());
                params.put("pass", password.getText().toString());


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void initViews() {
        back = findViewById(R.id.back);
        mobile = findViewById(R.id.mobile);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);


        mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent == null) return;
                        if (intent.hasExtra("verification") && intent.getStringExtra("verification").equals("success")) {
                            mobile.setVisibility(View.GONE);
                            password.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}