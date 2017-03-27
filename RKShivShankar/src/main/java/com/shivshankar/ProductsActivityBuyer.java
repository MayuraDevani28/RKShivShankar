package com.shivshankar;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.shivshankar.adapters.ProductsAdapterSeller;
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

public class ProductsActivityBuyer extends BaseActivityBuyer implements OnClickListener, OnResult {

    TextView mTv_no_data_found, mTv_title, mTv_count_items;
    Button mBtn_add_now;
    private LinearLayout mLl_no_data_found;
    RecyclerView mRv_items;
    LinearLayout mFl_whole;
    private ImageView mIv_filer;
    LottieAnimationView animationView2;
    LottieAnimationView animationView;
    public ImageView mIv_close;

    int startFrom = 0;
    Boolean isLogedIn = false;
    String strcategoryIds = "", srtpriceRange = "", strfabricIds = "", strSortBy = "", total = "";

    private static final int SORT_RESULT = 9, FILTER_RESULT = 10;
    ProductsAdapterSeller adapter;
    ArrayList<ProductItem> listArray = new ArrayList<ProductItem>();
    String strSearch = "", brandId = "", strFabricType = "";
    boolean cameFromFilter = false;
    Resources res;
    int pageNo = 1, pageSize = 50;
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
            strcategoryIds = getIntent().getStringExtra(commonVariables.INTENT_EXTRA_KEY_FILTER_CAT);
            if (strcategoryIds == null)
                strcategoryIds = "";
            strfabricIds = getIntent().getStringExtra(commonVariables.INTENT_EXTRA_KEY_FILTER_FABRIC);
            if (strfabricIds == null)
                strfabricIds = "";
            srtpriceRange = getIntent().getStringExtra(commonVariables.INTENT_EXTRA_KEY_FILTER_PRICE);
            if (srtpriceRange == null)
                srtpriceRange = "";
            String strsortBy = getIntent().getStringExtra(commonVariables.INTENT_EXTRA_SORT_DATA);
            if (strsortBy != null && !strsortBy.equalsIgnoreCase(""))
                strSortBy = strsortBy;
            else
                strSortBy = "";
            strSearch = getIntent().getStringExtra(commonVariables.KEY_SEARCH_STR);
            if (strSearch == null)
                strSearch = "";

            APIs.GetProductList_Suit_Buyer(this, this, brandId, pageNo, strcategoryIds, srtpriceRange, strfabricIds, strSortBy, strFabricType);

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
            mNav_customer_service.setOnClickListener(this);
            mNav_about_us.setOnClickListener(this);
            mNav_our_policy.setOnClickListener(this);
            mNav_contact_us.setOnClickListener(this);

            mTv_title = (TextView) rootView.findViewById(R.id.tv_title);
            mIv_close = (ImageView) findViewById(R.id.iv_close);
            mIv_close.setOnClickListener(this);


            mFl_whole = (LinearLayout) rootView.findViewById(R.id.fl_whole);
            mRv_items = (RecyclerView) rootView.findViewById(R.id.gv_items);
            mRv_items.setHasFixedSize(true);
//            mRv_items.setNestedScrollingEnabled(false);

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
                                if ((visibleItemCount + pastVisiblesItems) >= (totalItemCount - 30)) {
                                    loading = false;
                                    try {
                                        startFrom = startFrom + pageSize;
                                        APIs.GetProductList_Suit_Buyer(null, ProductsActivityBuyer.this, brandId, pageNo, strcategoryIds, srtpriceRange, strfabricIds, strSortBy, strFabricType);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
//                        else {
//                            if (strSearch.isEmpty()) {
//                                mCv_bottom_menu.setVisibility(View.VISIBLE);
//                            } else {
//                            }
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            mBtn_add_now = (Button) rootView.findViewById(R.id.btn_add_now);
            mBtn_add_now.setOnClickListener(this);
            mTv_no_data_found = (TextView) rootView.findViewById(R.id.tv_no_data_found);
            mTv_no_data_found.setVisibility(View.GONE);
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
            if (requestCode == SORT_RESULT && resultCode == Activity.RESULT_OK && data != null) {
                String strSortByNew = data.getExtras().getString(commonVariables.INTENT_EXTRA_SORT_DATA);
                if (strSortBy != null && strSortByNew != null) {
                    if (!strSortBy.equals(strSortByNew)) {
                        listArray.clear();
                        pageNo = 1;
                        strSortBy = strSortByNew;
                        APIs.GetProductList_Suit_Buyer(null, this, brandId, pageNo, strcategoryIds, srtpriceRange, strfabricIds, strSortBy, strFabricType);
                    }
                }
            } else if (requestCode == FILTER_RESULT && resultCode == Activity.RESULT_OK && data != null) {
                String filter = data.getExtras().getString(commonVariables.KEY_FILTER_DATA);
//                if (strFilter != null && filter != null) {
//                    if (!strFilter.equals(filter)) {
                listArray.clear();
                adapter.notifyDataSetChanged();
                pageNo = 1;
//                        strFilter = filter;
                APIs.GetProductList_Suit_Buyer(null, this, brandId, pageNo, strcategoryIds, srtpriceRange, strfabricIds, strSortBy, strFabricType);
//                    }
//                }
            } else if (requestCode == commonVariables.REQUEST_CODE_SEARCH && resultCode == RESULT_OK && data != null) {
                if (data != null) {
                    String strSearch = data.getExtras().getString(commonVariables.INTENT_EXTRA_SEARCH_STRING);
                    if (!strSearch.isEmpty()) {
                        Intent intent = new Intent(this, ProductsActivityBuyer.class);
                        intent.putExtra(commonVariables.KEY_SEARCH_STR, strSearch);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setListAdapter(ArrayList<ProductItem> listArray) {
        try {
            if (listArray.size() == 0) {
                mRv_items.setVisibility(View.GONE);
                mTv_no_data_found.setVisibility(View.VISIBLE);
                String strMessage = "Uhh! We you have not added any product yet. Want to add now ?";
                if (!strSearch.isEmpty())
                    strMessage = "<font color=\"#000\">" + "No results found for \"" + strSearch + "\"" + "</font>" + "<br />Please check the spelling or type any other keyword to search";
                mTv_no_data_found.setText((Html.fromHtml(strMessage)));
                startAnim();
            } else {
                mTv_no_data_found.setVisibility(View.GONE);
                mRv_items.setVisibility(View.VISIBLE);
                mFl_whole.setVisibility(View.VISIBLE);
                adapter = new ProductsAdapterSeller(this, listArray);
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
            } else if (view == mNav_customer_service) {
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, CustomerServiceActivityBuyer.class));
                overridePendingTransition(0, 0);
            } else if (view == mNav_about_us) {
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, AboutUsActivityBuyer.class));
                overridePendingTransition(0, 0);
            } else if (view == mNav_our_policy) {
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, OurPolicyActivityBuyer.class));
                overridePendingTransition(0, 0);
            } else if (view == mNav_contact_us) {
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, ContactUsActivityBuyer.class));
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
                Intent intent = new Intent(getApplicationContext(), ImagePickerActivity.class);
                intent.putExtra(commonVariables.KEY_IS_BRAND, false);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
//            if (v == mTv_filter) {
//                Intent intent = new Intent(this, FiltersActivity.class);
//                intent.putExtra(commonVariables.KEY_FILTER_DATA, strFilter);
//                intent.putExtra(commonVariables.KEY_CATEGORY_ID, strCategory);
//                startActivityForResult(intent, FILTER_RESULT);
//                overridePendingTransition(R.anim.fade_in_fast, R.anim.fade_out_fast);
////                fragment = new FiltersActivity(catName, strFilter);
////                FragmentManager fragmentManager = getSupportFragmentManager();
////                fragmentManager.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
//            } else if (v == mTv_sort) {
//                Intent intent = new Intent(this, SortActivity.class);
//                startActivityForResult(intent, SORT_RESULT);
//                overridePendingTransition(R.anim.fade_in_fast, R.anim.fade_out_fast);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        try {
//            commonMethods.setCartFromPreference(mTv_cart_count);
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
                            listArray.add(new ProductItem(jObjItem.optString("ProductId"), jObjItem.optString("ProductCode"), jObjItem.optString("OfferPrice"), jObjItem.optString("ImageName"), "", "", "", "", "", "", "", "", "", "", "", "", "", jObjItem.optInt("MinOrderQty"), false, false, null));
                        }
                    }
                    if (pageNo == 1)
                        setListAdapter(listArray);
                    else {
                        if (listArray.size() != 0) {
                            mLl_no_data_found.setVisibility(View.GONE);
                            mRv_items.setVisibility(View.VISIBLE);
                            if (isLogedIn && adapter != null)
                                adapter.notifyDataSetChanged();
                        }
                    }

                    if (job.optInt("Total") > listArray.size())
                        loading = true;
                }
            } else {
                setListAdapter(listArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

