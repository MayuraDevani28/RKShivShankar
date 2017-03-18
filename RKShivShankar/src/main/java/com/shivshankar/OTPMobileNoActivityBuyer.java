package com.shivshankar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shivshankar.ServerCall.APIs;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.SmsListener;
import com.shivshankar.utills.SmsReceiver;
import com.shivshankar.utills.Validation;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.json.JSONObject;


public class OTPMobileNoActivityBuyer extends BaseActivitySeller implements View.OnClickListener, OnResult {
    private EditText mEdt_OTP;
    private TextView mTv_resend_verification_code, mTv_otp_message;
    private Button mBtn_submit;
    String strMobileNo;
    public ImageView mIv_close;
    String otp_message;

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
            View rootView = getLayoutInflater().inflate(R.layout.activity_otp_mobile_seller, frameLayout);
            bindViews(rootView);

            strMobileNo = getIntent().getStringExtra(commonVariables.KEY_MOBILE_NO);
            otp_message = getIntent().getStringExtra(commonVariables.KEY_OTP_MESSAGE);
            requestPermissionSMSReceive();

            SmsReceiver.bindListener(new SmsListener() {
                @Override
                public void messageReceived(String messageText) {
                    Log.d("Text", messageText);
                    mEdt_OTP.setText(messageText);
                    mEdt_OTP.setSelection(messageText.length());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestPermissionSMSReceive() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.RECEIVE_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECEIVE_SMS}, commonVariables.REQUEST_RECEIVE_MESSAGE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, 0);
    }

    @SuppressLint("Override")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case commonVariables.REQUEST_RECEIVE_MESSAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }

    private void bindViews(View rootView) {
        mEdt_OTP = (EditText) rootView.findViewById(R.id.edt_otp);
        mTv_resend_verification_code = (TextView) rootView.findViewById(R.id.tv_resend_verification_code);
        mTv_resend_verification_code.setOnClickListener(this);
        mBtn_submit = (Button) rootView.findViewById(R.id.btn_submit);
        mBtn_submit.setOnClickListener(this);

        mIv_close = (ImageView) rootView.findViewById(R.id.iv_close);
        mIv_close.setOnClickListener(this);
        mTv_otp_message = (TextView) rootView.findViewById(R.id.tv_otp_message);
        mTv_otp_message.setText(otp_message);
    }

    @Override
    public void onClick(View view) {
        try {
            if (view == mIv_close) {
                returnBack(false);
            } else if (view == mBtn_submit) {
                String strOTP = mEdt_OTP.getText().toString().trim();
                mEdt_OTP.setError(null);

                if (Validation.isEmptyEdittext(mEdt_OTP)) {
                    mEdt_OTP.setError("Enter OTP");
                    mEdt_OTP.requestFocus();
                } else
                    callVerifyOTPForNewMobileNoAPI(strOTP);
            } else if (view == mTv_resend_verification_code) {
                callCheckMobileNoForRegistrationAPI(strMobileNo);
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


    private void callVerifyOTPForNewMobileNoAPI(String strOTP) {
        Uri uri = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/VerifyOTPForNewMobileNo")
                .appendQueryParameter("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, ""))
                .appendQueryParameter("strOTP", strOTP)
                .appendQueryParameter("newMobileNo", strMobileNo)
                .build();
        String query = uri.toString();
//        new ServerAPICAll(null, this).execute(query);
        APIs.callAPI(null, this, query);
    }

    @Override
    public void onResult(JSONObject jObjWhole) {
        try {
            if (jObjWhole != null) {
                String result = jObjWhole.optString("res");
                String strApiName = jObjWhole.optString("api");
                if (strApiName.equalsIgnoreCase("CheckMobileNoForRegistration")) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setTitle(commonVariables.appname);
                    builder.setMessage(result);
                    builder.setPositiveButton("Ok", null);
                    builder.show();
                } else if (strApiName.equalsIgnoreCase("VerifyOTPForNewMobileNo")) {
                    if (result.equals("success")) {
                        returnBack(true);
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
