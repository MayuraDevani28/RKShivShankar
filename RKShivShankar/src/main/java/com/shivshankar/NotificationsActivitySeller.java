package com.shivshankar;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.adapters.NotificationListAdapter;
import com.shivshankar.classes.NavigationItem;
import com.shivshankar.customcontrols.SwipeDismissListViewTouchListener;
import com.shivshankar.utills.AlertDialogManager;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivitySeller extends BaseActivityNotiSeller implements OnResult, View.OnClickListener, AdapterView.OnItemClickListener {
    ListView mLv_notification;

    NotificationListAdapter mAdapter;
    List<NavigationItem> listNavigationItems = new ArrayList<NavigationItem>();
    ImageView mIv_close;
    boolean unDoClicked = false;
    CoordinatorLayout coordinatorLayout;
    LottieAnimationView animationView2, animationView;
    private LinearLayout mLl_no_data_found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            Window window = getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            View rootView = getLayoutInflater().inflate(R.layout.activity_notification_seller, frameLayout);
            rootView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
            bindViews(rootView);

            APIs.GetNotifications_Seller(this, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, 0);
    }


    @Override
    protected void onResume() {
        try {
            super.onResume();
            if (mTv_username != null) {
                String strProfile = AppPreferences.getPrefs().getString(commonVariables.KEY_BUYER_PROFILE, "");
                if (!strProfile.isEmpty() && !strProfile.equalsIgnoreCase("null"))
                    mTv_username.setText(WordUtils.capitalizeFully(new JSONObject(strProfile).optString("Name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindViews(View rootView) {
        try {
            mIv_logo_nav.setOnClickListener(this);
            mIv_logo_toolbar.setOnClickListener(this);
            mTv_username.setOnClickListener(this);
            mTv_logout.setOnClickListener(this);
            mLl_close.setOnClickListener(this);

            mNav_my_profile.setOnClickListener(this);
            mNav_my_products.setOnClickListener(this);
            mNav_notification.setOnClickListener(this);
            mNav_my_orders.setOnClickListener(this);
            mNav_change_pass.setOnClickListener(this);

            mIv_close = (ImageView) rootView.findViewById(R.id.iv_close);
            mIv_close.setOnClickListener(this);
            mLv_notification = (ListView) rootView.findViewById(R.id.ll_notification);
            mLv_notification.setOnItemClickListener(this);

            mLl_no_data_found = (LinearLayout) rootView.findViewById(R.id.ll_no_data_found);
            animationView = (LottieAnimationView) rootView.findViewById(R.id.animation_view);
            animationView2 = (LottieAnimationView) rootView.findViewById(R.id.animation_view2);

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
                                            APIs.RemoveNotifications_Seller(NotificationsActivitySeller.this, NotificationsActivitySeller.this, item.getNotificationCustBindId());
                                        super.onDismissed(snackbar, event);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            snackbar.setActionTextColor(Color.WHITE);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_clear:
                AlertDialogManager.showDialogYesNo(this, "Do you want to clear all notifications?", "Yes", () -> APIs.RemoveAllNotifications_Seller(NotificationsActivitySeller.this, NotificationsActivitySeller.this));

            default:
                return super.onOptionsItemSelected(item);
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
                if (strApiName.equalsIgnoreCase("GetNotifications_Seller")) {
                    int resultId = jobj.optInt("notificationCount");
                    if (resultId != 0) {
                        JSONArray jsonData = jobj.optJSONArray("resData");
                        listNavigationItems.clear();
                        if (jsonData != null) {
                            for (int i = 0; i < jsonData.length(); i++) {
                                JSONObject jo = jsonData.optJSONObject(i);
                                listNavigationItems.add(new NavigationItem(jo.optString("NotificationCustBindId"), jo.optString("Title"), jo.optString("Message"), jo.optString("ImageUrl"), false, jo.optString("NotificationDate")));
                            }
                            setNotificationAdapter(listNavigationItems);
                        }
                    } else {
                        mLv_notification.setVisibility(View.GONE);
                        mLl_no_data_found.setVisibility(View.VISIBLE);
                        startAnim();
                    }
                } else if (strApiName.equalsIgnoreCase("RemoveNotifications_Seller")) {
                    unDoClicked = false;
                    int resultId = jobj.optInt("notificationCount");
                    AppPreferences.getPrefs().edit().putInt(commonVariables.KEY_NOTI_COUNT, resultId).apply();
                    if (resultId == 0) {
                        mLv_notification.setVisibility(View.GONE);
                        mLl_no_data_found.setVisibility(View.VISIBLE);
                        startAnim();
                    }
                } else if (strApiName.equalsIgnoreCase("RemoveAllNotifications_Seller")) {
                    AppPreferences.getPrefs().edit().putInt(commonVariables.KEY_NOTI_COUNT, 0).apply();
                    mLv_notification.setVisibility(View.GONE);
                    mLl_no_data_found.setVisibility(View.VISIBLE);
                    startAnim();
                }
            } else {
                mLv_notification.setVisibility(View.GONE);
                mLl_no_data_found.setVisibility(View.VISIBLE);
                startAnim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startAnim() {
        animationView.setProgress(0f);
        animationView.playAnimation();
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animationView2.setProgress(0f);
                animationView2.playAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animationView2.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animationView.setProgress(0f);
                animationView.playAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            finishAnim();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void finishAnim() {
        try {
            animationView.cancelAnimation();
            animationView2.cancelAnimation();
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
        } else if (view == mNav_my_orders) {
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, MyOrdersActivitySeller.class));
            overridePendingTransition(0, 0);
        } else if (view == mLl_close || view == mIv_logo_nav || view == mTv_username) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (view == mTv_logout) {
            drawer.closeDrawer(GravityCompat.START);
            commonMethods.logout(this, true);
        } else if (view == mIv_close) {
            finish();
            overridePendingTransition(0, 0);
        }
    }
}

