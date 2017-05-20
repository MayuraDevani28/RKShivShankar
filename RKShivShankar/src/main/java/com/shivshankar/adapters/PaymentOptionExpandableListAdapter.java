package com.shivshankar.adapters;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CompoundButton;

import com.shivshankar.OrderFormActivityBuyer;
import com.shivshankar.R;
import com.shivshankar.classes.Payment;

import java.util.List;


public class PaymentOptionExpandableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Payment> list;
    OrderFormActivityBuyer activity;
    static int positionSelected = -1;

    public PaymentOptionExpandableListAdapter(OrderFormActivityBuyer activity, List<Payment> data) {
        this.list = data;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_row_payment_header, parent, false);
        return new ListHeaderViewHolder(itemView);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Payment item = list.get(position);

        final ListHeaderViewHolder itemController = (ListHeaderViewHolder) holder;
        itemController.mTv_title.setText(item.getPaymentOptionName());
//        itemController.mTv_title.setChecked(position == mSelectedItem);

        itemController.mTv_title.setOnCheckedChangeListener(null);
        if (item.isChecked()) {
            itemController.mTv_title.setChecked(true);
            itemController.mTv_detail.setVisibility(View.VISIBLE);
            WebSettings webViewSettings = itemController.mTv_detail.getSettings();
            webViewSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webViewSettings.setJavaScriptEnabled(true);
            webViewSettings.setBuiltInZoomControls(true);
            webViewSettings.setPluginState(WebSettings.PluginState.ON);
            webViewSettings.setBuiltInZoomControls(false);

            String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/ProximaNova-Reg.otf\")}body {font-family: MyFont;font-size:13px;text-align: justify;}</style></head><body>";
            String pas = "</body></html>";
            String paymentDetail = item.getPaymentOptionDetails();
            paymentDetail = pish + paymentDetail.replace("\r\n", "<br/>") + pas;
            paymentDetail = pish + paymentDetail + pas;
            itemController.mTv_detail.loadData(paymentDetail, "text/html", "UTF-8");
        } else {
            itemController.mTv_title.setChecked(false);
            itemController.mTv_detail.setVisibility(View.GONE);
        }
        itemController.mTv_title.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    activity.strPaymentCode = "";
                    list.get(position).setChecked(false);
                    notifyItemChanged(position);
                } else {
                    if (positionSelected != -1)
                        list.get(positionSelected).setChecked(false);
                    notifyItemChanged(positionSelected);
                    positionSelected = position;
                    activity.strPaymentCode = list.get(position).getPaymentOptionKey();
                    list.get(position).setChecked(true);
                    notifyItemChanged(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private static class ListHeaderViewHolder extends RecyclerView.ViewHolder {


        public AppCompatCheckBox mTv_title;
        public WebView mTv_detail;


        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            mTv_title = (AppCompatCheckBox) itemView.findViewById(R.id.tv_title);
            mTv_detail = (WebView) itemView.findViewById(R.id.tv_paymentDetails);
        }
    }
}
