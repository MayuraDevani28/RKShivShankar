package com.shivshankar.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shivshankar.FabricProductsActivityBuyer;
import com.shivshankar.ProductsActivityBuyer;
import com.shivshankar.R;
import com.shivshankar.classes.Brand;
import com.shivshankar.utills.CircleTransform;
import com.shivshankar.utills.commonVariables;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Random;

public class BrandsAdapterBuyer extends RecyclerView.Adapter<BrandsAdapterBuyer.MyViewHolder> {

    private final AppCompatActivity activity;
    private final ArrayList<Brand> list;
    String stType = "";
    String catId;
    int d, gray;

    public BrandsAdapterBuyer(AppCompatActivity activity, ArrayList<Brand> list, String stType, String id) {

        this.activity = activity;
        this.list = list;
        try {
            this.stType = stType;
            catId = id;
            d = ContextCompat.getColor(activity, R.color.white);
            gray = ContextCompat.getColor(activity, R.color.gray_bg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mIv_imageView;
        private TextView mTv_name;
        RelativeLayout mLl_brand;

        public MyViewHolder(View itemView) {
            super(itemView);
            try {
                mTv_name = (TextView) itemView.findViewById(R.id.tv_brand_name);
                mIv_imageView = (ImageView) itemView.findViewById(R.id.iv_brand_logo);
                mLl_brand = (RelativeLayout) itemView.findViewById(R.id.ll_brand);
                mLl_brand.setOnClickListener(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View view) {
            try {
                Brand item = list.get(getAdapterPosition());
                if (view == mLl_brand) {
                    int cId = Integer.parseInt(catId);
                    Intent intent;
                    if (cId == 1) {//suit
                        intent = new Intent(activity, ProductsActivityBuyer.class);
                        intent.putExtra(commonVariables.KEY_FABRIC_TYPE, stType);
                        intent.putExtra(commonVariables.KEY_BRAND, item);
                        intent.putExtra(commonVariables.KEY_CATEGORY, catId);
                    } else {
                        intent = new Intent(activity, FabricProductsActivityBuyer.class);
                        intent.putExtra(commonVariables.KEY_FABRIC_TYPE, stType);
                        intent.putExtra(commonVariables.KEY_BRAND, item);
                        intent.putExtra(commonVariables.KEY_CATEGORY, catId);
                    }
                    activity.startActivity(intent);
                    activity.overridePendingTransition(0, 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_row_brands_buyer, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        try {
            final Brand item = list.get(position);
            holder.mTv_name.setText(WordUtils.capitalizeFully(item.getBrandName()));
            String strImageURL = item.getBrandLogo();

            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            holder.mLl_brand.setBackgroundColor(ColorUtils.setAlphaComponent(color, 99));

            if ((strImageURL != null) && (!strImageURL.equals("")))
                Glide.with(activity).load(strImageURL)//.asBitmap()
                        .placeholder(R.drawable.xml_round_gray).error(R.drawable.xml_round_white)
                        .centerCrop().transform(new CircleTransform(activity)).into(holder.mIv_imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

}