package com.shivshankar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.classes.Brand;
import com.shivshankar.classes.ProductItem;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.OnResultString;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import static com.shivshankar.utills.AppPreferences.getPrefs;
import static com.shivshankar.utills.commonVariables.REQUEST_ADD_UPDATE_PRODUCT;


public class AddUpdateProductActivitySeller extends BaseActivitySeller implements View.OnClickListener, OnResult, OnResultString, CompoundButton.OnCheckedChangeListener {
    ImageView mIv_imageView, mIv_change_image, mIv_close, mIv_brand;
    EditText mEdt_brand_name, mEdt_price, mEdt_min_qty;
    TextView mBtn_submit, mTv_title;
    private Button mBtn_add_brand;
    LinearLayout mLl_top_bottom_fab;
    private MaterialBetterSpinner mSp_top_fabrics, mSp_bottom_fabrics, mSp_dupatta, mSp_all_over, mSp_Category, mSp_Type;
    CheckBox mCb_all_over;
    Brand item;
    String strTop, strBottom, strDupatta, strAllOver, strCategory, strFabricType, productId = "0";
    AlertDialog alDialog;

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
    ProductItem product;

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

            setDropdowns();
            APIs.GetDropdowns(null, this);

            Gson gson = new Gson();
            String json = getIntent().getStringExtra(commonVariables.KEY_PRODUCT);
            if (json == null || json.isEmpty()) {
                setCapturedProductImage();
            } else {
                product = gson.fromJson(json, ProductItem.class);
                if (product != null) {
                    setProductData(product);
                    productId = product.getProductId();
                    APIs.GetProductDetail_Suit_Seller(this, this, product.getProductId());
                }
            }

            sv.setOnTouchListener((v, event) -> false);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCapturedProductImage() {
        try {
            byte[] byteArray = Base64.decode(getPrefs().getString("image_product", ""), Base64.DEFAULT);
            bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(getResources(), bmp);
            circularBitmapDrawable.setCircular(true);
            mIv_imageView.setImageDrawable(circularBitmapDrawable);
            mIv_imageView.setDrawingCacheEnabled(true);

            if (!(productId == null || productId.isEmpty())) {
                mTv_title.setText("Update Product");
            }
            SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
            editor.putString("image_product", "");
            editor.apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setProductData(ProductItem product) {
        try {
            mEdt_min_qty.setText(product.getMinOrderQty() + "");
            mEdt_price.setText(product.getOfferPrice() + "");

            String strImageURL = product.getImageName();
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
                }).into(new BitmapImageViewTarget(mIv_imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        mIv_imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }

//            strFabricType = product.get;
//            mSp_Type.setSelection(getPosAlloverFabric(strFabricType, VAL_TYPE));

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
        mIv_imageView = (ImageView) rootView.findViewById(R.id.iv_effectImg);
        mIv_brand = (ImageView) rootView.findViewById(R.id.iv_brand);
        mIv_change_image = (ImageView) rootView.findViewById(R.id.iv_change_image);
        mEdt_brand_name = (EditText) rootView.findViewById(R.id.edt_brand_name);
        mBtn_submit = (TextView) rootView.findViewById(R.id.btn_submit);
        mBtn_add_brand = (Button) findViewById(R.id.btn_add_brand);
        mBtn_add_brand.setOnClickListener(this);
        mLl_top_bottom_fab = (LinearLayout) findViewById(R.id.ll_top_bottom_fab);
        mSp_top_fabrics = (MaterialBetterSpinner) findViewById(R.id.sp_top_fabrics);
        mSp_bottom_fabrics = (MaterialBetterSpinner) findViewById(R.id.sp_bottom_fabrics);
        mSp_dupatta = (MaterialBetterSpinner) findViewById(R.id.sp_dupatta);
        mSp_all_over = (MaterialBetterSpinner) findViewById(R.id.sp_all_over);
        mSp_Category = (MaterialBetterSpinner) findViewById(R.id.sp_category);
        mSp_Type = (MaterialBetterSpinner) findViewById(R.id.sp_type);
        mEdt_price = (EditText) findViewById(R.id.edt_price);
        mEdt_min_qty = (EditText) findViewById(R.id.edt_min_qty);
        mCb_all_over = (CheckBox) findViewById(R.id.cb_all_over);
        mCb_all_over.setOnCheckedChangeListener(this);
        mTv_title = (TextView) rootView.findViewById(R.id.tv_title);

        mIv_change_image.setOnClickListener(this);
        setBrandData();

        mBtn_submit.setOnClickListener(this);
        mEdt_min_qty.setOnEditorActionListener((v1, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            Gson gson = new Gson();
            String json = getPrefs().getString(commonVariables.KEY_BRAND, "");
            item = gson.fromJson(json, Brand.class);
            setBrandData();

            if (requestCode == REQUEST_ADD_UPDATE_PRODUCT && resultCode == RESULT_OK) {
                if (data != null) {
                    boolean isBrandUpdatedChanged = data.getExtras().getBoolean(commonVariables.KEY_IS_PRODUCT_UPDATED);
                    if (isBrandUpdatedChanged) {
                        setCapturedProductImage();
//                        Gson gson = new Gson();
//                        json = getPrefs().getString(commonVariables.KEY_PRODUCT, "");
//                        product = gson.fromJson(json, Pro.class);
//                        setBrandData();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBrandData() {
        try {
            String json = getPrefs().getString(commonVariables.KEY_BRAND, "");
            if (!json.isEmpty()) {
                Gson gson = new Gson();
                item = gson.fromJson(json, Brand.class);
                if (!item.getBrandName().isEmpty()) {
                    mEdt_brand_name.setText(item.getBrandName());
                    mEdt_brand_name.setEnabled(false);
                    mBtn_add_brand.setText("Edit");

                    String strImageURL = item.getBrandLogo();
                    if ((strImageURL != null) && (!strImageURL.equals(""))) {
                        mIv_brand.setVisibility(View.VISIBLE);
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
                        }).into(new BitmapImageViewTarget(mIv_brand) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                mIv_brand.setImageDrawable(circularBitmapDrawable);
                            }
                        });
                    } else {
                        mIv_brand.setVisibility(View.GONE);
                    }
                } else
                    mBtn_add_brand.setText("Add");
            } else {
                mBtn_add_brand.setText("Add");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        try {
            if (view == mBtn_add_brand) {
                String strTxt = mBtn_add_brand.getText().toString();
                if (strTxt.equalsIgnoreCase("Add")) {
                    Intent intent = new Intent(getApplicationContext(), ImagePickerActivity.class);
                    intent.putExtra(commonVariables.KEY_IS_BRAND, true);
                    startActivityForResult(intent, commonVariables.REQUEST_ADD_UPDATE_BRAND_PRODUCT);
                    overridePendingTransition(0, 0);
                } else {
                    Intent intent = new Intent(getApplicationContext(), AddUpdateBrandActivitySeller.class);
                    SharedPreferences.Editor editor = getPrefs().edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(item);
                    editor.putString(commonVariables.KEY_BRAND, json);
                    editor.apply();
                    startActivityForResult(intent, commonVariables.REQUEST_ADD_UPDATE_BRAND_PRODUCT);
                    overridePendingTransition(0, 0);
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
                file = ImagePickerActivity.mFileTemp;
                String strBrandName = mEdt_brand_name.getText().toString().trim(),
                        strMinQty = mEdt_min_qty.getText().toString().trim(),
                        strPrice = mEdt_price.getText().toString().trim();

                if (file == null && mIv_imageView.getDrawable() == null) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setTitle(commonVariables.appname);
                    builder.setMessage("Please select product image");
                    builder.setPositiveButton("Ok", null);
                    builder.show();
                } else if (strBrandName.isEmpty()) {
                    mEdt_brand_name.setError("Brand name required");
                } else if (mLl_top_bottom_fab.getVisibility() == View.GONE && strAllOver.isEmpty()) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setTitle(commonVariables.appname);
                    builder.setMessage("Please select All over fabric");
                    builder.setPositiveButton("Ok", null);
                    builder.show();
                } else if (mLl_top_bottom_fab.getVisibility() == View.VISIBLE && (strTop.isEmpty() || strBottom.isEmpty() || strDupatta.isEmpty())) {
                    String type = "";
                    if (strTop.isEmpty())
                        type = "Top";
                    if (strBottom.isEmpty()) {
                        if (type.isEmpty())
                            type = "Bottom";
                        else
                            type = type + ", Bottom";
                    }
                    if (strDupatta.isEmpty()) {
                        if (type.isEmpty())
                            type = "Dupatta";
                        else
                            type = type + ", Dupatta";
                    }
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setTitle(commonVariables.appname);
                    builder.setMessage("Please select " + type + " fabric");
                    builder.setPositiveButton("Ok", null);
                    builder.show();
                } else if (strCategory.isEmpty()) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setTitle(commonVariables.appname);
                    builder.setMessage("Please select category");
                    builder.setPositiveButton("Ok", null);
                    builder.show();
                } else if (strFabricType.isEmpty()) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setTitle(commonVariables.appname);
                    builder.setMessage("Please select fabric type");
                    builder.setPositiveButton("Ok", null);
                    builder.show();
                } else if (strPrice.isEmpty() || Float.parseFloat(strPrice) == 0) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setTitle(commonVariables.appname);
                    builder.setMessage("Please select price");
                    builder.setPositiveButton("Ok", null);
                    builder.show();
                } else if (strMinQty.isEmpty() || Integer.parseInt(strMinQty) == 0) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setTitle(commonVariables.appname);
                    builder.setMessage("Please select min order quantity");
                    builder.setPositiveButton("Ok", null);
                    builder.show();
                } else {
                    if (file == null && mIv_imageView.getDrawable() != null) {
                        try {
                            Uri tempUri = commonMethods.getImageUri(getApplicationContext(), ((RoundedBitmapDrawable) mIv_imageView.getDrawable()).getBitmap());
                            file = new File(commonMethods.getRealPathFromURI(this, tempUri));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (mCb_all_over.isChecked()) {
                        strTop = "";
                        strBottom = "";
                        strDupatta = "";
                    } else
                        strAllOver = "";
                    if (productId == null || productId.isEmpty() || productId.equalsIgnoreCase("null"))
                        productId = "0";
                    APIs.AddUpdateProduct_Suit(this, this, productId, item.getBrandId(), strCategory, strTop, strBottom, strDupatta, strAllOver, mCb_all_over.isChecked(), strFabricType, strPrice, strMinQty, file, mIv_imageView);
//                        } else
//                            APIs.UpdateSellerBrand(this, this, strBrandName, file, mIv_imageView, item.getBrandId());
                }
            } else if (view == mIv_change_image) {
                Intent intent = new Intent(getApplicationContext(), ImagePickerActivity.class);
                intent.putExtra(commonVariables.KEY_IS_BRAND, false);
                Gson gson = new Gson();
                String json = gson.toJson(product);
                intent.putExtra(commonVariables.KEY_PRODUCT, json);
                startActivityForResult(intent, REQUEST_ADD_UPDATE_PRODUCT);
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
                JSONObject job = jobjWhole.optJSONObject("resData");
                if (strApiName.equalsIgnoreCase("GetDropdowns")) {
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
                } else if (strApiName.equalsIgnoreCase("GetProductDetail_Suit_Seller")) {
                    item = new Brand(job.optString("BrandId"), job.optString("BrandName"), job.optString("BrandLogo"));
                    setBrandData();
                    product.setBrand(item);
                    product.setAllOver(job.optBoolean("IsAllOver"));
                    product.setFabricId(job.optString("FabricId"));
                    product.setFabricName(job.optString("FabricName"));
                    product.setDupattaFabricId(job.optString("DupattaFabricId"));
                    product.setDupattaFabricName(job.optString("DupattaFabricName"));
                    product.setBottomFabricName(job.optString("BottomFabricId"));
                    product.setBottomFabricName(job.optString("BottomFabricName"));
                    product.setTopFabricId(job.optString("TopFabricId"));
                    product.setTopFabricName(job.optString("TopFabricName"));
                    product.setFabricType(job.optString("FabricType"));
                    product.setCategoryId(job.optString("CategoryId"));
                    product.setCategoryName(job.optString("CategoryName"));

                    if (product.isAllOver()) {
                        mCb_all_over.setChecked(true);
                        strAllOver = product.getFabricId();
                        mSp_all_over.setText(product.getFabricName());
                    } else {
                        mCb_all_over.setChecked(false);
                        strDupatta = product.getDupattaFabricId();
                        mSp_dupatta.setText(product.getDupattaFabricName());
                        strBottom = product.getBottomFabricId();
                        mSp_bottom_fabrics.setText(product.getBottomFabricName());
                        strTop = product.getTopFabricId();
                        mSp_top_fabrics.setText(product.getTopFabricName());
                    }

                    strFabricType = product.getFabricType();
                    mSp_Type.setText(product.getFabricType());

                    strCategory = product.getCategoryId();
                    mSp_Category.setText(product.getCategoryName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private int getPosAlloverFabric(String allFabId, String[] VAL) {
//        int pos = 0;
//        for (int i = 0; i < VAL.length; i++) {
//            if (VAL[i].equalsIgnoreCase(allFabId)) {
//                pos = i;
//                break;
//            }
//        }
//        return pos;
//    }

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
                strFabricType = val_type[i];
            });
            mSp_Type.setText("");
            strFabricType = "";
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
            mSp_dupatta.setAdapter(arrayAdapter);
            mSp_dupatta.setOnItemClickListener((adapterView, view, i, l) -> {
                strDupatta = val_dupatta[i];
            });
            mSp_dupatta.setText("");
            strDupatta = "";
            mSp_dupatta.clearFocus();
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

    private void setDropdowns() {
        setTopFabricData(SP_TOP, VAL_TOP);
        setBottomFabricData(SP_BOTTOM, VAL_BOTTOM);
        setDupattaData(SP_DUPATTA, VAL_DUPATTA);
        setAllOverData(SP_ALL_OVER, VAL_ALL_OVER);
        setCategoryData(SP_CATEGOTY, VAL_CATEGOTY);
        setTypeData(SP_TYPE, VAL_TYPE);
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            mSp_top_fabrics.setVisibility(View.GONE);
            mSp_bottom_fabrics.setVisibility(View.GONE);
            mSp_dupatta.setVisibility(View.GONE);
            mSp_all_over.setVisibility(View.VISIBLE);
            mLl_top_bottom_fab.setVisibility(View.GONE);
        } else {
            mSp_top_fabrics.setVisibility(View.VISIBLE);
            mSp_bottom_fabrics.setVisibility(View.VISIBLE);
            mSp_dupatta.setVisibility(View.VISIBLE);
            mSp_all_over.setVisibility(View.GONE);
            mLl_top_bottom_fab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResult(String result) {
        try {
            if (result != null) {
                JSONObject job = new JSONObject(result);
                String strAPIName = job.optString("api");
                if (strAPIName.equalsIgnoreCase("AddUpdateProduct_Suit")) {
                    int strresId = job.optInt("resInt");
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setTitle(commonVariables.appname);
                    builder.setMessage(job.optString("res"));
                    if (strresId == 1) {
                        try {
                            builder.setPositiveButton("Ok", (dialog, which) -> {
                                Intent intent = new Intent(getApplicationContext(), ProductsActivitySeller.class);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(0, 0);
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        builder.setPositiveButton("Ok", null);
                    }
                    alDialog = builder.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
