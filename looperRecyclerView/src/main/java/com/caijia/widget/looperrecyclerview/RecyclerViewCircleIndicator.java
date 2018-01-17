package com.caijia.widget.looperrecyclerview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * RecyclerView ViewPager模式时的指示器
 * Created by cai.jia on 2017/4/26 0026
 */
public class RecyclerViewCircleIndicator extends View implements PageChangeSnapHelper.OnPageChangeListener {

    private int mRadius;
    private int mSpace;
    private Paint mSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mNormalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int selectPosition;
    private int itemCount;
    private boolean reserveLayout;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RecyclerViewCircleIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public RecyclerViewCircleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public RecyclerViewCircleIndicator(Context context) {
        this(context, null);
    }

    public RecyclerViewCircleIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerViewCircleIndicator);
        try {
            mRadius = a.getDimensionPixelOffset(R.styleable.RecyclerViewCircleIndicator_rviRadius, dp2px(5));
            int selectedColor = a.getColor(R.styleable.RecyclerViewCircleIndicator_rviSelectedColor, 0xff33b5e5);
            int normalColor = a.getColor(R.styleable.RecyclerViewCircleIndicator_rviNormalColor, 0xff868686);
            mSpace = a.getDimensionPixelOffset(R.styleable.RecyclerViewCircleIndicator_rviSpace, dp2px(12));

            mSelectedPaint.setColor(selectedColor);
            mNormalPaint.setColor(normalColor);

            mSelectedPaint.setAntiAlias(true);
            mNormalPaint.setAntiAlias(true);

        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int diameter = mRadius * 2; //直径
        int width = itemCount * diameter + mSpace * (itemCount - 1);
        int height = diameter;
        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int diameter = mRadius * 2; //直径

        int actualWidth = itemCount * diameter + mSpace * (itemCount - 1);
        int wCenter = (width - actualWidth) / 2;
        int hCenter = (height - diameter) / 2;

        int startOffset = reserveLayout ? width - wCenter : wCenter;
        int topOffset = reserveLayout ? height - hCenter : hCenter;

        for (int i = 0; i < itemCount && itemCount > 1; i++) {
            int wStep = mRadius * (i * 2 + 1) + i * mSpace;
            int cx = reserveLayout ? startOffset - wStep : startOffset + wStep;
            int cy = reserveLayout ? topOffset - mRadius : topOffset + mRadius;

            canvas.drawCircle(
                    cx,
                    cy,
                    mRadius,
                    selectPosition == i ? mSelectedPaint : mNormalPaint);
        }
    }

    public void setupWithRecyclerView(@NonNull LooperPageRecyclerView recyclerView) {
        final RecyclerView.Adapter adapter = recyclerView.getActualAdapter();
        if (adapter == null) {
            throw new RuntimeException("RecyclerView Adapter is null");
        }
        this.itemCount = adapter.getItemCount();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                itemCount = adapter.getItemCount();
                requestLayout();
            }
        });

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (!(layoutManager instanceof LinearLayoutManager)) {
            throw new RuntimeException("RecyclerView layoutManager must be LinearLayoutManager");
        }
        this.reserveLayout = ((LinearLayoutManager) layoutManager).getReverseLayout();
        recyclerView.removeOnPageChangeListener(this);
        recyclerView.addOnPageChangeListener(this);
        requestLayout();
    }

    private int dp2px(float dpValue) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                getContext().getResources().getDisplayMetrics()));
    }

    @Override
    public void onPageSelected(RecyclerView.LayoutManager layoutManager, int position) {
        this.selectPosition = position % itemCount;
        invalidate();
    }
}
