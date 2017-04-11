package com.shivshankar.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shivshankar.R;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.classes.CartItem;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonVariables;
import com.shivshankar.viewpager.ViewPagerActivity;

import org.json.JSONObject;

import java.util.ArrayList;


@SuppressLint("NewApi")
public class OrderProductListAdapter extends RecyclerView.Adapter<OrderProductListAdapter.MyViewHolder> implements OnResult {

    private AppCompatActivity activity;

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
            holder.mTv_brand_name.setText((position + 1) + ". Brand: " + item.getBrandName());
            holder.mTv_product_code.setText(item.getProductCode());
            holder.mTv_quantity.setText(item.getCartQuantity() + " Items");
            holder.mTv_sub_total.setText(commonVariables.strCurrency_name + " " + item.getTotalAmount());
            holder.mTv_product_price.setText(commonVariables.strCurrency_name + " " + item.getOfferPrice());
            String strImageURL = item.getImageName();
            if ((strImageURL != null) && (!strImageURL.equals(""))) {
                Glide.with(activity).load(strImageURL).asBitmap().approximate().dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.1f).into(holder.mIv_product_image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mIv_product_image;
        private TextView mTv_product_price, mTv_sub_total, mTv_brand_name, mTv_product_code, mTv_quantity;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTv_product_code = (TextView) itemView.findViewById(R.id.tv_product_code);
            mTv_brand_name = (TextView) itemView.findViewById(R.id.tv_brand_name);
            mTv_quantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            mTv_product_price = (TextView) itemView.findViewById(R.id.tv_product_price);
            mIv_product_image = (ImageView) itemView.findViewById(R.id.iv_product_image);
            mTv_sub_total = (TextView) itemView.findViewById(R.id.tv_sub_total);
            mIv_product_image.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            CartItem item = listArray.get(getAdapterPosition());
            showPopup(item.getImageName(), item.getProductId());
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
        Glide.with(activity).load(imageName).diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.1f).into(imageView);
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