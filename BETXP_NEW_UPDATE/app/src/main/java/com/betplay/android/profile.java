package com.betplay.dpboss_off;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class profile extends AppCompatActivity {

    protected EditText name;
    protected EditText email;
    protected EditText mobile;
    protected latobold submit;
    protected EditText password;
    protected EditText confirm;
    protected latobold submit2;

    ViewDialog progressDialog;
    String url;
    String url2;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_profile);
        initView();
        url = constant.prefix + getString(R.string.profile);
        url2 = constant.prefix + getString(R.string.password);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        prefs = getSharedPreferences(constant.prefs,MODE_PRIVATE);

        name.setText(prefs.getString("name",null));
        email.setText(prefs.getString("email",null));
        mobile.setText(prefs.getString("mobile",null));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty())
                {
                    name.setError("Enter name");
                }
                else if (email.getText().toString().isEmpty())
                {
                    email.setError("Enter email");
                }
                else
                {
                    apicall();
                }
            }
        });

        submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().isEmpty())
                {
                    password.setError("Enter password");
                }
                else if (confirm.getText().toString().isEmpty())
                {
                    confirm.setError("Confirm password");
                }
                else if (!password.getText().toString().equals(confirm.getText().toString()))
                {
                    confirm.setError("Password does not match");
                }
                else
                {
                    apicall2();
                }
            }
        });
    }

    private void apicall2() {

        progressDialog = new ViewDialog(profile.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new StringRequest(Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            if (jsonObject1.getString("success").equalsIgnoreCase("1")) {

                                password.setText("");
                                confirm.setText("");

                                Toast.makeText(profile.this, "Password updated successfully", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(profile.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("pass", password.getText().toString());
                params.put("mobile", prefs.getString("mobile",null));

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    private void apicall() {

        progressDialog = new ViewDialog(profile.this);
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
                            if (jsonObject1.getString("success").equalsIgnoreCase("1")) {
                                SharedPreferences.Editor editor = getSharedPreferences(constant.prefs, MODE_PRIVATE).edit();
                                editor.putString("name", name.getText().toString()).apply();
                                editor.putString("email", email.getText().toString()).apply();

                                Toast.makeText(profile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(profile.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("name", name.getText().toString());
                params.put("email", email.getText().toString());
                params.put("mobile", prefs.getString("mobile",null));

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    private void initView() {
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        mobile = (EditText) findViewById(R.id.mobile);
        submit = (latobold) findViewById(R.id.submit);
        password = (EditText) findViewById(R.id.password);
        confirm = (EditText) findViewById(R.id.confirm);
        submit2 = (latobold) findViewById(R.id.submit2);
    }
}
