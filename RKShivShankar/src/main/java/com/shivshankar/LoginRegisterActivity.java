package com.shivshankar;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.shivshankar.adapters.ViewPagerAdapter;
import com.shivshankar.fragments.LoginFragment;
import com.shivshankar.fragments.RegisterFragment;
import com.shivshankar.utills.ExceptionHandler;

@SuppressLint("NewApi")
public class LoginRegisterActivity extends AppCompatActivity {


    private ViewPager viewPager;
    private TabLayout tabLayout;
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
            setContentView(R.layout.activity_login_register);
            init();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void init() {
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        tabLayout = (TabLayout)findViewById(R.id.pager_tabs);
        setupViewPager();
    }
    private void setupViewPager() {

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        loginFragment = new LoginFragment();
        pagerAdapter.addFragment(loginFragment, "LOGIN");
//        registerFragment = new RegisterFragment();
//        pagerAdapter.addFragment(registerFragment, "REGISTER");


        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(1);
    }
    private void callCheckAppVersionAPI() {
//        Uri uri = new Uri.Builder().scheme("http").authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
//                .path("MobileAPI/CheckAppVersion")
//                .appendQueryParameter("loginId", AppPreferences.getPrefs().getString(commonVariables.KEY_LOGIN_ID, "0"))
//                .appendQueryParameter("GCMRegistraionId", regId)
//                .build();
//        String query = uri.toString();
//        Log.v("TAGRK", "Calling With:" + query);
//        new ServerAPICAll(null, this).execute(query);

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, 0);
    }




}