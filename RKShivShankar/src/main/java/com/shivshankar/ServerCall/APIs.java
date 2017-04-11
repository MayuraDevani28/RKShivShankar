package com.shivshankar.ServerCall;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.shivshankar.Application.App;
import com.shivshankar.customcontrols.DialogHorizontalView;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.OnResultString;
import com.shivshankar.utills.commonVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mayura on 15/12/2016.
 */

public class APIs {


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

    public static void callPostAPI(AppCompatActivity activity, OnResult onresult, String url, Map<String, String> params, JSONObject job) {

        try {
            if (params != null) {
                Log.d("TAGRK", url + "\n" + params.toString());
            } else {
                Log.d("TAGRK", url + "\n" + job.toString());
            }
            try {
                if (activity != null) {
                    mView = new DialogHorizontalView();
                    mView.show(activity.getSupportFragmentManager(), "load");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject parameters = new JSONObject();
            if (params != null)
                parameters = new JSONObject(params.toString());
            else if (job != null)
                parameters = job;

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, parameters,
                    response -> {
                        try {
                            Log.d("TAGRK", response.toString());
                            try {
                                if (mView != null)
                                    mView.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            onresult.onResult(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    VolleyError error1 = new VolleyError(new String(error.networkResponse.data));
                    Log.v("TAGRK", "Error: " + error1.toString());
                }
                onresult.onResult(null);
                try {
                    if (mView != null)
                        mView.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance().addToRequestQueue(jsonObjReq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class MultipartCreateBrand extends AsyncTask<String, String, String> {
        AppCompatActivity activity;
        OnResultString onresult;
        DialogHorizontalView mView;
        String responseString = null;
        File file;
        String strBrand, strBrandId;
        ImageView imageView;


        public MultipartCreateBrand(AppCompatActivity activity, OnResultString onresult, String strBrandname, File file, ImageView imageView, String strBrandId) {
            this.activity = activity;
            this.onresult = onresult;
            this.file = file;
            strBrand = strBrandname;
            this.file = file;
            this.imageView = imageView;
            this.strBrandId = strBrandId;
        }


        @Override
        protected String doInBackground(String... strings) {
            return uploadFile(strBrand, imageView, file, strBrandId);
        }


        private String uploadFile(String strBrand, ImageView imageView, File file, String strBrandId) {
            try {
                String charset = "UTF-8";

                Uri uri2 = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD).path("MobileAPI/" + "CreateSellerBrand").build();
                if (!(strBrandId == null || strBrandId.isEmpty()))
                    uri2 = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD).path("MobileAPI/" + "UpdateSellerBrand").build();
                Log.d("TAGRK", uri2 + "\n");
                try {
                    MultipartUtility multipart = new MultipartUtility(uri2.toString(), charset);
                    multipart.addFilePart("ImageFile", file);
                    multipart.addFormField("strBrandName", strBrand);
                    Log.d("TAGRK", strBrand);
                    if (!(strBrandId == null || strBrandId.isEmpty())) {
                        multipart.addFormField("brandId", strBrandId);
                        Log.d("TAGRK", ", " + strBrandId);
                    }
                    multipart.addFormField("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"));
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

    public static class MultipartCreateProduct extends AsyncTask<String, String, String> {
        AppCompatActivity activity;
        OnResultString onresult;
        DialogHorizontalView mView;
        String responseString = null;
        File file;
        ImageView imageView;

        String strProductId, strBrandId, strCategory, strTop, strBottom, strDupatta, strAllOver, strFabricType, strPrice, strMinQty;
        boolean checked;


        public MultipartCreateProduct(AppCompatActivity activity, OnResultString onresult, File file, ImageView imageView,
                                      String strProductId, String strBrandId, String strCategory, String strTop, String strBottom, String strDupatta, String strAllOver, boolean checked, String strFabricType, String strPrice, String strMinQty) {
            this.activity = activity;
            this.onresult = onresult;
            this.file = file;
            this.file = file;
            this.imageView = imageView;
            this.strProductId = strProductId;
            this.strBrandId = strBrandId;
            this.strCategory = strCategory;
            this.strTop = strTop;
            this.strBottom = strBottom;
            this.strDupatta = strDupatta;
            this.strAllOver = strAllOver;
            this.strFabricType = strFabricType;
            this.strPrice = strPrice;
            this.strMinQty = strMinQty;
            this.checked = checked;
        }


        @Override
        protected String doInBackground(String... strings) {
            return uploadFile();
        }


        private String uploadFile() {
            try {
                String charset = "UTF-8";

                Uri uri2 = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD).path("MobileAPI/" + "AddUpdateProduct_Suit").build();
                Log.d("TAGRK", uri2 + "\n");
                try {
                    MultipartUtility multipart = new MultipartUtility(uri2.toString(), charset);
                    multipart.addFormField("productId", strProductId);
                    multipart.addFormField("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"));
                    multipart.addFormField("brandId", strBrandId);
                    multipart.addFormField("categoryId", strCategory);
                    multipart.addFormField("topFabricId", strTop);
                    multipart.addFormField("bottomFabricId", strBottom);
                    multipart.addFormField("dupattaFabricId", strDupatta);
                    multipart.addFormField("fabricId", strAllOver);
                    multipart.addFormField("IsallOverFlag", checked + "");
                    multipart.addFormField("fabricType", strFabricType);
                    multipart.addFormField("price", strPrice);
                    multipart.addFormField("minOrderQty", strMinQty);
                    multipart.addFilePart("ImageFile", file);
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


    public static void SellerBuyerLogin(AppCompatActivity activity, OnResult onresult, String strUserId, String strPassword, String strDeviceType, String strDeviceUUID, String strModelName, String strOSVersion, int strLoginType) {

        Uri uri = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/" + "SellerBuyerLogin")
                .appendQueryParameter("loginType", strLoginType + "")
                .appendQueryParameter("strUserId", strUserId)
                .appendQueryParameter("strPassword", strPassword)
                .appendQueryParameter("strDeviceType", strDeviceType)
                .appendQueryParameter("strDeviceUUID", strDeviceUUID)
                .appendQueryParameter("strModelName", strModelName)
                .appendQueryParameter("strOSVersion", strOSVersion)
                .appendQueryParameter("GCMRegistraionId", FirebaseInstanceId.getInstance().getToken())

                .build();
        String url = uri.toString();
        callAPI(activity, onresult, url);
    }

    public static void SellerBuyerForgotPassward(AppCompatActivity activity, OnResult onresult, String strUserId, int strLoginType) {

        Uri uri = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/" + "SellerBuyerForgotPassward")
                .appendQueryParameter("loginType", strLoginType + "")
                .appendQueryParameter("strUserName", strUserId)
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


    public static void GetSellerProfile(AppCompatActivity activity, OnResult onresult) {
        Uri uri = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/GetSellerProfile")
                .appendQueryParameter("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"))
                .build();
        String query = uri.toString();
        callAPI(activity, onresult, query);
    }


    public static void UpdateSellerProfile(AppCompatActivity activity, OnResult onresult,
                                           String fname, String email, String mobile, String city, String pincode,
                                           String State, String strCountryCode) {

        String query = commonVariables.SERVER_BASIC_URL + "MobileAPI/UpdateSellerProfile";

        Map<String, String> params = new HashMap<String, String>();
        params.put("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"));
        params.put("sellername", fname);
        params.put("emailId", email);
        params.put("mobileno", mobile);
        params.put("city", city);
        params.put("pincode", pincode);
        params.put("state", State);
        params.put("countrycode", strCountryCode);

        APIs.callPostAPI(activity, onresult, query, params, null);
    }

    public static void SellerChangePassword(AppCompatActivity activity, OnResult onresult, String strPassword, String strNewPassword) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/SellerChangePassword")
                .appendQueryParameter("oldPassword", strPassword)
                .appendQueryParameter("newPassword", strNewPassword)
                .appendQueryParameter("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0")).build();
        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);
    }

    public static void GetSellerBrandList(AppCompatActivity activity, OnResult onresult) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/GetSellerBrandList")
                .appendQueryParameter("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0")).build();
        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);
    }

    public static void CreateSellerBrand(AppCompatActivity activity, OnResultString onresult, String strBrandName, File file, ImageView mAvatarImage) {
        try {
            new MultipartCreateBrand(activity, onresult, strBrandName, file, mAvatarImage, "").execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void UpdateSellerBrand(AppCompatActivity activity, OnResultString onresult, String strBrandName, File file, ImageView mAvatarImage, String brandId) {
        try {
            new MultipartCreateBrand(activity, onresult, strBrandName, file, mAvatarImage, brandId).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void RemoveSellerBrand(AppCompatActivity activity, OnResult onresult, String brandId) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/RemoveSellerBrand")
                .appendQueryParameter("brandId", brandId)
                .appendQueryParameter("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0")).build();
        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);
    }

    public static void GetDropdowns(AppCompatActivity activity, OnResult onresult) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/GetDropdowns")
                .build();
        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);
    }

    public static void AddUpdateProduct_Suit(AppCompatActivity activity, OnResultString onresult, String strProductId, String brandId, String strCategory, String strTop, String strBottom, String strDupatta, String strAllOver, boolean checked, String strFabricType, String strPrice, String strMinQty, File file, ImageView mAvatarImage) {
        try {
            new MultipartCreateProduct(activity, onresult, file, mAvatarImage, strProductId, brandId, strCategory, strTop, strBottom, strDupatta, strAllOver, checked, strFabricType, strPrice, strMinQty).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void GetNotifications_Seller(AppCompatActivity activity, OnResult onresult) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/GetNotifications_Seller")
                .appendQueryParameter("sellerId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"))
                .build();
        String query = uri.toString();
        APIs.callAPI(null, onresult, query);
    }


    public static void GetNotifications_Buyer(AppCompatActivity activity, OnResult onresult) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/GetNotifications_Buyer")
                .appendQueryParameter("CustomerId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"))
                .build();
        String query = uri.toString();
        APIs.callAPI(null, onresult, query);
    }

    public static void RemoveNotifications_Seller(AppCompatActivity activity, OnResult onresult, String notificationCustBindId) {

        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/RemoveNotifications_Seller")
                .appendQueryParameter("sellerId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"))
                .appendQueryParameter("notificationCustBindId", notificationCustBindId)
                .build();
        String query = uri.toString();
        APIs.callAPI(null, onresult, query);
    }

    public static void RemoveNotifications_Buyer(AppCompatActivity activity, OnResult onresult, String notificationCustBindId) {

        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/RemoveNotifications_Buyer")
                .appendQueryParameter("CustomerId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"))
                .appendQueryParameter("notificationCustBindId", notificationCustBindId)
                .build();
        String query = uri.toString();
        APIs.callAPI(null, onresult, query);
    }

    public static void GetProductList_Suit_Seller(AppCompatActivity activity, OnResult onresult, String srchCategoryId, int pageNo, String srchProductCode) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/GetProductList_Suit_Seller")
                .appendQueryParameter("pageNo", pageNo + "")
                .appendQueryParameter("srchCategoryId", srchCategoryId)
                .appendQueryParameter("srchProductCode", srchProductCode)
                .appendQueryParameter("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0")).build();
        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);
    }

    public static void RemoveProduct_Suit(AppCompatActivity activity, OnResult onresult, String productId) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/RemoveProduct_Suit")
                .appendQueryParameter("productId", productId)
                .appendQueryParameter("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0")).build();
        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);
    }

    public static void GetProductDetail_Suit_Seller(AppCompatActivity activity, OnResult onresult, String productId) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/GetProductDetail_Suit_Seller")
                .appendQueryParameter("productId", productId)
                .appendQueryParameter("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0")).build();
        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);
    }

    public static void GetCategory(AppCompatActivity activity, OnResult onresult) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/GetCategory")
                .build();
        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);
    }


    public static void GetBuyerHome(AppCompatActivity activity, OnResult onresult) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/GetBuyerHome")
                .appendQueryParameter("buyerLoginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0")).build();
        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);

    }

    public static void GetBuyerBrands(AppCompatActivity activity, OnResult onresult, String categoryId, String strFabricType) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/GetBuyerBrands")
                .appendQueryParameter("categoryId", categoryId)
                .appendQueryParameter("fabricType", strFabricType)
                .appendQueryParameter("buyerLoginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0")).build();
        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);
    }

    public static void GetProductList_Suit_Buyer(AppCompatActivity activity, OnResult onresult, String brandId, int pageNo, String strcategoryIds, String strpriceRange, String strfabricIds, String strSortBy, String strFabricType, String strCatidSuitFabric) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/GetProductList_Suit_Buyer")
                .appendQueryParameter("pageNo", pageNo + "")
                .appendQueryParameter("brandId", brandId)
                .appendQueryParameter("categoryIds", strcategoryIds)
                .appendQueryParameter("priceRange", strpriceRange)
                .appendQueryParameter("fabricIds", strfabricIds)
                .appendQueryParameter("sortBy", strSortBy)
                .appendQueryParameter("FabricType", strFabricType)
                .appendQueryParameter("SuitFabricId", strCatidSuitFabric)
                .appendQueryParameter("buyerLoginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0")).build();
        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);

    }


    public static void SellerBuyerRegister(AppCompatActivity activity, OnResult onresult, String name, String email, String password, String mobile, String city, String company, String strDeviceUUID, int loginType) {

        String strModelName = Build.MODEL;
        String strOSVersion = Build.VERSION.RELEASE;
        String strDeviceType = "Android" + strDeviceUUID;

        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/SellerBuyerRegister")
                .appendQueryParameter("loginType", loginType + "")
                .appendQueryParameter("strName", name)
                .appendQueryParameter("strEmailId", email)
                .appendQueryParameter("strMobile", mobile)
                .appendQueryParameter("strFirmName", company)
                .appendQueryParameter("strPassword", password)
                .appendQueryParameter("strCity", city)
                .appendQueryParameter("strDeviceType", strDeviceType)
                .appendQueryParameter("strDeviceUUID", strDeviceUUID)
                .appendQueryParameter("strModelName", strModelName)
                .appendQueryParameter("strOSVersion", strOSVersion)
                .appendQueryParameter("GCMRegistraionId", FirebaseInstanceId.getInstance().getId())
                .build();

        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);
    }

    public static void VerifyOTP(AppCompatActivity activity, OnResult onresult, String strOTP, int loginType) {
        Uri uri = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/VerifyOTP")
                .appendQueryParameter("loginType", loginType + "")
                .appendQueryParameter("strUserName", AppPreferences.getPrefs().getString(commonVariables.KEY_USER_ID_FOR_OTP, ""))
                .appendQueryParameter("strOTP", strOTP)
                .build();
        String query = uri.toString();
        Log.v("TAG", "CAlling With:" + query);
        APIs.callAPI(activity, onresult, query);
    }


    public static void ResendRegistrationOTP(AppCompatActivity activity, OnResult onresult, int loginType) {
        Uri uri = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/ResendRegistrationOTP")
                .appendQueryParameter("loginType", loginType + "")
                .appendQueryParameter("strUserName", AppPreferences.getPrefs().getString(commonVariables.KEY_USER_ID_FOR_OTP, ""))
                .build();
        String query = uri.toString();
        Log.v("TAG", "CAlling With:" + query);
        APIs.callAPI(activity, onresult, query);
    }

    public static void GetBuyerProfile(AppCompatActivity activity, OnResult onresult) {
        Uri uri = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/GetBuyerProfile")
                .appendQueryParameter("buyerLoginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"))
                .build();
        String query = uri.toString();
        callAPI(activity, onresult, query);
    }

    public static void UpdateBuyerProfile(AppCompatActivity activity, OnResult onresult,
                                          String fname, String email, String mobile, String city, String pincode,
                                          String State, String strCountryCode) {

        String query = commonVariables.SERVER_BASIC_URL + "MobileAPI/UpdateBuyerProfile";

        Map<String, String> params = new HashMap<String, String>();
        params.put("buyerLoginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"));
        params.put("buyername", fname);
        params.put("emailId", email);
        params.put("mobileno", mobile);
        params.put("city", city);
        params.put("pincode", pincode);
        params.put("state", State);
        params.put("countrycode", strCountryCode);

        APIs.callPostAPI(activity, onresult, query, params, null);
    }


    public static void BuyerChangePassword(AppCompatActivity activity, OnResult onresult, String strPassword, String strNewPassword) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/BuyerChangePassword")
                .appendQueryParameter("oldPassword", strPassword)
                .appendQueryParameter("newPassword", strNewPassword)
                .appendQueryParameter("buyerLoginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0")).build();
        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);
    }


    public static void GetProductListFilters_Buyer(AppCompatActivity activity, OnResult onresult, String strFabricType, String brandId, int suit_or_fab) {

        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/GetProductListFilters_Buyer")
                .appendQueryParameter("brandId", brandId)
                .appendQueryParameter("fabricType", strFabricType)
                .appendQueryParameter("fabricOrSuitCatId", suit_or_fab + "")
                .appendQueryParameter("buyerLoginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0")).build();
        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);

    }


    public static void ProductActiveInactive(AppCompatActivity activity, OnResult onresult, String productId, boolean isActive) {

        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/ProductActiveInactive")
                .appendQueryParameter("productId", productId + "")
                .appendQueryParameter("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"))
                .appendQueryParameter("isActive", isActive + "")
                .build();
        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);

    }


    public static void GetPolicies(AppCompatActivity activity, OnResult onresult, String api) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD).path("MobileAPI/" + api)
                .build();

        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);

    }

    public static void GetCMSPages(AppCompatActivity activity, OnResult onresult) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD).path("MobileAPI/GetCMSPages")
                .build();

        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);
    }

    public static void AddProductToCart_Suit_Buyer(AppCompatActivity activity, OnResult onresult, JSONArray jarray) {

        String query = commonVariables.SERVER_BASIC_URL + "MobileAPI/AddProductToCart_Suit_Buyer";

        try {
            JSONObject jo = new JSONObject();
            jo.put("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"));
            jo.put("lstProduct", jarray);

            JSONObject jobj = new JSONObject();
            jobj.put("mdlSuitCart", jo);


//            Map<String, String> params = new HashMap<String, String>();
//            params.put("mdlSuitCart", jo.toString());

            APIs.callPostAPI(activity, onresult, query, null, jobj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void GetCartList_Suit_Buyer(AppCompatActivity activity, OnResult onresult) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD).path("MobileAPI/GetCartList_Suit_Buyer")
                .appendQueryParameter("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"))
                .build();

        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);
    }


    public static void RemoveCartProduct(AppCompatActivity activity, OnResult onresult, String productId) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD).path("MobileAPI/RemoveCartProduct")
                .appendQueryParameter("productId", productId)
                .appendQueryParameter("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"))
                .build();

        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);
    }


    public static void Update_Cart_Suit(AppCompatActivity activity, OnResult onresult, String cartId, String qty, String minQty) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD).path("MobileAPI/Update_Cart_Suit")
                .appendQueryParameter("CartId", cartId)
                .appendQueryParameter("newQty", qty)
                .appendQueryParameter("MinOdrQty", minQty)
                .appendQueryParameter("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"))
                .build();

        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);
    }


    public static void GetOrderSummary_Suit(AppCompatActivity activity, OnResult onresult) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD).path("MobileAPI/GetOrderSummary_Suit")
                .appendQueryParameter("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"))
                .build();

        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);
    }

    public static void ClearCart(AppCompatActivity activity, OnResult onresult) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD).path("MobileAPI/ClearCart")
                .appendQueryParameter("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"))
                .build();

        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);
    }

    public static void AddUpdateOrder_Suit(AppCompatActivity activity, OnResult onresult, String orderId, String strDeviceType, String strDeviceUUID, String strModelName, String strOSVersion, String bstrFname, String bstrAddress1, String bstrAddress2, String bstrPinCode, String bstrCity, String bstrState, String bstrCity1, String bstrCountryCode, String bstrMobile, String sstrFname, String sstrAddress1, String sstrAddress2, String sstrPinCode, String sstrCity, String sstrState, String sstrCity1, String sstrCountryCode, String sstrMobile, String note) {
        String query = commonVariables.SERVER_BASIC_URL + "MobileAPI/AddUpdateOrder_Suit";

        try {
            JSONObject jo = new JSONObject();
            jo.put("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"));
            jo.put("custOrderId", orderId);
            jo.put("DeviceType", strDeviceType);
            jo.put("DeviceUUID", strDeviceUUID);
            jo.put("ModelName", strModelName);
            jo.put("OSVersion", strOSVersion);
            jo.put("GCMRegistraionNo", FirebaseInstanceId.getInstance().getToken());
            jo.put("AppStoreRegistraionNo", "");
            jo.put("bFullName", bstrFname);
            jo.put("bAddress1", bstrAddress1);
            jo.put("bAddress2", bstrAddress2);
            jo.put("bPinCode", bstrPinCode);
            jo.put("bCity", bstrCity);
            jo.put("bState", bstrState);
            jo.put("bCountryCode", bstrCountryCode);
            jo.put("bMobileNo", bstrMobile);
            jo.put("sFullName", sstrFname);
            jo.put("sAddress1", sstrAddress1);
            jo.put("sAddress2", sstrAddress2);
            jo.put("sPinCode", sstrPinCode);
            jo.put("sCity", sstrCity1);
            jo.put("sState", sstrState);
            jo.put("sCountryCode", sstrCountryCode);
            jo.put("sMobileNo", sstrMobile);
            jo.put("customerNote", note);

            JSONObject jobj = new JSONObject();
            jobj.put("mdlOrder", jo);

//            Map<String, String> params = new HashMap<String, String>();
//            params.put("mdlOrder", jo.toString());

            APIs.callPostAPI(activity, onresult, query, null, jobj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void GetOrderDetailsMini(AppCompatActivity activity, OnResult onresult, String orderId) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD).path("MobileAPI/GetOrderDetailsMini")
                .appendQueryParameter("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"))
                .appendQueryParameter("orderId", orderId)
                .build();

        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);
    }

    public static void GetMyOrders(AppCompatActivity activity, OnResult onresult, int pageNo) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD).path("MobileAPI/GetMyOrders")
                .appendQueryParameter("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"))
                .appendQueryParameter("PageNo", pageNo + "")
                .build();

        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);
    }

    public static void GetOrderDetails(AppCompatActivity activity, OnResult onresult, String orderId) {
        String strSellerId = AppPreferences.getPrefs().getBoolean(commonVariables.KEY_IS_SELLER, false) ? AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0") : "0";
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD).path("MobileAPI/GetOrderDetails")
                .appendQueryParameter("orderId", orderId)
                .appendQueryParameter("sellerId", strSellerId)
                .build();

        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);
    }

    public static void GetOrderList_Seller(AppCompatActivity activity, OnResult onresult, int pageNo) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD).path("MobileAPI/GetOrderList_Seller")
                .appendQueryParameter("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"))
                .appendQueryParameter("PageNo", pageNo + "")
                .build();

        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);
    }


    public static void CheckAppVersion(OnResult onresult, String appVersionCode) {
        Uri uri = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("mobileapi/CheckAppVersion")
                .appendQueryParameter("OSVersion", "android")
                .appendQueryParameter("appVersionCode", appVersionCode)
                .appendQueryParameter("sellerId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"))
                .build();
        String query = uri.toString();
        APIs.callAPI(null, onresult, query);
    }

    public static void RemoveAllNotifications_Buyer(AppCompatActivity activity, OnResult onresult) {
        Uri uri = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("mobileapi/RemoveAllNotifications_Buyer")
                .appendQueryParameter("CustomerId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"))
                .build();
        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);
    }

    public static void RemoveAllNotifications_Seller(AppCompatActivity activity, OnResult onresult) {
        Uri uri = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("mobileapi/RemoveAllNotifications_Seller")
                .appendQueryParameter("sellerId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"))
                .build();
        String query = uri.toString();
        APIs.callAPI(activity, onresult, query);
    }
}
