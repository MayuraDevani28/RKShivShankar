package com.shivshankar.ServerCall;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.shivshankar.customcontrols.DialogHorizontalView;
import com.shivshankar.utills.OnResultString;
import com.shivshankar.utills.commonVariables;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Mayura on 12/13/2016.
 */

public class ServerImageUpload extends AsyncTask<String, String, String> {
    AppCompatActivity activity;
    OnResultString onresult;
    DialogHorizontalView mView;
    String responseString = null;
    File file;
    String name, mobile, address, email, strProjId;
    String uri2;

    public ServerImageUpload(AppCompatActivity activity, OnResultString onresult, String strBrandname, String address, String email, String strProjId, File file, Uri uri) {
        this.activity = activity;
        this.onresult = onresult;
        this.file = file;
        this.name = name;
        this.mobile = mobile;
        this.address = address;
        this.email = email;
        this.strProjId = strProjId;
    }


    @Override
    protected String doInBackground(String... strings) {
        return uploadFile(name, mobile, address, email, strProjId, file);
    }


    private String uploadFile(String name, String mobile, String address, String email, String strProj, File file) {
        try {
            String charset = "UTF-8";
            Uri uri2 = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD).path("mobileapi/" + "CareerSave").build();

            try {
                MultipartUtility multipart = new MultipartUtility(uri2.toString(), charset);
                multipart.addFilePart("htpFile", file);
                multipart.addFormField("Name", name);
                multipart.addFormField("Phone", mobile);
                multipart.addFormField("Address", address);
                multipart.addFormField("Email", email);
                multipart.addFormField("ApplyFor", strProj);

                List<String> response = multipart.finish();

                System.out.println("SERVER REPLIED:");

                for (String line : response) {
                    System.out.println(line);
                    responseString = line;
                }
            } catch (IOException ex) {
                System.err.println(ex);
                responseString = ex.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAGRK", "Exception: " + e.getMessage());
            responseString = e.getMessage();
        }
        return responseString;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            if (activity != null) {
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mView = new DialogHorizontalView();
                        mView.show(((AppCompatActivity) activity).getSupportFragmentManager(), "load");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAGRK", "Exception: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            if (activity != null) {
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (mView != null)
                            mView.dismiss();
                    }
                });
            }
            if (onresult != null && result != null) {
                Log.v("TAGRK", "l RESPONSE:" + result);
                onresult.onResult(result);
            } else
                Log.v("TAGRK", "RESPONSE:" + result);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAGRK", "Exception: " + e.getMessage());
        }
    }

}
