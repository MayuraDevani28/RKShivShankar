package com.shivshankar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shivshankar.ServerCall.APIs;
import com.shivshankar.adapters.FilterAttrubuteAdapter;
import com.shivshankar.classes.FilterAttribute;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class FilterActivityBuyer extends AppCompatActivity implements OnClickListener, OnResult {

    public ImageView mIv_close;
    private TextView mTv_menu_clear, mTv_menu_apply;
    LinearLayout mLl_whole_item;

    String strFabricType, brandId;
    LinearLayout mLl_filters, mLl_lists;
    int suit_or_fab;//1=suit, 2=fab
    ArrayList<TextView> listTextView = new ArrayList<TextView>();
    ArrayList<RecyclerView> listList = new ArrayList<RecyclerView>();
    String strCategoryIds = "", strPriceRange = "", strFabricIds = "", strSortBy = "";

    ArrayList<ArrayList<FilterAttribute>> listSelOptions = new ArrayList<>();
    ArrayList<FilterAttrubuteAdapter> listAdapter = new ArrayList<>();

    int white, primary, black;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }

            setContentView(R.layout.activity_filter);

            strFabricType = getIntent().getStringExtra(commonVariables.KEY_FABRIC_TYPE);
            brandId = getIntent().getStringExtra(commonVariables.KEY_BRAND);
            suit_or_fab = getIntent().getIntExtra(commonVariables.KEY_SUIT_OR_FABRIC, 1);

            strCategoryIds = getIntent().getStringExtra(commonVariables.INTENT_EXTRA_KEY_FILTER_CAT);
            if (strCategoryIds == null)
                strCategoryIds = "";
            strFabricIds = getIntent().getStringExtra(commonVariables.INTENT_EXTRA_KEY_FILTER_FABRIC);
            if (strFabricIds == null)
                strFabricIds = "";
            strPriceRange = getIntent().getStringExtra(commonVariables.INTENT_EXTRA_KEY_FILTER_PRICE);
            if (strPriceRange == null)
                strPriceRange = "";
            strSortBy = getIntent().getStringExtra(commonVariables.INTENT_EXTRA_KEY_FILTER_SORT);
            if (strSortBy == null)
                strSortBy = "";

            white = ContextCompat.getColor(this, R.color.white);
            primary = ContextCompat.getColor(this, R.color.colorPrimary);
            black = ContextCompat.getColor(this, R.color.black);

            bindViews();
            APIs.GetProductListFilters_Buyer(this, this, strFabricType, brandId, suit_or_fab);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void bindViews() {
        try {
            mIv_close = (ImageView) findViewById(R.id.iv_close);
            mIv_close.setOnClickListener(this);
            mLl_whole_item = (LinearLayout) findViewById(R.id.ll_whole_item);
            mTv_menu_clear = (TextView) findViewById(R.id.tv_clear);
            mTv_menu_clear.setOnClickListener(this);
            mTv_menu_apply = (TextView) findViewById(R.id.tv_apply);
            mTv_menu_apply.setOnClickListener(this);

            mLl_filters = (LinearLayout) findViewById(R.id.ll_filters);
            mLl_lists = (LinearLayout) findViewById(R.id.ll_lists);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_bottom);
    }

    private void returnBack() {
        try {
            String stSort = "", stCat = "", stPrice = "", stFab = "";
            for (int i = 0; i < listTextView.size(); i++) {
                String filter = listTextView.get(i).getTag(R.string.filter_key).toString();
                String str = ((FilterAttrubuteAdapter) listList.get(i).getAdapter()).getSelectedItems();
                if (filter.equalsIgnoreCase("sort"))
                    stSort = str;
                else if (filter.equalsIgnoreCase("fabric"))
                    stFab = str;
                else if (filter.equalsIgnoreCase("price"))
                    stPrice = str;
                else if (filter.equalsIgnoreCase("category"))
                    stCat = str;
            }
            Intent output = new Intent();
            output.putExtra(commonVariables.INTENT_EXTRA_KEY_FILTER_SORT, stSort);
            output.putExtra(commonVariables.INTENT_EXTRA_KEY_FILTER_CAT, stCat);
            output.putExtra(commonVariables.INTENT_EXTRA_KEY_FILTER_PRICE, stPrice);
            output.putExtra(commonVariables.INTENT_EXTRA_KEY_FILTER_FABRIC, stFab);
            setResult(RESULT_OK, output);
            finish();
            overridePendingTransition(R.anim.hold, R.anim.slide_bottom);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        try {
            AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            view.startAnimation(buttonClick);
            if (view == mTv_menu_clear) {
                clearItems();
            } else if (view == mIv_close) {
                onBackPressed();
            } else if (view == mTv_menu_apply) {
                returnBack();
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void clearItems() {
        try {
            for (int i = 0; i < listSelOptions.size(); i++) {
                ArrayList<FilterAttribute> li = listSelOptions.get(i);
                for (int j = 0; j < li.size(); j++) {
                    li.get(j).setSelected(false);
                }
            }

            for (int i = 0; i < listAdapter.size(); i++) {
                listAdapter.get(i).notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResult(JSONObject jobjWhole) {
        try {
            mLl_whole_item.setVisibility(View.VISIBLE);
            JSONArray jarWhole = jobjWhole.optJSONArray("resData");
            if (jarWhole != null) {
                int val12 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());
                int val14 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getResources().getDisplayMetrics());
                for (int i = 0; i < jarWhole.length(); i++) {
                    JSONObject jobjFilter = jarWhole.optJSONObject(i);
                    TextView mTv = new TextView(this);
                    mTv.setText(jobjFilter.optString("filterName"));
                    mTv.setTextColor(black);
                    mTv.setGravity(Gravity.CENTER);
                    mTv.setTag(R.string.position, i);
                    String filter = jobjFilter.optString("filterKey");
                    mTv.setTag(R.string.filter_key, filter);
                    mTv.setPadding(val14, val12, val14, val12);
                    mTv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int pos = (int) view.getTag(R.string.position);
                            setList(pos);

                        }
                    });
                    listTextView.add(mTv);
                    mLl_filters.addView(mTv);

                    RecyclerView mRv = new RecyclerView(this);
                    mRv.setLayoutManager(new LinearLayoutManager(this));

                    JSONArray jarr = jobjFilter.optJSONArray("lstFilters");
                    ArrayList<FilterAttribute> listFilter = new ArrayList<>();
                    if (jarr != null && jarr.length() != 0) {
                        for (int j = 0; j < jarr.length(); j++) {
                            JSONObject jsonItem = jarr.optJSONObject(j);
                            boolean isSelected = false;
                            try {
                                if (filter.equalsIgnoreCase("sort"))
                                    isSelected = strSortBy.equalsIgnoreCase(jsonItem.getString("Id"));//isSelected(strSortBy,jsonItem.getString("Id"));
                                else if (filter.equalsIgnoreCase("fabric"))
                                    isSelected = isSelected(strFabricIds, jsonItem.getString("Id"));
                                else if (filter.equalsIgnoreCase("price"))
                                    isSelected = isSelected(strPriceRange, jsonItem.getString("Id"));
                                else if (filter.equalsIgnoreCase("category"))
                                    isSelected = isSelected(strCategoryIds, jsonItem.getString("Id"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            listFilter.add(new FilterAttribute(jsonItem.optString("Id"), jsonItem.optString("Name"), isSelected));
                        }
                        FilterAttrubuteAdapter adapter = new FilterAttrubuteAdapter(this, listFilter, filter.equalsIgnoreCase("sort"));
                        listAdapter.add(adapter);
                        mRv.setAdapter(adapter);
                        mRv.setVisibility(View.GONE);
                        listList.add(mRv);
                        mLl_lists.addView(mRv);
                    }
                    listSelOptions.add(listFilter);
                }
                listTextView.get(0).performClick();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isSelected(String id, String idCurrent) {
        boolean isIn = false;
        String[] arr = id.split(",");
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equalsIgnoreCase(idCurrent)) {
                isIn = true;
                break;
            }
        }
        return isIn;
    }

    private void setList(int pos) {
        for (int i = 0; i < listList.size(); i++) {
            listList.get(i).setVisibility(View.GONE);
            listTextView.get(i).setBackgroundColor(white);
            listTextView.get(i).setTextColor(black);
        }
        listList.get(pos).setVisibility(View.VISIBLE);
        listTextView.get(pos).setBackgroundColor(primary);
        listTextView.get(pos).setTextColor(white);
    }
}