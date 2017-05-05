package com.shivshankar;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.adapters.SuitProductsAdapterBuyer;
import com.shivshankar.classes.Brand;
import com.shivshankar.classes.ProductItem;
import com.shivshankar.utills.AlertDialogManager;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonVariables;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    SuitProductsAdapterBuyer adapter;
    ArrayList<ProductItem> listArray = new ArrayList<ProductItem>();
    String strSearch = "", brandId = "", strFabricType = "", total = "", strCatidSuitFabric = "";
    Resources res;
    int pageNo = 1;
    boolean loading, isFirstScrollDone = false;
    private LinearLayout mLl_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        View rootView = getLayoutInflater().inflate(R.layout.activity_products_seller, frameLayout);
        rootView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));

        try {
            res = getResources();
            bindViews(rootView);

            strFabricType = getIntent().getStringExtra(commonVariables.KEY_FABRIC_TYPE);
            strCatidSuitFabric = getIntent().getStringExtra(commonVariables.KEY_CATEGORY);
            Brand category = (Brand) getIntent().getSerializableExtra(commonVariables.KEY_BRAND);
            if (category != null) {
                brandId = category.getBrandId();
                mTv_title.setText(WordUtils.capitalizeFully(category.getBrandName()));
            }

            strSearch = getIntent().getStringExtra(commonVariables.KEY_SEARCH_STR);
            if (strSearch == null)
                strSearch = "";

            APIs.GetProductList_Suit_Buyer(this, this, brandId, pageNo, strCategoryIds, srtPriceRange, strFabricIds, strSortBy, strFabricType, strCatidSuitFabric);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindViews(View rootView) {

        try {

            mLl_title = (LinearLayout) rootView.findViewById(R.id.ll_title);
            mIv_filer = (ImageView) findViewById(R.id.iv_filer);
            mIv_filer.setOnClickListener(this);

            mTv_title = (TextView) rootView.findViewById(R.id.tv_title);
            mIv_close = (ImageView) findViewById(R.id.iv_close);
            mIv_close.setOnClickListener(this);
            mLl_add_to_cart = (LinearLayout) rootView.findViewById(R.id.ll_add_to_cart);
            mLl_add_to_cart.setVisibility(View.VISIBLE);
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
                            if (mLl_title.getVisibility() == View.VISIBLE) {
                                collapse(mLl_title);
                            }
                            // ViewPropertyAnimator.animate(mLl_title).cancel();
                            //ViewPropertyAnimator.animate(mLl_title).translationY(-mLl_title.getHeight()).setDuration(500).start();
                            // mLl_title.setVisibility(View.GONE);
                            //mLl_title.animate().translationY(-mLl_title.getHeight()).setInterpolator(new AccelerateInterpolator(1));
                            // mLl_title.setVisibility(View.GONE);
                            //slideToTop(mLl_title);
                            if (loading) {
                                if ((visibleItemCount + pastVisiblesItems) >= (totalItemCount - 10)) {
                                    loading = false;
                                    APIs.GetProductList_Suit_Buyer(null, ProductsActivityBuyer.this, brandId, ++pageNo, strCategoryIds, srtPriceRange, strFabricIds, strSortBy, strFabricType, strCatidSuitFabric);
                                }
                            }
                        } else {
                            if (mLl_title.getVisibility() == View.GONE) {
                                expand(mLl_title);
                            }
                            // ViewPropertyAnimator.animate(mLl_title).cancel();
                            //ViewPropertyAnimator.animate(mLl_title).translationY(0).setDuration(500).start();
                            //slideToTop(mLl_title);
                            //mLl_title.setVisibility(View.VISIBLE);
                            // mLl_title.animate().translationY(0).setInterpolator(new DecelerateInterpolator(1));
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
                APIs.GetProductList_Suit_Buyer(null, this, brandId, pageNo, strCategoryIds, srtPriceRange, strFabricIds, strSortBy, strFabricType, strCatidSuitFabric);
            } else if (requestCode == commonVariables.REQUEST_LOGIN && resultCode == RESULT_OK) {
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
                mFl_whole.setVisibility(View.GONE);
                mLl_no_data_found.setVisibility(View.VISIBLE);
                String strMessage = "Uhh! no products found. Search for products now?";
                if (!strSearch.isEmpty())
                    strMessage = "<font color=\"#000\">" + "No products found for \"" + strSearch + "\"" + "</font>";
                mTv_no_data_found.setText((Html.fromHtml(strMessage)));
                mBtn_add_now.setText("Search Now");
                startAnim();
            } else {
                mLl_no_data_found.setVisibility(View.GONE);
                mFl_whole.setVisibility(View.VISIBLE);
                adapter = new SuitProductsAdapterBuyer(this, listArray);
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
                if (adapter.isOneChecked()) {
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
                        APIs.AddProductToCart_Suit_Buyer(this, this, jarr, strCatidSuitFabric);
                    } else {
                        Intent intent = new Intent(this, LoginRegisterActivity.class);
                        intent.putExtra(commonVariables.KEY_FOR_LOGIN, true);
                        startActivityForResult(intent, commonVariables.REQUEST_LOGIN);
                    }
                } else {
                    AlertDialogManager.showDialog(this, "Select alteast one item to add in cart", null);
                }
            } else
                super.onClick(view);
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
                            mFl_whole.setVisibility(View.VISIBLE);
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
                        AppPreferences.getPrefs().edit().putInt(commonVariables.CART_COUNT, jobjWhole.optInt("cartCount")).apply();
                        Intent intent = new Intent(this, CartActivityBuyer.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0, 0);
                    } else {
                        AlertDialogManager.showDialog(this, result, null);
                    }
                }
            } else {
                setListAdapter(listArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density)) * 6);
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density)) * 6);
        v.startAnimation(a);
    }
}

