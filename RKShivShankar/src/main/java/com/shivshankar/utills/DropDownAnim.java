package com.shivshankar.utills;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Mayura on 5/22/2017.
 */

public class DropDownAnim extends Animation {
    private final int targetHeight;
    private final View view;
    private final boolean down;

    public DropDownAnim(View view, int targetHeight, boolean down) {
        this.view = view;
        this.targetHeight = targetHeight;
        this.down = down;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight;
        if (down) {
            newHeight = (int) (targetHeight * interpolatedTime);
        } else {
            newHeight = targetHeight - (int) (targetHeight * interpolatedTime);//(int) (targetHeight * (1 - interpolatedTime));
        }
        view.getLayoutParams().height = newHeight;
        view.requestLayout();
        view.setVisibility(down ? View.VISIBLE : View.GONE);
    }

    @Override
    public void initialize(int width, int height, int parentWidth,
                           int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
