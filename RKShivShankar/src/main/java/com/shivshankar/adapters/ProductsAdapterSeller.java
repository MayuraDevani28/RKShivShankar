package com.shivshankar.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shivshankar.ProductsActivitySeller;
import com.shivshankar.R;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.classes.CatalogItem;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONObject;

import java.util.List;

/*
 * Created by Mayura Devani 09-02-2015
 * This Adapter class is for Image downloading and displaying same image in the image View.
 * Caching is managed automatically.
 */
@SuppressLint({"NewApi", "ResourceAsColor"})
public class ProductsAdapterSeller extends RecyclerView.Adapter<ProductsAdapterSeller.MyViewHolder> implements OnResult {

    private final AppCompatActivity activity;
    private final List<CatalogItem> list;
    private String strLoginId;
    private static int posit;
    Resources res;
    Dialog dialog;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar mPbar_product;
        private ImageView mIv_product_image;
        private TextView mTv_product_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
            mIv_product_image = (ImageView) itemView.findViewById(R.id.iv_product_image);
            mPbar_product = (ProgressBar) itemView.findViewById(R.id.pbar_product);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_row_product_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        try {
            try {
                holder.mTv_product_name.setSelected(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.mIv_product_image.setOnClickListener(v -> {
                final int position1 = Integer.parseInt(v.getTag(R.string.position).toString());
                try {
                    Log.v("TAGRK", "I'm Clicked" + position1);

                    try {
                        final Animation anim = AnimationUtils.loadAnimation(activity, 0);
                        v.startAnimation(anim);
                        final Handler h = new Handler();
                        h.postDelayed(() -> {
                            anim.cancel();
                            CatalogItem cat = list.get(position1);
//                                    Intent intent = new Intent(activity, CatalogDetailActivity.class);
//                                    intent.putExtra(commonVariables.INTENT_EXTRA_KEY_PRODUCT_OBJECT, cat);
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                        intent.putExtra("TransitionName", "Image" + position);
//                                        activity.startActivityForResult(intent, 80, ActivityOptions.makeSceneTransitionAnimation(activity, v, v.getTransitionName()).toBundle());
//                                    } else {
//                                        activity.startActivity(intent);
//                                        activity.overridePendingTransition(0, 0);
//                                    }
                        }, 30);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            final CatalogItem item = list.get(position);
            holder.mTv_product_name.setText(WordUtils.capitalizeFully("Catalog: " + item.getCatalogName()));


            String strImageURL = item.getImageName();
            if ((strImageURL != null) && (!strImageURL.equals(""))) {
                Glide.with(activity).load(strImageURL).asBitmap().placeholder(R.color.gray_bg).error(R.drawable.camera).into(holder.mIv_product_image);
            } else {
                holder.mPbar_product.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public ProductsAdapterSeller(AppCompatActivity activity, List<CatalogItem> list) {

        this.activity = activity;
        this.list = list;
        try {
            res = activity.getResources();
            strLoginId = AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void callAddCatalogToCartAPI(String catalogId, int i, String strLoginId, String strcatalogSizes) {

        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD).path("MobileAPI/AddCatalogToCart")
                .appendQueryParameter("catalogId", catalogId)
                .appendQueryParameter("catalogQty", i + "")
                .appendQueryParameter("loginId", strLoginId + "")
                .appendQueryParameter("catalogSizes", strcatalogSizes)
                .appendQueryParameter("productIds", "").build();

        String query = uri.toString();
        APIs.callAPI(null, this, query);
    }


    @Override
    public void onResult(JSONObject jobjWhole) {
        if (jobjWhole != null) {
            try {
                if (dialog != null)
                    dialog.dismiss();

                String strAPIName = jobjWhole.optString("api");

                if (strAPIName.equalsIgnoreCase("AddCatalogToCart")) {
                    String strResult = jobjWhole.optString("res");
                    if (strResult.equalsIgnoreCase("success")) {
                        int cCount = jobjWhole.optInt("TotalCartItems");
                        commonMethods.saveAndSetCartCount(cCount, ((ProductsActivitySeller) activity).mTv_cart_count);
                        commonMethods.cartCountAnimation(activity, ((ProductsActivitySeller) activity).mTv_cart_count);
                    } else if (strResult.equalsIgnoreCase("selectsize")) {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
                        builder.setTitle(commonVariables.appname);
                        builder.setMessage("Select Size");
                        builder.setPositiveButton("Ok", null);
                        builder.show();
//                        CatalogItem product = list.get(posit);
//                        MainActivity.mNavigationDrawerFragment.showBackButton();
                    } else if (strResult.equalsIgnoreCase("noquantity")) {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
                        builder.setTitle(commonVariables.appname);
                        builder.setMessage("Product Out of stock");
                        builder.setPositiveButton("Ok", null);
                        builder.show();
                    }
                } else if (jobjWhole.has("Cart_List")) {
                    JSONObject job = jobjWhole.optJSONObject("Cart_List");
                    String status = job.optString("status");
                    if (status != null) {
                        if (status.equals("Add")) {
                            commonMethods.cartCountAnimation(activity, ((ProductsActivitySeller) activity).mTv_cart_count);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}