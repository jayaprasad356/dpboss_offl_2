package com.betplay.dpboss_off;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import im.delight.android.webview.AdvancedWebView;

public class charts extends AppCompatActivity implements AdvancedWebView.Listener {

    private AdvancedWebView mWebView;

    ViewDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        initViews();

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        progressDialog = new ViewDialog(charts.this);
        progressDialog.showDialog();

        mWebView = (AdvancedWebView) findViewById(R.id.webview);
        mWebView.setListener(this, this);
        mWebView.setMixedContentAllowed(true);
        if (getIntent().getStringExtra("href").contains("http")){
            Log.e("chart_url - ",constant.prefix+getIntent().getStringExtra("href"));
            mWebView.loadUrl(getIntent().getStringExtra("href"));
        } else if (getIntent().getStringExtra("href").contains("manual://")) {
            Log.e("chart_url2 - ",constant.prefix+"/chart/"+getIntent().getStringExtra("href").replace("manual://","").replace(" ","%20"));
            mWebView.loadUrl(constant.prefix+"/chart/"+getIntent().getStringExtra("href").replace("manual://","").replace(" ","%20"));
        } else
        {
            Log.e("chart_url2 - ",constant.prefix+"chart_single.php?url="+getIntent().getStringExtra("href"));
            mWebView.loadUrl(constant.prefix+"chart_single.php?url="+getIntent().getStringExtra("href"));
        }


    }

    private void initViews() {
        mWebView = findViewById(R.id.webview);
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
    }

    @Override
    public void onPageFinished(String url) {
        progressDialog.hideDialog();
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }
}