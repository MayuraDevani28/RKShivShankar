package com.shivshankar.adapters;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shivshankar.R;
import com.shivshankar.classes.Order;
import com.shivshankar.fragments.OrderDetailFragment;
import com.shivshankar.utills.commonVariables;

import java.util.ArrayList;
import java.util.List;


public class OrdersAdapterBuyer extends RecyclerView.Adapter<OrdersAdapterBuyer.MyViewHolder> {
    private final AppCompatActivity activity;
    private final List<Order> listItems;
    Resources res;


    public OrdersAdapterBuyer(AppCompatActivity activity, ArrayList<Order> listArray2) {

        this.activity = activity;
        this.listItems = listArray2;
        try {
            res = activity.getResources();
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
            GradientDrawable bgShape = (GradientDrawable) holder.mLl_order.getBackground();
            bgShape.setColor(ContextCompat.getColor(activity, color));

            holder.mTv_order_no.setText(order.getOrderNo());
            holder.mTv_order_date.setText("Ordered on: " + order.getOrderDate());
            holder.mTv_total.setText(commonVariables.strCurrency_name + " " + order.getTotal());
            holder.horizontalAdapter.setData(order.getListImages()); // List of Strings
            holder.horizontalAdapter.setRowIndex(position);
            holder.mTv_count.setText(order.getListImages().size() + " Items");

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
        RecyclerView mRv_products;
        GalleryAdapter horizontalAdapter;

        public MyViewHolder(View itemView) {
            super(itemView);
            mLl_order = (LinearLayout) itemView.findViewById(R.id.ll_order);
            mTv_order_no = (TextView) itemView.findViewById(R.id.tv_order_no);
            mTv_order_date = (TextView) itemView.findViewById(R.id.tv_order_date);
            mTv_total = (TextView) itemView.findViewById(R.id.tv_total);
            mTv_view = (TextView) itemView.findViewById(R.id.tv_view);
            mTv_status = (TextView) itemView.findViewById(R.id.tv_status);
            mRv_products = (RecyclerView) itemView.findViewById(R.id.rv_products);
            mTv_count = (TextView) itemView.findViewById(R.id.tv_count_products);

            int i = 4;
            if (res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                i = 6;
            }
            mRv_products.setLayoutManager(new GridLayoutManager(activity, i));
            horizontalAdapter = new GalleryAdapter(activity);
            mRv_products.setAdapter(horizontalAdapter);

            mTv_view.setOnClickListener(this);
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
