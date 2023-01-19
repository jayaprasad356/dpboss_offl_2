package com.betplay.dpboss_off;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SpDpTp extends AppCompatActivity {

    private CardView back;
    private Spinner type;
    private EditText number;
    private EditText amount;
    private latobold add;
    private RecyclerView recyclerview;
    private EditText totalamount;
    private latobold submit;
    TextView open_game, close_game;
    TextView title, balance, screenTitle;
    LinearLayout type_container, digit_header;

    ArrayList<String> temp_numbers = new ArrayList<>();
    ArrayList<String> temp_types = new ArrayList<>();

    String open_av = "0";

    SharedPreferences prefs;
    ArrayList<String> list;
    ArrayList<String> numbers = new ArrayList<>();
    adapterbetting adapterbetting;
    String market, game;
    ViewDialog progressDialog;
    String url;
    int total = 0;
    ArrayList<String> fillnumber = new ArrayList<>();
    ArrayList<String> fillamount = new ArrayList<>();
    ArrayList<String> fillmarket = new ArrayList<>();
    String numb, amou, types,timing = "";
    ArrayList<String> singlePatti = new ArrayList<>();
    ArrayList<String> doublePatti = new ArrayList<>();
    ArrayList<String> triplePatti = new ArrayList<>();

    // 0 - open, 1 - close
    int selectedType = 0;
    int gameType = 0;
    private ImageView coin;
    private LinearLayout walletView;
    private CardView toolbar;
    private latobold openGame;
    private latobold closeGame;
    private LinearLayout typeContainer;
    private latobold singlePanna;
    private latobold doublePanna;
    private latobold triplePanna;
    private LinearLayout spdptpPanel;
    private LinearLayout digitHeader;
    private NestedScrollView scrollView;
    private RecyclerView sampleRecycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_dp_tp);
        initViews();
        open_av = getIntent().getStringExtra("open_av");
        url = constant.prefix + getString(R.string.bet);
        if (getIntent().hasExtra("timing")){
            timing = getIntent().getStringExtra("timing");
        }
        prefs = getSharedPreferences(constant.prefs, MODE_PRIVATE);
        game = getIntent().getStringExtra("game");
        market = getIntent().getStringExtra("market");
        numbers = getIntent().getStringArrayListExtra("list");

        singlePatti = singlepatti();
        doublePatti = doublepatti();
        triplePatti = triplepatti();

        title.setText(market.replace("_", "").toUpperCase(Locale.ROOT) + ", " + game.toUpperCase(Locale.ROOT));


        if (!game.equals("jodi") && !getIntent().hasExtra("timing")) {
            ArrayList<String> typeof = new ArrayList<>();

            if (open_av.equals("1")) {
                typeof.add("OPEN");
            }
            typeof.add("CLOSE");

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SpDpTp.this, R.layout.simple_list_item_2, typeof);
            type.setAdapter(arrayAdapter);
            type_container.setVisibility(View.VISIBLE);

            if (open_av.equals("0")) {
                selectedType = 1;
                close_game.setTextColor(getResources().getColor(R.color.md_white_1000));
                close_game.setBackgroundColor(getResources().getColor(R.color.primary));
                open_game.setTextColor(getResources().getColor(R.color.font));
                open_game.setBackgroundColor(getResources().getColor(R.color.gray));
            }

        } else {
            //    title.setVisibility(View.GONE);
            type.setVisibility(View.GONE);
            type_container.setVisibility(View.GONE);
        }

        open_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedType = 0;
                open_game.setTextColor(getResources().getColor(R.color.font));
                open_game.setBackgroundColor(getResources().getColor(R.color.primary));
                close_game.setTextColor(getResources().getColor(R.color.font));
                close_game.setBackgroundColor(getResources().getColor(R.color.gray));
                submit.setBackgroundColor(getResources().getColor(R.color.primary));

                if (!open_av.equals("1")) {
                    fillnumber.clear();
                    fillamount.clear();
                    fillmarket.clear();
                    AdapterSingleGames rc = new AdapterSingleGames(SpDpTp.this, fillnumber, fillamount, fillmarket);
                    recyclerview.setLayoutManager(new GridLayoutManager(SpDpTp.this, 1));
                    recyclerview.setAdapter(rc);
                    rc.notifyDataSetChanged();

                    if (fillmarket.size() > 0) {
                        digit_header.setVisibility(View.VISIBLE);
                    } else {
                        digit_header.setVisibility(View.GONE);
                    }

                    submit.setText("Bidding closed");
                    submit.setBackgroundColor(getResources().getColor(R.color.gray));
                }
            }
        });

        close_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedType = 1;
                close_game.setTextColor(getResources().getColor(R.color.font));
                close_game.setBackgroundColor(getResources().getColor(R.color.primary));
                open_game.setTextColor(getResources().getColor(R.color.font));
                open_game.setBackgroundColor(getResources().getColor(R.color.gray));
                submit.setBackgroundColor(getResources().getColor(R.color.primary));
            }
        });


        singlePanna.setOnClickListener(v -> {

            AdapterSingleGames rc = new AdapterSingleGames(SpDpTp.this, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            recyclerview.setLayoutManager(new GridLayoutManager(SpDpTp.this, 1));
            recyclerview.setAdapter(rc);


            fillnumber.clear();
            fillamount.clear();
            fillmarket.clear();


            if (fillmarket.size() > 0) {
                digit_header.setVisibility(View.VISIBLE);
            } else {
                digit_header.setVisibility(View.GONE);
            }

            gameType = 0;

            temp_numbers.clear();
            temp_types.clear();

            singlePanna.setBackgroundColor(getResources().getColor(R.color.primary));
            singlePanna.setTextColor(getResources().getColor(R.color.font));
            doublePanna.setBackgroundColor(getResources().getColor(R.color.gray));
            doublePanna.setTextColor(getResources().getColor(R.color.font));
            triplePanna.setBackgroundColor(getResources().getColor(R.color.gray));
            triplePanna.setTextColor(getResources().getColor(R.color.font));

            for (int a = 0; a < singlePatti.size(); a++){

                int numberx = 0;
                String comapre = "";

                for (int i = 0; i < singlePatti.get(a).length(); i++){
                    numberx += Integer.parseInt(String.valueOf(singlePatti.get(a).charAt(i)));
                }

                if (numberx > 9){
                    comapre = String.valueOf(String.valueOf(numberx).charAt(1));
                } else {
                    comapre = String.valueOf(numberx);
                }

                if (comapre.equals(number.getText().toString())){
                    temp_numbers.add(singlePatti.get(a));
                    temp_types.add("singlepatti");
                }
            }
            adapter_number_circles adapter_number_circles = new adapter_number_circles(SpDpTp.this, temp_numbers);
            sampleRecycler.setLayoutManager(new GridLayoutManager(SpDpTp.this, 5));
            sampleRecycler.setAdapter(adapter_number_circles);

        });


        doublePanna.setOnClickListener(v -> {

            AdapterSingleGames rc = new AdapterSingleGames(SpDpTp.this, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            recyclerview.setLayoutManager(new GridLayoutManager(SpDpTp.this, 1));
            recyclerview.setAdapter(rc);

            fillnumber.clear();
            fillamount.clear();
            fillmarket.clear();



            if (fillmarket.size() > 0) {
                digit_header.setVisibility(View.VISIBLE);
            } else {
                digit_header.setVisibility(View.GONE);
            }

            gameType = 1;

            temp_numbers.clear();
            temp_types.clear();

            doublePanna.setBackgroundColor(getResources().getColor(R.color.primary));
            doublePanna.setTextColor(getResources().getColor(R.color.font));
            singlePanna.setBackgroundColor(getResources().getColor(R.color.gray));
            singlePanna.setTextColor(getResources().getColor(R.color.font));
            triplePanna.setBackgroundColor(getResources().getColor(R.color.gray));
            triplePanna.setTextColor(getResources().getColor(R.color.font));

            for (int a = 0; a < doublePatti.size(); a++){

                int numberx = 0;
                String comapre = "";

                for (int i = 0; i < doublePatti.get(a).length(); i++){
                    numberx += Integer.parseInt(String.valueOf(doublePatti.get(a).charAt(i)));
                }

                if (numberx > 9){
                    comapre = String.valueOf(String.valueOf(numberx).charAt(1));
                } else {
                    comapre = String.valueOf(numberx);
                }

                if (comapre.equals(number.getText().toString())){
                    temp_numbers.add(doublePatti.get(a));
                    temp_types.add("doublepatti");
                }
            }
            adapter_number_circles adapter_number_circles = new adapter_number_circles(SpDpTp.this, temp_numbers);
            sampleRecycler.setLayoutManager(new GridLayoutManager(SpDpTp.this, 5));
            sampleRecycler.setAdapter(adapter_number_circles);

        });



        triplePanna.setOnClickListener(v -> {

            AdapterSingleGames rc = new AdapterSingleGames(SpDpTp.this, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            recyclerview.setLayoutManager(new GridLayoutManager(SpDpTp.this, 1));
            recyclerview.setAdapter(rc);

            fillnumber.clear();
            fillamount.clear();
            fillmarket.clear();



            if (fillmarket.size() > 0) {
                digit_header.setVisibility(View.VISIBLE);
            } else {
                digit_header.setVisibility(View.GONE);
            }


            gameType = 2;

            temp_numbers.clear();
            temp_types.clear();

            triplePanna.setBackgroundColor(getResources().getColor(R.color.primary));
            triplePanna.setTextColor(getResources().getColor(R.color.font));
            singlePanna.setBackgroundColor(getResources().getColor(R.color.gray));
            singlePanna.setTextColor(getResources().getColor(R.color.font));
            doublePanna.setBackgroundColor(getResources().getColor(R.color.gray));
            doublePanna.setTextColor(getResources().getColor(R.color.font));

            for (int a = 0; a < triplePatti.size(); a++){

                int numberx = 0;
                String comapre = "";

                for (int i = 0; i < triplePatti.get(a).length(); i++){
                    numberx += Integer.parseInt(String.valueOf(triplePatti.get(a).charAt(i)));
                }

                if (numberx > 9){
                    comapre = String.valueOf(String.valueOf(numberx).charAt(1));
                } else {
                    comapre = String.valueOf(numberx);
                }

                if (comapre.equals(number.getText().toString())){
                    temp_numbers.add(triplePatti.get(a));
                    temp_types.add("triplepatti");
                }
            }
            adapter_number_circles adapter_number_circles = new adapter_number_circles(SpDpTp.this, temp_numbers);
            sampleRecycler.setLayoutManager(new GridLayoutManager(SpDpTp.this, 5));
            sampleRecycler.setAdapter(adapter_number_circles);

        });



        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty() || s == null) {
                    // DO NOTHING FIELD IS EMPTY
                } else if (Integer.parseInt(s.toString()) > constant.max_single) {
                    amount.setText(constant.max_single + "");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String num = intent.getStringExtra("number");
                fillamount.remove(Integer.parseInt(num));
                fillnumber.remove(Integer.parseInt(num));
                fillmarket.remove(Integer.parseInt(num));

                AdapterSingleGames rc = new AdapterSingleGames(SpDpTp.this, fillnumber, fillamount, fillmarket);
                recyclerview.setLayoutManager(new GridLayoutManager(SpDpTp.this, 1));
                recyclerview.setAdapter(rc);
                rc.notifyDataSetChanged();

                if (fillmarket.size() > 0) {
                    digit_header.setVisibility(View.VISIBLE);
                } else {
                    digit_header.setVisibility(View.GONE);
                }

                total = 0;
                for (int a = 0; a < fillamount.size(); a++) {
                    total = total + Integer.parseInt(fillamount.get(a));
                }
                totalamount.setText(total + "");
            }
        };

        IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");
        registerReceiver(mReceiver, intentFilter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount.getText().toString().isEmpty() || Integer.parseInt(amount.getText().toString()) < constant.min_single) {
                    amount.setError("Enter amount between " + constant.min_single + " - " + constant.max_single);
                } else {

                    for (int ax = 0; ax < temp_numbers.size(); ax++){

                        fillnumber.add(temp_numbers.get(ax));
                        Log.e("amountt1",amount.getText().toString());
                        fillamount.add(amount.getText().toString());
                        if (game.equals("jodi")) {
                            fillmarket.add("");
                        } else {
                            if (selectedType == 0) {
                                fillmarket.add("OPEN");
                            } else {
                                fillmarket.add("CLOSE");
                            }
                        }
                    }


                    AdapterSingleGames rc = new AdapterSingleGames(SpDpTp.this, fillnumber, fillamount, fillmarket);
                    recyclerview.setLayoutManager(new GridLayoutManager(SpDpTp.this, 1));
                    recyclerview.setAdapter(rc);


                    if (fillmarket.size() > 0) {
                        digit_header.setVisibility(View.VISIBLE);
                    } else {
                        digit_header.setVisibility(View.GONE);
                    }

                    total = 0;
                    for (int a = 0; a < fillamount.size(); a++) {
                        total = total + Integer.parseInt(fillamount.get(a));
                    }
                    totalamount.setText(total + "");


                    number.setText("");
                    amount.setText("");

                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fillnumber.size() > 0) {
                    if (total <= Integer.parseInt(prefs.getString("wallet", null))) {
                        numb = "";
                        amou = "";
                        types = "";

                        numb = TextUtils.join(",", fillnumber);
                        amou = TextUtils.join(",", fillamount);
                        types = TextUtils.join(",", fillmarket);


                        apicall();
                    } else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(SpDpTp.this);
                        builder1.setMessage("You don't have enough points");
                        builder1.setCancelable(true);
                        builder1.setNegativeButton(
                                "Close",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }

            }
        });

    }

    @Override
    protected void onResume() {
        balance.setText(getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("wallet", "0"));
        super.onResume();
    }



    private void apicall() {

        progressDialog = new ViewDialog(SpDpTp.this);
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

                            if (jsonObject1.getString("active").equals("0")) {
                                Toast.makeText(SpDpTp.this, "Your account temporarily disabled by admin", Toast.LENGTH_SHORT).show();

                                getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().clear().apply();
                                Intent in = new Intent(getApplicationContext(), login.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                finish();
                            }

                            if (!jsonObject1.getString("session").equals(getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null))) {
                                Toast.makeText(SpDpTp.this, "Session expired ! Please login again", Toast.LENGTH_SHORT).show();

                                getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().clear().apply();
                                Intent in = new Intent(getApplicationContext(), login.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                finish();
                            }

                            if (jsonObject1.getString("success").equalsIgnoreCase("1")) {

                                Intent in = new Intent(getApplicationContext(), thankyou.class);
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
                        Toast.makeText(SpDpTp.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if (gameType == 0) {
                    game = "singlepatti";
                } else if (gameType == 1) {
                    game = "doublepatti";
                } else if (gameType == 2) {
                    game = "triplepatti";
                }
                if (!timing.equals("")){
                    params.put("timing",timing);
                }
                params.put("number", numb);
                params.put("amount", amou);
                params.put("bazar", market);
                params.put("total", total + "");
                params.put("game", game);
                params.put("mobile", prefs.getString("mobile", null));
                params.put("types", types);

                params.put("session", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void initViews() {
        back = findViewById(R.id.back);
        type = findViewById(R.id.type);
        number = findViewById(R.id.number);
        amount = findViewById(R.id.amount);
        add = findViewById(R.id.add);
        recyclerview = findViewById(R.id.recyclerview);
        totalamount = findViewById(R.id.totalamount);
        submit = findViewById(R.id.submit);
        title = findViewById(R.id.title);
        balance = findViewById(R.id.balance);
        screenTitle = findViewById(R.id.title);
        open_game = findViewById(R.id.open_game);
        close_game = findViewById(R.id.close_game);
        type_container = findViewById(R.id.type_container);
        digit_header = findViewById(R.id.digit_header);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        coin = findViewById(R.id.coin);
        walletView = findViewById(R.id.wallet_view);
        toolbar = findViewById(R.id.toolbar);
        singlePanna = findViewById(R.id.single_panna);
        doublePanna = findViewById(R.id.double_panna);
        triplePanna = findViewById(R.id.triple_panna);
        spdptpPanel = findViewById(R.id.spdptp_panel);
        scrollView = findViewById(R.id.scrollView);
        sampleRecycler = findViewById(R.id.sample_recycler);
    }



    public ArrayList<String> doublepatti() {
        ArrayList<String> number = new ArrayList<>();
        number.add("100");
        number.add("119");
        number.add("155");
        number.add("227");
        number.add("335");
        number.add("344");
        number.add("399");
        number.add("588");
        number.add("669");
        number.add("200");
        number.add("110");
        number.add("228");
        number.add("255");
        number.add("336");
        number.add("499");
        number.add("660");
        number.add("688");
        number.add("778");
        number.add("300");
        number.add("166");
        number.add("229");
        number.add("337");
        number.add("355");
        number.add("445");
        number.add("599");
        number.add("779");
        number.add("788");
        number.add("400");
        number.add("112");
        number.add("220");
        number.add("266");
        number.add("338");
        number.add("446");
        number.add("455");
        number.add("699");
        number.add("770");
        number.add("500");
        number.add("113");
        number.add("122");
        number.add("177");
        number.add("339");
        number.add("366");
        number.add("447");
        number.add("799");
        number.add("889");
        number.add("600");
        number.add("114");
        number.add("277");
        number.add("330");
        number.add("448");
        number.add("466");
        number.add("556");
        number.add("880");
        number.add("899");
        number.add("700");
        number.add("115");
        number.add("133");
        number.add("188");
        number.add("223");
        number.add("377");
        number.add("449");
        number.add("557");
        number.add("566");
        number.add("800");
        number.add("116");
        number.add("224");
        number.add("233");
        number.add("288");
        number.add("440");
        number.add("477");
        number.add("558");
        number.add("990");
        number.add("900");
        number.add("117");
        number.add("144");
        number.add("199");
        number.add("225");
        number.add("388");
        number.add("559");
        number.add("577");
        number.add("667");
        number.add("550");
        number.add("668");
        number.add("244");
        number.add("299");
        number.add("226");
        number.add("488");
        number.add("677");
        number.add("118");
        number.add("334");

        return number;
    }

    public ArrayList<String> singlepatti() {
        ArrayList<String> number = new ArrayList<>();
        number.add("128");
        number.add("137");
        number.add("146");
        number.add("236");
        number.add("245");
        number.add("290");
        number.add("380");
        number.add("470");
        number.add("489");
        number.add("560");
        number.add("678");
        number.add("579");
        number.add("589");
        number.add("129");
        number.add("138");
        number.add("147");
        number.add("156");
        number.add("237");
        number.add("246");
        number.add("345");
        number.add("390");
        number.add("480");
        number.add("570");
        number.add("679");
        number.add("120");
        number.add("139");
        number.add("148");
        number.add("157");
        number.add("238");
        number.add("247");
        number.add("256");
        number.add("346");
        number.add("490");
        number.add("580");
        number.add("670");
        number.add("689");
        number.add("130");
        number.add("149");
        number.add("158");
        number.add("167");
        number.add("239");
        number.add("248");
        number.add("257");
        number.add("347");
        number.add("356");
        number.add("590");
        number.add("680");
        number.add("789");
        number.add("140");
        number.add("159");
        number.add("168");
        number.add("230");
        number.add("249");
        number.add("258");
        number.add("267");
        number.add("348");
        number.add("357");
        number.add("456");
        number.add("690");
        number.add("780");
        number.add("123");
        number.add("150");
        number.add("169");
        number.add("178");
        number.add("240");
        number.add("259");
        number.add("268");
        number.add("349");
        number.add("358");
        number.add("457");
        number.add("367");
        number.add("790");
        number.add("124");
        number.add("160");
        number.add("179");
        number.add("250");
        number.add("269");
        number.add("278");
        number.add("340");
        number.add("359");
        number.add("368");
        number.add("458");
        number.add("467");
        number.add("890");
        number.add("125");
        number.add("134");
        number.add("170");
        number.add("189");
        number.add("260");
        number.add("279");
        number.add("350");
        number.add("369");
        number.add("378");
        number.add("459");
        number.add("567");
        number.add("468");
        number.add("126");
        number.add("135");
        number.add("180");
        number.add("234");
        number.add("270");
        number.add("289");
        number.add("360");
        number.add("379");
        number.add("450");
        number.add("469");
        number.add("478");
        number.add("568");
        number.add("127");
        number.add("136");
        number.add("145");
        number.add("190");
        number.add("235");
        number.add("280");
        number.add("370");
        number.add("479");
        number.add("460");
        number.add("569");
        number.add("389");
        number.add("578");

        return number;
    }



    public ArrayList<String> triplepatti() {

        ArrayList<String> number = new ArrayList<>();
        number.add("000");
        number.add("111");
        number.add("222");
        number.add("333");
        number.add("444");
        number.add("555");
        number.add("666");
        number.add("777");
        number.add("888");
        number.add("999");

        return number;
    }
}