package com.betplay.dpboss_off;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class chart_menu extends AppCompatActivity {

    RecyclerView recyclerview;
    EditText search;
    String url;

    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> result = new ArrayList<>();
    ViewDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_menu);
        initViews();
        url = constant.prefix + getString(R.string.charts);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        apicall();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!search.getText().toString().isEmpty()){
                    ArrayList<String> name2 = new ArrayList<>();
                    ArrayList<String> result2 = new ArrayList<>();

                    for (int a =0; a < name.size(); a++)
                    {
                        if (name.get(a).toLowerCase().contains(s.toString().toLowerCase())){
                            name2.add(name.get(a));
                            result2.add(result.get(a));
                        }
                    }

                    adapter_chart rc = new adapter_chart(chart_menu.this,name2,result2);
                    recyclerview.setLayoutManager(new GridLayoutManager(chart_menu.this, 2));
                    recyclerview.setAdapter(rc);
                    rc.notifyDataSetChanged();
                }
                else  {
                    adapter_chart rc = new adapter_chart(chart_menu.this,name,result);
                    recyclerview.setLayoutManager(new GridLayoutManager(chart_menu.this, 2));
                    recyclerview.setAdapter(rc);
                    rc.notifyDataSetChanged();
                }

            }
        });

    }


    private void apicall() {
        progressDialog = new ViewDialog(chart_menu.this);
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

                            JSONArray jsonArray = jsonObject1.getJSONArray("data");

                            for (int a = 0; a < jsonArray.length(); a++){
                                JSONObject jsonObject = jsonArray.getJSONObject(a);

                                name.add(jsonObject.getString("market"));
                                result.add(jsonObject.getString("result"));

                            }
                            adapter_chart rc = new adapter_chart(chart_menu.this,name,result);
                            recyclerview.setLayoutManager(new GridLayoutManager(chart_menu.this, 2));
                            recyclerview.setAdapter(rc);
                            rc.notifyDataSetChanged();



                        } catch (JSONException e) {
                            e.printStackTrace();

                            progressDialog.hideDialog();

                            Toast.makeText(chart_menu.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();

                        progressDialog.hideDialog();
                        Toast.makeText(chart_menu.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
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
        search = findViewById(R.id.search);
    }
}