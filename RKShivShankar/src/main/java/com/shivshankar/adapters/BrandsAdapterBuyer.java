package com.shivshankar.adapters;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.shivshankar.ProductsActivityBuyer;
import com.shivshankar.R;
import com.shivshankar.classes.Brand;
import com.shivshankar.utills.commonVariables;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;

public class BrandsAdapterBuyer extends RecyclerView.Adapter<BrandsAdapterBuyer.MyViewHolder> {

    private final AppCompatActivity activity;
    private final ArrayList<Brand> list;
    private static int posit;
    Resources res;
    String stType = "";

    public BrandsAdapterBuyer(AppCompatActivity activity, ArrayList<Brand> list, String stType) {

        this.activity = activity;
        this.list = list;
        try {
            res = activity.getResources();
            this.stType = stType;
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
                posit = getAdapterPosition();
                Brand item = list.get(posit);
                if (view == mLl_brand) {
                    Intent intent = new Intent(activity, ProductsActivityBuyer.class);
                    intent.putExtra(commonVariables.KEY_FABRIC_TYPE, stType);
                    intent.putExtra(commonVariables.KEY_BRAND, item);
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
            int colpink = res.getColor(R.color.brand2);
            int colBrand1 = res.getColor(R.color.brand1);
            if (position % 2 == 0) {
                holder.mLl_brand.setBackgroundColor(colBrand1);
            } else
                holder.mLl_brand.setBackgroundColor(colpink);

            final Brand item = list.get(position);
            holder.mTv_name.setText(WordUtils.capitalizeFully(item.getBrandName()));
            String strImageURL = item.getBrandLogo();
            if ((strImageURL != null) && (!strImageURL.equals("")))
                Glide.with(activity).load(strImageURL).asBitmap()
                        .placeholder(R.drawable.xml_round_gray).error(R.drawable.xml_round_white)
                        .centerCrop().into(new BitmapImageViewTarget(holder.mIv_imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.mIv_imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

}