package com.shivshankar.ServerCall;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shivshankar.Application.App;
import com.shivshankar.customcontrols.DialogHorizontalView;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.OnResultString;
import com.shivshankar.utills.commonVariables;

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

        APIs.callPostAPI(activity, onresult, query, params);
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

    public static void callGetNotificationsAPI(AppCompatActivity activity, OnResult onresult) {
        Uri uri = new Uri.Builder().scheme("http")
                .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                .path("MobileAPI/GetNotifications")
                .appendQueryParameter("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"))
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
            Log.d("TAGRK", url + "\n" + params.toString());
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


//    public static void callMultipartAPI(AppCompatActivity activity, OnResult onresult, String url, Map<String, String> params, File file, ImageView mAvatarImage) {
//
//        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, response -> {
//            String resultResponse = new String(response.data);
//            Log.d("TAGRK", resultResponse.toString());
//        }, error -> {
//            error.printStackTrace();
//            if (error.networkResponse != null && error.networkResponse.data != null) {
//                VolleyError error1 = new VolleyError(new String(error.networkResponse.data));
//                Log.v("TAGRK", "Error: " + error1.toString());
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                return params;
//            }
//
//            @Override
//            protected Map<String, DataPart> getByteData() {
//                Map<String, DataPart> params = new HashMap<>();
//                // file name could found file base or direct access from real path
//                // for now just get bitmap data from ImageView
//                params.put("ImageFile", new DataPart("file.jpg", getFileDataFromDrawable(activity.getBaseContext(), mAvatarImage.getDrawable()), "image/jpeg"));
//
//                return params;
//            }
//        };
//        App.getInstance().addToRequestQueue(multipartRequest);
//    }
//
//    public static byte[] getFileDataFromDrawable(Context context, Drawable drawable) {
//        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
//        return byteArrayOutputStream.toByteArray();
//    }


}
