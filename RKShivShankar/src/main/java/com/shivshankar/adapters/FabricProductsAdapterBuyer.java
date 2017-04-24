package com.shivshankar.adapters;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shivshankar.FabricColorsActivityBuyer;
import com.shivshankar.R;
import com.shivshankar.classes.ProductItem;
import com.shivshankar.utills.commonVariables;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

public class FabricProductsAdapterBuyer extends RecyclerView.Adapter<FabricProductsAdapterBuyer.MyViewHolder> {

    private final AppCompatActivity activity;
    private final ArrayList<ProductItem> list;
    String stType = "";
    String catId;

    public FabricProductsAdapterBuyer(AppCompatActivity activity, ArrayList<ProductItem> list, String stType, String id) {
        this.activity = activity;
        this.list = list;
        this.stType = stType;
        catId = id;
    }

    public List<ProductItem> getItems() {
        return list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mTv_product_info;
        private ImageView mIv_product_image;
        private TextView mTv_product_name;
        RelativeLayout mRv_checked;
        FrameLayout mLl_whole;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
            mIv_product_image = (ImageView) itemView.findViewById(R.id.iv_product_image);
            mIv_product_image.setOnClickListener(this);
            mLl_whole = (FrameLayout) itemView.findViewById(R.id.ll_whole);
            mRv_checked = (RelativeLayout) itemView.findViewById(R.id.rv_checked);
            mTv_product_info = (TextView) itemView.findViewById(R.id.tv_info);
            mTv_product_info.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View view) {
            try {
                ProductItem item = list.get(getAdapterPosition());

                Intent intent = new Intent(activity, FabricColorsActivityBuyer.class);
                intent.putExtra(commonVariables.KEY_FABRIC_TYPE, stType);
                intent.putExtra(commonVariables.KEY_BRAND, item);
                intent.putExtra(commonVariables.KEY_CATEGORY, catId);
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_row_product_item_buyer, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ProductItem item = list.get(position);
        try {
            holder.mTv_product_name.setSelected(true);
            holder.mTv_product_name.setText(WordUtils.capitalizeFully(item.getProductCode()));
            String strImageURL = item.getImageName();
            Log.v("TAGRK", "ImageURL: " + strImageURL);
            if ((strImageURL != null) && (!strImageURL.equals(""))) {
                Glide.with(activity)
                        .load(strImageURL)
                        .asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.IMMEDIATE).dontAnimate()
                        .thumbnail(0.1f).override(200, 200)
                        .error(R.drawable.no_img)
                        .into(holder.mIv_product_image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}