package com.betplay.dpboss_off;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class login extends AppCompatActivity {

    protected ImageView draw;
    protected TextInputEditText mobile;
    protected TextInputEditText password;
    protected latobold submit;
    protected LinearLayout create;
    protected TextView forgot;

    ViewDialog progressDialog;
    String url ;

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    String hash = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_login);
        initView();
        url = constant.prefix + getString(R.string.login);
        hash = getRandomString(30);


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this, signup.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = constant.getWhatsapp(getApplicationContext());
                if (url.equals("")){
                    Toast.makeText(login.this, "Getting information from sever", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(getApplicationContext(), MainActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    finish();
                    return;
                }

                Uri uri = Uri.parse(url);
                Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(sendIntent);
               // startActivity(new Intent(login.this,ForgotPassword.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobile.getText().toString().isEmpty()) {
                    mobile.setError("Enter mobile number");
                } else if (password.getText().toString().isEmpty()) {
                    password.setError("Enter password");
                } else {

                    SharedPreferences.Editor editor = getSharedPreferences(constant.prefs, MODE_PRIVATE).edit();
                    editor.putString("session", hash).apply();

                    apicall();
                }
            }
        });
    }

    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    private void apicall() {

        progressDialog = new ViewDialog(login.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            if (jsonObject1.getString("success").equalsIgnoreCase("1")) {
                                SharedPreferences.Editor editor = getSharedPreferences(constant.prefs, MODE_PRIVATE).edit();
                                editor.putString("mobile", mobile.getText().toString()).apply();
                                editor.putString("login", "true").apply();
                                editor.putString("name", jsonObject1.getString("name")).apply();
                                editor.putString("email", jsonObject1.getString("email")).apply();

                                Intent in = new Intent(getApplicationContext(), MainActivity.class);
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
                        Toast.makeText(login.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", mobile.getText().toString());
                params.put("pass", password.getText().toString());
                params.put("session", hash);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }



    private void initView() {
        mobile = (TextInputEditText) findViewById(R.id.mobile);
        password = (TextInputEditText) findViewById(R.id.password);
        submit = (latobold) findViewById(R.id.submit);
        create = findViewById(R.id.create);
        forgot = (TextView) findViewById(R.id.forgot);
    }
}
