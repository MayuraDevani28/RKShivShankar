package com.shivshankar.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
    private EditText mTv_Brand;
    private EditText mTv_Top_Fabrics;
    private EditText mTv_Bottom_Fabrics;
    private EditText mTv_Dupatta;
    private EditText mTv_All_Fabrics;
    private EditText mTv_Category;
    private EditText mTv_Type;
    private EditText mTv_Price;
    private EditText mTv_Min_Qty;
    private LinearLayout mLL_Fabrics;
    private TextInputLayout mEdt_Dupatta;
    private TextInputLayout mEdt_All_Fabrics;
    private EditText mTv_Product_Code;
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
                        .asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.IMMEDIATE).dontAnimate().listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        try {
                            holder.imageView.setImageResource(R.color.transparent);
                            holder.imageView.setBackgroundColor(Color.parseColor(listImages.get(position).getIFSCCode()));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                        .thumbnail(0.1f).error(R.drawable.no_img)
                        .override(200, 200).into(holder.imageView);

            } else {
                try {
//                    holder.imageView.setImageResource(R.color.transparent);
                    String color = listImages.get(position).getIFSCCode();
                    if (color.length() == 4) {
                        color = color + color.substring(1, 4);
                    }
                    holder.imageView.setBackgroundColor(Color.parseColor(color));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
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
            showPopup(order.getImageURL(), order.getId() + "");
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
        ImageView close = (ImageView) view.findViewById(R.id.iv_close);
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
        Glide.with(activity).load(imageName).diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f).error(R.drawable.no_img).into(imageView);
        APIs.GetProductDetail_Suit_Seller(activity, this, productId);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}