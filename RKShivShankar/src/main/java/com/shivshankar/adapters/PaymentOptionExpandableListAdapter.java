package com.shivshankar.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.shivshankar.R;
import com.shivshankar.classes.Payment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class PaymentOptionExpandableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int HEADER = 0;
    public static final int CHILD = 1;

    private List<Payment> data;
    private Context context;
    private int quantity;

    private TextView mTv_mild;
    private TextView mTv_medium;
    private TextView mTv_spicy;
    private String strTaste;
    private int mSelectedItem = -1;
    //private List<Item> listSearch;

    public PaymentOptionExpandableListAdapter(List<Payment> data) {
        this.data = data;
        //this.listSearch = new ArrayList<Item>();
        //this.listSearch.addAll(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_header_payment, parent, false);
        return new ListHeaderViewHolder(itemView);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Payment item = data.get(position);


        final ListHeaderViewHolder itemController = (ListHeaderViewHolder) holder;
        itemController.header_title.setText(item.getPaymentOptionName());
        itemController.header_title.setChecked(position == mSelectedItem);
        WebSettings webViewSettings = itemController.child_title.getSettings();
        webViewSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setBuiltInZoomControls(true);
        webViewSettings.setPluginState(WebSettings.PluginState.ON);
        webViewSettings.setBuiltInZoomControls(false);

        String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/ProximaNova-Reg.otf\")}body {font-family: MyFont;font-size:13px;text-align: justify;}</style></head><body>";
        String pas = "</body></html>";
        String paymentDetail = data.get(position).getPaymentOptionDetails();
        paymentDetail = pish + paymentDetail.replace("\r\n", "<br/>") + pas;
        paymentDetail = pish + paymentDetail + pas;
        itemController.child_title.loadData(paymentDetail, "text/html", "UTF-8");

               /* itemController.btn_expand_toggle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (item.invisibleChildren == null) {
                            item.invisibleChildren = new ArrayList<Item>();
                            int count = 0;
                            int pos = data.indexOf(itemController.refferalItem);
                            while (data.size() > pos + 1 && data.get(pos + 1).type == CHILD) {
                                item.invisibleChildren.add(data.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                            itemController.btn_expand_toggle.setImageResource(R.mipmap.plus);
                        } else {
                            int pos = data.indexOf(itemController.refferalItem);
                            int index = pos + 1;
                            for (Item i : item.invisibleChildren) {
                                data.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            itemController.btn_expand_toggle.setImageResource(R.mipmap.minus);
                            item.invisibleChildren = null;
                        }
                    }
                });
*/


        itemController.header_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedItem = position;
                //notifyItemRangeChanged(0, data.size());
                notifyDataSetChanged();

                      /*  if(itemController.header_title.isSelected() == true){
                                itemController.textView.setVisibility(View.VISIBLE);
                        }
                        else {
                            itemController.textView.setVisibility(View.GONE);
                        }*/
                      /*  if (item.invisibleChildren == null) {
                            item.invisibleChildren = new ArrayList<Item>();
                            int count = 0;
                            int pos = data.indexOf(itemController.refferalItem);
                            while (data.size() > pos + 1 && data.get(pos + 1).type == CHILD) {
                                item.invisibleChildren.add(data.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                          //  itemController.btn_expand_toggle.setImageResource(R.mipmap.plus);
                        } else {
                            int pos = data.indexOf(itemController.refferalItem);
                            int index = pos + 1;
                            for (Item i : item.invisibleChildren) {
                                data.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                           // itemController.btn_expand_toggle.setImageResource(R.mipmap.minus);
                            item.invisibleChildren = null;
                        }*/
            }
        });


        //childController.child_title.setText(data.get(position).text);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class ListHeaderViewHolder extends RecyclerView.ViewHolder {


        public AppCompatCheckBox header_title;
        public WebView child_title;


        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            header_title = (AppCompatCheckBox) itemView.findViewById(R.id.header_title);
            child_title = (WebView) itemView.findViewById(R.id.tv_paymentDetails);
        }
    }




  /*  private void showPopup() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context,R.style.popupTheme);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_add_to_cart, null);

        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();

        // Get a reference for the custom view close button
        ImageView closeButton = (ImageView) view.findViewById(R.id.iv_back);
        ImageView mIv_plus = (ImageView) view.findViewById(R.id.iv_plus);
        final TextView mTv_quantity = (TextView) view.findViewById(R.id.tv_quantity);
        ImageView mIv_minus = (ImageView) view.findViewById(R.id.iv_minus);
        final TextView mTv_price = (TextView) view.findViewById(R.id.tv_baseprice);
        final TextView mTv_totalprice = (TextView) view.findViewById(R.id.tv_totalprice);
        Button checkout = (Button) view.findViewById(R.id.btn_add_to_cart);
        mTv_mild = (TextView) view.findViewById(R.id.tv_mild);
        mTv_medium = (TextView) view.findViewById(R.id.tv_medium);
        mTv_spicy = (TextView) view.findViewById(R.id.tv_spicy);
        //Button submit = (Button) view.findViewById(R.id.btn_submit);
       // Button close = (Button) view.findViewById(R.id.btn_cancel);
        mTv_mild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strTaste = "Mild";
                mTv_medium.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.xml_black_white_square_box));
                mTv_mild.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.xml_black_pink_square_box));
                mTv_spicy.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.xml_black_white_square_box));


                    mTv_medium.setTextColor(Color.parseColor("#222222"));
                    mTv_mild.setTextColor(Color.WHITE);
                    mTv_spicy.setTextColor(Color.parseColor("#222222"));


            }
        });
        mTv_medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strTaste = "Medium";
                mTv_medium.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.xml_black_pink_square_box));
                mTv_mild.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.xml_black_white_square_box));
                mTv_spicy.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.xml_black_white_square_box));


                    mTv_medium.setTextColor(Color.WHITE);
                    mTv_mild.setTextColor(Color.parseColor("#222222"));
                    mTv_spicy.setTextColor(Color.parseColor("#222222"));

            }
        });
        mTv_spicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strTaste = "Spicy";
                mTv_medium.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.xml_black_white_square_box));
                mTv_mild.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.xml_black_white_square_box));
                mTv_spicy.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.xml_black_pink_square_box));


                    mTv_medium.setTextColor(Color.parseColor("#222222"));
                    mTv_mild.setTextColor(Color.parseColor("#222222"));
                    mTv_spicy.setTextColor(Color.WHITE);

            }
        });
        mIv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity != 1) {
                    quantity = quantity - 1;
                    mTv_quantity.setText(quantity + "");
                    int price = (Integer.parseInt(mTv_price.getText().toString().trim())*quantity);
                    mTv_totalprice.setText("\u20b9 "+price);
                }
            }
        });

        mIv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = quantity + 1;
                mTv_quantity.setText(quantity + "");
                int price = (Integer.parseInt(mTv_price.getText().toString().trim())*quantity);
                mTv_totalprice.setText("\u20b9 "+price);
            }
        });

        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                dialog.dismiss();
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                dialog.dismiss();
                //context.startActivity(new Intent(context,AddToCartActivity.class));
            }
        });
        // Set a click listener for the popup window close button
       *//* close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                dialog.dismiss();
            }
        });*//*
        // Set a click listener for the popup window close button
        *//*submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                dialog.dismiss();
            }
        });*//*

                *//*
                    public void showAtLocation (View parent, int gravity, int x, int y)
                        Display the content view in a popup window at the specified location. If the
                        popup window cannot fit on screen, it will be clipped.
                        Learn WindowManager.LayoutParams for more information on how gravity and the x
                        and y parameters are related. Specifying a gravity of NO_GRAVITY is similar
                        to specifying Gravity.LEFT | Gravity.TOP.

                    Parameters
                        parent : a parent view to get the getWindowToken() token from
                        gravity : the gravity which controls the placement of the popup window
                        x : the popup's x location offset
                        y : the popup's y location offset
                *//*
        // Finally, show the popup window at the center location of root relative layout

    }*/
   /* public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        data.clear();
        if (charText.length() == 0) {
            data.addAll(listSearch);
        }
        else
        {
            for (Item wp : listSearch)
            {
                if (wp.text.toLowerCase(Locale.getDefault()).contains(charText))
                {
                    data.add(wp);
                }
				*//*if (wp.getAddress().toLowerCase(Locale.getDefault()).contains(charText))
                {
					restaurantArrayList.add(wp);
				}*//*

            }
        }
        notifyDataSetChanged();
    }*/
}
