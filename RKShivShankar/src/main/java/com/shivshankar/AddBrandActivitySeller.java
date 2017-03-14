package com.shivshankar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import java.io.ByteArrayOutputStream;

public class AddBrandActivitySeller extends BaseActivitySeller implements View.OnClickListener {
    ImageView mIv_effectImg, mIv_change_image;
    EditText mEdt_brand_name;
    TextView mBtn_submit;
    Bitmap bmp;

    private ImageView mIv_logo_nav, mIv_logo_toolbar;
    private TextView mTv_username, mTv_logout;
    private LinearLayout mNav_my_profile, mNav_my_products, mNav_notification, mNav_change_pass, mLl_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        View rootView = getLayoutInflater().inflate(R.layout.activity_add_brand_seller, frameLayout);
        bindViews(rootView);
    }

    private void bindViews(View rootView) {
        mIv_effectImg = (ImageView) rootView.findViewById(R.id.iv_effectImg);
        mIv_change_image = (ImageView) rootView.findViewById(R.id.iv_change_image);
        mEdt_brand_name = (EditText) rootView.findViewById(R.id.edt_brand_name);
        mBtn_submit = (TextView) rootView.findViewById(R.id.btn_submit);

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


        mIv_change_image.setOnClickListener(this);
        try {
            byte[] byteArray = Base64.decode(AppPreferences.getPrefs().getString("image", ""), Base64.DEFAULT);
            String strBrandName = AppPreferences.getPrefs().getString(commonVariables.KEY_BRAND_NAME, "");

            bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(getResources(), bmp);
            circularBitmapDrawable.setCircular(true);
            mIv_effectImg.setImageDrawable(circularBitmapDrawable);
            mIv_effectImg.setDrawingCacheEnabled(true);
            if (!strBrandName.isEmpty())
                mEdt_brand_name.setText(strBrandName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mBtn_submit.setOnClickListener(this);
        mEdt_brand_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    mBtn_submit.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View view) {
        try {
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
            } else if (view == mBtn_submit) {
                String strBrandName = mEdt_brand_name.getText().toString().trim();
                if (strBrandName.isEmpty()) {
                    mEdt_brand_name.setError("Brand name required");
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivitySeller.class);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    String saveThis = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
                    editor.putString("image", saveThis);
                    editor.putString(commonVariables.KEY_BRAND_NAME, strBrandName);
                    editor.apply();

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0, 0);
                }
            } else if (view == mIv_change_image) {
                Intent intent = new Intent(getApplicationContext(), ImagePickerActivity.class);
                intent.putExtra(commonVariables.KEY_IS_BRAND, true);
                startActivity(intent);
//                overridePendingTransition(R.anim.slide_up, android.R.anim.fade_out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
