package com.shivshankar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.ActionMenuView;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.classes.Brand;
import com.shivshankar.utills.AlertDialogManager;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.shivshankar.R.id.ll_create_brand;


public class MainActivitySeller extends BaseActivitySeller implements View.OnClickListener, OnResult {

    LinearLayout mLl_create_brand, mLl_add_product;
    RelativeLayout mLl_brand;
    ImageView mIv_change_image, mIv_imageView, mIv_delete_image;
    TextView mTv_brand_name, mTv_welcome;
    View view_top;
    FrameLayout mFl_with_brand;
    Brand item;
    boolean isBackpressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        try {
            View rootView = getLayoutInflater().inflate(R.layout.activity_main_seller, frameLayout);
            bindViews(rootView);

            Gson gson = new Gson();
            String json = AppPreferences.getPrefs().getString(commonVariables.KEY_BRAND, "");
            item = gson.fromJson(json, Brand.class);
            if (item != null)
                setBrandVisibility(true, item);
            else
                APIs.GetSellerBrandList(this, this);

            if (AppPreferences.getPrefs().getBoolean(commonVariables.KEY_FIRST_TIME, true)) {
                //showGuideline();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void showGuideline() {
        try {
            toolbar.bringToFront();
            if (toolbar.findViewById(R.id.action_notifications) != null) {
                final SpannableString strMenu = new SpannableString("This will open Navigation Menu, which contain quick links for Profile, Orders, Products, Notifications and Change Password."),
                        strNotification = new SpannableString("You can see all notifications for order status, discounts, coupons and special offers after clicking here."),
                        strCreateBrand = new SpannableString("You are create brand after clicking here."),
                        strAddProduct = new SpannableString("You can add products anytime using this button");
                strMenu.setSpan(new StyleSpan(Typeface.ITALIC), 15, 15 + "Navigation Menu".length(), 0);
                strCreateBrand.setSpan(new StyleSpan(Typeface.ITALIC), 8, 8 + "create brand".length(), 0);
                strNotification.setSpan(new StyleSpan(Typeface.ITALIC), 16, 16 + "notifications".length(), 0);
                strAddProduct.setSpan(new StyleSpan(Typeface.ITALIC), 8, 8 + "add products".length(), 0);

                final TapTargetSequence sequence = new TapTargetSequence(this)
                        .targets(
                                TapTarget.forToolbarNavigationIcon(toolbar, "This is Menu button", strMenu)
                                        .id(1)
                                        .dimColor(android.R.color.white)
                                        .outerCircleColor(R.color.colorPrimary)
                                        .targetCircleColor(android.R.color.white)
                                        .textColor(android.R.color.white)
                                        .targetRadius(50)
                                        .cancelable(false)
                                , TapTarget.forView(((ActionMenuView) toolbar.getChildAt(2)).getChildAt(0), "Notifications", strNotification).textColorInt(ContextCompat.getColor(this, R.color.white))
                                        .id(2)
                                        .dimColor(android.R.color.white)
                                        .outerCircleColor(R.color.colorPrimary)
                                        .targetCircleColor(android.R.color.white)
                                        .targetRadius(50)
                                        .textColor(android.R.color.white)
                                , TapTarget.forView(mLl_create_brand, "Create Brand", strAddProduct)
                                        .id(3)
                                        .dimColor(android.R.color.white)
                                        .outerCircleColor(R.color.colorPrimary)
                                        .targetCircleColor(android.R.color.white)
                                        .targetRadius(50)
                                        .textColor(android.R.color.white)
                                , TapTarget.forView(mLl_add_product, "Add Product", strAddProduct)
                                        .id(4)
                                        .dimColor(android.R.color.white)
                                        .outerCircleColor(R.color.colorPrimary)
                                        .targetCircleColor(android.R.color.white)
                                        .targetRadius(50)
                                        .textColor(android.R.color.white)

                        );
                sequence.start();
                AppPreferences.getPrefs().edit().putBoolean(commonVariables.KEY_FIRST_TIME, false).apply();
            }
        } catch (Exception e) {
            AppPreferences.getPrefs().edit().putBoolean(commonVariables.KEY_FIRST_TIME, true).apply();
            e.printStackTrace();

            Log.e("TAGRK", "Error:" + e.toString());
        }
    }*/

    @Override
    public void onBackPressed() {
        if (isBackpressedOnce)
            super.onBackPressed();
        else {
            isBackpressedOnce = true;
            Toast.makeText(MainActivitySeller.this, "Press again to close " + commonVariables.appname, Toast.LENGTH_SHORT).show();
        }
    }

    private void bindViews(View rootView) {
        mLl_create_brand = (LinearLayout) rootView.findViewById(ll_create_brand);
        mLl_create_brand.setOnClickListener(this);
        mLl_brand = (RelativeLayout) rootView.findViewById(R.id.ll_brand);
        mFl_with_brand = (FrameLayout) rootView.findViewById(R.id.fl_with_brand);
        mLl_add_product = (LinearLayout) rootView.findViewById(R.id.ll_add_product);
        mLl_add_product.setOnClickListener(this);
        mIv_change_image = (ImageView) rootView.findViewById(R.id.iv_change_image);
        mIv_change_image.setOnClickListener(this);
//        mIv_delete_image = (ImageView) rootView.findViewById(R.id.iv_delete);
//        mIv_delete_image.setOnClickListener(this);
        mIv_imageView = (ImageView) rootView.findViewById(R.id.iv_effectImg);
        mTv_brand_name = (TextView) rootView.findViewById(R.id.tv_brand_name);
        view_top = rootView.findViewById(R.id.view_top);
        mTv_welcome = (TextView) rootView.findViewById(R.id.tv_welcome);
    }

    private void setBrandVisibility(boolean b, Brand brand) {
        try {
            if (b) {
                view_top.setVisibility(View.GONE);
                mLl_brand.setVisibility(View.VISIBLE);
                mLl_create_brand.setVisibility(View.GONE);
                String strImageURL = brand.getBrandLogo();
                if ((strImageURL != null) && (!strImageURL.equals(""))) {
                    Glide.with(this).load(strImageURL).asBitmap()
                            .placeholder(R.drawable.xml_round_gray).error(R.drawable.xml_round_white)
                            .centerCrop().into(new BitmapImageViewTarget(mIv_imageView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            mIv_imageView.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }
                mIv_imageView.setDrawingCacheEnabled(true);
                mTv_brand_name.setText(brand.getBrandName());

//                commonMethods.startZommingAnim(mFl_with_brand);
            } else {
                view_top.setVisibility(View.VISIBLE);
                mLl_brand.setVisibility(View.GONE);
                mLl_create_brand.setVisibility(View.VISIBLE);


                commonMethods.startZommingAnim(mLl_create_brand);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            isBackpressedOnce = false;
            Gson gson = new Gson();
            String json = AppPreferences.getPrefs().getString(commonVariables.KEY_BRAND, "");
            item = gson.fromJson(json, Brand.class);
            if (item != null)
                setBrandVisibility(true, item);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == commonVariables.REQUEST_ADD_UPDATE_BRAND && resultCode == RESULT_OK && data != null) {
                if (data != null) {
                    boolean isBrandUpdatedChanged = data.getExtras().getBoolean(commonVariables.KEY_IS_BRAND_UPDATED);
                    if (isBrandUpdatedChanged) {
                        Gson gson = new Gson();
                        String json = AppPreferences.getPrefs().getString(commonVariables.KEY_BRAND, "");
                        item = gson.fromJson(json, Brand.class);
                        setBrandVisibility(true, item);
//                        APIs.GetSellerBrandList(this, this);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        try {
            AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            view.startAnimation(buttonClick);
            if (view == mLl_create_brand) {
                Intent intent = new Intent(getApplicationContext(), ImagePickerActivity.class);
                intent.putExtra(commonVariables.KEY_IS_BRAND, true);
                startActivityForResult(intent, commonVariables.REQUEST_ADD_UPDATE_BRAND);
            } else if (view == mIv_change_image) {
                Intent intent = new Intent(getApplicationContext(), AddUpdateBrandActivitySeller.class);
                SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
                Gson gson = new Gson();
                String json = gson.toJson(item);
                editor.putString(commonVariables.KEY_BRAND, json);
                editor.apply();

                startActivityForResult(intent, commonVariables.REQUEST_ADD_UPDATE_BRAND);
                overridePendingTransition(0, 0);
            } else if (view == mIv_delete_image) {
                AlertDialogManager.showDialogYesNo(this, "Do you want to delete this brand?", "Yes", () -> APIs.RemoveSellerBrand(MainActivitySeller.this, MainActivitySeller.this, item.getBrandId() + ""));

            } else if (view == mLl_add_product) {
                Intent intent = new Intent(getApplicationContext(), ImagePickerActivity.class);
                intent.putExtra(commonVariables.KEY_IS_BRAND, false);
                startActivity(intent);
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
                if (strApiName.equalsIgnoreCase("GetSellerBrandList")) {
                    JSONArray jarray = jobjWhole.optJSONArray("resData");
                    if (jarray != null) {
                        JSONObject jObjItem = jarray.optJSONObject(0);
                        item = new Brand(jObjItem.optString("BrandId"), jObjItem.optString("BrandName"), jObjItem.optString("BrandLogo"));
                        setBrandVisibility(true, item);
                        SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(item);
                        editor.putString(commonVariables.KEY_BRAND, json);
                        editor.apply();

                    } else
                        setBrandVisibility(false, null);

                } else if (strApiName.equalsIgnoreCase("RemoveSellerBrand")) {
                    int strresId = jobjWhole.optInt("resInt");
                    Runnable listener = null;
                    if (strresId == 1) {
                        listener = () -> {
                            AppPreferences.getPrefs().edit().putString(commonVariables.KEY_BRAND, "").apply();
                            setBrandVisibility(false, null);
                        };
                    }
                    AlertDialogManager.showDialog(this, jobjWhole.optString("res"), listener);
                }
            } else {
                setBrandVisibility(false, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
