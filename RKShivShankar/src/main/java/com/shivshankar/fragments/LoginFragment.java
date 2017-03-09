package com.shivshankar.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.shivshankar.R;
import com.shivshankar.adapters.GalleryAdapter;

import java.util.ArrayList;

public class LoginFragment extends Fragment {

    RecyclerView recyclerView;
    boolean isClick;
    ArrayList<String> listImages;

    private GalleryAdapter galleryListAdapter;

    public LoginFragment() {
    }

    public LoginFragment(boolean isclick) {
        isClick = isclick;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
