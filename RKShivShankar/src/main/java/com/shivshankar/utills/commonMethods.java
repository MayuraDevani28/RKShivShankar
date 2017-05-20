package com.shivshankar.utills;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.shivshankar.FabricProductsActivityBuyer;
import com.shivshankar.ProductsActivityBuyer;
import com.shivshankar.R;
import com.shivshankar.SplashActivity;
import com.shivshankar.classes.SC3Object;
import com.shivshankar.classes.Suggestion;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;

@SuppressLint("SimpleDateFormat")
public class commonMethods {

    public static boolean isOnline(AppCompatActivity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static int getIndexOf(ArrayList<SC3Object> listCountry, String strCountryCode) {
        int ccode = 254;
        for (int i = 0; i < listCountry.size(); i++) {
            if (listCountry.get(i).getIFSCCode().equalsIgnoreCase(strCountryCode)) {
                ccode = i;
            }
        }
        return ccode;
    }

    public static boolean knowInternetOn(Activity activity) {
        if (activity != null)
            if (isConnectedWifi(activity) || isConnectedMobile(activity)) {
                return true;
            }
        return false;
    }

    public static boolean isConnectedWifi(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public static boolean isConnectedMobile(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static void showInternetAlert(final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(activity);
                alertDialog.setTitle("Internet settings");
                alertDialog.setMessage("Internet is not on. Do you want to go to settings menu?");
                alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        activity.startActivity(intent);
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });
    }

    public static void hidesoftKeyboard(Activity activity) {
        if (activity != null) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static ArrayList<Suggestion> getSavedSearchArray() {
        ArrayList<Suggestion> mResults = new ArrayList<>();
        try {
            int size = AppPreferences.getPrefs().getInt("array_size", 0);
            for (int i = 0; i < size; i++)
                mResults.add(new Suggestion(AppPreferences.getPrefs().getString("array_" + i, null)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.reverse(mResults);
        return mResults;
    }

    public static void saveSearchArray(ArrayList<Suggestion> myResArray) {
        try {
            SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
            if (myResArray == null)
                myResArray = new ArrayList<>();
            editor.putInt("array_size", myResArray.size());
            for (int i = 0; i < myResArray.size(); i++)
                editor.putString("array_" + i, myResArray.get(i).getBody());
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void performSearch(AppCompatActivity activity, String strSearch, String catId) {

        try {
            if (!strSearch.equals("")) {

                Log.v("TAGRK", "SEARCHING FOR: " + strSearch);
                ArrayList<Suggestion> array = getSavedSearchArray();
                if (!SavedStill(array, strSearch)) {
                    array.add(new Suggestion(strSearch));
                    saveSearchArray(array);
                }

                int cId = Integer.parseInt(catId);
                Intent intent;
                if (cId == 1) {//suit
                    intent = new Intent(activity, ProductsActivityBuyer.class);
                    intent.putExtra(commonVariables.KEY_SEARCH_STR, strSearch);
                    intent.putExtra(commonVariables.KEY_CATEGORY, catId);
                } else {//fabric
//                        intent = new Intent(activity, FabricColorsActivityBuyer.class);
                    intent = new Intent(activity, FabricProductsActivityBuyer.class);
                    intent.putExtra(commonVariables.KEY_SEARCH_STR, strSearch);
                    intent.putExtra(commonVariables.KEY_CATEGORY, catId);
                }
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static boolean SavedStill(ArrayList<Suggestion> array, String strSearch) {
        boolean saved = false;
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getBody().equalsIgnoreCase(strSearch)) {
                saved = true;
                break;
            }
        }
        return saved;
    }

    public static void cartCountAnimation(final Activity activity, final TextView mTv_cart_count) {
        try {
            if (mTv_cart_count != null) {
                final int[] count = {3};
                mTv_cart_count.clearAnimation();
                Animation zoomin = AnimationUtils.loadAnimation(activity, R.anim.zoom_in);
                Animation zoomout = AnimationUtils.loadAnimation(activity, R.anim.zoom_out);
                mTv_cart_count.startAnimation(zoomin);
                zoomin.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                    }

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        if (count[0] != 0)
                            mTv_cart_count.startAnimation(zoomout);
                    }
                });

                zoomout.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                    }

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        mTv_cart_count.startAnimation(zoomin);
                        count[0]--;
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logout(AppCompatActivity activity, boolean isShowDialog) {
        if (isShowDialog) {
            AlertDialogManager.showDialogCustom(activity, "Do you want to logout?", "Logout", "Cancel", () -> commonMethods.logoutCode(activity), null);
        } else
            logoutCode(activity);
    }

    private static void logoutCode(AppCompatActivity activity) {
        SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
        editor.putString(commonVariables.KEY_LOGIN_ID, "0");
        editor.putBoolean(commonVariables.KEY_IS_LOG_IN, false);
        editor.putString(commonVariables.KEY_SELLER_PROFILE, "");
        editor.putString(commonVariables.KEY_BUYER_PROFILE, "");
        editor.putString(commonVariables.KEY_BRAND, "");
        editor.putBoolean(commonVariables.KEY_IS_BRAND, false);
        editor.putBoolean(commonVariables.KEY_IS_SELLER, false);
        editor.putBoolean(commonVariables.KEY_IS_SKIPPED_LOGIN_BUYER, false);
        editor.putBoolean(commonVariables.KEY_FIRST_TIME, false);
        editor.commit();
        Intent intent = new Intent(activity, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(0, 0);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }


    public static void startZommingAnim(View mLl_create_brand) {
        ScaleAnimation scal = new ScaleAnimation(0.5f, 1.1f, 0.5f, 1.1f, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
        scal.setDuration(500);
        scal.setFillAfter(true);
        mLl_create_brand.setAnimation(scal);
        scal.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ScaleAnimation scal = new ScaleAnimation(1.1f, 1f, 1.1f, 1f, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
                scal.setDuration(100);
                scal.setFillAfter(true);
                mLl_create_brand.setAnimation(scal);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getRealPathFromURI(AppCompatActivity activity, Uri uri) {
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        int idx = 0;
        try {
            cursor.moveToFirst();
            idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor.getString(idx);
    }


    public static void expand(final View v) {
        try {
            v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final int targetHeight = v.getMeasuredHeight();

            // Older versions of android (pre API 21) cancel animations for views with a height of 0.
            v.getLayoutParams().height = 1;
            v.setVisibility(View.VISIBLE);
            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    try {
                        int height = interpolatedTime == 1
                                ? ViewGroup.LayoutParams.WRAP_CONTENT
                                : (int) (targetHeight * interpolatedTime == 0 ? 1 : interpolatedTime);
                        if (v.getLayoutParams().height != 0 && v.getLayoutParams().width != 0) {
                            v.getLayoutParams().height = height;
                            v.requestLayout();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            // 1dp/ms
            a.setDuration(((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density)) * 6);
            v.startAnimation(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void collapse(final View v) {
        try {
            final int initialHeight = v.getMeasuredHeight();

            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    try {
                        if (interpolatedTime == 1) {
                            v.setVisibility(View.GONE);
                        } else {
                            int height = initialHeight - (int) (initialHeight * interpolatedTime);
                            if (v.getLayoutParams().height != 0 && v.getLayoutParams().width != 0) {
                                v.getLayoutParams().height = height;
                                v.requestLayout();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            // 1dp/ms
            a.setDuration(((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density)) * 6);
            v.startAnimation(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

}
