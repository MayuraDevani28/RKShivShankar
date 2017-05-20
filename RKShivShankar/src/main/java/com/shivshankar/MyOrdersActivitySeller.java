package com.shivshankar;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.adapters.OrdersAdapterSeller;
import com.shivshankar.classes.Order;
import com.shivshankar.classes.SC3Object;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class MyOrdersActivitySeller extends BaseActivitySeller implements OnClickListener, OnResult {

    TextView mTv_no_data_found, mTv_title, mTv_count_items;
    Button mBtn_add_now;
    private LinearLayout mLl_no_data_found,mLl_title;
    public RecyclerView mRv_items;
    FrameLayout mFl_whole;
    private ImageView mIv_close;
    LottieAnimationView animationView2, animationView;

    Boolean isLogedIn = false;
    String total = "";

    OrdersAdapterSeller adapter;
    ArrayList<Order> listArray = new ArrayList<Order>();
    int pageNo = 1;
    boolean loading, isFirstScrollDone = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        View rootView = getLayoutInflater().inflate(R.layout.activity_my_orders_buyer, frameLayout);
        //rootView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));

        try {
            bindViews(rootView);
            APIs.GetOrderList_Seller(this, this, pageNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindViews(View rootView) {

        try {
            mLl_title = (LinearLayout) rootView.findViewById(R.id.ll_title);
            mTv_title = (TextView) rootView.findViewById(R.id.tv_title);
            mTv_title.setText("My Orders");
            mIv_close = (ImageView) findViewById(R.id.iv_close);
            mIv_close.setOnClickListener(this);

            mFl_whole = (FrameLayout) rootView.findViewById(R.id.fl_whole);
            mRv_items = (RecyclerView) rootView.findViewById(R.id.lv_orders);
            mRv_items.setHasFixedSize(true);

            int i = 1;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                i = 2;
            }
            GridLayoutManager mLayoutManager = new GridLayoutManager(this, i);
            mRv_items.setLayoutManager(mLayoutManager);

            mRv_items.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    try {
                        int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                        int visibleItemCount = mLayoutManager.getChildCount();
                        if (isFirstScrollDone && listArray.size() != 0) {
                            int citem = (pastVisiblesItems + visibleItemCount);
                            if (citem != 2) {
                                mTv_count_items.setText(" (" + citem + "/" + total + " products)");
                            }
                        }
                        isFirstScrollDone = true;
                        if (dy > 0) {
                            int totalItemCount = mLayoutManager.getItemCount();
                            if (loading) {
                                if ((visibleItemCount + pastVisiblesItems) >= (totalItemCount - 10)) {
                                    loading = false;
                                    APIs.GetOrderList_Seller(MyOrdersActivitySeller.this, MyOrdersActivitySeller.this, ++pageNo);
                                }
                            }
                            if (mLl_title.getVisibility() == View.VISIBLE)
                                commonMethods.collapse(mLl_title);
                        } else if (mLl_title.getVisibility() == View.GONE)
                            commonMethods.expand(mLl_title);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            mBtn_add_now = (Button) rootView.findViewById(R.id.btn_add_now);
            mBtn_add_now.setText("GO TO HOME");
            mBtn_add_now.setOnClickListener(this);
            mTv_no_data_found = (TextView) rootView.findViewById(R.id.tv_no_data_found);
            mTv_title.setOnClickListener(this);

            mTv_count_items = (TextView) rootView.findViewById(R.id.tv_no_items);
            mLl_no_data_found = (LinearLayout) rootView.findViewById(R.id.ll_no_data_found);
            animationView = (LottieAnimationView) rootView.findViewById(R.id.animation_view);
            animationView2 = (LottieAnimationView) rootView.findViewById(R.id.animation_view2);

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

    private void setListAdapter(ArrayList<Order> listArray) {
        try {
            if (listArray.size() == 0) {
                mRv_items.setVisibility(View.GONE);
                mLl_no_data_found.setVisibility(View.VISIBLE);
                String strMessage = "Uhh! You have no orders yet";
                mTv_no_data_found.setText((Html.fromHtml(strMessage)));
                startAnim();
            } else {
                mLl_no_data_found.setVisibility(View.GONE);
                mRv_items.setVisibility(View.VISIBLE);
                mFl_whole.setVisibility(View.VISIBLE);
                adapter = new OrdersAdapterSeller(this, listArray);
                mRv_items.setAdapter(adapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {
        try {
            AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            view.startAnimation(buttonClick);
            if (view == mIv_close) {
                Intent output = new Intent();
                setResult(RESULT_OK, output);
                finish();
                overridePendingTransition(0, 0);
            } else if (view == mBtn_add_now) {
                Intent intent = new Intent(getApplicationContext(), MainActivitySeller.class);
                intent.putExtra(commonVariables.KEY_IS_BRAND, false);
                startActivity(intent);
                overridePendingTransition(0, 0);
            } else
                super.onClick(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onResume() {
//        try {
//            invalidateOptionsMenu();
//            isLogedIn = AppPreferences.getPrefs().getBoolean(commonVariables.KEY_IS_LOG_IN, false);
//            if (adapter != null)
//                adapter.notifyDataSetChanged();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        super.onResume();
//    }

    @SuppressLint("NewApi")
    @Override
    public void onResult(JSONObject jobjWhole) {
        try {
            if (jobjWhole != null) {

                String strApiName = jobjWhole.optString("api");
                if (strApiName.equalsIgnoreCase("GetOrderList_Seller")) {
                    JSONObject joi = jobjWhole.optJSONObject("resData");
                    JSONArray jarray = joi.optJSONArray("lstOrders");
                    total = joi.optString("totalOrderCount");

                    mTv_count_items.setVisibility(View.VISIBLE);
                    mTv_count_items.setText(" (" + total + " orders)");
                    if (jarray != null) {
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jObjItem = jarray.optJSONObject(i);
                            JSONArray jarr = jObjItem.optJSONArray("lstProductimg");
                            ArrayList<SC3Object> arr = new ArrayList<SC3Object>();
                            if (jarr != null)
                                for (int j = 0; j < jarr.length(); j++) {
                                    JSONObject jo = jarr.getJSONObject(j);
                                    arr.add(new SC3Object(jo.optInt("ProductId"), "", "", jo.optString("ProductImages")));
                                }
                            listArray.add(new Order(jObjItem.optString("OrderId"), jObjItem.optString("OrderNo"), jObjItem.optString("OrderDate"), jObjItem.optString("TotalPrice"), jObjItem.optString("OrderStatus"), arr, jObjItem.optString("CustomerName")));
                        }
                    }
                    if (pageNo == 1) {
                        if (jarray == null || jarray.length() == 0)
                            listArray.clear();
                        setListAdapter(listArray);
                    } else {
                        if (listArray.size() != 0) {
                            mLl_no_data_found.setVisibility(View.GONE);
                            mRv_items.setVisibility(View.VISIBLE);
                            if (isLogedIn && adapter != null)
                                adapter.notifyDataSetChanged();
                        }
                    }

                    if (jobjWhole.optInt("totalOrderCount") > listArray.size())
                        loading = true;
                }
            } else {
                setListAdapter(listArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

