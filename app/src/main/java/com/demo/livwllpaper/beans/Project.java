package com.demo.livwllpaper.beans;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.text.TextUtils;
import com.demo.livwllpaper.Utilsx.Utils;
import com.demo.livwllpaper.activities.SaveActivity;
import com.demo.livwllpaper.databases.DatabaseHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class Project implements Parcelable {
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IMAGEM = "imagem";
    public static final String COLUMN_MASK = "mask";
    public static final String COLUMN_RESOLUTION = "resolution";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_URI = "uri";
    public static String CREATE_TABLE = "CREATE TABLE tb_Project(id INTEGER PRIMARY KEY,description TEXT,imagem BLOB,mask BLOB,uri TEXT,resolution INTEGER,time INTEGER)";
    public static final Creator CREATOR = new Creator_class();
    public static String DROP_TABLE = "DROP TABLE IF EXISTS tb_Project";
    public static final String TABLE = "tb_Project";
    public static final int TAMANHO_APRESENTACAO = 800;
    public static final int TEMPO_SAVE_INIT = 6000;
    private long id;
    private Bitmap imagem;
    private List<Point> list_LPMLW_Points = new CopyOnWriteArrayList();
    private List<Point> list_Points_Presentation = new CopyOnWriteArrayList();
    private Bitmap mask;
    private float proportion_Presentation;
    private int resolutionSave;
    private int timeSave;
    private String title;
    private Uri uri;

    @Override 
    public int describeContents() {
        return 0;
    }

    
    public static class Creator_class implements Creator {
        Creator_class() {
        }

        @Override 
        public Project createFromParcel(Parcel parcel) {
            return new Project(parcel);
        }

        @Override 
        public Project[] newArray(int i) {
            return new Project[i];
        }
    }

    public Project(String str, Bitmap bitmap, Uri uri) {
        this.title = str;
        setImagem(bitmap);
        this.uri = uri;
        set_Time_Save(6000);
        set_Resolution_Save(Math.max(bitmap.getHeight(), bitmap.getWidth()));
    }

    public Project(long j, String str, Bitmap bitmap, Uri uri, int i, int i2) {
        this.id = j;
        this.title = str;
        if (bitmap != null) {
            setImagem(bitmap);
        }
        this.uri = uri;
        set_Resolution_Save(i);
        set_Time_Save(i2);
    }

    public void refresh_Time_Resolution(DatabaseHandler databaseHandler) {
        SQLiteDatabase readableDatabase = databaseHandler.getReadableDatabase();
        Cursor query = readableDatabase.query(TABLE, new String[]{COLUMN_RESOLUTION, COLUMN_TIME}, "id=?", new String[]{String.valueOf(this.id)}, null, null, "id", null);
        if (query != null) {
            if (query.moveToFirst()) {
                set_Resolution_Save(query.getInt(0));
                set_Time_Save(query.getInt(1));
            }
            query.close();
            readableDatabase.close();
        }
    }

    public boolean reloadBitmapUri(Context context, DatabaseHandler databaseHandler) {
        boolean z;
        Cursor query = context.getContentResolver().query(this.uri, new String[]{"_data"}, null, null, null);
        if (query != null) {
            if (query.moveToFirst() && new File(query.getString(0)).exists()) {
                z = true;
            } else {
                z = false;
            }
            query.close();
        } else {
            z = false;
        }
        if (z) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                InputStream openInputStream = context.getContentResolver().openInputStream(this.uri);
                setImagem(BitmapFactory.decodeStream(openInputStream, null, options));
                openInputStream.close();
                File file = new File(Utils.getImgPath(context, this.uri));
                boolean delete = file.delete();
                if (!delete && !(delete = file.getCanonicalFile().delete()) && !(delete = context.getApplicationContext().deleteFile(file.getName()))) {
                    delete = context.getContentResolver().delete(this.uri, null, null) > 0;
                }
                if (delete) {
                    this.uri = Utils.writeImageAndGetPathUri(context, getImagem(), this.title);
                    updateProject(databaseHandler);
                }
                Utils.scannFiles(context);
                return true;
            } catch (Exception unused) {
                return false;
            }
        } else {
            try {
                setImagem(BitmapFactory.decodeFileDescriptor(ParcelFileDescriptor.open(new File(this.uri.getPath()), 268435456).getFileDescriptor()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    public long getId() {
        return this.id;
    }

    public void setId(long j) {
        this.id = j;
    }

    public float get_Proportion() {
        return this.proportion_Presentation;
    }

    public Bitmap get_Mask() {
        return this.mask;
    }

    public void set_Mask(Bitmap bitmap) {
        this.mask = bitmap;
    }

    public void setImagem(Bitmap bitmap) {
        this.imagem = bitmap;
        if (bitmap != null) {
            this.proportion_Presentation = ((float) (bitmap.getWidth() > bitmap.getHeight() ? bitmap.getWidth() : bitmap.getHeight())) / 800.0f;
        }
    }

    public int get_Time_Save() {
        return this.timeSave;
    }

    public void set_Time_Save(int i) {
        if (i < 2000) {
            this.timeSave = 2000;
        } else if (i > 10000) {
            this.timeSave = 10000;
        } else {
            this.timeSave = i;
        }
    }

    public int get_Save_Resolution() {
        return this.resolutionSave;
    }

    public void set_Resolution_Save(int i) {
        if (i < SaveActivity.MIN_RESOLUTION_SAVE) {
            this.resolutionSave = SaveActivity.MIN_RESOLUTION_SAVE;
        } else if (i > SaveActivity.MAX_RESOLUTION_SAVE) {
            this.resolutionSave = SaveActivity.MAX_RESOLUTION_SAVE;
        } else {
            this.resolutionSave = i;
        }
    }

    public Bitmap getImagem() {
        return this.imagem;
    }

    public byte[] getImagemBytes(int i) {
        return Utils.bitmapToBytes(this.imagem, i);
    }

    public int getWidth() {
        Bitmap bitmap = this.imagem;
        if (bitmap != null) {
            return bitmap.getWidth();
        }
        return 0;
    }

    public int getHeight() {
        Bitmap bitmap = this.imagem;
        if (bitmap != null) {
            return bitmap.getHeight();
        }
        return 0;
    }

    public byte[] get_Masking_Bytes() {
        Bitmap bitmap = this.mask;
        if (bitmap != null) {
            return Utils.bitmapToBytes(bitmap, 0);
        }
        return null;
    }

    public List<Point> get_Points_List() {
        return this.list_LPMLW_Points;
    }

    public List<Point> getListaPontosApresentacao() {
        return this.list_Points_Presentation;
    }

    public void set_Title(String str) {
        this.title = str;
    }

    public String get_Title() {
        return this.title;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return this.uri;
    }

    public synchronized int updateProject(DatabaseHandler databaseHandler) {
        int update;
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DESCRIPTION, get_Title());
        contentValues.put(COLUMN_URI, getUri().toString());
        contentValues.put(COLUMN_MASK, get_Masking_Bytes());
        contentValues.put(COLUMN_RESOLUTION, Integer.valueOf(get_Save_Resolution()));
        contentValues.put(COLUMN_TIME, Integer.valueOf(get_Time_Save()));
        SQLiteDatabase writableDatabase = databaseHandler.getWritableDatabase();
        update = writableDatabase.update(TABLE, contentValues, "id = ?", new String[]{String.valueOf(getId())});
        writableDatabase.close();
        return update;
    }

    public synchronized int update_Title(DatabaseHandler databaseHandler) {
        int update;
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DESCRIPTION, get_Title());
        SQLiteDatabase writableDatabase = databaseHandler.getWritableDatabase();
        update = writableDatabase.update(TABLE, contentValues, "id = ?", new String[]{String.valueOf(getId())});
        writableDatabase.close();
        return update;
    }

    public synchronized void deleteProject(DatabaseHandler databaseHandler) {
        delete_Points(databaseHandler, this.list_LPMLW_Points);
        SQLiteDatabase writableDatabase = databaseHandler.getWritableDatabase();
        writableDatabase.delete(TABLE, "id = ?", new String[]{String.valueOf(getId())});
        writableDatabase.close();
    }

    public synchronized void delete_Points(DatabaseHandler databaseHandler, List<Point> list) {
        if (list != null) {
            if (list.size() > 0) {
                this.list_LPMLW_Points.removeAll(list);
                Long[] lArr = new Long[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    lArr[i] = Long.valueOf(list.get(i).getId());
                }
                SQLiteDatabase writableDatabase = databaseHandler.getWritableDatabase();
                writableDatabase.execSQL(String.format("DELETE FROM tb_point WHERE id IN (%s);", TextUtils.join(", ", lArr)));
                writableDatabase.close();
            }
        }
    }

    public synchronized void deletePonto(DatabaseHandler databaseHandler, Point lPMLWPoint) {
        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
        copyOnWriteArrayList.add(lPMLWPoint);
        delete_Points(databaseHandler, copyOnWriteArrayList);
    }

    public synchronized void addProject(DatabaseHandler databaseHandler) {
        SQLiteDatabase writableDatabase = databaseHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DESCRIPTION, get_Title());
        contentValues.put(COLUMN_URI, getUri().toString());
        contentValues.put(COLUMN_MASK, get_Masking_Bytes());
        contentValues.put(COLUMN_RESOLUTION, Integer.valueOf(get_Save_Resolution()));
        contentValues.put(COLUMN_TIME, Integer.valueOf(get_Time_Save()));
        setId(writableDatabase.insert(TABLE, null, contentValues));
        writableDatabase.close();
    }

    public synchronized int update_Points(DatabaseHandler databaseHandler, Point lPMLWPoint) {
        int update;
        synchronized (this) {
            Point lPMLWPoint2 = lPMLWPoint.get_Copy(this.proportion_Presentation);
            ContentValues contentValues = new ContentValues();
            contentValues.put(Point.COLUMN_XINIT, Float.valueOf(lPMLWPoint2.getXInit()));
            contentValues.put(Point.COLUMN_YINIT, Float.valueOf(lPMLWPoint2.getYInit()));
            contentValues.put(Point.COLUMN_XFIM, Float.valueOf(lPMLWPoint2.getXFim()));
            contentValues.put(Point.COLUMN_YFIM, Float.valueOf(lPMLWPoint2.getYFim()));
            contentValues.put(Point.COLUMN_ESTATICO, Integer.valueOf(lPMLWPoint2.is_static() ? 1 : 0));
            contentValues.put(Point.COLUMN_ID_Project, Long.valueOf(getId()));
            SQLiteDatabase writableDatabase = databaseHandler.getWritableDatabase();
            update = writableDatabase.update(Point.TABLE, contentValues, "id = ?", new String[]{String.valueOf(lPMLWPoint2.getId())});
            writableDatabase.close();
        }
        return update;
    }

    public synchronized void add_point(DatabaseHandler databaseHandler, Point lPMLWPoint) {
        Point lPMLWPoint2 = lPMLWPoint.get_Copy(this.proportion_Presentation);
        SQLiteDatabase writableDatabase = databaseHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Point.COLUMN_XINIT, Float.valueOf(lPMLWPoint2.getXInit()));
        contentValues.put(Point.COLUMN_YINIT, Float.valueOf(lPMLWPoint2.getYInit()));
        contentValues.put(Point.COLUMN_XFIM, Float.valueOf(lPMLWPoint2.getXFim()));
        contentValues.put(Point.COLUMN_YFIM, Float.valueOf(lPMLWPoint2.getYFim()));
        contentValues.put(Point.COLUMN_ESTATICO, Integer.valueOf(lPMLWPoint2.is_static() ? 1 : 0));
        contentValues.put(Point.COLUMN_ID_Project, Long.valueOf(getId()));
        lPMLWPoint.setId(writableDatabase.insert(Point.TABLE, null, contentValues));
        lPMLWPoint2.setId(lPMLWPoint.getId());
        this.list_LPMLW_Points.add(lPMLWPoint2);
        writableDatabase.close();
    }

    public synchronized void load_Points(DatabaseHandler databaseHandler) {
        Point lPMLWPoint;
        this.list_LPMLW_Points.clear();
        SQLiteDatabase readableDatabase = databaseHandler.getReadableDatabase();
        Cursor query = readableDatabase.query(Point.TABLE, new String[]{"id", Point.COLUMN_ESTATICO, Point.COLUMN_XINIT, Point.COLUMN_YINIT, Point.COLUMN_XFIM, Point.COLUMN_YFIM}, "id_Project=?", new String[]{String.valueOf(this.id)}, null, null, null, null);
        if (query.moveToFirst()) {
            do {
                if (query.getInt(1) == 1) {
                    lPMLWPoint = new Point(query.getFloat(2), query.getFloat(3), true);
                } else {
                    lPMLWPoint = new Point(query.getFloat(2), query.getFloat(3), query.getFloat(4), query.getFloat(5));
                }
                lPMLWPoint.setId(query.getLong(0));
                this.list_LPMLW_Points.add(lPMLWPoint);
            } while (query.moveToNext());
            query.close();
            readableDatabase.close();
        } else {
            query.close();
            readableDatabase.close();
        }
    }

    public Project(Parcel parcel) {
        this.id = parcel.readLong();
        this.title = parcel.readString();
        this.uri = Uri.parse(parcel.readString());
        this.resolutionSave = parcel.readInt();
        this.timeSave = parcel.readInt();
        parcel.readList(this.list_LPMLW_Points, Point.class.getClassLoader());
    }

    @Override 
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.id);
        parcel.writeString(this.title);
        parcel.writeString(this.uri.toString());
        parcel.writeInt(this.resolutionSave);
        parcel.writeInt(this.timeSave);
        parcel.writeList(this.list_LPMLW_Points);
    }
}
