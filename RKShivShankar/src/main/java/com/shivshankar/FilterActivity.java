package com.shivshankar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.shivshankar.adapters.FilterAttrubuteAdapter;
import com.shivshankar.classes.FilterAttribute;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class FilterActivity extends AppCompatActivity implements OnClickListener, OnResult, OnItemClickListener {

    private static final String TAG = "TAG FilterActivity";
    private TextView mTv_menu_clear, mTv_menu_apply;

    private LinearLayout mLl_ocation, mLl_fabric, mLl_color, mLl_price, mLl_discount;
    private TextView mTv_ocation, mTv_fabric, mTv_color, mTv_price, mTv_discount;
    private ListView mLv_ocation, mLv_fabric, mLv_color, mLv_price, mLv_discount;

    String strOcation = "^Ocassion_", strFabric = "^Fabric_", strPrice = "^Price_", strDiscount = "";
    public String strColor = "^Color_";

    String strCategoryName;
    String strFilter;
    ArrayList<FilterAttribute> listOcation = new ArrayList<FilterAttribute>();
    ArrayList<FilterAttribute> listFabric = new ArrayList<FilterAttribute>();
    //	ArrayList<ColorJodhaa> listColor = new ArrayList<ColorJodhaa>();
    ArrayList<FilterAttribute> listPrice = new ArrayList<FilterAttribute>();
    ArrayList<FilterAttribute> listDiscount = new ArrayList<FilterAttribute>();

    //	ArrayAdapter<ColorJodhaa> adapterColor;
    FilterAttrubuteAdapter adapterOcation, adapterDiscount, adapterFabric, adapterPrice;
    public ArrayList<String> arryAttrbs = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            setContentView(R.layout.activity_filter);

//			strCategoryName = getIntent().getStringExtra(commonVariables.INTENT_EXTRA_CATEGORY_NAME);
//			strFilter = getIntent().getStringExtra(commonVariables.INTENT_EXTRA_FILTER_DATA);
//			strColor = getIntent().getStringExtra(commonVariables.INTENT_FILTER_COLOR);
//			strDiscount = getIntent().getStringExtra(commonVariables.INTENT_FILTER_DISCOUNT);
//			strOcation = getIntent().getStringExtra(commonVariables.INTENT_FILTER_OCATION);
//			strFabric = getIntent().getStringExtra(commonVariables.INTENT_FILTER_FABRIC);
//			strPrice = getIntent().getStringExtra(commonVariables.INTENT_FILTER_PRICE);

//			arryAttrbs = getIntent().getStringArrayListExtra(commonVariables.INTENT_FILTER_BACKUP);
            if (arryAttrbs == null)
                arryAttrbs = new ArrayList<String>();
            setActionBar();
            bindViews();
//			APIs.GetProductListFilters_Buyer();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void bindViews() {
        try {
            mTv_ocation = (TextView) findViewById(R.id.tv_ocation);
            mLl_ocation = (LinearLayout) findViewById(R.id.ll_ocation);
            mLl_ocation.setOnClickListener(this);
            mTv_fabric = (TextView) findViewById(R.id.tv_fabric);
            mLl_fabric = (LinearLayout) findViewById(R.id.ll_fabric);
            mLl_fabric.setOnClickListener(this);
            mTv_color = (TextView) findViewById(R.id.tv_color);
            mLl_color = (LinearLayout) findViewById(R.id.ll_color);
            mLl_color.setOnClickListener(this);
            mTv_price = (TextView) findViewById(R.id.tv_unit_price);
            mLl_price = (LinearLayout) findViewById(R.id.ll_price);
            mLl_price.setOnClickListener(this);
            mTv_discount = (TextView) findViewById(R.id.tv_discount);
            mLl_discount = (LinearLayout) findViewById(R.id.ll_discount);
            mLl_discount.setOnClickListener(this);

            mLv_ocation = (ListView) findViewById(R.id.lv_ocation);
            mLv_ocation.setOnItemClickListener(this);
            mLv_fabric = (ListView) findViewById(R.id.lv_fabric);
            mLv_fabric.setOnItemClickListener(this);
            mLv_color = (ListView) findViewById(R.id.lv_color);
            mLv_color.setOnItemClickListener(this);
            mLv_price = (ListView) findViewById(R.id.lv_price);
            mLv_price.setOnItemClickListener(this);
            mLv_discount = (ListView) findViewById(R.id.lv_discount);
            mLv_discount.setOnItemClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setActionBar() {
        try {

//			mTv_menu_clear = (TextView) findViewById(R.id.tv_clear);
//			mTv_menu_clear.setOnClickListener(this);
//			mTv_menu_apply = (TextView) findViewById(R.id.tv_apply);
//			mTv_menu_apply.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void returnBack() {
        try {
            Intent output = new Intent();
            strColor = strColor.replace("^Color_,", "^Color_");
            strOcation = strOcation.replace("^Ocassion_,", "^Ocassion_");
            strFabric = strFabric.replace("^Fabric_,", "^Fabric_");
            strPrice = strPrice.replace("^Price_,", "^Price_");

            String strFilterData = strColor + strDiscount + strFabric + strOcation + strPrice;
            strFilterData = strFilterData.replaceAll("_,,^", "_");
            strFilterData = strFilterData.replaceAll("_,", "_");
            strFilterData = strFilterData.replaceAll(",,", "");
            strFilterData = strFilterData.replaceAll(",^", "^");

            Log.v(TAG, "rETURNING: " + strFilterData);
//			output.putExtra(commonVariables.INTENT_EXTRA_FILTER_DATA, strFilterData);
//			output.putExtra(commonVariables.INTENT_FILTER_COLOR, strColor);
//			output.putExtra(commonVariables.INTENT_FILTER_DISCOUNT, strDiscount);
//			output.putExtra(commonVariables.INTENT_FILTER_OCATION, strOcation);
//			output.putExtra(commonVariables.INTENT_FILTER_FABRIC, strFabric);
//			output.putExtra(commonVariables.INTENT_FILTER_PRICE, strPrice);
//
//			output.putStringArrayListExtra(commonVariables.INTENT_FILTER_BACKUP, arryAttrbs);
            setResult(RESULT_OK, output);
            finish();

            // overridePendingTransition(R.anim.slide_in_left,
            // R.anim.slide_out_right);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    Log.v(TAG, "COLOR:" + strColor + "\nOCATION:" + strOcation + "\nPRICE:" + strPrice + "\nFabric:" + strFabric + "\nDiscount:" + strDiscount);
                    // onBackPressed();
                    finish();
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        try {
            AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            view.startAnimation(buttonClick);
            if (view == mTv_menu_clear) {

                // if (mLv_ocation.getVisibility() == View.VISIBLE) {
                clearItems(mLv_ocation, adapterOcation);
                strOcation = "^Ocassion_";
                // } else if (mLv_color.getVisibility() == View.VISIBLE) {
//				clearItemsColor(mLv_color, adapterColor);
                strColor = "^Color_";
                // } else if (mLv_price.getVisibility() == View.VISIBLE) {
                clearItems(mLv_price, adapterPrice);
                strPrice = "^Price_";
                // } else if (mLv_discount.getVisibility() == View.VISIBLE) {
                clearItems(mLv_discount, adapterDiscount);
                strDiscount = "";
                // } else if (mLv_fabric.getVisibility() == View.VISIBLE) {
                clearItems(mLv_fabric, adapterFabric);
                strFabric = "^Fabric_";
                // }

            } else if (view == mTv_menu_apply) {
                returnBack();
            } else if (view == mLl_ocation) {
                mLl_ocation.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_ocation.setTextColor(getResources().getColor(R.color.colorPrimary));
                mLl_fabric.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_fabric.setTextColor(getResources().getColor(R.color.black));
                mLl_color.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_color.setTextColor(getResources().getColor(R.color.black));
                mLl_price.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_price.setTextColor(getResources().getColor(R.color.black));
                mLl_discount.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_discount.setTextColor(getResources().getColor(R.color.black));

                mLv_ocation.setVisibility(View.VISIBLE);
                mLv_fabric.setVisibility(View.GONE);
                mLv_color.setVisibility(View.GONE);
                mLv_price.setVisibility(View.GONE);
                mLv_discount.setVisibility(View.GONE);

            } else if (view == mLl_fabric) {
                mLl_ocation.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_ocation.setTextColor(getResources().getColor(R.color.black));
                mLl_fabric.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_fabric.setTextColor(getResources().getColor(R.color.colorPrimary));
                mLl_color.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_color.setTextColor(getResources().getColor(R.color.black));
                mLl_price.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_price.setTextColor(getResources().getColor(R.color.black));
                mLl_discount.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_discount.setTextColor(getResources().getColor(R.color.black));

                mLv_ocation.setVisibility(View.GONE);
                mLv_fabric.setVisibility(View.VISIBLE);
                mLv_color.setVisibility(View.GONE);
                mLv_price.setVisibility(View.GONE);
                mLv_discount.setVisibility(View.GONE);

            } else if (view == mLl_color) {
                mLl_ocation.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_ocation.setTextColor(getResources().getColor(R.color.black));
                mLl_fabric.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_fabric.setTextColor(getResources().getColor(R.color.black));
                mLl_color.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_color.setTextColor(getResources().getColor(R.color.colorPrimary));
                mLl_price.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_price.setTextColor(getResources().getColor(R.color.black));
                mLl_discount.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_discount.setTextColor(getResources().getColor(R.color.black));

                mLv_ocation.setVisibility(View.GONE);
                mLv_fabric.setVisibility(View.GONE);
                mLv_color.setVisibility(View.VISIBLE);
                mLv_price.setVisibility(View.GONE);
                mLv_discount.setVisibility(View.GONE);

            } else if (view == mLl_price) {
                mLl_ocation.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_ocation.setTextColor(getResources().getColor(R.color.black));
                mLl_fabric.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_fabric.setTextColor(getResources().getColor(R.color.black));
                mLl_color.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_color.setTextColor(getResources().getColor(R.color.black));
                mLl_price.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_price.setTextColor(getResources().getColor(R.color.colorPrimary));
                mLl_discount.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_discount.setTextColor(getResources().getColor(R.color.black));

                mLv_ocation.setVisibility(View.GONE);
                mLv_fabric.setVisibility(View.GONE);
                mLv_color.setVisibility(View.GONE);
                mLv_price.setVisibility(View.VISIBLE);
                mLv_discount.setVisibility(View.GONE);

            } else if (view == mLl_discount) {
                mLl_ocation.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_ocation.setTextColor(getResources().getColor(R.color.black));
                mLl_fabric.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_fabric.setTextColor(getResources().getColor(R.color.black));
                mLl_color.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_color.setTextColor(getResources().getColor(R.color.black));
                mLl_price.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_price.setTextColor(getResources().getColor(R.color.black));
                mLl_discount.setBackgroundColor(getResources().getColor(R.color.white));
                mTv_discount.setTextColor(getResources().getColor(R.color.colorPrimary));

                mLv_ocation.setVisibility(View.GONE);
                mLv_fabric.setVisibility(View.GONE);
                mLv_color.setVisibility(View.GONE);
                mLv_price.setVisibility(View.GONE);
                mLv_discount.setVisibility(View.VISIBLE);
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void clearItems(ListView lv, FilterAttrubuteAdapter adapterOcation2) {
        try {
            SparseBooleanArray checkedItemPositions = lv.getCheckedItemPositions();
            int itemCount = lv.getCount();
            for (int i = 0; i < itemCount; i++) {
                if (checkedItemPositions.get(i)) {
                    lv.performItemClick(lv.getChildAt(i), i, 90);
                }
            }
            checkedItemPositions.clear();
            if (adapterOcation2 != null)
                adapterOcation2.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void clearItemsColor(ListView lv, ArrayAdapter<ColorJodhaa> adapterColor2) {
//		try {
//			SparseBooleanArray checkedItemPositions = lv.getCheckedItemPositions();
//			int itemCount = lv.getCount();
//			for (int i = 0; i < itemCount; i++) {
//				if (checkedItemPositions.get(i)) {
//					lv.performItemClick(lv.getChildAt(i), i, 90);
//				}
//			}
//			checkedItemPositions.clear();
//			adapterColor2.notifyDataSetChanged();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//    }


    private boolean isInAttrList(String string) {
        boolean isIn = false;
        if (arryAttrbs != null)
            for (int i = 0; i < arryAttrbs.size(); i++) {
                if (arryAttrbs.get(i).equalsIgnoreCase(string)) {
                    isIn = true;
                    break;
                }
            }
        return isIn;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            View convertView = view;
            if (parent == mLv_ocation) {

                if (!strOcation.contains(listOcation.get(position).getName())) {
                    strOcation = strOcation + "," + listOcation.get(position).getName();
                    arryAttrbs.add(listOcation.get(position).getName());
                    listOcation.get(position).setSelected(true);
                } else {
                    strOcation = strOcation.replace(listOcation.get(position).getName(), "");
                    arryAttrbs.remove(arryAttrbs.indexOf(listOcation.get(position).getName()));
                    listOcation.get(position).setSelected(false);
                }
                adapterOcation.notifyDataSetChanged();

            } else if (parent == mLv_fabric) {

                if (!strFabric.contains(listFabric.get(position).getName())) {
                    strFabric = strFabric + "," + listFabric.get(position).getName();
                    arryAttrbs.add(listFabric.get(position).getName());
                    listFabric.get(position).setSelected(true);
                } else {
                    strFabric = strFabric.replace(listFabric.get(position).getName(), "");
                    arryAttrbs.remove(arryAttrbs.indexOf(listFabric.get(position).getName()));
                    listFabric.get(position).setSelected(false);
                }
                adapterFabric.notifyDataSetChanged();
            } else if (parent == mLv_color) {

                if (convertView != null) {
                    CheckedTextView mCb_filter_option = (CheckedTextView) convertView.findViewById(R.id.cb_filter_option);
                    mCb_filter_option.performClick();
                }
            } else if (parent == mLv_price) {
                boolean isselBef = listPrice.get(position).isSelected();
                for (int i = 0; i < listPrice.size(); i++) {
                    listPrice.get(i).setSelected(false);
                    if (arryAttrbs.indexOf(listPrice.get(i).getName()) != -1)
                        arryAttrbs.remove(listPrice.get(i).getName());
                }
                listPrice.get(position).setSelected(isselBef);

                if (!listPrice.get(position).isSelected()) {
                    listPrice.get(position).setSelected(true);
                    strPrice = "^Price_" + listPrice.get(position).getName();
                    arryAttrbs.add(listPrice.get(position).getName());
                } else {
                    listPrice.get(position).setSelected(false);
                    strPrice = "^Price_";
                    if (arryAttrbs.indexOf(listPrice.get(position).getName()) != -1)
                        arryAttrbs.remove(listPrice.get(position).getName());
                }
                adapterPrice.notifyDataSetChanged();
            } else if (parent == mLv_discount) {

                if (!strDiscount.contains(listDiscount.get(position).getName())) {
                    strDiscount = strDiscount + "," + listDiscount.get(position).getName();
                    arryAttrbs.add(listDiscount.get(position).getName());
                    listDiscount.get(position).setSelected(true);
                } else {
                    strDiscount = strDiscount.replace(listDiscount.get(position).getName(), "");
                    arryAttrbs.remove(arryAttrbs.indexOf(listDiscount.get(position).getName()));
                    listDiscount.get(position).setSelected(false);
                }
                adapterDiscount.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResult(JSONObject jobjWhole) {
        try {
            int count = 0;
            JSONArray jarrayFabric = jobjWhole.optJSONArray("ShopFabricList");
            for (int i = 0; i < jarrayFabric.length(); i++) {
                JSONObject jsonItem = jarrayFabric.optJSONObject(i);
                boolean isSelected = false;
                String val = jsonItem.optString("FabricValue");
                if (isInAttrList(val)) {
                    isSelected = true;
                    count = count + 1;
                    if (!strFabric.contains(val))
                        strFabric = strFabric + "," + val;
                    if (arryAttrbs != null)
                        if (arryAttrbs.indexOf(val) == -1)
                            arryAttrbs.add(val);
                } else {
                    String st = val;
                    if (strFabric.contains(st + ","))
                        st = st + ",";
                    strFabric = strFabric.replace(st, "");
                    if (arryAttrbs != null)
                        if (arryAttrbs.indexOf(val) != -1)
                            arryAttrbs.remove(arryAttrbs.indexOf(val));
                }
                listFabric.add(new FilterAttribute(jsonItem.optString("FabricValue"), jsonItem.optString("TotalCount"), isSelected));
            }
            if (count != 0)
                mTv_fabric.setText("Fabric (" + count + ")");
            if (listFabric.size() == 0)
                mLl_fabric.setVisibility(View.GONE);
            else {
                adapterFabric = new FilterAttrubuteAdapter(this, R.layout.adapter_row_checkbox, listFabric);
                mLv_fabric.setAdapter(adapterFabric);
            }

            count = 0;
            JSONArray jarrayPrice = jobjWhole.optJSONArray("ShopPriceList");
            for (int i = 0; i < jarrayPrice.length(); i++) {
                JSONObject jsonItem = jarrayPrice.optJSONObject(i);
                boolean isSelected = false;
                String val = jsonItem.optString("MinPrice") + "-" + jsonItem.optString("MaxPrice");
                if (isInAttrList(val)) {
                    isSelected = true;
                    count = count + 1;
                }
                listPrice.add(new FilterAttribute(jsonItem.optString("MinPrice") + "-" + jsonItem.optString("MaxPrice"), jsonItem.optString("TotalCount"), isSelected));
            }
            if (count != 0)
                mTv_price.setText("Price (" + count + ")");
            if (listPrice.size() == 0)
                mLl_price.setVisibility(View.GONE);
            else {
                adapterPrice = new FilterAttrubuteAdapter(this, R.layout.adapter_row_checkbox, listPrice);
                mLv_price.setAdapter(adapterPrice);
            }
            count = 0;
            JSONArray jarrayColor = jobjWhole.optJSONArray("ShopColorList");
//            for (int i = 0; i < jarrayColor.length(); i++) {
//                JSONObject jsonItem = jarrayColor.optJSONObject(i);
//                boolean isSelected = false;
//
//                if (isInAttrList(jsonItem.optString("ColorId"))) {
//                    isSelected = true;
//                    count = count + 1;
//                }
//                listColor.add(new ColorJodhaa(jsonItem.optString("ColorId"), jsonItem.optString("ColorCode"), jsonItem.optString("HexValue"), jsonItem.optString("TotalCount"), isSelected));
//            }
//            if (count != 0)
//                mTv_color.setText("Color (" + count + ")");
//            if (listColor.size() == 0)
//                mLl_color.setVisibility(View.GONE);
//            else {
//                adapterColor = new ColorListAdapter(this, R.layout.adapter_row_color_checkbox, listColor);
//                mLv_color.setAdapter(adapterColor);
//            }

            count = 0;
            JSONArray jarrayOcation = jobjWhole.optJSONArray("ShopOcassionList");
            for (int i = 0; i < jarrayOcation.length(); i++) {
                JSONObject jsonItem = jarrayOcation.optJSONObject(i);
                boolean isSelected = false;
                String val = jsonItem.optString("OcassionValue");
                if (isInAttrList(val)) {
                    isSelected = true;
                    count = count + 1;
                    if (!strOcation.contains(val))
                        strOcation = strOcation + "," + val;
                    if (arryAttrbs != null)
                        if (arryAttrbs.indexOf(val) == -1)
                            arryAttrbs.add(val);
                } else {
                    String st = val;
                    if (strOcation.contains(st + ","))
                        st = st + ",";
                    strOcation = strOcation.replace(st, "");
                    if (arryAttrbs != null)
                        if (arryAttrbs.indexOf(val) != -1)
                            arryAttrbs.remove(arryAttrbs.indexOf(val));
                }
                listOcation.add(new FilterAttribute(jsonItem.optString("OcassionValue"), jsonItem.optString("TotalCount"), isSelected));
            }
            if (count != 0)
                mTv_ocation.setText("Ocation (" + count + ")");
            if (listOcation.size() == 0)
                mLl_ocation.setVisibility(View.GONE);
            else {
                adapterOcation = new FilterAttrubuteAdapter(this, R.layout.adapter_row_checkbox, listOcation);
                mLv_ocation.setAdapter(adapterOcation);
            }

            count = 0;
            JSONArray jarrayDiscount = jobjWhole.optJSONArray("ShopDiscountList");
            for (int i = 0; i < jarrayDiscount.length(); i++) {
                JSONObject jsonItem = jarrayDiscount.optJSONObject(i);
                boolean isSelected = false;
                String val = jsonItem.optString("amount");
                if (isInAttrList(val)) {
                    isSelected = true;
                    count = count + 1;
                    if (!strDiscount.contains(val))
                        strDiscount = strDiscount + "," + val;
                    if (arryAttrbs != null)
                        if (arryAttrbs.indexOf(val) == -1)
                            arryAttrbs.add(val);
                } else {
                    String st = val;
                    if (strDiscount.contains(st + ","))
                        st = st + ",";
                    strDiscount = strDiscount.replace(st, "");
                    if (arryAttrbs != null)
                        if (arryAttrbs.indexOf(val) != -1)
                            arryAttrbs.remove(arryAttrbs.indexOf(val));
                }
                listDiscount.add(new FilterAttribute(jsonItem.getString("amount"), jsonItem.optString("TotalCount"), isSelected));
            }
            if (count != 0)
                mTv_discount.setText("Discount (" + count + ")");
            if (listDiscount.size() == 0)
                mLl_discount.setVisibility(View.GONE);
            else {
                adapterDiscount = new FilterAttrubuteAdapter(this, R.layout.adapter_row_checkbox, listDiscount);
                mLv_discount.setAdapter(adapterDiscount);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}