package com.shivshankar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shivshankar.ServerCall.APIs;
import com.shivshankar.adapters.CartAdapterBuyer;
import com.shivshankar.classes.CartItem;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonVariables;

import org.json.JSONObject;

import java.util.ArrayList;

public class OrderSuccessActivityBuyer extends BaseActivityCartBuyer implements View.OnClickListener, OnResult {

    private LinearLayout mLl_whole;
    RecyclerView mRv_items;

    private LinearLayout mLl_confirm_order, mLl_shipping, mLl_shipping_address;
    private TextView mTv_subtotal, mTv_shipping, mTv_grand_total, mName, mTv_address, mTv_mobile_no, mTv_order_date, mTv_order_no;
    CartAdapterBuyer adapter;
    ArrayList<CartItem> listArray = new ArrayList<>();

    String orderId = "";
    Resources res;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        View rootView = getLayoutInflater().inflate(R.layout.activity_order_success, frameLayout);
        //rootView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
        try {
            res = getResources();
            bindViews(rootView);
            orderId = getIntent().getStringExtra(commonVariables.KEY_ORDER);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            APIs.GetOrderDetailsMini(this, this, orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindViews(View rootView) {

        try {
            mLl_whole = (LinearLayout) rootView.findViewById(R.id.ll_view);
            mRv_items = (RecyclerView) rootView.findViewById(R.id.rv_items);
            mRv_items.setHasFixedSize(true);

            int i = 1;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                i = 2;
            }
            GridLayoutManager mLayoutManager = new GridLayoutManager(this, i);
            mRv_items.setLayoutManager(mLayoutManager);

            mTv_order_date = (TextView) findViewById(R.id.tv_order_date);
            mTv_order_no = (TextView) findViewById(R.id.tv_order_no);

            mTv_subtotal = (TextView) rootView.findViewById(R.id.tv_subtotal);
            mTv_shipping = (TextView) rootView.findViewById(R.id.tv_shipping);
            mTv_grand_total = (TextView) rootView.findViewById(R.id.tv_grand_total);
            mLl_confirm_order = (LinearLayout) rootView.findViewById(R.id.ll_confirm_order);
            mLl_confirm_order.setOnClickListener(this);
            mLl_shipping = (LinearLayout) rootView.findViewById(R.id.ll_shipping);

            mLl_shipping_address = (LinearLayout) findViewById(R.id.ll_shipping_address);
            mName = (TextView) findViewById(R.id.name);
            mTv_address = (TextView) findViewById(R.id.tv_address);
            mTv_mobile_no = (TextView) findViewById(R.id.tv_mobile_no);

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setListAdapter(ArrayList<CartItem> listArray) {
        try {
            if (listArray.size() == 0) {
                mRv_items.setVisibility(View.GONE);
            } else {
                mRv_items.setVisibility(View.VISIBLE);
                adapter = new CartAdapterBuyer(this, listArray, null, null);
                mRv_items.setAdapter(adapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivityBuyer.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {
        try {
            AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            view.startAnimation(buttonClick);
            if (view == mLl_confirm_order) {
                onBackPressed();
            } else
                super.onClick(view);
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
                if (strApiName.equalsIgnoreCase("GetOrderDetailsMini")) {
                    mLl_whole.setVisibility(View.VISIBLE);

                    JSONObject jo = jobjWhole.optJSONObject("resData");

                    mTv_order_date.setText(jo.optString("OrderDate"));
                    mTv_order_no.setText(jo.optString("OrderNo"));

                    mTv_grand_total.setText(commonVariables.strCurrency_name + " " + jo.optString("TotalAmount"));
                    if (Integer.parseInt(jo.optString("ShippingCharge")) != 0) {
                        mLl_shipping.setVisibility(View.VISIBLE);
                        mTv_shipping.setText(commonVariables.strCurrency_name + " " + jo.optString("ShippingCharge"));
                    } else
                        mLl_shipping.setVisibility(View.GONE);
                    mTv_subtotal.setText(commonVariables.strCurrency_name + " " + jo.optString("SubTotal"));


                    try {
                        JSONObject jObj = jo.optJSONObject("OrderShippingInfo");
                        if (jObj.optString("sCustomerName").isEmpty()) {
                            mLl_shipping_address.setVisibility(View.GONE);
                        } else {
                            mLl_shipping_address.setVisibility(View.VISIBLE);
                            setAddressData(jObj);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    JSONObject job = jobjWhole.optJSONObject("resData");
//                    JSONArray jarray = job.optJSONArray("lstCartItems");
//                    if (jarray != null) {
//                        for (int i = 0; i < jarray.length(); i++) {
//                            JSONObject jObjItem = jarray.optJSONObject(i);
//                            listArray.add(new CartItem(jObjItem.optString("CartId"), jObjItem.optString("ProductId"), jObjItem.optString("ProductCode"), jObjItem.optString("productName"), jObjItem.optString("BrandName"), jObjItem.optString("MarketPrice"), jObjItem.optString("OfferPrice"), jObjItem.optString("TotalPrice"), jObjItem.optString("DiscountPercent"), jObjItem.optString("ImageName"), jObjItem.optInt("CartQuantity"), jObjItem.optInt("MinOrderQuantity"), jObjItem.optBoolean("IsOutOfStock"), false));
//                        }
//                    }
//                    AppPreferences.getPrefs().edit().putInt(commonVariables.CART_COUNT, listArray.size()).apply();
//                    if (jarray == null || jarray.length() == 0)
//                        listArray.clear();
//                    setListAdapter(listArray);
                }
            } else {
                setListAdapter(listArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAddressData(JSONObject jo) {

        mTv_mobile_no.setText(jo.optString("sMobile"));

        String strAddress = "";
        String str = jo.optString("sCustomerName");
        if (!str.isEmpty() && !str.equalsIgnoreCase("null"))
            strAddress = str + " ";

        mName.setText(strAddress);
        strAddress = "";

        str = jo.optString("sAddressLine1");
        if (!str.isEmpty() && !str.equalsIgnoreCase("null"))
            strAddress = strAddress + str;

        str = jo.optString("sAddressLine2");
        if (!str.isEmpty() && !str.equalsIgnoreCase("null"))
            strAddress = strAddress + ", " + str;

        str = jo.optString("sCityName");
        if (!str.isEmpty() && !str.equalsIgnoreCase("null"))
            strAddress = strAddress + ", " + str;

        str = jo.optString("sPincode");
        if (!str.isEmpty() && !str.equalsIgnoreCase("null"))
            strAddress = strAddress + ", " + str + "\n";

        str = jo.optString("sStateName");
        if (!str.isEmpty() && !str.equalsIgnoreCase("null"))
            strAddress = strAddress + "State: " + str + "\n";

        str = jo.optString("sCountryName");
        if (!str.isEmpty() && !str.equalsIgnoreCase("null"))
            strAddress = strAddress + "Country: " + str;

        mTv_address.setText(strAddress);
    }
}

