package com.betplay.dpboss_off;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lib.kingja.switchbutton.SwitchMultiButton;

public class deposit_money extends AppCompatActivity {


    ViewDialog progressDialog;
    String url;
    ArrayList<String> gateways = new ArrayList<>();
    String gateway = "";
    SwitchMultiButton mSwitchMultiButton;
    EditText amount;
    private CardView back;
    private CardView toolbar;
    private SwitchMultiButton switchmultibutton;
    private LinearLayout gpayIcon;
    private LinearLayout phonepeIcon;
    private LinearLayout paytmIcon;
    private latobold submit;
    private LinearLayout whatsapp;


    String url2 = constant.prefix + "get_payment.php";
    String url3 = constant.prefix + "upi_payment.php";
    String _amount = "0";
    final int UPI_PAYMENT = 0;
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();
    String hash, hashKey;
    String package_name = "";
    String selectedApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_money);
        initViews();

        url = constant.prefix + getString(R.string.get_gateway);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        amount = findViewById(R.id.amount);
        mSwitchMultiButton = findViewById(R.id.switchmultibutton);
        findViewById(R.id.whatsapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = constant.getWhatsapp(getApplicationContext()) + "?text=" + "I Want To Add Money:";

                Uri uri = Uri.parse(url);
                Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(sendIntent);
            }
        });

      //  apicall();

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount.getText().toString().isEmpty() || amount.getText().toString().equals("0")) {
                    amount.setError("Enter valid amount");
                } else if (Integer.parseInt(amount.getText().toString()) < Integer.parseInt(getSharedPreferences(constant.prefs, Context.MODE_PRIVATE).getString("min_deposit", constant.min_deposit + ""))) {
                    amount.setError("amount must be more than " + getSharedPreferences(constant.prefs, Context.MODE_PRIVATE).getString("min_deposit", constant.min_deposit + ""));
                } else {

                    if (mSwitchMultiButton.getVisibility() == View.VISIBLE) {
                        gateway = gateways.get(mSwitchMultiButton.getSelectedTab());
                    }

                    startActivity(new Intent(deposit_money.this, webview.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("amount", amount.getText().toString()).putExtra("gateway", gateway));
                }
            }
        });


        paytmIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (amount.getText().toString().isEmpty() || amount.getText().toString().equals("0")){
                    amount.setError("Enter points");
                    return;
                } else if (Integer.parseInt(amount.getText().toString()) < Integer.parseInt(getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("min_deposit","10"))){
                    amount.setError("Enter points above "+getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("min_deposit","10"));
                    return;
                }

                apicall3("paytm","paytm");

            }
        });

        gpayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount.getText().toString().isEmpty() || amount.getText().toString().equals("0")){
                    amount.setError("Enter points");
                    return;
                } else if (Integer.parseInt(amount.getText().toString()) < Integer.parseInt(getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("min_deposit","10"))){
                    amount.setError("Enter points above "+getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("min_deposit","10"));
                    return;
                }

                apicall3("gpay","gpay");
            }
        });

        phonepeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount.getText().toString().isEmpty() || amount.getText().toString().equals("0")){
                    amount.setError("Enter points");
                    return;
                } else if (Integer.parseInt(amount.getText().toString()) < Integer.parseInt(getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("min_deposit","10"))){
                    amount.setError("Enter points above "+getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("min_deposit","10"));
                    return;
                }

                apicall3("phonepe","phonepe");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPI_PAYMENT) {
            if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                if (data != null) {
                    String trxt = data.getStringExtra("response");
                    Log.d("UPI", "onActivityResult: " + trxt);
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add(trxt);
                    upiPaymentDataOperation(dataList);
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
            } else {
                Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                ArrayList<String> dataList = new ArrayList<>();
                dataList.add("nothing");
                upiPaymentDataOperation(dataList);
            }
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(deposit_money.this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(deposit_money.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                apicall2();
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(deposit_money.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                // apicall2();
            } else {
                Toast.makeText(deposit_money.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(deposit_money.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }


    String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }



    private void apicall() {

        progressDialog = new ViewDialog(deposit_money.this);
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
                            for (int a = 0; jsonArray.length() > a; a++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(a);
                                gateways.add(jsonObject.getString("name").toLowerCase());
                            }

                            if (gateways.contains("paytm") && gateways.contains("razorpay")) {
                                mSwitchMultiButton.setText("Paytm", "Razorpay");
                                mSwitchMultiButton.setVisibility(View.VISIBLE);
                            } else if (gateways.contains("paytm") && !gateways.contains("razorpay")) {
                                gateway = "paytm";
                            } else if (!gateways.contains("paytm") && gateways.contains("razorpay")) {
                                gateway = "razorpay";
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
                        Toast.makeText(deposit_money.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void apicall3(String upiApp, String type) {

        progressDialog = new ViewDialog(deposit_money.this);
        progressDialog.showDialog();

        hashKey = randomString(10);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String response = null;

        final StringRequest postRequest = new StringRequest(Request.Method.POST, url3,
                response1 -> {
                    Log.e("edsa", "efsdc" + response1);
                    progressDialog.hideDialog();
                    try {
                        JSONObject jsonObject1 = new JSONObject(response1);

                        if (jsonObject1.getString("success").equals("1")) {
                            hash = jsonObject1.getString("hash");
                            payUsingUpi(amount.getText().toString(), getString(R.string.app_name), "Adding coins to wallet",upiApp);
                        } else {
                            Toast.makeText(deposit_money.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.hideDialog();
                    }
                },
                error -> {
                    error.printStackTrace();
                    progressDialog.hideDialog();
                    Toast.makeText(deposit_money.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));
                params.put("session", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));
                params.put("amount", amount.getText().toString());
                params.put("hash_key", hashKey);
                params.put("type", type);


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void apicall2() {

        progressDialog = new ViewDialog(deposit_money.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String response = null;

        final StringRequest postRequest = new StringRequest(Request.Method.POST, url2,
                response1 -> {
                    Log.e("edsa", "efsdc" + response1);
                    progressDialog.hideDialog();
                    try {
                        JSONObject jsonObject1 = new JSONObject(response1);

                        if (jsonObject1.getString("success").equals("0")) {
                            new androidx.appcompat.app.AlertDialog.Builder(deposit_money.this)
                                    .setTitle("Payment Received")
                                    .setMessage("We received your payment successfully, We will update your wallet balance in sometime")
                                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                            finish();
                                        }
                                    })
                                    .show();

                        } else {
                            Toast.makeText(deposit_money.this, "Coins added to wallet", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.hideDialog();
                    }
                },
                error -> {
                    error.printStackTrace();
                    progressDialog.hideDialog();
                    Toast.makeText(deposit_money.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));
                params.put("hash_key", hashKey);
                params.put("hash", hash);
                params.put("amount", amount.getText().toString());
                params.put("session", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    void payUsingUpi(String amount, String name, String note, String upiApp) {


        selectedApp = upiApp;


        String upi_id = getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("upi", "");
        String mcc = getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("merchant", "");


        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upi_id)
                .appendQueryParameter("mc", mcc)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount + ".00")
                .appendQueryParameter("cu", "INR")
                .appendQueryParameter("tr", "WHATSAPP_QR")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        //    Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        Intent chooser = new Intent(Intent.ACTION_VIEW);
        chooser.setData(uri);

        switch (upiApp) {
            case "gpay":
                package_name = getString(R.string.gpay);
                break;
            case "paytm":
                package_name = getString(R.string.paytm);
                break;
            case "phonepe":
                package_name = getString(R.string.phonepe);
                break;
        }

        chooser.setPackage(package_name);

        PackageManager pm = getPackageManager();

        if (!isPackageInstalled(package_name, pm)) {
            new androidx.appcompat.app.AlertDialog.Builder(deposit_money.this)
                    .setTitle(upiApp + " Not Installed")
                    .setMessage("Your device don't have application installed, Do you want to download now ?")
                    .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + package_name)));
                            } catch (ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + package_name)));
                            }
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton("Later", null)
                    .show();

            return;
        }
        // check if intent resolves
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(deposit_money.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }

    }


    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        Log.e("checkingPackage", ":" + packageName);
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    private void initViews() {
        back = findViewById(R.id.back);
        toolbar = findViewById(R.id.toolbar);
        amount = findViewById(R.id.amount);
        switchmultibutton = findViewById(R.id.switchmultibutton);
        gpayIcon = findViewById(R.id.gpay_icon);
        phonepeIcon = findViewById(R.id.phonepe_icon);
        paytmIcon = findViewById(R.id.paytm_icon);
        submit = findViewById(R.id.submit);
        whatsapp = findViewById(R.id.whatsapp);
    }
}