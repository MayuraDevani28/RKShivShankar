package com.shivshankar.ServerCall;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shivshankar.Application.App;
import com.shivshankar.customcontrols.DialogHorizontalView;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonVariables;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mayura on 15/12/2016.
 */

public class APIs {
    public static void SellerLogin(AppCompatActivity activity, OnResult onresult, String strUserId, String strPassword, String strDeviceType, String strDeviceUUID, String strModelName, String strOSVersion, String GCMRegistraionId) {

        Uri uri = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/" + "SellerLogin")
                .appendQueryParameter("strUserId", strUserId)
                .appendQueryParameter("strPassword", strPassword)
                .appendQueryParameter("strDeviceType", strDeviceType)
                .appendQueryParameter("strDeviceUUID", strDeviceUUID)
                .appendQueryParameter("strModelName", strModelName)
                .appendQueryParameter("strOSVersion", strOSVersion)
                .appendQueryParameter("GCMRegistraionId", GCMRegistraionId)

                .build();
        String url = uri.toString();
        callAPI(activity, onresult, url);
    }

    public static void SellerForgotPassward(AppCompatActivity activity, OnResult onresult, String strUserId) {

        Uri uri = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/" + "SellerForgotPassward")
                .appendQueryParameter("strUserId", strUserId)
                .build();
        String url = uri.toString();
        callAPI(activity, onresult, url);
    }


    public static void GetCountryList(AppCompatActivity activity, OnResult onresult) {
        Uri uri = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/GetCountryList")
                .build();
        String query = uri.toString();
        callAPI(null, onresult, query);
    }


    public static void GetSellerProfile(AppCompatActivity activity, OnResult onresult, String strLoginId) {
        Uri uri = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/GetSellerProfile")
                .appendQueryParameter("loginId", strLoginId)
                .build();
        String query = uri.toString();
        callAPI(activity, onresult, query);
    }


    public static void UpdateSellerProfile(AppCompatActivity activity, OnResult onresult, String loginId,
                                           String fname, String email, String mobile, String city, String pincode,
                                           String State, String strCountryCode) {

        String query = commonVariables.SERVER_BASIC_URL + "MobileAPI/UpdateSellerProfile";

        Map<String, String> params = new HashMap<String, String>();
        params.put("loginId", loginId);
        params.put("sellername", fname);
        params.put("emailId", email);
        params.put("mobileno",mobile);
        params.put("city", city);
        params.put("pincode", pincode);
        params.put("state", State);
        params.put("countrycode", strCountryCode);

        APIs.callPostAPI(activity, onresult, query, params);
    }


    static DialogHorizontalView mView = null;

    public static void callAPI(AppCompatActivity activity, OnResult onresult, String url) {
        try {
            Log.d("TAGRK", url);
            if (activity != null) {
                mView = new DialogHorizontalView();
                mView.show(activity.getSupportFragmentManager(), "load");
            }
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    response -> {
                        Log.d("TAGRK", response.toString());
                        onresult.onResult(response);
                        if (mView != null)
                            mView.dismiss();
                    }, error -> {
                VolleyLog.d("TAGRK", "Error: " + error.getMessage());
                onresult.onResult(null);
                if (mView != null)
                    mView.dismiss();
            });
            jsonObjReq.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            App.getInstance().addToRequestQueue(jsonObjReq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void callPostAPI(AppCompatActivity activity, OnResult onresult, String url, Map<String, String> params) {

        try {
            Log.d("TAGRK", url);
            if (activity != null) {
                mView = new DialogHorizontalView();
                mView.show(activity.getSupportFragmentManager(), "load");
            }
            JSONObject parameters = new JSONObject(params);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, parameters,
                    response -> {
                        Log.d("TAGRK", response.toString());
                        if (mView != null)
                            mView.dismiss();
                        onresult.onResult(response);
                    }, error -> {
                VolleyLog.d("TAGRK", "Error: " + error.getMessage());
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    VolleyError error1 = new VolleyError(new String(error.networkResponse.data));
                    Log.v("TAGRK", "Error: " + error1.toString());
                }


                onresult.onResult(null);
                if (mView != null)
                    mView.dismiss();
            }) {
//                @Override
//                protected Map<String, String> getParams() {
//                    return params;
//                }
                //            @Override
                //            public Map<String, String> getHeaders() throws AuthFailureError {
                //                HashMap<String, String> headers = new HashMap<String, String>();
                //                headers.put("Content-Type", "application/json");
                //                headers.put("apiKey", "xxxxxxxxxxxxxxx");
                //                return headers;
                //            }
            };

            App.getInstance().addToRequestQueue(jsonObjReq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
