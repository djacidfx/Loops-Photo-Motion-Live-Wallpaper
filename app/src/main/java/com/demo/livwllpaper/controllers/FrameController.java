package com.demo.livwllpaper.controllers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import com.demo.livwllpaper.beans.Point;
import com.demo.livwllpaper.beans.TriangleBitmap;
import com.demo.livwllpaper.delanauy.Delaunay;


public class FrameController {
    private static FrameController frame_controller;
    private static Bitmap imagemManipulada;
    private int ANIM_ALPHA_MAX = 255;
    private int ANIM_ALPHA_PERCENT = 50;
    private Canvas canvas;

    private FrameController() {
    }

    public static FrameController getInstance() {
        if (frame_controller == null) {
            frame_controller = new FrameController();
        }
        return frame_controller;
    }

    public int getAlpha(int i, float f) {
        float f2 = (float) ((this.ANIM_ALPHA_PERCENT * i) / 100);
        int i2 = this.ANIM_ALPHA_MAX;
        float f3 = ((float) i2) / f2;
        float f4 = ((float) i) - f2;
        if (f >= f4) {
            return i2 + Math.round((f4 - f) * f3);
        }
        return f < f2 ? Math.round(f3 * f) : i2;
    }

    public Bitmap get_Frame_On_Move(Delaunay lPMLWDelaunay, int i, int i2, float f, Bitmap.Config config) {
        int round = Math.round(((float) (i * i2)) / 1000.0f);
        int floor = (int) Math.floor((double) (((float) i2) * (f / 1000.0f)));
        for (Point lPMLWPoint : lPMLWDelaunay.get_Points_List()) {
            if (!lPMLWPoint.is_static()) {
                float xInit = lPMLWPoint.getXInit() - ((lPMLWPoint.getXFim() - lPMLWPoint.getXInit()) / 1.0f);
                float yInit = lPMLWPoint.getYInit() - ((lPMLWPoint.getYFim() - lPMLWPoint.getYInit()) / 1.0f);
                float f2 = (float) floor;
                float f3 = (float) round;
                lPMLWPoint.set_Current_Position_Animation(xInit + (((lPMLWPoint.getXFim() - xInit) / f3) * f2), yInit + (f2 * ((lPMLWPoint.getYFim() - yInit) / f3)));
            }
        }
        Bitmap createBitmap = Bitmap.createBitmap(lPMLWDelaunay.getImagemDelaunay().getWidth(), lPMLWDelaunay.getImagemDelaunay().getHeight(), config);
        Canvas canvas = new Canvas(createBitmap);
        for (TriangleBitmap lPMLWTriangleBitmap : lPMLWDelaunay.get_Triangles_List()) {
            if (!lPMLWTriangleBitmap.is_static()) {
                lPMLWTriangleBitmap.desenhaDistorcao(canvas, config);
            }
        }
        return createBitmap;
    }
}
