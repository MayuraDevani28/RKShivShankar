package com.shivshankar.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.classes.Brand;
import com.shivshankar.classes.ProductItem;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonVariables;
import com.shivshankar.viewpager.ViewPagerActivity;

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
    Dialog dialog;
    int val28 = 28, val5 = 5;
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

        private final TextView mTv_product_info;
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
            mRv_checked.setOnClickListener(this);
            mTv_product_info = (TextView) itemView.findViewById(R.id.tv_info);
            mTv_product_info.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            try {
                posit = getAdapterPosition();
                ProductItem product = list.get(posit);

                if (view == mTv_product_info) {

                    showPopup(list.get(getAdapterPosition()).getImageName(),list.get(getPosition()).getProductId());
                } else {
                    if (product.isActive()) {
                        product.setActive(false);
                    } else
                        product.setActive(true);
                    notifyDataSetChanged();
                }
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
                Glide.with(activity).load(strImageURL).dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.1f).into(holder.mIv_product_image);
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
                if (strApiName.equalsIgnoreCase("GetProductDetail_Suit_Seller")) {
                    JSONObject job = jobjWhole.optJSONObject("resData");
                   // item = new Brand(job.optString("BrandId"), job.optString("BrandName"), job.optString("BrandLogo"));
                    mTv_Product_Code.setText(job.optString("ProductCode"));
                    mTv_Brand.setText( job.optString("BrandName"));
                    mTv_Category.setText(job.optString("CategoryName"));
                    mTv_Type.setText(job.optString("FabricType"));
                    mTv_Price.setText(""+job.optInt("OfferPrice"));
                    mTv_Min_Qty.setText(""+job.optInt("MinOrderQty"));
                    if(job.optBoolean("IsAllOver")){
                        mLL_Fabrics.setVisibility(View.GONE);
                        mEdt_All_Fabrics.setVisibility(View.VISIBLE);
                        mEdt_Dupatta.setVisibility(View.GONE);
                        mTv_All_Fabrics.setText(job.optString("FabricName"));


                    }
                    else {
                        mLL_Fabrics.setVisibility(View.VISIBLE);
                        mEdt_All_Fabrics.setVisibility(View.GONE);
                        mEdt_Dupatta.setVisibility(View.VISIBLE);
                        mTv_Top_Fabrics.setText(job.optString("TopFabricName"));
                        mTv_Bottom_Fabrics.setText(job.optString("BottomFabricName"));
                        mTv_Dupatta.setText(job.optString("DupattaFabricName"));
                    }
                  /*  product.setBrand(item);
                    product.setAllOver(job.optBoolean("IsAllOver"));
                    product.setFabricId(job.optString("FabricId"));
                    product.setFabricName(job.optString("FabricName"));
                    product.setDupattaFabricId(job.optString("DupattaFabricId"));
                    product.setDupattaFabricName(job.optString("DupattaFabricName"));
                    product.setBottomFabricName(job.optString("BottomFabricId"));
                    product.setBottomFabricName(job.optString("BottomFabricName"));
                    product.setTopFabricId(job.optString("TopFabricId"));
                    product.setTopFabricName(job.optString("TopFabricName"));
                    product.setFabricType(job.optString("FabricType"));
                    product.setCategoryId(job.optString("CategoryId"));
                    product.setCategoryName(job.optString("CategoryName"));
                    setData();*/


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setData() {

    }

    private void showPopup(String imageName, String productId) {
        dialog = new Dialog(
                activity, R.style.popupTheme);
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_product_detail, null);


        dialog.setContentView(view); // your custom view.
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
        ImageView close = (ImageView) view.findViewById(R.id.iv_close);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_gallery);
        mTv_Brand = (EditText)view.findViewById(R.id.tv_brand_name);
        mTv_Product_Code = (EditText)view.findViewById(R.id.tv_product_code);
        mTv_Top_Fabrics = (EditText)view.findViewById(R.id.tv_top_fabrics);
        mTv_Bottom_Fabrics = (EditText)view.findViewById(R.id.tv_bottom_fabrics);
        mTv_Dupatta = (EditText)view.findViewById(R.id.tv_dupatta);
        mEdt_Dupatta = (TextInputLayout)view.findViewById(R.id.edt_dupatta);
        mTv_All_Fabrics = (EditText)view.findViewById(R.id.tv_all_fabrics);
        mEdt_All_Fabrics = (TextInputLayout)view.findViewById(R.id.edt_all_fabrics);
        mTv_Category = (EditText)view.findViewById(R.id.tv_category);
        mTv_Type = (EditText)view.findViewById(R.id.tv_type);
        mTv_Price = (EditText)view.findViewById(R.id.tv_price);
        mTv_Min_Qty = (EditText)view.findViewById(R.id.tv_min_qty);
        mLL_Fabrics = (LinearLayout)view.findViewById(R.id.ll_top_bottom_fab);
        String[] Images = {imageName};
        Glide.with(activity).load(imageName).placeholder(R.color.gray_bg).diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.1f).into(imageView);
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