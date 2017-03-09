package com.shivshankar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.commonVariables;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.ByteArrayOutputStream;

public class AddProductActivitySeller extends BaseActivitySeller implements View.OnClickListener {
    ImageView mIv_effectImg, mIv_change_image;
    EditText mEdt_brand_name, mEdt_price;
    TextView mBtn_submit;
    private Button mBtn_add_brand;
    private MaterialBetterSpinner mSp_top_fabrics, mSp_bottom_fabrics, mSp_top_dupatta, mSp_all_over, mSp_type, mSp_min_qty;

    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        View rootView = getLayoutInflater().inflate(R.layout.activity_add_product_seller, frameLayout);
        bindViews(rootView);
    }

    private void bindViews(View rootView) {
        mIv_effectImg = (ImageView) rootView.findViewById(R.id.iv_effectImg);
        mIv_change_image = (ImageView) rootView.findViewById(R.id.iv_change_image);
        mEdt_brand_name = (EditText) rootView.findViewById(R.id.edt_brand_name);
        mBtn_submit = (TextView) rootView.findViewById(R.id.btn_submit);
        mBtn_add_brand = (Button) findViewById(R.id.btn_add_brand);
        mSp_top_fabrics = (MaterialBetterSpinner) findViewById(R.id.sp_top_fabrics);
        mSp_bottom_fabrics = (MaterialBetterSpinner) findViewById(R.id.sp_bottom_fabrics);
        mSp_top_dupatta = (MaterialBetterSpinner) findViewById(R.id.sp_top_dupatta);
        mSp_all_over = (MaterialBetterSpinner) findViewById(R.id.sp_all_over);
        mSp_type = (MaterialBetterSpinner) findViewById(R.id.sp_type);
        mEdt_price = (EditText) findViewById(R.id.edt_price);
        mSp_min_qty = (MaterialBetterSpinner) findViewById(R.id.sp_min_qty);


        mIv_change_image.setOnClickListener(this);
        try {
            byte[] byteArray = Base64.decode(AppPreferences.getPrefs().getString("image_product", ""), Base64.DEFAULT);
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
        mEdt_brand_name.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                mBtn_submit.performClick();
                return true;
            }
            return false;
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
            if (view == mBtn_submit) {
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
                    editor.putString("image_product", saveThis);
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
