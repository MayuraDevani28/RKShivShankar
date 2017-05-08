package com.shivshankar.adapters;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
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
import com.shivshankar.R;
import com.shivshankar.classes.Order;
import com.shivshankar.fragments.OrderDetailFragment;
import com.shivshankar.utills.RoundedCornersTransformation;
import com.shivshankar.utills.commonVariables;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class OrdersAdapterSeller extends RecyclerView.Adapter<OrdersAdapterSeller.MyViewHolder> {
    private final AppCompatActivity activity;
    private final List<Order> listItems;
    Resources res;
    int d, gray;

    public OrdersAdapterSeller(AppCompatActivity activity, ArrayList<Order> listArray2) {

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
            holder.mTv_c_name.setText(order.getCustName());

            GradientDrawable bgShape = (GradientDrawable) holder.mLl_order.getBackground();
            Random rnd = new Random();
            color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            bgShape.setColor(ColorUtils.setAlphaComponent(color, 99));

            String strImageURL = order.getListImages().get(0).getImageURL();
            if ((strImageURL != null) && (!strImageURL.equals(""))) {
                Glide.with(activity).load(strImageURL).asBitmap()//.dontAnimate()//.approximate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//.thumbnail(0.1f)
                        .error(R.drawable.no_img)
                        .transform(new RoundedCornersTransformation(activity,13,0, RoundedCornersTransformation.CornerType.LEFT))
                        .into(holder.mIv_img);
            }

            int count = order.getListImages().size();
            if (count == 1) {
                holder.mRl_images.setVisibility(View.INVISIBLE);
            } else {
                holder.mRl_images.setVisibility(View.VISIBLE);
                holder.mTv_count.setText("+ " + (count - 1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_row_orders_seller, parent, false);
        return new OrdersAdapterSeller.MyViewHolder(itemView);
    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTv_order_no, mTv_order_date, mTv_total, mTv_c_name, mTv_status, mTv_count;
        LinearLayout mLl_order;
        RelativeLayout mRl_images;
        ImageView mIv_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            mLl_order = (LinearLayout) itemView.findViewById(R.id.ll_order);
            mTv_order_no = (TextView) itemView.findViewById(R.id.tv_order_no);
            mTv_order_date = (TextView) itemView.findViewById(R.id.tv_order_date);
            mTv_c_name = (TextView) itemView.findViewById(R.id.tv_c_name);
            mTv_total = (TextView) itemView.findViewById(R.id.tv_total);
            mTv_status = (TextView) itemView.findViewById(R.id.tv_status);
            mRl_images = (RelativeLayout) itemView.findViewById(R.id.rl_images);
            mTv_count = (TextView) itemView.findViewById(R.id.tv_count_products);
            mIv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            mIv_img.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            try {
                OrderDetailFragment fragment = OrderDetailFragment.newInstance(listItems.get(getAdapterPosition()));

                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.fl_whole, fragment).addToBackStack(null).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
