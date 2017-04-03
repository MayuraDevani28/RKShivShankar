package com.shivshankar.adapters;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shivshankar.R;
import com.shivshankar.classes.CartItem;

import java.util.ArrayList;


@SuppressLint("NewApi")
public class OrderProductListAdapter extends RecyclerView.Adapter<OrderProductListAdapter.MyViewHolder> {

    private AppCompatActivity activity;

    ArrayList<CartItem> listArray = new ArrayList<CartItem>();

    public OrderProductListAdapter(AppCompatActivity activity, ArrayList<CartItem> objects) {
        this.activity = activity;
        this.listArray = objects;
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
//            holder.mTv_product_name.setText((position + 1) + ". " + item.getCatalogItem().getCatalogName());
//            holder.mTv_product_code.setText(item.getCatalogItem().getCatalogCode());
//            holder.mTv_quantity.setText(item.getCatalogItem().getQuantity() + "");
//            holder.mTv_sub_total.setText(strCurrencyName + " " + item.getCatalogItem().getMarketPrice());
//            holder.mTv_product_price.setText(strCurrencyName + " " + item.getPrice());
//            String strImageURL = item.getCatalogItem().getImage_url();
//            if ((strImageURL != null) && (!strImageURL.equals("")))
//                imageLoader.DisplayImage(strImageURL, mIv_product_image, mPbar_product);
//            else
//                mPbar_product.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mPbar_product;
        private ImageView mIv_product_image;
        private TextView mTv_product_name;
        private TextView mTv_product_code;
        private TextView mTv_quantity;
        private LinearLayout mLl_size;
        private TextView mTv_product_size;
        private TextView mTv_product_price;
        private TextView mTv_sub_total, mTv_product_code_label;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTv_product_code = (TextView) itemView.findViewById(R.id.tv_product_code);
            mPbar_product = (ProgressBar) itemView.findViewById(R.id.pbar_product);
            mTv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
            mTv_quantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            mTv_sub_total = (TextView) itemView.findViewById(R.id.tv_sub_total);
            mLl_size = (LinearLayout) itemView.findViewById(R.id.ll_size);
            mTv_product_price = (TextView) itemView.findViewById(R.id.tv_product_price);
            mTv_product_size = (TextView) itemView.findViewById(R.id.tv_product_size);
            mTv_product_code_label = (TextView) itemView.findViewById(R.id.tv_product_code_label);
        }
    }
}