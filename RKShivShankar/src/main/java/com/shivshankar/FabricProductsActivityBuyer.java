package com.shivshankar;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
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
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.adapters.FabricProductsAdapterBuyer;
import com.shivshankar.classes.Brand;
import com.shivshankar.classes.ProductItem;
import com.shivshankar.utills.AlertDialogManager;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.shivshankar.utills.commonMethods.collapse;
import static com.shivshankar.utills.commonMethods.expand;

public class FabricProductsActivityBuyer extends BaseActivityBuyer implements OnClickListener, OnResult {

    TextView mTv_no_data_found, mTv_title, mTv_count_items;
    Button mBtn_add_now;
    private LinearLayout mLl_no_data_found, mLl_title;
    public RecyclerView mRv_items;
    LinearLayout mFl_whole;
    private ImageView mIv_filer, mIv_close;
    LottieAnimationView animationView2, animationView;

    String strCategoryIds = "", srtPriceRange = "", strFabricIds = "", strSortBy = "", strCatidSuitFabric = "";
    String strSearch = "", brandId = "0", strFabricType = "", total = "";
    int pageNo = 1;
    boolean loading, isFirstScrollDone = false;
    ArrayList<ProductItem> listArray = new ArrayList<>();
    FabricProductsAdapterBuyer adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        View rootView = getLayoutInflater().inflate(R.layout.activity_products_seller, frameLayout);
//        //rootView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));

        try {
            bindViews(rootView);

            strFabricType = getIntent().getStringExtra(commonVariables.KEY_FABRIC_TYPE);
            if (strFabricType == null)
                strFabricType = "";
            strCatidSuitFabric = getIntent().getStringExtra(commonVariables.KEY_CATEGORY);
            strSearch = getIntent().getStringExtra(commonVariables.KEY_SEARCH_STR);
            if (strSearch == null)
                strSearch = "";

            Brand category = (Brand) getIntent().getSerializableExtra(commonVariables.KEY_BRAND);
            if (category != null) {
                brandId = category.getBrandId();
                mTv_title.setText(WordUtils.capitalizeFully(category.getBrandName()));
            } else {
                mTv_title.setText("Search " + WordUtils.capitalizeFully(strSearch));
            }

            APIs.GetProduct_Fabric_Buyer(this, this, brandId, pageNo, strCategoryIds, srtPriceRange, strFabricIds, strSortBy, strFabricType, strSearch);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindViews(View rootView) {

        try {
            mLl_title = (LinearLayout) findViewById(R.id.ll_title);
            mIv_filer = (ImageView) findViewById(R.id.iv_filer);
            mIv_filer.setVisibility(View.GONE);
            mIv_filer.setOnClickListener(this);

            mTv_title = (TextView) rootView.findViewById(R.id.tv_title);
            mIv_close = (ImageView) findViewById(R.id.iv_close);
            mIv_close.setOnClickListener(this);
            mFl_whole = (LinearLayout) rootView.findViewById(R.id.fl_whole);

            mBtn_add_now = (Button) rootView.findViewById(R.id.btn_add_now);
            mBtn_add_now.setOnClickListener(this);
            mTv_no_data_found = (TextView) rootView.findViewById(R.id.tv_no_data_found);
            mTv_title.setOnClickListener(this);

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

            mTv_count_items = (TextView) rootView.findViewById(R.id.tv_no_items);
            mLl_no_data_found = (LinearLayout) rootView.findViewById(R.id.ll_no_data_found);
            animationView = (LottieAnimationView) rootView.findViewById(R.id.animation_view);
            animationView2 = (LottieAnimationView) rootView.findViewById(R.id.animation_view2);

            mRv_items.addOnScrollListener(new RecyclerView.OnScrollListener() {
                boolean scroll_down;

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (scroll_down) {
                        collapse(mLl_title);
                    } else if (mLl_title.getVisibility() == View.GONE) {
                        expand(mLl_title);
                    }
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
                                    APIs.GetProduct_Fabric_Buyer(null, FabricProductsActivityBuyer.this, brandId, ++pageNo, strCategoryIds, srtPriceRange, strFabricIds, strSortBy, strFabricType, strSearch);
                                }
                            }
//                            if (mLl_title.getVisibility() == View.VISIBLE)
//                                commonMethods.collapse(mLl_title);
                        }
//                        else if (mLl_title.getVisibility() == View.GONE)
//                            commonMethods.expand(mLl_title);
                        if (dy > 42) {
                            scroll_down = true;
                        } else if (dy < -5) {
                            scroll_down = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startAnim() {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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

                pageNo = 1;
                APIs.GetProduct_Fabric_Buyer(null, this, brandId, pageNo, strCategoryIds, srtPriceRange, strFabricIds, strSortBy, strFabricType, strSearch);
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
                String strMessage = "Uhh! No products found. Search for products now?";
                if (!strSearch.isEmpty())
                    strMessage = "<font color=\"#000\">" + "No products found for \"" + strSearch + "\"" + "</font>";
                mTv_no_data_found.setText((Html.fromHtml(strMessage)));
                mBtn_add_now.setText("Search Now");
                startAnim();
            } else {
                mLl_no_data_found.setVisibility(View.GONE);
                mFl_whole.setVisibility(View.VISIBLE);
                adapter = new FabricProductsAdapterBuyer(this, listArray, strFabricType, brandId);
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
                intent.putExtra(commonVariables.KEY_SUIT_OR_FABRIC, 2);//1=suit, 2=fabric
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
                if (strApiName.equalsIgnoreCase("GetProduct_Fabric_Buyer")) {

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
                            if (adapter != null)
                                adapter.notifyDataSetChanged();
                        }
                    }

                    if (job.optInt("totalProductCount") > listArray.size())
                        loading = true;
                } else if (strApiName.equalsIgnoreCase("AddProductToCart_Fabric_Buyer")) {
                    int resultId = jobjWhole.optInt("resInt");
                    String result = jobjWhole.optString("res");
                    if (resultId == 1) {
                        AppPreferences.getPrefs().edit().putInt(commonVariables.CART_COUNT, jobjWhole.optInt("cartCount")).apply();
                        Intent intent = new Intent(this, CartActivityBuyer.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    } else {
                        AlertDialogManager.showDialog(this, result, null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
