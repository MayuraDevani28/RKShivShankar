package com.shivshankar.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.liuguangqiang.progressbar.CircleProgressBar;
import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.shivshankar.AddUpdateProductActivitySeller;
import com.shivshankar.ProductsActivitySeller;
import com.shivshankar.R;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.classes.ProductItem;
import com.shivshankar.utills.AlertDialogManager;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonVariables;
import com.shivshankar.viewpager.ViewPagerActivity;

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
    boolean isActive;

    Dialog dialog;
    private EditText mTv_Brand, mTv_Top_Fabrics, mTv_Bottom_Fabrics, mTv_Dupatta, mTv_All_Fabrics, mTv_Category, mTv_Type, mTv_Price, mTv_Min_Qty, mTv_Product_Code;
    private TextInputLayout mEdt_Dupatta, mEdt_All_Fabrics;
    private LinearLayout mLL_Fabrics;

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
            mIv_product_image.setOnClickListener(this);
            mMultiple_actions_left = (FloatingActionMenu) itemView.findViewById(R.id.multiple_actions_left);
            mMultiple_actions_left.setIconAnimated(true);
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
            } else if (view == mFb_eye) {

                Runnable listner;
                String strMessage;

                if (list.get(posit).isActive()) {
                    isActive = false;
                    strMessage = "Do you want to hide this product?";
                    listner = () -> APIs.ProductActiveInactive(activity, ProductsAdapterSeller.this, product.getProductId(), isActive);
                } else {
                    isActive = true;
                    strMessage = "Do you want to show this product?";
                    listner = () -> APIs.ProductActiveInactive(activity, ProductsAdapterSeller.this, product.getProductId(), isActive);
                }
                AlertDialogManager.showDialogYesNo(activity, strMessage, "Yes", listner);
            } else if (view == mFb_delete) {
                Runnable listner = () -> APIs.RemoveProduct_Suit(activity, ProductsAdapterSeller.this, product.getProductId());
                AlertDialogManager.showDialogYesNo(activity, "Do you want to delete this product?", "Yes", listner);
            } else if (view == mIv_product_image) {
                showPopup(list.get(getAdapterPosition()),list.get(getAdapterPosition()).getImageName(), product.getProductId());
            }
        }
    }

    private void showPopup(ProductItem productItem, String imageName, String productId) {
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
        LinearLayout ll_edit = (LinearLayout) view.findViewById(R.id.ll_edit);
        ll_edit.setVisibility(View.VISIBLE);
        ll_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                    view.startAnimation(buttonClick);

                    Intent intent = new Intent(activity, AddUpdateProductActivitySeller.class);
                    Gson gson = new Gson();
                    String json = gson.toJson(productItem);
                    intent.putExtra(commonVariables.KEY_PRODUCT, json);

                    activity.startActivityForResult(intent, commonVariables.REQUEST_ADD_UPDATE_PRODUCT);
                    activity.overridePendingTransition(0, 0);
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_row_product_item_seller, parent, false);
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
        if (item.isActive()) {
            holder.mFb_eye.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_eye_black));
        } else {
            holder.mFb_eye.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_eye_off_black));
        }
        holder.mTv_product_name.setText(WordUtils.capitalizeFully(item.getProductCode()));
        String strImageURL = item.getImageName();
        if ((strImageURL != null) && (!strImageURL.equals(""))) {
            Glide.with(activity).load(strImageURL)//.asBitmap().approximate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    //.thumbnail(0.01f).dontAnimate()
                    .error(R.drawable.no_img).into(holder.mIv_product_image);
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

                    if (strresId == 1) {
                        list.remove(posit);
                        if (list.size() == 0) {
                            ((ProductsActivitySeller) activity).setListAdapter(list);
                        } else
                            notifyItemRemoved(posit);
                    } else {
                        AlertDialogManager.showDialog(activity, jobjWhole.optString("res"), null);
                    }

                } else if (strApiName.equalsIgnoreCase("ProductActiveInactive")) {
                    int strresId = jobjWhole.optInt("resInt");
                    if (strresId == 1) {
                        list.set(posit, new ProductItem(list.get(posit).getProductId(), list.get(posit).getProductCode(), list.get(posit).getOfferPrice(), list.get(posit).getImageName(), "", "", "", "", "", "", "", "", "", "", "", "", "", list.get(posit).getMinOrderQty(), false, false, null, isActive));
                        notifyItemChanged(posit);
                    } else {
                        AlertDialogManager.showDialog(activity, jobjWhole.optString("res"), null);
                    }

                } else if (strApiName.equalsIgnoreCase("GetProductDetail_Suit_Seller")) {
                    JSONObject job = jobjWhole.optJSONObject("resData");
                    // item = new Brand(job.optString("BrandId"), job.optString("BrandName"), job.optString("BrandLogo"));
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