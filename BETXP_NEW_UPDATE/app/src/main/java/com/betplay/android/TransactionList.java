package com.betplay.dpboss_off;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import android.util.Log;

public class TransactionList extends AppCompatActivity {

    protected RecyclerView recyclerview;
    ViewDialog progressDialog;
    String url;
    private TextInputEditText fromDate;
    private TextInputEditText toDate;
    private latobold submit;
    private CardView back;
    private ImageView filter;
    private CardView toolbar;
    final Calendar myCalendar = Calendar.getInstance();
    String myFormat = "dd/MM/yy";
    private RelativeLayout dateFilter;
    private latobold description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);
        initViews();
        initView();
        url = constant.prefix + "transaction_new.php";
        findViewById(R.id.back).setOnClickListener(v -> finish());

        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabelFrom();
        };
        fromDate.setOnClickListener(view -> new DatePickerDialog(TransactionList.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        DatePickerDialog.OnDateSetListener date2 = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabelTo();
        };

        toDate.setOnClickListener(view -> new DatePickerDialog(TransactionList.this, date2, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show());
        apicall("", "");

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFilter.setVisibility(View.VISIBLE);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apicall(fromDate.getText().toString(), toDate.getText().toString());
                dateFilter.setVisibility(View.GONE);
                description.setText(fromDate.getText().toString()+" - "+toDate.getText().toString());

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (dateFilter.getVisibility() == View.VISIBLE) {
            return;
        }
        super.onBackPressed();
    }

    private void updateLabelTo() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        toDate.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void updateLabelFrom() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        fromDate.setText(dateFormat.format(myCalendar.getTime()));
    }


    private void apicall(String fromDate, String toDate) {

        progressDialog = new ViewDialog(TransactionList.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("res",response);
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);

                            ArrayList<String> date = new ArrayList<>();
                            ArrayList<String> remark = new ArrayList<>();
                            ArrayList<String> amount = new ArrayList<>();
                            ArrayList<String> type = new ArrayList<>();

                            if (jsonObject1.has("data")){
                                JSONArray jsonArray = jsonObject1.getJSONArray("data");
                                for (int a = 0; jsonArray.length() > a; a++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(a);

                                    date.add(jsonObject.getString("date"));
                                    amount.add(jsonObject.getString("amount"));
                                    remark.add(jsonObject.getString("remark"));
                                    type.add(jsonObject.getString("type"));


                                }
                            }
                            adapter_transaction_new rc = new adapter_transaction_new(TransactionList.this, date, remark, amount, type);
                            recyclerview.setLayoutManager(new GridLayoutManager(TransactionList.this, 1));
                            recyclerview.setAdapter(rc);
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
                        Toast.makeText(TransactionList.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));
                if (!fromDate.equals("")) {
                    params.put("from_date", fromDate);
                }
                if (!toDate.equals("")) {
                    params.put("to_date", toDate);
                }

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    private void initView() {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
    }

    private void initViews() {
        fromDate = findViewById(R.id.from_date);
        toDate = findViewById(R.id.to_date);
        submit = findViewById(R.id.submit);
        back = findViewById(R.id.back);
        filter = findViewById(R.id.filter);
        toolbar = findViewById(R.id.toolbar);
        recyclerview = findViewById(R.id.recyclerview);
        dateFilter = findViewById(R.id.date_filter);
        description = findViewById(R.id.description);
    }
}
