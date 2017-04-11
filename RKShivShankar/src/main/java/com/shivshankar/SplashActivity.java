package com.shivshankar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.customcontrols.JumpBall;
import com.shivshankar.utills.AlertDialogManager;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.json.JSONObject;

@SuppressLint("NewApi")
public class SplashActivity extends AppCompatActivity implements OnResult {
    Animation animation;
    String versionName = "";
    int versionCode = 0;
    LinearLayout mFl_whole;
    Snackbar snack;
    boolean isVersioningDone = false, isTimerOut = false;
    JumpBall mJumpBall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

            if (commonMethods.knowInternetOn(this)) {
                Log.e("TAGRK", "ReId:!" + FirebaseInstanceId.getInstance().getToken());
            } else {
                Log.d("TAGRK", "Internet Problem!");
            }

            setContentView(R.layout.activity_splash_screen);
            bindview();

            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                versionName = pInfo.versionName;
                versionCode = pInfo.versionCode;
            } catch (Exception e) {
                e.printStackTrace();
            }

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    isTimerOut = true;
                    callMainActivity();
                }
            }, 2000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        try {
            mJumpBall.pause();
            super.onPause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        try {
            FirebaseInstanceId.getInstance().getToken();
            if (mJumpBall != null)
                mJumpBall.start();
            if (!commonMethods.knowInternetOn(this)) {
                snack = Snackbar.make(mFl_whole, R.string.no_internet, Snackbar.LENGTH_INDEFINITE);
                View view = snack.getView();
                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.WHITE);
                snack.show();
            } else if (!isVersioningDone)
                APIs.CheckAppVersion(this, BuildConfig.VERSION_CODE + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, 0);
    }

    private void bindview() {
        animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
        mFl_whole = (LinearLayout) findViewById(R.id.fl_whole);
        mJumpBall = (JumpBall) findViewById(R.id.jump_ball);
    }

    private void callMainActivity() {
        if (isVersioningDone && isTimerOut) {
            mJumpBall.finish();
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (AppPreferences.getPrefs().getBoolean(commonVariables.KEY_IS_LOG_IN, false)) {
                    if (AppPreferences.getPrefs().getBoolean(commonVariables.KEY_IS_SELLER, false))
                        startActivity(new Intent(SplashActivity.this, MainActivitySeller.class));
                    else
                        startActivity(new Intent(SplashActivity.this, MainActivityBuyer.class));
                } else {
                    if (AppPreferences.getPrefs().getBoolean(commonVariables.KEY_IS_SKIPPED_LOGIN_BUYER, false))
                        startActivity(new Intent(SplashActivity.this, MainActivityBuyer.class));
                    else
                        startActivity(new Intent(SplashActivity.this, LoginRegisterActivity.class));
                }
                finish();
                overridePendingTransition(0, 0);
            }, 600);
        }
    }

    @Override
    public void onResult(JSONObject jobj) {

        try {

            if (jobj != null) {
                String strApiName = jobj.optString("api");
                if (strApiName.equalsIgnoreCase("CheckAppVersion")) {
                    AppPreferences.getPrefs().edit().putInt(commonVariables.KEY_NOTI_COUNT, jobj.optInt("sellerNotificationCount")).apply();
                    switch (jobj.optInt("resInt")) {
                        case 0:
                            showNotCompatibleDialog(false);
                            break;
                        case 1:
                            isVersioningDone = true;
                            callMainActivity();
                            break;
                        case 2:
                            showNotCompatibleDialog(true);
                            break;
                    }
                }
            } else {
                AlertDialogManager.showDialog(this, "Sorry! Some error occurred, Please try again latter.", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showNotCompatibleDialog(boolean shouldEnterApp) {

        try {
            String strMessage = "A lot have been changed after this version, To continue using " + commonVariables.appname + ", Please update app from playstore";
            if (shouldEnterApp)
                strMessage = "A lot have been changed after this version, Please update app from playstore for more functionality and better user experience";

            Runnable listenerPos = () -> {
                final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                finishAffinity();
                overridePendingTransition(0, 0);
            };


            Runnable listenerNeg = () -> {
                if (shouldEnterApp) {
                    isVersioningDone = true;
                    callMainActivity();
                } else
                    finishAffinity();
            };
            AlertDialogManager.showDialogCustom(this, strMessage, "Update", "Cancel", listenerPos, listenerNeg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}