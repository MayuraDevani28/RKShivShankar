package com.shivshankar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.Validation;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONObject;

@SuppressLint("NewApi")
public class ChangePasswordActivityBuyer extends BaseActivityBuyer implements OnClickListener, OnResult {

    private EditText mEdt_current_password, mEdt_new_password, mEdt_confirm_password;
    ImageView mIv_eye_current_password, mIv_eye_new_password, mIv_eye_confirm_password;
    TextView mBtn_cancel;

    //    private TextView mTv_forgot_passord;
    private Button mBtn_save;
    ImageView mIv_close;
    AlertDialog dialog;
    private boolean isVisiblePass_curr = false, isVisiblePass_new = false, isVisiblePass_conf = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        View rootView = getLayoutInflater().inflate(R.layout.activity_change_password_seller, frameLayout);
        bindViews(rootView);

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
            mIv_logo_nav.setOnClickListener(this);
            mIv_logo_toolbar.setOnClickListener(this);
            mTv_username.setOnClickListener(this);
            mTv_logout.setOnClickListener(this);
            mLl_close.setOnClickListener(this);

            mNav_my_profile.setOnClickListener(this);
            mNav_my_orders.setOnClickListener(this);
            mNav_about_us.setOnClickListener(this);
            mNav_our_policy.setOnClickListener(this);
            mNav_contact_us.setOnClickListener(this);

            mEdt_current_password = (EditText) rootView.findViewById(R.id.edt_current_password);
            mEdt_new_password = (EditText) rootView.findViewById(R.id.edt_new_password);
            mEdt_confirm_password = (EditText) rootView.findViewById(R.id.edt_confirm_password);
            mIv_eye_current_password = (ImageView) rootView.findViewById(R.id.iv_eye_current_password);
            mIv_eye_current_password.setOnClickListener(this);
            mIv_eye_new_password = (ImageView) rootView.findViewById(R.id.iv_eye_new_password);
            mIv_eye_new_password.setOnClickListener(this);
            mIv_eye_confirm_password = (ImageView) rootView.findViewById(R.id.iv_eye_confirm_password);
            mIv_eye_confirm_password.setOnClickListener(this);
//            mTv_forgot_passord = (TextView) rootView.findViewById(R.id.tv_login_forgot_passord);
//            mTv_forgot_passord.setOnClickListener(this);
            mBtn_save = (Button) rootView.findViewById(R.id.btn_save);
            mBtn_save.setOnClickListener(this);
            mIv_close = (ImageView) rootView.findViewById(R.id.iv_close);
            mIv_close.setOnClickListener(this);
            mBtn_cancel = (TextView) rootView.findViewById(R.id.btn_cancel_receiver);
            mBtn_cancel.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            if (mTv_username != null) {
                String strProfile = AppPreferences.getPrefs().getString(commonVariables.KEY_SELLER_PROFILE, "");
                if (!strProfile.isEmpty() && !strProfile.equalsIgnoreCase("null"))
                    mTv_username.setText(WordUtils.capitalizeFully(new JSONObject(strProfile).optString("Name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, 0);
    }

    private void passwordVisibility_curr(EditText editComment) {
        try {
            editComment.requestFocus();
            if (isVisiblePass_curr) {
                isVisiblePass_curr = false;
                editComment.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                isVisiblePass_curr = true;
                editComment.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            mEdt_current_password.setError(null);
            mEdt_confirm_password.setError(null);
            mEdt_new_password.setError(null);
            if (view == mIv_logo_toolbar) {
                Intent intent = new Intent(this, MainActivitySeller.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
            } else if (view == mNav_my_profile) {
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, MyProfileActivityBuyer.class));
                overridePendingTransition(0, 0);
            } else if (view == mNav_my_orders) {
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, MyOrdersActivityBuyer.class));
                overridePendingTransition(0, 0);
            }  else if (view == mNav_about_us) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(this, CMSCallandDisplayActivityBuyer.class);
                intent.putExtra(commonVariables.INTENT_EXTRA_PAGE_NAME, "aboutus");
                startActivity(intent);
                overridePendingTransition(0, 0);
            } else if (view == mNav_our_policy) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(this, CMSListingActivityBuyer.class);
                intent.putExtra(commonVariables.INTENT_EXTRA_PAGE, "GetPolicies");
                intent.putExtra(commonVariables.INTENT_EXTRA_PAGE_NAME, "Our Policy");
                startActivity(intent);
                overridePendingTransition(0, 0);
            } else if (view == mNav_contact_us) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(this, CMSCallandDisplayActivityBuyer.class);
                intent.putExtra(commonVariables.INTENT_EXTRA_PAGE_NAME, "contactus");
                startActivity(intent);
                overridePendingTransition(0, 0);
            }else if (view == mLl_close || view == mIv_logo_nav || view == mTv_username) {
                drawer.closeDrawer(GravityCompat.START);
            }else if (view == mTv_logout) {
                drawer.closeDrawer(GravityCompat.START);
                if (mTv_logout.getText().equals("Login")) {
                    startActivity(new Intent(this, LoginRegisterActivity.class));
                    onBackPressed();
                } else {
                    commonMethods.logout(this, true);
                }
            }else if (view == mIv_close || view == mBtn_cancel) {
                returnBack();
            } else if (view == mIv_eye_confirm_password) {
                passwordVisibilityConfirm(mEdt_confirm_password);
            } else if (view == mIv_eye_current_password) {
                passwordVisibility_curr(mEdt_current_password);
            } else if (view == mIv_eye_new_password) {
                passwordVisibility_new(mEdt_new_password);
            }
//            else if (v == mTv_forgot_passord) {
//
//                android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(this);
//                alert.setTitle("Reset Password");
//
//                int padingval = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, getResources().getDisplayMetrics());
//                int pading15 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
//
//                final EditText input = new EditText(this);
//                input.setHint("Enter Email id/Mobile");
//                input.setText(AppPreferences.getPrefs().getString(commonVariables.KEY_CACHE_EMAIL, ""));
//                input.setBackground(ContextCompat.getDrawable(this, R.drawable.xml_black_border_square_box));
//                input.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium));
//                input.setPadding(padingval, padingval, padingval, padingval);
//                input.setSingleLine();
//                FrameLayout container = new FrameLayout(this);
//                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                params.leftMargin = pading15;
//                params.rightMargin = pading15;
//                params.topMargin = pading15;
//                params.bottomMargin = pading15;
//                input.setLayoutParams(params);
//                input.setSelection(input.getText().toString().length());
//                container.addView(input);
//                alert.setView(container);
//
//                alert.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                    }
//
//                });
//
//                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        dialog.cancel();
//                    }
//                });
//
//                dialog = alert.create();
//                dialog.show();
//                dialog.getWindow().getAttributes();
//
//                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
//                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium));
//
//                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        strUserId = input.getText().toString().trim();
//                        if (Validation.isEmptyEdittext(input))
//                            input.setError("Email/Mobile is required.");
//                        else if (strUserId.contains("@") && !Validation.isValidEmail(strUserId))
//                            input.setError("Invalid Email Address.");
//                        else
//                            callResendRegistrationOTPAPI(strUserId);
//                    }
//                });
//                dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.black));
//                dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.black));
//
//            }
            else if (view == mBtn_save) {
                String strPassword = mEdt_current_password.getText().toString().trim();
                String strNew_password = mEdt_new_password.getText().toString().trim();
                String strConfirm_password = mEdt_confirm_password.getText().toString().trim();

                if (Validation.isEmptyEdittext(mEdt_current_password)) {
                    mEdt_current_password.setError("Current Password required.");
                } else if (Validation.isEmptyEdittext(mEdt_new_password)) {
                    mEdt_new_password.setError("New Password required.");
                } else if (strNew_password.length() < 6) {
                    mEdt_new_password.setError("Min 6 characters required.");
                } else if (Validation.isEmptyEdittext(mEdt_confirm_password)) {
                    mEdt_confirm_password.setError("Confirm Password is required.");
                } else if (!strNew_password.equals(strConfirm_password)) {
                    mEdt_confirm_password.setError("Passwords Does not match.");
                } else {
                    if (commonMethods.knowInternetOn(ChangePasswordActivityBuyer.this)) {
                        APIs.BuyerChangePassword(this, this, strPassword, strNew_password);
                    } else {
                        commonMethods.showInternetAlert(ChangePasswordActivityBuyer.this);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void returnBack() {

        try {
            commonMethods.hidesoftKeyboard(this);
            finish();
            overridePendingTransition(0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void callResendRegistrationOTPAPI(String strUserId) {
//        Uri uri = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
//                .path("MobileAPI/ResendRegistrationOTP")
//                .appendQueryParameter("strUserId", strUserId)
//                .build();
//        String query = uri.toString();
////        new ServerAPICAll(this, this).execute(query);
//        APIs.callAPI(null, this, query);
//    }

    @Override
    public void onResult(JSONObject jObjWhole) {
        if (jObjWhole != null) {
            try {
                String strApiName = jObjWhole.optString("api");
                int resId = jObjWhole.optInt("resInt");
                String result = jObjWhole.optString("res");
                if (strApiName.equalsIgnoreCase("SellerChangePassword") && resId == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(commonVariables.appname);
                    builder.setMessage(result);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            onBackPressed();
                            finish();
                            overridePendingTransition(0, 0);
                        }
                    });
                    builder.show();
                }
//                else if (strApiName.equalsIgnoreCase("ResendRegistrationOTP") && resId == 1) {
//                    if (dialog != null)
//                        dialog.dismiss();
//
//                    Intent intent = new Intent(ChangePasswordActivitySeller.this, ChangePasswordWithOTPActivityBuyer.class);
//                    intent.putExtra(commonVariables.KEY_SELLER_PROFILE, strUserId);
//                    intent.putExtra(commonVariables.KEY_OTP_MESSAGE, jObjWhole.optString("resMsg"));
//                    startActivityForResult(intent, commonVariables.REQUEST_PASSWORD_CHANGE);
//                    overridePendingTransition(0, 0);
//
//                }
                else {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == commonVariables.REQUEST_PASSWORD_CHANGE && resultCode == RESULT_OK && data != null) {
                if (data != null) {
                    boolean isAddressChanged = data.getExtras().getBoolean(commonVariables.KEY_PASSWORD_CHANGED);
                    if (isAddressChanged) {
                        returnBack();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
