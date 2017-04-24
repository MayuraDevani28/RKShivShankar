package com.shivshankar.fragments;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.shivshankar.R;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.adapters.FabricColorsAdapterBuyer;
import com.shivshankar.classes.FabricColor;
import com.shivshankar.classes.ProductItem;
import com.shivshankar.classes.SC3Object;
import com.shivshankar.utills.OnResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FabricColorsListFragment extends Fragment implements OnResult ,View.OnClickListener {

    RecyclerView recyclerView;

    private FabricColorsAdapterBuyer galleryListAdapter;
    SC3Object fabricColor;
    ProductItem productItem;
    int pageNo = 1;
    String sortBy = "", searchText = "";
    ArrayList<FabricColor> listColor = new ArrayList<>();
    LinearLayout mLl_no_data_found;
    Button mBtn_add_now;
    LottieAnimationView animationView2, animationView;

    public FabricColorsListFragment(SC3Object FabricColor, ProductItem item) {
        fabricColor = FabricColor;
        productItem = item;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fabriccolorlist, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_color);
        mLl_no_data_found = (LinearLayout) rootView.findViewById(R.id.ll_no_data_found);
        animationView = (LottieAnimationView) rootView.findViewById(R.id.animation_view);
        animationView2 = (LottieAnimationView) rootView.findViewById(R.id.animation_view2);
        mBtn_add_now = (Button) rootView.findViewById(R.id.btn_add_now);
        mBtn_add_now.setOnClickListener(this);

        APIs.GetProduct_ColorList_Fabric(null, this, productItem.getProductId(), fabricColor.getId(), pageNo, sortBy, searchText);
        return rootView;
    }


    public void setData(ArrayList<FabricColor> listImages) {
        try {
            if (listImages.size() != 0) {
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                recyclerView.setHasFixedSize(true);
                recyclerView.setNestedScrollingEnabled(false);
                galleryListAdapter = new FabricColorsAdapterBuyer((AppCompatActivity) getActivity(), listImages);
                recyclerView.setAdapter(galleryListAdapter);
                recyclerView.setVisibility(View.VISIBLE);
                mLl_no_data_found.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.GONE);
                mLl_no_data_found.setVisibility(View.VISIBLE);
                startAnim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startAnim() {
        animationView.setProgress(0f);
        animationView.playAnimation();
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animationView2.setProgress(0f);
                animationView2.playAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animationView2.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animationView.setProgress(0f);
                animationView.playAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();

        try {
            finishAnim();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void finishAnim() {
        try {
            animationView.cancelAnimation();
            animationView2.cancelAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onResult(JSONObject jobjWhole) {
        try {
            if (jobjWhole != null) {

                String strApiName = jobjWhole.optString("api");
                if (strApiName.equalsIgnoreCase("GetProduct_ColorList_Fabric")) {
                    JSONObject job = jobjWhole.optJSONObject("resData");
                    JSONArray jarray = job.optJSONArray("Data");
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jo = jarray.optJSONObject(i);
                        listColor.add(new FabricColor(jo.optInt("ColorId"), jo.optString("ProductId"), jo.optString("ColorName"), jo.optString("HeaxCode"), jo.optString("ColorImage"), false));
                    }
                    setData(listColor);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        try {
            if (view == mBtn_add_now) {
               getActivity().onBackPressed();
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<FabricColor> getItems() {
        return listColor;
    }
}
