package com.shivshankar;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.classes.FabricColor;
import com.shivshankar.classes.ProductItem;
import com.shivshankar.classes.SC3Object;
import com.shivshankar.fragments.FabricColorsListFragment;
import com.shivshankar.utills.AlertDialogManager;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonVariables;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FabricColorsActivityBuyer extends BaseActivityBuyer implements OnClickListener, OnResult {

    TextView mTv_no_data_found, mTv_title, mTv_count_items;
    Button mBtn_add_now;
    private LinearLayout mLl_no_data_found;
    ViewPager viewPager;
    TabLayout tabLayout;
    LinearLayout mFl_whole, mLl_add_to_cart;
    private ImageView mIv_filer, mIv_close;
    LottieAnimationView animationView2, animationView;

    String strFabricType = "", strCatidFabric, itemId = "0";
    ArrayList<FabricColorsListFragment> listAdapter = new ArrayList<>();
    ProductItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        View rootView = getLayoutInflater().inflate(R.layout.activity_fabric_colors_buyer, frameLayout);
        rootView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));

        try {
            bindViews(rootView);

            strFabricType = getIntent().getStringExtra(commonVariables.KEY_FABRIC_TYPE);
            strCatidFabric = getIntent().getStringExtra(commonVariables.KEY_CATEGORY);
            item = (ProductItem) getIntent().getSerializableExtra(commonVariables.KEY_BRAND);
            if (item != null) {
                itemId = item.getProductId();
                mTv_title.setText(WordUtils.capitalizeFully(strFabricType + " Fabrics"));
            }

            APIs.GetProduct_ColorTypes(this, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindViews(View rootView) {

        try {
            mIv_filer = (ImageView) findViewById(R.id.iv_filer);
            mIv_filer.setVisibility(View.GONE);
            mIv_filer.setOnClickListener(this);

            mTv_title = (TextView) rootView.findViewById(R.id.tv_title);
            mIv_close = (ImageView) findViewById(R.id.iv_close);
            mIv_close.setOnClickListener(this);
            mLl_add_to_cart = (LinearLayout) rootView.findViewById(R.id.ll_add_to_cart);
            mLl_add_to_cart.setOnClickListener(this);

            mFl_whole = (LinearLayout) rootView.findViewById(R.id.fl_whole);

            mBtn_add_now = (Button) rootView.findViewById(R.id.btn_add_now);
            mBtn_add_now.setOnClickListener(this);
            mTv_no_data_found = (TextView) rootView.findViewById(R.id.tv_no_data_found);
            mTv_title.setOnClickListener(this);

            viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
            tabLayout = (TabLayout) rootView.findViewById(R.id.pager_tabs);

            mTv_count_items = (TextView) rootView.findViewById(R.id.tv_no_items);
            mLl_no_data_found = (LinearLayout) rootView.findViewById(R.id.ll_no_data_found);
            animationView = (LottieAnimationView) rootView.findViewById(R.id.animation_view);
            animationView2 = (LottieAnimationView) rootView.findViewById(R.id.animation_view2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ViewPagerAdapterDetail extends FragmentStatePagerAdapter {

        private final List<Fragment> fragments = new ArrayList<>();
        private final List<String> titles = new ArrayList<>();

        public ViewPagerAdapterDetail(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    private void startAnim() {
        animationView.setProgress(0f);
        animationView.playAnimation();
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animationView2.setProgress(0f);
                animationView2.playAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animationView2.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animationView.setProgress(0f);
                animationView.playAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();

        try {
            finishAnim();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void finishAnim() {
        try {
            animationView.cancelAnimation();
            animationView2.cancelAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            mTv_count_items.setText("");
            if (requestCode == commonVariables.REQUEST_LOGIN && resultCode == RESULT_OK) {
                boolean isLoggedIn = data.getExtras().getBoolean(commonVariables.KEY_IS_LOG_IN);
                if (isLoggedIn)
                    mLl_add_to_cart.performClick();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {
        try {
            AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
            view.startAnimation(buttonClick);
            if (view == mIv_filer) {
                Intent intent = new Intent(this, FilterActivityBuyer.class);
                intent.putExtra(commonVariables.KEY_FABRIC_TYPE, strFabricType);
                intent.putExtra(commonVariables.KEY_BRAND, itemId);
                intent.putExtra(commonVariables.KEY_SUIT_OR_FABRIC, 2);//1=suit, 2=fabric
                startActivityForResult(intent, commonVariables.REQUEST_FILTER_PRODUCT);
                overridePendingTransition(R.anim.slide_up, R.anim.hold);
            } else if (view == mIv_close) {
                Intent output = new Intent();
                setResult(RESULT_OK, output);
                finish();
                overridePendingTransition(0, 0);
            } else if (view == mBtn_add_now) {
                Intent intent = new Intent(getApplicationContext(), MainActivityBuyer.class);
                intent.putExtra(commonVariables.KEY_IS_BRAND, false);
                startActivity(intent);
                overridePendingTransition(0, 0);
            } else if (view == mLl_add_to_cart) {
                JSONArray jarr = new JSONArray();
                JSONObject jo = new JSONObject();
                jo.put("ProductId", item.getProductId());
                jo.put("ProductTotalQty", 1);
                jo.put("ProductTotalCut", 2);
                JSONArray arrStr = new JSONArray();
                for (int i = 0; i < listAdapter.size(); i++) {
                    List<FabricColor> lisarr = listAdapter.get(i).getItems();
                    for (int j = 0; j < lisarr.size(); j++) {
                        if (lisarr.get(j).isActive()) {
                            arrStr.put(lisarr.get(j).getHeaxCode());
                        }
                    }
                }
                jo.put("ColorHexCodes", arrStr);
                jarr.put(jo);
                if (arrStr.length() == 0) {
                    AlertDialogManager.showDialog(this, "Select alteast one color to add in cart", null);
                } else if (arrStr.length() > 4) {
                    AlertDialogManager.showDialog(this, "You can select only 4 colors at a time", null);
                } else {
                    if (AppPreferences.getPrefs().getBoolean(commonVariables.KEY_IS_LOG_IN, false)) {
                        APIs.AddProductToCart_Fabric_Buyer(this, this, jarr, strCatidFabric);
                    } else {
                        Intent intent = new Intent(this, LoginRegisterActivity.class);
                        intent.putExtra(commonVariables.KEY_FOR_LOGIN, true);
                        startActivityForResult(intent, commonVariables.REQUEST_LOGIN);
                    }
                }
            } else
                super.onClick(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onResult(JSONObject jobjWhole) {
        try {
            if (jobjWhole != null) {

                String strApiName = jobjWhole.optString("api");
                if (strApiName.equalsIgnoreCase("GetProduct_ColorTypes")) {
                    JSONArray jarray = jobjWhole.optJSONArray("resData");
                    int l = jarray.length();
                    if (l != 0) {
                        ViewPagerAdapterDetail pagerAdapter = new ViewPagerAdapterDetail(getSupportFragmentManager());
                        viewPager.setOffscreenPageLimit(l);
                        listAdapter.clear();
                        for (int i = 0; i < l; i++) {
                            JSONObject jo = jarray.optJSONObject(i);
                            SC3Object fabricColor = new SC3Object(jo.optInt("ColorTypeId"), jo.optString("ColorType"), "", "");
                            FabricColorsListFragment fragment = new FabricColorsListFragment(fabricColor, item);
                            pagerAdapter.addFragment(fragment, fabricColor.getName());
                            listAdapter.add(fragment);
                        }

                        viewPager.setAdapter(pagerAdapter);
                        tabLayout.setupWithViewPager(viewPager);
                        mFl_whole.setVisibility(View.VISIBLE);
                    } else {
                        mFl_whole.setVisibility(View.GONE);
                        mLl_no_data_found.setVisibility(View.VISIBLE);
                        startAnim();
                    }
                } else if (strApiName.equalsIgnoreCase("AddProductToCart_Fabric_Buyer")) {
                    int resultId = jobjWhole.optInt("resInt");
                    String result = jobjWhole.optString("res");
                    if (resultId == 1) {
                        AppPreferences.getPrefs().edit().putInt(commonVariables.CART_COUNT, jobjWhole.optInt("cartCount")).apply();
                        Intent intent = new Intent(this, CartActivityBuyer.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    } else {
                        AlertDialogManager.showDialog(this, result, null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

