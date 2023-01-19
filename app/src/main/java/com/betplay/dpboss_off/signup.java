package com.betplay.dpboss_off;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

public class signup extends AppCompatActivity {

    protected TextInputEditText mobile;
    protected TextInputEditText name;
    protected TextInputEditText password;
    protected latobold submit;
    ViewDialog progressDialog;
    String url;
    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";

    String hash = "";

    ActivityResultLauncher<Intent> mStartForResult;
    private ImageView logo;
    private EditText cpassword;
    private EditText email;
    private LinearLayout create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_signup);
        initViews();
        initView();
        url = constant.prefix + getString(R.string.register);
        hash = getRandomString(30);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobile.getText().toString().isEmpty() || mobile.getText().toString().length() != 10) {
                    mobile.setError("Enter valid mobile number");
                } else if (name.getText().toString().isEmpty()) {
                    name.setError("Enter name");
                } else if (password.getText().toString().isEmpty()) {
                    password.setError("Enter valid password");
                } else if (email.getText().toString().isEmpty()) {
                    email.setError("Enter valid password");
                } else if (!email.getText().toString().equals(password.getText().toString())) {
                    email.setError("Password does not match");
                } else {
                    apicall();
                }

            }
        });
    }


    private void apicall() {

        progressDialog = new ViewDialog(signup.this);
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

                                Toast.makeText(signup.this, "Account created successfully, Please login now", Toast.LENGTH_SHORT).show();

                                SharedPreferences.Editor editor = getSharedPreferences(constant.prefs, MODE_PRIVATE).edit();
                                editor.putString("mobile", mobile.getText().toString()).apply();
                                editor.putString("login", "true").apply();
                                editor.putString("name", name.getText().toString()).apply();
                                editor.putString("email", "").apply();
                                editor.putString("session", hash).apply();

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
                        Toast.makeText(signup.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", mobile.getText().toString());
                params.put("name", name.getText().toString());
                params.put("email", "");
                params.put("pass", password.getText().toString());
                params.put("refcode", "");
                params.put("hash", hash);


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private static String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    private void initView() {
        mobile = (TextInputEditText) findViewById(R.id.mobile);
        name = (TextInputEditText) findViewById(R.id.name);
        password = (TextInputEditText) findViewById(R.id.password);
        submit = (latobold) findViewById(R.id.submit);

        mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent == null) return;
                        if (intent.hasExtra("verification") && intent.getStringExtra("verification").equals("success")) {
                            apicall();
                        }
                    }
                });
    }

    private void initViews() {
        logo = findViewById(R.id.logo);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        password = findViewById(R.id.password);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);
        email = findViewById(R.id.email);
        create = findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signup.this, login.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }
}
