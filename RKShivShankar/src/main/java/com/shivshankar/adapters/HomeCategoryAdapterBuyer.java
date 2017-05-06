package com.shivshankar.adapters;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.shivshankar.BrandsActivityBuyer;
import com.shivshankar.R;
import com.shivshankar.classes.Brand;
import com.shivshankar.utills.commonVariables;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;

public class HomeCategoryAdapterBuyer extends RecyclerView.Adapter<HomeCategoryAdapterBuyer.MyViewHolder> {

    private final AppCompatActivity activity;
    private final ArrayList<Brand> list;

    public HomeCategoryAdapterBuyer(AppCompatActivity activity, ArrayList<Brand> list) {
        this.activity = activity;
        this.list = list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        FrameLayout mLl_top;
        LinearLayout mLlGroup,mLl_top2;
        private ImageView mIv_image, mIv_down;
        private TextView mTv_name, mTvDyed, mTvPrinted;
//        private RadioGroup mRadioGroup;

        public MyViewHolder(View itemView) {
            super(itemView);
            try {
                mLl_top = (FrameLayout) itemView.findViewById(R.id.ll_top);
                mLl_top2 = (LinearLayout) itemView.findViewById(R.id.ll_top2);
                mIv_image = (ImageView) itemView.findViewById(R.id.iv_image);
                mTv_name = (TextView) itemView.findViewById(R.id.tv_name);
                mIv_down = (ImageView) itemView.findViewById(R.id.iv_down);
                mTvDyed = (TextView) itemView.findViewById(R.id.tvDyed);
                mTvPrinted = (TextView) itemView.findViewById(R.id.tvPrinted);
                mLlGroup = (LinearLayout) itemView.findViewById(R.id.llGroup);

                mLl_top.setOnClickListener(this);
                mLl_top2.setOnClickListener(this);
                mTvDyed.setOnClickListener(this);
                mTvPrinted.setOnClickListener(this);

//                mRadioGroup = (RadioGroup) itemView.findViewById(R.id.radioGroup);
//                mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
//                        try {
//                            if (checkedId != -1) {
//                                String stType = "";
//                                if (checkedId == R.id.radioDyed) {
//                                    stType = "dyed";
//                                } else if (checkedId == R.id.radioPrinted) {
//                                    stType = "printed";
//                                }
//                                Intent intent = new Intent(activity, BrandsActivityBuyer.class);
//                                intent.putExtra(commonVariables.KEY_FABRIC_TYPE, stType);
//                                Gson gson = new Gson();
//                                Brand item = list.get(getLayoutPosition());
//                                String json = gson.toJson(item);
//
//                                intent.putExtra(commonVariables.KEY_CATEGORY, json);
//
//                                activity.startActivity(intent);
//                                activity.overridePendingTransition(0, 0);
////                                radioGroup.clearCheck();
////                                mRadioGroup.setVisibility(View.GONE);
////                                mIv_down.setRotation(0);
//                                radioGroup.clearCheck();
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View view) {
            try {
                if (view == mLl_top || view == mLl_top2) {
                    if (mLlGroup.getVisibility() == View.VISIBLE) {
                        mLlGroup.setVisibility(View.GONE);
                        mIv_down.setRotation(0);
                    } else {
                        mLlGroup.setVisibility(View.VISIBLE);
                        mIv_down.setRotation(180);
                    }
                } else if (view == mTvDyed) {
                    click(getLayoutPosition(), "dyed");
                } else if (view == mTvPrinted) {
                    click(getLayoutPosition(), "printed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void click(int layoutPosition, String stType) {

        try {
            Intent intent = new Intent(activity, BrandsActivityBuyer.class);
            intent.putExtra(commonVariables.KEY_FABRIC_TYPE, stType);
            Gson gson = new Gson();
            Brand item = list.get(layoutPosition);
            String json = gson.toJson(item);
            intent.putExtra(commonVariables.KEY_CATEGORY, json);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_row_homecat_buyer, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        try {

            final Brand item = list.get(position);
            holder.mTv_name.setText(WordUtils.capitalizeFully(item.getBrandName()));
            String strImageURL = item.getBrandLogo();
            if ((strImageURL != null) && (!strImageURL.equals("")))
                Glide.with(activity).load(strImageURL)//.asBitmap()
                        .placeholder(R.color.gray_bg_transparent)
                        .error(R.drawable.no_img_big)
                        .into(holder.mIv_image);//.centerCrop()
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}