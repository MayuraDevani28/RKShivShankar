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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.liuguangqiang.progressbar.CircleProgressBar;
import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.shivshankar.R;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.classes.ProductItem;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonVariables;
import com.shivshankar.viewpager.ViewPagerActivity;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressLint({"NewApi", "ResourceAsColor"})
public class SuitProductsAdapterBuyer extends RecyclerView.Adapter<SuitProductsAdapterBuyer.MyViewHolder> implements OnResult {

    private final AppCompatActivity activity;
    private final ArrayList<ProductItem> list;
    private static int posit;
    Dialog dialog;
    private EditText mTv_Brand, mTv_Top_Fabrics, mTv_Bottom_Fabrics, mTv_Dupatta, mTv_All_Fabrics, mTv_Category, mTv_Type, mTv_Price, mTv_Min_Qty,mTv_Product_Code;
    private TextInputLayout mEdt_Dupatta, mEdt_All_Fabrics;
    private LinearLayout mLL_Fabrics;
    private ImageView imageView;


    public SuitProductsAdapterBuyer(AppCompatActivity activity, ArrayList<ProductItem> list) {
        this.activity = activity;
        this.list = list;
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
                    showPopup(list.get(getAdapterPosition()).getImageName(), product.getProductId());
                } else {
                    if (product.isActive()) {
                        product.setActive(false);
                    } else
                        product.setActive(true);
                    notifyItemChanged(posit);
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
                Glide.with(activity)
                        .load(strImageURL)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//.asBitmap()
                        .priority(Priority.IMMEDIATE)//.dontAnimate()
                        //.thumbnail(0.1f).override(200, 200)
                        .error(R.drawable.no_img)
                        .into(holder.mIv_product_image);
            }
            if (item.isActive())
                holder.mRv_checked.setVisibility(View.VISIBLE);
            else
                holder.mRv_checked.setVisibility(View.GONE);

//            if (isOneChecked()) {
//                activity.runOnUiThread(new Runnable(){
//                    @Override
//                    public void run() {
//                        mLl_add_to_cart.setVisibility(View.VISIBLE);
//                    } });
//
////                ((ProductsActivityBuyer) activity).mRv_items.setPadding(val5, val5, val5, val28);
//            } else {
//                activity.runOnUiThread(new Runnable(){
//                    @Override
//                    public void run() {
//                        mLl_add_to_cart.setVisibility(View.GONE);
//                    } });
//
////                ((ProductsActivityBuyer) activity).mRv_items.setPadding(val5, val5, val5, val5);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean isOneChecked() {
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

                if (strApiName.equalsIgnoreCase("GetProductDetail_Suit_Seller")) {
                    JSONObject job = jobjWhole.optJSONObject("resData");
                    // item = new Brand(job.optString("BrandId"), job.optString("BrandName"), job.optString("BrandLogo"));
                    String[] Images = {job.optString("LargeImageName")};
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
        CircleProgressBar progressBar = (CircleProgressBar) view.findViewById(R.id.progressbar1);
        SwipeBackLayout swipeBackLayout = (SwipeBackLayout) view.findViewById(R.id.swipe_layout);
        swipeBackLayout.setOnSwipeBackListener(new SwipeBackLayout.SwipeBackListener() {
            @Override
            public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
                progressBar.setProgress((int) (progressBar.getMax() * fractionAnchor));
                if (progressBar.getMax() * fractionAnchor == 100)
                    dialog.dismiss();
            }
        });
        RelativeLayout close = (RelativeLayout) view.findViewById(R.id.rl_close);
        imageView = (ImageView) view.findViewById(R.id.image_gallery);
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
                .thumbnail(0.1f)
                .error(R.drawable.no_img_big).into(imageView);
        APIs.GetProductDetail_Suit_Seller(activity, this, productId);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                dialog.dismiss();

            }
        });


    }
}