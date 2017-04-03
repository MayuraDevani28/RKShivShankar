package com.shivshankar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.classes.Brand;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONException;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        try {
            View rootView = getLayoutInflater().inflate(R.layout.activity_main_seller, frameLayout);
            rootView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.slide_in_right));
            bindViews(rootView);

            Gson gson = new Gson();
            String json = AppPreferences.getPrefs().getString(commonVariables.KEY_BRAND, "");
            item = gson.fromJson(json, Brand.class);
            if (item != null)
                setBrandVisibility(true, item);
            else
                APIs.GetSellerBrandList(this, this);

//            try {
//                byte[] byteArray = Base64.decode(AppPreferences.getPrefs().getString("image", ""), Base64.DEFAULT);
//                String strBrandName = AppPreferences.getPrefs().getString(commonVariables.KEY_BRAND_NAME, "");
//
//                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//                RoundedBitmapDrawable circularBitmapDrawable =
//                        RoundedBitmapDrawableFactory.create(getResources(), bmp);
//                circularBitmapDrawable.setCircular(true);
//
//                setBrandVisibility(!strBrandName.isEmpty(), circularBitmapDrawable, strBrandName);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

//            try {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    swipeRefreshLayout.setProgressViewOffset(false, 0, 200);
//                }
//                swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_green_light);
//                swipeRefreshLayout.setOnRefreshListener(() -> {
//                    swipeRefreshLayout.setRefreshing(false);
////                    APIs.GetHomeBannerwithText(this, this);
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        } catch (Exception e) {
            e.printStackTrace();
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

        mIv_logo_nav.setOnClickListener(this);
        mIv_logo_toolbar.setOnClickListener(this);
        mTv_username.setOnClickListener(this);
        mTv_logout.setOnClickListener(this);
        mLl_close.setOnClickListener(this);

//        mNav_my_brands = (LinearLayout) findViewById(R.id.nav_my_brands);
//        mNav_my_brands.setOnClickListener(this);
        mNav_my_profile.setOnClickListener(this);
        mNav_my_products.setOnClickListener(this);
        mNav_notification.setOnClickListener(this);
        mNav_change_pass.setOnClickListener(this);
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
            if (mTv_username != null) {
                setUserName();
            }
            Gson gson = new Gson();
            String json = AppPreferences.getPrefs().getString(commonVariables.KEY_BRAND, "");
            item = gson.fromJson(json, Brand.class);
            if (item != null)
                setBrandVisibility(true, item);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUserName() {
        try {
            String strProfile = AppPreferences.getPrefs().getString(commonVariables.KEY_SELLER_PROFILE, "");
            if (!strProfile.isEmpty() && !strProfile.equalsIgnoreCase("null")) {
                String strUname = WordUtils.capitalizeFully(new JSONObject(strProfile).optString("Name"));
                mTv_username.setText(strUname);
                mTv_welcome.setText("Welcome " + strUname);
            }
        } catch (JSONException e) {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(commonVariables.appname);
                builder.setMessage("Do you want to delete this brand?");
                builder.setPositiveButton("Yes", (dialog, which) -> APIs.RemoveSellerBrand(this, this, item.getBrandId() + ""));
                builder.setNegativeButton("No", null);
                builder.show();
            } else if (view == mLl_add_product) {
                Intent intent = new Intent(getApplicationContext(), ImagePickerActivity.class);
                intent.putExtra(commonVariables.KEY_IS_BRAND, false);
                startActivity(intent);
            } else if (view == mNav_my_profile) {
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, MyProfileActivitySeller.class));
                overridePendingTransition(0, 0);
            }
//            else if (view == mNav_my_brands) {
//                drawer.closeDrawer(GravityCompat.START);
//                startActivity(new Intent(this, BrandsActivitySeller.class));
//                overridePendingTransition(0, 0);
//            }
            else if (view == mNav_my_products) {
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
            } else if (view == mLl_close || view == mIv_logo_nav || view == mTv_username) {
                drawer.closeDrawer(GravityCompat.START);
            } else if (view == mTv_logout) {
                drawer.closeDrawer(GravityCompat.START);
                commonMethods.logout(this, true);
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
                }
                if (strApiName.equalsIgnoreCase("RemoveSellerBrand")) {
                    int strresId = jobjWhole.optInt("resInt");
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setTitle(commonVariables.appname);
                    builder.setMessage(jobjWhole.optString("res"));
                    if (strresId == 1) {
                        builder.setPositiveButton("Ok", (dialog, which) -> {
                            AppPreferences.getPrefs().edit().putString(commonVariables.KEY_BRAND, "").apply();
                            setBrandVisibility(false, null);
                        });
                    } else {
                        builder.setPositiveButton("Ok", null);
                    }
                    builder.show();
                }
            } else {
                setBrandVisibility(false, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
