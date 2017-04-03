package com.shivshankar.adapters;

import android.annotation.SuppressLint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shivshankar.ProductsActivityBuyer;
import com.shivshankar.ProductsActivitySeller;
import com.shivshankar.R;
import com.shivshankar.classes.ProductItem;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonVariables;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressLint({"NewApi", "ResourceAsColor"})
public class ProductsAdapterBuyer extends RecyclerView.Adapter<ProductsAdapterBuyer.MyViewHolder> implements OnResult {

    private final AppCompatActivity activity;
    private final ArrayList<ProductItem> list;
    private static int posit;
    LinearLayout mLl_add_to_cart;
    int val28 = 28, val5 = 5;

    public ProductsAdapterBuyer(AppCompatActivity activity, ArrayList<ProductItem> list, LinearLayout mLl_add_to_cart) {
        this.activity = activity;
        this.list = list;
        this.mLl_add_to_cart = mLl_add_to_cart;
        val28 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, activity.getResources().getDisplayMetrics());
        val5 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, activity.getResources().getDisplayMetrics());
    }

    public List<ProductItem> getItems() {
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
        }

        @Override
        public void onClick(View view) {
            try {
                posit = getAdapterPosition();
                ProductItem product = list.get(posit);
                if (product.isActive()) {
                    product.setActive(false);
                } else
                    product.setActive(true);
                notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                Glide.with(activity).load(strImageURL).placeholder(R.color.gray_bg).dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.no_img).thumbnail(0.1f).into(holder.mIv_product_image);
            }
            if (item.isActive())//checked
                holder.mRv_checked.setVisibility(View.VISIBLE);
            else
                holder.mRv_checked.setVisibility(View.GONE);

            if (isOneChecked()) {
                mLl_add_to_cart.setVisibility(View.VISIBLE);
                ((ProductsActivityBuyer) activity).mRv_items.setPadding(val5, val5, val5, val28);
            } else {
                mLl_add_to_cart.setVisibility(View.GONE);
                ((ProductsActivityBuyer) activity).mRv_items.setPadding(val5, val5, val5, val5);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean isOneChecked() {
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

    @Override
    public void onResult(JSONObject jobjWhole) {
        if (jobjWhole != null) {
            try {
                String strApiName = jobjWhole.optString("api");
                if (strApiName.equalsIgnoreCase("RemoveProduct_Suit")) {
                    int strresId = jobjWhole.optInt("resInt");
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
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