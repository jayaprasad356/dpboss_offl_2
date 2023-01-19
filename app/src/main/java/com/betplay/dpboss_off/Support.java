package com.betplay.dpboss_off;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Support extends AppCompatActivity {

    private CardView back;
    private CardView toolbar;
    private latobold submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        initViews();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        submit.setOnClickListener(v -> {
            String url = constant.getWhatsapp(getApplicationContext());

            Uri uri = Uri.parse(url);
            Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(sendIntent);
        });
    }

    private void initViews() {
        back = findViewById(R.id.back);
        toolbar = findViewById(R.id.toolbar);
        submit = findViewById(R.id.submit);
    }
}