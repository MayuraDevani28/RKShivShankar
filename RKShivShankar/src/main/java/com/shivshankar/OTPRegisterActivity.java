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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shivshankar.ServerCall.APIs;
import com.shivshankar.utills.AlertDialogManager;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.SmsListener;
import com.shivshankar.utills.SmsReceiver;
import com.shivshankar.utills.commonVariables;

import org.json.JSONObject;

import static com.shivshankar.utills.commonVariables.REQUEST_RECEIVE_MESSAGE;


public class OTPRegisterActivity extends AppCompatActivity implements View.OnClickListener, OnResult {
    // private EditText mEdt_OTP;
    private TextView mTv_resend_verification_code, mTv_otp_message;
    private ImageView mBtn_submit;
    public ImageView mIv_close;
    int stType;
    private EditText one, two, three, four;
    boolean isForLogin;

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
            isForLogin = getIntent().getBooleanExtra(commonVariables.KEY_FOR_LOGIN, false);
            bindViews();
            requestPermissionSMSReceive();

            SmsReceiver.bindListener(new SmsListener() {
                @Override
                public void messageReceived(String messageText) {
                    Log.d("Text", messageText);
                    //String str = "9465";
                    String str = messageText;
                    char[] cArray = str.toCharArray();
                    one.setText("" + cArray[0]);
                    two.setText("" + cArray[1]);
                    three.setText("" + cArray[2]);
                    four.setText("" + cArray[3]);
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
            case commonVariables.REQUEST_RECEIVE_MESSAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }

    private void bindViews() {
        one = (EditText) findViewById(R.id.tv1);
        two = (EditText) findViewById(R.id.tv2);
        three = (EditText) findViewById(R.id.tv3);
        four = (EditText) findViewById(R.id.tv4);
        mTv_resend_verification_code = (TextView) findViewById(R.id.tv_resend_verification_code);
        mTv_resend_verification_code.setOnClickListener(this);
        mBtn_submit = (ImageView) findViewById(R.id.btn_submit);
        mBtn_submit.setOnClickListener(this);

        mIv_close = (ImageView) findViewById(R.id.iv_close);
        mIv_close.setOnClickListener(this);
        mTv_otp_message = (TextView) findViewById(R.id.tv_otp_message);

        one.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (one.getText().toString().length() == 1) {
                    one.clearFocus();
                    two.requestFocus();
                    two.setCursorVisible(true);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });

        two.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (two.getText().toString().length() == 1) {
                    two.clearFocus();
                    three.requestFocus();
                    three.setCursorVisible(true);
                }
                if (s.toString().trim().length() == 0) {
                    one.requestFocus();
                    one.setSelection(one.getText().toString().length());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        three.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (three.getText().toString().length() == 1) {
                    three.clearFocus();
                    four.requestFocus();
                    four.setCursorVisible(true);

                }
                if (s.toString().trim().length() == 0) {
                    two.requestFocus();
                    two.setSelection(two.getText().toString().length());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        four.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (four.getText().toString().length() == 1) {
                }
                if (s.toString().trim().length() == 0) {
                    three.requestFocus();
                    three.setSelection(three.getText().toString().length());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mIv_close) {
            finish();
            overridePendingTransition(0, 0);
        } else if (v == mBtn_submit) {
            String strOTP = one.getText().toString().trim() + two.getText().toString().trim() + three.getText().toString().trim() + four.getText().toString().trim();
            if (strOTP.length() < 4) {
                Toast.makeText(this, "Please enter four digit otp.", Toast.LENGTH_SHORT).show();
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
                    AlertDialogManager.showDialog(this, result,null);
                } else if (strApiName.equalsIgnoreCase("VerifyOTP")) {
                    int resultId = jobjWhole.optInt("resInt");
                    if (resultId == 1) {

                        SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
                        editor.putString(commonVariables.KEY_LOGIN_ID, jobjWhole.optString("loginId"));
                        editor.putBoolean(commonVariables.KEY_IS_LOG_IN, true);

                        if (stType == 0) {
                            editor.putBoolean(commonVariables.KEY_IS_SELLER, true);
                            editor.putString(commonVariables.KEY_SELLER_PROFILE, jobjWhole.optJSONObject("resData").toString());
                        } else {
                            editor.putBoolean(commonVariables.KEY_IS_SELLER, false);
                            editor.putString(commonVariables.KEY_BUYER_PROFILE, jobjWhole.optJSONObject("resData").toString());
                        }
                        editor.apply();


                        if (isForLogin) {
                            Intent output = new Intent();
                            output.putExtra(commonVariables.KEY_IS_LOG_IN, true);
                            setResult(RESULT_OK, output);
                        } else {
                            Intent intent;
                            if (stType == 0) {
                                intent = new Intent(this, MainActivitySeller.class);
                                startActivity(intent);
                            } else {
                                intent = new Intent(this, MainActivityBuyer.class);
                                startActivity(intent);
                            }
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }

                        finish();
                        overridePendingTransition(0, 0);

                    } else {
                        AlertDialogManager.showDialog(this, result,null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
