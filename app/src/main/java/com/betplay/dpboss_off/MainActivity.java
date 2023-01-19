package com.betplay.dpboss_off;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    protected ScrollView scrollView;
    protected TextView balance;
    protected latonormal hometext;
    SwitchCompat resultNotification;
    SliderView sliderView;
    private SliderAdapter adapter;

    RecyclerView recyclerview;
    SharedPreferences preferences;
    String url;
    String is_gateway = "0";
//  CardView wallet_history;
    LinearLayout game_charts;
    SwipeRefreshLayout swiperefresh;
    private ImageView back;
    private ImageView coin;
    private LinearLayout walletView;
    private RelativeLayout toolbar;
    private SliderView imageSlider;
    private LinearLayout depositMoney;
    private LinearLayout withdrawMoney;
    private LinearLayout openChart;
    private LinearLayout walletHistory;
    private CardView homeMenu;
    private ImageView whatsappIcon;
    private CardView single;
    private CardView jodi;
    private CardView singlepatti;
    private CardView doublepatti;
    private CardView tripepatti;
    private CardView halfsangam;
    private CardView fullsangam;
    private Context context;
    private CardView dpMotor;
    final String num = "+917428546865";
    final String msg = "add point admin";
    private LinearLayout games;
    private LinearLayout mybids;
    private LinearLayout passbook;
    private LinearLayout funds;
    private LinearLayout support;
    Button btnAddPoints;
    CardView cdNikalnahai;
    Button JamaKarna,btnWithdrawal;
    LinearLayout lautoDeposit;
    final int UPI_PAYMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initViews();

        findViewById(R.id.whatsapp_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp();
            }
        });
        url = constant.prefix + getString(R.string.home);


        preferences = getSharedPreferences(constant.prefs, MODE_PRIVATE);
        apicall();

        if (preferences.getString("wallet", null) != null) {
            balance.setText(preferences.getString("wallet", null));
        } else {
            balance.setText("Loading");
        }

        if (preferences.getString("homeline", null) != null) {
            if (preferences.getString("homeline", "").equals("")) {
                hometext.setVisibility(View.GONE);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    hometext.setText(Html.fromHtml(preferences.getString("homeline", ""), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    hometext.setText(Html.fromHtml(preferences.getString("homeline", null)));
                }
            }
        } else {
            hometext.setText("Loading...");
        }
        lautoDeposit.setOnClickListener(view -> {
            try {
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault());
                String transcId = df.format(c);
                payUsingUpi(""+Double.parseDouble("10"), constant.UPI_ID_VAL, constant.UPI_ID_VAL,transcId);

            }catch (Exception e){
                Log.d("PAYMENT_GATEWAY",e.getMessage());

            }

        });

        cdNikalnahai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWhatsApp("sir nikalna h amt");
            }
        });

        JamaKarna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWhatsApp("sir add krna hai");
            }
        });
        btnWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWhatsApp("sir mere is details pe bhej do");
            }
        });


        single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, bazar.class).putExtra("game", "single").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        jodi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, bazar.class).putExtra("game", "jodi").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });


        singlepatti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, bazar.class).putExtra("game", "singlepatti").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        doublepatti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, bazar.class).putExtra("game", "doublepatti").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        tripepatti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, bazar.class).putExtra("game", "tripepatti").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });


        halfsangam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, bazar.class).putExtra("game", "halfsangam").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        fullsangam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, bazar.class).putExtra("game", "fullsangam").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        dpMotor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, bazar.class).putExtra("game", "red_bracket").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });



        // deposit_money.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
                //openWhatsApp("DEPOSIT REQUEST:");
        //        if (is_gateway.equals("1")) {
        //            startActivity(new Intent(MainActivity.this, deposit_money.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        //                    .putExtra("action", "deposit"));
        //        } else {
        //            openWhatsApp("DEPOSIT REQUEST:");
        //        }
        //    }
        //});

        game_charts.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ledger.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)));


//         withdraw_money.setOnClickListener(v -> {
//           startActivity(new Intent(MainActivity.this, withdraw.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//           openWhatsApp("WITHDRAW REQUEST:");
//         });

//      wallet_history.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, played.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)));

    }
    void payUsingUpi(String amount, String upiId, String name, String transcId) {

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)                  // YOUR UPI ID
                .appendQueryParameter("pn", upiId)                  // USE YOUR UPI ID NOT YOUR NAME
                .appendQueryParameter("mc", "1234")
                .appendQueryParameter("tid", transcId)
                .appendQueryParameter("tr", "asgdfuyas3153")
                .appendQueryParameter("am", amount)
                .appendQueryParameter("mam", amount)
                .appendQueryParameter("cu", "INR")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }
    }


    public void createDrawer() {


        Typeface face = Typeface.createFromAsset(getAssets(), "Oxygen-Bold.ttf");

        PrimaryDrawerItem home = new PrimaryDrawerItem().withName("HOME").withIcon(R.drawable.house).withIdentifier(999).withTypeface(face);
        PrimaryDrawerItem account = new PrimaryDrawerItem().withName("My Profile").withIcon(R.drawable.user_icon).withIdentifier(1).withTypeface(face);
        PrimaryDrawerItem charts = new PrimaryDrawerItem().withName("Charts").withIdentifier(101).withIcon(R.drawable.chart_new).withTypeface(face);
        PrimaryDrawerItem rate = new PrimaryDrawerItem().withName("GAME RATES").withIdentifier(2).withIcon(R.drawable.chart_iconn).withTypeface(face);
        PrimaryDrawerItem earn = new PrimaryDrawerItem().withName("Refer and Earn").withIcon(R.drawable.exchange).withIdentifier(21).withTypeface(face);
        PrimaryDrawerItem notice = new PrimaryDrawerItem().withName("Notification").withIcon(R.drawable.warning).withIdentifier(3).withTypeface(face);
        PrimaryDrawerItem deposit = new PrimaryDrawerItem().withName("Deposit").withIcon(R.drawable.deposit_new).withIdentifier(4).withTypeface(face);
        PrimaryDrawerItem withdraw = new PrimaryDrawerItem().withName("WITHDRAW").withIcon(R.drawable.atm_withdraw).withIdentifier(41).withTypeface(face);
//      PrimaryDrawerItem withdraw_details = new PrimaryDrawerItem().withName("PAYMENT DETAILS").withIcon(R.drawable.payment_method).withIdentifier(411).withTypeface(face);
        PrimaryDrawerItem ledger = new PrimaryDrawerItem().withName("WIN (Rewards)").withIcon(R.drawable.winning).withIdentifier(6).withTypeface(face);
        PrimaryDrawerItem details = new PrimaryDrawerItem().withName("DETAILS").withIcon(R.drawable.transaction).withIdentifier(8).withTypeface(face);
        PrimaryDrawerItem played = new PrimaryDrawerItem().withName("GAME HISTORY").withIcon(R.drawable.console).withIdentifier(9).withTypeface(face);
        PrimaryDrawerItem howto = new PrimaryDrawerItem().withName("How to Play").withIcon(R.drawable.help).withIdentifier(10).withTypeface(face);
        PrimaryDrawerItem share = new PrimaryDrawerItem().withName("SHARE").withIcon(R.drawable.share_2).withIdentifier(11).withTypeface(face);
        PrimaryDrawerItem logout = new PrimaryDrawerItem().withName("Logout").withIcon(R.drawable.logout_icon).withIdentifier(7).withTypeface(face);

        List<IDrawerItem> drawerItems = new ArrayList<>();
        if (getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("verify", "0").equals("1")) {
            drawerItems.add(home);
            drawerItems.add(details);
            drawerItems.add(played);
//          drawerItems.add(withdraw_details);
            drawerItems.add(ledger);
            drawerItems.add(howto);
            drawerItems.add(rate);
            drawerItems.add(notice);
            drawerItems.add(share);
            walletView.setVisibility(View.VISIBLE);
            homeMenu.setVisibility(View.VISIBLE);
          //  games.setVisibility(View.VISIBLE);
        } else {
            drawerItems.add(home);
            drawerItems.add(howto);
            drawerItems.add(notice);
            drawerItems.add(share);
            walletView.setVisibility(View.GONE);
            homeMenu.setVisibility(View.GONE);
            //   games.setVisibility(View.GONE);
        }

        final Drawer drawer = new DrawerBuilder()
                .withHeaderDivider(true)
                .withActivity(MainActivity.this)
                .withSliderBackgroundColor(getResources().getColor(R.color.md_white_1000))
                .withTranslucentStatusBar(true)
                .withHeader(R.layout.header)
                .withFooter(R.layout.footer)
                .withActionBarDrawerToggle(false)
                .withHeaderDivider(false)
                .withDrawerItems(
                        drawerItems
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem.equals(1)) {
                            startActivity(new Intent(MainActivity.this, profile.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        if (drawerItem.equals(101)) {
                            startActivity(new Intent(MainActivity.this, chart_menu.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        if (drawerItem.equals(2)) {
                            startActivity(new Intent(MainActivity.this, rate.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        if (drawerItem.equals(21)) {
                            startActivity(new Intent(MainActivity.this, earn.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        if (drawerItem.equals(3)) {
                            startActivity(new Intent(MainActivity.this, notice.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        if (drawerItem.equals(4)) {
                            openWhatsApp();
                        }
                        if (drawerItem.equals(41)) {
                            openWhatsApp("WITHDRAW REQUEST - ");
                        }
                        if (drawerItem.equals(411)) {
                            startActivity(new Intent(MainActivity.this, WithdrawDetails.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        if (drawerItem.equals(10)) {
                            startActivity(new Intent(MainActivity.this, howot.class));
                        }
                        if (drawerItem.equals(11)) {

                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT,
                                    "Download " + getString(R.string.app_name) + " and earn coins at home, Download link - " + constant.link);
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);
                        }
                        if (drawerItem.equals(7)) {
                            preferences.edit().clear().apply();
                            Intent in = new Intent(MainActivity.this, login.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(in);
                            MainActivity.this.finish();
                        }
                        if (drawerItem.equals(6)) {
                            startActivity(new Intent(MainActivity.this, ledger.class));
                        }
                        if (drawerItem.equals(8)) {
                            startActivity(new Intent(MainActivity.this, TransactionList.class));
                        }
                        if (drawerItem.equals(9)) {
                            startActivity(new Intent(MainActivity.this, played.class));
                        }

                        return false;
                    }
                })
                .build();

        TextView name = drawer.getHeader().findViewById(R.id.name);
        name.setText(preferences.getString("name", ""));
        TextView mobile = drawer.getHeader().findViewById(R.id.mobile);
        mobile.setText(preferences.getString("mobile", ""));
        resultNotification = drawer.getHeader().findViewById(R.id.resultNotification);

        if (preferences.getString("result", null) != null) {
            resultNotification.setChecked(preferences.getString("result", null).equals("1"));
        } else {
            resultNotification.setChecked(false);
        }

        resultNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                FirebaseMessaging.getInstance().subscribeToTopic("result")
                        .addOnCompleteListener(task2 -> {
                            preferences.edit().putString("result", "1").apply();
                        });
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("result")
                        .addOnCompleteListener(task2 -> {
                            preferences.edit().putString("result", "0").apply();
                        });
            }
        });


        drawer.getFooter().findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.edit().clear().apply();
                Intent in = new Intent(MainActivity.this, login.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                MainActivity.this.finish();
            }
        });


        drawer.getHeader().findViewById(R.id.deposit_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp();
            }
        });

        drawer.getHeader().findViewById(R.id.withdraw_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = constant.getWhatsapp(getApplicationContext());

                Uri uri = Uri.parse(url);
                Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(sendIntent);
            }
        });
        drawer.getHeader().findViewById(R.id.view_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, profile.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen()) {
                    drawer.closeDrawer();
                } else {
                    drawer.openDrawer();
                }
            }
        });

    }

    private void apicall() {


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        try {

                            JSONObject jsonObject1 = new JSONObject(response);

                            if (jsonObject1.getString("active").equals("0")) {
                                Toast.makeText(MainActivity.this, "Your account temporarily disabled by admin", Toast.LENGTH_SHORT).show();

                                preferences.edit().clear().apply();
                                Intent in = new Intent(getApplicationContext(), login.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                finish();
                            }

                            if (!jsonObject1.getString("session").equals(getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null))) {
                                Toast.makeText(MainActivity.this, "Session expired ! Please login again", Toast.LENGTH_SHORT).show();

                                preferences.edit().clear().apply();
                                Intent in = new Intent(getApplicationContext(), login.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                finish();
                            }

                            balance.setText(jsonObject1.getString("wallet"));

                            if (jsonObject1.getString("homeline").equals("")) {
                                hometext.setVisibility(View.GONE);
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    hometext.setText(Html.fromHtml(jsonObject1.getString("homeline"), Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    hometext.setText(Html.fromHtml(jsonObject1.getString("homeline")));
                                }
                            }


                            ArrayList<String> name = new ArrayList<>();
                            ArrayList<String> result = new ArrayList<>();


                            ArrayList<String> is_open = new ArrayList<>();
                            ArrayList<String> open_time = new ArrayList<>();
                            ArrayList<String> close_time = new ArrayList<>();
                            ArrayList<String> open_av = new ArrayList<>();

                            JSONArray jsonArray = jsonObject1.getJSONArray("result");
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(a);

                                open_time.add(jsonObject.getString("open_time"));
                                close_time.add(jsonObject.getString("close_time"));
                                name.add(jsonObject.getString("market"));
                                result.add(jsonObject.getString("result"));
                                is_open.add(jsonObject.getString("is_open"));
                                open_av.add(jsonObject.getString("is_close"));

                            }

                            adapter = new SliderAdapter(MainActivity.this);

                            if (jsonObject1.has("images")) {
                                JSONArray jsonArrayx = jsonObject1.getJSONArray("images");
                                for (int a = 0; a < jsonArrayx.length(); a++) {
                                    JSONObject jsonObject = jsonArrayx.getJSONObject(a);

                                    SliderItem sliderItem1 = new SliderItem();
                                    sliderItem1.setImageUrl(constant.project_root + "admin/" + jsonObject.getString("image"));
                                    adapter.addItem(sliderItem1);

                                }
                                sliderView.setSliderAdapter(adapter);
                            } else {
                                sliderView.setVisibility(View.GONE);
                            }


                            adapter_result rc = new adapter_result(MainActivity.this, name, result, is_open, open_time, close_time, open_av);
                            recyclerview.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
                            recyclerview.setAdapter(rc);


                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("wallet", jsonObject1.getString("wallet")).apply();
                            editor.putString("homeline", jsonObject1.getString("homeline")).apply();
                            editor.putString("code", jsonObject1.getString("code")).apply();
                            editor.putString("is_gateway", jsonObject1.getString("gateway")).apply();
                            editor.putString("whatsapp", jsonObject1.getString("whatsapp")).apply();
                            editor.putString("transfer_points_status", jsonObject1.getString("transfer_points_status")).apply();
                            editor.putString("paytm", jsonObject1.getString("paytm")).apply();
                            editor.putString("verify", jsonObject1.getString("verify")).apply();

                            createDrawer();

                            is_gateway = jsonObject1.getString("gateway");

                            if (swiperefresh.isRefreshing()) {
                                swiperefresh.setRefreshing(false);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();


                            Toast.makeText(MainActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();

                        Toast.makeText(MainActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", preferences.getString("mobile", null));
                params.put("session", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);

        btnAddPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWhatsApp("add point admin");
            }
        });
    }

    @Override
    protected void onResume() {
        apicall();
        super.onResume();
    }

    private void openWhatsApp() {
        String url = constant.getWhatsapp(getApplicationContext());

        Uri uri = Uri.parse(url);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(sendIntent);
    }

    private void openWhatsApp(String msg) {
        String url = constant.getWhatsapp(getApplicationContext()) + "?text=" + msg;

        Uri uri = Uri.parse(url);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(sendIntent);
    }


    private void initViews() {
        sliderView = findViewById(R.id.imageSlider);
        balance = findViewById(R.id.balance);
        hometext = findViewById(R.id.hometext);
        scrollView = findViewById(R.id.scrollView);
        recyclerview = findViewById(R.id.recyclerview);
        btnAddPoints = findViewById(R.id.btnAddPoints);
        cdNikalnahai = findViewById(R.id.cdNikalnahai);
        JamaKarna = findViewById(R.id.JamaKarna);
        btnWithdrawal = findViewById(R.id.btnWithdrawal);

        game_charts = findViewById(R.id.open_chart);
        lautoDeposit = findViewById(R.id.lautoDeposit);
//      deposit_money = findViewById(R.id.deposit_money);
//      withdraw_money = findViewById(R.id.withdraw_money);
//      wallet_history = findViewById(R.id.wallet_history);
        swiperefresh = findViewById(R.id.swiperefresh);

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apicall();
            }
        });


        hometext.setMovementMethod(LinkMovementMethod.getInstance());

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        back = findViewById(R.id.back);
        back = findViewById(R.id.back);
        coin = findViewById(R.id.coin);
        walletView = findViewById(R.id.wallet_view);
        toolbar = findViewById(R.id.toolbar);
        homeMenu = findViewById(R.id.home_menu);
        whatsappIcon = findViewById(R.id.whatsapp_icon);
        single = findViewById(R.id.single);
        jodi = findViewById(R.id.jodi);
        singlepatti = findViewById(R.id.singlepatti);
        doublepatti = findViewById(R.id.doublepatti);
        tripepatti = findViewById(R.id.tripepatti);
        halfsangam = findViewById(R.id.halfsangam);
        fullsangam = findViewById(R.id.fullsangam);
        dpMotor = findViewById(R.id.dp_motor);
        games = findViewById(R.id.games);
        mybids = findViewById(R.id.mybids);
        passbook = findViewById(R.id.passbook);
        funds = findViewById(R.id.funds);
        support = findViewById(R.id.support);

        mybids.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, com.betplay.dpboss_off.played.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)));
        passbook.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, com.betplay.dpboss_off.transactions.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)));
        funds.setOnClickListener(v -> openWhatsApp());
        support.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, com.betplay.dpboss_off.Support.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)));
    }
//    private boolean isAppInstalled (String s){
//        PackageManager packageManager = getPackageManager();
//        boolean is_installed;
//
//        try {
//            packageManager.getPackageInfo(s, PackageManager.GET_ACTIVITIES);
//            is_installed = true;
//        } catch (PackageManager.NameNotFoundException e) {
//            is_installed = false;
//            e.printStackTrace();
//        }
//        return is_installed;
//    }

    private boolean isWhatappInstalled(){

        PackageManager packageManager = getPackageManager();
        boolean whatsappInstalled;

        try {

            packageManager.getPackageInfo("com.whatsapp",PackageManager.GET_ACTIVITIES);
            whatsappInstalled = true;


        }catch (PackageManager.NameNotFoundException e){

            whatsappInstalled = false;

        }

        return whatsappInstalled;

    }

}
