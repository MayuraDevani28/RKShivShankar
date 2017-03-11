package com.shivshankar.adapters;

import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
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

import java.util.ArrayList;
import java.util.List;


public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> {
    // GPSC/201516/72/1113
    private final AppCompatActivity activity;
    private final List<Order> listItems;
    Resources res;

    public OrdersAdapter(AppCompatActivity activity, ArrayList<Order> listArray2) {

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
            holder.mTv_view.setTag(R.string.position, position);
            holder.mTv_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv = (TextView) v;
                    final int pos = Integer.parseInt(tv.getTag(R.string.position).toString());

//                    OrderDetailFragment fragment = new OrderDetailFragment(listItems.get(pos));
//                    try {
//                        FragmentManager fragmentManager = activity.getSupportFragmentManager();
//                        fragmentManager.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

                }
            });
            Order order = listItems.get(position);
            if (order.isShowShipNowBtn()) {
                holder.mTv_ship_now.setVisibility(View.VISIBLE);
                holder.mTv_ship_now.setTag(R.string.position, position);
                holder.mTv_ship_now.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        TextView tv = (TextView) v;
//                        final int pos = Integer.parseInt(tv.getTag(R.string.position).toString());
//                        Intent intent = new Intent(activity, ShipNowOrdersActivity.class);
//                        intent.putExtra(commonVariables.INTENT_EXTRA_ORDER_ID, listItems.get(pos).getOrderId());
//                        activity.startActivity(intent);
//                        activity.overridePendingTransition(R.anim.fade_in_fast, R.anim.fade_out_fast);
                    }
                });
            } else
                holder.mTv_ship_now.setVisibility(View.GONE);


            String status = order.getStatus();
            holder.mTv_status.setText(status);
            int color = R.color.gray;
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
            holder.mTv_total.setText(order.getTotal());
            holder.mTv_c_name.setText(order.getCustomerName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_row_orders_buyer, parent, false);
        return new OrdersAdapter.MyViewHolder(itemView);
    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTv_order_no, mTv_order_date, mTv_total, mTv_c_name, mTv_status, mTv_view, mTv_ship_now;
        LinearLayout mLl_order;

        public MyViewHolder(View itemView) {
            super(itemView);
            mLl_order = (LinearLayout) itemView.findViewById(R.id.ll_order);
            mTv_order_no = (TextView) itemView.findViewById(R.id.tv_order_no);
            mTv_order_date = (TextView) itemView.findViewById(R.id.tv_order_date);
            mTv_c_name = (TextView) itemView.findViewById(R.id.tv_c_name);
            mTv_total = (TextView) itemView.findViewById(R.id.tv_total);
            mTv_view = (TextView) itemView.findViewById(R.id.tv_view);
            mTv_ship_now = (TextView) itemView.findViewById(R.id.tv_ship_now);
            mTv_status = (TextView) itemView.findViewById(R.id.tv_status);
        }
    }
}
