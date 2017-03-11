package com.shivshankar.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shivshankar.R;
import com.shivshankar.classes.SC3Object;

import java.util.ArrayList;

public class CountryAdapter extends BaseAdapter implements Filterable {
	Context context;
	ArrayList<SC3Object> statelist;
	ArrayList<SC3Object> mStringFilterList;
	ValueFilter valueFilter;

	public CountryAdapter(Context context, ArrayList<SC3Object> statelist) {
		this.context = context;
		this.statelist = statelist;
		mStringFilterList = statelist;
	}

	@Override
	public int getCount() {
		return statelist.size();
	}

	@Override
	public Object getItem(int position) {
		return statelist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return statelist.indexOf(getItem(position));
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		convertView = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_row_country, null);

			TextView name_tv = (TextView) convertView.findViewById(R.id.tv_name);
			ImageView iv = (ImageView) convertView.findViewById(R.id.iv_bank);
			ProgressBar mPbar_product = (ProgressBar) convertView.findViewById(R.id.pbar_product);

			SC3Object state = statelist.get(position);

			name_tv.setText(state.getName());
//			String strImageURL = state.getImageURL();
//			if (!strImageURL.isEmpty() && !strImageURL.equalsIgnoreCase("null")) {
//				iv.setVisibility(View.VISIBLE);
//				Glide.with(context).load(strImageURL).into(iv);
////				imageLoader.DisplayImage(strImageURL, iv, mPbar_product);
//
//			} else {
////				iv.setImageDrawable(context.getResources().getDrawable(R.drawable.close_white));
//				iv.setVisibility(View.GONE);
//			}
		}
		return convertView;
	}

	@Override
	public Filter getFilter() {
		if (valueFilter == null) {
			valueFilter = new ValueFilter();
		}
		return valueFilter;
	}

	private class ValueFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();

			if (constraint != null && constraint.length() > 0) {
				ArrayList<SC3Object> filterList = new ArrayList<SC3Object>();
				for (int i = 0; i < mStringFilterList.size(); i++) {
					if ((mStringFilterList.get(i).getName().toUpperCase()).contains(constraint.toString().toUpperCase())) {

						SC3Object state = new SC3Object(mStringFilterList.get(i).getId(), mStringFilterList.get(i).getName(), mStringFilterList.get(i).getIFSCCode(), mStringFilterList.get(i)
								.getImageURL());

						filterList.add(state);
					}
				}
				results.count = filterList.size();
				results.values = filterList;
			} else {
				results.count = mStringFilterList.size();
				results.values = mStringFilterList;
			}
			return results;

		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			statelist = (ArrayList<SC3Object>) results.values;
			notifyDataSetChanged();
		}

	}

}
