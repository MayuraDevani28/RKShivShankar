package com.shivshankar.adapters;

import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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


public class OrdersAdapterSeller extends RecyclerView.Adapter<OrdersAdapterSeller.MyViewHolder> {
    private final AppCompatActivity activity;
    private final List<Order> listItems;
    Resources res;


    public OrdersAdapterSeller(AppCompatActivity activity, ArrayList<Order> listArray2) {

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
            holder.mTv_order_date.setText(order.getOrderDate());
            holder.mTv_total.setText(commonVariables.strCurrency_name + " " + order.getTotal());
            holder.mTv_c_name.setText(order.getCustName());

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
        private TextView mTv_order_no, mTv_order_date, mTv_total, mTv_c_name, mTv_status, mTv_view;
        LinearLayout mLl_order;

        public MyViewHolder(View itemView) {
            super(itemView);
            mLl_order = (LinearLayout) itemView.findViewById(R.id.ll_order);
            mTv_order_no = (TextView) itemView.findViewById(R.id.tv_order_no);
            mTv_order_date = (TextView) itemView.findViewById(R.id.tv_order_date);
            mTv_c_name = (TextView) itemView.findViewById(R.id.tv_c_name);
            mTv_total = (TextView) itemView.findViewById(R.id.tv_total);
            mTv_view = (TextView) itemView.findViewById(R.id.tv_view);
            mTv_status = (TextView) itemView.findViewById(R.id.tv_status);

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
