package com.betplay.dpboss_off;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

public class howot extends AppCompatActivity {

    protected latonormal text;
    ViewDialog progressDialog;
    String url;
    private CardView back;
    private CardView toolbar;
    private latobold submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_howot);
        initViews();
        initView();
        url = constant.prefix + getString(R.string.content);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("youtube","")));
                startActivity(browserIntent);
            }
        });
        apicall();
    }

    private void apicall() {

        progressDialog = new ViewDialog(howot.this);
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

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                text.setText(Html.fromHtml(jsonObject1.getString("howtoplay"), Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                text.setText(Html.fromHtml(jsonObject1.getString("howtoplay")));
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
                        Toast.makeText(howot.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("text", "howtoplay");

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    private void initView() {
        text = (latonormal) findViewById(R.id.text);

        text.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initViews() {
        back = findViewById(R.id.back);
        toolbar = findViewById(R.id.toolbar);
        text = findViewById(R.id.text);
        submit = findViewById(R.id.submit);
    }
}
