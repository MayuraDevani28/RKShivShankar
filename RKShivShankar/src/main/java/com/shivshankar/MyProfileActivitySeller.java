package com.shivshankar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shivshankar.ServerCall.APIs;
import com.shivshankar.adapters.CountryAdapter;
import com.shivshankar.classes.SC3Object;
import com.shivshankar.utills.AlertDialogManager;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.Validation;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class MyProfileActivitySeller extends BaseActivitySeller implements OnClickListener, OnResult, View.OnFocusChangeListener {

    private TextView mBtn_logout, mBtn_save_profile, mBtn_change_password;//, mBtn_add_mobile;


    private EditText mEdt_register_first_name, mEdt_register_email, mEdt_register_city, mEdt_register_mobile_wholesaler, mEdt_pincode, mSp_country_billing, mSp_address_state_billing;//mEdt_register_company
    private TextInputLayout mTI_register_first_name, mTI_register_email, mTI_register_city, mTI_register_mobile_wholesaler, mTI_pincode, mTI_country_billing, mTI_address_state_billing;

    public ImageView mIv_close;

    String strCountryCode = "";
    ArrayList<SC3Object> listCountry = new ArrayList<SC3Object>();

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
            }
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
            View rootView = getLayoutInflater().inflate(R.layout.activity_my_profile_seller, frameLayout);
            rootView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
            bindViews(rootView);

            try {
                String strProfile = AppPreferences.getPrefs().getString(commonVariables.KEY_SELLER_PROFILE, "");
                if (!strProfile.isEmpty())
                    setProfile(new JSONObject(strProfile));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            APIs.GetCountryList(this, this);
            APIs.GetSellerProfile(this, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        finish();
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
            if (mTv_username != null) {
                String strProfile = AppPreferences.getPrefs().getString(commonVariables.KEY_SELLER_PROFILE, "");
                if (!strProfile.isEmpty() && !strProfile.equalsIgnoreCase("null"))
                    mTv_username.setText(WordUtils.capitalizeFully(new JSONObject(strProfile).optString("Name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    private void bindViews(View rootView) {

        mIv_logo_nav.setOnClickListener(this);
        mIv_logo_toolbar.setOnClickListener(this);
        mTv_username.setOnClickListener(this);
        mTv_logout.setOnClickListener(this);
        mLl_close.setOnClickListener(this);

        mNav_my_profile.setOnClickListener(this);
        mNav_my_products.setOnClickListener(this);
        mNav_notification.setOnClickListener(this);
        mNav_my_orders.setOnClickListener(this);
        mNav_change_pass.setOnClickListener(this);

        mIv_close = (ImageView) rootView.findViewById(R.id.iv_close);
        mIv_close.setOnClickListener(this);

        mEdt_register_first_name = (EditText) rootView.findViewById(R.id.edt_register_first_name);
        mEdt_register_email = (EditText) rootView.findViewById(R.id.edt_register_email);
        mEdt_register_city = (EditText) rootView.findViewById(R.id.edt_register_city);
        mEdt_pincode = (EditText) rootView.findViewById(R.id.edt_pincode);
        mSp_address_state_billing = (EditText) rootView.findViewById(R.id.edt_register_state);
        mEdt_register_mobile_wholesaler = (EditText) rootView.findViewById(R.id.edt_otp);
        mSp_country_billing = (EditText) rootView.findViewById(R.id.edt_register_country);

        mTI_register_first_name = (TextInputLayout) rootView.findViewById(R.id.ti_register_first_name);
        mTI_register_email = (TextInputLayout) rootView.findViewById(R.id.ti_register_email);
        mTI_register_city = (TextInputLayout) rootView.findViewById(R.id.ti_register_city);
        mTI_pincode = (TextInputLayout) rootView.findViewById(R.id.ti_pincode);
        mTI_address_state_billing = (TextInputLayout) rootView.findViewById(R.id.ti_register_state);
        mTI_register_mobile_wholesaler = (TextInputLayout) rootView.findViewById(R.id.ti_otp);
        mTI_country_billing = (TextInputLayout) rootView.findViewById(R.id.ti_register_country);

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
                        APIs.GetSellerProfile(this, this);
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
    public void onClick(View view) {
        try {
            AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            view.startAnimation(buttonClick);
            if (view == mIv_logo_toolbar) {
                Intent intent = new Intent(this, MainActivitySeller.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
            } else if (view == mNav_my_profile) {
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, MyProfileActivitySeller.class));
                overridePendingTransition(0, 0);
            } else if (view == mNav_my_products) {
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, ProductsActivitySeller.class));
                overridePendingTransition(0, 0);
            } else if (view == mNav_notification) {
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, NotificationsActivitySeller.class));
                overridePendingTransition(0, 0);
            } else if (view == mNav_change_pass) {
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, ChangePasswordActivitySeller.class));
                overridePendingTransition(0, 0);
            } else if (view == mNav_my_orders) {
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, MyOrdersActivitySeller.class));
                overridePendingTransition(0, 0);
            } else if (view == mLl_close || view == mIv_logo_nav || view == mTv_username) {
                drawer.closeDrawer(GravityCompat.START);
            } else if (view == mTv_logout) {
                drawer.closeDrawer(GravityCompat.START);
                commonMethods.logout(this, true);
            } else if (view == mIv_close) {
                returnBack();
            } else if (view == mBtn_save_profile) {
                updateProfile();
            } else if (view == mBtn_logout) {
                commonMethods.logout(this, true);
            } else if (view == mBtn_change_password) {
                Intent intent = new Intent(this, ChangePasswordActivitySeller.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            } else if (view == mSp_country_billing)
                showCountryDialog(mSp_country_billing);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    private void updateProfile() {
        try {
            String name = mEdt_register_first_name.getText().toString().trim();
            boolean fullNamerequiredError = false;
            if (!name.contains(" "))
                fullNamerequiredError = true;
            String email = mEdt_register_email.getText().toString().trim();
            String mobile = mEdt_register_mobile_wholesaler.getText().toString().trim();
            String city = mEdt_register_city.getText().toString().trim();
            String strPincode = mEdt_pincode.getText().toString().trim();
            String state = mSp_address_state_billing.getText().toString().trim();
            String strCountryB = mSp_country_billing.getText().toString().trim();
            mTI_register_first_name.setError(null);
            mTI_register_email.setError(null);
            mTI_register_mobile_wholesaler.setError(null);
            mTI_register_city.setError(null);

            if (Validation.isEmptyEdittext(mEdt_register_first_name)) {
                mTI_register_first_name.setError("First name is required");
                mEdt_register_first_name.requestFocus();
            } else if (fullNamerequiredError) {
                mTI_register_first_name.setError("Enter full name");
                mEdt_register_first_name.requestFocus();
            } else if (city.isEmpty()) {
                mTI_register_city.setError("City required");
                mEdt_register_city.requestFocus();
            } else if (strPincode.isEmpty()) {
                mTI_pincode.setError("Pincode required");
                mEdt_pincode.requestFocus();
            } else if (strCountryB.isEmpty()) {
                mTI_country_billing.setError("Country required");
                mSp_country_billing.requestFocus();
            } else if (state.isEmpty()) {
                mTI_address_state_billing.setError("State required");
                mSp_address_state_billing.requestFocus();
            }  else if (commonMethods.knowInternetOn(this)) {
                APIs.UpdateSellerProfile(this, this, name, email, mobile, city, strPincode, state, strCountryCode);
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
                        mSp_country_billing.setText(listCountry.get(commonMethods.getIndexOf(listCountry, strCountryCode)).getName());
                    }
                } else if (strAPIName.equalsIgnoreCase("GetSellerProfile")) {
                    JSONObject JOb = jObWhole.optJSONObject("resData");

                    setProfile(JOb);
                    SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
                    editor.putString(commonVariables.KEY_SELLER_PROFILE, JOb.toString());
                    editor.apply();

                } else if (strAPIName.equalsIgnoreCase("UpdateSellerProfile")) {
                    int strresId = jObWhole.optInt("resInt");

                    Runnable listener = null;
                    if (strresId == 1) {
                        try {
                            SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
                            editor.putString(commonVariables.KEY_SELLER_PROFILE, jObWhole.optString("resData"));
                            editor.apply();

                            listener = () -> {
                                onBackPressed();
                                overridePendingTransition(0, 0);
                            };
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    AlertDialogManager.showDialog(this, jObWhole.optString("res"), listener);
                }
            }
            commonMethods.hidesoftKeyboard(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setProfile(JSONObject JOb) {
        try {
            String strFName = JOb.optString("Name"), strMobile = JOb.optString("MobileNo"), strEmail = JOb.optString("EmailId"), strCity = JOb.optString("City"), strPincode = JOb.optString("PinCode"), strState = JOb.optString("State");
            if (!strFName.equalsIgnoreCase("null")) {
                mEdt_register_first_name.setText(strFName);
            }

            if (!strEmail.equalsIgnoreCase("null"))
                mEdt_register_email.setText(strEmail);
            if (!strMobile.equalsIgnoreCase("null"))
                mEdt_register_mobile_wholesaler.setText(strMobile);
            if (!strCity.equalsIgnoreCase("null"))
                mEdt_register_city.setText(strCity);
            if (!strPincode.equalsIgnoreCase("null"))
                mEdt_pincode.setText(strPincode);
            if (!strState.equalsIgnoreCase("null"))
                mSp_address_state_billing.setText(strState);
            strCountryCode = JOb.optString("CountryCode");
            if (strCountryCode == null)
                strCountryCode = "";
            if (!strCountryCode.isEmpty())
                mSp_country_billing.setText(listCountry.get(commonMethods.getIndexOf(listCountry, strCountryCode)).getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFocusChange(View arg0, boolean arg1) {

        if (arg1 == true) {
            if (arg0 == mSp_country_billing)
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
                    mTI_country_billing.setError(null);
                    mEdt_country.setText(country.getName());
                    strCountryCode = country.getIFSCCode();

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
