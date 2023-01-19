package com.betplay.dpboss_off;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
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

public class starline_markets extends AppCompatActivity {


    RecyclerView recyclerview;

    ArrayList<String> number = new ArrayList<>();
    String game;
    ViewDialog progressDialog;
    String url;
    String url2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starline_markets);
        initViews();
        url2 = constant.prefix + getResources().getString(R.string.market_list_starline);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        apicall2();
    }


    private void apicall2() {

        progressDialog = new ViewDialog(starline_markets.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new StringRequest(Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);

                            JSONArray jsonArray = jsonObject1.getJSONArray("data");

                            ArrayList<String> name = new ArrayList<>();

                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(a);
                                name.add(jsonObject.getString("name"));
                            }

                            adapter_market_starline rc = new adapter_market_starline(starline_markets.this,name);
                            recyclerview.setLayoutManager(new GridLayoutManager(starline_markets.this, 1));
                            recyclerview.setAdapter(rc);
                            rc.notifyDataSetChanged();


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
                        Toast.makeText(starline_markets.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("session",getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }



    private void initViews() {

        recyclerview = findViewById(R.id.recyclerview);
    }
}
