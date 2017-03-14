package com.shivshankar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.json.JSONObject;


public class MainActivitySeller extends BaseActivitySeller implements View.OnClickListener, OnResult {

    LinearLayout mLl_create_brand, mLl_add_product, mLl_brand;
    ImageView mIv_change_image, mIv_effectImg;
    TextView mTv_brand_name;
    View view_top;
    private ImageView mIv_logo_nav, mIv_logo_toolbar;
    private TextView mTv_username, mTv_logout;
    private LinearLayout mNav_my_profile, mNav_my_products, mNav_notification, mNav_change_pass, mLl_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        try {
            View rootView = getLayoutInflater().inflate(R.layout.activity_main_seller, frameLayout);
            bindViews(rootView);

            try {
                byte[] byteArray = Base64.decode(AppPreferences.getPrefs().getString("image", ""), Base64.DEFAULT);
                String strBrandName = AppPreferences.getPrefs().getString(commonVariables.KEY_BRAND_NAME, "");

                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), bmp);
                circularBitmapDrawable.setCircular(true);

                setBrandVisibility(!strBrandName.isEmpty(), circularBitmapDrawable, strBrandName);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            new ServerSMS().execute();
//            APIs.GetHomeBannerwithText(this, this);
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    swipeRefreshLayout.setProgressViewOffset(false, 0, 200);
                }
                swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_green_light);
                swipeRefreshLayout.setOnRefreshListener(() -> {
                    swipeRefreshLayout.setRefreshing(false);
//                    APIs.GetHomeBannerwithText(this, this);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindViews(View rootView) {
        mLl_create_brand = (LinearLayout) rootView.findViewById(R.id.ll_create_brand);
        mLl_create_brand.setOnClickListener(this);
        mLl_brand = (LinearLayout) rootView.findViewById(R.id.ll_brand);
        mLl_add_product = (LinearLayout) rootView.findViewById(R.id.ll_add_product);
        mLl_add_product.setOnClickListener(this);
        mIv_change_image = (ImageView) rootView.findViewById(R.id.iv_change_image);
        mIv_change_image.setOnClickListener(this);
        mIv_effectImg = (ImageView) rootView.findViewById(R.id.iv_effectImg);
        mTv_brand_name = (TextView) rootView.findViewById(R.id.tv_brand_name);
        view_top = rootView.findViewById(R.id.view_top);


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
    }

    private void setBrandVisibility(boolean b, RoundedBitmapDrawable circularBitmapDrawable, String strBrandName) {
        try {
            if (b) {
                mLl_brand.setVisibility(View.VISIBLE);
                mLl_create_brand.setVisibility(View.GONE);
                if (circularBitmapDrawable != null) {
                    mIv_effectImg.setImageDrawable(circularBitmapDrawable);
                    mIv_effectImg.setDrawingCacheEnabled(true);
                    mTv_brand_name.setText(strBrandName);
                    view_top.setVisibility(View.GONE);
                }
            } else {
                mLl_brand.setVisibility(View.GONE);
                mLl_create_brand.setVisibility(View.VISIBLE);
                view_top.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        try {
            if (view == mLl_create_brand) {
                Intent intent = new Intent(getApplicationContext(), ImagePickerActivity.class);
                intent.putExtra(commonVariables.KEY_IS_BRAND, true);
                startActivity(intent);
            } else if (view == mIv_change_image) {
                Intent intent = new Intent(getApplicationContext(), AddBrandActivitySeller.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            } else if (view == mLl_add_product) {
                Intent intent = new Intent(getApplicationContext(), ImagePickerActivity.class);
                intent.putExtra(commonVariables.KEY_IS_BRAND, false);
                startActivity(intent);
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

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResult(JSONObject result) {
        try {
            swipeRefreshLayout.setRefreshing(false);
            Log.v("TAGRK", "RESULT: " + result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* public class ServerSMS extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            return sendSms();
        }
    }
    public String sendSms() {
        try {
            // Construct data
            String user = "username=" + "zalakdoors@gmail.com";
            String hash = "&hash=" + "1c9dcb84aff3c39f6112e51dd75da09553d2dfbfa6e600277b8331ce8d586cad";
            String message = "&message=" + "This is your message";
            String sender = "&sender=" + "TXTLCL";
            String numbers = "&numbers=" + "917572807928";

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("http://api.textlocal.in/send/?").openConnection();
            String data = user + hash + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();
            Log.v(commonVariables.TAG, "RESPONSE: " + stringBuffer.toString());
            return stringBuffer.toString();
        } catch (Exception e) {
            System.out.println("Error SMS " + e);
            return "Error " + e;
        }
    }*/

}
