package com.shivshankar.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.shivshankar.R;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.classes.CartItem;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;
import com.shivshankar.viewpager.ViewPagerActivity;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONObject;

import java.util.ArrayList;


@SuppressLint("NewApi")
public class OrderProductListAdapter extends RecyclerView.Adapter<OrderProductListAdapter.MyViewHolder> implements OnResult {

    private AppCompatActivity activity;

    Dialog dialog;
    private EditText mTv_Brand, mTv_Top_Fabrics, mTv_Bottom_Fabrics, mTv_Dupatta, mTv_All_Fabrics, mTv_Category, mTv_Type, mTv_Price, mTv_Min_Qty, mTv_Product_Code;
    private TextInputLayout mEdt_Dupatta, mEdt_All_Fabrics;
    private LinearLayout mLL_Fabrics, mLl_fabric;
    Resources res;
    ArrayList<CartItem> listArray = new ArrayList<CartItem>();

    public OrderProductListAdapter(AppCompatActivity activity, ArrayList<CartItem> objects) {
        this.activity = activity;
        this.listArray = objects;
        res = activity.getResources();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_row_order_products_buyer, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        try {

            final CartItem item = listArray.get(position);

            holder.mTv_brand_name.setSelected(true);
            holder.mTv_brand_name.setText(WordUtils.capitalize(item.getProductCode() + " (" + item.getBrandName() + ")"));

            if (item.getSuitFbricId() == 2) {
                try {
                    holder.mLl_qty_cut.setVisibility(View.VISIBLE);
                    holder.mLl_suit.setVisibility(View.GONE);
                    holder.mLl_fabric.setVisibility(View.VISIBLE);
                    holder.mTv_total_qty.setText(item.getCartQuantity() + "");
                    holder.mTv_total_cut.setText(item.getFabricCuts() + "");
                    holder.mTv_price_fabric.setText(commonVariables.strCurrency_name + " " + item.getOfferPrice());
                    holder.mTv_total_fabric.setText(commonVariables.strCurrency_name + " " + item.getTotalAmount());
                    holder.mTv_body_type.setText(WordUtils.capitalizeFully(item.getBodyPart()));
                    if (item.getBodyPart().equalsIgnoreCase("Top")) {
                        holder.mLl_top_type.setVisibility(View.VISIBLE);
                        if (item.getFabric_FrontQty() != 0) {
                            holder.mLl_front.setVisibility(View.VISIBLE);
                            setCutQTY(holder.mTv_front, item.getFabric_FrontCut(), item.getFabric_FrontQty());
                        } else
                            holder.mLl_front.setVisibility(View.GONE);

                        if (item.getFabric_BackQty() != 0) {
                            holder.mLl_back.setVisibility(View.VISIBLE);
                            setCutQTY(holder.mTv_back, item.getFabric_BackCut(), item.getFabric_BackQty());
                        } else
                            holder.mLl_back.setVisibility(View.GONE);

                        if (item.getFabric_BajuQty() != 0) {
                            holder.mLl_baju.setVisibility(View.VISIBLE);
                            setCutQTY(holder.mTv_baju, item.getFabric_BajuCut(), item.getFabric_BajuQty());
                        } else
                            holder.mLl_baju.setVisibility(View.GONE);

                        if (item.getFabric_ExtraQty() != 0) {
                            holder.mLl_extra.setVisibility(View.VISIBLE);
                            setCutQTY(holder.mTv_extra, item.getFabric_ExtraCut(), item.getFabric_ExtraQty());
                        } else
                            holder.mLl_extra.setVisibility(View.GONE);
                    } else {
                        holder.mLl_top_type.setVisibility(View.GONE);
                    }
                    holder.mTv_total_cut.setText(item.getFabricCuts() + "");
                    holder.mTv_total_qty.setText(item.getCartQuantity() + "");

                    holder.horizontalAdapter.setData(item.getFabric_Colors()); // List of Strings
                    holder.horizontalAdapter.setRowIndex(position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                holder.mLl_suit.setVisibility(View.VISIBLE);
                holder.mLl_fabric.setVisibility(View.GONE);
                holder.mLl_qty_cut.setVisibility(View.GONE);
                holder.mTv_price.setText(commonVariables.strCurrency_name + " " + item.getOfferPrice());
                holder.mTv_total.setText(commonVariables.strCurrency_name + " " + item.getTotalAmount());
                holder.mTv_qty.setText(item.getCartQuantity() + "");
            }

            String strImageURL = item.getImageName();
            if ((strImageURL != null) && (!strImageURL.equals(""))) {
                Glide.with(activity).load(strImageURL)//.dontAnimate()//.asBitmap().approximate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//.thumbnail(0.1f)
                        .error(R.drawable.no_img)
                        .into(holder.mIv_product_image);
            }
//            if (position == listArray.size() - 1)
//                holder.view_bottom.setVisibility(View.GONE);
//            else
//                holder.view_bottom.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCutQTY(TextView mTv_front, double cut, int qty) {
        String cut2 = cut + "";
        if (cut2.endsWith(".0"))
            cut2 = cut2.substring(0, cut2.length() - 2);

        mTv_front.setText("Qty: " + qty + ", " + "Cut: " + cut2);
    }

    @Override
    public int getItemCount() {
        return listArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mIv_product_image;

        private TextView mTv_brand_name, mTv_qty, mTv_price, mTv_total, mTv_body_type, mTv_front, mTv_back, mTv_baju, mTv_extra, mTv_total_qty, mTv_total_cut, mTv_price_fabric, mTv_total_fabric;
        private LinearLayout mLl_suit, mLl_fabric, mLl_top_type, mLl_back, mLl_front, mLl_baju, mLl_extra, mLl_qty_cut;
        View view_bottom;
        RecyclerView mRv_products;
        GalleryAdapter horizontalAdapter;

        public MyViewHolder(View itemView) {
            super(itemView);
            view_bottom = itemView.findViewById(R.id.view_bottom);
            mIv_product_image = (ImageView) itemView.findViewById(R.id.iv_product_image);
            mTv_brand_name = (TextView) itemView.findViewById(R.id.tv_brand_name);
            mLl_suit = (LinearLayout) itemView.findViewById(R.id.ll_suit);
            mTv_qty = (TextView) itemView.findViewById(R.id.tv_quantity);
            mTv_price = (TextView) itemView.findViewById(R.id.tv_product_price);
            mTv_total = (TextView) itemView.findViewById(R.id.tv_sub_total);
            mLl_fabric = (LinearLayout) itemView.findViewById(R.id.ll_fabric);
            mTv_body_type = (TextView) itemView.findViewById(R.id.tv_body_type);
            mLl_top_type = (LinearLayout) itemView.findViewById(R.id.ll_top_type);
            mLl_front = (LinearLayout) itemView.findViewById(R.id.ll_front);
            mTv_front = (TextView) itemView.findViewById(R.id.tv_front);
            mLl_back = (LinearLayout) itemView.findViewById(R.id.ll_back);
            mTv_back = (TextView) itemView.findViewById(R.id.tv_back);
            mLl_baju = (LinearLayout) itemView.findViewById(R.id.ll_baju);
            mTv_baju = (TextView) itemView.findViewById(R.id.tv_baju);
            mLl_extra = (LinearLayout) itemView.findViewById(R.id.ll_extra);
            mTv_extra = (TextView) itemView.findViewById(R.id.tv_extra);
            mLl_qty_cut = (LinearLayout) itemView.findViewById(R.id.ll_qty_cut);
            mTv_total_qty = (TextView) itemView.findViewById(R.id.tv_total_qty);
            mTv_total_cut = (TextView) itemView.findViewById(R.id.tv_total_cut);
            mTv_price_fabric = (TextView) itemView.findViewById(R.id.tv_price_fabric);
            mTv_total_fabric = (TextView) itemView.findViewById(R.id.tv_total_fabric);
            mIv_product_image.setOnClickListener(this);

            mRv_products = (RecyclerView) itemView.findViewById(R.id.rv_products);
            int i = 6;
            if (res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (commonMethods.isTablet(activity))
                    i = 10;
                else
                    i = 8;
            }
            mRv_products.setLayoutManager(new GridLayoutManager(activity, i));
            horizontalAdapter = new GalleryAdapter(activity);
            mRv_products.setAdapter(horizontalAdapter);
        }


        @Override
        public void onClick(View view) {
            CartItem item = listArray.get(getAdapterPosition());
            if (item.getSuitFbricId() == 1) {
                showPopup(item.getImageName(), item.getProductId());
            } else
                showPopupFabric(item.getImageName(), item.getProductId(), "#FFFFFF");

        }
    }


    private void showPopup(String imageName, String productId) {
        dialog = new Dialog(
                activity, R.style.popupTheme);
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_popup_product_detail, null);

        dialog.setContentView(view); // your custom view.
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //dialog.getWindow().setGravity(Gravity.BOTTOM);
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
        mTv_Top_Fabrics = (EditText) view.findViewById(R.id.tv_top_fabrics);
        mTv_Bottom_Fabrics = (EditText) view.findViewById(R.id.tv_bottom_fabrics);
        mTv_Dupatta = (EditText) view.findViewById(R.id.tv_dupatta);
        mEdt_Dupatta = (TextInputLayout) view.findViewById(R.id.edt_dupatta);
        mTv_All_Fabrics = (EditText) view.findViewById(R.id.tv_all_fabrics);
        mEdt_All_Fabrics = (TextInputLayout) view.findViewById(R.id.edt_all_fabrics);
        mTv_Category = (EditText) view.findViewById(R.id.tv_category);
        mTv_Type = (EditText) view.findViewById(R.id.tv_type);
        mTv_Price = (EditText) view.findViewById(R.id.tv_price);
        mTv_Min_Qty = (EditText) view.findViewById(R.id.tv_min_qty);
        mLL_Fabrics = (LinearLayout) view.findViewById(R.id.ll_top_bottom_fab);
        String[] Images = {imageName};
        Glide.with(activity).load(imageName).diskCacheStrategy(DiskCacheStrategy.ALL)//.thumbnail(0.1f)
                .error(R.drawable.no_img).into(imageView);
        APIs.GetProductDetail_Suit_Seller(activity, this, productId);


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
//            Glide.with(activity).load(imageName).diskCacheStrategy(DiskCacheStrategy.ALL)//.thumbnail(0.1f)
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
                if (strApiName.equalsIgnoreCase("GetProductDetail_Suit_Seller")) {
                    JSONObject job = jobjWhole.optJSONObject("resData");
                    mTv_Product_Code.setText(job.optString("ProductCode"));
                    mTv_Brand.setText(job.optString("BrandName"));
                    mTv_Category.setText(job.optString("CategoryName"));
                    mTv_Type.setText(job.optString("FabricType"));
                    mTv_Price.setText(commonVariables.strCurrency_name + " " + job.optInt("OfferPrice"));
                    mTv_Min_Qty.setText("" + job.optInt("MinOrderQty"));
                    if (job.optBoolean("IsAllOver")) {
                        mLL_Fabrics.setVisibility(View.GONE);
                        mEdt_All_Fabrics.setVisibility(View.VISIBLE);
                        mEdt_Dupatta.setVisibility(View.GONE);
                        mTv_All_Fabrics.setText(job.optString("FabricName"));


                    } else {
                        mLL_Fabrics.setVisibility(View.VISIBLE);
                        mEdt_All_Fabrics.setVisibility(View.GONE);
                        mEdt_Dupatta.setVisibility(View.VISIBLE);
                        mTv_Top_Fabrics.setText(job.optString("TopFabricName"));
                        mTv_Bottom_Fabrics.setText(job.optString("BottomFabricName"));
                        mTv_Dupatta.setText(job.optString("DupattaFabricName"));
                    }
                } else if (strApiName.equalsIgnoreCase("GetProductDetail_Fabric")) {
                    JSONObject job = jobjWhole.optJSONObject("resData");
                    mTv_Product_Code.setText(job.optString("ProductCode"));
                    mTv_Brand.setText(job.optString("BrandName"));
                    mTv_Category.setText(job.optString("CategoryName"));
                    mTv_Type.setText(job.optString("FabricType"));
                    mTv_Price.setText(commonVariables.strCurrency_name + " " + job.optString("OfferPrice") + "/mtr");
//                    mTv_Min_Qty.setText("" + job.optInt("MinOrderQty"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}