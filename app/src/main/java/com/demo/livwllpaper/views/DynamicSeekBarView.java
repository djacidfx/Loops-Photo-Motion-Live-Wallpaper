package com.demo.livwllpaper.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.core.content.res.ResourcesCompat;

import com.demo.livwllpaper.R;


public class DynamicSeekBarView extends LinearLayout implements SeekBar.OnSeekBarChangeListener {
    private View arrow;
    private LinearLayout llInfo;
    private CustomSeekBar seekBar;
    private String seekBarTextInfo = "";
    private TextView tvInfo;

    @Override 
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override 
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public DynamicSeekBarView(Context context) {
        super(context);
        init(context, null);
    }

    public DynamicSeekBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public DynamicSeekBarView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        View view;
        View inflate = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dynamic_seek_bar_view, (ViewGroup) this, false);
        this.seekBar = (CustomSeekBar) inflate.findViewById(R.id.seekBar);
        this.arrow = inflate.findViewById(R.id.arrow);
        this.tvInfo = (TextView) inflate.findViewById(R.id.tvInfo);
        this.llInfo = (LinearLayout) inflate.findViewById(R.id.llInfo);
        this.seekBar.setOnSeekBarChangeListener(this);
        TextView textView = this.tvInfo;
        textView.setText("" + this.seekBar.getProgress());
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.DynamicSeekBarView, 0, 0);
            int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize( R.styleable.DynamicSeekBarView_dsbv_infoSize, getResources().getDimensionPixelSize(R.dimen.default_thumb_size));
            int resourceId = obtainStyledAttributes.getResourceId(R.styleable.DynamicSeekBarView_dsbv_thumbDrawable, 0);
            int resourceId2 = obtainStyledAttributes.getResourceId(R.styleable.DynamicSeekBarView_dsbv_progressDrawable, 0);
            int resourceId3 = obtainStyledAttributes.getResourceId(R.styleable.DynamicSeekBarView_dsbv_infoBackgroundColor, 0);
            int resourceId4 = obtainStyledAttributes.getResourceId(R.styleable.DynamicSeekBarView_dsbv_infoTextColor, 0);
            int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(R.styleable.DynamicSeekBarView_dsbv_infoSize, 0);
            int dimensionPixelSize3 = obtainStyledAttributes.getDimensionPixelSize(R.styleable.DynamicSeekBarView_dsbv_infoTextSize, 0);
            int resourceId5 = obtainStyledAttributes.getResourceId(R.styleable.DynamicSeekBarView_dsbv_infoTextColor, 0);
            int i = obtainStyledAttributes.getInt(R.styleable.DynamicSeekBarView_dsbv_infoRadius, 0);
            int resourceId6 = obtainStyledAttributes.getResourceId(R.styleable.DynamicSeekBarView_dsbv_progressBackgroundColor, 0);
            int i2 = obtainStyledAttributes.getInt(R.styleable.DynamicSeekBarView_dsbv_max, 100);
            final int i3 = obtainStyledAttributes.getInt(R.styleable.DynamicSeekBarView_dsbv_progress, 0);
            boolean z = obtainStyledAttributes.getBoolean(R.styleable.DynamicSeekBarView_dsbv_isHideInfo, false);
            String string = obtainStyledAttributes.getString(R.styleable.DynamicSeekBarView_dsbv_infoInitText);
            if (resourceId != 0) {
                this.seekBar.setThumb(resourceId, dimensionPixelSize);
            }
            PorterDuff.Mode mode = PorterDuff.Mode.SRC_ATOP;
            if (resourceId2 != 0) {
                this.seekBar.setProgressDrawable(getResources().getDrawable(resourceId2));
            } else {
                LayerDrawable layerDrawable = (LayerDrawable) this.seekBar.getProgressDrawable();
                if (resourceId3 != 0) {
                    Drawable findDrawableByLayerId = layerDrawable.findDrawableByLayerId(16908301);
                    findDrawableByLayerId.setColorFilter(getColorValue(resourceId3), mode);
                    layerDrawable.setDrawableByLayerId(16908301, findDrawableByLayerId);
                }
                if (resourceId4 != 0) {
                    Drawable findDrawableByLayerId2 = layerDrawable.findDrawableByLayerId(16908288);
                    findDrawableByLayerId2.setColorFilter(getColorValue(resourceId4), mode);
                    layerDrawable.setDrawableByLayerId(16908288, findDrawableByLayerId2);
                }
            }
            this.seekBar.setMax(i2);
            this.seekBar.setProgress(i3);
            if (!z) {
                this.llInfo.setVisibility(View.VISIBLE);
                if (dimensionPixelSize2 != 0) {
                    LayoutParams layoutParams = (LayoutParams) this.tvInfo.getLayoutParams();
                    layoutParams.width = dimensionPixelSize2;
                    this.tvInfo.setLayoutParams(layoutParams);
                }
                if (dimensionPixelSize3 != 0) {
                    this.tvInfo.setTextSize(0, (float) dimensionPixelSize3);
                }
                if (resourceId5 != 0) {
                    this.tvInfo.setTextColor(getColorValue(resourceId5));
                }
                if (resourceId6 != 0) {
                    GradientDrawable gradientDrawable = new GradientDrawable();
                    gradientDrawable.setShape(GradientDrawable.RECTANGLE);
                    if (i != 0) {
                        gradientDrawable.setCornerRadius((float) i);
                    }
                    gradientDrawable.setColor(getColorValue(resourceId6));
                    this.tvInfo.setBackground(gradientDrawable);
                    this.arrow.getBackground().setColorFilter(getColorValue(resourceId6), mode);
                } else {
                    GradientDrawable gradientDrawable2 = new GradientDrawable();
                    gradientDrawable2.setShape(GradientDrawable.RECTANGLE);
                    if (i != 0) {
                        gradientDrawable2.setCornerRadius((float) i);
                    }
                    gradientDrawable2.setColor(getColorValue(R.color.default_color));
                    this.tvInfo.setBackground(gradientDrawable2);
                    this.arrow.getBackground().setColorFilter(getColorValue(R.color.default_color), mode);
                }
                new Handler().postDelayed(new Runnable() { 
                    @Override 
                    public void run() {
                        DynamicSeekBarView.this.setInfoPosition(i3);
                    }
                }, 500);
                if (!TextUtils.isEmpty(string)) {
                    this.tvInfo.setText(string);
                } else {
                    TextView textView2 = this.tvInfo;
                    textView2.setText("" + i3);
                }
            } else {
                this.llInfo.setVisibility(View.GONE);
            }
            view = inflate;
        } else {
            view = inflate;
        }
        addView(view);
    }

    public void setProgress(int i) {
        this.seekBar.setProgress(i);
    }

    public void setMax(int i) {
        this.seekBar.setMax(i);
    }

    private int getColorValue(int i) {
        return ResourcesCompat.getColor(getResources(), i, null);
    }

    @Override 
    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        setInfoPosition(i);
        TextView textView = this.tvInfo;
        textView.setText("" + i);
    }

    
    public void setInfoPosition(int i) {
        this.seekBar.setProgress(getStepValue(i));
        this.arrow.setX((float) (getSeekBarThumbPosX(this.seekBar) - (this.arrow.getWidth() / 2)));
        TextView textView = this.tvInfo;
        textView.setX((float) getTimePosition(this.seekBar, textView));
    }

    private int getTimePosition(SeekBar seekBar, View view) {
        int seekBarThumbPosX = getSeekBarThumbPosX(seekBar);
        int width = seekBar.getWidth();
        if ((view.getWidth() / 2) + seekBarThumbPosX >= width) {
            return width - view.getWidth();
        }
        if (seekBarThumbPosX - (view.getWidth() / 2) <= seekBar.getPaddingLeft()) {
            return (int) seekBar.getX();
        }
        return seekBarThumbPosX - (view.getWidth() / 2);
    }

    private int getStepValue(int i) {
        return Math.round((float) (i / 1)) * 1;
    }

    private int getSeekBarThumbPosX(SeekBar seekBar) {
        return seekBar.getPaddingLeft() + ((((seekBar.getWidth() - seekBar.getPaddingLeft()) - seekBar.getPaddingRight()) * seekBar.getProgress()) / seekBar.getMax());
    }

    public void setSeekBarChangeListener(SeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
        this.seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
    }

    public void setInfoText(String str, int i) {
        this.tvInfo.setText(str);
        setInfoPosition(i);
    }
}
