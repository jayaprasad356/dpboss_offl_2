package com.betplay.dpboss_off;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ledger extends AppCompatActivity {

    protected RecyclerView recyclerview;

    ViewDialog progressDialog;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_ledger);
        initView();
        url = constant.prefix + getString(R.string.ledger);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        apicall();
    }

    private void apicall() {

        progressDialog = new ViewDialog(ledger.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.e("edsa", "efsdc" + response);
                    progressDialog.hideDialog();
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);


                        ArrayList<String> date = new ArrayList<>();
                        ArrayList<String> remark = new ArrayList<>();
                        ArrayList<String> amount = new ArrayList<>();
                        ArrayList<String> type = new ArrayList<>();


                        JSONArray jsonArray = jsonObject1.getJSONArray("data");
                        for (int a= 0; jsonArray.length()>a;a++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(a);

                            date.add(jsonObject.getString("date"));
                            amount.add(jsonObject.getString("amount"));
                            remark.add(jsonObject.getString("remark"));
                            type.add(jsonObject.getString("type"));

                            adaptertransaction rc = new adaptertransaction(ledger.this,date,remark,amount,type);
                            recyclerview.setLayoutManager(new GridLayoutManager(ledger.this, 1));
                            recyclerview.setAdapter(rc);
                            rc.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.hideDialog();
                    }
                },
                error -> {
                    error.printStackTrace();
                    progressDialog.hideDialog();
                    Toast.makeText(ledger.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("mobile",null));


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    private void initView() {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
    }
}
