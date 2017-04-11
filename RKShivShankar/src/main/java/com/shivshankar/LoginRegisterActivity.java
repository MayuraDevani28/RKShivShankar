package com.shivshankar;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.shivshankar.adapters.ViewPagerAdapter;
import com.shivshankar.fragments.LoginFragment;
import com.shivshankar.fragments.RegisterFragment;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.commonVariables;

@SuppressLint("NewApi")
public class LoginRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    public ViewPager viewPager;
    private TabLayout tabLayout;
    public LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    ViewPagerAdapter pagerAdapter;
    public ImageView mIv_close;
    public boolean isForLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
            isForLogin = getIntent().getBooleanExtra(commonVariables.KEY_FOR_LOGIN, false);
            setContentView(R.layout.activity_login_register);
            bindViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindViews() {
        mIv_close = (ImageView) findViewById(R.id.iv_close);
        mIv_close.setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.pager_tabs);
        setupViewPager();
    }

    private void setupViewPager() {
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout);
        loginFragment = new LoginFragment();
        pagerAdapter.addFragment(loginFragment, "LOGIN");
        registerFragment = new RegisterFragment();
        pagerAdapter.addFragment(registerFragment, "REGISTER");

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(1);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, 0);
    }

    public void setRegisterVisibility(boolean b) {
        try {
            if (b) {
                pagerAdapter.addFragment(registerFragment, "REGISTER");
            } else {
                pagerAdapter.removeFragment("REGISTER");
            }
            pagerAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
        view.startAnimation(buttonClick);
        if (view == mIv_close) {
            loginFragment.callBuyerWithoutLogin();
        }
    }
}