package com.shivshankar;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.adapters.CartAdapterBuyer;
import com.shivshankar.classes.CartItem;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mayura on 4/1/2017.
 */

public class OrderFormActivity extends AppCompatActivity implements View.OnClickListener, OnResult, CompoundButton.OnCheckedChangeListener {



    private LinearLayout mLl_order_summary, mLl_confirm_order, mLl_shipping;
    private TextView mTv_subtotal, mTv_shipping, mTv_grand_total;

    LinearLayout mNav_my_profile, mNav_my_orders, mNav_about_us, mNav_our_policy, mNav_contact_us, mLl_close;
    Toolbar toolbar;
    DrawerLayout drawer;

    ImageView mIv_logo_nav, mIv_logo_toolbar;
    TextView mTv_username, mTv_logout;

    CartAdapterBuyer adapter;
    ArrayList<CartItem> listArray = new ArrayList<>();
    Resources res;
    private CheckBox mCB_Defferent_Address;
    private LinearLayout mll_Billing_Title;
    private LinearLayout mll_Billing;
    private EditText mEdt_FullName_s;
    private EditText mEdt_Email_s;
    private EditText mEdt_Mobile_s;
    private EditText mEdt_Address1_s;
    private EditText mEdt_Address2_s;
    private EditText mEdt_City_s;
    private EditText mEdt_State_s;
    private EditText mEdt_Country_s;
    private EditText mEdt_Pincode_s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_order_form);

        try {
            res = getResources();
            bindViews();

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setLogo(null);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (AppPreferences.getPrefs().getInt(commonVariables.CART_COUNT, 0) != 0) {
                APIs.GetOrderSummary_Suit(null, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindViews() {

        try {
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            toolbar = (Toolbar) findViewById(R.id.toolbar);

            mNav_my_profile = (LinearLayout) findViewById(R.id.nav_my_profile);
            mNav_my_orders = (LinearLayout) findViewById(R.id.nav_my_orders);
            mNav_about_us = (LinearLayout) findViewById(R.id.nav_about_us);
            mNav_our_policy = (LinearLayout) findViewById(R.id.nav_our_policy);
            mNav_contact_us = (LinearLayout) findViewById(R.id.nav_contact_us);

            mIv_logo_nav = (ImageView) findViewById(R.id.iv_logo_nav);
            mIv_logo_toolbar = (ImageView) findViewById(R.id.iv_logo_toolbar);
            mTv_username = (TextView) findViewById(R.id.tv_username);
            mTv_logout = (TextView) findViewById(R.id.tv_logout);
            mLl_close = (LinearLayout) findViewById(R.id.ll_close);

            mll_Billing_Title = (LinearLayout)findViewById(R.id.ll_billing_title);
            mll_Billing = (LinearLayout)findViewById(R.id.ll_billing);

            mEdt_FullName_s = (EditText)findViewById(R.id.edt_full_name_shipping);
            mEdt_Email_s = (EditText)findViewById(R.id.edt_email_shipping);
            mEdt_Mobile_s = (EditText)findViewById(R.id.edt_mobile_shipping);
            mEdt_Address1_s = (EditText)findViewById(R.id.edt_address1_shipping);
            mEdt_Address2_s = (EditText)findViewById(R.id.edt_address2_shipping);
            mEdt_City_s = (EditText)findViewById(R.id.edt_address_city_shipping);
            mEdt_State_s = (EditText)findViewById(R.id.edt_address_state_shipping);
            mEdt_Country_s = (EditText)findViewById(R.id.sp_country_shipping);
            mEdt_Pincode_s = (EditText)findViewById(R.id.edt_pin_code_shipping);

            mCB_Defferent_Address = (CheckBox) findViewById(R.id.cb_same_address);
            mCB_Defferent_Address.setOnCheckedChangeListener(this);

            mIv_logo_nav.setOnClickListener(this);
            mIv_logo_toolbar.setOnClickListener(this);
            mTv_username.setOnClickListener(this);
            mTv_logout.setOnClickListener(this);
            mLl_close.setOnClickListener(this);

            mNav_my_profile.setOnClickListener(this);
            mNav_my_orders.setOnClickListener(this);
            mNav_about_us.setOnClickListener(this);
            mNav_our_policy.setOnClickListener(this);
            mNav_contact_us.setOnClickListener(this);






            mLl_order_summary = (LinearLayout) findViewById(R.id.ll_order_summary);
            mTv_subtotal = (TextView) findViewById(R.id.tv_subtotal);
            mTv_shipping = (TextView) findViewById(R.id.tv_shipping);
            mTv_grand_total = (TextView) findViewById(R.id.tv_grand_total);
            mLl_confirm_order = (LinearLayout) findViewById(R.id.ll_confirm_order);
            mLl_confirm_order.setOnClickListener(this);
            mLl_shipping = (LinearLayout) findViewById(R.id.ll_shipping);

        } catch (Exception e) {
            e.printStackTrace();
        }
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, 0);
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {
        try {
            AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            view.startAnimation(buttonClick);
            if (view == mIv_logo_toolbar) {
                Intent intent = new Intent(this, MainActivityBuyer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
            } else if (view == mNav_my_profile) {
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, MyProfileActivityBuyer.class));
                overridePendingTransition(0, 0);
            } else if (view == mNav_my_orders) {
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, MyOrdersActivityBuyer.class));
                overridePendingTransition(0, 0);
            } else if (view == mNav_about_us) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(this, CMSCallandDisplayActivityBuyer.class);
                intent.putExtra(commonVariables.INTENT_EXTRA_PAGE_NAME, "aboutus");
                startActivity(intent);
                overridePendingTransition(0, 0);
            } else if (view == mNav_our_policy) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(this, CMSListingActivityBuyer.class);
                intent.putExtra(commonVariables.INTENT_EXTRA_PAGE, "GetPolicies");
                intent.putExtra(commonVariables.INTENT_EXTRA_PAGE_NAME, "Our Policy");
                startActivity(intent);
                overridePendingTransition(0, 0);
            } else if (view == mNav_contact_us) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(this, CMSCallandDisplayActivityBuyer.class);
                intent.putExtra(commonVariables.INTENT_EXTRA_PAGE_NAME, "contactus");
                startActivity(intent);
                overridePendingTransition(0, 0);
            } else if (view == mLl_close || view == mIv_logo_nav || view == mTv_username) {
                drawer.closeDrawer(GravityCompat.START);
            } else if (view == mTv_logout) {
                drawer.closeDrawer(GravityCompat.START);
                if (mTv_logout.getText().equals("Login")) {
                    startActivity(new Intent(this, LoginRegisterActivity.class));
                    onBackPressed();
                } else {
                    commonMethods.logout(this, true);
                }
            }  else if (view == mLl_confirm_order) {
               /* Intent intent = new Intent(this, OrderFormActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        try {
            invalidateOptionsMenu();
            if (adapter != null)
                adapter.notifyDataSetChanged();

            if (mTv_username != null) {
                String strProfile = AppPreferences.getPrefs().getString(commonVariables.KEY_SELLER_PROFILE, "");
                if (!strProfile.isEmpty() && !strProfile.equalsIgnoreCase("null"))
                    mTv_username.setText(WordUtils.capitalizeFully(new JSONObject(strProfile).optString("Name")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @SuppressLint("NewApi")
    @Override
    public void onResult(JSONObject jobjWhole) {
        try {
            if (jobjWhole != null) {
                String strApiName = jobjWhole.optString("api");
                if (strApiName.equalsIgnoreCase("GetOrderSummary_Suit")) {
                    mLl_order_summary.setVisibility(View.VISIBLE);
                    JSONObject jo = jobjWhole.optJSONObject("resData");
                    mTv_grand_total.setText(jo.optString("TotalAmount"));
                    if (Integer.parseInt(jo.optString("ShippingCharge")) != 0) {
                        mLl_shipping.setVisibility(View.VISIBLE);
                        mTv_shipping.setText(jo.optString("ShippingCharge"));
                    } else
                        mLl_shipping.setVisibility(View.GONE);
                    mTv_subtotal.setText(jo.optString("SubTotal"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            mll_Billing.setVisibility(View.VISIBLE);
            mll_Billing_Title.setVisibility(View.VISIBLE);
        }
        else {
            mll_Billing.setVisibility(View.GONE);
            mll_Billing_Title.setVisibility(View.GONE);
        }
    }
}

