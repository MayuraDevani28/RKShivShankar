package com.shivshankar;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;


public class BaseActivityCartBuyer extends AppCompatActivity {

    protected FrameLayout frameLayout;
    Toolbar toolbar;
    DrawerLayout drawer;
    CoordinatorLayout main_content;
    LinearLayout mNav_my_profile, mNav_my_orders, mNav_about_us, mNav_our_policy, mNav_contact_us, mLl_close;

    ImageView mIv_logo_nav, mIv_logo_toolbar;
    TextView mTv_username, mTv_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_base_cart);

            bindViews();
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setLogo(null);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void bindViews() {
        try {
            frameLayout = (FrameLayout) findViewById(R.id.container);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            main_content = (CoordinatorLayout) findViewById(R.id.main_content);

            mNav_my_profile = (LinearLayout) findViewById(R.id.nav_my_profile);
            mNav_my_orders = (LinearLayout) findViewById(R.id.nav_my_orders);
            mNav_about_us = (LinearLayout) findViewById(R.id.nav_about_us);
            mNav_our_policy = (LinearLayout) findViewById(R.id.nav_our_policy);
            mNav_contact_us = (LinearLayout) findViewById(R.id.nav_contact_us);

            mIv_logo_nav = (ImageView) findViewById(R.id.iv_logo_nav);
            mIv_logo_toolbar = (ImageView) findViewById(R.id.iv_logo_toolbar);
            mTv_username = (TextView) findViewById(R.id.tv_username);
            mTv_logout = (TextView) findViewById(R.id.tv_logout);
            mLl_close = (LinearLayout) findViewById(R.id.ll_close);

            if (!AppPreferences.getPrefs().getBoolean(commonVariables.KEY_IS_LOG_IN, false)) {
                findViewById(R.id.v_order).setVisibility(View.GONE);
                findViewById(R.id.v_prof).setVisibility(View.GONE);
                mTv_logout.setText("Login");
                mNav_my_profile.setVisibility(View.GONE);
                mNav_my_orders.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            if (mNav_my_profile != null && !commonMethods.isOnline(this)) {
                Snackbar.make(mNav_my_profile, getString(R.string.no_internet), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        try {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                finish();
                overridePendingTransition(0, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

