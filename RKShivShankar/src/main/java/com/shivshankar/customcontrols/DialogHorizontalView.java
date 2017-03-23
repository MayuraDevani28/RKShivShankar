package com.shivshankar.customcontrols;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;

import com.shivshankar.R;


public class DialogHorizontalView extends DialogFragment {

    Dialog mDialog;

    public DialogHorizontalView() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mDialog == null) {
            try {
                mDialog = new Dialog(getActivity(), R.style.cat_dialog);
                mDialog.setContentView(R.layout.dialog_custom_progress_horizontal);
                mDialog.setCanceledOnTouchOutside(true);
                mDialog.getWindow().setGravity(Gravity.CENTER);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mDialog;
    }
}
