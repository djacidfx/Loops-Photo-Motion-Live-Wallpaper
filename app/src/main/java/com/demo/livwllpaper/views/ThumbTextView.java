package com.demo.livwllpaper.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


public class ThumbTextView extends TextView {
    private LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);
    private int width = 0;

    public ThumbTextView(Context context) {
        super(context);
    }

    public ThumbTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void attachToSeekBar(SeekBar seekBar) {
        String charSequence = getText().toString();
        if (!TextUtils.isEmpty(charSequence) && seekBar != null) {
            float measureText = getPaint().measureText(charSequence);
            int paddingLeft = (this.width - seekBar.getPaddingLeft()) - seekBar.getPaddingRight();
            int paddingRight = (int) ((((float) this.width) - measureText) - ((float) seekBar.getPaddingRight()));
            int paddingLeft2 = seekBar.getPaddingLeft();
            int progress = ((int) (((double) (((float) paddingLeft) * ((float) ((((double) seekBar.getProgress()) * 1.0d) / ((double) seekBar.getMax()))))) - (((double) measureText) / 2.0d))) + paddingLeft2;
            if (progress <= paddingLeft2) {
                paddingRight = paddingLeft2;
            } else if (progress < paddingRight) {
                paddingRight = progress;
            }
            this.lp.setMargins(paddingRight, 0, 0, 0);
            setLayoutParams(this.lp);
        }
    }

    @Override 
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.width == 0) {
            this.width = MeasureSpec.getSize(i);
        }
    }
}
