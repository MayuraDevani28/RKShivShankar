package com.shivshankar.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.shivshankar.LoginRegisterActivity;
import com.shivshankar.OTPRegisterActivity;
import com.shivshankar.R;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.Validation;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.json.JSONObject;

public class RegisterFragment extends Fragment implements View.OnClickListener, OnResult {

    boolean isClick;
    Button mBtn_login;
    private EditText mEdt_register_first_name, mEdt_register_email, mEdt_register_city, mEdt_register_company, mEdt_register_password, mEdt_register_confirm_password2, mEdt_register_mobile;
    TextInputLayout mLl_firm;
    ImageView mIv_eye;
    RadioGroup radioGroup;
    private boolean isVisiblePass = false;

    String regId, strDeviceUUID = commonVariables.uuid;
    int stType = 1;
    Context context;

    public RegisterFragment() {
    }

    @SuppressLint("ValidFragment")
    public RegisterFragment(boolean isclick) {
        isClick = isclick;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        try {
            bindViews(view);
            context = getActivity();
//            if (commonMethods.knowInternetOn(getActivity())) {
//                if (TextUtils.isEmpty(regId)) {
//                    regId = FirebaseInstanceId.getInstance().getToken();
//                    Log.e("TAG", "ReId:!" + regId);
//                } else {
//                    Log.d("TAG", "Already Registered with GCM Server!");
//                }
//            } else {
//                Log.d("TAG", "Internet Problem!");
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void bindViews(View view) {

        mEdt_register_first_name = (EditText) view.findViewById(R.id.edt_register_first_name);
        mEdt_register_email = (EditText) view.findViewById(R.id.edt_register_email);
        mEdt_register_password = (EditText) view.findViewById(R.id.edt_register_password);
        mIv_eye = (ImageView) view.findViewById(R.id.iv_eye);
        mIv_eye.setOnClickListener(this);
        mEdt_register_company = (EditText) view.findViewById(R.id.edt_register_company);
        mLl_firm = (TextInputLayout) view.findViewById(R.id.ll_firm);
        mEdt_register_city = (EditText) view.findViewById(R.id.edt_register_city);
        mEdt_register_mobile = (EditText) view.findViewById(R.id.edt_mobile);
        mBtn_login = (Button) view.findViewById(R.id.btn_register);
        mBtn_login.setOnClickListener(this);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.radioBuyer) {
                    stType = 1;
                    mLl_firm.setVisibility(View.GONE);
                } else if (checkedId == R.id.radioSeller) {
                    stType = 0;
                    mLl_firm.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            if (v == mIv_eye) {
                passwordVisibility(mEdt_register_password);
            } else if (v == mBtn_login) {

                String name = mEdt_register_first_name.getText().toString().trim();
                boolean fullNamerequiredError = false;
                if (!name.isEmpty() && name.contains(" ")) {
                    try {
                        name = name.replaceAll("  ", " ");
                        fullNamerequiredError = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else
                    fullNamerequiredError = true;
                String email = mEdt_register_email.getText().toString().trim();
                String mobile = mEdt_register_mobile.getText().toString().trim();
                String password = mEdt_register_password.getText().toString().trim();
                String city = mEdt_register_city.getText().toString().trim();
                String company = mEdt_register_company.getText().toString().trim();
                int subscribe = 1;
                mEdt_register_first_name.setError(null);
//                mEdt_register_last_name.setError(null);
                mEdt_register_email.setError(null);
                mEdt_register_password.setError(null);
                mEdt_register_mobile.setError(null);
                mEdt_register_city.setError(null);
                mEdt_register_company.setError(null);

                if (Validation.isEmptyEdittext(mEdt_register_first_name)) {
                    mEdt_register_first_name.setError("First name required");
                    mEdt_register_first_name.requestFocus();
                }
//                else if (Validation.isEmptyEdittext(mEdt_register_last_name))
//                    mEdt_register_last_name.setError("Last Name is required.");
                else if (fullNamerequiredError) {
                    mEdt_register_first_name.setError("Full name required");
                    mEdt_register_first_name.requestFocus();
                } else if (Validation.isEmptyEdittext(mEdt_register_email)) {
                    mEdt_register_email.setError("Email required");
                    mEdt_register_email.requestFocus();
                } else if (!Validation.isValidEmail(mEdt_register_email.getText().toString().trim())) {
                    mEdt_register_email.setError("Invalid Email Address.");
                    mEdt_register_email.requestFocus();
                } else if (mobile.isEmpty()) {
                    mEdt_register_mobile.setError("Mobile is required.");
                    mEdt_register_mobile.requestFocus();
                } else if (mobile.length() < 6 || mobile.length() > 13) {
                    mEdt_register_mobile.setError("Enter valid mobile number.");
                    mEdt_register_mobile.requestFocus();
                } else if (city.isEmpty()) {
                    mEdt_register_city.setError("City is required.");
                    mEdt_register_city.requestFocus();
                } else if (Validation.isEmptyEdittext(mEdt_register_password)) {
                    mEdt_register_password.setError("Password is required.");
                    mEdt_register_password.requestFocus();
                } else if (password.length() < 6) {
                    mEdt_register_password.setError("Min 6 characters required.");
                    mEdt_register_password.requestFocus();
                } else if (commonMethods.knowInternetOn(getActivity())) {
                    SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
                    editor.putString(commonVariables.KEY_CACHE_EMAIL, email);
//                    editor.putString(commonVariables.KEY_CACHE_PASS, password);
                    editor.apply();
                    APIs.SellerBuyerRegister((AppCompatActivity) getActivity(), this, name, email, password, mobile, city, company, strDeviceUUID, regId, stType);
                } else {
                    commonMethods.showInternetAlert(getActivity());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResult(JSONObject jobjWhole) {
        try {
            if (jobjWhole != null) {
                String strApiName = jobjWhole.optString("api");
                if (strApiName.equalsIgnoreCase("SellerBuyerRegister")) {
                    String result = jobjWhole.optString("res");
                    int resultId = jobjWhole.optInt("resInt");
                    if (resultId == 1) {
                        SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
                        editor.putString(commonVariables.KEY_USER_ID_FOR_OTP, mEdt_register_email.getText().toString().trim());
                        editor.apply();

                        Intent intent = new Intent(getActivity(), OTPRegisterActivity.class);
                        intent.putExtra(commonVariables.KEY_USER_TYPE, stType);
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition(0, 0);

                    } else {
                        if (resultId == 2) {
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                            builder.setTitle(commonVariables.appname);
                            builder.setMessage(result);
                            final String finalResult = result;
                            builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        ((LoginRegisterActivity) getActivity()).loginFragment.mEdtUsername.setText(mEdt_register_email.getText().toString().trim());
                                        ((LoginRegisterActivity) getActivity()).viewPager.setCurrentItem(0);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
//                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                                    intent.putExtra(commonVariables.REGISTER_EMAIL, mEdt_register_email.getText().toString().trim());
//                                    intent.putExtra(commonVariables.REGISTER_F_L_NAME, mEdt_register_first_name.getText().toString().trim());
//                                    intent.putExtra(commonVariables.REGISTER_MOBILE, mEdt_register_mobile.getText().toString().trim());
//                                    intent.putExtra(commonVariables.REGISTER_FIRM, mEdt_register_company.getText().toString().trim());
//                                    intent.putExtra(commonVariables.REGISTER_CITY, mEdt_register_city.getText().toString().trim());
//                                    if (finalResult.toUpperCase().contains("MOBILE"))
//                                        strLoginEmail = mEdt_register_mobile.getText().toString().trim();
//                                    intent.putExtra(commonVariables.LOGIN_EMAIL, strLoginEmail);
//                                    startActivity(intent);
//                                    finish();
//                                    overridePendingTransition(R.anim.fade_in_fast, R.anim.fade_out_fast);
                                }
                            });
                            builder.setNegativeButton("Cancel", null);
                            builder.show();

                        } else {
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                            builder.setTitle(commonVariables.appname);
                            builder.setMessage(result);
                            builder.setPositiveButton("Ok", null);
                            builder.show();
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
