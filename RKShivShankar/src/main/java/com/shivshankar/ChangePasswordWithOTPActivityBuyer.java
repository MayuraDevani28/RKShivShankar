package com.shivshankar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shivshankar.ServerCall.APIs;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.SmsListener;
import com.shivshankar.utills.SmsReceiver;
import com.shivshankar.utills.Validation;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.json.JSONObject;


@SuppressLint("NewApi")
public class ChangePasswordWithOTPActivityBuyer extends BaseActivityBuyer implements OnClickListener, OnResult {

    private EditText mEdt_otp, mEdt_new_password, mEdt_confirm_password;
    ImageView mIv_eye_new_password, mIv_eye_confirm_password;
    private TextView mTv_resend_verification_code, mTv_otp_message;
    private Button mBtn_save;
    ImageView mIv_close;
    AlertDialog dialog;
    private String strUserId = "";
    private boolean isVisiblePass_new = false, isVisiblePass_conf = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        View rootView = getLayoutInflater().inflate(R.layout.activity_change_password_otp_seller, frameLayout);
        bindViews(rootView);

        try {
            strUserId = getIntent().getStringExtra(commonVariables.KEY_LOGIN_ID);

            requestPermissionSMSReceive();

            SmsReceiver.bindListener(new SmsListener() {
                @Override
                public void messageReceived(String messageText) {
                    Log.d("Text", messageText);
                    mEdt_otp.setText(messageText);
                    mEdt_otp.setSelection(messageText.length());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    onBackPressed();
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void bindViews(View rootView) {
        try {
            mEdt_otp = (EditText) rootView.findViewById(R.id.edt_otp);
            mEdt_new_password = (EditText) rootView.findViewById(R.id.edt_new_password);
            mEdt_confirm_password = (EditText) rootView.findViewById(R.id.edt_confirm_password);
            mIv_eye_new_password = (ImageView) rootView.findViewById(R.id.iv_eye_new_password);
            mIv_eye_new_password.setOnClickListener(this);
            mIv_eye_confirm_password = (ImageView) rootView.findViewById(R.id.iv_eye_confirm_password);
            mIv_eye_confirm_password.setOnClickListener(this);
            mBtn_save = (Button) rootView.findViewById(R.id.btn_save);
            mBtn_save.setOnClickListener(this);
            mIv_close = (ImageView) rootView.findViewById(R.id.iv_close);
            mIv_close.setOnClickListener(this);
            mTv_resend_verification_code = (TextView) rootView.findViewById(R.id.tv_resend_verification_code);
            mTv_resend_verification_code.setOnClickListener(this);
            mTv_otp_message = (TextView) rootView.findViewById(R.id.tv_otp_message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, 0);
    }

    private void passwordVisibility_new(EditText editComment) {
        try {
            editComment.requestFocus();
            if (isVisiblePass_new) {
                isVisiblePass_new = false;
                editComment.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                isVisiblePass_new = true;
                editComment.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void passwordVisibilityConfirm(EditText editComment) {
        try {
            editComment.requestFocus();
            if (isVisiblePass_conf) {
                isVisiblePass_conf = false;
                editComment.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                isVisiblePass_conf = true;
                editComment.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        try {
            AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            view.startAnimation(buttonClick);
            mEdt_otp.setError(null);
            mEdt_confirm_password.setError(null);
            mEdt_new_password.setError(null);
            if (view == mIv_close) {
                returnBack(false);
            } else if (view == mIv_eye_confirm_password) {
                passwordVisibilityConfirm(mEdt_confirm_password);
            } else if (view == mTv_resend_verification_code) {
                callResendRegistrationOTPAPI();
            } else if (view == mIv_eye_new_password) {
                passwordVisibility_new(mEdt_new_password);
            } else if (view == mBtn_save) {

                String strOTP = mEdt_otp.getText().toString().trim();
                String strNew_password = mEdt_new_password.getText().toString().trim();
                String strConfirm_password = mEdt_confirm_password.getText().toString().trim();

                if (Validation.isEmptyEdittext(mEdt_otp)) {
                    mEdt_otp.setError("OTP required.");
                } else if (Validation.isEmptyEdittext(mEdt_new_password)) {
                    mEdt_new_password.setError("New Password required.");
                } else if (strNew_password.length() < 6) {
                    mEdt_new_password.setError("Min 6 characters required.");
                } else if (Validation.isEmptyEdittext(mEdt_confirm_password)) {
                    mEdt_confirm_password.setError("Confirm Password is required.");
                } else if (!strNew_password.equals(strConfirm_password)) {
                    mEdt_confirm_password.setError("Passwords Does not match.");
                } else {
                    if (commonMethods.knowInternetOn(ChangePasswordWithOTPActivityBuyer.this)) {
                        callForgotPasswardAPI(strOTP, strUserId, strNew_password);
                    } else {
                        commonMethods.showInternetAlert(ChangePasswordWithOTPActivityBuyer.this);
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
            output.putExtra(commonVariables.KEY_PASSWORD_CHANGED, b);
            setResult(RESULT_OK, output);
            finish();
            overridePendingTransition(0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callResendRegistrationOTPAPI() {
        Uri uri = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/ResendRegistrationOTP")
                .appendQueryParameter("strUserId", strUserId)
                .build();
        String query = uri.toString();
        APIs.callAPI(this, this, query);
    }

    private void callForgotPasswardAPI(String strOTP, String strLoginId, String strNewPassword) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/ForgotPassward")
                .appendQueryParameter("strUserId", strLoginId)
                .appendQueryParameter("strOTP", strOTP)
                .appendQueryParameter("newPassword", strNewPassword).build();
        String query = uri.toString();
        APIs.callAPI(this, this, query);
    }

    @Override
    public void onResult(JSONObject jObjWhole) {
        if (jObjWhole != null) {
            try {
                String strApiName = jObjWhole.optString("api");
                int resId = jObjWhole.optInt("resInt");
                String result = jObjWhole.optString("res");
                if (strApiName.equalsIgnoreCase("ResendRegistrationOTP")) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setTitle(commonVariables.appname);
                    builder.setMessage(result);
                    builder.setPositiveButton("Ok", null);
                    builder.show();
                } else if (strApiName.equalsIgnoreCase("ForgotPassward") && resId == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(commonVariables.appname);
                    builder.setMessage(result);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            returnBack(true);
                        }
                    });
                    builder.show();
                } else {
                    if (dialog != null)
                        dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(commonVariables.appname);
                    builder.setMessage(result);
                    builder.setPositiveButton("Ok", null);
                    builder.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
