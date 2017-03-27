package com.shivshankar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.shivshankar.ServerCall.APIs;
import com.shivshankar.adapters.OrdersAdapter;
import com.shivshankar.classes.Order;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class MyOrdersActivityBuyer extends BaseActivityBuyer implements OnClickListener, OnResult {

    private RecyclerView mLv_orders;
    private TextView mTv_order_no_data;
    ArrayList<Order> listArray = new ArrayList<Order>();
    public ImageView mIv_close;

    public MyOrdersActivityBuyer() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            Window window = getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);
            setContentView(R.layout.activity_my_orders_buyer);
            bindViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        try {
            if (!commonMethods.knowInternetOn(this)) {
                Snackbar snack = Snackbar.make(mTv_order_no_data, R.string.no_internet, Snackbar.LENGTH_LONG);
                View view = snack.getView();
                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.WHITE);
                snack.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    private void callOrdersAPI(String strLoginId) {
        Uri uri = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/GetMyOrders")
                .appendQueryParameter("loginId", strLoginId)
                .build();
        String query = uri.toString();
        APIs.callAPI(null, this, query);
    }

    private void bindViews() {
        mIv_close = (ImageView) findViewById(R.id.iv_close);
        mIv_close.setOnClickListener(this);
        mLv_orders = (RecyclerView) findViewById(R.id.lv_orders);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mLv_orders.setLayoutManager(layoutManager);
        mTv_order_no_data = (TextView) findViewById(R.id.tv_order_no_data);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, 0);
    }

    public void returnBack() {
        try {
            Intent output = new Intent();
            setResult(RESULT_OK, output);
            finish();
            overridePendingTransition(0, 0);
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
                returnBack();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onResult(JSONObject jobjWhole) {

        try {
            if (jobjWhole != null) {
                String strApiName = jobjWhole.optString("api");
                if (strApiName.equalsIgnoreCase("GetMyOrders")) {
                    JSONArray jarray = jobjWhole.optJSONArray("resData");
                    if (jarray != null) {
                        listArray.clear();
                        int l = jarray.length();
                        for (int i = 0; i < l; i++) {
                            JSONObject joOrder = jarray.optJSONObject(i);
                            listArray.add(new Order(joOrder.optString("OrderId"), joOrder.optString("OrderNo"), joOrder.optString("OrderDate"), " " + joOrder.optString("TotalAmount"), joOrder.optString("ShipTo"), joOrder.optString("OrderStatus"), joOrder.optBoolean("ShowShipNowBtn")));
                        }
                    }
                    setListAdapter(listArray);
                }
            }
            commonMethods.hidesoftKeyboard(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setListAdapter(ArrayList<Order> listArray2) {
        try {
            int l = listArray2.size();
            if (l == 0) {
                mTv_order_no_data.setVisibility(View.VISIBLE);
            } else {
                mTv_order_no_data.setVisibility(View.GONE);
                mLv_orders.setVisibility(View.VISIBLE);
                OrdersAdapter adapter = new OrdersAdapter((AppCompatActivity) this, listArray2);
                mLv_orders.setAdapter(adapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
