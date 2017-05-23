package com.github.library;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView 为ViewPager滚动模式时,Page选中的监听事件
 * Created by cai.jia on 2017/4/26 0026
 */

public class PageChangeSnapHelper extends PagerSnapHelper {

    private int targetPosition;
    private List<OnPageChangeListener> pageChangeListeners;

    public PageChangeSnapHelper() {
        targetPosition = -1;
        pageChangeListeners = new ArrayList<>();
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        int targetPosition = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
        pageChanged(layoutManager, targetPosition);
        return targetPosition;
    }

    private void pageChanged(RecyclerView.LayoutManager layoutManager, int targetPosition) {
        int itemCount = layoutManager.getItemCount();
        if (targetPosition < 0 || targetPosition >= itemCount) {
            return;
        }
        if (this.targetPosition != targetPosition) {
            this.targetPosition = targetPosition;
            if (pageChangeListeners != null && !pageChangeListeners.isEmpty()) {
                for (OnPageChangeListener pageSelectListener : pageChangeListeners) {
                    pageSelectListener.onPageSelected(layoutManager, targetPosition);
                }
            }
        }
    }

    @Nullable
    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager,
                                              @NonNull View targetView) {
        int targetPosition = layoutManager.getPosition(targetView);
        pageChanged(layoutManager, targetPosition);
        return super.calculateDistanceToFinalSnap(layoutManager, targetView);
    }

    public interface OnPageChangeListener{

        void onPageSelected(RecyclerView.LayoutManager layoutManager, int position);
    }

    public void addOnPageChangeListener(OnPageChangeListener l) {
        if (l == null) {
            return;
        }
        pageChangeListeners.add(l);
    }

    public void removeOnPageChangeListener(OnPageChangeListener l) {
        if (l == null) {
            return;
        }
        pageChangeListeners.remove(l);
    }

    public void clearOnPageChangeListeners() {
        pageChangeListeners.clear();
    }
}
