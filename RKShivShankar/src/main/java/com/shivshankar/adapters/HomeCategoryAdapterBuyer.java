package com.shivshankar.adapters;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shivshankar.BrandsActivitySeller;
import com.shivshankar.R;
import com.shivshankar.classes.Brand;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonVariables;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeCategoryAdapterBuyer extends RecyclerView.Adapter<HomeCategoryAdapterBuyer.MyViewHolder> implements OnResult {

    private final AppCompatActivity activity;
    private final ArrayList<Brand> list;
    private static int posit;

    public HomeCategoryAdapterBuyer(AppCompatActivity activity, ArrayList<Brand> list) {

        this.activity = activity;
        this.list = list;
    }

    @Override
    public void onResult(JSONObject jobjWhole) {
        try {
            if (jobjWhole != null) {
                String strApiName = jobjWhole.optString("api");
                if (strApiName.equalsIgnoreCase("RemoveSellerBrand")) {
                    int strresId = jobjWhole.optInt("resInt");
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle(commonVariables.appname);
                    builder.setMessage(jobjWhole.optString("res"));
                    if (strresId == 1) {
                        builder.setPositiveButton("Ok", (dialog, which) -> {
                            list.remove(posit);
                            if (list.size() == 0) {
                                AppPreferences.getPrefs().edit().putString(commonVariables.KEY_BRAND, "").apply();
                                ((BrandsActivitySeller) activity).setListAdapter(list);
                            } else
                                notifyDataSetChanged();
                        });
                    } else {
                        builder.setPositiveButton("Ok", null);
                    }
                    builder.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout mLl_top;
        private ImageView mIv_image, mIv_down;
        private TextView mTv_name;
        private RadioGroup mRadioGroup;

        public MyViewHolder(View itemView) {
            super(itemView);
            try {
                mLl_top = (LinearLayout) itemView.findViewById(R.id.ll_top);
                mIv_image = (ImageView) itemView.findViewById(R.id.iv_image);
                mTv_name = (TextView) itemView.findViewById(R.id.tv_name);
                mIv_down = (ImageView) itemView.findViewById(R.id.iv_down);
                mRadioGroup = (RadioGroup) itemView.findViewById(R.id.radioGroup);
                mLl_top.setOnClickListener(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View view) {
            try {
                if (view == mLl_top) {
                    if (mRadioGroup.getVisibility() == View.VISIBLE) {
                        mRadioGroup.setVisibility(View.GONE);
                        mIv_down.setRotation(0);
                    } else {
                        mRadioGroup.setVisibility(View.VISIBLE);
                        mIv_down.setRotation(180);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_row_homecat_buyer, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        try {

            final Brand item = list.get(position);
            holder.mTv_name.setText(WordUtils.capitalizeFully(item.getBrandName()));
            String strImageURL = item.getBrandLogo();
            if ((strImageURL != null) && (!strImageURL.equals("")))
                Glide.with(activity).load(strImageURL).asBitmap()
                        .placeholder(R.color.gray_bg_transparent).error(R.color.red)
                        .centerCrop().into(holder.mIv_image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

}