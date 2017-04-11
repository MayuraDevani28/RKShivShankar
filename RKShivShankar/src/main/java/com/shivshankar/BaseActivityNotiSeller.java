package com.shivshankar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.commonVariables;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class BaseActivityNotiSeller extends AppCompatActivity {
    protected FrameLayout frameLayout;

    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    DrawerLayout drawer;
    CoordinatorLayout main_content;
    LinearLayout mNav_my_profile, mNav_my_products, mNav_notification, mNav_change_pass, mNav_my_orders, mLl_close;
    TextView mTv_username, mTv_logout;
    ImageView mIv_logo_nav, mIv_logo_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_base_seller);

            bindViews();
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setLogo(null);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            toggle.setDrawerIndicatorEnabled(false);
            toolbar.setNavigationIcon(R.drawable.ic_menu);
            toolbar.setNavigationOnClickListener(view -> drawer.openDrawer(GravityCompat.START));
            drawer.addDrawerListener(toggle);
            toggle.syncState();


            swipeRefreshLayout.setEnabled(false);
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

            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

            mNav_my_profile = (LinearLayout) findViewById(R.id.nav_my_profile);
            mNav_my_products = (LinearLayout) findViewById(R.id.nav_my_products);
            mNav_notification = (LinearLayout) findViewById(R.id.nav_notification);
            mNav_my_orders = (LinearLayout) findViewById(R.id.nav_my_orders);
            mNav_change_pass = (LinearLayout) findViewById(R.id.nav_change_pass);

            mIv_logo_nav = (ImageView) findViewById(R.id.iv_logo_nav);
            mIv_logo_toolbar = (ImageView) findViewById(R.id.iv_logo_toolbar);
            mTv_username = (TextView) findViewById(R.id.tv_username);
            mTv_logout = (TextView) findViewById(R.id.tv_logout);
            mLl_close = (LinearLayout) findViewById(R.id.ll_close);

            setUserName();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUserName() {
        try {
            String strProfile = AppPreferences.getPrefs().getString(commonVariables.KEY_SELLER_PROFILE, "");
            if (!strProfile.isEmpty() && !strProfile.equalsIgnoreCase("null")) {
                String strUname = WordUtils.capitalizeFully(new JSONObject(strProfile).optString("Name"));
                mTv_username.setText(strUname);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    Intent intent = new Intent(this, MainActivitySeller.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            if (mTv_username != null) {
                setUserName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        try {
            if (AppPreferences.getPrefs().getInt(commonVariables.KEY_NOTI_COUNT, 0) != 0) {
                MenuInflater menuInflater = getMenuInflater();
                menuInflater.inflate(R.menu.cart, menu);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
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
