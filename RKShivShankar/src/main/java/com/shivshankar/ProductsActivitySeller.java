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
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.adapters.ProductsAdapterSeller;
import com.shivshankar.classes.ProductItem;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.shivshankar.R.id.btn_add_now;
import static com.shivshankar.R.id.ll_header;
import static com.shivshankar.R.id.ll_no_data_found;

public class ProductsActivitySeller extends BaseActivitySeller implements OnClickListener, OnResult, View.OnFocusChangeListener {

    TextView mTv_no_data_found, mTv_title, mTv_count_items, mTv_go;
    Button mBtn_add_now;
    private LinearLayout mLl_no_data_found, mFl_whole, mLl_header_whole, mLl_title;
    RecyclerView mRv_items;
    private ImageView mIv_filer, mIv_add_product;
    LottieAnimationView animationView2, animationView;

    private LinearLayout mLl_header, mLl_header_views;
    private MaterialBetterSpinner mSp_Category;
    private EditText mEdt_product_code;
    private ImageView mIv_down, mIv_close;


    int pageNo = 1;
    String total = "", strCategory = "", strSearch = "";
    String[] SP_CATEGOTY = {"Category"};
    String[] VAL_CATEGOTY = {""};

    ProductsAdapterSeller adapter;
    ArrayList<ProductItem> listArray = new ArrayList<>();
    Resources res;
    boolean loading, isFirstScrollDone = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        View rootView = getLayoutInflater().inflate(R.layout.activity_products_seller, frameLayout);
        //rootView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));

        try {
            res = getResources();
            bindViews(rootView);

            setCategoryData(SP_CATEGOTY, VAL_CATEGOTY);
            APIs.GetCategory(null, this);
            APIs.GetProductList_Suit_Seller(this, this, strCategory, pageNo, strSearch);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindViews(View rootView) {

        try {
            mLl_title = (LinearLayout) rootView.findViewById(R.id.ll_title);
            mIv_filer = (ImageView) findViewById(R.id.iv_filer);
            mIv_filer.setOnClickListener(this);
            mIv_add_product = (ImageView) findViewById(R.id.iv_add_product);
            mIv_add_product.setVisibility(View.VISIBLE);
            mIv_add_product.setOnClickListener(this);

            mTv_title = (TextView) rootView.findViewById(R.id.tv_title);
            mTv_go = (TextView) rootView.findViewById(R.id.tv_go);
            mTv_go.setOnClickListener(this);
            mIv_close = (ImageView) findViewById(R.id.iv_close);
            mIv_close.setOnClickListener(this);

            mFl_whole = (LinearLayout) rootView.findViewById(R.id.fl_whole);
            mLl_header_whole = (LinearLayout) rootView.findViewById(R.id.ll_header_whole);
            mRv_items = (RecyclerView) rootView.findViewById(R.id.gv_items);
            mRv_items.setHasFixedSize(true);

            int i = 2;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (commonMethods.isTablet(this))
                    i = 6;
                else
                    i = 4;
            }
            GridLayoutManager mLayoutManager = new GridLayoutManager(this, i);
            mRv_items.setLayoutManager(mLayoutManager);

            mRv_items.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    mLl_header_whole.setVisibility(View.GONE);
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
                                    APIs.GetProductList_Suit_Seller(ProductsActivitySeller.this, ProductsActivitySeller.this, strCategory, ++pageNo, strSearch);
                                }
                            }
                            if (mLl_title.getVisibility() == View.VISIBLE)
                                commonMethods.collapse(mLl_title);
                        } else if (mLl_title.getVisibility() == View.GONE)
                            commonMethods.expand(mLl_title);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            mBtn_add_now = (Button) rootView.findViewById(btn_add_now);
            mBtn_add_now.setOnClickListener(this);
            mTv_no_data_found = (TextView) rootView.findViewById(R.id.tv_no_data_found);
            mTv_title.setOnClickListener(this);

            mTv_count_items = (TextView) rootView.findViewById(R.id.tv_no_items);
            mLl_no_data_found = (LinearLayout) rootView.findViewById(ll_no_data_found);
            animationView = (LottieAnimationView) rootView.findViewById(R.id.animation_view);
            animationView2 = (LottieAnimationView) rootView.findViewById(R.id.animation_view2);

            mLl_header = (LinearLayout) rootView.findViewById(ll_header);
            mIv_down = (ImageView) rootView.findViewById(R.id.iv_down);
            mLl_header_views = (LinearLayout) rootView.findViewById(R.id.ll_header_views);
            mSp_Category = (MaterialBetterSpinner) rootView.findViewById(R.id.sp_category);
            mEdt_product_code = (EditText) rootView.findViewById(R.id.edt_product_code);
            mEdt_product_code.setOnFocusChangeListener(this);
            mLl_header.setOnClickListener(this);

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

    public void setListAdapter(ArrayList<ProductItem> listArray) {
        try {
            if (listArray.size() == 0) {
                mFl_whole.setVisibility(View.GONE);
                if (strCategory.isEmpty() && mEdt_product_code.getText().toString().trim().isEmpty()) {
                    mLl_header_whole.setVisibility(View.GONE);
                } else {
                    mLl_header_whole.setVisibility(View.VISIBLE);
                }
                mLl_no_data_found.setVisibility(View.VISIBLE);
                String strMessage = "Uhh! We you have not added any product yet. Want to add now ?";
                if (!strSearch.isEmpty())
                    strMessage = "<font color=\"#000\">" + "No results found for \"" + strSearch + "\"" + "</font>" + "<br />Please check product code";
                mTv_no_data_found.setText((Html.fromHtml(strMessage)));
                startAnim();
            } else {
                mLl_no_data_found.setVisibility(View.GONE);
                mFl_whole.setVisibility(View.VISIBLE);
                mRv_items.setVisibility(View.VISIBLE);
                adapter = new ProductsAdapterSeller(this, listArray);
                mRv_items.setAdapter(adapter);
                mTv_count_items.setVisibility(View.VISIBLE);
                mTv_count_items.setText(" (" + total + " products)");
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
            if (view == mLl_header) {
                if (mLl_header_views.getVisibility() == View.VISIBLE) {
                    mIv_down.setRotation(0);
                    mLl_header_views.setVisibility(View.GONE);
                } else {
                    mIv_down.setRotation(180);
                    mLl_header_views.setVisibility(View.VISIBLE);
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
            } else if (view == mTv_go) {
                strSearch = mEdt_product_code.getText().toString().trim();
                listArray.clear();
                pageNo = 1;
                APIs.GetProductList_Suit_Seller(this, this, strCategory, pageNo, strSearch);
            } else if (view == mIv_filer) {
                if (mLl_header_whole.getVisibility() == View.VISIBLE)
                    mLl_header_whole.setVisibility(View.GONE);
                else
                    mLl_header_whole.setVisibility(View.VISIBLE);
            } else if (view == mIv_add_product) {
                mBtn_add_now.performClick();
            } else
                super.onClick(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//
//    @Override
//    public void onResume() {
//        try {
//            invalidateOptionsMenu();
//            if (adapter != null)
//                adapter.notifyDataSetChanged();
//
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
                if (strApiName.equalsIgnoreCase("GetCategory")) {
                    JSONArray jarr = jobjWhole.optJSONArray("resData");
                    int l = jarr.length();
                    SP_CATEGOTY = new String[l + 1];
                    VAL_CATEGOTY = new String[l + 1];
                    SP_CATEGOTY[0] = "All";
                    VAL_CATEGOTY[0] = "";
                    for (int i = 0; i < jarr.length(); i++) {
                        JSONObject jo = jarr.optJSONObject(i);
                        String name = jo.optString("CategoryName");
                        String id = jo.optString("CategoryId");
                        SP_CATEGOTY[i + 1] = name;
                        VAL_CATEGOTY[i + 1] = id;
                    }
                    setCategoryData(SP_CATEGOTY, VAL_CATEGOTY);
                } else if (strApiName.equalsIgnoreCase("GetProductList_Suit_Seller")) {

                    mFl_whole.setVisibility(View.VISIBLE);
                    mLl_header_whole.setVisibility(View.GONE);

                    JSONObject job = jobjWhole.optJSONObject("resData");
                    JSONArray jarray = job.optJSONArray("lstProducts");
                    total = job.optString("totalProductCount");
                    mTv_count_items.setVisibility(View.VISIBLE);
                    mTv_count_items.setText(" (" + total + " products)");
                    if (jarray != null) {
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jObjItem = jarray.optJSONObject(i);
                            listArray.add(new ProductItem(jObjItem.optString("ProductId"), jObjItem.optString("ProductCode"), jObjItem.optString("OfferPrice"), jObjItem.optString("ImageName"), "", "", "", "", "", "", "", "", "", "", "", "", "", jObjItem.optInt("MinOrderQty"), false, false, null, jObjItem.optBoolean("IsActive")));
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
                            if (adapter != null)
                                adapter.notifyDataSetChanged();
                        }
                    }

                    if (job.optInt("totalProductCount") > listArray.size())
                        loading = true;
                }
            } else {
                setListAdapter(listArray);
            }
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
                mEdt_product_code.setText("");
                strSearch = "";

                listArray.clear();
                pageNo = 1;
                APIs.GetProductList_Suit_Seller(this, this, strCategory, pageNo, strSearch);
            });
            mSp_Category.setText("");
            strCategory = "";
            mSp_Category.clearFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {
            mSp_Category.setText("");
            strCategory = "";
        }
    }
}

