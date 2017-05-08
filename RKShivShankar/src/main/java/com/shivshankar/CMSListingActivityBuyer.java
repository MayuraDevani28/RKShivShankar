package com.shivshankar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.shivshankar.ServerCall.APIs;
import com.shivshankar.adapters.CMSListAdapter;
import com.shivshankar.classes.CMS;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CMSListingActivityBuyer extends BaseActivityBuyer implements View.OnClickListener, OnResult {
    ImageView mIv_close;
    RecyclerView mGv_More;
    TextView mTv_title;
    ArrayList<CMS> list = new ArrayList<CMS>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        try {
            Window window = getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
            }
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);
            View rootView = getLayoutInflater().inflate(R.layout.activity_cms_lists, frameLayout);


            String api = getIntent().getStringExtra(commonVariables.INTENT_EXTRA_PAGE);
            String pageName = getIntent().getStringExtra(commonVariables.INTENT_EXTRA_PAGE_NAME);
            bindViews(rootView);
            APIs.GetPolicies(this, this, api);
            mTv_title.setText(pageName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAdapter(ArrayList<CMS> list) {
        try {
            if (list.size() == 0) {
                mGv_More.setVisibility(View.GONE);
            } else {
                mGv_More.setVisibility(View.VISIBLE);
                CMSListAdapter adapter = new CMSListAdapter(this, list);
                mGv_More.setAdapter(adapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindViews(View rootView) {
        try {
            mIv_close = (ImageView) rootView.findViewById(R.id.iv_close);
            mIv_close.setOnClickListener(this);

            mGv_More = (RecyclerView) rootView.findViewById(R.id.gv_more);

            GridLayoutManager layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.col_home));
            mGv_More.setLayoutManager(layoutManager);

            mTv_title = (TextView) rootView.findViewById(R.id.tv_title);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        returnBack();

    }

    private void returnBack() {
        try {
            Intent output = new Intent();
            setResult(RESULT_OK, output);
            finish();
            overridePendingTransition(0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
        view.startAnimation(buttonClick);
        if (view == mIv_close) {
            onBackPressed();
        } else
            super.onClick(view);
    }

    @Override
    public void onResult(JSONObject jobj) {

        try {
            if (jobj != null) {
                String strAPIName = jobj.optString("api");
                if (strAPIName.equalsIgnoreCase("GetPolicies")) {
                    JSONArray jArray = jobj.optJSONArray("resData");
                    ArrayList<String> listTabContents = new ArrayList<String>();

                    if (jArray.length() == 0) {
                    } else {
                        for (int k = 0; k < jArray.length(); k++) {
                            JSONObject jsonAccordian = jArray.optJSONObject(k);
                            String strPolicyText = jsonAccordian.optString("ContentText");
//                            strPolicyText = "<html><body>" + "<p style=\"text-align: justify;font-size:8px;\"><font color='#646363'><small>" + strPolicyText.replace("\r\n", "<br/>") + "</small></font></p>" + "</body></html>";
                            list.add(new CMS(jsonAccordian.optString("Title"), strPolicyText));
                        }
                        setAdapter(list);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}