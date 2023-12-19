package com.demo.livwllpaper.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.net.Uri;
import com.demo.livwllpaper.Utilsx.Utils;
import com.demo.livwllpaper.beans.Point;
import com.demo.livwllpaper.beans.Project;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Loops_Photo_Motion_Database";
    public static final int DATABASE_VERSION = 3;
    public static DatabaseHandler db;

    public static DatabaseHandler getInstance(Context context) {
        if (db == null) {
            db = new DatabaseHandler(context);
        }
        return db;
    }

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 3);
    }

    @Override 
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(Project.CREATE_TABLE);
        sQLiteDatabase.execSQL(Point.CREATE_TABLE);
    }

    @Override 
    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        onUpgrade(sQLiteDatabase, i, i2);
    }

    @Override 
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL(Point.DROP_TABLE);
        sQLiteDatabase.execSQL(Project.DROP_TABLE);
        onCreate(sQLiteDatabase);
    }

    public Project getProject(long j) {
        Project lPMLWProject;
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor query = readableDatabase.query(Project.TABLE, new String[]{"id", Project.COLUMN_DESCRIPTION, Project.COLUMN_MASK, Project.COLUMN_URI, Project.COLUMN_RESOLUTION, Project.COLUMN_TIME}, "id=?", new String[]{String.valueOf(j)}, null, null, "id", null);
        if (query != null ? query.moveToFirst() : false) {
            lPMLWProject = new Project((long) Integer.parseInt(query.getString(0)), query.getString(1), null, Uri.parse(query.getString(3)), query.getInt(4), query.getInt(5));
            if (query.getBlob(2) != null) {
                lPMLWProject.set_Mask(Utils.bytesToBitmap(query.getBlob(2)).copy(Bitmap.Config.ARGB_8888, true));
            }
            lPMLWProject.load_Points(this);
        } else {
            lPMLWProject = null;
        }
        query.close();
        readableDatabase.close();
        return lPMLWProject;
    }

    public Project get_Last_Project() {
        Project lPMLWProject;
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor query = readableDatabase.query(Project.TABLE, new String[]{"id", Project.COLUMN_DESCRIPTION, Project.COLUMN_MASK, Project.COLUMN_URI, Project.COLUMN_RESOLUTION, Project.COLUMN_TIME}, "id=(SELECT MAX(id)  FROM tb_Project)", null, null, null, null, null);
        if (query.moveToFirst()) {
            lPMLWProject = new Project((long) Integer.parseInt(query.getString(0)), query.getString(1), null, Uri.parse(query.getString(3)), query.getInt(4), query.getInt(5));
            lPMLWProject.set_Resolution_Save(query.getInt(4));
            lPMLWProject.set_Time_Save(query.getInt(5));
            if (query.getBlob(2) != null) {
                lPMLWProject.set_Mask(Utils.bytesToBitmap(query.getBlob(2)).copy(Bitmap.Config.ARGB_8888, true));
            }
            lPMLWProject.load_Points(this);
        } else {
            lPMLWProject = null;
        }
        query.close();
        readableDatabase.close();
        return lPMLWProject;
    }

    public Project getProjectAnterior(Project lPMLWProject) {
        Project lPMLWProject2;
        SQLiteDatabase readableDatabase = getReadableDatabase();
        String[] strArr = {"id", Project.COLUMN_DESCRIPTION, Project.COLUMN_MASK, Project.COLUMN_URI, Project.COLUMN_RESOLUTION, Project.COLUMN_TIME};
        Cursor query = readableDatabase.query(Project.TABLE, strArr, "id=" + (lPMLWProject.getId() - 1) + "", null, null, null, null, null);
        if (query.moveToFirst()) {
            lPMLWProject2 = new Project((long) Integer.parseInt(query.getString(0)), query.getString(1), null, Uri.parse(query.getString(3)), query.getInt(4), query.getInt(5));
            lPMLWProject2.set_Resolution_Save(query.getInt(4));
            lPMLWProject2.set_Time_Save(query.getInt(5));
            if (query.getBlob(2) != null) {
                lPMLWProject2.set_Mask(Utils.bytesToBitmap(query.getBlob(2)).copy(Bitmap.Config.ARGB_8888, true));
            }
            lPMLWProject2.load_Points(this);
        } else {
            lPMLWProject2 = null;
        }
        query.close();
        readableDatabase.close();
        return lPMLWProject2;
    }

    public List<Project> get_All_Projects() {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        ArrayList arrayList = new ArrayList();
        Cursor query = readableDatabase.query(Project.TABLE, new String[]{"id", Project.COLUMN_DESCRIPTION, Project.COLUMN_URI, Project.COLUMN_RESOLUTION, Project.COLUMN_TIME}, null, null, null, null, "id desc", null);
        if (query.moveToFirst()) {
            do {
                arrayList.add(new Project((long) Integer.parseInt(query.getString(0)), query.getString(1), null, Uri.parse(query.getString(2)), query.getInt(3), query.getInt(4)));
            } while (query.moveToNext());
            query.close();
            readableDatabase.close();
            return arrayList;
        }
        query.close();
        readableDatabase.close();
        return arrayList;
    }

    public long getCountProjects() {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        SQLiteStatement compileStatement = readableDatabase.compileStatement("SELECT  count(*) FROM tb_Project");
        long simpleQueryForLong = compileStatement.simpleQueryForLong();
        compileStatement.close();
        readableDatabase.close();
        return simpleQueryForLong;
    }
}
