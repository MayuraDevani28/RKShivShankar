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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.liuguangqiang.progressbar.CircleProgressBar;
import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.shivshankar.FabricColorsActivityBuyer;
import com.shivshankar.R;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.classes.ProductItem;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonVariables;
import com.shivshankar.viewpager.ViewPagerActivity;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FabricProductsAdapterBuyer extends RecyclerView.Adapter<FabricProductsAdapterBuyer.MyViewHolder> implements OnResult {

    private final AppCompatActivity activity;
    private final ArrayList<ProductItem> list;
    String stType = "";

    String catId;
    Dialog dialog;
    private EditText mTv_Brand, mTv_Category, mTv_Type, mTv_Price,  mTv_Product_Code;
    LinearLayout mLl_fabric;
    private String strLargeImageName;
    private ImageView imageView;

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

        private ImageView mIv_product_image;
        private TextView mTv_info, mTv_product_name;
        RelativeLayout mRv_checked;
        FrameLayout mLl_whole;

        public MyViewHolder(View itemView) {
            super(itemView);
            try {
                mTv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
                mIv_product_image = (ImageView) itemView.findViewById(R.id.iv_product_image);
                mIv_product_image.setOnClickListener(this);
                mLl_whole = (FrameLayout) itemView.findViewById(R.id.ll_whole);
                mRv_checked = (RelativeLayout) itemView.findViewById(R.id.rv_checked);
                mTv_info = (TextView) itemView.findViewById(R.id.tv_info);
                mTv_info.setOnClickListener(this);
//                mTv_info.setVisibility(View.GONE);
//                itemView.findViewById(R.id.tv_white).setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View view) {
            try {
                ProductItem item = list.get(getAdapterPosition());
                if (view == mIv_product_image) {
                    Intent intent = new Intent(activity, FabricColorsActivityBuyer.class);
                    intent.putExtra(commonVariables.KEY_FABRIC_TYPE, stType);
                    intent.putExtra(commonVariables.KEY_BRAND, item);
                    intent.putExtra(commonVariables.KEY_CATEGORY, catId);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(0, 0);
                } else if (view == mTv_info) {
                    showPopupFabric(item.getImageName(), item.getProductId() + "", "#FFFFFF");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showPopupFabric(String strImageURL, String productId, String hexCode) {
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
            imageView = (ImageView) view.findViewById(R.id.image_gallery);
            mTv_Brand = (EditText) view.findViewById(R.id.tv_brand_name);
            mTv_Product_Code = (EditText) view.findViewById(R.id.tv_product_code);
            mTv_Category = (EditText) view.findViewById(R.id.tv_category);
            mTv_Type = (EditText) view.findViewById(R.id.tv_type);
            mTv_Price = (EditText) view.findViewById(R.id.tv_price);
            view.findViewById(R.id.ti_qty).setVisibility(View.GONE);

            mLl_fabric = (LinearLayout) view.findViewById(R.id.ll_fabric);
            mLl_fabric.setVisibility(View.GONE);

            //String[] Images = {strImageURL};
//            Glide.with(activity).load(strImageURL).diskCacheStrategy(DiskCacheStrategy.ALL)
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


        } catch (Exception e) {
            e.printStackTrace();
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
            if ((strImageURL != null) && (!strImageURL.equals(""))) {
                Glide.with(activity)
                        .load(strImageURL)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.IMMEDIATE)
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
                    mTv_Price.setText(commonVariables.strCurrency_name + " " + job.optString("OfferPrice") + "/mtr");
                    strLargeImageName = job.optString("LargeImageName");

                    String[] Images = {strLargeImageName};
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

//                    mTv_Min_Qty.setText("" + job.optInt("MinOrderQty"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}