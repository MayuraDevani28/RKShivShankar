package com.shivshankar.adapters;

import android.annotation.SuppressLint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shivshankar.CartActivity;
import com.shivshankar.R;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.classes.CartItem;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonVariables;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressLint({"NewApi", "ResourceAsColor"})
public class CartAdapterBuyer extends RecyclerView.Adapter<CartAdapterBuyer.MyViewHolder> implements OnResult {

    private final AppCompatActivity activity;
    private final ArrayList<CartItem> list;
    private static int posit;

    public CartAdapterBuyer(AppCompatActivity activity, ArrayList<CartItem> list) {
        this.activity = activity;
        this.list = list;
    }

    public List<CartItem> getItems() {
        return list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, TextWatcher {

        private ImageView mIv_product_image, mIv_delete;
        private TextView mTv_product_code, mTv_price, mTv_total, mBtn_update;
        EditText mTv_qty;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTv_product_code = (TextView) itemView.findViewById(R.id.tv_product_code);
            mIv_product_image = (ImageView) itemView.findViewById(R.id.iv_product_image);
            mIv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
            mTv_price = (TextView) itemView.findViewById(R.id.tv_price);
            mTv_qty = (EditText) itemView.findViewById(R.id.tv_qty);
            mTv_qty.addTextChangedListener(this);
            mTv_total = (TextView) itemView.findViewById(R.id.tv_total);
            mIv_delete.setOnClickListener(this);
            mBtn_update = (TextView) itemView.findViewById(R.id.btn_update);
            mBtn_update.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            posit = getAdapterPosition();
            CartItem product = list.get(posit);
            if (view == mIv_delete) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(commonVariables.appname);
                builder.setMessage("Do you want to delete this product?");
                builder.setPositiveButton("Yes", (dialog, which) ->
                        APIs.RemoveCartProduct(activity, CartAdapterBuyer.this, product.getProductId()));
                builder.setNegativeButton("No", null);
                builder.show();
            } else if (view == mBtn_update) {
                String qty = mTv_qty.getText().toString().trim();
                if (Integer.parseInt(qty) == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle(commonVariables.appname);
                    builder.setMessage("Quantity can't be 0");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                } else
                    APIs.Update_Cart_Suit(activity, CartAdapterBuyer.this, product.getCartId(), qty);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!editable.toString().equals(list.get(getAdapterPosition()).getCartQuantity())) {
                mBtn_update.setVisibility(View.VISIBLE);
            } else
                mBtn_update.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_row_cart, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final CartItem item = list.get(position);
        try {
            holder.mTv_product_code.setSelected(true);
            holder.mTv_product_code.setText(WordUtils.capitalize(item.getProductCode() + " (" + item.getBrandName() + ")"));
            holder.mTv_price.setText(item.getOfferPrice());
            holder.mTv_qty.setText(item.getCartQuantity() + "");
            holder.mTv_total.setText(item.getTotalAmount());

            String strImageURL = item.getImageName();
            if ((strImageURL != null) && (!strImageURL.equals(""))) {
                Glide.with(activity).load(strImageURL).asBitmap().placeholder(R.color.gray_bg).approximate().dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.no_img).thumbnail(0.1f).into(holder.mIv_product_image);
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
                if (strApiName.equalsIgnoreCase("RemoveCartProduct")) {
                    int strresId = jobjWhole.optInt("resInt");
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle(commonVariables.appname);
                    builder.setMessage(jobjWhole.optString("res"));
                    if (strresId == 1) {
                        builder.setPositiveButton("Ok", (dialog, which) -> {
                            list.remove(posit);
                            AppPreferences.getPrefs().edit().putInt(commonVariables.CART_COUNT, list.size()).apply();
                            if (list.size() == 0) {
                                ((CartActivity) activity).setListAdapter(list);
                            } else
                                notifyDataSetChanged();
                        });
                    } else {
                        builder.setPositiveButton("Ok", null);
                    }
                    builder.show();
                } else if (strApiName.equalsIgnoreCase("Update_Cart_Suit")) {
                    int strresId = jobjWhole.optInt("resInt");
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle(commonVariables.appname);
                    builder.setMessage(jobjWhole.optString("res"));
                    if (strresId == 1) {
                        builder.setPositiveButton("Ok", (dialog, which) -> {
                            list.get(posit).setCartQuantity(8);
                            list.get(posit).setTotalAmount("8888");
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