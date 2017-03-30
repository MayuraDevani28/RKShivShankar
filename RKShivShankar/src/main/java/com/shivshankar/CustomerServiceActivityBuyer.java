package com.shivshankar;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.shivshankar.utills.ExceptionHandler;

public class CustomerServiceActivityBuyer extends AppCompatActivity implements View.OnClickListener {

    TextView mTv_title;
    private WebView mWv_iframe;
    ImageView mIv_close;
    String strTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            Window window = getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
            setContentView(R.layout.activity_about_us);
//            strTitle = getIntent().getStringExtra(commonVariables.INTENT_EXTRA_PAGE_NAME);
            String strContent ="";// getIntent().getStringExtra(commonVariables.INTENT_EXTRA_PAGE);
            bindViews();

            mTv_title.setText(strTitle);
            String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/ProximaNova-Reg.otf\")}body {font-family: MyFont;font-size:13px;text-align: justify;}</style></head><body>";
            String pas = "</body></html>";
            strContent = pish + strContent.replace("\r\n", "<br/>") + pas;
            strContent = pish + strContent + pas;
            mWv_iframe.loadData(strContent, "text/html", "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    onBackPressed();
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    private void bindViews() {
        try {
            mTv_title = (TextView) findViewById(R.id.tv_title);
            mWv_iframe = (WebView) findViewById(R.id.wv_iframe);
            mIv_close = (ImageView) findViewById(R.id.iv_close);
            mIv_close.setOnClickListener(this);

            WebSettings webViewSettings = mWv_iframe.getSettings();
            webViewSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webViewSettings.setJavaScriptEnabled(true);
            webViewSettings.setBuiltInZoomControls(true);
            webViewSettings.setPluginState(WebSettings.PluginState.ON);
            webViewSettings.setBuiltInZoomControls(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            finish();
            overridePendingTransition(0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mIv_close) {
            onBackPressed();
        }
    }


}
