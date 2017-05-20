package com.shivshankar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.adapters.CountryAdapter;
import com.shivshankar.adapters.PaymentOptionExpandableListAdapter;
import com.shivshankar.classes.Payment;
import com.shivshankar.classes.SC3Object;
import com.shivshankar.utills.AlertDialogManager;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.Validation;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by  Mayura on 4/1/2017.
 */

public class OrderFormActivityBuyer extends BaseActivityCartBuyer implements View.OnClickListener, OnResult, CompoundButton.OnCheckedChangeListener, View.OnFocusChangeListener {

    Resources res;
    boolean call = false;
    private LinearLayout mLl_order_summary, mLl_shipping, mLl_billing, mLl_confirm_order;

    private TextView mTv_subtotal, mTv_shipping, mTv_grand_total;
    private EditText mEdt_full_name_shipping, mEdt_mobile_shipping, mEdt_address1_shipping, mEdt_address2_shipping, mEdt_address_city_shipping, mEdt_address_state_shipping, mSp_country_shipping, mEdt_pin_code_shipping;
    private TextInputLayout mTi_full_name_shipping, mTi_mobile_shipping, mTi_address1_shipping, mTi_address2_shipping, mTi_address_city_shipping, mTi_address_state_shipping, mTi_country_shipping, mTi_pin_code_shipping;

    private CheckBox mCB_Different_Address;
    private EditText mEdt_full_name_billing, mEdt_mobile_billing, mEdt_address1_billing, mEdt_address2_billing, mEdt_address_city_billing, mEdt_address_state_billing, mSp_country_billing, mEdt_pin_code_billing, mEdt_note;
    private TextInputLayout mTi_full_name_billing, mTi_mobile_billing, mTi_address1_billing, mTi_address2_billing, mTi_address_city_billing, mTi_address_state_billing, mTi_country_billing, mTi_pin_code_billing;
    private RecyclerView mRv_payment;

    String strDeviceUUID = commonVariables.uuid;
    String bstrCountryCode = "";
    String sstrCountryCode = "";
    public String strPaymentCode = "";
    ArrayList<SC3Object> listCountry = new ArrayList<SC3Object>();
    private ArrayList<Payment> paymentList;
    private PaymentOptionExpandableListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            res = getResources();
            View rootView = getLayoutInflater().inflate(R.layout.activity_order_form_buyer, frameLayout);
            //rootView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
            bindViews(rootView);

            APIs.GetPaymentOptions(this, this);
            if (listCountry.size() == 0)
                APIs.GetCountryList(null, this);
            try {
                String strProfile = AppPreferences.getPrefs().getString(commonVariables.KEY_BUYER_PROFILE, "");
                if (!strProfile.isEmpty())
                    setProfileShipping(new JSONObject(strProfile));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (commonMethods.knowInternetOn(this)) {
                Log.e("TAGRK", "ReId:!" + FirebaseInstanceId.getInstance().getToken());
            } else {
                Log.d("TAGRK", "Internet Problem!");
            }

            if (AppPreferences.getPrefs().getInt(commonVariables.CART_COUNT, 0) != 0) {
                APIs.GetOrderSummary_Suit(null, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setProfileShipping(JSONObject JOb) {
        try {
            String strFName = JOb.optString("Name"), strMobile = JOb.optString("MobileNo"), strEmail = JOb.optString("EmailId"), strCity = JOb.optString("City"), strPincode = JOb.optString("PinCode"), strState = JOb.optString("State");
            if (!strFName.equalsIgnoreCase("null")) {
                mEdt_full_name_shipping.setText(strFName);
            }

            if (!strMobile.equalsIgnoreCase("null"))
                mEdt_mobile_shipping.setText(strMobile);
            if (!strCity.equalsIgnoreCase("null"))
                mEdt_address_city_shipping.setText(strCity);
            if (!strPincode.equalsIgnoreCase("null"))
                mEdt_pin_code_shipping.setText(strPincode);
            if (!strState.equalsIgnoreCase("null"))
                mEdt_address_state_shipping.setText(strState);

            String strAdd = AppPreferences.getPrefs().getString(commonVariables.ADDRESS_SHIPPING, "");
            if (!strState.equalsIgnoreCase("null"))
                mEdt_address1_shipping.setText(strAdd);

            sstrCountryCode = JOb.optString("CountryCode");
            if (sstrCountryCode == null)
                sstrCountryCode = "";
            if (!sstrCountryCode.isEmpty())
                mSp_country_shipping.setText(listCountry.get(commonMethods.getIndexOf(listCountry, sstrCountryCode)).getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFocusChange(View arg0, boolean arg1) {

        if (arg1 == true) {
            if (arg0 == mSp_country_billing || arg0 == mSp_country_shipping)
                showCountryDialog((EditText) arg0);
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void showCountryDialog(final EditText mEdt_country) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_custom_bank);
        EditText inputSearch = (EditText) dialog.findViewById(R.id.inputSearch);
        ListView list = (ListView) dialog.findViewById(R.id.list_view);
        dialog.setTitle("Select Country");
        dialog.setCancelable(true);
        final CountryAdapter state_adapter = new CountryAdapter(this, listCountry);
        list.setAdapter(state_adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                try {
                    SC3Object country = (SC3Object) parent.getItemAtPosition(position);

                    mEdt_country.setText(country.getName());
                    if (mEdt_country == mSp_country_billing) {
                        mTi_country_billing.setError(null);
                        bstrCountryCode = country.getIFSCCode();
                    } else {
                        mTi_country_shipping.setError(null);
                        sstrCountryCode = country.getIFSCCode();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                commonMethods.hidesoftKeyboard(OrderFormActivityBuyer.this);
                dialog.cancel();
            }
        });
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                state_adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindViews(View rootView) {

        try {

            mLl_order_summary = (LinearLayout) rootView.findViewById(R.id.ll_order_summary);
            mTv_subtotal = (TextView) rootView.findViewById(R.id.tv_subtotal);
            mLl_shipping = (LinearLayout) rootView.findViewById(R.id.ll_shipping);
            mTv_shipping = (TextView) rootView.findViewById(R.id.tv_shipping);
            mTv_grand_total = (TextView) rootView.findViewById(R.id.tv_grand_total);
            mEdt_full_name_shipping = (EditText) rootView.findViewById(R.id.edt_full_name_shipping);
            mEdt_mobile_shipping = (EditText) rootView.findViewById(R.id.edt_mobile_shipping);
            mEdt_address1_shipping = (EditText) rootView.findViewById(R.id.edt_address1_shipping);
            mEdt_address2_shipping = (EditText) rootView.findViewById(R.id.edt_address2_shipping);
            mEdt_address_city_shipping = (EditText) rootView.findViewById(R.id.edt_address_city_shipping);
            mEdt_address_state_shipping = (EditText) rootView.findViewById(R.id.edt_address_state_shipping);
            mSp_country_shipping = (EditText) rootView.findViewById(R.id.sp_country_shipping);
            mRv_payment = (RecyclerView) rootView.findViewById(R.id.rv_payment);
            mSp_country_shipping.setOnFocusChangeListener(this);
            mSp_country_shipping.setCursorVisible(false);
            mSp_country_shipping.setOnClickListener(this);

            mEdt_pin_code_shipping = (EditText) rootView.findViewById(R.id.edt_pin_code_shipping);


            mTi_full_name_shipping = (TextInputLayout) rootView.findViewById(R.id.ti_full_name_shipping);
            mTi_mobile_shipping = (TextInputLayout) rootView.findViewById(R.id.ti_mobile_shipping);
            mTi_address1_shipping = (TextInputLayout) rootView.findViewById(R.id.ti_address1_shipping);
            mTi_address2_shipping = (TextInputLayout) rootView.findViewById(R.id.ti_address2_shipping);
            mTi_address_city_shipping = (TextInputLayout) rootView.findViewById(R.id.ti_address_city_shipping);
            mTi_address_state_shipping = (TextInputLayout) rootView.findViewById(R.id.ti_address_state_shipping);
            mTi_country_shipping = (TextInputLayout) rootView.findViewById(R.id.ti_country_shipping);
            mTi_pin_code_shipping = (TextInputLayout) rootView.findViewById(R.id.ti_pin_code_shipping);

            mCB_Different_Address = (CheckBox) rootView.findViewById(R.id.cb_different_Address);

            mLl_billing = (LinearLayout) rootView.findViewById(R.id.ll_billing);
            mEdt_full_name_billing = (EditText) rootView.findViewById(R.id.edt_full_name_billing);
            mEdt_mobile_billing = (EditText) rootView.findViewById(R.id.edt_mobile_billing);
            mEdt_address1_billing = (EditText) rootView.findViewById(R.id.edt_address1_billing);
            mEdt_address2_billing = (EditText) rootView.findViewById(R.id.edt_address2_billing);
            mEdt_address_city_billing = (EditText) rootView.findViewById(R.id.edt_address_city_billing);
            mEdt_address_state_billing = (EditText) rootView.findViewById(R.id.edt_address_state_billing);
            mSp_country_billing = (EditText) rootView.findViewById(R.id.sp_country_billing);
            mSp_country_billing.setOnFocusChangeListener(this);
            mSp_country_billing.setCursorVisible(false);
            mSp_country_billing.setOnClickListener(this);
            mEdt_pin_code_billing = (EditText) rootView.findViewById(R.id.edt_pin_code_billing);

            mTi_full_name_billing = (TextInputLayout) rootView.findViewById(R.id.ti_full_name_billing);
            mTi_mobile_billing = (TextInputLayout) rootView.findViewById(R.id.ti_mobile_billing);
            mTi_address1_billing = (TextInputLayout) rootView.findViewById(R.id.ti_address1_billing);
            mTi_address2_billing = (TextInputLayout) rootView.findViewById(R.id.ti_address2_billing);
            mTi_address_city_billing = (TextInputLayout) rootView.findViewById(R.id.ti_address_city_billing);
            mTi_address_state_billing = (TextInputLayout) rootView.findViewById(R.id.ti_address_state_billing);
            mTi_country_billing = (TextInputLayout) rootView.findViewById(R.id.ti_country_billing);
            mTi_pin_code_billing = (TextInputLayout) rootView.findViewById(R.id.ti_pin_code_billing);

            mEdt_note = (EditText) rootView.findViewById(R.id.edt_note);
            mLl_confirm_order = (LinearLayout) rootView.findViewById(R.id.ll_confirm_order);

            mCB_Different_Address.setOnCheckedChangeListener(this);

            mLl_order_summary = (LinearLayout) rootView.findViewById(R.id.ll_order_summary);
            mTv_subtotal = (TextView) rootView.findViewById(R.id.tv_subtotal);
            mTv_shipping = (TextView) rootView.findViewById(R.id.tv_shipping);
            mTv_grand_total = (TextView) rootView.findViewById(R.id.tv_grand_total);
            mLl_confirm_order = (LinearLayout) rootView.findViewById(R.id.ll_confirm_order);
            mLl_confirm_order.setOnClickListener(this);
            mLl_shipping = (LinearLayout) rootView.findViewById(R.id.ll_shipping);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, 0);
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {
        try {
            AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            view.startAnimation(buttonClick);
            if (view == mIv_logo_toolbar) {
                Intent intent = new Intent(this, MainActivityBuyer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
            } else if (view == mSp_country_billing)
                showCountryDialog(mSp_country_billing);
            else if (view == mSp_country_shipping)
                showCountryDialog(mSp_country_shipping);
            else if (view == mLl_confirm_order) {
                call = false;
                String sstrFname = mEdt_full_name_shipping.getText().toString();
                String sstrMobile = mEdt_mobile_shipping.getText().toString();
                String sstrAddress1 = mEdt_address1_shipping.getText().toString();
                String sstrAddress2 = mEdt_address2_shipping.getText().toString();
                String sstrCity = mEdt_address_city_shipping.getText().toString();
                String sstrState = mEdt_address_state_shipping.getText().toString();
                String sstrPinCode = mEdt_pin_code_shipping.getText().toString();

                AppPreferences.getPrefs().edit().putString(commonVariables.ADDRESS_SHIPPING, sstrAddress1).apply();

                if (Validation.isEmptyEdittext(mEdt_full_name_shipping)) {
                    mTi_full_name_shipping.setError("Name required");
                    mEdt_full_name_shipping.requestFocus();
                } else if (Validation.isEmptyEdittext(mEdt_mobile_shipping)) {
                    mTi_mobile_shipping.setError("Mobile no required");
                    mEdt_mobile_shipping.requestFocus();
                } else if (sstrMobile.length() < 10 || sstrMobile.length() > 13) {
                    mTi_mobile_shipping.setError("Mobile no is not valid");
                    mEdt_mobile_shipping.requestFocus();
                } else if (Validation.isEmptyEdittext(mEdt_address1_shipping)) {
                    mTi_address1_shipping.setError("Address required");
                    mEdt_address1_shipping.requestFocus();
                } else if (Validation.isEmptyEdittext(mEdt_address_city_shipping)) {
                    mTi_address_city_shipping.setError("City required");
                    mEdt_address_city_shipping.requestFocus();
                } else if (Validation.isEmptyEdittext(mEdt_address_state_shipping)) {
                    mTi_address_state_shipping.setError("State required");
                    mEdt_address_state_shipping.requestFocus();
                } else if (Validation.isEmptyEdittext(mSp_country_shipping)) {
                    mTi_country_shipping.setError("Select Country");
                    mSp_country_shipping.requestFocus();
                } else if (Validation.isEmptyEdittext(mEdt_pin_code_shipping)) {
                    mTi_pin_code_shipping.setError("Pin Code required");
                    mEdt_pin_code_shipping.requestFocus();
                } else if (sstrPinCode.length() < 3 || sstrPinCode.length() > 10) {
                    mTi_pin_code_shipping.setError("Pincode is not valid");
                    mEdt_pin_code_shipping.requestFocus();
                } else {
                    String bstrFname = mEdt_full_name_billing.getText().toString();
                    String bstrMobile = mEdt_mobile_billing.getText().toString();
                    String bstrAddress1 = mEdt_address1_billing.getText().toString();
                    String bstrAddress2 = mEdt_address2_billing.getText().toString();
                    String bstrCity = mEdt_address_city_billing.getText().toString();
                    String bstrState = mEdt_address_state_billing.getText().toString();
                    String bstrPinCode = mEdt_pin_code_billing.getText().toString();
                    bstrCountryCode = sstrCountryCode;

                    if (mCB_Different_Address.isChecked()) {
                        if (Validation.isEmptyEdittext(mEdt_full_name_billing)) {
                            mTi_full_name_billing.setError("Name required");
                            mEdt_full_name_billing.requestFocus();
                        } else if (Validation.isEmptyEdittext(mEdt_mobile_billing)) {
                            mTi_mobile_billing.setError("Mobile no required");
                            mEdt_mobile_billing.requestFocus();
                        } else if (bstrMobile.length() < 10 || bstrMobile.length() > 13) {
                            mTi_mobile_billing.setError("Mobile no is not valid");
                            mEdt_mobile_billing.requestFocus();
                        } else if (Validation.isEmptyEdittext(mEdt_address1_billing)) {
                            mTi_address1_billing.setError("Address required");
                            mEdt_address1_billing.requestFocus();
                        } else if (Validation.isEmptyEdittext(mEdt_address_city_billing)) {
                            mTi_address_city_billing.setError("City required");
                            mEdt_address_city_billing.requestFocus();
                        } else if (Validation.isEmptyEdittext(mEdt_address_state_billing)) {
                            mTi_address_state_billing.setError("State required");
                            mEdt_address_state_billing.requestFocus();
                        } else if (Validation.isEmptyEdittext(mSp_country_billing)) {
                            mTi_country_billing.setError("Select Country");
                            mSp_country_billing.requestFocus();
                        } else if (Validation.isEmptyEdittext(mEdt_pin_code_billing)) {
                            mTi_pin_code_billing.setError("Pin Code required");
                            mEdt_pin_code_billing.requestFocus();
                        } else if (bstrPinCode.length() < 3 || bstrPinCode.length() > 10) {
                            mTi_pin_code_billing.setError("Pincode is not valid");
                            mEdt_pin_code_billing.requestFocus();
                        } else {
                            call = true;
                        }

                    } else {
                        call = true;
                        bstrFname = sstrFname;
                        bstrMobile = sstrMobile;
                        bstrAddress1 = sstrAddress1;
                        bstrAddress2 = sstrAddress2;
                        bstrCity = sstrCity;
                        bstrState = sstrState;
                        bstrPinCode = sstrPinCode;
                    }

                    if (strPaymentCode.isEmpty()) {
                        call = false;
                        AlertDialogManager.showDialog(this, "Please select payment options", null);
                    }

                    if (call) {
                        String strModelName = Build.MODEL;
                        String strOSVersion = Build.VERSION.RELEASE;
                        String strDeviceType = "Android";

                        APIs.AddUpdateOrder_Suit(this, this, "", strDeviceType, strDeviceUUID, strModelName, strOSVersion,
                                bstrFname, bstrAddress1, bstrAddress2, bstrPinCode, bstrCity, bstrState, bstrCity, bstrCountryCode, bstrMobile,
                                sstrFname, sstrAddress1, sstrAddress2, sstrPinCode, sstrCity, sstrState, sstrCity, sstrCountryCode, sstrMobile, mEdt_note.getText().toString().trim(), strPaymentCode);
                    }
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
                if (strApiName.equalsIgnoreCase("GetCountryList")) {
                    JSONArray jarray = jobjWhole.optJSONArray("resData");
                    int length = jarray.length();
                    if (length == 0) {
                    } else {
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jo = jarray.optJSONObject(i);
                            listCountry.add(new SC3Object(i, jo.optString("Name"), jo.optString("Id"), ""));
                        }
                    }
                    if (!bstrCountryCode.isEmpty()) {
                        mSp_country_billing.setText(listCountry.get(commonMethods.getIndexOf(listCountry, bstrCountryCode)).getName());
                    }
                    if (!sstrCountryCode.isEmpty()) {
                        mSp_country_shipping.setText(listCountry.get(commonMethods.getIndexOf(listCountry, sstrCountryCode)).getName());
                    }
                } else if (strApiName.equalsIgnoreCase("GetPaymentOptions")) {
                    JSONArray jarray = jobjWhole.optJSONArray("resData");
                    paymentList = new ArrayList<Payment>();
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject c = jarray.optJSONObject(i);
                        Payment item = new Payment(c.optString("PaymentOptionKey"), c.optString("PaymentOptionName"), c.optString("PaymentOptionDetails"), false);
                        paymentList.add(item);
                    }
                    setPaymentOptionData(paymentList);


                } else if (strApiName.equalsIgnoreCase("GetOrderSummary_Suit")) {
                    mLl_order_summary.setVisibility(View.VISIBLE);
                    JSONObject jo = jobjWhole.optJSONObject("resData");
                    mTv_grand_total.setText(commonVariables.strCurrency_name + " " + jo.optString("TotalAmount"));
                    if (Integer.parseInt(jo.optString("ShippingCharge")) != 0) {
                        mLl_shipping.setVisibility(View.VISIBLE);
                        mTv_shipping.setText(commonVariables.strCurrency_name + " " + jo.optString("ShippingCharge"));
                    } else
                        mLl_shipping.setVisibility(View.GONE);
                    mTv_subtotal.setText(commonVariables.strCurrency_name + " " + jo.optString("SubTotal"));
                } else if (strApiName.equalsIgnoreCase("AddUpdateOrder_Suit")) {
                    int strresId = jobjWhole.optInt("resInt");
                    JSONObject job = jobjWhole.optJSONObject("resData");
                    if (strresId == 1) {
                        try {
                            AppPreferences.getPrefs().edit().putInt(commonVariables.CART_COUNT, 0).apply();

                            Intent intent = new Intent(this, OrderSuccessActivityBuyer.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra(commonVariables.KEY_ORDER, jobjWhole.optString("newCustOrderId"));
                            startActivity(intent);
                            finish();
                            overridePendingTransition(0, 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        AlertDialogManager.showDialog(this, jobjWhole.optString("res"), null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPaymentOptionData(ArrayList<Payment> paymentList) {
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        llm.setAutoMeasureEnabled(true);
        mRv_payment.setLayoutManager(llm);
        //mRv_payment.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter = new PaymentOptionExpandableListAdapter(this, paymentList);
        mRv_payment.setAdapter(adapter);
//        mRv_payment.getItemAnimator().setChangeDuration(0);
//        mRv_payment.setHasFixedSize(true);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        call = false;
        if (isChecked) {
            mLl_billing.setVisibility(View.VISIBLE);
        } else {
            mLl_billing.setVisibility(View.GONE);
        }
    }
}

