package com.shivshankar.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.google.firebase.iid.FirebaseInstanceId;
import com.shivshankar.LoginRegisterActivity;
import com.shivshankar.OTPRegisterActivity;
import com.shivshankar.R;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.utills.AlertDialogManager;
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
    private TextInputLayout mTi_register_first_name, mTi_register_email, mTi_register_city, mLl_firm, mTi_register_password, mTi_register_mobile;
    ImageView mIv_eye;
    RadioGroup radioGroup;
    private boolean isVisiblePass = false;

    String strDeviceUUID = commonVariables.uuid;
    int stType = 1;

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
            if (commonMethods.knowInternetOn(getActivity())) {
                Log.e("TAGRK", "ReId:!" + FirebaseInstanceId.getInstance().getToken());
            } else {
                Log.d("TAGRK", "Internet Problem!");
            }
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
        mEdt_register_company = (EditText) view.findViewById(R.id.edt_firm);
        mEdt_register_city = (EditText) view.findViewById(R.id.edt_register_city);
        mEdt_register_mobile = (EditText) view.findViewById(R.id.edt_mobile);

        mTi_register_first_name = (TextInputLayout) view.findViewById(R.id.ti_register_first_name);
        mTi_register_email = (TextInputLayout) view.findViewById(R.id.ti_register_email);
        mTi_register_password = (TextInputLayout) view.findViewById(R.id.ti_register_password);
        mLl_firm = (TextInputLayout) view.findViewById(R.id.ll_firm);
        mTi_register_city = (TextInputLayout) view.findViewById(R.id.ti_register_city);
        mTi_register_mobile = (TextInputLayout) view.findViewById(R.id.ti_mobile);

        mBtn_login = (Button) view.findViewById(R.id.btn_register);
        mBtn_login.setOnClickListener(this);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                try {
                    if (checkedId == R.id.radioBuyer) {
                        stType = 1;
//                        ((LoginRegisterActivity) getActivity()).setRegisterVisibility(true);
                        ((LoginRegisterActivity) getActivity()).mIv_close.setVisibility(View.VISIBLE);
                        mLl_firm.setVisibility(View.GONE);
                    } else if (checkedId == R.id.radioSeller) {
                        stType = 0;
//                        ((LoginRegisterActivity) getActivity()).setRegisterVisibility(false);
                        ((LoginRegisterActivity) getActivity()).mIv_close.setVisibility(View.GONE);
                        mLl_firm.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
            editComment.setSelection(editComment.getText().toString().length());
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
                mTi_register_first_name.setError(null);
                mTi_register_email.setError(null);
                mTi_register_password.setError(null);
                mTi_register_mobile.setError(null);
                mTi_register_city.setError(null);
                mLl_firm.setError(null);

                if (Validation.isEmptyEdittext(mEdt_register_first_name)) {
                    mTi_register_first_name.setError("First name required");
                    mEdt_register_first_name.requestFocus();
                }
//                else if (Validation.isEmptyEdittext(mEdt_register_last_name))
//                    mEdt_register_last_name.setError("Last Name is required.");
                else if (fullNamerequiredError) {
                    mTi_register_first_name.setError("Full name required");
                    mEdt_register_first_name.requestFocus();
                } else if (Validation.isEmptyEdittext(mEdt_register_email)) {
                    mTi_register_email.setError("Email required");
                    mEdt_register_email.requestFocus();
                } else if (!Validation.isValidEmail(mEdt_register_email.getText().toString().trim())) {
                    mTi_register_email.setError("Invalid Email Address.");
                    mEdt_register_email.requestFocus();
                } else if (mobile.isEmpty()) {
                    mTi_register_mobile.setError("Mobile is required.");
                    mEdt_register_mobile.requestFocus();
                } else if (mobile.length() < 6 || mobile.length() > 13) {
                    mTi_register_mobile.setError("Enter valid mobile number.");
                    mEdt_register_mobile.requestFocus();
                } else if (city.isEmpty()) {
                    mTi_register_city.setError("City is required.");
                    mEdt_register_city.requestFocus();
                } else if (Validation.isEmptyEdittext(mEdt_register_password)) {
                    mTi_register_password.setError("Password is required.");
                    mEdt_register_password.requestFocus();
                } else if (password.length() < 6) {
                    mTi_register_password.setError("Min 6 characters required.");
                    mEdt_register_password.requestFocus();
                } else if (commonMethods.knowInternetOn(getActivity())) {
                    SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
                    editor.putString(commonVariables.KEY_CACHE_EMAIL, email);
                    editor.apply();
                    APIs.SellerBuyerRegister((AppCompatActivity) getActivity(), this, name, email, password, mobile, city, company, strDeviceUUID, stType);
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
                    if (resultId == 2) {
                        SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
                        editor.putString(commonVariables.KEY_USER_ID_FOR_OTP, mEdt_register_email.getText().toString().trim());
                        editor.apply();

                        Intent intent = new Intent(getActivity(), OTPRegisterActivity.class);
                        intent.putExtra(commonVariables.KEY_USER_TYPE, stType);
                        intent.putExtra(commonVariables.KEY_FOR_LOGIN, ((LoginRegisterActivity) getActivity()).isForLogin);
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition(0, 0);

                    } else {
                        if (resultId == 1) {
                            Runnable listenerPos = () -> {
                                try {
                                    ((LoginRegisterActivity) getActivity()).loginFragment.mEdtUsername.setText(mEdt_register_email.getText().toString().trim());
                                    ((LoginRegisterActivity) getActivity()).viewPager.setCurrentItem(0);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            };
                            AlertDialogManager.showDialogCustom((AppCompatActivity) getActivity(), result, "Login", "Cancel", listenerPos, null);

                        } else {
                            AlertDialogManager.showDialog((AppCompatActivity) getActivity(), result, null);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
