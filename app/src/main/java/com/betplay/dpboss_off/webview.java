package com.betplay.dpboss_off;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import im.delight.android.webview.AdvancedWebView;

public class webview extends AppCompatActivity implements AdvancedWebView.Listener {

    AdvancedWebView mWebView;
    String gateway = "0", amount = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        amount = getIntent().getStringExtra("amount");
        gateway = getIntent().getStringExtra("gateway");

        mWebView = (AdvancedWebView) findViewById(R.id.webview);
        mWebView.setListener(this, this);
        mWebView.setMixedContentAllowed(false);

        if (gateway.equals("paytm")){
            mWebView.loadUrl(constant.project_root+"paytm/pgRedirect.php?amount="+amount+"&user="+getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("mobile", null));
        } else {
            mWebView.loadUrl(constant.project_root+"razorpay/pay.php?amount="+amount+"&user="+getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("mobile", null));
        }

    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

        if (url.contains("failed.com")){
            Toast.makeText(this, "Payment not completed", Toast.LENGTH_SHORT).show();
            finish();
        } else if (url.contains("success.com")){
            Toast.makeText(this, "Payment completed", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }
}