package com.demo.livwllpaper.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.CancellationSignal;
import java.io.FileNotFoundException;
import java.io.IOException;


class LPMLWVideoProvider extends ContentProvider {
    @Override 
    public int delete(Uri uri, String str, String[] strArr) {
        return 0;
    }

    @Override 
    public String getType(Uri uri) {
        return null;
    }

    @Override 
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override 
    public boolean onCreate() {
        return false;
    }

    @Override 
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        return null;
    }

    @Override 
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }

    @Override 
    public AssetFileDescriptor openAssetFile(Uri uri, String str) throws FileNotFoundException {
        AssetManager assets = getContext().getAssets();
        String lastPathSegment = uri.getLastPathSegment();
        if (lastPathSegment != null) {
            try {
                return assets.openFd(lastPathSegment);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            throw new FileNotFoundException();
        }
    }

    @Override 
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2, CancellationSignal cancellationSignal) {
        return super.query(uri, strArr, str, strArr2, str2, cancellationSignal);
    }
}
