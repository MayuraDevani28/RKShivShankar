package com.shivshankar;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.adapters.CartAdapterBuyer;
import com.shivshankar.classes.CartItem;
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

/**
 * Created by Mayura on 4/1/2017.
 */

public class CartActivityBuyer extends BaseActivityCartBuyer implements View.OnClickListener, OnResult {

    TextView mTv_no_data_found;
    Button mBtn_add_now;
    private LinearLayout mLl_no_data_found, mLl_whole;
    RecyclerView mRv_items;
    LottieAnimationView animationView2, animationView;

    private LinearLayout mLl_order_summary, mLl_confirm_order, mLl_shipping;
    private TextView mTv_subtotal, mTv_shipping, mTv_grand_total;


    CartAdapterBuyer adapter;
    ArrayList<CartItem> listArray = new ArrayList<>();
    Resources res;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        View rootView = getLayoutInflater().inflate(R.layout.activity_cart, frameLayout);
        rootView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
        try {
            res = getResources();
            bindViews(rootView);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (AppPreferences.getPrefs().getInt(commonVariables.CART_COUNT, 0) != 0) {
                APIs.GetCartList_Suit_Buyer(this, this);
                APIs.GetOrderSummary_Suit(null, this);
            } else
                setListAdapter(listArray);
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
            mNav_my_orders.setOnClickListener(this);
            mNav_about_us.setOnClickListener(this);
            mNav_our_policy.setOnClickListener(this);
            mNav_contact_us.setOnClickListener(this);

            mLl_whole = (LinearLayout) rootView.findViewById(R.id.ll_view);
            mRv_items = (RecyclerView) rootView.findViewById(R.id.rv_items);
            mRv_items.setHasFixedSize(true);

            int i = 1;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                i = 2;
            }
            GridLayoutManager mLayoutManager = new GridLayoutManager(this, i);
            mRv_items.setLayoutManager(mLayoutManager);


            mBtn_add_now = (Button) rootView.findViewById(R.id.btn_add_now);
            mBtn_add_now.setOnClickListener(this);
            mTv_no_data_found = (TextView) rootView.findViewById(R.id.tv_no_data_found);

            mLl_no_data_found = (LinearLayout) rootView.findViewById(R.id.ll_no_data_found);
            animationView = (LottieAnimationView) rootView.findViewById(R.id.animation_view);
            animationView2 = (LottieAnimationView) rootView.findViewById(R.id.animation_view2);

            mLl_order_summary = (LinearLayout) rootView.findViewById(R.id.ll_order_summary);
            mTv_subtotal = (TextView) rootView.findViewById(R.id.tv_subtotal);
            mTv_shipping = (TextView) rootView.findViewById(R.id.tv_shipping);
            mTv_grand_total = (TextView) rootView.findViewById(R.id.tv_grand_total);
            mLl_confirm_order = (LinearLayout) rootView.findViewById(R.id.ll_confirm_order);
            mLl_confirm_order.setOnClickListener(this);
            mLl_shipping = (LinearLayout) rootView.findViewById(R.id.ll_shipping);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        try {
            if (AppPreferences.getPrefs().getInt(commonVariables.CART_COUNT, 0) != 0) {
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
                AlertDialogManager.showDialogYesNo(this, "Do you want to remove all cart items?", "Yes", () -> APIs.ClearCart(CartActivityBuyer.this, CartActivityBuyer.this));
            default:
                return super.onOptionsItemSelected(item);
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

    public void setListAdapter(ArrayList<CartItem> listArray) {
        try {
            if (listArray.size() == 0) {
                mRv_items.setVisibility(View.GONE);
                mLl_order_summary.setVisibility(View.GONE);
                mLl_no_data_found.setVisibility(View.VISIBLE);
                String strMessage = "Uhh! Your Cart is Empty. Want to add now ?";
                mTv_no_data_found.setText((Html.fromHtml(strMessage)));
                startAnim();
                mLl_order_summary.setVisibility(View.GONE);
            } else {
                mLl_no_data_found.setVisibility(View.GONE);
                mRv_items.setVisibility(View.VISIBLE);
                adapter = new CartAdapterBuyer(this, listArray);
                mRv_items.setAdapter(adapter);
            }
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
            } else if (view == mBtn_add_now) {
                Intent intent = new Intent(this, MainActivityBuyer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            } else if (view == mLl_confirm_order) {
                Intent intent = new Intent(this, OrderFormBuyerActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
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
            if (mLl_no_data_found.getVisibility() == View.VISIBLE && animationView != null) {
                startAnim();
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
                if (strApiName.equalsIgnoreCase("GetCartList_Suit_Buyer")) {
                    mLl_whole.setVisibility(View.VISIBLE);

                    JSONObject job = jobjWhole.optJSONObject("resData");
                    JSONArray jarray = job.optJSONArray("lstCartItems");
                    if (jarray != null) {
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jObjItem = jarray.optJSONObject(i);
                            listArray.add(new CartItem(jObjItem.optString("CartId"), jObjItem.optString("ProductId"), jObjItem.optString("ProductCode"), jObjItem.optString("productName"), jObjItem.optString("BrandName"), jObjItem.optString("MarketPrice"), jObjItem.optString("OfferPrice"), jObjItem.optString("TotalPrice"), jObjItem.optString("DiscountPercent"), jObjItem.optString("ImageName"), jObjItem.optInt("CartQuantity"), jObjItem.optInt("MinOrderQuantity"), jObjItem.optBoolean("IsOutOfStock"), false));
                        }
                    }
                    AppPreferences.getPrefs().edit().putInt(commonVariables.CART_COUNT, listArray.size()).apply();
                    if (jarray == null || jarray.length() == 0)
                        listArray.clear();
                    setListAdapter(listArray);
                } else if (strApiName.equalsIgnoreCase("GetOrderSummary_Suit")) {
                    mLl_order_summary.setVisibility(View.VISIBLE);
                    JSONObject jo = jobjWhole.optJSONObject("resData");
                    mTv_grand_total.setText(commonVariables.strCurrency_name + " " + jo.optString("TotalAmount"));
                    if (Integer.parseInt(jo.optString("ShippingCharge")) != 0) {
                        mLl_shipping.setVisibility(View.VISIBLE);
                        mTv_shipping.setText(commonVariables.strCurrency_name + " " + jo.optString("ShippingCharge"));
                    } else
                        mLl_shipping.setVisibility(View.GONE);
                    mTv_subtotal.setText(commonVariables.strCurrency_name + " " + jo.optString("SubTotal"));
                } else if (strApiName.equalsIgnoreCase("ClearCart")) {
                    listArray.clear();
                    AppPreferences.getPrefs().edit().putInt(commonVariables.CART_COUNT, 0).apply();
                    setListAdapter(listArray);
                    invalidateOptionsMenu();
                }
            } else {
                setListAdapter(listArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

