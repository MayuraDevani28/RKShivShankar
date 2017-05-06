package com.shivshankar.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shivshankar.NotificationsActivityBuyer;
import com.shivshankar.NotificationsActivitySeller;
import com.shivshankar.R;
import com.shivshankar.classes.NavigationItem;

import java.util.List;

public class NotificationListAdapter extends ArrayAdapter<NavigationItem> {

    private Activity activity;
    private TextView mTv_name, mTv_message, mTv_date;
    LinearLayout mLl_whole_item;
    ImageView imageView, mIv_delete;
    int size;
    int[] androidColors;
    boolean unDoClicked = false;
    List<NavigationItem> navigationItems;

    public NotificationListAdapter(Activity activity2, int resource, List<NavigationItem> navigationItems) {
        super(activity2, resource, navigationItems);
        this.activity = activity2;
        size = (int) ((activity.getWindowManager().getDefaultDisplay().getWidth() - 16) / 1.3);
        androidColors = activity.getResources().getIntArray(R.array.androidcolors);
        this.navigationItems = navigationItems;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_row_notification, parent, false);

            mLl_whole_item = (LinearLayout) convertView.findViewById(R.id.ll_whole_item);
            mTv_name = (TextView) convertView.findViewById(R.id.item_name);
            mTv_message = (TextView) convertView.findViewById(R.id.item_message);
            imageView = (ImageView) convertView.findViewById(R.id.imageView);
            mIv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
            mIv_delete.setTag(R.string.position, position);
            mTv_date = (TextView) convertView.findViewById(R.id.item_date);
            NavigationItem item = getItem(position);
            mTv_name.setText(item.getText());
            mTv_message.setText(item.getMessage());
            mTv_date.setText(item.getDate());

            String strImageURL = item.getImageUrl();

            if ((strImageURL != null) && (!strImageURL.equals(""))) {
                Glide.with(activity).load(strImageURL)//.asBitmap().approximate().thumbnail(0.1f).dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
            }

            mIv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        int pos = Integer.parseInt(view.getTag(R.string.position).toString());
                        NavigationItem itemnoti = navigationItems.get(pos);
                        navigationItems.remove(pos);
                        notifyDataSetChanged();
                        try {
                            Snackbar snackbar = Snackbar.make(view, "Notification's gone !", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        unDoClicked = true;
                                        item.setUnDoClicked(true);
                                        navigationItems.add(pos, item);
                                        notifyDataSetChanged();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            snackbar.addCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    try {
                                        if (!unDoClicked) {
                                            if (activity instanceof NotificationsActivitySeller)
                                                ((NotificationsActivitySeller) activity).callRemoveNoti(itemnoti);
                                            else
                                                ((NotificationsActivityBuyer) activity).callRemoveNoti(itemnoti);
                                        }
                                        super.onDismissed(snackbar, event);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            snackbar.setActionTextColor(Color.WHITE);
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            snackbar.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}