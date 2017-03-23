package com.shivshankar.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.shivshankar.AddUpdateProductActivitySeller;
import com.shivshankar.ProductsActivitySeller;
import com.shivshankar.R;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.classes.ProductItem;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonVariables;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONObject;

import java.util.ArrayList;

/*
 * Created by Mayura Devani 09-02-2015
 * This Adapter class is for Image downloading and displaying same image in the image View.
 * Caching is managed automatically.
 */
@SuppressLint({"NewApi", "ResourceAsColor"})
public class ProductsAdapterSeller extends RecyclerView.Adapter<ProductsAdapterSeller.MyViewHolder> implements OnResult {

    private final AppCompatActivity activity;
    private final ArrayList<ProductItem> list;
    private static int posit;

    public ProductsAdapterSeller(AppCompatActivity activity, ArrayList<ProductItem> list) {
        this.activity = activity;
        this.list = list;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private FloatingActionMenu mMultiple_actions_left;
        private FloatingActionButton mFb_edit, mFb_eye, mFb_delete;
        private ImageView mIv_product_image;
        private TextView mTv_product_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
            mIv_product_image = (ImageView) itemView.findViewById(R.id.iv_product_image);
            mMultiple_actions_left = (FloatingActionMenu) itemView.findViewById(R.id.multiple_actions_left);
            mMultiple_actions_left.setIconAnimated(false);
            mFb_edit = (FloatingActionButton) itemView.findViewById(R.id.fb_edit);
            mFb_eye = (FloatingActionButton) itemView.findViewById(R.id.fb_eye);
            mFb_delete = (FloatingActionButton) itemView.findViewById(R.id.fb_delete);
            mFb_edit.setOnClickListener(this);
            mFb_delete.setOnClickListener(this);
            mFb_eye.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            posit = getAdapterPosition();
            ProductItem product = list.get(posit);
            if (view == mFb_edit) {
                Intent intent = new Intent(activity, AddUpdateProductActivitySeller.class);
                Gson gson = new Gson();
                String json = gson.toJson(product);
                intent.putExtra(commonVariables.KEY_PRODUCT, json);

                activity.startActivityForResult(intent, commonVariables.REQUEST_ADD_UPDATE_PRODUCT);
                activity.overridePendingTransition(0, 0);
            } else if (view == mFb_delete) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(commonVariables.appname);
                builder.setMessage("Do you want to delete this product?");
                builder.setPositiveButton("Yes", (dialog, which) ->
                        APIs.RemoveProduct_Suit(activity, ProductsAdapterSeller.this, product.getProductId()));
                builder.setNegativeButton("No", null);
                builder.show();
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_row_product_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ProductItem item = list.get(position);
        try {
            holder.mTv_product_name.setSelected(true);
            if (item.isExpanded()) {
                holder.mMultiple_actions_left.open(false);
            } else {
                holder.mMultiple_actions_left.close(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.mTv_product_name.setText(WordUtils.capitalizeFully(item.getProductCode()));
        String strImageURL = item.getImageName();
        if ((strImageURL != null) && (!strImageURL.equals(""))) {
            Glide.with(activity).load(strImageURL).asBitmap().placeholder(R.color.gray_bg).dontAnimate().error(R.drawable.camera).into(holder.mIv_product_image);
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
                if (strApiName.equalsIgnoreCase("RemoveProduct_Suit")) {
                    int strresId = jobjWhole.optInt("resInt");
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
                    builder.setTitle(commonVariables.appname);
                    builder.setMessage(jobjWhole.optString("res"));
                    if (strresId == 1) {
                        builder.setPositiveButton("Ok", (dialog, which) -> {
                            list.remove(posit);
                            if (list.size() == 0) {
                                ((ProductsActivitySeller) activity).setListAdapter(list);
                            } else
                                notifyDataSetChanged();
                        });
                    } else {
                        builder.setPositiveButton("Ok", null);
                    }
                    builder.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}