package com.demo.livwllpaper.beans;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Parcel;
import android.os.Parcelable;


public class Point implements Parcelable {
    public static final String COLUMN_ESTATICO = "estatico";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID_Project = "id_Project";
    public static final String COLUMN_XFIM = "xFim";
    public static final String COLUMN_XINIT = "xInit";
    public static final String COLUMN_YFIM = "yFim";
    public static final String COLUMN_YINIT = "yInit";
    public static String CREATE_TABLE = "CREATE TABLE tb_point(id INTEGER PRIMARY KEY,id_Project INTEGER NOT NULL,xInit REAL NOT NULL,yInit REAL NOT NULL,xFim REAL NOT NULL,yFim REAL NOT NULL,estatico INTEGER NOT NULL, FOREIGN KEY(id_Project) REFERENCES tb_Project(id))";
    public static final Creator CREATOR = new creator_listener();
    public static String DROP_TABLE = "DROP TABLE IF EXISTS tb_point";
    private static float RAY_POINT_INITIALISE = 4.8f;
    private static float STROKE_SETA_INIT = 3.8f;
    public static final String TABLE = "tb_point";
    private boolean estatico;
    private long id;
    private float xAtual;
    private float xFim;
    private float xInit;
    private float yAtual;
    private float yFim;
    private float yInit;
    private Paint paint_Point = new Paint(1);
    private Paint paint_Arrow = new Paint(1);
    private boolean selected = false;

    @Override 
    public int describeContents() {
        return 0;
    }

    
    static class creator_listener implements Creator {
        creator_listener() {
        }

        @Override 
        public Point createFromParcel(Parcel parcel) {
            return new Point(parcel);
        }

        @Override 
        public Point[] newArray(int i) {
            return new Point[i];
        }
    }

    
    @Override 
    public Point clone() throws CloneNotSupportedException {
        return (Point) super.clone();
    }

    @Override 
    public String toString() {
        return "P" + this.id + ": (" + getXInit() + "," + getYInit() + ")";
    }

    @Override 
    public boolean equals(Object obj) {
        Point lPMLWPoint = (Point) obj;
        if (!super.equals(obj) && !(this.xInit == lPMLWPoint.xInit && this.yInit == lPMLWPoint.yInit)) {
            long j = this.id;
            if (j == 0 || j != lPMLWPoint.id) {
                return false;
            }
        }
        return true;
    }

    public Point get_Copy(float f) {
        Point lPMLWPoint = new Point(this.xInit * f, this.yInit * f, this.xFim * f, this.yFim * f);
        lPMLWPoint.id = this.id;
        lPMLWPoint.estatico = this.estatico;
        return lPMLWPoint;
    }

    public Point(float f, float f2, boolean z) {
        this.estatico = false;
        this.xInit = f;
        this.yInit = f2;
        this.xFim = f;
        this.yFim = f2;
        this.xAtual = f;
        this.yAtual = f2;
        this.estatico = z;
        initialize();
    }

    public Point(float f, float f2, float f3, float f4) {
        this.estatico = false;
        this.xInit = f;
        this.yInit = f2;
        this.xFim = f3;
        this.yFim = f4;
        this.xAtual = f;
        this.yAtual = f2;
        this.estatico = false;
        initialize();
    }

    private void initialize() {
        this.paint_Point.setAntiAlias(true);
        this.paint_Point.setFilterBitmap(true);
        this.paint_Point.setStyle(Paint.Style.FILL);
        this.paint_Arrow.setAntiAlias(true);
        this.paint_Arrow.setFilterBitmap(true);
        this.paint_Arrow.setStyle(Paint.Style.STROKE);
        this.paint_Arrow.setStrokeJoin(Paint.Join.ROUND);
        this.paint_Arrow.setStrokeCap(Paint.Cap.ROUND);
        this.paint_Arrow.setStrokeWidth(2.0f);
    }

    public float angular_coefficient(Point lPMLWPoint) {
        return (lPMLWPoint.getYInit() - this.yInit) / (lPMLWPoint.getXInit() - this.xInit);
    }

    public boolean in_Circumference(Point lPMLWPoint, Point lPMLWPoint2) {
        Point median = lPMLWPoint.median(lPMLWPoint2);
        return in_Circumference(median, median.distance_to(lPMLWPoint));
    }

    public boolean in_Circumference(Point lPMLWPoint, double d) {
        return lPMLWPoint.distance_to(this) <= d;
    }

    public double distance_to(Point lPMLWPoint) {
        float xInit = lPMLWPoint.getXInit() - getXInit();
        float yInit = lPMLWPoint.getYInit() - getYInit();
        return Math.sqrt((double) ((xInit * xInit) + (yInit * yInit)));
    }

    public Point median(Point lPMLWPoint) {
        return new Point((lPMLWPoint.getXInit() + getXInit()) / 2.0f, (lPMLWPoint.getYInit() + getYInit()) / 2.0f, true);
    }

    public Point sub(Point lPMLWPoint) {
        return new Point(this.xInit - lPMLWPoint.xInit, this.yInit - lPMLWPoint.yInit, true);
    }

    public Point add(Point lPMLWPoint) {
        return new Point(this.xInit + lPMLWPoint.xInit, this.yInit + lPMLWPoint.yInit, true);
    }

    public Point mult(double d) {
        return new Point((float) (((double) this.xInit) * d), (float) (((double) this.yInit) * d), true);
    }

    public double mag() {
        float f = this.xInit;
        float f2 = this.yInit;
        return Math.sqrt((double) ((f * f) + (f2 * f2)));
    }

    public double dot(Point lPMLWPoint) {
        return (double) ((this.xInit * lPMLWPoint.xInit) + (this.yInit * lPMLWPoint.yInit));
    }

    public double cross(Point lPMLWPoint) {
        return (double) ((this.yInit * lPMLWPoint.xInit) - (this.xInit * lPMLWPoint.yInit));
    }

    public void to_draw_Point_Movement(Canvas canvas) {
        if (!this.estatico) {
            this.paint_Point.setColor(Color.parseColor("#ffffff"));
            float xAtual = getXAtual();
            float yAtual = getYAtual();
            float f = RAY_POINT_INITIALISE;
            canvas.drawCircle(xAtual, yAtual, Math.max(f, f / 3.0f), this.paint_Point);
        }
    }

    public void to_draw_only_point(Canvas canvas, int i, float f) {
        this.paint_Point.setAlpha(i);
        if (this.selected) {
            this.paint_Point.setColor(Color.parseColor("#ffffff"));
        } else {
            this.paint_Point.setColor(is_static() ? -65536 : Color.parseColor("#ffffff"));
        }
        float f2 = RAY_POINT_INITIALISE;
        float max = Math.max(f2 / f, f2 / 3.0f);
        if (this.selected) {
            max *= 1.5f;
        }
        canvas.drawCircle(getXInit(), getYInit(), max, this.paint_Point);
    }

    public void to_draw_Arrow(Canvas canvas, int i, float f) {
        this.paint_Arrow.setAlpha(i);
        if (this.selected) {
            this.paint_Arrow.setColor(Color.parseColor("#000000"));
            Paint paint = this.paint_Arrow;
            float f2 = STROKE_SETA_INIT;
            paint.setStrokeWidth(Math.max(f2 / f, f2 / 2.0f));
        } else {
            this.paint_Arrow.setColor(is_static() ? -65536 : Color.parseColor("#9ff40b"));
            Paint paint2 = this.paint_Arrow;
            float f3 = STROKE_SETA_INIT;
            paint2.setStrokeWidth(Math.max((f3 / 2.0f) / f, f3 / 4.0f));
        }
        float xInit = getXInit();
        float xFim = getXFim();
        float yInit = getYInit();
        float yFim = getYFim();
        canvas.drawLine(xInit, yInit, xFim, yFim, this.paint_Arrow);
        float f4 = xFim - xInit;
        float f5 = yFim - yInit;
        float sqrt = (float) (1.0d / (Math.sqrt((double) ((f4 * f4) + (f5 * f5))) / ((double) Math.max(8.0f / f, 4.0f))));
        float f6 = 1.0f - sqrt;
        float f7 = f6 * f4;
        float f8 = sqrt * f5;
        float f9 = f6 * f5;
        float f10 = sqrt * f4;
        float f11 = (f9 - f10) + yInit;
        float f12 = yInit + f9 + f10;
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(f7 + f8 + xInit, f11);
        path.lineTo(xFim, yFim);
        path.lineTo(xInit + (f7 - f8), f12);
        this.paint_Arrow.setStyle(Paint.Style.FILL);
        path.close();
        canvas.drawPath(path, this.paint_Arrow);
    }

    public void set_Current_Position_Animation(float f, float f2) {
        this.xAtual = f;
        this.yAtual = f2;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long j) {
        this.id = j;
    }

    public float getXAtual() {
        return this.xAtual;
    }

    public float getYAtual() {
        return this.yAtual;
    }

    public float getXInit() {
        return this.xInit;
    }

    public float getYInit() {
        return this.yInit;
    }

    public float getXFim() {
        return this.xFim;
    }

    public float getYFim() {
        return this.yFim;
    }

    public boolean is_static() {
        return this.estatico;
    }

    public void set_Selected(boolean z) {
        this.selected = z;
    }

    public boolean is_Selected() {
        return this.selected;
    }

    public void setOrigem(float f, float f2) {
        this.xInit = f;
        this.yInit = f2;
    }

    public void set_Destination(float f, float f2) {
        this.xFim = f;
        this.yFim = f2;
        this.estatico = false;
    }

    public Point(Parcel parcel) {
        boolean z = false;
        this.estatico = false;
        this.id = parcel.readLong();
        this.xInit = parcel.readFloat();
        this.yInit = parcel.readFloat();
        this.estatico = parcel.readInt() == 1 ? true : z;
        this.xFim = parcel.readFloat();
        this.yFim = parcel.readFloat();
        initialize();
    }

    @Override 
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.id);
        parcel.writeFloat(this.xInit);
        parcel.writeFloat(this.yInit);
        parcel.writeInt(this.estatico ? 1 : 0);
        parcel.writeFloat(this.xFim);
        parcel.writeFloat(this.yFim);
    }
}
