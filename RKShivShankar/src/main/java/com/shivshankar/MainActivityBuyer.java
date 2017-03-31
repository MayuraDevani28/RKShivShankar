package com.shivshankar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.adapters.HomeCategoryAdapterBuyer;
import com.shivshankar.classes.Brand;
import com.shivshankar.classes.DataHelper;
import com.shivshankar.classes.Suggestion;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivityBuyer extends BaseActivityBuyer implements View.OnClickListener, OnResult {
    LinearLayout mLl_view;
    private FloatingSearchView mSearchView;
    RecyclerView mRv_items;
    private boolean mIsDarkSearchTheme = false;
    private String mLastQuery = "";

    ArrayList<Brand> listArray = new ArrayList<Brand>();
    HomeCategoryAdapterBuyer adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        try {
            View rootView = getLayoutInflater().inflate(R.layout.activity_main_buyer, frameLayout);
            rootView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.slide_in_right));
            bindViews(rootView);

            setupFloatingSearch();

            APIs.GetBuyerHome(this, this);
//            swipeRefreshLayout.setEnabled(true);
//            try {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    swipeRefreshLayout.setProgressViewOffset(false, 0, 35);
//                }
//                swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light, android.R.color.holo_purple, android.R.color.holo_blue_light);
//                swipeRefreshLayout.setOnRefreshListener(() -> {
//                    APIs.GetBuyerHome(this, this);
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

    public void bindViews(View rootView) {
        try {
            mSearchView = (FloatingSearchView) rootView.findViewById(R.id.floating_search_view);
            EditText editText = (EditText) mSearchView.findViewById(R.id.search_bar_text);
            editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium));
            mLl_view = (LinearLayout) rootView.findViewById(R.id.ll_view);
            mRv_items = (RecyclerView) rootView.findViewById(R.id.rv_items);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            mRv_items.setLayoutManager(manager);

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        try {
            AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            view.startAnimation(buttonClick);
            if (view == mNav_my_profile) {
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResult(JSONObject jobjWhole) {
        try {
            swipeRefreshLayout.setRefreshing(false);
            if (jobjWhole != null) {
                String strApiName = jobjWhole.optString("api");
                if (strApiName.equalsIgnoreCase("GetBuyerHome")) {
                    JSONArray jarray = jobjWhole.optJSONArray("resData");
                    if (jarray != null) {
                        listArray.clear();
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jo = jarray.optJSONObject(i);
                            String strimg;
                            if (i == 0)
                                strimg = "https://2.bp.blogspot.com/-wXBht38cDw4/WEFhq2JZFYI/AAAAAAAADMI/57HnJbpCwcAYKoJvhs_ANSXh-ceOYh9pACLcB/s400/banner3%2B%25281%2529.png";
                            else
                                strimg = "http://www.webindia123.com/fashionfabrics/images/saree.jpg";

                            listArray.add(new Brand(jo.optString("CategoryId"), jo.optString("CategoryName"), strimg));//jo.optString("CategoryImage")
                        }
                        setListAdapter(listArray);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListAdapter(ArrayList<Brand> listArray) {
        try {
            if (listArray.size() == 0) {
                mRv_items.setVisibility(View.GONE);
            } else {
                mRv_items.setVisibility(View.VISIBLE);
                adapter = new HomeCategoryAdapterBuyer(this, listArray);
                mRv_items.setAdapter(adapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            setupFloatingSearch();
            DataHelper.sColorSuggestions = commonMethods.getSavedSearchArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setupFloatingSearch() {
        try {
            mSearchView.setOnQueryChangeListener((oldQuery, newQuery) -> {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {
                    mSearchView.showProgress();
                    DataHelper.findSuggestions(MainActivityBuyer.this, newQuery, 5,
                            250, results -> {
                                mSearchView.swapSuggestions(results);
                                mSearchView.hideProgress();
                            });
                }
            });

            mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
                @Override
                public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                    Suggestion colorSuggestion = (Suggestion) searchSuggestion;
                    commonMethods.performSearch(MainActivityBuyer.this, colorSuggestion.getBody());
                    mLastQuery = searchSuggestion.getBody();
                }

                @Override
                public void onSearchAction(String query) {
                    mLastQuery = query;
                    commonMethods.performSearch(MainActivityBuyer.this, query);
                }
            });

            mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
                @Override
                public void onFocus() {
                    DataHelper.sColorSuggestions = commonMethods.getSavedSearchArray();
                    mSearchView.swapSuggestions(DataHelper.getHistory(MainActivityBuyer.this, 3));
                }

                @Override
                public void onFocusCleared() {
                    DataHelper.sColorSuggestions = commonMethods.getSavedSearchArray();
                    mSearchView.setSearchBarTitle(mLastQuery);
                }
            });

            mSearchView.setOnBindSuggestionCallback((suggestionView, leftIcon, textView, item, itemPosition) -> {
                Suggestion colorSuggestion = (Suggestion) item;

                String textColor = mIsDarkSearchTheme ? "#ffffff" : "#000000";
                String textLight = mIsDarkSearchTheme ? "#bfbfbf" : "#787878";

                if (colorSuggestion.getIsHistory()) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_history_black_24dp, null));

                    Util.setIconColor(leftIcon, Color.parseColor(textColor));
                    leftIcon.setAlpha(.36f);
                } else {
                    leftIcon.setAlpha(0.0f);
                    leftIcon.setImageDrawable(null);
                }

                textView.setTextColor(Color.parseColor(textColor));
                String text = colorSuggestion.getBody()
                        .replaceFirst(mSearchView.getQuery(),
                                "<font color=\"" + textLight + "\">" + mSearchView.getQuery() + "</font>");
                textView.setText(Html.fromHtml(text));
            });

            mSearchView.setOnSuggestionsListHeightChanged(newHeight -> mLl_view.setTranslationY(newHeight));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
