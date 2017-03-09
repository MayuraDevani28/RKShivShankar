package com.shivshankar.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shivshankar.R;
import com.shivshankar.adapters.GalleryAdapter;

import java.util.ArrayList;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    RecyclerView recyclerView;
    boolean isClick;
    ArrayList<String> listImages;

    private GalleryAdapter galleryListAdapter;
    private Button btnRegister;

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
        btnRegister = (Button)v.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onClick(View v) {

    }
}
