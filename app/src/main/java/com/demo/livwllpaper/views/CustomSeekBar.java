package com.demo.livwllpaper.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.demo.livwllpaper.R;


public class CustomSeekBar extends AppCompatSeekBar {
    public static final int ROTATION_ANGLE_CW_270 = 270;
    public static final int ROTATION_ANGLE_CW_90 = 90;
    public static final int ROTATION_ANGLE_DEFAULT = 0;
    private boolean isHideProgressInit;
    private int mRotationAngle = 0;
    private int progressDrawable;

    public CustomSeekBar(Context context) {
        super(context);
        initialize(context, null, 0, 0);
    }

    public CustomSeekBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initialize(context, attributeSet, i, 0);
    }

    public CustomSeekBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(context, attributeSet, 0, 0);
    }

    @SuppressLint("ResourceType")
    public void clearBackground() {
        setProgressDrawable(new ColorDrawable(getResources().getColor(17170445)));
    }

    @SuppressLint("ResourceType")
    private void initialize(Context context, AttributeSet attributeSet, int i, int i2) {
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.CustomSeekBar, i, i2);

            this.mRotationAngle = obtainStyledAttributes.getInteger(R.styleable.CustomSeekBar_dms_rotation, 0);
            int resourceId = obtainStyledAttributes.getResourceId(R.styleable.CustomSeekBar_dms_thumbDrawable, 0);
            this.progressDrawable = obtainStyledAttributes.getResourceId(R.styleable.CustomSeekBar_dms_progressDrawable, 0);
            this.isHideProgressInit = obtainStyledAttributes.getBoolean(R.styleable.CustomSeekBar_dms_isHideProgressInit, false);
            if (resourceId != 0) {
                int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.CustomSeekBar_dms_thumbSize, 0);
                if (dimensionPixelSize != 0) {
                    Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), resourceId);
                    if (decodeResource == null) {
                        Drawable drawable = getResources().getDrawable(resourceId);
                        Canvas canvas = new Canvas();
                        canvas.setBitmap(Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888));
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        drawable.draw(canvas);
                        setThumb(drawable);
                    } else {
                        Bitmap createBitmap = Bitmap.createBitmap(dimensionPixelSize, dimensionPixelSize, Bitmap.Config.ARGB_8888);
                        new Canvas(createBitmap).drawBitmap(decodeResource, new Rect(0, 0, decodeResource.getWidth(), decodeResource.getHeight()), new Rect(0, 0, createBitmap.getWidth(), createBitmap.getHeight()), (Paint) null);
                        setThumb(new BitmapDrawable(getResources(), createBitmap));
                    }
                } else {
                    Drawable drawable2 = getResources().getDrawable(resourceId);
                    Canvas canvas2 = new Canvas();
                    canvas2.setBitmap(Bitmap.createBitmap(drawable2.getIntrinsicWidth(), drawable2.getIntrinsicHeight(), Bitmap.Config.ARGB_8888));
                    drawable2.setBounds(0, 0, drawable2.getIntrinsicWidth(), drawable2.getIntrinsicHeight());
                    drawable2.draw(canvas2);
                    setThumb(drawable2);
                }
                if (this.isHideProgressInit) {
                    setProgressDrawable(new ColorDrawable(getResources().getColor(17170445)));
                } else if (this.progressDrawable != 0) {
                    setProgressDrawable(getResources().getDrawable(this.progressDrawable));
                }
                obtainStyledAttributes.recycle();
            }
        }
    }

    public void setThumb(int i, int i2) {
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), i);
        if (decodeResource != null) {
            Bitmap createBitmap = Bitmap.createBitmap(i2, i2, Bitmap.Config.ARGB_8888);
            new Canvas(createBitmap).drawBitmap(decodeResource, new Rect(0, 0, decodeResource.getWidth(), decodeResource.getHeight()), new Rect(0, 0, createBitmap.getWidth(), createBitmap.getHeight()), (Paint) null);
            setThumb(new BitmapDrawable(getResources(), createBitmap));
            return;
        }
        setThumbDrawable(i);
    }

    public void setThumbDrawable(int i) {
        Drawable drawable = getResources().getDrawable(i);
        Canvas canvas = new Canvas();
        canvas.setBitmap(Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888));
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        setThumb(drawable);
    }

    @Override 
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        int i5 = this.mRotationAngle;
        if (i5 == 90 || i5 == 270) {
            super.onSizeChanged(i2, i, i4, i3);
        } else {
            super.onSizeChanged(i, i2, i3, i4);
        }
    }

    @Override 
    public synchronized void setProgress(int i) {
        super.setProgress(i);
        onSizeChanged(getWidth(), getHeight(), 0, 0);
    }

    @Override 
    protected synchronized void onMeasure(int i, int i2) {
        int i3 = this.mRotationAngle;
        if (i3 == 90 || i3 == 270) {
            super.onMeasure(i2, i);
            setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
        } else {
            super.onMeasure(i, i2);
            setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
        }
    }

    
    @Override 
    public void onDraw(Canvas canvas) {
        int i = this.mRotationAngle;
        if (i == 90) {
            canvas.rotate(-90.0f);
            canvas.translate((float) (-getHeight()), 0.0f);
        } else if (i == 270) {
            canvas.rotate(90.0f);
            canvas.translate(0.0f, (float) (-getWidth()));
        }
        super.onDraw(canvas);
    }

    @Override 
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        int i = this.mRotationAngle;
        if (i != 90 && i != 270) {
            return super.onTouchEvent(motionEvent);
        }
        int action = motionEvent.getAction();
        if (action == 0 || action == 1 || action == 2) {
            int max = getMax() - ((int) ((((float) getMax()) * motionEvent.getY()) / ((float) getHeight())));
            if (this.mRotationAngle == 90) {
                setProgress(max);
            } else {
                setProgress(max);
            }
            onSizeChanged(getWidth(), getHeight(), 0, 0);
        }
        return true;
    }
}
