package com.caijia.widget.looperrecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.caijia.widget.R;

import java.util.List;

/**
 * 循环的RecyclerView,类似ViewPager
 * Created by cai.jia on 2017/5/23 0023
 */

public class LooperPageRecyclerView extends RecyclerView {

    private PageChangeSnapHelper snapHelper;
    private LooperRecyclerViewHelper looperHelper;
    private OnAttachedOrDetachedWindow l;
    private boolean isLooper;
    private boolean isAutoScroll;

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
            int interval = a.getInt(R.styleable.LooperPageRecyclerView_prInterval, 6000);
            isLooper = a.getBoolean(R.styleable.LooperPageRecyclerView_prIsLooper, true);
            isAutoScroll = a.getBoolean(R.styleable.LooperPageRecyclerView_prIsAutoScroll, true);
            looperHelper = new LooperRecyclerViewHelper(interval, speedTimes);

        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }

    private Adapter actualAdapter;

    @Override
    public void setAdapter(Adapter adapter) {
        if (isLooper) {
            super.setAdapter(new WrapperAdapter(adapter));
        }else{
            super.setAdapter(adapter);
        }
        actualAdapter = adapter;
        snapHelper.attachToRecyclerView(this);
        if (isAutoScroll) {
            looperHelper.attachToRecyclerView(this);
        }
    }

    /**
     * 获取包装前的Adapter
     * @return
     */
    public Adapter getActualAdapter() {
        return actualAdapter;
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

    /**
     * 主要是为了可以无限循坏,对getItemCount做处理
     */
    private class WrapperAdapter extends Adapter {

        private Adapter adapter;

        public WrapperAdapter(Adapter adapter) {
            this.adapter = adapter;
            itemCount = adapter.getItemCount();
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position, List payloads) {
            adapter.onBindViewHolder(holder, position, payloads);
        }

        @Override
        public int getItemViewType(int position) {
            return adapter.getItemViewType(position);
        }

        @Override
        public void setHasStableIds(boolean hasStableIds) {
            adapter.setHasStableIds(hasStableIds);
        }

        @Override
        public long getItemId(int position) {
            return adapter.getItemId(position);
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            adapter.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(ViewHolder holder) {
            return adapter.onFailedToRecycleView(holder);
        }

        @Override
        public void onViewAttachedToWindow(ViewHolder holder) {
            adapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            adapter.onViewDetachedFromWindow(holder);
        }

        private int itemCount;

        @Override
        public void registerAdapterDataObserver(final AdapterDataObserver observer) {
            adapter.registerAdapterDataObserver(new AdapterDataObserver() {
                @Override
                public void onChanged() {
                    itemCount = adapter.getItemCount();
                    observer.onChanged();
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    observer.onItemRangeChanged(positionStart, itemCount);
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                    observer.onItemRangeChanged(positionStart, itemCount, payload);
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    observer.onItemRangeInserted(positionStart, itemCount);
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    observer.onItemRangeRemoved(positionStart, itemCount);
                }

                @Override
                public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                    observer.onItemRangeMoved(fromPosition, toPosition, itemCount);
                }
            });
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            adapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            adapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            adapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            adapter.onBindViewHolder(holder, position);
        }

        @Override
        public int getItemCount() {
            return itemCount > 1 ? Integer.MAX_VALUE : itemCount;
        }
    }
}
