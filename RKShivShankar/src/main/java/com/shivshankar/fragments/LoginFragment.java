package com.shivshankar.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.shivshankar.LoginRegisterActivity;
import com.shivshankar.MainActivityBuyer;
import com.shivshankar.MainActivitySeller;
import com.shivshankar.OTPRegisterActivity;
import com.shivshankar.R;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.Validation;
import com.shivshankar.utills.commonVariables;

import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;

public class LoginFragment extends Fragment implements View.OnClickListener, OnResult {

    private Button btnLogin;
    public EditText mEdtUsername, mEdtPassword;
    TextView mTv_forget;
    LinearLayout mLl_skip;
    private String strUserId, stPassword;
    AlertDialog dialog;
    ImageView mIv_eye;
    private RadioGroup radioGroup;
    public int stType = 1;
    private boolean isVisiblePass = false;
    String regId, strDeviceUUID = commonVariables.uuid;


    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        try {
            init(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void init(View v) {
        mEdtUsername = (EditText) v.findViewById(R.id.edt_username);
        mEdtPassword = (EditText) v.findViewById(R.id.edt_password);
        mEdtPassword.setOnEditorActionListener((v1, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
                login();
                // hide virtual keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEdtPassword.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                return true;
            }
            return false;
        });
        mIv_eye = (ImageView) v.findViewById(R.id.iv_eye);
        mIv_eye.setOnClickListener(this);
        mTv_forget = (TextView) v.findViewById(R.id.tv_forget);
        mTv_forget.setOnClickListener(this);
        mLl_skip = (LinearLayout) v.findViewById(R.id.ll_skip);
        mLl_skip.setOnClickListener(this);
        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);

        btnLogin = (Button) v.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        String strCacheEmail = AppPreferences.getPrefs().getString(commonVariables.KEY_CACHE_EMAIL, "");
        if (!strCacheEmail.isEmpty()) {
            mEdtUsername.setText(strCacheEmail);
            mEdtUsername.setSelection(strCacheEmail.length());
        }
        mEdtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mEdtUsername.setError(null);
                mEdtPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                try {
                    if (checkedId == R.id.radioBuyer) {
                        stType = 1;
                        ((LoginRegisterActivity) getActivity()).setRegisterVisibility(true);
                        ((LoginRegisterActivity) getActivity()).mIv_close.setVisibility(View.VISIBLE);
                        mLl_skip.setVisibility(View.VISIBLE);
                    } else if (checkedId == R.id.radioSeller) {
                        stType = 0;
                        ((LoginRegisterActivity) getActivity()).setRegisterVisibility(false);
                        ((LoginRegisterActivity) getActivity()).mIv_close.setVisibility(View.GONE);
                        mLl_skip.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


//        context = getActivity().getApplicationContext();
//        if (commonMethods.knowInternetOn(getActivity())) {
//            if (TextUtils.isEmpty(regId)) {
//                regId = FirebaseInstanceId.getInstance().getToken();
//                Log.e("TAGRK", "ReId:!" + regId);
//            } else {
//                Log.d("TAGRK", "Already Registered with GCM Server!");
//            }
//        } else {
//            Log.d("TAGRK", "Internet Problem!");
//        }
    }


    private void passwordVisibility(EditText editComment) {
        try {
            editComment.requestFocus();
            if (isVisiblePass) {
                isVisiblePass = false;
                editComment.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                isVisiblePass = true;
                editComment.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            editComment.setSelection(editComment.getText().toString().length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        if (v == btnLogin) {
            login();
        } else if (v == mIv_eye) {
            passwordVisibility(mEdtPassword);
        } else if (v == mLl_skip) {
            callBuyerWithoutLogin();
        } else if (v == mTv_forget) {

            android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity());
            alert.setTitle("Reset Password");

            int padingval = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, getResources().getDisplayMetrics());
            int pading15 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());

            final EditText input = new EditText(getActivity());
            input.setHint("Enter Email id/Mobile");
            input.setText(mEdtUsername.getText().toString().trim());
            input.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.xml_black_border_square_box));
            input.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium));
            input.setPadding(padingval, padingval, padingval, padingval);
            input.setSingleLine();
            FrameLayout container = new FrameLayout(getActivity());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = pading15;
            params.rightMargin = pading15;
            params.topMargin = pading15;
            params.bottomMargin = pading15;
            input.setLayoutParams(params);
            input.setSelection(input.getText().length());
            container.addView(input);
            alert.setView(container);

            alert.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }

            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                }
            });

            dialog = alert.create();
            dialog.show();
            dialog.getWindow().getAttributes();


            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium));

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    strUserId = input.getText().toString().trim();
                    if (Validation.isEmptyEdittext(input))
                        input.setError("Email/Mobile is required.");
                    else if (strUserId.contains("@") && !Validation.isValidEmail(strUserId))
                        input.setError("Invalid Email Address.");
                    else {
                        APIs.SellerBuyerForgotPassward((AppCompatActivity) getActivity(), LoginFragment.this, strUserId, stType);
                    }
                }
            });
            dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.black));


        }
    }

    public void callBuyerWithoutLogin() {

        SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
        editor.putBoolean(commonVariables.KEY_IS_LOG_IN, false);
        editor.putBoolean(commonVariables.KEY_IS_SELLER, false);
        editor.putBoolean(commonVariables.KEY_IS_SKIPPED_LOGIN_BUYER, true);
        editor.apply();
        if (((LoginRegisterActivity) getActivity()).isForLogin) {
            getActivity().finish();
        } else {
            Intent i = new Intent(getActivity(), MainActivityBuyer.class);
            getActivity().finish();
            getActivity().startActivity(i);
        }
        getActivity().overridePendingTransition(0, 0);
    }

    private void login() {

        strUserId = mEdtUsername.getText().toString().trim();
        stPassword = mEdtPassword.getText().toString().trim();
        AppPreferences.getPrefs().edit().putString(commonVariables.KEY_CACHE_EMAIL, strUserId).apply();

        if (Validation.isEmptyEdittext(mEdtUsername) && Validation.isEmptyEdittext(mEdtPassword)) {
            mEdtUsername.setError("Enter Username");
            mEdtUsername.requestFocus();
            mEdtPassword.setError("Enter Password");
        } else if (Validation.isEmptyEdittext(mEdtUsername)) {
            mEdtUsername.requestFocus();
            mEdtUsername.setError("Enter Username");
        } else if (Validation.isEmptyEdittext(mEdtPassword)) {
            mEdtPassword.requestFocus();
            mEdtPassword.setError("Enter Password");
        } else {
            String strModelName = Build.MODEL;
            String strOSVersion = Build.VERSION.RELEASE;
            String strDeviceType = "Android";
            //FirebaseInstanceId.getInstance().getToken()
            APIs.SellerBuyerLogin((AppCompatActivity) getActivity(), this, strUserId, stPassword, strDeviceType, strDeviceUUID, strModelName, strOSVersion, regId, stType);
        }

    }

    @Override
    public void onResult(JSONObject jobj) {
        try {
            if (jobj != null) {
                String strApiName = jobj.optString("api");
                String result = jobj.optString("res");
                if (strApiName.equalsIgnoreCase("SellerBuyerLogin")) {
                    int resultId = jobj.optInt("resInt");
                    if (resultId == 1) {
                        SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
                        editor.putBoolean(commonVariables.KEY_IS_LOG_IN, true);
                        editor.putString(commonVariables.KEY_LOGIN_ID, jobj.optString("loginId"));
                        if (stType == 0) {
                            editor.putBoolean(commonVariables.KEY_IS_SELLER, true);
                            editor.putString(commonVariables.KEY_SELLER_PROFILE, jobj.optJSONObject("resData").toString());
                        } else {
                            editor.putBoolean(commonVariables.KEY_IS_SELLER, false);
                            editor.putString(commonVariables.KEY_BUYER_PROFILE, jobj.optJSONObject("resData").toString());
                        }
                        editor.apply();

                        if (((LoginRegisterActivity) getActivity()).isForLogin) {
                            Intent output = new Intent();
                            output.putExtra(commonVariables.KEY_IS_LOG_IN, true);
                            getActivity().setResult(RESULT_OK, output);
                        } else {
                            if (stType == 0) {
                                Intent i = new Intent(getActivity(), MainActivitySeller.class);
                                getActivity().startActivity(i);
                            } else {
                                Intent i = new Intent(getActivity(), MainActivityBuyer.class);
                                getActivity().startActivity(i);
                            }
                        }
                        getActivity().finish();
                        getActivity().overridePendingTransition(0, 0);
                    } else if (resultId == 2) {
                        SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
                        editor.putString(commonVariables.KEY_USER_ID_FOR_OTP, mEdtUsername.getText().toString().trim());
                        editor.apply();

                        //added by praful
                        Intent intent = new Intent(getActivity(), OTPRegisterActivity.class);
                        intent.putExtra(commonVariables.KEY_USER_TYPE, stType);
                        intent.putExtra(commonVariables.KEY_FOR_LOGIN, ((LoginRegisterActivity) getActivity()).isForLogin);
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition(0, 0);

                    } else {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                        builder.setTitle(commonVariables.appname);
                        builder.setMessage(result);
                        builder.setPositiveButton("Ok", null);
                        builder.show();
                    }
                } else if (strApiName.equalsIgnoreCase("SellerBuyerForgotPassward")) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                    builder.setTitle(commonVariables.appname);
                    builder.setMessage(result);
                    builder.setPositiveButton("Ok", null);
                    builder.show();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
