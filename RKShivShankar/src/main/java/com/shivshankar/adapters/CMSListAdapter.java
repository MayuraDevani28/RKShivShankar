package com.shivshankar.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shivshankar.CMSDisplayActivityBuyer;
import com.shivshankar.R;
import com.shivshankar.classes.CMS;
import com.shivshankar.utills.commonVariables;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Mayura Devani 09-02-2015
 * This Adapter class is for Image downloading and displaying same image in the image View.
 * Caching is managed automatically.
 */
@SuppressLint({"NewApi", "ResourceAsColor"})
public class CMSListAdapter extends RecyclerView.Adapter<CMSListAdapter.MyViewHolder> {
    private final AppCompatActivity activity;
    List<CMS> list;

    Gson gson;
    Resources res;

    public CMSListAdapter(AppCompatActivity activity, ArrayList<CMS> list) {
        this.activity = activity;
        gson = new Gson();
        try {
            this.list = list;
            res = activity.getResources();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<CMS> getList() {
        return list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_row_cms_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {

            holder.mTv_about.setTag(R.string.position, position);
            String strName = list.get(position).getName();
            holder.mTv_about.setText(strName);
            //set height in proportion to screen size
//            int proportionalHeight = UIUtil.containerHeight((MainActivity) mCntx);
//            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, proportionalHeight); // (width, height)
//            holder.mTv_about.setLayoutParams(params);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTv_about;

        public MyViewHolder(View itemView) {
            super(itemView);
            try {
                mTv_about = (TextView) itemView.findViewById(R.id.tv_about);
                mTv_about.setOnClickListener(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View v) {
            try {
                Intent intent = new Intent(activity, CMSDisplayActivityBuyer.class);
                intent.putExtra(commonVariables.INTENT_EXTRA_PAGE_NAME, list.get(getAdapterPosition()).getName());
                intent.putExtra(commonVariables.INTENT_EXTRA_PAGE, list.get(getAdapterPosition()).getContent());
                activity.startActivity(intent);
                activity.overridePendingTransition(0,0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}