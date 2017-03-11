package com.shivshankar.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.shivshankar.MainActivitySeller;
import com.shivshankar.R;
import com.shivshankar.adapters.GalleryAdapter;
import com.shivshankar.utills.Validation;

import java.util.ArrayList;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    RecyclerView recyclerView;
    boolean isClick;
    ArrayList<String> listImages;

    private GalleryAdapter galleryListAdapter;
    private Button btnRegister;
    private EditText edtOtp;
    private EditText edtFullname;
    private EditText edtMail;
    private EditText edtPassword;
    private EditText edtMobile;
    private RadioGroup radioGroup;
    private String stType;
    private String stFullname;
    private String stMail;
    private String stPassword;
    private String stMobile;
    private String stOtp;
    private Intent i;

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
            init(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
    private void init(View v) {
        edtFullname = (EditText)v.findViewById(R.id.edt_full_name);
        edtMail = (EditText)v.findViewById(R.id.edt_email);
        edtPassword = (EditText)v.findViewById(R.id.edt_password);
        edtMobile = (EditText)v.findViewById(R.id.edt_mobile);
        edtOtp = (EditText)v.findViewById(R.id.edt_otp);
        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        btnRegister = (Button)v.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);
        edtOtp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (edtOtp.getRight() - edtOtp.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        Toast.makeText(getActivity(),"Resend",Toast.LENGTH_LONG).show();
                        return true;
                    }
                }
                return false;
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {




            @Override

            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // find which radio button is selected

                if(checkedId == R.id.radioBuyer) {

                    stType = "BUYER";
                } else if(checkedId == R.id.radioSeller) {

                    stType = "SELLER";

                }

            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onClick(View v) {
        if(v == btnRegister){
            stFullname = edtFullname.getText().toString();
            stMail = edtMail.getText().toString();
            stPassword = edtPassword.getText().toString();
            stMobile = edtMobile.getText().toString();
            stOtp = edtOtp.getText().toString();
            if(Validation.isEmptyEdittext(edtFullname) && Validation.isEmptyEdittext(edtMail) && Validation.isEmptyEdittext(edtPassword) && Validation.isEmptyEdittext(edtMobile) && Validation.isEmptyEdittext(edtOtp)){
                edtFullname.setError("Enter Fullname");
                edtMail.setError("Enter Mail Address");
                edtPassword.setError("Enter Password");
                edtMobile.setError("Enter Mobile Number");
                edtOtp.setError("Enter OTP");
            }else if(Validation.isEmptyEdittext(edtFullname)){
                edtFullname.setError("Enter Fullname");
            }else if(Validation.isEmptyEdittext(edtMail)){
                edtMail.setError("Enter Mail Address");
            }else if(Validation.isEmptyEdittext(edtPassword)){
                edtPassword.setError("Enter Password");
            }else if(Validation.isEmptyEdittext(edtMobile)){
                edtMobile.setError("Enter Mobile Number");
            }else if(Validation.isEmptyEdittext(edtOtp)){
                edtOtp.setError("Enter OTP");
            }else if(Validation.isValidEmail(stMail)){
                edtMail.setError("Enter Valid Mail Address");
            }
            else  if(stMobile.length() < 7){
                edtMail.setError("Enter Valid Password");
            }
            else {
                i = new Intent(getActivity(), MainActivitySeller.class);
                getActivity().finish();
                startActivity(i);
            }

        }

    }
}
