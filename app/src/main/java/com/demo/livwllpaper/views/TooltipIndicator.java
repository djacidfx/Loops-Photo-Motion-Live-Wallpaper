package com.demo.livwllpaper.views;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.demo.livwllpaper.R;

import java.util.List;
import java.util.Objects;


public class TooltipIndicator extends RelativeLayout implements ViewPager.OnPageChangeListener, View.OnTouchListener {
    private static final String TAG = "ToolTipIndicator";
    private PagerAdapter adapter;
    private ValueAnimator collapseAnimator;
    private Drawable[] drawableList;
    private ValueAnimator expandAnimator;
    private int lineHeight;
    private int lineMargin;
    private int lineWidth;
    private int lineWidthSelected;
    private int lineWidthSelectedWithMargin;
    private int lineWidthWithMargin;
    private LinearLayout linesLayout;
    private int selectedLineDrawableResource;
    private int selectedPosition;
    private int tooltipHeight;
    private RelativeLayout tooltipView;
    private AppCompatImageView tooltipViewImage;
    private int tooltipWidth;
    private int unselectedLineDrawableResource;
    private ViewPager viewPager;

    @Override 
    public void onPageScrollStateChanged(int i) {
    }

    @Override 
    public void onPageScrolled(int i, float f, int i2) {
    }

    public TooltipIndicator(Context context) {
        this(context, null);
    }

    public TooltipIndicator(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TooltipIndicator(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.selectedPosition = -1;
        setClipChildren(false);
        setClipToPadding(false);
        setClickable(true);
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.TooltipIndicator);
        try {
            this.lineWidth = (int) obtainStyledAttributes.getDimension( R.styleable.TooltipIndicator_ti_lineWidth, (float) dpToPx(16));
            this.lineHeight = (int) obtainStyledAttributes.getDimension( R.styleable.TooltipIndicator_ti_lineHeight, (float) dpToPx(6));
            this.lineWidthSelected = (int) obtainStyledAttributes.getDimension( R.styleable.TooltipIndicator_ti_lineWidthSelected, (float) dpToPx(32));
            int dimension = (int) obtainStyledAttributes.getDimension(R.styleable.TooltipIndicator_ti_lineMargin, (float) dpToPx(4));
            this.lineMargin = dimension;
            this.lineWidthWithMargin = this.lineWidth + (dimension * 2);
            this.lineWidthSelectedWithMargin = this.lineWidthSelected + (dimension * 2);
            this.tooltipWidth = (int) obtainStyledAttributes.getDimension( R.styleable.TooltipIndicator_ti_tooltipWidth, (float) dpToPx(100));
            this.tooltipHeight = (int) obtainStyledAttributes.getDimension( R.styleable.TooltipIndicator_ti_tooltipHeight, (float) dpToPx(180));
            this.selectedLineDrawableResource = obtainStyledAttributes.getResourceId( R.styleable.TooltipIndicator_ti_selectedLineDrawable, R.drawable.tooltip_indicator_rounded_line_selected);
            this.unselectedLineDrawableResource = obtainStyledAttributes.getResourceId( R.styleable.TooltipIndicator_ti_unselectedLineDrawable, R.drawable.tooltip_indicator_rounded_line_unselected);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    public void setupViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        PagerAdapter adapter = viewPager.getAdapter();
        this.adapter = adapter;
        Objects.requireNonNull(adapter, "ViewPager's adapter cannot be null.");
        removeAllViews();
        initIndicatorLines();
        initToolTipView();
        selectPage(0);
        viewPager.removeOnPageChangeListener(this);
        viewPager.addOnPageChangeListener(this);
        setOnTouchListener(this);
    }

    public void setToolTipDrawables(List<Drawable> list) {
        PagerAdapter pagerAdapter = this.adapter;
        Objects.requireNonNull(pagerAdapter, "ViewPager's adapter cannot be null.");
        this.drawableList = (Drawable[]) list.toArray(new Drawable[pagerAdapter.getCount()]);
    }

    private void initToolTipView() {
        LayoutParams layoutParams = new LayoutParams(this.tooltipWidth, this.tooltipHeight);
        layoutParams.topMargin = -(layoutParams.height + dpToPx(8));
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        this.tooltipView = relativeLayout;
        relativeLayout.setLayoutParams(layoutParams);
        this.tooltipView.setBackgroundResource(R.drawable.tooltip_indicator_rounded_line_selected);
        this.tooltipView.setPadding(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));
        AppCompatImageView appCompatImageView = new AppCompatImageView(getContext());
        this.tooltipViewImage = appCompatImageView;
        appCompatImageView.setLayoutParams(new LayoutParams(-1, -1));
        this.tooltipView.setScaleX(0.0f);
        this.tooltipView.setScaleY(0.0f);
        this.tooltipView.setAlpha(0.0f);
        this.tooltipView.setTranslationY((float) (layoutParams.height / 2));
        this.tooltipView.addView(this.tooltipViewImage);
        addView(this.tooltipView);
    }

    private void initIndicatorLines() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        this.linesLayout = linearLayout;
        linearLayout.setVerticalGravity(0);
        int count = this.adapter.getCount();
        for (int i = 0; i < count; i++) {
            View view = new View(getContext());
            view.setBackgroundResource(this.unselectedLineDrawableResource);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(this.lineWidth, this.lineHeight);
            layoutParams.leftMargin = this.lineMargin;
            layoutParams.rightMargin = this.lineMargin;
            view.setLayoutParams(layoutParams);
            this.linesLayout.addView(view);
        }
        addView(this.linesLayout);
    }

    @Override 
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        Drawable[] drawableArr = this.drawableList;
        if (!(drawableArr == null || drawableArr.length == 0)) {
            int min = Math.min(Math.max(0, x), getMeasuredWidth());
            RelativeLayout relativeLayout = this.tooltipView;
            relativeLayout.setX((float) (min - (relativeLayout.getMeasuredWidth() / 2)));
            int i = this.selectedPosition;
            int i2 = this.lineWidthWithMargin;
            int i3 = i * i2;
            int i4 = this.lineWidthSelectedWithMargin;
            if (min >= i3 + i4) {
                i = i + 1 + ((min - (i3 + i4)) / i2);
            } else if (min <= i3) {
                i = min / i2;
            }
            int min2 = Math.min(i, this.drawableList.length - 1);
            Drawable background = this.tooltipViewImage.getBackground();
            Drawable[] drawableArr2 = this.drawableList;
            if (background != drawableArr2[min2]) {
                this.tooltipViewImage.setBackgroundDrawable(drawableArr2[min2]);
            }
            int action = motionEvent.getAction();
            if (action == 0) {
                this.tooltipView.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).translationY(1.0f).setInterpolator(new OvershootInterpolator()).start();
            } else if (action == 1) {
                this.tooltipView.animate().alpha(0.0f).scaleX(0.0f).scaleY(0.0f).translationY((float) (this.tooltipView.getMeasuredHeight() / 2)).setInterpolator(new LinearInterpolator()).start();
            }
        }
        return true;
    }

    private void selectPage(int i) {
        int i2 = this.selectedPosition;
        if (i2 != -1) {
            collapseView(this.linesLayout.getChildAt(i2));
        }
        this.selectedPosition = i;
        expandView(this.linesLayout.getChildAt(i));
    }

    private void expandView(final View view) {
        view.setBackgroundResource(this.selectedLineDrawableResource);
        ValueAnimator valueAnimator = this.expandAnimator;
        if (valueAnimator != null) {
            valueAnimator.end();
        }
        ValueAnimator ofObject = ValueAnimator.ofObject(new IntEvaluator(), Integer.valueOf(this.lineWidth), Integer.valueOf(this.lineWidthSelected));
        this.expandAnimator = ofObject;
        ofObject.setInterpolator(new DecelerateInterpolator());
        this.expandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { 
            @Override 
            public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                layoutParams.width = ((Integer) valueAnimator2.getAnimatedValue()).intValue();
                view.setLayoutParams(layoutParams);
            }
        });
        this.expandAnimator.setDuration(200L);
        this.expandAnimator.start();
    }

    private void collapseView(final View view) {
        view.setBackgroundResource(this.unselectedLineDrawableResource);
        ValueAnimator valueAnimator = this.collapseAnimator;
        if (valueAnimator != null) {
            valueAnimator.end();
        }
        ValueAnimator ofObject = ValueAnimator.ofObject(new IntEvaluator(), Integer.valueOf(this.lineWidthSelected), Integer.valueOf(this.lineWidth));
        this.collapseAnimator = ofObject;
        ofObject.setInterpolator(new DecelerateInterpolator());
        this.collapseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { 
            @Override 
            public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                layoutParams.width = ((Integer) valueAnimator2.getAnimatedValue()).intValue();
                view.setLayoutParams(layoutParams);
            }
        });
        this.collapseAnimator.setDuration(500L);
        this.collapseAnimator.start();
    }

    @Override 
    public void onPageSelected(int i) {
        selectPage(i);
    }

    public int dpToPx(int i) {
        return (int) TypedValue.applyDimension(1, (float) i, getResources().getDisplayMetrics());
    }
}
