package com.shivshankar.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shivshankar.R;
import com.shivshankar.classes.NavigationItem;

import java.util.List;
public class NotificationListAdapter extends ArrayAdapter<NavigationItem> {

    private Activity activity;
    private TextView mTv_name, mTv_message, mTv_date;
    LinearLayout mLl_whole_item;
    ImageView imageView;
    int size;
    int[] androidColors;

    public NotificationListAdapter(Activity activity2, int resource, List<NavigationItem> navigationItems) {
        super(activity2, resource, navigationItems);
        this.activity = activity2;
        size = (int) ((activity.getWindowManager().getDefaultDisplay().getWidth() - 16) / 1.3);
        androidColors = activity.getResources().getIntArray(R.array.androidcolors);
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
            mTv_date = (TextView) convertView.findViewById(R.id.item_date);
            NavigationItem item = getItem(position);
            mTv_name.setText(item.getText());
            mTv_message.setText(item.getMessage());
            mTv_date.setText("On "+item.getDate());

            String strImageURL = item.getImageUrl();
            if ((strImageURL != null) && (!strImageURL.equals(""))) {
                Glide.with(activity).load(strImageURL).asBitmap().approximate().dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.1f).into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
