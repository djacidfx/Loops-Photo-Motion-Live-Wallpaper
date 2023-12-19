package com.demo.livwllpaper.Utilsx;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class StorageUtils {
    String Package_name;
    private Context kContext;

    public StorageUtils(Context context) {
        this.kContext = context;
        this.Package_name = context.getPackageName();
    }
    public File getStorageDir30(String str) {
        File file;
        int i = 0;
        while (true) {
            file = null;

            try {
                if (i >= this.kContext.getExternalMediaDirs().length || this.kContext.getExternalMediaDirs()[i].getAbsolutePath().startsWith("/storage/emulated/0/Android/media/")) {
                    break;
                }
                i++;
            } catch (Exception unused) {
                return file;
            }
        }
        if (i != this.kContext.getExternalMediaDirs().length) {
            File file2 = new File(this.kContext.getExternalMediaDirs()[i], str);
            try {
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                file = file2;
            } catch (Exception unused2) {
                return file2;
            }
        }
        Log.e("in_storage", "fdirectory_exists " + file.exists());
        return file;
    }

    public File getCreateStorageDirectoryPath(String str) {
        return getStorageDir30(str);
    }


}
