package com.shivshankar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class BaseActivitySeller extends AppCompatActivity {
    protected FrameLayout frameLayout;

    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    DrawerLayout drawer;
    CoordinatorLayout main_content;
    LinearLayout mNav_my_profile, mNav_my_products, mNav_notification, mNav_change_pass, mLl_close;
    TextView mTv_cart_count, mTv_username, mTv_logout;
    ImageView mIv_logo_nav, mIv_logo_toolbar;
    File file = null;

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
            menuInflater.inflate(R.menu.main, menu);
            final View menu_layout = menu.findItem(R.id.action_notifications).getActionView();
            mTv_cart_count = (TextView) menu_layout.findViewById(R.id.tv_cart_count);

            updateHotCount(3);
            commonMethods.cartCountAnimation(this, mTv_cart_count);
            new MyMenuItemStuffListener(menu_layout, "Show hot message") {
                @Override
                public void onClick(View view) {
                    commonMethods.cartCountAnimation(BaseActivitySeller.this, mTv_cart_count);
                    startActivity(new Intent(BaseActivitySeller.this, NotificationsActivitySeller.class));
                    overridePendingTransition(0, 0);
                }
            };

        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }

    // so we can call this asynchronously
    public void updateHotCount(final int count) {
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

//test mayura
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

    static abstract class MyMenuItemStuffListener implements View.OnClickListener, View.OnLongClickListener {
        private String hint;
        private View view;

        MyMenuItemStuffListener(View view, String hint) {
            this.view = view;
            this.hint = hint;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        abstract public void onClick(View view);

        @Override
        public boolean onLongClick(View v) {
            try {
                final int[] screenPos = new int[2];
                final Rect displayFrame = new Rect();
                view.getLocationOnScreen(screenPos);
                view.getWindowVisibleDisplayFrame(displayFrame);
                final Context context = view.getContext();
                final int width = view.getWidth();
                final int height = view.getHeight();
                final int midy = screenPos[1] + height / 2;
                final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
                Toast cheatSheet = Toast.makeText(context, hint, Toast.LENGTH_SHORT);
                if (midy < displayFrame.height()) {
                    cheatSheet.setGravity(Gravity.TOP | Gravity.RIGHT,
                            screenWidth - screenPos[0] - width / 2, height);
                } else {
                    cheatSheet.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, height);
                }
                cheatSheet.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    }
}
