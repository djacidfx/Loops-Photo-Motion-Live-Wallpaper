package com.demo.livwllpaper.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.AsyncTask;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.ViewCompat;

import com.demo.livwllpaper.R;


public class WLupaImageView extends AppCompatImageView {
    private static int TAMANHO_LUPA = 300;
    private static float ZOOM_INIT = 1.5f;
    private Canvas canvasResultado;
    private Bitmap imagemZoom;
    private Bitmap lupa;
    private Bitmap mascara;
    private Paint paintMask;
    private Bitmap resultadoLupa;
    private float zoom = ZOOM_INIT;

    public WLupaImageView(Context context) {
        super(context);
        initialize();
    }

    public WLupaImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize();
    }

    public WLupaImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initialize();
    }

    private void initialize() {
        if (!isInEditMode()) {
            Paint paint = new Paint(1);
            this.paintMask = paint;
            paint.setFilterBitmap(true);
            this.paintMask.setStyle(Paint.Style.FILL);
            this.paintMask.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.lupa);
            int i = TAMANHO_LUPA;
            this.lupa = Bitmap.createScaledBitmap(decodeResource, i, i, true);
            Bitmap decodeResource2 = BitmapFactory.decodeResource(getResources(), R.drawable.lupa_mask);
            int i2 = TAMANHO_LUPA;
            this.mascara = Bitmap.createScaledBitmap(decodeResource2, i2, i2, true);
            int i3 = TAMANHO_LUPA;
            this.resultadoLupa = Bitmap.createBitmap(i3, i3, Bitmap.Config.ARGB_8888);
            this.canvasResultado = new Canvas(this.resultadoLupa);
        }
    }

    @Override 
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void set_Position_Magnifying_glass(final float f, final float f2) {
        if (this.imagemZoom != null) {
            new AsyncTask() {
                
                @Override 
                protected Object doInBackground(Object[] objArr) {
                    Bitmap createBitmap = Bitmap.createBitmap(WLupaImageView.TAMANHO_LUPA, WLupaImageView.TAMANHO_LUPA, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    canvas.drawColor(ViewCompat.MEASURED_STATE_MASK);
                    canvas.drawBitmap(WLupaImageView.this.imagemZoom, (WLupaImageView.this.zoom * f * -1.0f) + ((float) (WLupaImageView.TAMANHO_LUPA / 2)), (WLupaImageView.this.zoom * f2 * -1.0f) + ((float) (WLupaImageView.TAMANHO_LUPA / 2)), (Paint) null);
                    canvas.drawBitmap(WLupaImageView.this.mascara, 0.0f, 0.0f, WLupaImageView.this.paintMask);
                    WLupaImageView.this.canvasResultado.drawBitmap(createBitmap, 0.0f, 0.0f, (Paint) null);
                    WLupaImageView.this.canvasResultado.drawBitmap(WLupaImageView.this.lupa, 0.0f, 0.0f, (Paint) null);
                    return null;
                }

                @Override 
                protected void onPostExecute(Object obj) {
                    WLupaImageView.this.invalidate();
                    super.onPostExecute(obj);
                }
            }.execute(new Object[0]);
        }
    }

    public void setImageBitmap(Bitmap bitmap, float f) {
        this.zoom = Math.min(ZOOM_INIT * f, 4.0f);
        setImageBitmap(bitmap);
    }

    @Override 
    public void setImageBitmap(Bitmap bitmap) {
        this.imagemZoom = Bitmap.createScaledBitmap(bitmap, (int) (((float) bitmap.getWidth()) * this.zoom), (int) (((float) bitmap.getHeight()) * this.zoom), true);
        super.setImageBitmap(this.resultadoLupa);
    }
}
