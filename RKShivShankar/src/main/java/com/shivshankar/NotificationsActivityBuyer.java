package com.shivshankar;

import android.animation.Animator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.shivshankar.utills.commonVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivityBuyer extends BaseActivityCartBuyer implements OnResult, View.OnClickListener, AdapterView.OnItemClickListener {
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
            //rootView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
            bindViews(rootView);

            APIs.GetNotifications_Buyer(this, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, 0);
    }


    private void bindViews(View rootView) {
        try {
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
                            removeNoti(item, position);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_clear:
                AlertDialogManager.showDialogYesNo(this, "Do you wantto clear all notifications?", "Yes", () -> APIs.RemoveAllNotifications_Buyer(NotificationsActivityBuyer.this, NotificationsActivityBuyer.this));

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
                if (strApiName.equalsIgnoreCase("GetNotifications_Buyer")) {
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
                } else if (strApiName.equalsIgnoreCase("RemoveNotifications_Buyer")) {
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
            invalidateOptionsMenu();
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
        if (view == mIv_close) {
            finish();
            overridePendingTransition(0, 0);
        } else
            super.onClick(view);
    }

    public void removeNoti(NavigationItem item, int position) {
        try {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Notification's gone !", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    unDoClicked = true;
                    item.setUnDoClicked(true);
                    mAdapter.insert(item, position);
                    mAdapter.notifyDataSetChanged();
                }
            });
            snackbar.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    try {
                        if (!unDoClicked)
                            APIs.RemoveNotifications_Buyer(NotificationsActivityBuyer.this, NotificationsActivityBuyer.this, item.getNotificationCustBindId());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callRemoveNoti(NavigationItem item) {
        APIs.RemoveNotifications_Buyer(NotificationsActivityBuyer.this, NotificationsActivityBuyer.this, item.getNotificationCustBindId());
    }
}

