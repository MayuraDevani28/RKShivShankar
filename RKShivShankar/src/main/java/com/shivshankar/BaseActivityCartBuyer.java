package com.shivshankar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.shivshankar.customcontrols.NavDrawerViewBuyer;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.commonMethods;


public class BaseActivityCartBuyer extends AppCompatActivity implements View.OnClickListener {

    protected FrameLayout frameLayout;
    Toolbar toolbar;
    public DrawerLayout drawer;
    CoordinatorLayout main_content;
    ImageView mIv_logo_toolbar;

    NavDrawerViewBuyer navigationView_buyer;
    NavigationView navView;

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

            setupViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            if (mIv_logo_toolbar != null && !commonMethods.isOnline(this)) {
                Snackbar.make(mIv_logo_toolbar, getString(R.string.no_internet), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
            navigationView_buyer.setLoginLogout();
            navigationView_buyer.setUserName();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupViews() {
        try {
            navigationView_buyer = new NavDrawerViewBuyer(this);
            navView.addView(navigationView_buyer);
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
            navView = (NavigationView) findViewById(R.id.navigation_drawer_container);

            mIv_logo_toolbar = (ImageView) findViewById(R.id.iv_logo_toolbar);
            mIv_logo_toolbar.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mIv_logo_toolbar) {
            Intent intent = new Intent(this, MainActivityBuyer.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(0, 0);
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

