package com.shivshankar.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.shivshankar.FilterActivity;
import com.shivshankar.R;
import com.shivshankar.classes.FilterAttribute;

import java.util.ArrayList;


public class FilterAttrubuteAdapter extends ArrayAdapter<FilterAttribute> {
	private FilterActivity activity;
	private CheckedTextView mCb_filter_option;
	ArrayList<FilterAttribute> listOption = new ArrayList<FilterAttribute>();

	public FilterAttrubuteAdapter(FilterActivity activity, int resource, ArrayList<FilterAttribute> objects) {
		super(activity, resource, objects);
		this.activity = activity;
		this.listOption = objects;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.adapter_row_checkbox, parent, false);
		try {

			convertView.setTag(R.string.position, pos);

			mCb_filter_option = (CheckedTextView) convertView.findViewById(R.id.cb_filter_option);
			mCb_filter_option.setTag(R.string.position, pos);
			mCb_filter_option.setText(listOption.get(pos).getName() + " (" + listOption.get(pos).getCount() + ")");

			if (listOption.get(pos).isSelected()) {
				mCb_filter_option.setChecked(true);
				((ListView) parent).setItemChecked(pos, true);

			} else {
				mCb_filter_option.setChecked(false);
				((ListView) parent).setItemChecked(pos, false);
			}

			// mCb_filter_option.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			//
			// int pos =
			// Integer.parseInt(v.getTag(R.string.position).toString());
			// CheckedTextView ctv = (CheckedTextView) v;
			//
			// if (listOption.get(pos).isSelected()) {
			// ctv.setChecked(false);
			// listOption.get(pos).setSelected(false);
			// } else {
			// ctv.setChecked(true);
			// listOption.get(pos).setSelected(true);
			// }
			//
			// if (!strOption.contains(listOption.get(pos).getName())) {
			// strOption = strOption + "," + listOption.get(pos).getName();
			// if (activity.arryAttrbs != null)
			// if (activity.arryAttrbs.indexOf(listOption.get(pos).getName()) ==
			// -1)
			// activity.arryAttrbs.add(listOption.get(pos).getName());
			// } else {
			// String st = listOption.get(pos).getName();
			// if (strOption.contains(st + ","))
			// st = st + ",";
			// strOption = strOption.replace(st, "");
			// if (activity.arryAttrbs != null)
			// if (activity.arryAttrbs.indexOf(listOption.get(pos).getName()) !=
			// -1)
			// activity.arryAttrbs.remove(activity.arryAttrbs.indexOf(listOption.get(pos).getName()));
			// }
			// }
			// });

		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
}