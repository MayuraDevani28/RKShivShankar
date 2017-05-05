package com.shivshankar.adapters;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.shivshankar.AddUpdateBrandActivitySeller;
import com.shivshankar.BrandsActivitySeller;
import com.shivshankar.R;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.classes.Brand;
import com.shivshankar.utills.AlertDialogManager;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.CircleTransform;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonVariables;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONObject;

import java.util.ArrayList;

public class BrandsAdapterSeller extends RecyclerView.Adapter<BrandsAdapterSeller.MyViewHolder> implements OnResult {

    private final AppCompatActivity activity;
    private final ArrayList<Brand> list;
    private static int posit;
    int d, gray;

    public BrandsAdapterSeller(AppCompatActivity activity, ArrayList<Brand> list) {

        this.activity = activity;
        this.list = list;
        try {
            d = ContextCompat.getColor(activity, R.color.white);
            gray = ContextCompat.getColor(activity, R.color.gray_bg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResult(JSONObject jobjWhole) {
        try {
            if (jobjWhole != null) {
                String strApiName = jobjWhole.optString("api");
                if (strApiName.equalsIgnoreCase("RemoveSellerBrand")) {
                    int strresId = jobjWhole.optInt("resInt");

                    if (strresId == 1) {
                        list.remove(posit);
                        if (list.size() == 0) {
                            AppPreferences.getPrefs().edit().putString(commonVariables.KEY_BRAND, "").apply();
                            ((BrandsActivitySeller) activity).setListAdapter(list);
                        } else
                            notifyItemRemoved(posit);
                    } else {
                        AlertDialogManager.showDialog(activity, jobjWhole.optString("res"), null);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mIv_imageView, mIv_change_image, mIv_delete;
        private TextView mTv_name;
        RelativeLayout mLl_brand;

        public MyViewHolder(View itemView) {
            super(itemView);
            try {
                mTv_name = (TextView) itemView.findViewById(R.id.tv_brand_name);
                mIv_imageView = (ImageView) itemView.findViewById(R.id.iv_brand_logo);
                mIv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
                mIv_change_image = (ImageView) itemView.findViewById(R.id.iv_change_image);
                mLl_brand = (RelativeLayout) itemView.findViewById(R.id.ll_brand);
                mIv_change_image.setOnClickListener(this);
                mIv_delete.setOnClickListener(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View view) {
            try {
                posit = getAdapterPosition();
                Brand item = list.get(posit);
                if (view == mIv_change_image) {
                    Intent intent = new Intent(activity, AddUpdateBrandActivitySeller.class);
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    String saveThis = "";
//                    if (mIv_imageView.getDrawable() != null) {
//                        try {
//                            Bitmap bitmap1;
//                            if (mIv_imageView.getDrawable() instanceof RoundedBitmapDrawable)
//                                bitmap1 = ((RoundedBitmapDrawable) mIv_imageView.getDrawable()).getBitmap();
//                            else
//                                bitmap1 = ((BitmapDrawable) mIv_imageView.getDrawable()).getBitmap();
//                            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                            byte[] byteArray = stream.toByteArray();
//                            saveThis = Base64.encodeToString(byteArray, Base64.DEFAULT);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
                    SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(item);
                    editor.putString(commonVariables.KEY_BRAND, json);
                    editor.apply();
                    activity.startActivityForResult(intent, commonVariables.REQUEST_ADD_UPDATE_BRAND);
                    activity.overridePendingTransition(0, 0);
                } else {
                    AlertDialogManager.showDialogYesNo(activity, "Do you want to delete this brand?", "Yes", () -> APIs.RemoveSellerBrand(activity, BrandsAdapterSeller.this, item.getBrandId() + ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_row_brands_seller, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        try {
//            int colpink = res.getColor(R.color.brand2);
//            int colBrand1 = res.getColor(R.color.brand1);
//            if (position % 2 == 0) {
//                holder.mLl_brand.setBackgroundColor(colBrand1);
//            } else
//                holder.mLl_brand.setBackgroundColor(colpink);

            final Brand item = list.get(position);
            holder.mTv_name.setText(WordUtils.capitalizeFully(item.getBrandName()));
            String strImageURL = item.getBrandLogo();
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