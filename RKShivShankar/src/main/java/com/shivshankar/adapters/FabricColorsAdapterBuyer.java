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
//        implements OnResult
{

    private final FabricColorsActivityBuyer activity;
    private final ArrayList<FabricColor> list;
    private static int posit;

//    Dialog dialog;
//    private EditText mTv_Brand, mTv_Category, mTv_Type, mTv_Price, mTv_Min_Qty, mTv_Product_Code;
//    LinearLayout mLl_fabric;
//    private int mRowIndex = -1;

    public FabricColorsAdapterBuyer(FabricColorsActivityBuyer activity, ArrayList<FabricColor> list) {
        this.activity = activity;
        this.list = list;
    }

    public List<FabricColor> getItems() {
        return list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mIv_product_image;
//        private TextView mTv_product_name, mTv_info;
        private RelativeLayout mRv_checked;
        private FrameLayout mLl_whole;

        public MyViewHolder(View itemView) {
            super(itemView);
//            mTv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
            mIv_product_image = (ImageView) itemView.findViewById(R.id.iv_product_image);
            mLl_whole = (FrameLayout) itemView.findViewById(R.id.ll_whole);
            mLl_whole.setOnClickListener(this);
            mRv_checked = (RelativeLayout) itemView.findViewById(R.id.rv_checked);
            mRv_checked.setOnClickListener(this);
//            mTv_info = (TextView) itemView.findViewById(R.id.tv_info);
//            mTv_info.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            try {
                posit = getAdapterPosition();
                FabricColor product = list.get(posit);
                if (view == mRv_checked || view == mLl_whole) {
                    boolean isActive = product.isActive();
//                if (product.isActive()) {
//                    product.setActive(false);
//                } else {
//                    product.setActive(true);
//                }
//                notifyItemChanged(posit);


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
//                else if (view == mTv_info) {
//                    showPopupFabric(product.getColorImage(), product.getProductId() + "", product.getHeaxCode());
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    private void showPopupFabric(String strImageURL, String productId, String hexCode) {
//        try {
//            dialog = new Dialog(
//                    activity, R.style.popupTheme);
//            LayoutInflater inflater = (LayoutInflater) activity
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View view = inflater.inflate(R.layout.dialog_popup_product_detail, null);
//
//
//            dialog.setContentView(view);
//            dialog.setCancelable(true);
//            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//            dialog.show();
//            ImageView close = (ImageView) view.findViewById(R.id.iv_close);
//            ImageView imageView = (ImageView) view.findViewById(R.id.image_gallery);
//            mTv_Brand = (EditText) view.findViewById(R.id.tv_brand_name);
//            mTv_Product_Code = (EditText) view.findViewById(R.id.tv_product_code);
//            mTv_Category = (EditText) view.findViewById(R.id.tv_category);
//            mTv_Type = (EditText) view.findViewById(R.id.tv_type);
//            mTv_Price = (EditText) view.findViewById(R.id.tv_price);
//            view.findViewById(R.id.ti_qty).setVisibility(View.GONE);
//
//            mLl_fabric = (LinearLayout) view.findViewById(R.id.ll_fabric);
//            mLl_fabric.setVisibility(View.GONE);
//
//            String[] Images = {strImageURL};
////            Glide.with(activity).load(strImageURL).diskCacheStrategy(DiskCacheStrategy.ALL)
////                    .error(R.drawable.no_img_big).into(imageView);
//
//            if ((strImageURL != null) && (!strImageURL.equals(""))) {
//
//                Glide.with(activity).load(strImageURL).diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.IMMEDIATE)
//                        .error(R.drawable.no_img_big)
//                        .listener(new RequestListener<String, GlideDrawable>() {
//                            @Override
//                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                try {
//                                    imageView.setBackgroundColor(Color.parseColor(hexCode));
//                                } catch (Exception e1) {
//                                    e1.printStackTrace();
//                                }
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                return false;
//                            }
//                        })
//                        .into(imageView);
//
//            } else {
//                imageView.setBackgroundColor(Color.parseColor(hexCode));
//            }
//
//
//            APIs.GetProductDetail_Fabric(activity, this, productId);
//            close.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog.dismiss();
//                }
//            });
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(activity, ViewPagerActivity.class);
//                    i.putExtra(commonVariables.INTENT_EXTRA_LIST_IMAGE_ARRAY, Images);
//                    i.putExtra(commonVariables.INTENT_EXTRA_POSITION, 0);
//                    i.putExtra(commonVariables.KEY_IS_LANDSCAPE, false);
//                    activity.startActivity(i);
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_row_fabric_color_buyer, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final FabricColor item = list.get(position);
        try {
//            holder.mTv_product_name.setSelected(true);
//            holder.mTv_product_name.setText(WordUtils.capitalizeFully(item.getColorName()));
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


//    @Override
//    public void onResult(JSONObject jobjWhole) {
//        if (jobjWhole != null) {
//            try {
//                String strApiName = jobjWhole.optString("api");
//                if (strApiName.equalsIgnoreCase("GetProductDetail_Fabric")) {
//                    JSONObject job = jobjWhole.optJSONObject("resData");
//                    mTv_Product_Code.setText(job.optString("ProductCode"));
//                    mTv_Brand.setText(job.optString("BrandName"));
//                    mTv_Category.setText(job.optString("CategoryName"));
//                    mTv_Type.setText(job.optString("FabricType"));
//                    mTv_Price.setText(commonVariables.strCurrency_name + " " + job.optString("OfferPrice") + "/mtr");
////                    mTv_Min_Qty.setText("" + job.optInt("MinOrderQty"));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
}