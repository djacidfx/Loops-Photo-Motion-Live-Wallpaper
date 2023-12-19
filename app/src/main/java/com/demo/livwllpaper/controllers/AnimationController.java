package com.demo.livwllpaper.controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.CountDownTimer;
import androidx.core.view.ViewCompat;
import com.demo.livwllpaper.Utilsx.Utils;
import com.demo.livwllpaper.beans.Point;
import com.demo.livwllpaper.beans.Project;
import com.demo.livwllpaper.beans.TriangleBitmap;
import com.demo.livwllpaper.delanauy.Delaunay;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class AnimationController {
    private static final int ANIM_ALPHA_PONTOS = 255;
    public static final int ANIM_FPS = 30;
    private static AnimationController animation_controller;
    private Delaunay LPMLWDelaunay_Introduction;
    private CountDownTimer animCount;
    private Canvas canvas;
    public Project current_LPMLW_project;
    private Bitmap presentation_Image;
    private AnimateListener listenerAnim = null;
    private Paint paint_mask = new Paint(1);
    public int Animation_time = 10000;
    FrameController LPMLWFrameController = FrameController.getInstance();

    
    public interface AnimateListener {
        void onAnimate(Bitmap bitmap);
    }

    
    public interface Iterator_Of_Point {
        void onIterate(Point lPMLWPoint);
    }

    
    public interface ManipuladorDePonto {
        void onFinish(Bitmap bitmap);

        void onManipulate(Point lPMLWPoint);
    }

    private AnimationController(Project lPMLWProject) {
        setNovoProject(lPMLWProject);
        this.paint_mask.setAntiAlias(true);
        this.paint_mask.setFilterBitmap(true);
        this.paint_mask.setStyle(Paint.Style.FILL);
        this.paint_mask.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.paint_mask.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }

    public static AnimationController init(Project lPMLWProject) {
        AnimationController lPMLWAnimationController = animation_controller;
        if (lPMLWAnimationController == null) {
            animation_controller = new AnimationController(lPMLWProject);
        } else {
            lPMLWAnimationController.setNovoProject(lPMLWProject);
        }
        return animation_controller;
    }

    public static AnimationController getInstance() {
        return animation_controller;
    }

    private void setNovoProject(Project lPMLWProject) {
        this.current_LPMLW_project = lPMLWProject;
        float width = (float) lPMLWProject.getImagem().getWidth();
        float height = (float) lPMLWProject.getImagem().getHeight();
        float f = width > height ? 800.0f / width : 800.0f / height;
        this.presentation_Image = Bitmap.createScaledBitmap(lPMLWProject.getImagem(), Math.round(width * f), Math.round(height * f), true);
        Delaunay lPMLWDelaunay = this.LPMLWDelaunay_Introduction;
        if (lPMLWDelaunay != null) {
            lPMLWDelaunay.clear();
        }
        this.LPMLWDelaunay_Introduction = new Delaunay(this.presentation_Image);
        for (Point lPMLWPoint : lPMLWProject.get_Points_List()) {
            this.LPMLWDelaunay_Introduction.add_point(lPMLWPoint.get_Copy(1.0f / lPMLWProject.get_Proportion()));
        }
    }

    public Bitmap getImagemRepresentacao(boolean z, float f) {
        Bitmap copy = this.presentation_Image.copy(Bitmap.Config.ARGB_8888, true);
        this.LPMLWDelaunay_Introduction.restart_Points();
        this.canvas = new Canvas(copy);
        if (z) {
            for (TriangleBitmap lPMLWTriangleBitmap : this.LPMLWDelaunay_Introduction.get_Triangles_List()) {
                lPMLWTriangleBitmap.desenhaTrajeto(this.canvas, f);
            }
        }
        for (Point lPMLWPoint : this.LPMLWDelaunay_Introduction.get_Points_List()) {
            if (!lPMLWPoint.is_static()) {
                lPMLWPoint.to_draw_Arrow(this.canvas, 255, f);
            }
            lPMLWPoint.to_draw_only_point(this.canvas, 255, f);
        }
        return copy;
    }

    public void stopAnimation(Context context) {
        CountDownTimer countDownTimer = this.animCount;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        this.LPMLWDelaunay_Introduction.clear();
    }

    public void startAnimation() {
        CountDownTimer countDownTimer = this.animCount;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        final Bitmap.Config config = Bitmap.Config.ARGB_8888;
        this.LPMLWDelaunay_Introduction.setImagemDelaunay(Utils.getImage_without_Mask(this.presentation_Image, ToolsController.getMask(), config));
        Bitmap bitmap = this.LPMLWDelaunay_Introduction.get_Static_Triangles(Bitmap.Config.ARGB_8888);
        Bitmap bitmap2 = Utils.get_masked_image(this.presentation_Image, ToolsController.getMask(), Bitmap.Config.ARGB_8888);
        final Bitmap createBitmap = Bitmap.createBitmap(bitmap);
        new Canvas(createBitmap).drawBitmap(bitmap2, 0.0f, 0.0f, (Paint) null);
        final Bitmap createBitmap2 = Bitmap.createBitmap(this.presentation_Image.getWidth(), this.presentation_Image.getHeight(), config);
        final Canvas canvas = new Canvas(createBitmap2);
        canvas.drawBitmap(this.presentation_Image, 0.0f, 0.0f, (Paint) null);
        final Paint paint = new Paint(1);
        paint.setFilterBitmap(true);
        paint.setAntiAlias(true);
        paint.setDither(true);
        this.animCount = new CountDownTimer((long) this.Animation_time, 33) { 
            @Override 
            public void onTick(long j) {
                System.currentTimeMillis();
                float f = (float) (((long) AnimationController.this.Animation_time) - j);
                Bitmap bitmap3 = AnimationController.this.LPMLWFrameController.get_Frame_On_Move(AnimationController.this.LPMLWDelaunay_Introduction, AnimationController.this.Animation_time, 30, f, config);
                float f2 = ((((float) AnimationController.this.Animation_time) / 2.0f) + f) % ((float) AnimationController.this.Animation_time);
                Bitmap bitmap4 = AnimationController.this.LPMLWFrameController.get_Frame_On_Move(AnimationController.this.LPMLWDelaunay_Introduction, AnimationController.this.Animation_time, 30, f2, config);
                paint.setAlpha(AnimationController.this.LPMLWFrameController.getAlpha(AnimationController.this.Animation_time, f));
                canvas.drawBitmap(bitmap3, 0.0f, 0.0f, (Paint) null);
                paint.setAlpha(AnimationController.this.LPMLWFrameController.getAlpha(AnimationController.this.Animation_time, f2));
                canvas.drawBitmap(bitmap4, 0.0f, 0.0f, paint);
                canvas.drawBitmap(createBitmap, 0.0f, 0.0f, (Paint) null);
                if (AnimationController.this.listenerAnim != null) {
                    AnimationController.this.listenerAnim.onAnimate(createBitmap2);
                }
            }

            @Override 
            public void onFinish() {
                AnimationController.this.startAnimation();
            }
        }.start();
    }

    public static int getImagemWidth() {
        return animation_controller.presentation_Image.getWidth();
    }

    public static int getImagemHeight() {
        return animation_controller.presentation_Image.getHeight();
    }

    public void setOnAnimateListener(AnimateListener animateListener) {
        this.listenerAnim = animateListener;
    }

    public void iterate_Points(Iterator_Of_Point iterator_Of_Point) {
        for (Point lPMLWPoint : this.LPMLWDelaunay_Introduction.get_Points_List()) {
            iterator_Of_Point.onIterate(lPMLWPoint);
        }
    }

    public List<Point> get_Selected_Points_List() {
        LinkedList linkedList = new LinkedList();
        for (Point lPMLWPoint : this.LPMLWDelaunay_Introduction.get_Points_List()) {
            if (lPMLWPoint.is_Selected()) {
                linkedList.add(lPMLWPoint);
            }
        }
        return linkedList;
    }

    public List<Point> get_All_Points_List() {
        LinkedList linkedList = new LinkedList();
        for (Point lPMLWPoint : this.LPMLWDelaunay_Introduction.get_Points_List()) {
            linkedList.add(lPMLWPoint);
        }
        return linkedList;
    }

    public boolean is_there_Point(Point lPMLWPoint) {
        for (Point lPMLWPoint2 : this.LPMLWDelaunay_Introduction.get_Points_List()) {
            if (lPMLWPoint2.equals(lPMLWPoint)) {
                return true;
            }
        }
        return false;
    }

    public boolean has_Selected_Point() {
        for (Point lPMLWPoint : this.LPMLWDelaunay_Introduction.get_Points_List()) {
            if (lPMLWPoint.is_Selected()) {
                return true;
            }
        }
        return false;
    }

    public void delete_Selected_Points() {
        delete_Points(get_Selected_Points_List());
    }

    public void delete_All_Points() {
        delete_All_Points(get_All_Points_List());
    }

    public void delete_Points(List<Point> list) {
        this.LPMLWDelaunay_Introduction.delete_Points(list);
    }

    public void delete_All_Points(List<Point> list) {
        this.LPMLWDelaunay_Introduction.delete_Points(list);
    }

    public void delete_point(Point lPMLWPoint) {
        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
        copyOnWriteArrayList.add(lPMLWPoint);
        this.LPMLWDelaunay_Introduction.delete_Points(copyOnWriteArrayList);
    }

    public void add_point(Point lPMLWPoint) {
        this.LPMLWDelaunay_Introduction.add_point(lPMLWPoint);
    }

    public void set_Time_Animation(int i) {
        this.Animation_time = i;
    }

    public int getTempoAnimacao() {
        return this.Animation_time;
    }
}
