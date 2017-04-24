package com.shivshankar.adapters;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.shivshankar.R;
import com.shivshankar.classes.Order;
import com.shivshankar.fragments.OrderDetailFragment;
import com.shivshankar.utills.commonVariables;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class OrdersAdapterBuyer extends RecyclerView.Adapter<OrdersAdapterBuyer.MyViewHolder> {
    private final AppCompatActivity activity;
    private final List<Order> listItems;
    Resources res;
    int d, gray;

    public OrdersAdapterBuyer(AppCompatActivity activity, ArrayList<Order> listArray2) {

        this.activity = activity;
        this.listItems = listArray2;
        try {
            res = activity.getResources();
            d = ContextCompat.getColor(activity, R.color.white);
            gray = ContextCompat.getColor(activity, R.color.gray_bg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            Order order = listItems.get(position);

            String status = order.getStatus();
            holder.mTv_status.setText(status);
            int color = R.color.black;
            if (!status.isEmpty()) {
                if (status.equalsIgnoreCase("In Process"))
                    color = R.color.status_in_process;
                else if (status.equalsIgnoreCase("PAYMENT RECEIVED"))
                    color = R.color.status_payment_received;
                else if (status.equalsIgnoreCase("pending"))
                    color = R.color.status_pending;
                else if (status.equalsIgnoreCase("complete"))
                    color = R.color.status_complete;
                else if (status.equalsIgnoreCase("INCOMPLETE"))
                    color = R.color.status_incomplete;
                else if (status.equalsIgnoreCase("DELIVERED"))
                    color = R.color.status_delivered;
                else if (status.equalsIgnoreCase("canceled"))
                    color = R.color.status_canceled;
            }
            holder.mTv_status.setTextColor(ContextCompat.getColor(activity, color));

            holder.mTv_order_no.setText(order.getOrderNo());
            holder.mTv_order_date.setText(order.getOrderDate());
            holder.mTv_total.setText("Total: " + commonVariables.strCurrency_name + " " + order.getTotal());

            String strImageURL = order.getListImages().get(0).getImageURL();
            if ((strImageURL != null) && (!strImageURL.equals(""))) {
                Glide.with(activity).load(strImageURL).asBitmap().approximate().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.1f)
                        .error(R.drawable.no_img)
                        .into(new BitmapImageViewTarget(holder.mIv_img) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                try {
//                                    Palette.PaletteAsyncListener paletteListener = new Palette.PaletteAsyncListener() {
//                                        public void onGenerated(Palette palette) {
//                                            try {
//                                                GradientDrawable bgShape = (GradientDrawable) holder.mLl_order.getBackground();
//                                                int col = palette.getVibrantColor(d);
//                                                if (col == d) {
//                                                    bgShape.setColor(ContextCompat.getColor(activity, gray));
//                                                } else
//                                                    bgShape.setColor(ColorUtils.setAlphaComponent(col, 99));
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    };
//
//                                    if (resource != null && !resource.isRecycled()) {
//                                        Palette.from(resource).generate(paletteListener);
//                                    }
                                    GradientDrawable bgShape = (GradientDrawable) holder.mLl_order.getBackground();
                                    Random rnd = new Random();
                                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                                    bgShape.setColor(ColorUtils.setAlphaComponent(color, 99));

                                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                                    circularBitmapDrawable.setCornerRadius(10);
                                    holder.mIv_img.setImageDrawable(circularBitmapDrawable);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });


            }

            int count = order.getListImages().size();
            if (count == 1) {
                holder.mRl_images.setVisibility(View.INVISIBLE);
            } else {
                holder.mRl_images.setVisibility(View.VISIBLE);
                holder.mTv_count.setText("+ " + (count - 1));
            }
            //            holder.horizontalAdapter.setData(order.getListImages()); // List of Strings
//            holder.horizontalAdapter.setRowIndex(position);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_row_orders_buyer, parent, false);
        return new OrdersAdapterBuyer.MyViewHolder(itemView);
    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTv_order_no, mTv_order_date, mTv_total, mTv_status, mTv_view, mTv_count;
        LinearLayout mLl_order;
        RelativeLayout mRl_images;
        ImageView mIv_img;
//        RecyclerView mRv_products;
//        GalleryAdapter horizontalAdapter;

        public MyViewHolder(View itemView) {
            super(itemView);
            mLl_order = (LinearLayout) itemView.findViewById(R.id.ll_order);
            mTv_order_no = (TextView) itemView.findViewById(R.id.tv_order_no);
            mTv_order_date = (TextView) itemView.findViewById(R.id.tv_order_date);
            mTv_total = (TextView) itemView.findViewById(R.id.tv_total);
            mTv_view = (TextView) itemView.findViewById(R.id.tv_view);
            mTv_status = (TextView) itemView.findViewById(R.id.tv_status);
            mRl_images = (RelativeLayout) itemView.findViewById(R.id.rl_images);
            mTv_count = (TextView) itemView.findViewById(R.id.tv_count_products);
            mIv_img = (ImageView) itemView.findViewById(R.id.iv_img);

//            mRv_products = (RecyclerView) itemView.findViewById(R.id.rv_products);
//            int i = 4;
//            if (res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                i = 6;
//            }
//            mRv_products.setLayoutManager(new GridLayoutManager(activity, i));
//            horizontalAdapter = new GalleryAdapter(activity);
//            mRv_products.setAdapter(horizontalAdapter);

            mTv_view.setOnClickListener(this);
            mIv_img.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            try {
                OrderDetailFragment fragment = new OrderDetailFragment(listItems.get(getAdapterPosition()));
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.fl_whole, fragment).addToBackStack(null).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
