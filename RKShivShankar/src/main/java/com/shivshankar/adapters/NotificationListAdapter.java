package com.shivshankar.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shivshankar.R;
import com.shivshankar.classes.NavigationItem;

import java.util.List;


public class NotificationListAdapter extends ArrayAdapter<NavigationItem> {

	private Activity activity;
	private TextView mTv_name,mTv_message;
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
			mTv_message= (TextView) convertView.findViewById(R.id.item_message);
			imageView = (ImageView) convertView.findViewById(R.id.imageView);
			mTv_name.setText(getItem(position).getText());
			mTv_message.setText(getItem(position).getMessage());

			String strImageURL = getItem(position).getImageUrl();
			if (strImageURL.isEmpty()) {
			} else {

//				ImageAware imageAware = new ImageViewAware(imageView, false);
//				commonVariables.imageLoader.displayImage(strImageURL, imageAware, commonVariables.options_loader_rect);
			}

			try {
				Animation scaleUp = AnimationUtils.loadAnimation(activity, R.anim.list_animation);
				mLl_whole_item.startAnimation(scaleUp);
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;
	}

	public void removeItemAt(int position) {

		try {
			removeItemAt(position);
			notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
