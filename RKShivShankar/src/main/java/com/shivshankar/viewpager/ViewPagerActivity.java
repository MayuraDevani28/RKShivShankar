package com.shivshankar.viewpager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.liuguangqiang.progressbar.CircleProgressBar;
import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.shivshankar.R;
import com.shivshankar.customcontrols.CirclePageIndicator;
import com.shivshankar.customcontrols.ExtendedViewPager;
import com.shivshankar.customcontrols.TouchImageView;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.commonVariables;


@SuppressLint("NewApi")
public class ViewPagerActivity extends AppCompatActivity implements OnClickListener {

    private static String[] IMAGES = {"ness.jpg", "squirrel.jpg"};

    private ExtendedViewPager page;
    private CirclePageIndicator mIndicator;
    private ImageView mIv_backImage;
    private ImageView mIv_nextImage, mIv_close;
    private CircleProgressBar progressBar;
    private SwipeBackLayout swipeBackLayout;
    private RelativeLayout rll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().getBooleanExtra(commonVariables.KEY_IS_LANDSCAPE, true))
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            setContentView(R.layout.view_pager);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Transition exitTrans = new Fade();
                exitTrans.setDuration(600);
                getWindow().setEnterTransition(exitTrans);
            }
            isStoragePermissionGranted();
            IMAGES = getIntent().getStringArrayExtra(commonVariables.INTENT_EXTRA_LIST_IMAGE_ARRAY);
            int pos = getIntent().getIntExtra(commonVariables.INTENT_EXTRA_POSITION, 0);

            final PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
            page = (ExtendedViewPager) findViewById(R.id.pager);
            mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
            progressBar = (CircleProgressBar) findViewById(R.id.progressbar1);
            rll = (RelativeLayout) findViewById(R.id.rl_close);
            rll.setOnClickListener(this);

            page.setOffscreenPageLimit(2);
            page.setAdapter(pagerAdapter);
            page.setCurrentItem(pos);
            swipeBackLayout = (SwipeBackLayout) findViewById(R.id.swipe_layout);
            swipeBackLayout.setEnableFlingBack(false);
            swipeBackLayout.setOnPullToBackListener(new SwipeBackLayout.SwipeBackListener() {
                @Override
                public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
                    progressBar.setProgress((int) (progressBar.getMax() * fractionAnchor));
                }
            });
            // setDragEdge(SwipeBackLayout.DragEdge.TOP);
            page.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {

                    if (position > 0) {
                        View view = page.getChildAt(position - 1);
                        if (view != null) {
                            TouchImageView img = (TouchImageView) view.findViewById(R.id.imageView);
                            img.resetZoom();
                        }
                    }
                    if (position < page.getChildCount() - 1) {
                        View view = page.getChildAt(position + 1);
                        if (view != null) {
                            TouchImageView img = (TouchImageView) view.findViewById(R.id.imageView);
                            img.resetZoom();
                        }
                    }
                    for (int i = 0; i < page.getAdapter().getCount(); i++) {
                        View view = page.getChildAt(i);
                        if (view != null) {
                            TouchImageView img = (TouchImageView) view.findViewById(R.id.imageView);
                            img.resetZoom();
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            mIndicator.setViewPager(page);
            mIv_backImage = (ImageView) findViewById(R.id.iv_back_pager);
            mIv_backImage.setOnClickListener(this);
            mIv_nextImage = (ImageView) findViewById(R.id.iv_next_pager);
            mIv_nextImage.setOnClickListener(this);
            mIv_close = (ImageView) findViewById(R.id.iv_close);
            mIv_close.setOnClickListener(this);
            if (IMAGES != null && IMAGES.length == 1) {
                mIv_backImage.setVisibility(View.GONE);
                mIv_nextImage.setVisibility(View.GONE);
                mIndicator.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        try {
            if (v == mIv_backImage) {
                if (page.getCurrentItem() == 0)
                    page.setCurrentItem(IMAGES.length - 1, true);
                else
                    page.setCurrentItem(getItem(-1), true);
            } else if (v == mIv_nextImage) {
                if (page.getCurrentItem() == IMAGES.length - 1)
                    page.setCurrentItem(0, true);
                else
                    page.setCurrentItem(getItem(+1), true);
            } else if (v == rll)
                onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getItem(int i) {
        return page.getCurrentItem() + i;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ViewPagerFragment fragment = new ViewPagerFragment();
            fragment.setImagePath(IMAGES[position]);
            fragment.resetImages();
            return fragment;
        }

        @Override
        public int getCount() {
            return IMAGES.length;
        }
    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("","Permission is granted");
                return true;
            } else {

                Log.v("TAGRK","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAGRK","Permission is granted");
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("TAGRK","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }
}
