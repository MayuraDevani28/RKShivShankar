package com.shivshankar.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

public class LoginFragment extends Fragment implements View.OnClickListener {

    RecyclerView recyclerView;
    boolean isClick;
    ArrayList<String> listImages;
    Intent i;

    private GalleryAdapter galleryListAdapter;
    private Button btnLogin;
    private EditText edtUsername;
    private EditText edtPassword;
    private String stUsername;
    private String stPassword;
    private RadioGroup radioGroup;
    public String stType;

    public LoginFragment() {
    }

    @SuppressLint("ValidFragment")
    public LoginFragment(boolean isclick) {
        isClick = isclick;
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
        edtUsername = (EditText)v.findViewById(R.id.edt_username);
        edtPassword = (EditText)v.findViewById(R.id.edt_password);
        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);

        btnLogin = (Button)v.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

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

        if(v == btnLogin){

            stUsername = edtUsername.getText().toString();
            stPassword = edtPassword.getText().toString();
            if(Validation.isEmptyEdittext(edtUsername) && Validation.isEmptyEdittext(edtPassword)){
                edtUsername.setError("Enter Username");
                edtPassword.setError("Enter Password");
            }else if(Validation.isEmptyEdittext(edtUsername)){
                edtUsername.setError("Enter Username");
            }else if(Validation.isEmptyEdittext(edtPassword)){
                edtPassword.setError("Enter Password");
            }
            else {
                i = new Intent(getActivity(), MainActivitySeller.class);
                getActivity().finish();
                startActivity(i);
            }


        }
    }
}
