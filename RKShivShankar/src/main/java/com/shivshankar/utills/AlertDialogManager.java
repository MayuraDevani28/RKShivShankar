package com.shivshankar.utills;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shivshankar.R;

public class AlertDialogManager {

    public static void showDialog(AppCompatActivity activity, String message, Runnable block1) {
        Dialog dialog = new Dialog(activity, R.style.popupTheme);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_custom_alert_dialog, null);

        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationFade;
        dialog.show();
        ImageView close = (ImageView) view.findViewById(R.id.iv_close);
        TextView mBtn_Done = (TextView) view.findViewById(R.id.btn_done);
        TextView mTv_message = (TextView) view.findViewById(R.id.tv_message);
        mTv_message.setText(message);
        close.setOnClickListener(view12 -> {
            AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            view12.startAnimation(buttonClick);
            dialog.dismiss();
        });

        mBtn_Done.setOnClickListener(view1 -> {
            AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            view1.startAnimation(buttonClick);

            if (block1 != null) {
                block1.run();
            }
            dialog.dismiss();
        });
    }

    public static void showSuccessDialog(AppCompatActivity activity, String message, Runnable block1) {
        Dialog dialog = new Dialog(activity, R.style.popupTheme);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_custom_alert_dialog, null);

        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationFade;
        dialog.show();

        view.findViewById(R.id.iv_close).setVisibility(View.GONE);
        TextView mBtn_Done = (TextView) view.findViewById(R.id.btn_done);
        TextView mTv_message = (TextView) view.findViewById(R.id.tv_message);
        view.findViewById(R.id.fl_success).setVisibility(View.VISIBLE);
        mTv_message.setText(message);

        mBtn_Done.setOnClickListener(view1 -> {
            AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            view1.startAnimation(buttonClick);

            if (block1 != null) {
                block1.run();
            }
            dialog.dismiss();
        });
    }

    public static void showDialogYesNo(AppCompatActivity activity, String message, String strBtnText, Runnable block1) {
        Dialog dialog = new Dialog(activity, R.style.popupTheme);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_custom_alert_dialog, null);

        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationFade;
        dialog.show();
        TextView mTv_message = (TextView) view.findViewById(R.id.tv_message);
        mTv_message.setText(message);

        TextView mBtn_no = (TextView) view.findViewById(R.id.btn_no);
        mBtn_no.setVisibility(View.VISIBLE);
        mBtn_no.setOnClickListener(view1 -> {
            AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            view1.startAnimation(buttonClick);
            dialog.dismiss();
        });

        ImageView close = (ImageView) view.findViewById(R.id.iv_close);
        close.setOnClickListener(view12 -> {
            AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            view12.startAnimation(buttonClick);
            dialog.dismiss();
        });

        TextView mBtn_Done = (TextView) view.findViewById(R.id.btn_done);
        mBtn_Done.setText(strBtnText);

        mBtn_Done.setOnClickListener(view13 -> {
            AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            view13.startAnimation(buttonClick);

            if (block1 != null) {
                block1.run();
            }
            dialog.dismiss();
        });

    }

    public static void showDialogCustom(AppCompatActivity activity, String message, String strPosBtnText, String strNegBtnText, Runnable blockPos, Runnable blockNeg) {
        Dialog dialog = new Dialog(activity, R.style.popupTheme);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_custom_alert_dialog, null);

        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationFade;
        dialog.show();
        TextView mTv_message = (TextView) view.findViewById(R.id.tv_message);
        mTv_message.setText(message);


        ImageView close = (ImageView) view.findViewById(R.id.iv_close);
        close.setOnClickListener(view1 -> {
            AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            view1.startAnimation(buttonClick);
            dialog.dismiss();
        });

        TextView mBtn_Done = (TextView) view.findViewById(R.id.btn_done);
        mBtn_Done.setText(strPosBtnText);
        TextView mBtn_no = (TextView) view.findViewById(R.id.btn_no);
        mBtn_no.setVisibility(View.VISIBLE);
        mBtn_no.setText(strNegBtnText);

        mBtn_Done.setOnClickListener(view13 -> {
            AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            view13.startAnimation(buttonClick);

            if (blockPos != null) {
                blockPos.run();
            }
            dialog.dismiss();
        });
        mBtn_no.setOnClickListener(view13 -> {
            AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            view13.startAnimation(buttonClick);

            if (blockNeg != null) {
                blockNeg.run();
            }
            dialog.dismiss();
        });
    }
}
