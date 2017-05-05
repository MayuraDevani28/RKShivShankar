package com.shivshankar;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.gson.Gson;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.adapters.BrandsAdapterBuyer;
import com.shivshankar.classes.Brand;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BrandsActivityBuyer extends BaseActivityBuyer implements OnClickListener, OnResult {

    TextView mTv_no_data_found, mTv_title, mTv_count_items, mTv_brand_search;
    Button mBtn_add_now;
    LottieAnimationView animationView2, animationView;
    private LinearLayout mLl_no_data_found, mLl_title;
    LinearLayout mFl_whole;
    RecyclerView mRv_items;
    private ImageView mIv_filer;

    GridLayoutManager mLayoutManager;
    public ImageView mIv_close;

    String total = "", strFabricType;
    BrandsAdapterBuyer adapter;
    ArrayList<Brand> listArray = new ArrayList<Brand>();
    boolean isFirstScrollDone = false;
    Brand item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        View rootView = getLayoutInflater().inflate(R.layout.activity_products_seller, frameLayout);
//        //rootView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
        try {
            Gson gson = new Gson();
            String json = getIntent().getStringExtra(commonVariables.KEY_CATEGORY);
            strFabricType = getIntent().getStringExtra(commonVariables.KEY_FABRIC_TYPE);
            if (!json.isEmpty()) {
                item = gson.fromJson(json, Brand.class);
            }
            bindViews(rootView);
            mTv_title.setText("Our Brands");
            mTv_brand_search.setVisibility(View.VISIBLE);
            mTv_brand_search.setText("(" + WordUtils.capitalize(strFabricType + " " + item.getBrandName()) + ")");
            APIs.GetBuyerBrands(this, this, item.getBrandId(), strFabricType);
            setCartAndNotiCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void bindViews(View rootView) {

        try {
            mLl_title = (LinearLayout) rootView.findViewById(R.id.ll_title);
            mTv_title = (TextView) rootView.findViewById(R.id.tv_title);
            mIv_close = (ImageView) findViewById(R.id.iv_close);
            mIv_close.setOnClickListener(this);

            mIv_filer = (ImageView) findViewById(R.id.iv_filer);
            mIv_filer.setVisibility(View.GONE);

            mRv_items = (RecyclerView) rootView.findViewById(R.id.gv_items);
            mLayoutManager = new GridLayoutManager(this, 2);
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
                                mTv_count_items.setText(" (" + citem + "/" + total + " brands)");
                            }
                        }
                        isFirstScrollDone = true;
                        if (dy > 0) {
                            if (mLl_title.getVisibility() == View.VISIBLE)
                                commonMethods.collapse(mLl_title);
                        } else if (mLl_title.getVisibility() == View.GONE)
                            commonMethods.expand(mLl_title);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            mBtn_add_now = (Button) rootView.findViewById(R.id.btn_add_now);
            mBtn_add_now.setText("Go back");
            mBtn_add_now.setOnClickListener(this);
            mTv_no_data_found = (TextView) rootView.findViewById(R.id.tv_no_data_found);
            mLl_no_data_found = (LinearLayout) rootView.findViewById(R.id.ll_no_data_found);
            mLl_no_data_found.setVisibility(View.GONE);
            mFl_whole = (LinearLayout) rootView.findViewById(R.id.fl_whole);

            mTv_count_items = (TextView) rootView.findViewById(R.id.tv_no_items);
            mTv_brand_search = (TextView) rootView.findViewById(R.id.tv_brand_search);

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

    public void setListAdapter(ArrayList<Brand> listArray) {
        try {
            if (listArray.size() == 0) {
                mLl_no_data_found.setVisibility(View.VISIBLE);
                mFl_whole.setVisibility(View.GONE);
                String strMessage = "Uhh! No brands found for " + strFabricType + " " + item.getBrandName();
                startAnim();
                mTv_no_data_found.setText((Html.fromHtml(strMessage)));
            } else {
                mLl_no_data_found.setVisibility(View.GONE);
                mFl_whole.setVisibility(View.VISIBLE);
                adapter = new BrandsAdapterBuyer(this, listArray, strFabricType, item.getBrandId());
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
            if (view == mIv_close) {
                Intent output = new Intent();
                setResult(RESULT_OK, output);
                finish();
                overridePendingTransition(0, 0);
            } else if (view == mBtn_add_now) {
                onBackPressed();
                overridePendingTransition(0, 0);
            } else
                super.onClick(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onResume() {
//        try {
//            if (adapter != null)
//                adapter.notifyDataSetChanged();
////            setUserName()
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        super.onResume();
//    }

    @SuppressLint("NewApi")
    @Override
    public void onResult(JSONObject jobjWhole) {
        try {
            if (jobjWhole != null) {
                String strApiName = jobjWhole.optString("api");
                if (strApiName.equalsIgnoreCase("GetBuyerBrands")) {
                    JSONArray jarray = jobjWhole.optJSONArray("resData");
                    total = jobjWhole.optString("count");
                    mTv_count_items.setVisibility(View.VISIBLE);
//                    mTv_count_items.setText(" (" + total + " brands)");
                    if (jarray != null) {
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jObjItem = jarray.optJSONObject(i);
                            listArray.add(new Brand(jObjItem.optString("BrandId"), jObjItem.optString("BrandName"), jObjItem.optString("BrandLogo")));
                        }
                    }
                    setListAdapter(listArray);
                }
            } else {
                setListAdapter(listArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

