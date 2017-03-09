package com.shivshankar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.commonMethods;

public class BaseActivitySeller extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public TextView mTv_cart_count;
    protected FrameLayout frameLayout;
    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_base_seller);
            frameLayout = (FrameLayout) findViewById(R.id.container);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setLogo(R.drawable.ic_logo);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            toggle.setDrawerIndicatorEnabled(false);
            toolbar.setNavigationIcon(R.drawable.ic_menu);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawer.openDrawer(Gravity.LEFT);
                }
            });
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        try {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.main, menu);
            final View menu_layout = menu.findItem(R.id.action_settings).getActionView();
            mTv_cart_count = (TextView) menu_layout.findViewById(R.id.tv_cart_count);

            updateHotCount(3);
            commonMethods.cartCountAnimation(this, mTv_cart_count);
            new MyMenuItemStuffListener(menu_layout, "Show hot message") {
                @Override
                public void onClick(View v) {
                    commonMethods.cartCountAnimation(BaseActivitySeller.this, mTv_cart_count);
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


    @Override
    public void onBackPressed() {

        try {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id == R.id.nav_camera) {
            } else if (id == R.id.nav_gallery) {
            } else if (id == R.id.nav_slideshow) {
            } else if (id == R.id.nav_manage) {
            } else if (id == R.id.nav_share) {
            } else if (id == R.id.nav_send) {
            }
            drawer.closeDrawer(GravityCompat.START);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
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
        abstract public void onClick(View v);

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
