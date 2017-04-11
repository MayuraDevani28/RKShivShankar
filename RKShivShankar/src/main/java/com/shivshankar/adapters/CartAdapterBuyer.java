package com.shivshankar.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shivshankar.CartActivityBuyer;
import com.shivshankar.R;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.classes.Brand;
import com.shivshankar.classes.CartItem;
import com.shivshankar.classes.ProductItem;
import com.shivshankar.utills.AlertDialogManager;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonVariables;
import com.shivshankar.viewpager.ViewPagerActivity;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressLint({"NewApi", "ResourceAsColor"})
public class CartAdapterBuyer extends RecyclerView.Adapter<CartAdapterBuyer.MyViewHolder> implements OnResult {

    private final AppCompatActivity activity;
    private final ArrayList<CartItem> list;
    private static int posit;

    Dialog dialog;
    private ProductItem product;
    private Brand item;
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


    public CartAdapterBuyer(AppCompatActivity activity, ArrayList<CartItem> list) {
        this.activity = activity;
        this.list = list;
    }

    public List<CartItem> getItems() {
        return list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, TextWatcher {
        boolean isChanging = false;
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
            mIv_product_image.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            posit = getAdapterPosition();
            CartItem product = list.get(posit);
            if (view == mIv_product_image) {
                showPopup(list.get(getAdapterPosition()).getImageName(), product.getProductId());
            } else if (view == mIv_delete) {
                AlertDialogManager.showDialogYesNo(activity, "Do you want to delete this product?", "Yes", () -> APIs.RemoveCartProduct(activity, CartAdapterBuyer.this, product.getProductId()));

            } else if (view == mBtn_update) {
                String qty = mTv_qty.getText().toString().trim();
                if (Integer.parseInt(qty) == 0) {
                    AlertDialogManager.showDialog(activity, "Quantity can't be 0", null);
                } else if (Integer.parseInt(qty) < product.getMinOrderQuantity()) {
                    AlertDialogManager.showDialog(activity, "Minimum " + product.getMinOrderQuantity() + " quantity required", null);
                } else
                    APIs.Update_Cart_Suit(activity, CartAdapterBuyer.this, product.getCartId(), qty, product.getMinOrderQuantity() + "");
            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            isChanging = true;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            try {
                if (isChanging && !editable.toString().trim().isEmpty() && Integer.parseInt(editable.toString().trim()) != list.get(getAdapterPosition()).getCartQuantity()) {
                    mBtn_update.setVisibility(View.VISIBLE);
                } else
                    mBtn_update.setVisibility(View.INVISIBLE);
                isChanging = false;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
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
            holder.mTv_price.setText(commonVariables.strCurrency_name + " " + item.getOfferPrice());
            holder.mTv_qty.setText(item.getCartQuantity() + "");
            holder.mTv_total.setText(commonVariables.strCurrency_name + " " + item.getTotalAmount());

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
        return list.size();
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
                } else if (strApiName.equalsIgnoreCase("RemoveCartProduct")) {
                    int strresId = jobjWhole.optInt("resInt");

                    if (strresId == 1) {
                        list.remove(posit);
                        AppPreferences.getPrefs().edit().putInt(commonVariables.CART_COUNT, list.size()).apply();
                        if (list.size() == 0) {
                            ((CartActivityBuyer) activity).setListAdapter(list);
                        } else
                            notifyDataSetChanged();
                    } else {
                        AlertDialogManager.showDialog(activity, jobjWhole.optString("res"), null);
                    }

                } else if (strApiName.equalsIgnoreCase("Update_Cart_Suit")) {
                    int strresId = jobjWhole.optInt("resInt");

                    if (strresId == 1) {
                        list.get(posit).setCartQuantity(jobjWhole.optJSONObject("resData").optInt("CartQuantity"));
                        list.get(posit).setTotalAmount(jobjWhole.optJSONObject("resData").optString("TotalPrice"));
                        notifyDataSetChanged();
                    } else {
                        AlertDialogManager.showDialog(activity, jobjWhole.optString("res"), null);
                    }

                }
                APIs.GetOrderSummary_Suit(null, (CartActivityBuyer) activity);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                // Dismiss the popup window
                dialog.dismiss();

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ViewPagerActivity.class);
                i.putExtra(commonVariables.INTENT_EXTRA_LIST_IMAGE_ARRAY, Images);
                i.putExtra(commonVariables.INTENT_EXTRA_POSITION, posit);
                i.putExtra(commonVariables.KEY_IS_LANDSCAPE, false);
                activity.startActivity(i);
            }
        });

    }
}