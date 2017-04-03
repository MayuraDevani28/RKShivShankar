package com.shivshankar;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.adapters.ProductsAdapterBuyer;
import com.shivshankar.classes.Brand;
import com.shivshankar.classes.ProductItem;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.shivshankar.utills.commonVariables.REQUEST_LOGIN;

public class ProductsActivityBuyer extends BaseActivityBuyer implements OnClickListener, OnResult {

    TextView mTv_no_data_found, mTv_title, mTv_count_items;
    Button mBtn_add_now;
    private LinearLayout mLl_no_data_found;
    public RecyclerView mRv_items;
    LinearLayout mFl_whole, mLl_add_to_cart;
    private ImageView mIv_filer, mIv_close;
    LottieAnimationView animationView2, animationView;

    Boolean isLogedIn = false;
    String strCategoryIds = "", srtPriceRange = "", strFabricIds = "", strSortBy = "";

    ProductsAdapterBuyer adapter;
    ArrayList<ProductItem> listArray = new ArrayList<ProductItem>();
    String strSearch = "", brandId = "", strFabricType = "", total = "";
    Resources res;
    int pageNo = 1;
    boolean loading, isFirstScrollDone = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        View rootView = getLayoutInflater().inflate(R.layout.activity_products_seller, frameLayout);
        try {
            res = getResources();
            bindViews(rootView);

            strFabricType = getIntent().getStringExtra(commonVariables.KEY_FABRIC_TYPE);
            Brand category = (Brand) getIntent().getSerializableExtra(commonVariables.KEY_BRAND);
            if (category != null) {
                brandId = category.getBrandId();
                mTv_title.setText(WordUtils.capitalizeFully(category.getBrandName()));
            }

            strSearch = getIntent().getStringExtra(commonVariables.KEY_SEARCH_STR);
            if (strSearch == null)
                strSearch = "";

            APIs.GetProductList_Suit_Buyer(this, this, brandId, pageNo, strCategoryIds, srtPriceRange, strFabricIds, strSortBy, strFabricType);

            SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
            editor.putString(commonVariables.KEY_LAST_SORT_BY, strSortBy);
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindViews(View rootView) {

        try {
            main_content.setBackgroundResource(R.color.white);
            mIv_filer = (ImageView) findViewById(R.id.iv_filer);
            mIv_filer.setOnClickListener(this);
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

            mTv_title = (TextView) rootView.findViewById(R.id.tv_title);
            mIv_close = (ImageView) findViewById(R.id.iv_close);
            mIv_close.setOnClickListener(this);
            mLl_add_to_cart = (LinearLayout) rootView.findViewById(R.id.ll_add_to_cart);
            mLl_add_to_cart.setOnClickListener(this);

            mFl_whole = (LinearLayout) rootView.findViewById(R.id.fl_whole);
            mRv_items = (RecyclerView) rootView.findViewById(R.id.gv_items);
            mRv_items.setHasFixedSize(true);

            int i = 2;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                i = 4;
            }
            GridLayoutManager mLayoutManager = new GridLayoutManager(this, i);
            mRv_items.setLayoutManager(mLayoutManager);

            mRv_items.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    try {
                        int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                        int visibleItemCount = mLayoutManager.getChildCount();
                        if (isFirstScrollDone && listArray.size() != 0) {
                            int citem = (pastVisiblesItems + visibleItemCount);
                            if (citem != 2) {
                                mTv_count_items.setText(" (" + citem + "/" + total + " products)");
                            }
                        }
                        isFirstScrollDone = true;
                        if (dy > 0) {
                            int totalItemCount = mLayoutManager.getItemCount();
                            if (loading) {
                                if ((visibleItemCount + pastVisiblesItems) >= (totalItemCount - 10)) {
                                    loading = false;
                                    APIs.GetProductList_Suit_Buyer(null, ProductsActivityBuyer.this, brandId, ++pageNo, strCategoryIds, srtPriceRange, strFabricIds, strSortBy, strFabricType);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            mBtn_add_now = (Button) rootView.findViewById(R.id.btn_add_now);
            mBtn_add_now.setOnClickListener(this);
            mTv_no_data_found = (TextView) rootView.findViewById(R.id.tv_no_data_found);
            mTv_title.setOnClickListener(this);

            mTv_count_items = (TextView) rootView.findViewById(R.id.tv_no_items);
            mLl_no_data_found = (LinearLayout) rootView.findViewById(R.id.ll_no_data_found);
            animationView = (LottieAnimationView) rootView.findViewById(R.id.animation_view);
            animationView2 = (LottieAnimationView) rootView.findViewById(R.id.animation_view2);

        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            mTv_count_items.setText("");
            if (requestCode == commonVariables.REQUEST_FILTER_PRODUCT && resultCode == RESULT_OK && data != null) {
                strCategoryIds = data.getExtras().getString(commonVariables.INTENT_EXTRA_KEY_FILTER_CAT);
                if (strCategoryIds == null)
                    strCategoryIds = "";
                strFabricIds = data.getExtras().getString(commonVariables.INTENT_EXTRA_KEY_FILTER_FABRIC);
                if (strFabricIds == null)
                    strFabricIds = "";
                srtPriceRange = data.getExtras().getString(commonVariables.INTENT_EXTRA_KEY_FILTER_PRICE);
                if (srtPriceRange == null)
                    srtPriceRange = "";
                strSortBy = data.getExtras().getString(commonVariables.INTENT_EXTRA_KEY_FILTER_SORT);
                if (strSortBy == null)
                    strSortBy = "";

                listArray.clear();
                adapter.notifyDataSetChanged();
                pageNo = 1;
                APIs.GetProductList_Suit_Buyer(null, this, brandId, pageNo, strCategoryIds, srtPriceRange, strFabricIds, strSortBy, strFabricType);
            } else if (requestCode == REQUEST_LOGIN && resultCode == RESULT_OK) {
                boolean isLoggedIn = data.getExtras().getBoolean(commonVariables.KEY_IS_LOG_IN);
                if (isLoggedIn)
                    mLl_add_to_cart.performClick();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setListAdapter(ArrayList<ProductItem> listArray) {
        try {
            if (listArray.size() == 0) {
                mRv_items.setVisibility(View.GONE);
                mLl_no_data_found.setVisibility(View.VISIBLE);
                String strMessage = "Uhh! We you have not added any product yet. Want to add now ?";
                if (!strSearch.isEmpty())
                    strMessage = "<font color=\"#000\">" + "No results found for \"" + strSearch + "\"" + "</font>" + "<br />Please check the spelling or type any other keyword to search";
                mTv_no_data_found.setText((Html.fromHtml(strMessage)));
                startAnim();
            } else {
                mLl_no_data_found.setVisibility(View.GONE);
                mRv_items.setVisibility(View.VISIBLE);
                mFl_whole.setVisibility(View.VISIBLE);
                adapter = new ProductsAdapterBuyer(this, listArray, mLl_add_to_cart);
                mRv_items.setAdapter(adapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {
        try {
            AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            view.startAnimation(buttonClick);
            if (view == mIv_filer) {
                Intent intent = new Intent(this, FilterActivityBuyer.class);
                intent.putExtra(commonVariables.KEY_FABRIC_TYPE, strFabricType);
                intent.putExtra(commonVariables.KEY_BRAND, brandId);
                intent.putExtra(commonVariables.KEY_SUIT_OR_FABRIC, 1);
                if (!strSortBy.isEmpty())
                    intent.putExtra(commonVariables.INTENT_EXTRA_KEY_FILTER_SORT, strSortBy);
                if (!strFabricIds.isEmpty())
                    intent.putExtra(commonVariables.INTENT_EXTRA_KEY_FILTER_FABRIC, strFabricIds);
                if (!strCategoryIds.isEmpty())
                    intent.putExtra(commonVariables.INTENT_EXTRA_KEY_FILTER_CAT, strCategoryIds);
                if (!srtPriceRange.isEmpty())
                    intent.putExtra(commonVariables.INTENT_EXTRA_KEY_FILTER_PRICE, srtPriceRange);
                startActivityForResult(intent, commonVariables.REQUEST_FILTER_PRODUCT);
                overridePendingTransition(R.anim.slide_up, R.anim.hold);
            } else if (view == mIv_logo_toolbar) {
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
            } else if (view == mIv_close) {
                Intent output = new Intent();
                setResult(RESULT_OK, output);
                finish();
                overridePendingTransition(0, 0);
            } else if (view == mBtn_add_now) {
                Intent intent = new Intent(getApplicationContext(), MainActivityBuyer.class);
                intent.putExtra(commonVariables.KEY_IS_BRAND, false);
                startActivity(intent);
                overridePendingTransition(0, 0);
            } else if (view == mLl_add_to_cart) {

                if (AppPreferences.getPrefs().getBoolean(commonVariables.KEY_IS_LOG_IN, false)) {
                    JSONArray jarr = new JSONArray();
                    List<ProductItem> lisarr = adapter.getItems();
                    for (int i = 0; i < lisarr.size(); i++) {
                        if (lisarr.get(i).isActive()) {
                            ProductItem item = lisarr.get(i);
                            JSONObject jo = new JSONObject();
                            jo.put("ProductId", item.getProductId());
                            jo.put("SuitQty", item.getMinOrderQty());
                            jarr.put(jo);
                        }
                    }
                    APIs.AddProductToCart_Suit_Buyer(this, this, jarr);
                } else {
                    Intent intent = new Intent(this, LoginRegisterActivity.class);
                    intent.putExtra(commonVariables.KEY_FOR_LOGIN, true);
                    startActivityForResult(intent, commonVariables.REQUEST_LOGIN);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        try {
            invalidateOptionsMenu();
            isLogedIn = AppPreferences.getPrefs().getBoolean(commonVariables.KEY_IS_LOG_IN, false);
            if (adapter != null)
                adapter.notifyDataSetChanged();

            if (mTv_username != null) {
                String strProfile = AppPreferences.getPrefs().getString(commonVariables.KEY_BUYER_PROFILE, "");
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
                if (strApiName.equalsIgnoreCase("GetProductList_Suit_Buyer")) {

                    JSONObject job = jobjWhole.optJSONObject("resData");
                    JSONArray jarray = job.optJSONArray("lstProducts");

                    total = job.optString("totalProductCount");
                    mTv_count_items.setVisibility(View.VISIBLE);
                    mTv_count_items.setText(" (" + total + " products)");
                    if (jarray != null) {
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jObjItem = jarray.optJSONObject(i);
                            listArray.add(new ProductItem(jObjItem.optString("ProductId"), jObjItem.optString("ProductCode"), jObjItem.optString("OfferPrice"), jObjItem.optString("ImageName"), "", "", "", "", "", "", "", "", "", "", "", "", "", jObjItem.optInt("MinOrderQty"), false, false, null, false));
                        }
                    }
                    if (pageNo == 1) {
                        if (jarray == null || jarray.length() == 0)
                            listArray.clear();
                        setListAdapter(listArray);
                    } else {
                        if (listArray.size() != 0) {
                            mLl_no_data_found.setVisibility(View.GONE);
                            mRv_items.setVisibility(View.VISIBLE);
                            if (isLogedIn && adapter != null)
                                adapter.notifyDataSetChanged();
                        }
                    }

                    if (job.optInt("totalProductCount") > listArray.size())
                        loading = true;
                } else if (strApiName.equalsIgnoreCase("AddProductToCart_Suit_Buyer")) {
                    int resultId = jobjWhole.optInt("resInt");
                    String result = jobjWhole.optString("res");
                    if (resultId == 1) {
                        Intent intent = new Intent(this, CartActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    } else {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                        builder.setTitle(commonVariables.appname);
                        builder.setMessage(result);
                        builder.setPositiveButton("Ok", null);
                        builder.show();
                    }
                }
            } else {
                setListAdapter(listArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

