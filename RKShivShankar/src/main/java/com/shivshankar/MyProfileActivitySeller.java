package com.shivshankar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shivshankar.ServerCall.APIs;
import com.shivshankar.adapters.CountryAdapter;
import com.shivshankar.classes.SC3Object;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.Validation;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class MyProfileActivitySeller extends BaseActivitySeller implements OnClickListener, OnResult, View.OnFocusChangeListener {

    private TextView mBtn_logout;

    private TextView mBtn_save_profile;//, mBtn_add_mobile;
    private EditText mEdt_register_first_name, mEdt_register_email, mEdt_register_city, mEdt_register_company, mEdt_register_mobile_wholesaler;
    private EditText mSp_address_state_billing;
    private EditText mSp_country_billing;

    private TextView mBtn_change_password;
    public ImageView mIv_close;

    String strLoginId = "", strCountryCode = "", strStateCode = "";
    ArrayList<SC3Object> listCountry = new ArrayList<SC3Object>();
    ArrayList<SC3Object> listState = new ArrayList<SC3Object>();
    boolean calledFirstTime = true;

    /*
       <android.support.design.widget.TextInputLayout
                            android:id="@+id/inputLayout"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            app:errorEnabled="true">

     */
    public MyProfileActivitySeller() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            Window window = getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
            }
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
            View rootView = getLayoutInflater().inflate(R.layout.activity_my_profile_seller, frameLayout);
            bindViews(rootView);
            strLoginId = AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "");

            callGetCustomerProfileAPI(strLoginId);
            callCountryAPI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onResume() {
        try {
            if (!commonMethods.knowInternetOn(this)) {
                Snackbar snack = Snackbar.make(mBtn_save_profile, R.string.no_internet, Snackbar.LENGTH_LONG);
                View view = snack.getView();
                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.WHITE);
                snack.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }


    //    private void callClearCartAPI(String strDeviceUUID) {
//        Uri uri = new Uri.Builder().scheme("http")
//                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
//                .path("mobile/ClearCart")
//                .appendQueryParameter("sessionId", strDeviceUUID).build();
//        String query = uri.toString();
//        Log.v("TAG", "Calling With:" + query);
//        new ServerAPICAll(null, this).execute(query);
//
//    }
    private void callGetCountryStateAPI(String strLoginId, String strCountryCode) {
        Uri uri = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("mobile/GetCountryState")
                .appendQueryParameter("loginId", strLoginId)
                .appendQueryParameter("countryCode", strCountryCode)
                .build();
        String query = uri.toString();
        Log.v("TAG", "CAlling With:" + query);
//        new ServerAPICAll(this, this).execute(query);
        APIs.callAPI(null, this, query);
    }

    private void callCountryAPI() {
        Uri uri = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("mobile/GetCountryList")
                .build();
        String query = uri.toString();
        Log.v("TAG", "CAlling With:" + query);
//        new ServerAPICAll(null, this).execute(query);
        APIs.callAPI(null, this, query);
    }

    void callGetCustomerProfileAPI(String strLoginId) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("mobile/GetCustomerProfile")
                .appendQueryParameter("loginId", strLoginId).build();
        String query = uri.toString();
        Log.v("TAG", "Calling With:" + query);
//        new ServerAPICAll(this, this).execute(query);
        APIs.callAPI(null, this, query);
    }


    private void CallUpdateCustomerProfileAPI(String loginId, String fname, String lname, String email, String mobile, String city, String company, String State) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("mobile/UpdateCustomerProfile")
                .appendQueryParameter("loginId", loginId)
                .appendQueryParameter("FirstName", fname)
                .appendQueryParameter("LastName", lname)
                .appendQueryParameter("CompanyName", company)
                .appendQueryParameter("CityName", city)
                .appendQueryParameter("StateName", State)
                .appendQueryParameter("CountryCode", strCountryCode)
                .build();
        String query = uri.toString();
        Log.v("TAG", "Calling With:" + query);
//        new ServerAPICAll(this, this).execute(query);
        APIs.callAPI(null, this, query);
    }

    private void bindViews(View rootView) {

        mIv_close = (ImageView) rootView.findViewById(R.id.iv_close);
        mIv_close.setOnClickListener(this);

        mEdt_register_first_name = (EditText) rootView.findViewById(R.id.edt_register_first_name);
        mEdt_register_email = (EditText) rootView.findViewById(R.id.edt_register_email);
        mEdt_register_company = (EditText) rootView.findViewById(R.id.edt_register_company);
        mEdt_register_city = (EditText) rootView.findViewById(R.id.edt_register_city);
        mEdt_register_mobile_wholesaler = (EditText) rootView.findViewById(R.id.edt_otp);
        mSp_address_state_billing = (EditText) rootView.findViewById(R.id.edt_register_state);
        mSp_country_billing = (EditText) rootView.findViewById(R.id.edt_register_country);
//        mBtn_add_mobile = (TextView) rootView.findViewById(R.id.btn_add_mobile);
//        mBtn_add_mobile.setOnClickListener(this);

        mBtn_save_profile = (TextView) rootView.findViewById(R.id.btn_save_profile);
        mBtn_save_profile.setOnClickListener(this);
        mBtn_logout = (TextView) rootView.findViewById(R.id.btn_logout);
        mBtn_logout.setOnClickListener(this);
        setControlsAccess(true);
        mSp_country_billing.setOnFocusChangeListener(this);
        mSp_country_billing.setCursorVisible(false);
        mSp_country_billing.setOnClickListener(this);

        mBtn_change_password = (TextView) rootView.findViewById(R.id.btn_change_password);
        mBtn_change_password.setOnClickListener(this);

    }

    public void returnBack() {

        try {
            Intent output = new Intent();
            setResult(RESULT_OK, output);
            finish();
            overridePendingTransition(0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == commonVariables.REQUEST_ADD_MOBILE && resultCode == RESULT_OK && data != null) {
                if (data != null) {
                    boolean isAddressChanged = data.getExtras().getBoolean(commonVariables.KEY_MOBILE_CHANGED);
                    if (isAddressChanged) {
                        callGetCustomerProfileAPI(strLoginId);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {

        try {
            if (v == mIv_close) {
                returnBack();
            } else if (v == mBtn_save_profile) {
                updateProfile();
            }
//            else if (v == mBtn_add_mobile) {
//                Intent intent = new Intent(this, AddMobileNoActivityBuyer.class);
//                startActivityForResult(intent, commonVariables.REQUEST_ADD_MOBILE);
//                overridePendingTransition(0, 0);
//            }
            else if (v == mBtn_logout) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle(commonVariables.appname);
                builder.setMessage("Do you want to logout ?");
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        commonMethods.logout(MyProfileActivitySeller.this);
//                        callClearCartAPI(strDeviceUUID);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();

            } else if (v == mBtn_change_password) {
                Intent intent = new Intent(this, ChangePasswordActivitySeller.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            } else if (v == mSp_country_billing)
                showCountryDialog(mSp_country_billing);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    private void updateProfile() {
        try {
            String name = mEdt_register_first_name.getText().toString().trim();
            String fname = name;
            String lname = "";
            boolean fullNamerequiredError = false;
            if (name.contains(" ")) {
                try {
                    fullNamerequiredError = false;
                    fname = name.split(" ")[0];
                    lname = name.split(" ")[1];
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                fullNamerequiredError = true;
            String email = mEdt_register_email.getText().toString().trim();
            String mobile = mEdt_register_mobile_wholesaler.getText().toString().trim();
            String city = mEdt_register_city.getText().toString().trim();
            String company = mEdt_register_company.getText().toString().trim();
            String state = mSp_address_state_billing.getText().toString().trim();
            String strCountryB = mSp_country_billing.getText().toString().trim();
            mEdt_register_first_name.setError(null);
            mEdt_register_email.setError(null);
            mEdt_register_mobile_wholesaler.setError(null);
            mEdt_register_city.setError(null);
            mEdt_register_company.setError(null);

            if (Validation.isEmptyEdittext(mEdt_register_first_name)) {
                mEdt_register_first_name.setError("First name is required.");
                mEdt_register_first_name.requestFocus();
            } else if (fullNamerequiredError) {
                mEdt_register_first_name.setError("Enter full name.");
                mEdt_register_first_name.requestFocus();
            } else if (city.isEmpty()) {
                mEdt_register_city.setError("City required");
                mEdt_register_city.requestFocus();
            } else if (strCountryB.isEmpty()) {
                mSp_country_billing.setError("Country required");
                mSp_country_billing.requestFocus();
            } else if (state.isEmpty()) {
                mSp_address_state_billing.setError("State required");
                mSp_address_state_billing.requestFocus();
            } else if (commonMethods.knowInternetOn(this)) {
                CallUpdateCustomerProfileAPI(strLoginId, fname, lname, email, mobile, city, company, state);
            } else {
                commonMethods.showInternetAlert(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setControlsAccess(boolean b) {
        try {
            mEdt_register_first_name.setEnabled(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onResult(JSONObject jObWhole) {

        try {
            if (jObWhole != null) {
                String strAPIName = jObWhole.optString("api");
                if (strAPIName.equalsIgnoreCase("GetCountryList")) {
                    JSONArray jarray = jObWhole.optJSONArray("resData");
                    int length = jarray.length();
                    if (length == 0) {
                    } else {
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jo = jarray.optJSONObject(i);
                            listCountry.add(new SC3Object(i, jo.optString("Name"), jo.optString("Id"), ""));
                        }
                    }
                    if (!strCountryCode.isEmpty()) {
                        mSp_country_billing.setText(listCountry.get(getIndexOf(listCountry, strCountryCode)).getName());
                        callGetCountryStateAPI(strLoginId, strCountryCode);
                    }
                } else if (strAPIName.equalsIgnoreCase("GetCountryState")) {
                    listState.clear();
                    JSONArray jarray = jObWhole.optJSONArray("resData");
                    int length = jarray.length();
                    if (length == 0) {
                        strStateCode = "";
                        mSp_address_state_billing.setOnFocusChangeListener(null);
                        mSp_address_state_billing.setCursorVisible(true);
                        mSp_address_state_billing.setOnClickListener(null);
                    } else {
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jo = jarray.optJSONObject(i);
                            listState.add(new SC3Object(i, jo.optString("Name"), jo.optString("Id"), ""));
                        }
                        mSp_address_state_billing.setOnFocusChangeListener(this);
                        mSp_address_state_billing.setCursorVisible(false);
                        mSp_address_state_billing.setOnClickListener(this);
                    }
                    if (!strStateCode.isEmpty()) {
                        mSp_address_state_billing.setText(listState.get(getIndexOf(listState, strStateCode)).getName());
                    }
                    if (calledFirstTime) {
                        try {
                            strStateCode = getStateIdFromName(listState, mSp_address_state_billing.getText().toString().trim());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    calledFirstTime = false;
                } else if (strAPIName.equalsIgnoreCase("GetCustomerProfile")) {
                    JSONObject JOb = jObWhole.optJSONObject("resData");

                    String strFName = JOb.optString("FirstName"), strLName = JOb.optString("LastName"), strMobile = JOb.optString("MobileNo"), strEmail = JOb.optString("EmailId"), strCity = JOb.optString("CityName"), strCompanyName = JOb.optString("CompanyName"), strState = JOb.optString("StateName");
                    if (!strFName.equalsIgnoreCase("null")) {
                        mEdt_register_first_name.setText(strFName);
                        mEdt_register_first_name.setText(strFName + " " + strLName);
                    }
                    SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
                    editor.putString(commonVariables.KEY_LOGIN_USERNAME, strFName + " " + strLName);
                    editor.apply();

                    if (!strEmail.equalsIgnoreCase("null"))
                        mEdt_register_email.setText(strEmail);
                    if (!strMobile.equalsIgnoreCase("null"))
                        mEdt_register_mobile_wholesaler.setText(strMobile);
                    if (!strCity.equalsIgnoreCase("null"))
                        mEdt_register_city.setText(strCity);
                    if (!strCompanyName.equalsIgnoreCase("null"))
                        mEdt_register_company.setText(strCompanyName);
                    if (!strState.equalsIgnoreCase("null"))
                        mSp_address_state_billing.setText(strState);
                    strCountryCode = JOb.optString("CountryCode");
                    if (strCountryCode == null)
                        strCountryCode = "";

                } else if (strAPIName.equalsIgnoreCase("UpdateCustomerProfile")) {
                    int strresId = jObWhole.optInt("resId");

                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setTitle(commonVariables.appname);
                    builder.setMessage(jObWhole.optString("res"));
                    if (strresId == 1) {
                        try {
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onBackPressed();
                                    overridePendingTransition(0, 0);
                                }
                            });
                            String strFName = mEdt_register_first_name.getText().toString().trim();
//                            RightNavigationDrawerFragment.mTv_username.setText(strFName);
                            SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
                            editor.putString(commonVariables.KEY_LOGIN_USERNAME, strFName);
                            editor.apply();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        builder.setPositiveButton("Ok", null);
                    }
                    builder.show();
                }
            }
            commonMethods.hidesoftKeyboard(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getStateIdFromName(ArrayList<SC3Object> listState, String str) {
        String strCode = "";
        for (int i = 0; i < listState.size(); i++) {
            if (listState.get(i).getName().equalsIgnoreCase(str)) {
                strCode = listState.get(i).getIFSCCode();
            }
        }
        return strCode;
    }

    private int getIndexOf(ArrayList<SC3Object> listCountry, String strCountryCode) {
        int ccode = 254;
        for (int i = 0; i < listCountry.size(); i++) {
            if (listCountry.get(i).getIFSCCode().equalsIgnoreCase(strCountryCode)) {
                ccode = i;
            }
        }
        return ccode;
    }

    @Override
    public void onFocusChange(View arg0, boolean arg1) {

        if (arg1 == true) {
            if (arg0 == mSp_country_billing)
                showCountryDialog((EditText) arg0);
            else if (arg0 == mSp_address_state_billing)
                showStateDialog((EditText) arg0);
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void showStateDialog(final EditText mEdt_state) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        EditText inputSearch = (EditText) dialog.findViewById(R.id.inputSearch);
        ListView list = (ListView) dialog.findViewById(R.id.list_view);
        dialog.setTitle("Select State");
        dialog.setCancelable(true);
        final CountryAdapter state_adapter = new CountryAdapter(this, listState);
        list.setAdapter(state_adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                try {
                    SC3Object country = (SC3Object) parent.getItemAtPosition(position);
                    mEdt_state.setError(null);
                    mEdt_state.setText(country.getName());
                    strStateCode = country.getIFSCCode();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                commonMethods.hidesoftKeyboard(MyProfileActivitySeller.this);
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

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void showCountryDialog(final EditText mEdt_country) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
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
                    mEdt_country.setError(null);
                    mEdt_country.setText(country.getName());
                    strCountryCode = country.getIFSCCode();
                    mSp_address_state_billing.setText("");

                    callGetCountryStateAPI(strLoginId, strCountryCode);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                commonMethods.hidesoftKeyboard(MyProfileActivitySeller.this);
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
}
