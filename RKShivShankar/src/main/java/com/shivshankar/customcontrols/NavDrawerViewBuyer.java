package com.shivshankar.customcontrols;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shivshankar.BaseActivityBuyer;
import com.shivshankar.CMSCallandDisplayActivityBuyer;
import com.shivshankar.CMSListingActivityBuyer;
import com.shivshankar.CartActivityBuyer;
import com.shivshankar.LoginRegisterActivity;
import com.shivshankar.MainActivityBuyer;
import com.shivshankar.MyOrdersActivityBuyer;
import com.shivshankar.MyProfileActivityBuyer;
import com.shivshankar.NotificationsActivityBuyer;
import com.shivshankar.OrderFormActivityBuyer;
import com.shivshankar.OrderSuccessActivityBuyer;
import com.shivshankar.R;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.commonMethods;
import com.shivshankar.utills.commonVariables;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONException;
import org.json.JSONObject;

import static com.shivshankar.utills.AppPreferences.getPrefs;

/**
 * Created by Mayura on 12/1/2016.
 */
public class NavDrawerViewBuyer extends LinearLayout implements View.OnClickListener {

    LinearLayout mNav_my_profile, mNav_my_orders, mNav_about_us, mNav_our_policy, mNav_contact_us, mLl_close;
    TextView mTv_username, mTv_logout;
    ImageView mIv_logo_nav, iv_twitter, iv_fb, iv_g_plus, iv_pinterest, iv_youtube, iv_insta, iv_linkedin;
    AppCompatActivity activity;

    public NavDrawerViewBuyer(Context context) {
        super(context);
        setup();
        activity = (AppCompatActivity) context;
    }

    public NavDrawerViewBuyer(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
        activity = (AppCompatActivity) context;
    }

    private void setup() {
        try {
            inflate(getContext(), R.layout.view_navigation_buyer, this);

            mNav_my_profile = (LinearLayout) findViewById(R.id.nav_my_profile);
            mNav_my_orders = (LinearLayout) findViewById(R.id.nav_my_orders);
            mNav_about_us = (LinearLayout) findViewById(R.id.nav_about_us);
            mNav_our_policy = (LinearLayout) findViewById(R.id.nav_our_policy);
            mNav_contact_us = (LinearLayout) findViewById(R.id.nav_contact_us);

            mIv_logo_nav = (ImageView) findViewById(R.id.iv_logo_nav);

            mTv_username = (TextView) findViewById(R.id.tv_username);
            mTv_logout = (TextView) findViewById(R.id.tv_logout);
            mLl_close = (LinearLayout) findViewById(R.id.ll_close);

            mIv_logo_nav.setOnClickListener(this);
            mTv_username.setOnClickListener(this);
            mTv_logout.setOnClickListener(this);
            mLl_close.setOnClickListener(this);

            mNav_my_profile.setOnClickListener(this);
            mNav_my_orders.setOnClickListener(this);
            mNav_about_us.setOnClickListener(this);
            mNav_our_policy.setOnClickListener(this);
            mNav_contact_us.setOnClickListener(this);
            setLoginLogout();
            setUserName();

            iv_twitter = (ImageView) findViewById(R.id.iv_twitter);
            iv_fb = (ImageView) findViewById(R.id.iv_fb);
            iv_youtube = (ImageView) findViewById(R.id.iv_youtube);
            iv_pinterest = (ImageView) findViewById(R.id.iv_pinterest);
            iv_g_plus = (ImageView) findViewById(R.id.iv_g_plus);
            iv_insta = (ImageView) findViewById(R.id.iv_insta);
            iv_linkedin = (ImageView) findViewById(R.id.iv_linkedin);

            iv_twitter.setOnClickListener(this);
            iv_fb.setOnClickListener(this);
            iv_youtube.setOnClickListener(this);
            iv_pinterest.setOnClickListener(this);
            iv_g_plus.setOnClickListener(this);
            iv_insta.setOnClickListener(this);
            iv_linkedin.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUserName() {
        try {
            if (mTv_username != null) {
                String strProfile = AppPreferences.getPrefs().getString(commonVariables.KEY_BUYER_PROFILE, "");
                if (!strProfile.isEmpty() && !strProfile.equalsIgnoreCase("null"))
                    mTv_username.setText(WordUtils.capitalizeFully(new JSONObject(strProfile).optString("Name")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setLoginLogout() {
        try {
            if (!getPrefs().getBoolean(commonVariables.KEY_IS_LOG_IN, false)) {
                findViewById(R.id.v_order).setVisibility(View.GONE);
                findViewById(R.id.v_prof).setVisibility(View.GONE);
                mTv_logout.setText("Login");
                mNav_my_profile.setVisibility(View.GONE);
                mNav_my_orders.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        try {
            closeDrawer(activity);
            if (v == mNav_my_profile) {
                activity.startActivity(new Intent(activity, MyProfileActivityBuyer.class));
                activity.overridePendingTransition(0, 0);
            } else if (v == mNav_my_orders) {
                activity.startActivity(new Intent(activity, MyOrdersActivityBuyer.class));
                activity.overridePendingTransition(0, 0);
            } else if (v == mNav_about_us) {
                Intent intent = new Intent(activity, CMSCallandDisplayActivityBuyer.class);
                intent.putExtra(commonVariables.INTENT_EXTRA_PAGE_NAME, "aboutus");
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
            } else if (v == mNav_our_policy) {
                Intent intent = new Intent(activity, CMSListingActivityBuyer.class);
                intent.putExtra(commonVariables.INTENT_EXTRA_PAGE, "GetPolicies");
                intent.putExtra(commonVariables.INTENT_EXTRA_PAGE_NAME, "Our Policy");
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
            } else if (v == mNav_contact_us) {
                Intent intent = new Intent(activity, CMSCallandDisplayActivityBuyer.class);
                intent.putExtra(commonVariables.INTENT_EXTRA_PAGE_NAME, "contactus");
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
            } else if (v == mTv_logout) {
                if (mTv_logout.getText().equals("Login")) {
                    activity.startActivity(new Intent(activity, LoginRegisterActivity.class));
                    if (!(activity instanceof MainActivityBuyer))
                        activity.onBackPressed();
                } else {
                    commonMethods.logout(activity, true);
                }
            } else if (v == iv_fb) {
                String strFacebookLink = "1212377813sa12539";
                try {
                    Uri uri;
                    if (strFacebookLink.contains("www.facebook.com")) {
                        uri = Uri.parse(strFacebookLink);
                    } else {
                        activity.getPackageManager().getPackageInfo("com.facebook.katana", 0);
                        uri = Uri.parse("fb://page/" + strFacebookLink);
                    }
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                } catch (Exception e) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/RKShivshankar/")));
                }
                activity.overridePendingTransition(0, 0);
            } else if (v == iv_twitter) {
                Intent intent = null;
                try {
                    String strTwitterLink = "https://twitter.com/RKShivshankar";
                    if (strTwitterLink.equalsIgnoreCase(""))
                        strTwitterLink = "twitter://user?user_id=USERID";
                    if (strTwitterLink.startsWith("twitter:/")) {
                        activity.getPackageManager().getPackageInfo("com.twitter.android", 0);
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strTwitterLink));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    } else {
                        if (strTwitterLink.equalsIgnoreCase("twitter://user?user_id=USERID"))
                            strTwitterLink = "https://twitter.com/PROFILENAME";
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(strTwitterLink)));
                    }
                } catch (Exception e) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/PROFILENAME"));
                }
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
            } else if (v == iv_youtube) {
                String strYouTubeLink = "https://www.youtube.com/user/RKShivshankar";
                Intent intent;
                try {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setPackage("com.google.android.youtube");
                    intent.setData(Uri.parse(strYouTubeLink));
                    activity.startActivity(intent);
                    activity.overridePendingTransition(0, 0);
                } catch (ActivityNotFoundException e) {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(strYouTubeLink));
                    activity.startActivity(intent);
                    activity.overridePendingTransition(0, 0);
                }
            } else if (v == iv_pinterest) {
                try {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("pinterest://www.pinterest.com/")));
                } catch (Exception e) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.pinterest.com/")));
                }
            } else if (v == iv_g_plus) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setClassName("com.google.android.apps.plus",
                            "com.google.android.apps.plus.phone.UrlGatewayActivity");
                    //                    intent.putExtra("customAppUri", profile);
                    activity.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/")));
                }
            } else if (v == iv_insta) {
                Uri uri = Uri.parse("http://instagram.com/_u/RKShivshankar");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    activity.startActivity(likeIng);
                } catch (Exception e) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/RKShivshankar")));
                }
            } else if (v == iv_linkedin) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/"));
                try {
                    activity.getPackageManager().getPackageInfo("com.linkedin.android", 0);
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://profile/yourID"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeDrawer(AppCompatActivity activity) {
        if (activity instanceof BaseActivityBuyer)
            ((BaseActivityBuyer) activity).drawer.closeDrawer(Gravity.LEFT);
        else {
            if (activity instanceof CartActivityBuyer)
                ((CartActivityBuyer) activity).drawer.closeDrawer(Gravity.LEFT);
            else if (activity instanceof NotificationsActivityBuyer)
                ((NotificationsActivityBuyer) activity).drawer.closeDrawer(Gravity.LEFT);
            else if (activity instanceof OrderFormActivityBuyer)
                ((OrderFormActivityBuyer) activity).drawer.closeDrawer(Gravity.LEFT);
            else if (activity instanceof OrderSuccessActivityBuyer)
                ((OrderSuccessActivityBuyer) activity).drawer.closeDrawer(Gravity.LEFT);
        }
    }

}
