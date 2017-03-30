package com.shivshankar;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shivshankar.ServerCall.APIs;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.Validation;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.json.JSONObject;


public class AddMobileNoActivityBuyer extends BaseActivityBuyer implements View.OnClickListener, OnResult {
    private EditText mEdt_register_mobile_wholesaler;
    private Button mBtn_submit;
    public ImageView mIv_close;
    TextView mBtn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            Window window = getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
            View rootView = getLayoutInflater().inflate(R.layout.activity_add_mobile_seller, frameLayout);
            bindViews(rootView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, 0);
    }

    private void bindViews(View rootView) {
        mEdt_register_mobile_wholesaler = (EditText) rootView.findViewById(R.id.edt_otp);
        mBtn_submit = (Button) rootView.findViewById(R.id.btn_submit);
        mBtn_submit.setOnClickListener(this);
        mIv_close = (ImageView) rootView.findViewById(R.id.iv_close);
        mIv_close.setOnClickListener(this);
        mBtn_cancel = (TextView) rootView.findViewById(R.id.btn_cancel_receiver);
        mBtn_cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        try {
            AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            view.startAnimation(buttonClick);
            if (view == mIv_close) {
                finish();
                overridePendingTransition(0, 0);
            } else if (view == mBtn_submit) {
                String mobile = mEdt_register_mobile_wholesaler.getText().toString().trim();
                mEdt_register_mobile_wholesaler.setError(null);

                if (Validation.isEmptyEdittext(mEdt_register_mobile_wholesaler)) {
                    mEdt_register_mobile_wholesaler.setError("Mobile is required.");
                    mEdt_register_mobile_wholesaler.requestFocus();
                } else if (mobile.length() < 6 || mobile.length() > 13) {
                    mEdt_register_mobile_wholesaler.setError("Enter valid mobile number.");
                    mEdt_register_mobile_wholesaler.requestFocus();
                } else
                    callCheckMobileNoForRegistrationAPI(mobile);
            } else if (view == mBtn_cancel) {
                returnBack(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callCheckMobileNoForRegistrationAPI(String strMob) {
        Uri uri = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/CheckMobileNoForRegistration")
                .appendQueryParameter("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, ""))
                .appendQueryParameter("newMobileNo", strMob)
                .build();
        String query = uri.toString();
        APIs.callAPI(null, this, query);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == commonVariables.REQUEST_ADD_MOBILE && resultCode == RESULT_OK && data != null) {
                if (data != null) {
                    boolean isAddressChanged = data.getExtras().getBoolean(commonVariables.KEY_MOBILE_CHANGED);
                    if (isAddressChanged) {
                        returnBack(true);
                    } else
                        returnBack(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResult(JSONObject jObjWhole) {
        try {
            if (jObjWhole != null) {
                String result = jObjWhole.optString("res");
                String strApiName = jObjWhole.optString("api");
                int resId = jObjWhole.optInt("resInt");
                if (strApiName.equalsIgnoreCase("CheckMobileNoForRegistration")) {
                    if (resId == 1) {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                        builder.setTitle(commonVariables.appname);
                        builder.setMessage(result);
                        builder.setPositiveButton("Ok", (dialog, which) -> {
                            Intent intent = new Intent(AddMobileNoActivityBuyer.this, OTPMobileNoActivityBuyer.class);
                            intent.putExtra(commonVariables.KEY_MOBILE_NO, mEdt_register_mobile_wholesaler.getText().toString().trim());
                            startActivityForResult(intent, commonVariables.REQUEST_ADD_MOBILE);
                            overridePendingTransition(0, 0);
                        });
                        builder.show();
                    } else {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                        builder.setTitle(commonVariables.appname);
                        builder.setMessage(result);
                        builder.setPositiveButton("Ok", null);
                        builder.show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void returnBack(boolean b) {

        try {
            commonMethods.hidesoftKeyboard(this);
            Intent output = new Intent();
            output.putExtra(commonVariables.KEY_MOBILE_CHANGED, b);
            setResult(RESULT_OK, output);
            finish();
            overridePendingTransition(0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
