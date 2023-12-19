package com.demo.livwllpaper.beans;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;

import com.demo.livwllpaper.controllers.ToolsController;
import java.util.ArrayList;
import java.util.Arrays;


public class TriangleBitmap {
    public static float STROKE_DOT_SPACE_INIT = 8.0f;
    private static float STROKE_INIT = 0.8f;
    private float altura;
    private Bitmap bitmap_triangle;
    private float largura;
    private BitmapShader mBitmapShader;
    private Bitmap originalImage;
    private Point p1;
    private Point p2;
    private Point p3;
    private float proportion;
    private float xMaior;
    private float xMenor;
    private float yMaior;
    private float yMenor;
    private Paint mPaintShader = new Paint(1);
    private boolean maskInicial = false;
    private Paint paintPathMove = new Paint(1);
    private Paint paintPathStatic = new Paint(1);
    private Path path_triangle = new Path();

    public TriangleBitmap clone() throws CloneNotSupportedException {
        return (TriangleBitmap) super.clone();
    }

    public String toString() {
        return this.p1 + " " + this.p2 + " " + this.p3;
    }

    public static ArrayList<TriangleBitmap> cutInitialBitmap(Bitmap bitmap) {
        ArrayList<TriangleBitmap> arrayList = new ArrayList<>();
        int width = bitmap.getWidth() / bitmap.getHeight();
        float width2 = ((float) bitmap.getWidth()) * 0.1f;
        float f = -1.0f * width2;
        Point lPMLWPoint = new Point(f, f, true);
        Point lPMLWPoint2 = new Point(((float) bitmap.getWidth()) + width2, f, true);
        Point lPMLWPoint3 = new Point(((float) bitmap.getWidth()) + width2, ((float) bitmap.getHeight()) + width2, true);
        Point lPMLWPoint4 = new Point(f, ((float) bitmap.getHeight()) + width2, true);
        TriangleBitmap lPMLWTriangleBitmap = new TriangleBitmap(bitmap, lPMLWPoint, lPMLWPoint2, lPMLWPoint3);
        TriangleBitmap lPMLWTriangleBitmap2 = new TriangleBitmap(bitmap, lPMLWPoint3, lPMLWPoint4, lPMLWPoint);
        lPMLWTriangleBitmap.setMaskInicial(true);
        lPMLWTriangleBitmap2.setMaskInicial(true);
        arrayList.add(lPMLWTriangleBitmap);
        arrayList.add(lPMLWTriangleBitmap2);
        return arrayList;
    }

    public TriangleBitmap(Bitmap bitmap, Point lPMLWPoint, Point lPMLWPoint2, Point lPMLWPoint3) {
        this.xMaior = 0.0f;
        this.xMenor = Float.MAX_VALUE;
        this.yMaior = 0.0f;
        this.yMenor = Float.MAX_VALUE;
        this.originalImage = bitmap;
        this.mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        this.mPaintShader.setFilterBitmap(true);
        this.mPaintShader.setAntiAlias(true);
        this.mPaintShader.setShader(this.mBitmapShader);
        this.mPaintShader.setStrokeWidth(10.0f);
        this.p1 = lPMLWPoint;
        this.p2 = lPMLWPoint2;
        this.p3 = lPMLWPoint3;
        Point[] lPMLWPointArr = {lPMLWPoint, lPMLWPoint2, lPMLWPoint3};
        for (int i = 0; i < 3; i++) {
            this.xMenor = this.xMenor < lPMLWPointArr[i].getXInit() ? this.xMenor : lPMLWPointArr[i].getXInit();
            this.yMenor = this.yMenor < lPMLWPointArr[i].getYInit() ? this.yMenor : lPMLWPointArr[i].getYInit();
            this.xMaior = this.xMaior > lPMLWPointArr[i].getXInit() ? this.xMaior : lPMLWPointArr[i].getXInit();
            this.yMaior = this.yMaior > lPMLWPointArr[i].getYInit() ? this.yMaior : lPMLWPointArr[i].getYInit();
        }
        float f = this.xMenor;
        this.xMenor = f < 0.0f ? 0.0f : f;
        float f2 = this.yMenor;
        this.yMenor = f2 < 0.0f ? 0.0f : f2;
        this.paintPathMove.setStyle(Paint.Style.STROKE);
        this.paintPathMove.setFilterBitmap(true);
        Paint paint = this.paintPathMove;
        float f3 = STROKE_DOT_SPACE_INIT;
        paint.setPathEffect(new DashPathEffect(new float[]{f3, f3 * 2.0f}, 0.0f));
        this.paintPathMove.setStrokeWidth(STROKE_INIT);
        this.paintPathStatic.setStrokeWidth(STROKE_INIT);
        this.paintPathMove.setFilterBitmap(true);
        this.paintPathStatic.setColor(ToolsController.getColorMask());
        this.paintPathStatic.setFilterBitmap(true);
        this.paintPathStatic.setStyle(Paint.Style.FILL);
        this.paintPathStatic.setAntiAlias(true);
    }

    public void setOriginalImage(Bitmap bitmap) {
        clear();
        this.originalImage = bitmap;
        BitmapShader bitmapShader = new BitmapShader(this.originalImage, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        this.mBitmapShader = bitmapShader;
        this.mPaintShader.setShader(bitmapShader);
    }

    public boolean contains_vertice(Point lPMLWPoint, Point lPMLWPoint2) {
        return contains_Point(lPMLWPoint) && contains_Point(lPMLWPoint2);
    }

    public boolean contains_Point(Point lPMLWPoint) {
        return lPMLWPoint.equals(this.p1) || lPMLWPoint.equals(this.p2) || lPMLWPoint.equals(this.p3);
    }

    public boolean point_no_circumscript(Point lPMLWPoint) {
        return lPMLWPoint.in_Circumference(circumcenter(), this.p1.distance_to(circumcenter()));
    }

    public Vertice get_common_edge(TriangleBitmap lPMLWTriangleBitmap) {
        if (contains_vertice(lPMLWTriangleBitmap.getP1(), lPMLWTriangleBitmap.getP2())) {
            return new Vertice(lPMLWTriangleBitmap.getP1(), lPMLWTriangleBitmap.getP2());
        }
        if (contains_vertice(lPMLWTriangleBitmap.getP2(), lPMLWTriangleBitmap.getP3())) {
            return new Vertice(lPMLWTriangleBitmap.getP2(), lPMLWTriangleBitmap.getP3());
        }
        if (contains_vertice(lPMLWTriangleBitmap.getP3(), lPMLWTriangleBitmap.getP1())) {
            return new Vertice(lPMLWTriangleBitmap.getP3(), lPMLWTriangleBitmap.getP1());
        }
        return null;
    }

    public boolean and_neighbor(TriangleBitmap lPMLWTriangleBitmap) {
        return contains_vertice(lPMLWTriangleBitmap.getP1(), lPMLWTriangleBitmap.getP2()) || contains_vertice(lPMLWTriangleBitmap.getP2(), lPMLWTriangleBitmap.getP3()) || contains_vertice(lPMLWTriangleBitmap.getP3(), lPMLWTriangleBitmap.getP1());
    }

    public Vertice.Vertice_Distance_Package getVerticeMaisProximo(Point lPMLWPoint) {
        Vertice.Vertice_Distance_Package[] vertice_Distance_PackageArr = {new Vertice.Vertice_Distance_Package(new Vertice(this.p1, this.p2), computeClosestPoint(new Vertice(this.p1, this.p2), lPMLWPoint).sub(lPMLWPoint).mag()), new Vertice.Vertice_Distance_Package(new Vertice(this.p2, this.p3), computeClosestPoint(new Vertice(this.p2, this.p3), lPMLWPoint).sub(lPMLWPoint).mag()), new Vertice.Vertice_Distance_Package(new Vertice(this.p3, this.p1), computeClosestPoint(new Vertice(this.p3, this.p1), lPMLWPoint).sub(lPMLWPoint).mag())};
        Arrays.sort(vertice_Distance_PackageArr);
        return vertice_Distance_PackageArr[0];
    }

    private Point computeClosestPoint(Vertice lPMLWVertice, Point lPMLWPoint) {
        Point sub = lPMLWVertice.p2.sub(lPMLWVertice.p1);
        double dot = lPMLWPoint.sub(lPMLWVertice.p1).dot(sub) / sub.dot(sub);
        if (dot < 0.0d) {
            dot = 0.0d;
        } else if (dot > 1.0d) {
            dot = 1.0d;
        }
        return lPMLWVertice.p1.add(sub.mult(dot));
    }

    public Point get_point_outside_vertex(Vertice lPMLWVertice) {
        if (!this.p1.equals(lPMLWVertice.p1) && !this.p1.equals(lPMLWVertice.p2)) {
            return this.p1;
        }
        if (!this.p2.equals(lPMLWVertice.p1) && !this.p2.equals(lPMLWVertice.p2)) {
            return this.p2;
        }
        if (this.p3.equals(lPMLWVertice.p1) || this.p3.equals(lPMLWVertice.p2)) {
            return null;
        }
        return this.p3;
    }

    public double rectangle_Opposite_edge(Vertice lPMLWVertice) {
        float f;
        Point lPMLWPoint = get_point_outside_vertex(lPMLWVertice);
        float f2 = 0.0f;
        if (lPMLWPoint != null) {
            f2 = Math.abs(new Vertice(lPMLWPoint, lPMLWVertice.p1).angular_coefficient());
            f = Math.abs(new Vertice(lPMLWPoint, lPMLWVertice.p2).angular_coefficient());
        } else {
            f = 0.0f;
        }
        return Math.atan((double) (f2 + f));
    }

    public Point median() {
        return new Point(((this.p1.getXInit() + this.p2.getXInit()) + this.p3.getXInit()) / 3.0f, ((this.p1.getYInit() + this.p2.getYInit()) + this.p3.getYInit()) / 3.0f, true);
    }

    public Point circumcenter() {
        Point median = this.p1.median(this.p2);
        Point median2 = this.p2.median(this.p3);
        float angular_coefficient = -1.0f / this.p1.angular_coefficient(this.p2);
        if (Float.isInfinite(angular_coefficient)) {
            median = this.p3.median(this.p1);
            angular_coefficient = -1.0f / this.p3.angular_coefficient(this.p1);
        }
        float angular_coefficient2 = -1.0f / this.p2.angular_coefficient(this.p3);
        if (Float.isInfinite(angular_coefficient2)) {
            median2 = this.p3.median(this.p1);
            angular_coefficient2 = -1.0f / this.p3.angular_coefficient(this.p1);
        }
        float yInit = median.getYInit() - (median.getXInit() * angular_coefficient);
        float yInit2 = (yInit - (median2.getYInit() - (median2.getXInit() * angular_coefficient2))) / (angular_coefficient2 - angular_coefficient);
        return new Point(yInit2, (angular_coefficient * yInit2) + yInit, true);
    }

    public boolean is_static() {
        return this.p1.is_static() && this.p2.is_static() && this.p3.is_static();
    }

    private void desenharVertice(Canvas canvas, Point lPMLWPoint, Point lPMLWPoint2, boolean z) {
        if (lPMLWPoint.is_static() && lPMLWPoint2.is_static()) {
            canvas.drawLine(lPMLWPoint.getXInit(), lPMLWPoint.getYInit(), lPMLWPoint2.getXInit(), lPMLWPoint2.getYInit(), this.paintPathStatic);
        } else if (z) {
            canvas.drawLine(lPMLWPoint.getXInit(), lPMLWPoint.getYInit(), lPMLWPoint2.getXInit(), lPMLWPoint2.getYInit(), this.paintPathMove);
        }
    }

    public void desenhaTrajeto(Canvas canvas, float f) {
        if (!is_static()) {
            Paint paint = this.paintPathMove;
            float f2 = STROKE_DOT_SPACE_INIT;
            paint.setPathEffect(new DashPathEffect(new float[]{f2 / f, (f2 * 2.0f) / f}, 0.0f));
            this.paintPathMove.setStrokeWidth(STROKE_INIT / f);
            desenharVertice(canvas, this.p1, this.p2, false);
            desenharVertice(canvas, this.p2, this.p3, false);
            desenharVertice(canvas, this.p3, this.p1, false);
        } else if (!this.maskInicial) {
            this.paintPathStatic.setStrokeWidth(STROKE_INIT / f);
            this.paintPathStatic.setAlpha(ToolsController.getAlphaMask());
            this.path_triangle.reset();
            this.path_triangle.moveTo(this.p1.getXInit(), this.p1.getYInit());
            this.path_triangle.lineTo(this.p2.getXInit(), this.p2.getYInit());
            this.path_triangle.lineTo(this.p3.getXInit(), this.p3.getYInit());
            this.path_triangle.close();
            canvas.drawPath(this.path_triangle, this.paintPathStatic);
        }
    }

    public void setMaskInicial(boolean z) {
        this.maskInicial = z;
    }

    public void desenhaEstatico(Canvas canvas, Bitmap.Config config) {
        canvas.drawBitmap(getBitmap(config), 0.0f, 0.0f, (Paint) null);
    }

    public void desenhaPontos(Canvas canvas) {
        this.p1.to_draw_Point_Movement(canvas);
    }

    public void desenhaDistorcao(Canvas canvas, Bitmap.Config config) {
        Matrix matrix = new Matrix();
        float[] fArr = {this.p1.getXInit() - this.xMenor, this.p1.getYInit() - this.yMenor, this.p2.getXInit() - this.xMenor, this.p2.getYInit() - this.yMenor, this.p3.getXInit() - this.xMenor, this.p3.getYInit() - this.yMenor};
        float[] fArr2 = {this.p1.getXAtual(), this.p1.getYAtual(), this.p2.getXAtual(), this.p2.getYAtual(), this.p3.getXAtual(), this.p3.getYAtual()};
        Bitmap bitmap = getBitmap(config);
        matrix.setPolyToPoly(fArr, 0, fArr2, 0, 3);
        matrix.preScale((((float) bitmap.getWidth()) + 3.0f) / ((float) bitmap.getWidth()), (((float) bitmap.getHeight()) + 3.0f) / ((float) bitmap.getHeight()), (float) (bitmap.getWidth() / 2), (float) (bitmap.getHeight() / 2));
        canvas.drawBitmap(bitmap, matrix, null);
    }

    public void desenhaMascara(Canvas canvas) {
        Path path = new Path();
        path.moveTo(this.p1.getXInit(), this.p1.getYInit());
        path.lineTo(this.p2.getXInit(), this.p2.getYInit());
        path.lineTo(this.p3.getXInit(), this.p3.getYInit());
        path.close();
        canvas.drawPath(path, this.mPaintShader);
    }

    private Bitmap getBitmap(Bitmap.Config config) {
        if (this.bitmap_triangle == null) {
            Bitmap createBitmap = Bitmap.createBitmap(this.originalImage.getWidth(), this.originalImage.getHeight(), config);
            Path path = new Path();
            path.moveTo(this.p1.getXInit(), this.p1.getYInit());
            path.lineTo(this.p2.getXInit(), this.p2.getYInit());
            path.lineTo(this.p3.getXInit(), this.p3.getYInit());
            path.close();
            new Canvas(createBitmap).drawPath(path, this.mPaintShader);
            try {
                float f = this.xMaior - this.xMenor;
                this.largura = f;
                float f2 = this.yMaior - this.yMenor;
                this.altura = f2;
                if (f < 1.0f) {
                    f = 1.0f;
                }
                this.largura = f;
                if (f2 < 1.0f) {
                    f2 = 1.0f;
                }
                this.altura = f2;
                this.altura = Math.round(f2) + Math.round(this.yMenor) > createBitmap.getHeight() ? (float) (createBitmap.getHeight() - Math.round(this.yMenor)) : this.altura;
                float width = Math.round(this.largura) + Math.round(this.xMenor) > createBitmap.getWidth() ? (float) (createBitmap.getWidth() - Math.round(this.xMenor)) : this.largura;
                this.largura = width;
                if (width < 1.0f) {
                    this.xMenor = (float) (createBitmap.getWidth() - 1);
                    this.largura = 1.0f;
                }
                if (this.altura < 1.0f) {
                    this.yMenor = (float) (createBitmap.getHeight() - 1);
                    this.altura = 1.0f;
                }
                this.bitmap_triangle = Bitmap.createBitmap(createBitmap, (int) this.xMenor, (int) this.yMenor, (int) this.largura, (int) this.altura);
            } catch (Exception unused) {
            }
        }
        return this.bitmap_triangle;
    }

    private float sign(Point lPMLWPoint, Point lPMLWPoint2, Point lPMLWPoint3) {
        return ((lPMLWPoint.getXInit() - lPMLWPoint3.getXInit()) * (lPMLWPoint2.getYInit() - lPMLWPoint3.getYInit())) - ((lPMLWPoint2.getXInit() - lPMLWPoint3.getXInit()) * (lPMLWPoint.getYInit() - lPMLWPoint3.getYInit()));
    }

    private boolean hasSameSign(double d, double d2) {
        return Math.signum(d) == Math.signum(d2);
    }

    public boolean contains(Point lPMLWPoint) {
        double cross = lPMLWPoint.sub(this.p1).cross(this.p2.sub(this.p1));
        return hasSameSign(cross, lPMLWPoint.sub(this.p2).cross(this.p3.sub(this.p2))) && hasSameSign(cross, lPMLWPoint.sub(this.p3).cross(this.p1.sub(this.p3)));
    }

    public boolean contains_PointDentro(Point lPMLWPoint) {
        boolean z = sign(lPMLWPoint, this.p1, this.p2) < 0.0f;
        boolean z2 = sign(lPMLWPoint, this.p2, this.p3) < 0.0f;
        return z == z2 && z2 == ((sign(lPMLWPoint, this.p3, this.p1) > 0.0f ? 1 : (sign(lPMLWPoint, this.p3, this.p1) == 0.0f ? 0 : -1)) < 0);
    }

    public Point getP1() {
        return this.p1;
    }

    public Point getP2() {
        return this.p2;
    }

    public Point getP3() {
        return this.p3;
    }

    public void clear() {
        this.bitmap_triangle = null;
    }
}
