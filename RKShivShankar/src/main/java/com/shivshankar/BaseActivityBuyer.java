package com.shivshankar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;


public class BaseActivityBuyer extends AppCompatActivity {

    protected FrameLayout frameLayout;
    Toolbar toolbar;
    DrawerLayout drawer;
    SwipeRefreshLayout swipeRefreshLayout;
    CoordinatorLayout main_content;
    LinearLayout mNav_my_profile, mNav_my_orders, mNav_customer_service, mNav_about_us, mNav_our_policy, mNav_contact_us, mLl_close;

    ImageView mIv_logo_nav, mIv_logo_toolbar;
    TextView mTv_noti_count, mTv_cart_count, mTv_username, mTv_logout;
    View v_order, v_prof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_base_buyer);

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
            mNav_my_orders = (LinearLayout) findViewById(R.id.nav_my_orders);
            mNav_customer_service = (LinearLayout) findViewById(R.id.nav_customer_service);
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
                mTv_logout.setVisibility(View.GONE);
                mNav_my_profile.setVisibility(View.GONE);
                mNav_my_orders.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    Intent intent = new Intent(this, MainActivityBuyer.class);
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
            if (mTv_cart_count != null && !commonMethods.isOnline(this)) {
                Snackbar.make(mTv_cart_count, getString(R.string.no_internet), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        try {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.main_buyer, menu);
            final View menu_layout = menu.findItem(R.id.action_notifications).getActionView();
            mTv_noti_count = (TextView) menu_layout.findViewById(R.id.tv_cart_count);

            updateHotCount(mTv_noti_count, 3);
            commonMethods.cartCountAnimation(this, mTv_noti_count);
            new BaseActivitySeller.MyMenuItemStuffListener(menu_layout, "Show hot message") {
                @Override
                public void onClick(View view) {
                    commonMethods.cartCountAnimation(BaseActivityBuyer.this, mTv_noti_count);
                    startActivity(new Intent(BaseActivityBuyer.this, NotificationsActivitySeller.class));
                    overridePendingTransition(0, 0);
                }
            };

            final View cart_layout = menu.findItem(R.id.action_notifications).getActionView();
            ((ImageView) cart_layout.findViewById(R.id.iv_icon)).setImageResource(R.drawable.ic_cart);
            mTv_cart_count = (TextView) cart_layout.findViewById(R.id.tv_cart_count);

//            updateHotCount(mTv_cart_count, 3);
//            commonMethods.cartCountAnimation(this, mTv_cart_count);
            new BaseActivitySeller.MyMenuItemStuffListener(cart_layout, "Show hot message") {
                @Override
                public void onClick(View view) {
                    commonMethods.cartCountAnimation(BaseActivityBuyer.this, mTv_cart_count);
                    startActivity(new Intent(BaseActivityBuyer.this, NotificationsActivitySeller.class));
                    overridePendingTransition(0, 0);
                }
            };

        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }

    // so we can call this asynchronously
    public void updateHotCount(TextView mTv_cart_count, final int count) {
        try {
            if (mTv_cart_count == null) return;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (count > 0) {
                            mTv_cart_count.setVisibility(View.VISIBLE);
                            mTv_cart_count.setText(count + "");
                        } else
                            mTv_cart_count.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
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

