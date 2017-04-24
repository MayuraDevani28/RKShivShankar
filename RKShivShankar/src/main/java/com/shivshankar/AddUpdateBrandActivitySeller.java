package com.shivshankar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.classes.Brand;
import com.shivshankar.utills.AlertDialogManager;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResultString;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.json.JSONObject;

import java.io.File;

public class AddUpdateBrandActivitySeller extends BaseActivitySeller implements View.OnClickListener, OnResultString {
    ImageView mIv_imageView, mIv_change_image;
    EditText mEdt_brand_name;
    TextInputLayout mTi_brand_name;
    TextView mBtn_submit, mTv_title;
    Bitmap bmp;

    private ImageView mIv_close;
    Brand item;
    AlertDialog alDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
            super.onCreate(savedInstanceState);
            View rootView = getLayoutInflater().inflate(R.layout.activity_add_brand_seller, frameLayout);
            rootView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
            bindViews(rootView);

            Gson gson = new Gson();
            String json = AppPreferences.getPrefs().getString(commonVariables.KEY_BRAND, "");
            if (json.isEmpty()) {
                setImageFromPref();
            } else {
                item = gson.fromJson(json, Brand.class);
                setBrandData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBrandData() {
        try {
            if (!(item.getBrandId() == null || item.getBrandId().isEmpty())) {
                String strImageURL = item.getBrandLogo();
                if ((strImageURL != null) && (!strImageURL.equals(""))) {

                    Glide.with(this).load(strImageURL).asBitmap()
                            .placeholder(R.drawable.xml_round_gray).error(R.drawable.xml_round_white).listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            Log.v("TAGRK", "Exception");
                            e.printStackTrace();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Log.v("TAGRK", "Ready");
                            return false;
                        }
                    })
                            .into(new BitmapImageViewTarget(mIv_imageView) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    mIv_imageView.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                }
            }
            setTitleAndBrandName();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void bindViews(View rootView) {
        try {
            mIv_close = (ImageView) rootView.findViewById(R.id.iv_close);
            mIv_close.setOnClickListener(this);
            mIv_imageView = (ImageView) rootView.findViewById(R.id.iv_effectImg);
            mIv_change_image = (ImageView) rootView.findViewById(R.id.iv_change_image);
            mEdt_brand_name = (EditText) rootView.findViewById(R.id.edt_brand_name);
            mTi_brand_name = (TextInputLayout) rootView.findViewById(R.id.ti_brand_name);
            mTv_title = (TextView) rootView.findViewById(R.id.tv_title);
            mBtn_submit = (TextView) rootView.findViewById(R.id.btn_submit);

            mIv_change_image.setOnClickListener(this);

            mBtn_submit.setOnClickListener(this);
            mEdt_brand_name.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
                    mBtn_submit.performClick();
                    return true;
                }
                return false;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTitleAndBrandName() {
        if (!item.getBrandName().isEmpty())
            mEdt_brand_name.setText(item.getBrandName());
        if (!(item.getBrandId() == null || item.getBrandId().isEmpty())) {
            mTv_title.setText("Update Brand");
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View view) {
        try {
            AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            view.startAnimation(buttonClick);
            if (view == mIv_close) {
                onBackPressed();
            } else if (view == mBtn_submit) {
                if (!(item.getBrandId() == null || item.getBrandId().isEmpty()))
                    file = null;
                else
                    file = ImagePickerActivity.mFileTemp;
                String strBrandName = mEdt_brand_name.getText().toString().trim();
                if (strBrandName.isEmpty()) {
                    mTi_brand_name.setError("Brand name required");
                } else if (file == null) {
                    if (mIv_imageView.getDrawable() == null) {
                        AlertDialogManager.showDialog(this, "Please select brand logo", null);
                    } else {
                        Uri tempUri = commonMethods.getImageUri(getApplicationContext(), ((RoundedBitmapDrawable) mIv_imageView.getDrawable()).getBitmap());
                        file = new File(commonMethods.getRealPathFromURI(this, tempUri));
                        if (item == null || item.getBrandId() == null || item.getBrandId().isEmpty()) {
                            APIs.CreateSellerBrand(this, this, strBrandName, file, mIv_imageView);
                        } else
                            APIs.UpdateSellerBrand(this, this, strBrandName, file, mIv_imageView, item.getBrandId());
                    }
                } else {
                    if (item == null || item.getBrandId() == null || item.getBrandId().isEmpty()) {
                        APIs.CreateSellerBrand(this, this, strBrandName, file, mIv_imageView);
                    } else
                        APIs.UpdateSellerBrand(this, this, strBrandName, file, mIv_imageView, item.getBrandId());
                }
            } else if (view == mIv_change_image) {
                Intent intent = new Intent(getApplicationContext(), ImagePickerActivity.class);
                intent.putExtra(commonVariables.KEY_IS_BRAND, true);
                if (item != null) {
                    SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
                    Gson gson = new Gson();
                    item.setBrandName(mEdt_brand_name.getText().toString());
                    String json = gson.toJson(item);
                    editor.putString(commonVariables.KEY_BRAND, json);
                    editor.apply();
                }
                startActivityForResult(intent, commonVariables.REQUEST_ADD_UPDATE_BRAND);
//                overridePendingTransition(R.anim.slide_up, android.R.anim.fade_out);
            } else
                super.onClick(view);
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
                        setImageFromPref();
                        Gson gson = new Gson();
                        String json = AppPreferences.getPrefs().getString(commonVariables.KEY_BRAND, "");
                        if (!json.isEmpty()) {
                            item = gson.fromJson(json, Brand.class);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setImageFromPref() {
        String img = AppPreferences.getPrefs().getString("image", "");
        if (!img.isEmpty()) {
            byte[] byteArray = Base64.decode(img, Base64.DEFAULT);

            bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(getResources(), bmp);
            circularBitmapDrawable.setCircular(true);
            mIv_imageView.setImageDrawable(circularBitmapDrawable);
            mIv_imageView.setDrawingCacheEnabled(true);
            SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
            editor.putString("image", "");
            editor.apply();
        }
    }

    @Override
    public void onResult(String result) {
        try {
            if (result != null) {
                JSONObject job = new JSONObject(result);
                String strAPIName = job.optString("api");
                if (strAPIName.equalsIgnoreCase("CreateSellerBrand") || strAPIName.equalsIgnoreCase("UpdateSellerBrand")) {
                    int strresId = job.optInt("resInt");
                    Runnable listener = null;
                    if (strresId == 1) {
                        try {
                            listener = () -> {
                                {

//                                Intent intent = new Intent(getApplicationContext(), BrandsActivitySeller.class);
//                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                                byte[] byteArray = stream.toByteArray();
//                                String saveThis = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                    SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
                                    Gson gson = new Gson();
                                    item = new Brand(job.optString("itemId"), mEdt_brand_name.getText().toString(), job.optString("brandLogoPath"));
                                    String json = gson.toJson(item);
                                    editor.putString(commonVariables.KEY_BRAND, json);
                                    editor.apply();
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(intent);
                                    if (alDialog != null)
                                        alDialog.dismiss();
                                    Intent output = new Intent();
                                    output.putExtra(commonVariables.KEY_IS_BRAND_UPDATED, true);
//                                setResult(RESULT_OK, output);
                                    if (getParent() == null) {
                                        setResult(Activity.RESULT_OK, output);
                                    } else {
                                        getParent().setResult(Activity.RESULT_OK, output);
                                    }
                                    finish();
                                    overridePendingTransition(0, 0);
                                }
                            };
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    AlertDialogManager.showDialog(this, job.optString("res"), listener);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
