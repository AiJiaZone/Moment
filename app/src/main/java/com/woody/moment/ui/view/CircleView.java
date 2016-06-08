package com.woody.moment.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.woody.moment.R;

/**
 * Created by john on 6/3/16.
 */
public class CircleView extends ViewGroup {
    final private String TAG = "CircleView";

    final private float DEFAULT_RATIO = 1 / 2f;

    private int mRadius = 0;

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleView(Context context, AttributeSet attrs, int defaultAttrStyle) {
        super(context, attrs, defaultAttrStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

//        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int resWidth = 0;
        int resHeight = 0;
        final int DEFAULT_WIDTH = getDefaultWidth();
        if (widthMode != MeasureSpec.EXACTLY
                || heightMode != MeasureSpec.EXACTLY) {
            resWidth = getSuggestedMinimumWidth();
            resWidth = resWidth == 0 ? DEFAULT_WIDTH : resWidth;

//            resHeight = getSuggestedMinimumHeight();
//            resHeight = resHeight == 0 ? DEFAULT_WIDTH : resHeight;
        } else {
//            resWidth = resHeight = Math.min(resWidth, resHeight);
            resWidth = Math.min(resWidth, resHeight);
        }

        resWidth = resHeight = Math.min(DEFAULT_WIDTH / 2, resWidth);
        setMeasuredDimension(resWidth, resHeight);

        mRadius = Math.max(getMeasuredWidth(), getMeasuredHeight());

        final int COUNT = getChildCount();
        int childMode = MeasureSpec.EXACTLY;
        for (int i = 0; i < COUNT; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            int makeMeasureSpec = MeasureSpec.makeMeasureSpec((int) (mRadius * DEFAULT_RATIO), childMode);
            int id = child.getId();
            if (id == R.id.id_text_header) {
                child.measure(makeMeasureSpec, makeMeasureSpec);
            } else if (id == R.id.id_text_content) {
                child.measure(makeMeasureSpec * 2, makeMeasureSpec);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int COUNT = getChildCount();
        for (int i = 0; i < COUNT; i++) {
            View view = getChildAt(i);
            int id = view.getId();
            if (id == R.id.id_text_header) {
                t += view.getMeasuredHeight() / 2;
                l += (mRadius - view.getMeasuredWidth()) / 2;
            } else if (id == R.id.id_text_content) {
                t += view.getMeasuredHeight() / 3;
                l += (mRadius - view.getMeasuredWidth()) / 2 * DEFAULT_RATIO;
            }
            view.layout(l, t, r, b);
        }
    }

    final private int getDefaultWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return Math.min(outMetrics.widthPixels, outMetrics.heightPixels);
    }
}
