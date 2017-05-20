package com.shivshankar.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.shivshankar.FabricColorsActivityBuyer;
import com.shivshankar.R;
import com.shivshankar.classes.FabricColor;
import com.shivshankar.fragments.FabricColorsListFragment;

import java.util.ArrayList;
import java.util.List;

@SuppressLint({"NewApi", "ResourceAsColor"})
public class FabricColorsAdapterBuyer extends RecyclerView.Adapter<FabricColorsAdapterBuyer.MyViewHolder>
{

    private final FabricColorsActivityBuyer activity;
    private final ArrayList<FabricColor> list;

    public FabricColorsAdapterBuyer(FabricColorsActivityBuyer activity, ArrayList<FabricColor> list) {
        this.activity = activity;
        this.list = list;
    }

    public List<FabricColor> getItems() {
        return list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mIv_product_image;
        private RelativeLayout mRv_checked;
        private FrameLayout mLl_whole;

        public MyViewHolder(View itemView) {
            super(itemView);
            mIv_product_image = (ImageView) itemView.findViewById(R.id.iv_product_image);
            mLl_whole = (FrameLayout) itemView.findViewById(R.id.ll_whole);
            mLl_whole.setOnClickListener(this);
            mRv_checked = (RelativeLayout) itemView.findViewById(R.id.rv_checked);
            mRv_checked.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            try {
                FabricColor product = list.get( getAdapterPosition());
                if (view == mRv_checked || view == mLl_whole) {
                    boolean isActive = product.isActive();

                    for (int i = 0; i < activity.listFragment.size(); i++) {
                        FabricColorsListFragment fragmentList = activity.listFragment.get(i);
                        List<FabricColor> lisarr = fragmentList.getItems();
                        for (int j = 0; j < lisarr.size(); j++) {
                            if (lisarr.get(j).getColorId() == product.getColorId()) {
                                fragmentList.galleryListAdapter.getItems().get(j).setActive(!isActive);
                                fragmentList.galleryListAdapter.notifyItemChanged(j);
                                break;
                            }
                        }
                    }
                }
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
            if (item.isActive())
                holder.mRv_checked.setVisibility(View.VISIBLE);
            else
                holder.mRv_checked.setVisibility(View.GONE);

            String strImageURL = item.getColorImage();
            if ((strImageURL != null) && (!strImageURL.equals(""))) {
                Glide.with(activity)
                        .load(strImageURL)//.asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//.dontAnimate()
                        .priority(Priority.IMMEDIATE).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        try {
                            holder.mIv_product_image.setBackgroundColor(Color.parseColor(item.getHeaxCode()));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                }).error(R.drawable.no_img)//.thumbnail(0.1f).override(200, 200)
                        .into(holder.mIv_product_image);

            } else {
                holder.mIv_product_image.setBackgroundColor(Color.parseColor(item.getHeaxCode()));
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