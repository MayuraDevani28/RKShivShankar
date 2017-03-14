package com.shivshankar;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.shivshankar.ServerCall.APIs;
import com.shivshankar.adapters.NotificationListAdapter;
import com.shivshankar.classes.NavigationItem;
import com.shivshankar.customcontrols.SwipeDismissListViewTouchListener;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivitySeller extends BaseActivitySeller implements OnResult, View.OnClickListener, AdapterView.OnItemClickListener {
    boolean mAlreadyLoaded = false;
    ListView mLv_notification;
    TextView mTv_no_data_found;

    private ImageView mIv_logo_nav, mIv_logo_toolbar;
    private TextView mTv_username, mTv_logout;
    private LinearLayout mNav_my_profile, mNav_my_products, mNav_notification, mNav_change_pass, mLl_close;

    NotificationListAdapter mAdapter;
    String strLoginId = "0";
    //    SwipeRefreshLayout swipeRefreshLayout;
    List<NavigationItem> listNavigationItems = new ArrayList<NavigationItem>();
    ImageView mIv_close;
    boolean unDoClicked = false;
    CoordinatorLayout coordinatorLayout;

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
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            View rootView = getLayoutInflater().inflate(R.layout.activity_notification_seller, frameLayout);
            bindViews(rootView);

            strLoginId = AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "");
            callGetNotificationsAPI(this, strLoginId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callRemoveNotificationsAPI(String strLoginId, String notificationCustBindId) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/RemoveNotifications")
                .appendQueryParameter("loginId", strLoginId)
                .appendQueryParameter("notificationCustBindId", notificationCustBindId)
                .build();
        String query = uri.toString();
        Log.v("TAGRK", "Calling With:" + query);
//        new ServerAPICAll(null, this).execute(query);
        APIs.callAPI(null, this, query);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, 0);
    }

    private void callGetNotificationsAPI(AppCompatActivity activity, String userId2) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/GetNotifications")
                .appendQueryParameter("loginId", userId2)
                .build();
        String query = uri.toString();
        Log.v("TAGRK", "Calling With:" + query);
        APIs.callAPI(null, this, query);
    }

    private void bindViews(View rootView) {
        try {
            mIv_logo_nav = (ImageView) findViewById(R.id.iv_logo_nav);
            mIv_logo_nav.setOnClickListener(this);
            mIv_logo_toolbar = (ImageView) findViewById(R.id.iv_logo_toolbar);
            mIv_logo_toolbar.setOnClickListener(this);
            mTv_username = (TextView) findViewById(R.id.tv_username);
            mTv_logout = (TextView) findViewById(R.id.tv_logout);
            mTv_logout.setOnClickListener(this);
            mLl_close = (LinearLayout) findViewById(R.id.ll_close);
            mLl_close.setOnClickListener(this);

            mNav_my_profile = (LinearLayout) findViewById(R.id.nav_my_profile);
            mNav_my_profile.setOnClickListener(this);
            mNav_my_products = (LinearLayout) findViewById(R.id.nav_my_products);
            mNav_my_products.setOnClickListener(this);
            mNav_notification = (LinearLayout) findViewById(R.id.nav_notification);
            mNav_notification.setOnClickListener(this);
            mNav_change_pass = (LinearLayout) findViewById(R.id.nav_change_pass);
            mNav_change_pass.setOnClickListener(this);

            mIv_close = (ImageView) rootView.findViewById(R.id.iv_close);
            mIv_close.setOnClickListener(this);
            mLv_notification = (ListView) rootView.findViewById(R.id.ll_notification);
            mLv_notification.setOnItemClickListener(this);
            mTv_no_data_found = (TextView) rootView.findViewById(R.id.tv_no_data_found);
//        swipeRefreshLayout = (SwipeRefreshLayout)  rootView.findViewById(R.id.swipe_refresh_layout);
//        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light, android.R.color.holo_purple, android.R.color.holo_blue_light);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//
//            @Override
//            public void onRefresh() {
//                callGetNotificationsAPI(null, strLoginId);
//            }
//        });
//        mLv_notification.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//
//            @Override
//            public void onScrollChanged() {
//                boolean scrollY = mLv_notification.getFirstVisiblePosition() > 0 ||
//                        mLv_notification.getChildAt(0) == null ||
//                        mLv_notification.getChildAt(0).getTop() < 0;
//                if (scrollY)
//                    swipeRefreshLayout.setEnabled(false);
//                else
//                    swipeRefreshLayout.setEnabled(true);
//            }
//        });

            coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);
            SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(mLv_notification, new SwipeDismissListViewTouchListener.DismissCallbacks() {
                @Override
                public boolean canDismiss(int position) {
                    return true;
                }

                @Override
                public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                    unDoClicked = false;
                    try {
                        for (final int position : reverseSortedPositions) {
                            final NavigationItem item = mAdapter.getItem(position);
                            mAdapter.remove(item);
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Notification's gone !", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    unDoClicked = true;
                                    item.setUnDoClicked(true);
                                    mAdapter.insert(item, position);
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                            snackbar.setCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    try {
                                        if (!unDoClicked)
                                            callRemoveNotificationsAPI(strLoginId, item.getNotificationCustBindId());
                                        //                                    super.onDismissed(snackbar, event);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            // Changing message text color
                            snackbar.setActionTextColor(Color.WHITE);

                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);

                            snackbar.show();
                        }

                        mAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            if (Build.VERSION.SDK_INT >= 10) {
                mLv_notification.setOnTouchListener(touchListener);
                mLv_notification.setOnScrollListener(touchListener.makeScrollListener());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setNotificationAdapter(List<NavigationItem> listNavigationItems) {
        mAdapter = new NotificationListAdapter(this, R.layout.adapter_row_notification, listNavigationItems);
        mLv_notification.setAdapter(mAdapter);
    }

    @Override
    public void onResult(JSONObject jobj) {

        try {
            if (jobj != null) {
                String strApiName = jobj.optString("api");
                if (strApiName.equalsIgnoreCase("GetNotifications")) {
//                    swipeRefreshLayout.setRefreshing(false);
                    int resultId = jobj.optInt("notificationCount");
                    if (resultId != 0) {
                        JSONArray jsonData = jobj.optJSONArray("resData");
                        listNavigationItems.clear();
                        if (jsonData != null) {
                            for (int i = 0; i < jsonData.length(); i++) {
                                JSONObject jo = jsonData.optJSONObject(i);
                                listNavigationItems.add(new NavigationItem(jo.optString("NotificationCustBindId"), jo.optString("Title"), jo.optString("Message"), jo.optString("ImageUrl"), false));
                            }
                            setNotificationAdapter(listNavigationItems);
                        }
                    } else {
                        mLv_notification.setVisibility(View.GONE);
                        mTv_no_data_found.setVisibility(View.VISIBLE);
                    }
                } else if (strApiName.equalsIgnoreCase("RemoveNotifications")) {
                    unDoClicked = false;
                }
            } else {
                mLv_notification.setVisibility(View.GONE);
                mTv_no_data_found.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View view) {
        if (view == mIv_logo_toolbar) {
            Intent intent = new Intent(this, MainActivitySeller.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (view == mNav_my_profile) {
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, MyProfileActivitySeller.class));
            overridePendingTransition(0, 0);
        } else if (view == mNav_my_products) {
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, ProductsActivitySeller.class));
            overridePendingTransition(0, 0);
        } else if (view == mNav_notification) {
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, NotificationsActivitySeller.class));
            overridePendingTransition(0, 0);
        } else if (view == mNav_change_pass) {
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, ChangePasswordActivitySeller.class));
            overridePendingTransition(0, 0);
        } else if (view == mLl_close || view == mIv_logo_nav) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (view == mTv_logout) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setTitle(commonVariables.appname);
            builder.setMessage("Do you want to logout ?");
            builder.setPositiveButton("Logout", (arg0, arg1) -> commonMethods.logout(this));
            builder.setNegativeButton("Cancel", null);
            builder.show();
        } else if (view == mIv_close) {
            finish();
            overridePendingTransition(0, 0);
        }
    }
}

