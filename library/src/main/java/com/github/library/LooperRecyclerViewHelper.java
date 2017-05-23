package com.github.library;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * RecyclerView 自动滚动的工具类
 * Created by cai.jia on 2017/4/26 0026
 */

public class LooperRecyclerViewHelper implements LooperPageRecyclerView.OnAttachedOrDetachedWindow {

    private static final int MSG_SCROLL_TO_NEXT = 10000;
    private static final int INTERVAL = 4000;
    private static final int SPEED_TIMES = 4;

    private TimerHandler timerHandler;
    private LooperPageRecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private InternalScrollListener scrollListener;
    private ViewPropHelper propHelper;

    private int interval = INTERVAL;
    private int speedTimes = SPEED_TIMES;

    public LooperRecyclerViewHelper() {
        this(INTERVAL,SPEED_TIMES);
    }

    public LooperRecyclerViewHelper(int interval,int speedTimes) {
        this.interval = interval;
        this.speedTimes = speedTimes;
        propHelper = new ViewPropHelper();
        timerHandler = new TimerHandler(this);
        scrollListener = new InternalScrollListener(this);
    }

    public void attachToRecyclerView(@NonNull LooperPageRecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (!(layoutManager instanceof LinearLayoutManager)) {
            throw new RuntimeException("layoutManager is not LinearLayoutManager");
        }
        this.layoutManager = (LinearLayoutManager) layoutManager;
        destroyCallback();
        setupCallback();
        start();
    }

    private void destroyCallback() {
        if (recyclerView == null) {
            return;
        }

        recyclerView.removeOnScrollListener(scrollListener);
        recyclerView.setOnAttachedOrDetachedWindow(null);
    }

    private void setupCallback() {
        if (recyclerView == null) {
            return;
        }

        recyclerView.addOnScrollListener(scrollListener);
        recyclerView.setOnAttachedOrDetachedWindow(this);
    }

    @Override
    public void onAttachedToWindow() {
        View snapView = propHelper.findSnapView(layoutManager);
        if (snapView == null) {
            return;
        }
        int position = layoutManager.getPosition(snapView);
        int[] ints = propHelper.calculateDistanceToFinalSnap(layoutManager, snapView);
        if (ints[0] != 0 || ints[1] != 0) {
            scrollToPosition(position);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        stop();
    }

    private boolean isStop;

    private void stop() {
        isStop = true;
        timerHandler.removeMessages(MSG_SCROLL_TO_NEXT);
        timerHandler.removeCallbacks(looperTask);
    }

    private void start() {
        isStop = false;
        timerHandler.postDelayed(looperTask, interval);
    }

    private void scrollToNextPosition() {
        if (isStop) {
            return;
        }

        if (layoutManager == null || layoutManager.getChildCount() == 0) {
            return;
        }

        View snapView = propHelper.findSnapView(layoutManager);
        if (snapView == null) {
            return;
        }
        int position = layoutManager.getPosition(snapView);
        int[] ints = propHelper.calculateDistanceToFinalSnap(layoutManager, snapView);
        if (ints[0] == 0 && ints[1] == 0) {
            position++;
        }
        scrollToPosition(position);
    }

    private void scrollToPosition(int position) {
        if (position < 0) {
            position = layoutManager.getItemCount() - 1;
        }

        if (position >= layoutManager.getItemCount()) {
            position = 0;
        }

        LinearSmoothScroller smoothScroller = createSmoothScroller();
        smoothScroller.setTargetPosition(position);
        layoutManager.startSmoothScroll(smoothScroller);
    }

    private LinearSmoothScroller createSmoothScroller() {
        return new LinearSmoothScroller(recyclerView.getContext()) {

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return super.calculateSpeedPerPixel(displayMetrics) * speedTimes;
            }
        };
    }

    private static class InternalScrollListener extends RecyclerView.OnScrollListener {

        private WeakReference<LooperRecyclerViewHelper> ref;

        InternalScrollListener(LooperRecyclerViewHelper looperRecyclerViewHelper) {
            ref = new WeakReference<>(looperRecyclerViewHelper);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (ref.get() == null) {
                return;
            }

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                ref.get().start();

            } else {
                ref.get().stop();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (dx != 0 || dy != 0 && ref.get() != null) {
                ref.get().stop();
            }
        }
    }

    private static class TimerHandler extends Handler {

        WeakReference<LooperRecyclerViewHelper> ref;

        TimerHandler(LooperRecyclerViewHelper helper) {
            ref = new WeakReference<>(helper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_SCROLL_TO_NEXT) {
                if (ref.get() != null) {
                    ref.get().scrollToNextPosition();
                }
            }
        }
    }

    private Runnable looperTask = new Runnable() {
        @Override
        public void run() {
            timerHandler.sendEmptyMessage(MSG_SCROLL_TO_NEXT);
        }
    };
}
