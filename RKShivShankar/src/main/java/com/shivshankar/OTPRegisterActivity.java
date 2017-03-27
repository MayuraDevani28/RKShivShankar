package com.shivshankar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.SmsListener;
import com.shivshankar.utills.SmsReceiver;
import com.shivshankar.utills.Validation;
import com.shivshankar.utills.commonVariables;

import org.json.JSONObject;

import static com.shivshankar.utills.commonVariables.REQUEST_RECEIVE_MESSAGE;


public class OTPRegisterActivity extends AppCompatActivity implements View.OnClickListener, OnResult {
    private EditText mEdt_OTP;
    private TextView mTv_resend_verification_code, mTv_otp_message;
    private Button mBtn_submit;
    public ImageView mIv_close;
    String otp_message;
    int stType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_otp);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
            stType = getIntent().getIntExtra(commonVariables.KEY_USER_TYPE, 0);
            otp_message = getIntent().getStringExtra(commonVariables.KEY_OTP_MESSAGE);
            bindViews();
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
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECEIVE_SMS}, REQUEST_RECEIVE_MESSAGE);
            }
        }
    }

    @SuppressLint("Override")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_RECEIVE_MESSAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }

    private void bindViews() {
        mEdt_OTP = (EditText) findViewById(R.id.edt_otp);
        mTv_resend_verification_code = (TextView) findViewById(R.id.tv_resend_verification_code);
        mTv_resend_verification_code.setOnClickListener(this);
        mBtn_submit = (Button) findViewById(R.id.btn_submit);
        mBtn_submit.setOnClickListener(this);

        mIv_close = (ImageView) findViewById(R.id.iv_close);
        mIv_close.setOnClickListener(this);
        mTv_otp_message = (TextView) findViewById(R.id.tv_otp_message);
        if (!otp_message.isEmpty())
            mTv_otp_message.setText(otp_message);
    }

    @Override
    public void onClick(View v) {
        if (v == mIv_close) {
            finish();
            overridePendingTransition(0, 0);
        } else if (v == mBtn_submit) {
            String strOTP = mEdt_OTP.getText().toString().trim();
            mEdt_OTP.setError(null);

            if (Validation.isEmptyEdittext(mEdt_OTP)) {
                mEdt_OTP.setError("Enter OTP");
                mEdt_OTP.requestFocus();
            } else
                APIs.VerifyOTP(this, this, strOTP, stType);
        } else if (v == mTv_resend_verification_code) {
            APIs.ResendRegistrationOTP(this, this, stType);
            mTv_resend_verification_code.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResult(JSONObject jobjWhole) {
        try {
            if (jobjWhole != null) {
                String result = jobjWhole.optString("res");
                String strApiName = jobjWhole.optString("api");
                if (strApiName.equalsIgnoreCase("ResendRegistrationOTP")) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setTitle(commonVariables.appname);
                    builder.setMessage(result);
                    builder.setPositiveButton("Ok", null);
                    builder.show();
                } else if (strApiName.equalsIgnoreCase("VerifyOTP")) {
                    int resultId = jobjWhole.optInt("resInt");
                    if (resultId == 1) {

                        SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
                        editor.putString(commonVariables.KEY_LOGIN_ID, jobjWhole.optString("loginId"));
                        editor.putBoolean(commonVariables.KEY_IS_LOG_IN, true);

                        Intent intent;
                        if (stType == 0) {
                            editor.putBoolean(commonVariables.KEY_IS_SELLER, true);
                            editor.putString(commonVariables.KEY_SELLER_PROFILE, jobjWhole.optJSONObject("resData").toString());
                            intent = new Intent(this, MainActivitySeller.class);
                        } else {
                            editor.putBoolean(commonVariables.KEY_IS_SELLER, false);
                            editor.putString(commonVariables.KEY_BUYER_PROFILE, jobjWhole.optJSONObject("resData").toString());
                            intent = new Intent(this, MainActivityBuyer.class);
                        }
                        editor.apply();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0, 0);

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
}
