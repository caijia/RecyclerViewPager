package com.github.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 循环的RecyclerView,类似ViewPager
 * Created by cai.jia on 2017/5/23 0023
 */

public class LooperPageRecyclerView extends RecyclerView {

    private PageChangeSnapHelper snapHelper;
    private LooperRecyclerViewHelper looperHelper;
    private OnAttachedOrDetachedWindow l;

    public LooperPageRecyclerView(Context context) {
        this(context, null);
    }

    public LooperPageRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LooperPageRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        snapHelper = new PageChangeSnapHelper();
        TypedArray a = null;
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.LooperPageRecyclerView);
            int speedTimes = a.getInt(R.styleable.LooperPageRecyclerView_prSpeedTimes, 5);
            int interval = a.getInt(R.styleable.LooperPageRecyclerView_prInterval, 4000);
            looperHelper = new LooperRecyclerViewHelper(interval,speedTimes);
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        snapHelper.attachToRecyclerView(this);
        looperHelper.attachToRecyclerView(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (l != null) {
            l.onAttachedToWindow();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (l != null) {
            l.onDetachedFromWindow();
        }
    }

    public void setOnAttachedOrDetachedWindow(OnAttachedOrDetachedWindow l) {
        this.l = l;
    }

    public void addOnPageChangeListener(PageChangeSnapHelper.OnPageChangeListener l) {
        snapHelper.addOnPageChangeListener(l);
    }

    public void removeOnPageChangeListener(PageChangeSnapHelper.OnPageChangeListener l) {
        snapHelper.removeOnPageChangeListener(l);
    }

    public void clearOnPageChangeListeners() {
        snapHelper.clearOnPageChangeListeners();
    }

    public interface OnAttachedOrDetachedWindow {

        void onAttachedToWindow();

        void onDetachedFromWindow();
    }
}
