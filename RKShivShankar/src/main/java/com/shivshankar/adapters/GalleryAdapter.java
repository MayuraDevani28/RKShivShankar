package com.shivshankar.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.liuguangqiang.progressbar.CircleProgressBar;
import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.shivshankar.R;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.classes.SC3Object;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonVariables;
import com.shivshankar.viewpager.ViewPagerActivity;

import org.json.JSONObject;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> implements OnResult {

    AppCompatActivity activity;
    ArrayList<SC3Object> listImages;

    Dialog dialog;
    private EditText mTv_Brand, mTv_Category, mTv_Type, mTv_Price, mTv_Min_Qty, mTv_Product_Code;
    LinearLayout mLl_fabric;
    private int mRowIndex = -1;


    public GalleryAdapter(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gallery, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            holder.imageView.setVisibility(View.VISIBLE);
            String strImageURL = listImages.get(position).getImageURL();
            if ((strImageURL != null) && (!strImageURL.equals(""))) {
                Glide.with(activity)
                        .load(strImageURL)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//.asBitmap()
                        .priority(Priority.IMMEDIATE).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        try {
                            holder.imageView.setBackgroundColor(Color.parseColor(listImages.get(position).getIFSCCode()));
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
                        .into(holder.imageView);

            } else {
                holder.imageView.setBackgroundColor(Color.parseColor(listImages.get(position).getIFSCCode()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (listImages != null)
            return listImages.size();
        else
            return 0;
    }

    public void setData(ArrayList<SC3Object> data) {
        if (listImages != data) {
            listImages = data;
            notifyDataSetChanged();
        }
    }

    public void setRowIndex(int index) {
        mRowIndex = index;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image_gallery);
            imageView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            SC3Object order = listImages.get(getAdapterPosition());
            showPopupFabric(order.getImageURL(), order.getId() + "",order.getIFSCCode());
        }
    }


    private void showPopupFabric(String strImageURL, String productId,String hexCode) {
        try {
            dialog = new Dialog(
                    activity, R.style.popupTheme);
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_popup_product_detail, null);


            dialog.setContentView(view);
            dialog.setCancelable(true);
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.show();
            CircleProgressBar progressBar = (CircleProgressBar) view.findViewById(R.id.progressbar1);
            SwipeBackLayout swipeBackLayout = (SwipeBackLayout) view.findViewById(R.id.swipe_layout);
            swipeBackLayout.setOnSwipeBackListener(new SwipeBackLayout.SwipeBackListener() {
                @Override
                public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
                    progressBar.setProgress((int) (progressBar.getMax() * fractionAnchor));
                    if (progressBar.getMax() * fractionAnchor == 100)
                        dialog.dismiss();
                }
            });
            RelativeLayout close = (RelativeLayout) view.findViewById(R.id.rl_close);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_gallery);
            mTv_Brand = (EditText) view.findViewById(R.id.tv_brand_name);
            mTv_Product_Code = (EditText) view.findViewById(R.id.tv_product_code);
            mTv_Category = (EditText) view.findViewById(R.id.tv_category);
            mTv_Type = (EditText) view.findViewById(R.id.tv_type);
            mTv_Price = (EditText) view.findViewById(R.id.tv_price);
            view.findViewById(R.id.ti_qty).setVisibility(View.GONE);

            mLl_fabric = (LinearLayout) view.findViewById(R.id.ll_fabric);
            mLl_fabric.setVisibility(View.GONE);
            String[] Images = {strImageURL};
//            Glide.with(activity).load(imageName).diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .error(R.drawable.no_img_big).into(imageView);
            if ((strImageURL != null) && (!strImageURL.equals(""))) {

                Glide.with(activity).load(strImageURL).diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.IMMEDIATE)
                        .error(R.drawable.no_img_big)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                try {
                                    imageView.setBackgroundColor(Color.parseColor(hexCode));
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(imageView);

            } else {
                imageView.setBackgroundColor(Color.parseColor(hexCode));
            }

            APIs.GetProductDetail_Fabric(activity, this, productId);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, ViewPagerActivity.class);
                    i.putExtra(commonVariables.INTENT_EXTRA_LIST_IMAGE_ARRAY, Images);
                    i.putExtra(commonVariables.INTENT_EXTRA_POSITION, 0);
                    i.putExtra(commonVariables.KEY_IS_LANDSCAPE, false);
                    activity.startActivity(i);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResult(JSONObject jobjWhole) {
        if (jobjWhole != null) {
            try {
                String strApiName = jobjWhole.optString("api");
                if (strApiName.equalsIgnoreCase("GetProductDetail_Fabric")) {
                    JSONObject job = jobjWhole.optJSONObject("resData");
                    mTv_Product_Code.setText(job.optString("ProductCode"));
                    mTv_Brand.setText(job.optString("BrandName"));
                    mTv_Category.setText(job.optString("CategoryName"));
                    mTv_Type.setText(job.optString("FabricType"));
                    mTv_Price.setText(commonVariables.strCurrency_name + " " + job.optString("OfferPrice")+"/mtr");
//                    mTv_Min_Qty.setText("" + job.optInt("MinOrderQty"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}