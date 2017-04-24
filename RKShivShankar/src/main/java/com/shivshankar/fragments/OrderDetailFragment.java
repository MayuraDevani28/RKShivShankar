package com.shivshankar.fragments;

import android.annotation.SuppressLint;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shivshankar.MyOrdersActivityBuyer;
import com.shivshankar.R;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.adapters.OrderProductListAdapter;
import com.shivshankar.classes.CartItem;
import com.shivshankar.classes.Order;
import com.shivshankar.classes.SC3Object;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderDetailFragment extends Fragment implements OnClickListener, OnResult {

    public Order order;
    private ImageView mIv_close;
    private RecyclerView mLv_order_items;
    private TextView mTv_order_no, mTv_order_date, mTv_total, mName, mTv_address, mTv_mobile_no, mTv_customer_name;
    private TextView mTv_sub_total, mTv_discount_amount, mTv_discount_code_title, mTv_shipping_amount, mTv_product_total, mTv_order_status;
    private LinearLayout mBtn_home, mLl_detail, mLl_shipping_address, mLl_discount, mLl_shipping, mLl_order;


    ArrayList<CartItem> listArray = new ArrayList<CartItem>();
    String strLoginId = "";

    public OrderDetailFragment(Order orderD) {
        order = orderD;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_order_detail, container, false);
        try {
            strLoginId = AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "");

            bindViews(rootView);
            APIs.GetOrderDetails((AppCompatActivity) getActivity(), this, order.getOrderId());
            mTv_order_date.setText(order.getOrderDate());
            mTv_order_no.setText(order.getOrderNo());

            int color = R.color.black;
            String status = order.getStatus();
            if (!status.isEmpty()) {
                if (status.equalsIgnoreCase("In Process"))
                    color = R.color.status_in_process;
                else if (status.equalsIgnoreCase("PAYMENT RECEIVED"))
                    color = R.color.status_payment_received;
                else if (status.equalsIgnoreCase("pending"))
                    color = R.color.status_pending;
                else if (status.equalsIgnoreCase("complete"))
                    color = R.color.status_complete;
                else if (status.equalsIgnoreCase("INCOMPLETE"))
                    color = R.color.status_incomplete;
                else if (status.equalsIgnoreCase("DELIVERED"))
                    color = R.color.status_delivered;
                else if (status.equalsIgnoreCase("canceled"))
                    color = R.color.status_canceled;
            }
            mTv_order_status.setTextColor(ContextCompat.getColor(getActivity(), color));
            GradientDrawable bgShape = (GradientDrawable) mLl_order.getBackground();
            bgShape.setColor(ContextCompat.getColor(getActivity(), color));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootView;
    }

    private void bindViews(View rootView) {
        try {
            mIv_close = (ImageView) rootView.findViewById(R.id.iv_close);
            mTv_order_no = (TextView) rootView.findViewById(R.id.tv_order_no);
            mTv_order_date = (TextView) rootView.findViewById(R.id.tv_order_date);
            mLv_order_items = (RecyclerView) rootView.findViewById(R.id.lv_order_items);
            mLv_order_items.setNestedScrollingEnabled(false);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mLv_order_items.setLayoutManager(layoutManager);

            mTv_total = (TextView) rootView.findViewById(R.id.tv_total);
            mName = (TextView) rootView.findViewById(R.id.name);
            mTv_address = (TextView) rootView.findViewById(R.id.tv_address);
            mTv_mobile_no = (TextView) rootView.findViewById(R.id.tv_mobile_no);
            mTv_customer_name = (TextView) rootView.findViewById(R.id.tv_customer_name);
            mBtn_home = (LinearLayout) rootView.findViewById(R.id.ll_home);
            mBtn_home.setOnClickListener(this);
            mIv_close.setOnClickListener(this);

            mLl_shipping_address = (LinearLayout) rootView.findViewById(R.id.ll_shipping_address);

            mTv_order_status = (TextView) rootView.findViewById(R.id.tv_order_status);
            mTv_product_total = (TextView) rootView.findViewById(R.id.tv_product_total);
            mTv_sub_total = (TextView) rootView.findViewById(R.id.tv_sub_total);
            mTv_discount_code_title = (TextView) rootView.findViewById(R.id.tv_discount_code_title);
            mTv_discount_amount = (TextView) rootView.findViewById(R.id.tv_discount_amount);
            mTv_shipping_amount = (TextView) rootView.findViewById(R.id.tv_shipping_amount);
            mLl_discount = (LinearLayout) rootView.findViewById(R.id.ll_discount);
            mLl_shipping = (LinearLayout) rootView.findViewById(R.id.ll_shipping);
            mLl_detail = (LinearLayout) rootView.findViewById(R.id.ll_detail);

            mLl_order = (LinearLayout) rootView.findViewById(R.id.ll_order);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            if (v == mBtn_home) {
                ((MyOrdersActivityBuyer) getActivity()).onBackPressed();
            } else if (v == mIv_close) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
    }

    private void setAddressData(JSONObject jo) {

        mTv_mobile_no.setText(jo.optString("sMobile"));

        String strAddress = "";
        String str = jo.optString("sCustomerName");
        if (!str.isEmpty() && !str.equalsIgnoreCase("null"))
            strAddress = str + " ";

        mName.setText(strAddress);
        strAddress = "";

        str = jo.optString("sAddressLine1");
        if (!str.isEmpty() && !str.equalsIgnoreCase("null"))
            strAddress = strAddress + str;

        str = jo.optString("sAddressLine2");
        if (!str.isEmpty() && !str.equalsIgnoreCase("null"))
            strAddress = strAddress + ", " + str;

        str = jo.optString("sCityName");
        if (!str.isEmpty() && !str.equalsIgnoreCase("null"))
            strAddress = strAddress + ", " + str;

        str = jo.optString("sPincode");
        if (!str.isEmpty() && !str.equalsIgnoreCase("null"))
            strAddress = strAddress + ", " + str + "\n";

        str = jo.optString("sStateName");
        if (!str.isEmpty() && !str.equalsIgnoreCase("null"))
            strAddress = strAddress + "State: " + str + "\n";

        str = jo.optString("sCountryName");
        if (!str.isEmpty() && !str.equalsIgnoreCase("null"))
            strAddress = strAddress + "Country: " + str;

        mTv_address.setText(strAddress);
    }


    private void setListAdapter(ArrayList<CartItem> listArray2) {
        try {
            int l = listArray2.size();
            if (l != 0) {
                mLv_order_items.setVisibility(View.VISIBLE);
                mLv_order_items.setAdapter(new OrderProductListAdapter((AppCompatActivity) getActivity(), listArray2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResult(JSONObject jObWhole) {
        try {
            if (jObWhole != null) {
                String strAPIName = jObWhole.optString("api");
                if (strAPIName.equalsIgnoreCase("GetOrderDetails")) {
                    mLl_detail.setVisibility(View.VISIBLE);
                    JSONObject jObjAll = jObWhole.optJSONObject("resData");

                    mTv_customer_name.setText("Order by " + jObjAll.optString("CustomerName"));
                    mTv_order_date.setText("Ordered on " + jObjAll.optString("OrderDate"));
                    mTv_order_no.setText(jObjAll.optString("OrderNo"));

                    mTv_total.setText(commonVariables.strCurrency_name + " " + jObjAll.optString("SubTotal"));
                    mTv_order_status.setText(jObjAll.optString("OrderStatus"));

                    mTv_sub_total.setText(commonVariables.strCurrency_name + " " + jObjAll.optString("SubTotal"));
                    mTv_product_total.setText(commonVariables.strCurrency_name + " " + jObjAll.optString("TotalAmount"));
                    if (!jObjAll.optString("DiscountCodeAmt").equals("0")) {
                        mTv_discount_code_title.setText("Discount" + " (" + jObjAll.optString("DiscountCode") + ")");
                        mTv_discount_amount.setText("- " + commonVariables.strCurrency_name + " " + jObjAll.optString("DiscountCodeAmt"));
                        mLl_discount.setVisibility(View.VISIBLE);
                    } else
                        mLl_discount.setVisibility(View.GONE);

                    if (!jObjAll.optString("ShippingCharge").equals("0")) {
                        mTv_shipping_amount.setText(commonVariables.strCurrency_name + " " + jObjAll.optString("ShippingCharge"));
                        mLl_shipping.setVisibility(View.VISIBLE);
                    } else
                        mLl_shipping.setVisibility(View.GONE);

                    try {
                        JSONObject jObj = jObjAll.optJSONObject("OrderShippingInfo");
                        if (jObj.optString("sCustomerName").isEmpty()) {
                            mLl_shipping_address.setVisibility(View.GONE);
                        } else {
                            mLl_shipping_address.setVisibility(View.VISIBLE);
                            setAddressData(jObj);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    JSONArray jarray = jObjAll.optJSONArray("OrderDetailsList");
                    listArray.clear();
                    int len = jarray.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject jObjItem = jarray.optJSONObject(i);
                        int type = jObjItem.optInt("SuitFbricId"),
                                catQty = jObjItem.optInt("OrderQuantity"),
                                fabric_FrontQty = 0, fabric_BackQty = 0, fabric_BajuQty = 0, fabric_ExtraQty = 0;
                        double fabricCuts = 0, fabric_FrontCut = 0, fabric_BackCut = 0, fabric_BajuCut = 0, fabric_ExtraCut = 0;
                        String bodyPart = null, bodyFabricPart = null, fabric_Colors = null;


                        if (type == 2) {
                            JSONObject jobFabric = jObjItem.optJSONObject("lstFabricData");
                            bodyPart = jobFabric.optString("BodyPart");
                            bodyFabricPart = jobFabric.optString("BodyFabricPart");
                            fabricCuts = jobFabric.optDouble("FabricCuts");
                            catQty = jobFabric.optInt("FabricQty");
                            fabric_FrontQty = jobFabric.optInt("Fabric_FrontQty");
                            fabric_FrontCut = jobFabric.optDouble("Fabric_FrontCut");
                            fabric_BackQty = jobFabric.optInt("Fabric_BackQty");
                            fabric_BackCut = jobFabric.optDouble("Fabric_BackCut");
                            fabric_BajuQty = jobFabric.optInt("Fabric_BajuQty");
                            fabric_BajuCut = jobFabric.optDouble("Fabric_BajuCut");
                            fabric_ExtraQty = jobFabric.optInt("Fabric_ExtraQty");
                            fabric_ExtraCut = jobFabric.optDouble("Fabric_ExtraCut");
                            fabric_Colors = jobFabric.optString("Fabric_Colors");

                        }
                        ArrayList<SC3Object> fabcol=new ArrayList<>();
                        listArray.add(new CartItem(jObjItem.optString("CartId"),
                                jObjItem.optString("ProductId"), jObjItem.optString("ProductCode"),
                                jObjItem.optString("productName"), jObjItem.optString("BrandName"),
                                jObjItem.optString("MarketPrice"), jObjItem.optString("Price"),
                                jObjItem.optString("TotalFabricPrice"), jObjItem.optString("DiscountPercent"),
                                jObjItem.optString("ProductImage"), catQty,
                                jObjItem.optInt("MinOrderQuantity"),
                                jObjItem.optBoolean("IsOutOfStock"), false, type, bodyPart,
                                bodyFabricPart, fabricCuts
                                , fabric_FrontQty, fabric_FrontCut, fabric_BackQty
                                , fabric_BackCut, fabric_BajuQty, fabric_BajuCut
                                , fabric_ExtraQty, fabric_ExtraCut, fabcol
                        ));
                    }
                    setListAdapter(listArray);
                }
            }
            commonMethods.hidesoftKeyboard(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
