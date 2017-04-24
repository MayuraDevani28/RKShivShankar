package com.shivshankar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shivshankar.customcontrols.NavDrawerViewSeller;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import java.io.File;

public class BaseActivitySeller extends AppCompatActivity implements View.OnClickListener {
    protected FrameLayout frameLayout;

    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    public DrawerLayout drawer;
    CoordinatorLayout main_content;
    ImageView mIv_logo_toolbar;
    TextView mTv_noti_count;

    NavDrawerViewSeller navigationView_seller;
    NavigationView navView;

    File file = null;
    View menu_layout;

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

            setupViews();
            swipeRefreshLayout.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupViews() {
        try {
            navigationView_seller = new NavDrawerViewSeller(this);
            navView.addView(navigationView_seller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        navigationView_seller.setUserName();
        super.onResume();
    }

    private void bindViews() {
        try {

            frameLayout = (FrameLayout) findViewById(R.id.container);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            main_content = (CoordinatorLayout) findViewById(R.id.main_content);
            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

            navView = (NavigationView) findViewById(R.id.navigation_drawer_container);
            mIv_logo_toolbar = (ImageView) findViewById(R.id.iv_logo_toolbar);
            mIv_logo_toolbar.setOnClickListener(this);

        } catch (Exception e) {
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
    public boolean onCreateOptionsMenu(final Menu menu) {
        try {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.main, menu);
            menu_layout = menu.findItem(R.id.action_notifications).getActionView();
            mTv_noti_count = (TextView) menu_layout.findViewById(R.id.tv_cart_count);

            commonMethods.cartCountAnimation(this, mTv_noti_count);
            new MyMenuItemStuffListener(menu_layout, "Show hot message") {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(BaseActivitySeller.this, NotificationsActivitySeller.class));
                    overridePendingTransition(0, 0);
                }
            };
            setNotiCount();
            Log.v("TAGRK", "OncreatedOptionsmenu base seller");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void setNotiCount() {
        updateCount(AppPreferences.getPrefs().getInt(commonVariables.KEY_NOTI_COUNT, 0));

    }

    public void updateCount(final int count) {
        try {
            if (mTv_noti_count == null) return;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mTv_noti_count.setText(count + "");
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

    @Override
    public void onClick(View view) {
        try {
            if (view == mIv_logo_toolbar) {
                Intent intent = new Intent(this, MainActivitySeller.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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
