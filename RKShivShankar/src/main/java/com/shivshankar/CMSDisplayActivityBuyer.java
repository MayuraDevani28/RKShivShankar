package com.shivshankar;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.commonVariables;

@SuppressLint("NewApi")
public class CMSDisplayActivityBuyer extends BaseActivityBuyer implements View.OnClickListener {

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
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
            }
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);
            View rootView = getLayoutInflater().inflate(R.layout.activity_cms_call_and_display, frameLayout);

            strTitle = getIntent().getStringExtra(commonVariables.INTENT_EXTRA_PAGE_NAME);
            String strContent = getIntent().getStringExtra(commonVariables.INTENT_EXTRA_PAGE);
            bindViews(rootView);

            mTv_title.setText(strTitle);
            String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/NOTOSANS.TTF\")}body {font-family: MyFont;font-size:13px;text-align: justify;}</style></head><body>";
            String pas = "</body></html>";
//            strContent = pish + strContent.replace("\r\n", "<br/>") + pas;
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
    private void bindViews(View rootView) {
        try {
            mTv_title = (TextView) rootView.findViewById(R.id.tv_title);
            mWv_iframe = (WebView) rootView.findViewById(R.id.wv_iframe);
            mIv_close = (ImageView) rootView.findViewById(R.id.iv_close);
            mIv_close.setOnClickListener(this);

            WebSettings webViewSettings = mWv_iframe.getSettings();
            webViewSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webViewSettings.setJavaScriptEnabled(true);
            webViewSettings.setBuiltInZoomControls(true);
            webViewSettings.setPluginState(PluginState.ON);
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
    public void onClick(View view) {
        AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
        view.startAnimation(buttonClick);
        if (view == mIv_close) {
            onBackPressed();
        } else
            super.onClick(view);
    }


}
