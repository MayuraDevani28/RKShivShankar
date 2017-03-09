package com.shivshankar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonVariables;

import org.json.JSONObject;


public class MainActivitySeller extends BaseActivitySeller implements View.OnClickListener, OnResult {

    LinearLayout mLl_create_brand, mLl_brand;
    ImageView mIv_change_image, mIv_effectImg;
    TextView mTv_brand_name;
    View view_top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        try {
            View rootView = getLayoutInflater().inflate(R.layout.activity_main_seller, frameLayout);
            mLl_create_brand = (LinearLayout) rootView.findViewById(R.id.ll_create_brand);
            mLl_create_brand.setOnClickListener(this);
            mLl_brand = (LinearLayout) rootView.findViewById(R.id.ll_brand);
            mIv_change_image = (ImageView) rootView.findViewById(R.id.iv_change_image);
            mIv_change_image.setOnClickListener(this);
            mIv_effectImg = (ImageView) rootView.findViewById(R.id.iv_effectImg);
            mTv_brand_name = (TextView) rootView.findViewById(R.id.tv_brand_name);
            view_top = rootView.findViewById(R.id.view_top);

            FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_icon);
            fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show());

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
//            try {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    swipeRefreshLayout.setProgressViewOffset(false, 0, 200);
//                }
//                swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_green_light);
//                swipeRefreshLayout.setOnRefreshListener(() -> {
//                    APIs.GetHomeBannerwithText(this, this);
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
//                overridePendingTransition(R.anim.slide_up, android.R.anim.fade_out);
            } else if (view == mIv_change_image) {
                Intent intent = new Intent(getApplicationContext(), AddBrandActivitySeller.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResult(JSONObject result) {
        try {
            swipeRefreshLayout.setRefreshing(false);
            Log.v("TAG", "RESULT: " + result.toString());
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
