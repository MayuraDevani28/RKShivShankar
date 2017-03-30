package com.shivshankar.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.shivshankar.FilterActivityBuyer;
import com.shivshankar.R;
import com.shivshankar.classes.FilterAttribute;

import java.util.ArrayList;


public class FilterAttrubuteAdapter extends RecyclerView.Adapter<FilterAttrubuteAdapter.MyViewHolder> {
    private FilterActivityBuyer activity;
    ArrayList<FilterAttribute> listOption = new ArrayList<FilterAttribute>();
    boolean isSort;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CheckedTextView mCb_filter_option;

        public MyViewHolder(View itemView) {
            super(itemView);
            try {
                mCb_filter_option = (CheckedTextView) itemView.findViewById(R.id.cb_filter_option);
                mCb_filter_option.setOnClickListener(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View view) {
            try {
                int position = getAdapterPosition();
                if (isSort) {
                    for (int i = 0; i < listOption.size(); i++) {
                        if (i == position)
                            setcurrent(i);
                        else
                            listOption.get(i).setSelected(false);
                    }
                } else
                    setcurrent(position);
                notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void setcurrent(int position) {
            if (listOption.get(position).isSelected()) {
                listOption.get(position).setSelected(false);
            } else {
                listOption.get(position).setSelected(true);
            }
        }
    }

    public String getSelectedItems() {
        String sel = "";
        for (int i = 0; i < listOption.size(); i++) {
            FilterAttribute item = listOption.get(i);
            if (item.isSelected())
                sel = sel + item.getid() + ",";
        }
        if (!sel.isEmpty())
            sel = sel.substring(0, sel.length() - 1);
        return sel;
    }

    @Override
    public FilterAttrubuteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_row_checkbox, parent, false);
        return new FilterAttrubuteAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FilterAttrubuteAdapter.MyViewHolder holder, int position) {

        try {
            holder.mCb_filter_option.setText(listOption.get(position).getname());//+ " (" + listOption.get(position).getCount() + ")");
            if (listOption.get(position).isSelected()) {
                holder.mCb_filter_option.setChecked(true);
            } else {
                holder.mCb_filter_option.setChecked(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return listOption.size();
    }

    public FilterAttrubuteAdapter(FilterActivityBuyer activity, ArrayList<FilterAttribute> objects, boolean isSort) {
        this.activity = activity;
        this.listOption = objects;
        this.isSort = isSort;
    }

}