package com.demo.livwllpaper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

public class DrawableTextView extends AppCompatTextView {
    public DrawableTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override 
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getCompoundDrawables()[0];
        if (drawable != null) {
            canvas.translate((((float) getWidth()) - ((getPaint().measureText(getText().toString()) + ((float) drawable.getIntrinsicWidth())) + ((float) getCompoundDrawablePadding()))) / 2.0f, 0.0f);
        }
        Drawable drawable2 = getCompoundDrawables()[2];
        if (drawable2 != null) {
            float measureText = getPaint().measureText(getText().toString()) + ((float) drawable2.getIntrinsicWidth()) + ((float) getCompoundDrawablePadding());
            setPadding(0, 0, (int) (((float) getWidth()) - measureText), 0);
            canvas.translate((((float) getWidth()) - measureText) / 2.0f, 0.0f);
        }
        Drawable drawable3 = getCompoundDrawables()[1];
        if (drawable3 != null) {
            canvas.translate(0.0f, (((float) getHeight()) - ((getPaint().getTextSize() + ((float) drawable3.getIntrinsicHeight())) + ((float) getCompoundDrawablePadding()))) / 2.0f);
        }
        Drawable drawable4 = getCompoundDrawables()[3];
        if (drawable4 != null) {
            float textSize = getPaint().getTextSize() + ((float) drawable4.getIntrinsicHeight()) + ((float) getCompoundDrawablePadding());
            setPadding(0, 0, 0, (int) (((float) getHeight()) - textSize));
            canvas.translate(0.0f, (((float) getHeight()) - textSize) / 2.0f);
        }
        super.onDraw(canvas);
    }
}
