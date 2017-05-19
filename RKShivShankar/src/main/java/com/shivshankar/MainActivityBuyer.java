package com.shivshankar;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.adapters.HomeCategoryAdapterBuyer;
import com.shivshankar.classes.Brand;
import com.shivshankar.classes.DataHelper;
import com.shivshankar.classes.Suggestion;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivityBuyer extends BaseActivityBuyer implements OnResult {
    LinearLayout mLl_view;
    private FloatingSearchView mSearchView;
    RecyclerView mRv_items;
    private Spinner sp_Category;

    private boolean mIsDarkSearchTheme = false;
    private String mLastQuery = "", strCategoryID = "";
    ArrayList<Brand> listArray = new ArrayList<Brand>();
    HomeCategoryAdapterBuyer adapter;
    boolean isBackpressedOnce = false;
    String[] SP_CATEGORY_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        try {
            View rootView = getLayoutInflater().inflate(R.layout.activity_main_buyer, frameLayout);
            bindViews(rootView);
            setupFloatingSearch();

            APIs.GetBuyerHome(this, this);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        if (isBackpressedOnce) {
            finish();
            overridePendingTransition(0, 0);
        } else {
            if (!onActivityBackPress()) {
                isBackpressedOnce = true;
                Toast.makeText(MainActivityBuyer.this, "Press again to close " + commonVariables.appname, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showGuideline() {
        try {
            final SpannableString strMenu = new SpannableString("This will open Navigation Menu, which contain quick links for Profile, Orders, Policy, About Us and Contact Us."),
                    strNotification = new SpannableString("You can see all notifications for order status, discounts, coupons and special offers after clicking here."),
                    strCart = new SpannableString("You can see products you have added to your cart by clicking this button.");
            strMenu.setSpan(new StyleSpan(Typeface.ITALIC), 15, 15 + "Navigation Menu".length(), 0);
            strCart.setSpan(new StyleSpan(Typeface.ITALIC), 44, 44 + "cart".length(), 0);
            strNotification.setSpan(new StyleSpan(Typeface.ITALIC), 16, 16 + "notifications".length(), 0);

            final TapTargetSequence sequence = new TapTargetSequence(this)
                    .targets(
                            TapTarget.forToolbarNavigationIcon(toolbar, "This is Menu button", strMenu)
                                    .id(1)
                                    .dimColor(android.R.color.white)
                                    .outerCircleColor(R.color.colorPrimary)
                                    .targetCircleColor(android.R.color.white)
                                    .textColor(android.R.color.white)
                                    .targetRadius(50)
                                    .cancelable(false)
                            , TapTarget.forView(((ActionMenuView) toolbar.getChildAt(2)).getChildAt(0), "Cart", strCart)
                                    .textColorInt(ContextCompat.getColor(this, R.color.white))
                                    .id(2)
                                    .dimColor(android.R.color.white)
                                    .outerCircleColor(R.color.colorPrimary)
                                    .targetCircleColor(android.R.color.white)
                                    .targetRadius(50)
                                    .textColor(android.R.color.white)
                            , TapTarget.forView(((ActionMenuView) toolbar.getChildAt(2)).getChildAt(1), "Notifications", strNotification)
                                    .textColorInt(ContextCompat.getColor(this, R.color.white))
                                    .id(3)
                                    .dimColor(android.R.color.white)
                                    .outerCircleColor(R.color.colorPrimary)
                                    .targetCircleColor(android.R.color.white)
                                    .targetRadius(50)
                    );
            sequence.start();
            AppPreferences.getPrefs().edit().putBoolean(commonVariables.KEY_FIRST_TIME, false).apply();
        } catch (Exception e) {
            AppPreferences.getPrefs().edit().putBoolean(commonVariables.KEY_FIRST_TIME, true).apply();
            e.printStackTrace();

            Log.e("TAGRK", "Error:" + e.toString());
        }
    }

    public void bindViews(View rootView) {
        try {
            mSearchView = (FloatingSearchView) rootView.findViewById(R.id.floating_search_view);
            sp_Category = (Spinner) mSearchView.findViewById(R.id.sp_category);
            EditText editText = (EditText) mSearchView.findViewById(R.id.search_bar_text);
            editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium));
            mLl_view = (LinearLayout) rootView.findViewById(R.id.ll_view);
            mRv_items = (RecyclerView) rootView.findViewById(R.id.rv_items);
            int col = getResources().getInteger(R.integer.col_home);
            GridLayoutManager manager = new GridLayoutManager(this, col);
            mRv_items.setLayoutManager(manager);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResult(JSONObject jobjWhole) {
        try {
            if (AppPreferences.getPrefs().getBoolean(commonVariables.KEY_FIRST_TIME, true)) {
                showGuideline();
            }
            swipeRefreshLayout.setRefreshing(false);
            if (jobjWhole != null) {
                String strApiName = jobjWhole.optString("api");
                if (strApiName.equalsIgnoreCase("GetBuyerHome")) {
                    JSONArray jarray = jobjWhole.optJSONArray("resData");
                    String[] SP_CATEGORY = new String[jarray.length()];
                    SP_CATEGORY_VALUE = new String[jarray.length()];
                    if (jarray != null) {
                        listArray.clear();
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jo = jarray.optJSONObject(i);
                            listArray.add(new Brand(jo.optString("CategoryId"), jo.optString("CategoryName"), jo.optString("CategoryImage")));
                            SP_CATEGORY[i] = jo.optString("CategoryName");
                            SP_CATEGORY_VALUE[i] = jo.optString("CategoryId");
                        }
                        setListAdapter(listArray);
                        setCategory(SP_CATEGORY, SP_CATEGORY_VALUE);
                    }
                    SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
                    editor.putInt(commonVariables.CART_COUNT, jobjWhole.optInt("CartCount"));
                    editor.putInt(commonVariables.KEY_NOTI_COUNT, jobjWhole.optInt("notificationCount"));
                    editor.apply();

                    setCartAndNotiCount();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCategory(String[] sp_category, String[] sp_category_value) {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, sp_category);
        sp_Category.setAdapter(arrayAdapter);
        sp_Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strCategoryID = sp_category_value[i];
                if (!mLastQuery.isEmpty())
                    commonMethods.performSearch(MainActivityBuyer.this, mLastQuery, strCategoryID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
            isBackpressedOnce = false;
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
                    commonMethods.performSearch(MainActivityBuyer.this, colorSuggestion.getBody(), strCategoryID);
                    mLastQuery = searchSuggestion.getBody();
                }

                @Override
                public void onSearchAction(String query) {
                    mLastQuery = query;
                    commonMethods.performSearch(MainActivityBuyer.this, query, strCategoryID);
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
                suggestionView.setBackgroundColor(getResources().getColor(R.color.white));
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

    public boolean onActivityBackPress() {
        //if mSearchView.setSearchFocused(false) causes the focused search
        //to close, then we don't want to close the activity. if mSearchView.setSearchFocused(false)
        //returns false, we know that the search was already closed so the call didn't change the focus
        //state and it makes sense to call supper onBackPressed() and close the activity
        if (!mSearchView.setSearchFocused(false)) {
            return false;
        }
        return true;
    }
}
