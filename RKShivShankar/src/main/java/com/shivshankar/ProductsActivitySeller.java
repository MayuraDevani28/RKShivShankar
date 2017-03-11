package com.shivshankar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shivshankar.ServerCall.APIs;
import com.shivshankar.adapters.ProductsAdapterSeller;
import com.shivshankar.classes.CatalogItem;
import com.shivshankar.classes.Category;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonVariables;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductsActivitySeller extends BaseActivitySeller implements OnClickListener, OnResult {

    String strLoginId = "";
    TextView mTv_counter;
    private LinearLayout mLl_counter;

    RecyclerView mGv_items;
    RelativeLayout mFl_whole;
    TextView mTv_no_data_found, mTv_title, mTv_no_items;
    LinearLayout mCv_bottom_menu;
    private LinearLayout mTv_sort;
    private LinearLayout mTv_filter;
    LinearLayoutManager mLayoutManager;
    public ImageView mIv_close;

    //    int myLastVisiblePos;
    int startFrom = 0;
    Boolean isLogedIn = false;
    String strFilter = "", strSortBy = "Recently", total = "";

    private static final int SORT_RESULT = 9, FILTER_RESULT = 10;
    ProductsAdapterSeller adapter;
    ArrayList<CatalogItem> listArray = new ArrayList<CatalogItem>();
    String strSearch = "", categoryId = "";
    boolean cameFromFilter = false;
    Resources res;
    int pageNo = 1, pageSize = 50, padding;
    boolean loading, isFirstScrollDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        View rootView = getLayoutInflater().inflate(R.layout.activity_products, frameLayout);
        try {
            strLoginId = AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "");
            res = getResources();
            bindViews(rootView);
            Category category = (Category) getIntent().getSerializableExtra(commonVariables.INTENT_EXTRA_KEY_PRODUCT_OBJECT);
            if (category != null) {
                categoryId = category.getId();
                mTv_title.setText(WordUtils.capitalizeFully(category.getName()));
            }
            strFilter = getIntent().getStringExtra(commonVariables.INTENT_EXTRA_KEY_FILTER_STRING);
            if (strFilter == null)
                strFilter = "";
            String strsortBy = getIntent().getStringExtra(commonVariables.INTENT_EXTRA_SORT_DATA);
            if (strsortBy != null && !strsortBy.equalsIgnoreCase(""))
                strSortBy = strsortBy;
            else
                strSortBy = "";
            strSearch = getIntent().getStringExtra(commonVariables.KEY_SEARCH_STR);
            if (strSearch == null)
                strSearch = "";

            callGetCatalogListAPI(this, categoryId, pageNo, strFilter, strSortBy, strLoginId);

            padding = 35;
            SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
            editor.putString(commonVariables.KEY_LAST_SORT_BY, strSortBy);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callGetCatalogListAPI(AppCompatActivity activity, String cId, int pageNo, String filter, String sortby, String loginId) {

        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("mobile/GetCatalogList")
                .appendQueryParameter("categoryId", cId)
                .appendQueryParameter("filter", filter)
                .appendQueryParameter("sortby", sortby)
                .appendQueryParameter("pagesize", pageSize + "")
                .appendQueryParameter("pageno", pageNo + "")
                .appendQueryParameter("loginId", loginId)
                .appendQueryParameter("srch", strSearch)
                .build();

        String query = uri.toString();
        Log.v("TAG", "Calling With:" + query);
//        new ServerAPICAll(activity, this).execute(query);
        APIs.callAPI(activity, this, query);
    }

    private void bindViews(View rootView) {

        try {
            mTv_title = (TextView) rootView.findViewById(R.id.tv_title);
            mIv_close = (ImageView) findViewById(R.id.iv_close);
            mIv_close.setOnClickListener(this);

            mTv_counter = (TextView) rootView.findViewById(R.id.tv_counter);
            mLl_counter = (LinearLayout) rootView.findViewById(R.id.ll_counter);
            mFl_whole = (RelativeLayout) rootView.findViewById(R.id.fl_whole);
            mCv_bottom_menu = (LinearLayout) rootView.findViewById(R.id.cv_bottom_menu);
            if (!strSearch.isEmpty()) {
                mCv_bottom_menu.setVisibility(View.GONE);
            }
            mGv_items = (RecyclerView) rootView.findViewById(R.id.gv_items);
//            mGv_items.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            mGv_items.setLayoutManager(mLayoutManager);
            mGv_items.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    mLl_counter.setVisibility(View.GONE);
                    Log.v("TAG", "NewState:" + newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    try {
                        int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                        int visibleItemCount = mLayoutManager.getChildCount();
                        if (isFirstScrollDone && listArray.size() != 0) {
                            int citem = (pastVisiblesItems + visibleItemCount);
                            if (citem != 2) {
                                mTv_counter.setText(citem + "/" + total);
                                mLl_counter.setVisibility(View.VISIBLE);
                            }
                        }
                        isFirstScrollDone = true;
                        if (dy > 0) //check for scroll down
                        {
                            int totalItemCount = mLayoutManager.getItemCount();
//                            mGv_items.setPadding(0, 0, 0, 0);
                            mCv_bottom_menu.setVisibility(View.GONE);

                            if (loading) {
                                if ((visibleItemCount + pastVisiblesItems) >= (totalItemCount - 30)) {
                                    loading = false;
                                    Log.v("...", "Last Item Wow !");
                                    //Do pagination.. i.e. fetch new data

                                    try {
                                        startFrom = startFrom + pageSize;
                                        callGetCatalogListAPI(null, categoryId, ++pageNo, strFilter, strSortBy, strLoginId);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        } else {
                            if (strSearch.isEmpty()) {
//                                mGv_items.setPadding(0, 0, 0, padding);
                                mCv_bottom_menu.setVisibility(View.VISIBLE);
                            } else {
//                                mGv_items.setPadding(0, 0, 0, 0);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            mTv_sort = (LinearLayout) rootView.findViewById(R.id.tv_sort);
            mTv_filter = (LinearLayout) rootView.findViewById(R.id.tv_filter);
            mTv_no_data_found = (TextView) rootView.findViewById(R.id.tv_no_data_found);
            mTv_no_data_found.setVisibility(View.GONE);

            mTv_sort.setOnClickListener(this);
            mTv_filter.setOnClickListener(this);
            mTv_title.setOnClickListener(this);
            mTv_no_items = (TextView) rootView.findViewById(R.id.tv_no_items);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            mLl_counter.setVisibility(View.GONE);
            if (requestCode == SORT_RESULT && resultCode == Activity.RESULT_OK && data != null) {
                String strSortByNew = data.getExtras().getString(commonVariables.INTENT_EXTRA_SORT_DATA);
                if (strSortBy != null && strSortByNew != null) {
                    if (!strSortBy.equals(strSortByNew)) {
                        listArray.clear();
                        pageNo = 1;
                        strSortBy = strSortByNew;
                        callGetCatalogListAPI(null, categoryId, pageNo, strFilter, strSortBy, strLoginId);
                    }
                }
            } else if (requestCode == FILTER_RESULT && resultCode == Activity.RESULT_OK && data != null) {
                String filter = data.getExtras().getString(commonVariables.KEY_FILTER_DATA);
                if (strFilter != null && filter != null) {
                    if (!strFilter.equals(filter)) {

                        listArray.clear();
                        adapter.notifyDataSetChanged();
                        pageNo = 1;
                        strFilter = filter;
                        callGetCatalogListAPI(null, categoryId, pageNo, strFilter, strSortBy, strLoginId);
                    }
                }
            } else if (requestCode == commonVariables.REQUEST_CODE_SEARCH && resultCode == RESULT_OK && data != null) {
                if (data != null) {
                    String strSearch = data.getExtras().getString(commonVariables.INTENT_EXTRA_SEARCH_STRING);
                    if (!strSearch.isEmpty()) {
                        Intent intent = new Intent(this, ProductsActivitySeller.class);
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

    private void setListAdapter(ArrayList<CatalogItem> listArray) {
        try {
            mTv_sort.setEnabled(true);
            if (listArray.size() == 0) {
                mGv_items.setVisibility(View.GONE);
                mTv_no_data_found.setVisibility(View.VISIBLE);
                if (!cameFromFilter) {
                    mCv_bottom_menu.setVisibility(View.GONE);
                } else if (strSearch != null) {
                    mCv_bottom_menu.setVisibility(View.GONE);
                } else {
                    mTv_sort.setEnabled(false);
                }
                String strMessage = "No results found";
                if (!strSearch.isEmpty())
                    strMessage = "<font color=\"#000\">" + "No results found for \"" + strSearch + "\"" + "</font>" + "<br />Please check the spelling or type any other keyword to search";
                mTv_no_data_found.setText((Html.fromHtml(strMessage)));
            } else {
                mTv_no_data_found.setVisibility(View.GONE);
                mGv_items.setVisibility(View.VISIBLE);
                mFl_whole.setVisibility(View.VISIBLE);
                adapter = new ProductsAdapterSeller(this, listArray);
                mGv_items.setAdapter(adapter);
                if (!strSearch.isEmpty()) {
                    mCv_bottom_menu.setVisibility(View.GONE);
                } else
                    mCv_bottom_menu.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        try {
            if (v == mIv_close) {
                Intent output = new Intent();
                setResult(RESULT_OK, output);
                finish();
                overridePendingTransition(0, 0);
            }
//            if (v == mTv_filter) {
//                Intent intent = new Intent(this, FiltersActivity.class);
//                intent.putExtra(commonVariables.KEY_FILTER_DATA, strFilter);
//                intent.putExtra(commonVariables.KEY_CATEGORY_ID, categoryId);
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
            strLoginId = AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "");
            if (adapter != null)
                adapter.notifyDataSetChanged();
            String userName = AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_USERNAME, "Guest User");
//            if (userName != null && !userName.equalsIgnoreCase("") && !userName.equalsIgnoreCase("null"))
//                mTv_username.setText(userName);
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
                if (strApiName.equalsIgnoreCase("GetCatalogList")) {

                    JSONObject job = jobjWhole.optJSONObject("resData");
                    JSONArray jarray = job.optJSONArray("lstCatalog");
                    if (mTv_title.getText().toString().trim().isEmpty())
                        mTv_title.setText(jobjWhole.optString("categoryName"));
                    total = job.optString("Total");
                    mTv_no_items.setVisibility(View.VISIBLE);
                    mTv_no_items.setText(" " + total + " Catalogs");
                    if (jarray != null) {
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jObjItem = jarray.optJSONObject(i);
                            JSONArray jarrSize = jObjItem.optJSONArray("CatalogSizelist");
                            listArray.add(new CatalogItem(jObjItem.optString("CatalogId"), jObjItem.optString("CatalogCode"), jObjItem.optString("CatalogName"), jObjItem.optString("BrandName"), commonVariables.STRING_SERVER_IMAGE_URL + "CatalogImages/resized/" + jObjItem.optString("ImageName"), jObjItem.optString("MarketPrice"), jObjItem.optString("OfferPrice"), jObjItem.optString("AverageMarketPrice"), jObjItem.optString("AverageOfferPrice"), jObjItem.optInt("DiscountPercent"), jObjItem.optInt("TotalCatalogItem"), jObjItem.optInt("Quantity"), jObjItem.optBoolean("IsHotDeals"), jObjItem.optBoolean("IsOutOfStock"), jObjItem.optBoolean("HasSingleProduct")));
                        }
                    }
                    if (pageNo == 1)
                        setListAdapter(listArray);
                    else {
                        if (listArray.size() != 0) {
                            mTv_no_data_found.setVisibility(View.GONE);
                            mGv_items.setVisibility(View.VISIBLE);
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

