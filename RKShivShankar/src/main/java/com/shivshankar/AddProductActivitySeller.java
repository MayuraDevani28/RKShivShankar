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
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.classes.Brand;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import static com.shivshankar.utills.AppPreferences.getPrefs;

public class AddProductActivitySeller extends BaseActivitySeller implements View.OnClickListener, OnResult {
    ImageView mIv_effectImg, mIv_change_image, mIv_close;
    EditText mEdt_product_name, mEdt_price, mEdt_min_qty;
    TextView mBtn_submit, mTv_title;
    private Button mBtn_add_brand;
    private MaterialBetterSpinner mSp_top_fabrics, mSp_bottom_fabrics, mSp_top_dupatta, mSp_all_over, mSp_Category, mSp_Type;
    String strTop, strBottom, strDupatta, strAllOver, strCategory, strType, productId;

    String[] SP_TOP = {"Top Fabrics"};
    String[] VAL_TOP = {""};
    String[] SP_BOTTOM = {"Bottom Fabrics"};
    String[] VAL_BOTTOM = {""};
    String[] SP_DUPATTA = {"Dupatta"};
    String[] VAL_DUPATTA = {""};
    String[] SP_ALL_OVER = {"All Over"};
    String[] VAL_ALL_OVER = {""};
    String[] SP_TYPE = {"Type"};
    String[] VAL_TYPE = {""};
    String[] SP_CATEGOTY = {"Category"};
    String[] VAL_CATEGOTY = {""};
    Bitmap bmp;
    ScrollView sv;

    private ImageView mIv_logo_nav, mIv_logo_toolbar;
    private TextView mTv_username, mTv_logout;
    private LinearLayout mNav_my_profile, mNav_my_products, mNav_notification, mNav_change_pass, mLl_close;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        try {
            View rootView = getLayoutInflater().inflate(R.layout.activity_add_product_seller, frameLayout);
            bindViews(rootView);

            productId = getIntent().getStringExtra(commonVariables.INTENT_EXTRA_PRODUCT_ID);

            sv.setOnTouchListener((v, event) -> false);
            setDropdowns();
            APIs.GetDropdowns(this, this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setCategoryData(String[] sp_category, String[] val_category) {
        try {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, sp_category);
            mSp_Category.setAdapter(arrayAdapter);
            mSp_Category.setOnItemClickListener((adapterView, view, i, l) -> {
                strCategory = val_category[i];
            });
            mSp_Category.setText("");
            strCategory = "";
            mSp_Category.clearFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTypeData(String[] sp_type, String[] val_type) {
        try {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, sp_type);
            mSp_Type.setAdapter(arrayAdapter);
            mSp_Type.setOnItemClickListener((adapterView, view, i, l) -> {
                strType = val_type[i];
            });
            mSp_Type.setText("");
            strType = "";
            mSp_Type.clearFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAllOverData(String[] sp_all_over, String[] val_all_over) {
        try {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, sp_all_over);
            mSp_all_over.setAdapter(arrayAdapter);
            mSp_all_over.setOnItemClickListener((adapterView, view, i, l) -> {
                strAllOver = val_all_over[i];
            });
            mSp_all_over.setText("");
            strAllOver = "";
            mSp_all_over.clearFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDupattaData(String[] sp_dupatta, String[] val_dupatta) {
        try {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, sp_dupatta);
            mSp_top_dupatta.setAdapter(arrayAdapter);
            mSp_top_dupatta.setOnItemClickListener((adapterView, view, i, l) -> {
                strDupatta = val_dupatta[i];
            });
            mSp_top_dupatta.setText("");
            strDupatta = "";
            mSp_top_dupatta.clearFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBottomFabricData(String[] sp_bottom, String[] val_bottom) {
        try {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, sp_bottom);
            mSp_bottom_fabrics.setAdapter(arrayAdapter);
            mSp_bottom_fabrics.setOnItemClickListener((adapterView, view, i, l) -> {
                strBottom = val_bottom[i];
            });
            mSp_bottom_fabrics.setText("");
            strBottom = "";
            mSp_bottom_fabrics.clearFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTopFabricData(String[] sp_top, String[] val_top) {
        try {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, sp_top);
            mSp_top_fabrics.setAdapter(arrayAdapter);
            mSp_top_fabrics.setOnItemClickListener((adapterView, view, i, l) -> {
                strTop = val_top[i];
            });
            mSp_top_fabrics.setText("");
            strTop = "";
            mSp_top_fabrics.clearFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindViews(View rootView) {
        mIv_close = (ImageView) rootView.findViewById(R.id.iv_close);
        mIv_close.setOnClickListener(this);
        mIv_logo_nav = (ImageView) findViewById(R.id.iv_logo_nav);
        mIv_logo_nav.setOnClickListener(this);
        mIv_logo_toolbar = (ImageView) findViewById(R.id.iv_logo_toolbar);
        mIv_logo_toolbar.setOnClickListener(this);
        mTv_username = (TextView) findViewById(R.id.tv_username);
        mTv_username.setOnClickListener(this);
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

        sv = (ScrollView) rootView.findViewById(R.id.sv);
        mIv_effectImg = (ImageView) rootView.findViewById(R.id.iv_effectImg);
        mIv_change_image = (ImageView) rootView.findViewById(R.id.iv_change_image);
        mEdt_product_name = (EditText) rootView.findViewById(R.id.edt_brand_name);
        mBtn_submit = (TextView) rootView.findViewById(R.id.btn_submit);
        mBtn_add_brand = (Button) findViewById(R.id.btn_add_brand);
        mBtn_add_brand.setOnClickListener(this);
        mSp_top_fabrics = (MaterialBetterSpinner) findViewById(R.id.sp_top_fabrics);
        mSp_bottom_fabrics = (MaterialBetterSpinner) findViewById(R.id.sp_bottom_fabrics);
        mSp_top_dupatta = (MaterialBetterSpinner) findViewById(R.id.sp_top_dupatta);
        mSp_all_over = (MaterialBetterSpinner) findViewById(R.id.sp_all_over);
        mSp_Category = (MaterialBetterSpinner) findViewById(R.id.sp_category);
        mSp_Type = (MaterialBetterSpinner) findViewById(R.id.sp_type);
        mEdt_price = (EditText) findViewById(R.id.edt_price);
        mEdt_min_qty = (EditText) findViewById(R.id.edt_min_qty);
        mTv_title = (TextView) rootView.findViewById(R.id.tv_title);

        mIv_change_image.setOnClickListener(this);
        try {
            byte[] byteArray = Base64.decode(getPrefs().getString("image_product", ""), Base64.DEFAULT);
            bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(getResources(), bmp);
            circularBitmapDrawable.setCircular(true);
            mIv_effectImg.setImageDrawable(circularBitmapDrawable);
            mIv_effectImg.setDrawingCacheEnabled(true);
            String json = AppPreferences.getPrefs().getString(commonVariables.KEY_BRAND, "");
            if (!json.isEmpty()) {
                Gson gson = new Gson();
                Brand item = gson.fromJson(json, Brand.class);
                if (!item.getBrandName().isEmpty()) {
                    mEdt_product_name.setText(item.getBrandName());
                    mEdt_product_name.setEnabled(false);
                    mBtn_add_brand.setText("Edit");
                } else
                    mBtn_add_brand.setText("Add");
            }
            if (!(productId == null || productId.isEmpty())) {
                mTv_title.setText("Update Product");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mBtn_submit.setOnClickListener(this);
        mEdt_product_name.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                mBtn_submit.performClick();
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            if (mTv_username != null) {
                String strProfile = getPrefs().getString(commonVariables.KEY_LOGIN_SELLER_PROFILE, "");
                if (!strProfile.isEmpty() && !strProfile.equalsIgnoreCase("null"))
                    mTv_username.setText(WordUtils.capitalizeFully(new JSONObject(strProfile).optString("SellerName")));
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

    @Override
    public void onClick(View view) {
        try {
            if (view == mBtn_add_brand) {
                String strTxt = mBtn_add_brand.getText().toString();
                if (strTxt.equalsIgnoreCase("Add")) {

                } else {

                }
            } else if (view == mIv_logo_toolbar) {
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
            } else if (view == mLl_close || view == mIv_logo_nav || view == mTv_username) {
                drawer.closeDrawer(GravityCompat.START);
            } else if (view == mTv_logout) {
                drawer.closeDrawer(GravityCompat.START);
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle(commonVariables.appname);
                builder.setMessage("Do you want to logout?");
                builder.setPositiveButton("Logout", (arg0, arg1) -> commonMethods.logout(this));
                builder.setNegativeButton("Cancel", null);
                builder.show();
            } else if (view == mIv_close) {
                onBackPressed();
            } else if (view == mBtn_submit) {
                String strBrandName = mEdt_product_name.getText().toString().trim();
                if (strBrandName.isEmpty()) {
                    mEdt_product_name.setError("Brand name required");
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivitySeller.class);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    String saveThis = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    SharedPreferences.Editor editor = getPrefs().edit();
                    editor.putString("image_product", saveThis);
                    editor.apply();

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0, 0);
                }
            } else if (view == mIv_change_image) {
                Intent intent = new Intent(getApplicationContext(), ImagePickerActivity.class);
                intent.putExtra(commonVariables.KEY_IS_BRAND, false);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResult(JSONObject jobjWhole) {
        try {
            if (jobjWhole != null) {
                String strApiName = jobjWhole.optString("api");
                if (strApiName.equalsIgnoreCase("GetDropdowns")) {
                    JSONObject job = jobjWhole.optJSONObject("resData");
                    JSONArray jarr = job.optJSONArray("lstFabric");
                    int l = jarr.length();
                    SP_TOP = new String[l];
                    VAL_TOP = new String[l];
                    SP_BOTTOM = new String[l];
                    VAL_BOTTOM = new String[l];
                    SP_DUPATTA = new String[l];
                    VAL_DUPATTA = new String[l];
                    SP_ALL_OVER = new String[l];
                    VAL_ALL_OVER = new String[l];

                    for (int i = 0; i < jarr.length(); i++) {
                        JSONObject jo = jarr.optJSONObject(i);
                        String name = jo.optString("FabricName");
                        String id = jo.optString("FabricId");
                        SP_TOP[i] = name;
                        VAL_TOP[i] = id;
                        SP_BOTTOM[i] = name;
                        VAL_BOTTOM[i] = id;
                        SP_DUPATTA[i] = name;
                        VAL_DUPATTA[i] = id;
                        SP_ALL_OVER[i] = name;
                        VAL_ALL_OVER[i] = id;
                    }

                    jarr = job.optJSONArray("lstCategory");
                    l = jarr.length();
                    SP_CATEGOTY = new String[l];
                    VAL_CATEGOTY = new String[l];
                    for (int i = 0; i < jarr.length(); i++) {
                        JSONObject jo = jarr.optJSONObject(i);
                        String name = jo.optString("CategoryName");
                        String id = jo.optString("CategoryId");
                        SP_CATEGOTY[i] = name;
                        VAL_CATEGOTY[i] = id;
                    }

                    jarr = job.optJSONArray("lstFabricType");
                    l = jarr.length();
                    SP_TYPE = new String[l];
                    VAL_TYPE = new String[l];
                    for (int i = 0; i < jarr.length(); i++) {
                        JSONObject jo = jarr.optJSONObject(i);
                        String name = jo.optString("Name");
                        String id = jo.optString("Id");
                        SP_TYPE[i] = name;
                        VAL_TYPE[i] = id;
                    }
                    setDropdowns();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDropdowns() {
        setTopFabricData(SP_TOP, VAL_TOP);
        setBottomFabricData(SP_BOTTOM, VAL_BOTTOM);
        setDupattaData(SP_DUPATTA, VAL_DUPATTA);
        setAllOverData(SP_ALL_OVER, VAL_ALL_OVER);
        setCategoryData(SP_CATEGOTY, VAL_CATEGOTY);
        setTypeData(SP_TYPE, VAL_TYPE);
    }


}
