package com.demo.livwllpaper.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;


public class ZoomImageView extends AppCompatImageView {
    private static final int CLICK = 3;
    private static final int DRAG = 1;
    private static final int NONE = 0;
    private static final int ZOOM = 2;
    private OnTouchListener _onTouchListener;
    private Context context;
    private float[] f47m;
    private ScaleGestureDetector mScaleDetector;
    private Matrix matrix;
    private int oldMeasuredHeight;
    private int oldMeasuredWidth;
    private float origHeight;
    private float origWidth;
    private Matrix originalMatrix;
    private ScaleGestureDetector.SimpleOnScaleGestureListener scaleListenerExterno;
    private int viewHeight;
    private int viewWidth;
    private PointF last = new PointF();
    private float maxScale = 5.0f;
    private float minScale = 1.0f;
    private int mode = 0;
    private OnTouchListener onTouchListenerZoom = new zoom_image_touch_listener();
    private float saveScale = 1.0f;
    private PointF start = new PointF();
    private boolean zoomAtivado = false;

    float getFixDragTrans(float f, float f2, float f3) {
        if (f3 <= f2) {
            return 0.0f;
        }
        return f;
    }

    float getFixTrans(float f, float f2, float f3) {
        float f4;
        float f5;
        if (f3 <= f2) {
            f4 = f2 - f3;
            f5 = 0.0f;
        } else {
            f5 = f2 - f3;
            f4 = 0.0f;
        }
        if (f < f5) {
            return (-f) + f5;
        }
        if (f > f4) {
            return (-f) + f4;
        }
        return 0.0f;
    }

    
    class zoom_image_touch_listener implements OnTouchListener {
        zoom_image_touch_listener() {
        }

        
        @Override 
        
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getPointerCount() == 1) {
                ZoomImageView.this.set_Zoom_On(false);
            }
            ZoomImageView.this.mScaleDetector.onTouchEvent(motionEvent);
            PointF pointF = new PointF(motionEvent.getX(), motionEvent.getY());
            int action = motionEvent.getAction();
            if (action == 0) {
                ZoomImageView.this.last.set(pointF);
                ZoomImageView.this.start.set(ZoomImageView.this.last);
                ZoomImageView.this.mode = 1;
            } else if (action != 1) {
                if (action != 2) {
                    if (action == 6) {
                        ZoomImageView.this.mode = 0;
                    }
                }
                if (ZoomImageView.this.mode == 1) {
                    float f = pointF.y - ZoomImageView.this.last.y;
                    Matrix matrix = ZoomImageView.this.matrix;
                    float fixDragTrans = ZoomImageView.this.getFixDragTrans(pointF.x - ZoomImageView.this.last.x, (float) ZoomImageView.this.viewWidth, ZoomImageView.this.origWidth * ZoomImageView.this.saveScale);
                    ZoomImageView lPMLWZoomImageView = ZoomImageView.this;
                    matrix.postTranslate(fixDragTrans, lPMLWZoomImageView.getFixDragTrans(f, (float) lPMLWZoomImageView.viewHeight, ZoomImageView.this.origHeight * ZoomImageView.this.saveScale));
                    ZoomImageView.this.fixTrans();
                    ZoomImageView.this.last.set(pointF.x, pointF.y);
                }
            } else {
                ZoomImageView.this.mode = 0;
                int abs = (int) Math.abs(pointF.y - ZoomImageView.this.start.y);
                if (((int) Math.abs(pointF.x - ZoomImageView.this.start.x)) < 3 && abs < 3) {
                    ZoomImageView.this.performClick();
                }
                if (ZoomImageView.this.mode == 1) {
                }
            }
            ZoomImageView lPMLWZoomImageView2 = ZoomImageView.this;
            lPMLWZoomImageView2.setImageMatrix(lPMLWZoomImageView2.matrix);
            ZoomImageView.this.invalidate();
            return true;
        }
    }

    
    
    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private ScaleListener() {
        }

        @Override 
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            ZoomImageView.this.mode = 2;
            ZoomImageView.this.scaleListenerExterno.onScaleBegin(scaleGestureDetector);
            return true;
        }

        @Override 
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float f;
            float scaleFactor = scaleGestureDetector.getScaleFactor();
            float f2 = ZoomImageView.this.saveScale;
            ZoomImageView.this.saveScale *= scaleFactor;
            if (ZoomImageView.this.saveScale > ZoomImageView.this.maxScale) {
                ZoomImageView lPMLWZoomImageView = ZoomImageView.this;
                lPMLWZoomImageView.saveScale = lPMLWZoomImageView.maxScale;
                f = ZoomImageView.this.maxScale;
            } else {
                if (ZoomImageView.this.saveScale < ZoomImageView.this.minScale) {
                    ZoomImageView lPMLWZoomImageView2 = ZoomImageView.this;
                    lPMLWZoomImageView2.saveScale = lPMLWZoomImageView2.minScale;
                    f = ZoomImageView.this.minScale;
                }
                if (ZoomImageView.this.origWidth * ZoomImageView.this.saveScale > ((float) ZoomImageView.this.viewWidth) || ZoomImageView.this.origHeight * ZoomImageView.this.saveScale <= ((float) ZoomImageView.this.viewHeight)) {
                    ZoomImageView.this.matrix.postScale(scaleFactor, scaleFactor, (float) (ZoomImageView.this.viewWidth / 2), (float) (ZoomImageView.this.viewHeight / 2));
                } else {
                    ZoomImageView.this.matrix.postScale(scaleFactor, scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
                }
                ZoomImageView.this.fixTrans();
                ZoomImageView.this.scaleListenerExterno.onScale(scaleGestureDetector);
                return true;
            }
            scaleFactor = f / f2;
            if (ZoomImageView.this.origWidth * ZoomImageView.this.saveScale > ((float) ZoomImageView.this.viewWidth)) {
            }
            ZoomImageView.this.matrix.postScale(scaleFactor, scaleFactor, (float) (ZoomImageView.this.viewWidth / 2), (float) (ZoomImageView.this.viewHeight / 2));
            ZoomImageView.this.fixTrans();
            ZoomImageView.this.scaleListenerExterno.onScale(scaleGestureDetector);
            return true;
        }
    }

    public ZoomImageView(Context context) {
        super(context);
        sharedConstructing(context);
    }

    public ZoomImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        sharedConstructing(context);
    }

    private void sharedConstructing(Context context) {
        this.context = context;
        setClickable(true);
        this.context = context;
        this.saveScale = 1.0f;
        this.mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        Matrix matrix = new Matrix();
        this.matrix = matrix;
        this.f47m = new float[9];
        setImageMatrix(matrix);
        setScaleType(ScaleType.MATRIX);
        super.setOnTouchListener(this.onTouchListenerZoom);
    }

    public float getZoomScale() {
        return this.saveScale;
    }

    public void restartZoom() {
        this.saveScale = 1.0f;
        this.matrix = new Matrix();
        this.f47m = new float[9];
        setScaleType(ScaleType.FIT_CENTER);
        setImageMatrix(this.matrix);
        setScaleType(ScaleType.MATRIX);
    }

    @Override 
    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        this.originalMatrix = new Matrix(getImageMatrix());
    }

    @Override 
    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this._onTouchListener = onTouchListener;
    }

    public void set_Zoom_On(boolean z) {
        this.zoomAtivado = z;
        if (z) {
            super.setOnTouchListener(this.onTouchListenerZoom);
        } else {
            super.setOnTouchListener(this._onTouchListener);
        }
    }

    public void setMaxZoom(float f) {
        this.maxScale = f;
    }

    public void setScaleListener(ScaleGestureDetector.SimpleOnScaleGestureListener simpleOnScaleGestureListener) {
        this.scaleListenerExterno = simpleOnScaleGestureListener;
    }

    void fixTrans() {
        this.matrix.getValues(this.f47m);
        float[] fArr = this.f47m;
        float f = fArr[2];
        float f2 = fArr[5];
        float fixTrans = getFixTrans(f, (float) this.viewWidth, this.origWidth * this.saveScale);
        float fixTrans2 = getFixTrans(f2, (float) this.viewHeight, this.origHeight * this.saveScale);
        if (fixTrans != 0.0f || fixTrans2 != 0.0f) {
            this.matrix.postTranslate(fixTrans, fixTrans2);
        }
    }

    @Override 
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.viewWidth = MeasureSpec.getSize(i);
        int size = MeasureSpec.getSize(i2);
        this.viewHeight = size;
        int i3 = this.oldMeasuredHeight;
        int i4 = this.viewWidth;
        if ((i3 != i4 || i3 != size) && i4 != 0 && size != 0) {
            this.oldMeasuredHeight = size;
            this.oldMeasuredWidth = i4;
            if (this.saveScale == 1.0f) {
                Drawable drawable = getDrawable();
                if (drawable != null && drawable.getIntrinsicWidth() != 0 && drawable.getIntrinsicHeight() != 0) {
                    int intrinsicWidth = drawable.getIntrinsicWidth();
                    float f = (float) intrinsicWidth;
                    float intrinsicHeight = (float) drawable.getIntrinsicHeight();
                    float min = Math.min(((float) this.viewWidth) / f, ((float) this.viewHeight) / intrinsicHeight);
                    this.matrix.setScale(min, min);
                    float f2 = (((float) this.viewHeight) - (intrinsicHeight * min)) / 2.0f;
                    float f3 = (((float) this.viewWidth) - (f * min)) / 2.0f;
                    this.matrix.postTranslate(f3, f2);
                    this.origWidth = ((float) this.viewWidth) - (f3 * 2.0f);
                    this.origHeight = ((float) this.viewHeight) - (f2 * 2.0f);
                    setImageMatrix(this.matrix);
                } else {
                    return;
                }
            }
            fixTrans();
        }
    }
}
