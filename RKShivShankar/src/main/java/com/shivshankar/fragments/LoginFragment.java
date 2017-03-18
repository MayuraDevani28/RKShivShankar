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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.shivshankar.MainActivitySeller;
import com.shivshankar.R;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.Validation;
import com.shivshankar.utills.commonVariables;

import org.json.JSONObject;

public class LoginFragment extends Fragment implements View.OnClickListener, OnResult {

    private Button btnLogin;
    private EditText mEdtUsername, mEdtPassword;
    TextView mTv_forget;
    private String strUserId, stPassword;
    AlertDialog dialog;
    //    private RadioGroup radioGroup;
    public String stType;
    String regId, strDeviceUUID = commonVariables.uuid;
    Context context;

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
        mTv_forget = (TextView) v.findViewById(R.id.tv_forget);
        mTv_forget.setOnClickListener(this);
//        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);

        btnLogin = (Button) v.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        String strCacheEmail = AppPreferences.getPrefs().getString(commonVariables.KEY_CACHE_EMAIL, "");
        if (!strCacheEmail.isEmpty()) {
            mEdtUsername.setText(strCacheEmail);
            mEdtUsername.setSelection(strCacheEmail.length());
        }

//        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
//            if (checkedId == R.id.radioBuyer) {
//                stType = "BUYER";
//            } else if (checkedId == R.id.radioSeller) {
//                stType = "SELLER";
//            }
//        });

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {

        if (v == btnLogin) {
            login();
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
                        APIs.SellerForgotPassward((AppCompatActivity) getActivity(), LoginFragment.this, strUserId);
                    }
                }
            });
            dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.black));


        }
    }

    private void login() {
        strUserId = mEdtUsername.getText().toString().trim();
        stPassword = mEdtPassword.getText().toString().trim();
        AppPreferences.getPrefs().edit().putString(commonVariables.KEY_CACHE_EMAIL, strUserId).apply();

        if (Validation.isEmptyEdittext(mEdtUsername) && Validation.isEmptyEdittext(mEdtPassword)) {
            mEdtUsername.setError("Enter Username");
            mEdtPassword.setError("Enter Password");
        } else if (Validation.isEmptyEdittext(mEdtUsername)) {
            mEdtUsername.setError("Enter Username");
        } else if (Validation.isEmptyEdittext(mEdtPassword)) {
            mEdtPassword.setError("Enter Password");
        } else {
            String strModelName = Build.MODEL;
            String strOSVersion = Build.VERSION.RELEASE;
            String strDeviceType = "Android" + strDeviceUUID;
            //FirebaseInstanceId.getInstance().getToken()
            APIs.SellerLogin((AppCompatActivity) getActivity(), this, strUserId, stPassword, strDeviceType, strDeviceUUID, strModelName, strOSVersion, regId);
        }
    }

    @Override
    public void onResult(JSONObject jobj) {
        try {
            if (jobj != null) {
                String strApiName = jobj.optString("api");
                String result = jobj.optString("res");
                if (strApiName.equalsIgnoreCase("SellerLogin")) {
                    int resultId = jobj.optInt("resInt");
                    if (resultId != 0) {
                        SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
                        editor.putBoolean(commonVariables.KEY_IS_LOG_IN, true);
                        editor.putString(commonVariables.KEY_LOGIN_ID, jobj.optString("sellerId"));
                        editor.putString(commonVariables.KEY_LOGIN_SELLER_PROFILE, jobj.optJSONObject("resData").toString());
                        editor.apply();

                        Intent i = new Intent(getActivity(), MainActivitySeller.class);
                        getActivity().finish();
                        getActivity().startActivity(i);
                        getActivity().overridePendingTransition(0, 0);
                    } else {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                        builder.setTitle(commonVariables.appname);
                        builder.setMessage(result);
                        builder.setPositiveButton("Ok", null);
                        builder.show();
                    }
                } else if (strApiName.equalsIgnoreCase("SellerForgotPassward")) {
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
