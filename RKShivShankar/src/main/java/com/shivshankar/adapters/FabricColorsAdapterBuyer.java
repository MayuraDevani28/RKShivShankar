package com.shivshankar.adapters;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.shivshankar.R;
import com.shivshankar.classes.FabricColor;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressLint({"NewApi", "ResourceAsColor"})
public class FabricColorsAdapterBuyer extends RecyclerView.Adapter<FabricColorsAdapterBuyer.MyViewHolder> {

    private final AppCompatActivity activity;
    private final ArrayList<FabricColor> list;
    private static int posit;


    public FabricColorsAdapterBuyer(AppCompatActivity activity, ArrayList<FabricColor> list) {
        this.activity = activity;
        this.list = list;
    }

    public List<FabricColor> getItems() {
        return list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mIv_product_image;
        private TextView mTv_product_name;
        RelativeLayout mRv_checked;
        FrameLayout mLl_whole;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
            mIv_product_image = (ImageView) itemView.findViewById(R.id.iv_product_image);
            mLl_whole = (FrameLayout) itemView.findViewById(R.id.ll_whole);
            mLl_whole.setOnClickListener(this);
            mRv_checked = (RelativeLayout) itemView.findViewById(R.id.rv_checked);
            mRv_checked.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            try {
                posit = getAdapterPosition();
                FabricColor product = list.get(posit);

                if (product.isActive()) {
                    product.setActive(false);
                } else
                    product.setActive(true);
                notifyItemChanged(posit);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_row_fabric_color_buyer, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final FabricColor item = list.get(position);
        try {
            holder.mTv_product_name.setSelected(true);
            holder.mTv_product_name.setText(WordUtils.capitalizeFully(item.getColorName()));
            String strImageURL = item.getColorImage();
            if ((strImageURL != null) && (!strImageURL.equals(""))) {
                Glide.with(activity)
                        .load(strImageURL)
                        .asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.IMMEDIATE).dontAnimate().listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        try {
                            String color = item.getHeaxCode();
                            if (color.length() == 4) {
                                color = color + color.substring(1, 4);
                            }
                            holder.mIv_product_image.setBackgroundColor(Color.parseColor(color));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                }).thumbnail(0.1f).error(R.drawable.no_img)
                        .override(200, 200).into(holder.mIv_product_image);

            } else {
                String color = item.getHeaxCode();
                if (color.length() == 4) {
                    color = color + color.substring(1, 4);
                }
                holder.mIv_product_image.setBackgroundColor(Color.parseColor(color));
            }

            if (item.isActive())
                holder.mRv_checked.setVisibility(View.VISIBLE);
            else
                holder.mRv_checked.setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean isOneChecked() {
        boolean isOneChecked = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isActive()) {
                isOneChecked = true;
                break;
            }
        }
        return isOneChecked;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


}