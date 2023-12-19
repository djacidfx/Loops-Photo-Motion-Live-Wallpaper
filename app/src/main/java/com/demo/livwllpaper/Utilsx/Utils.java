package com.demo.livwllpaper.Utilsx;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.demo.livwllpaper.R;
import com.demo.livwllpaper.activities.Exportactivity;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class Utils {
    private static AlertDialog alertaProfessional;
    public static int height;
    private static final Paint pSemMask;
    private static final Paint paintMask;
    public static int width;
    public static String MY_PREFS_NAME = "prefs";
    public static boolean is_arrow_movement = false;
    public static int perform_Action = 0;

    public static void scannFiles(Context context) {
    }

    public static void showMensagemProfessional(Activity activity) {
    }

    static {
        Paint paint = new Paint(1);
        pSemMask = paint;
        Paint paint2 = new Paint(1);
        paintMask = paint2;
        paint2.setAntiAlias(true);
        paint2.setFilterBitmap(true);
        paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        paint.setFilterBitmap(true);
    }

    
    public static class scan_media_listener implements MediaScannerConnection.OnScanCompletedListener {
        @Override 
        public void onScanCompleted(String str, Uri uri) {
        }

        scan_media_listener() {
        }
    }

    private Utils() {
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    
    
    
    public static Bitmap createVideoThumbnailFromUri(Context context, Uri uri) {
        Bitmap bitmap;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            try {
                mediaMetadataRetriever = new MediaMetadataRetriever();
            } catch (Throwable th) {
                try {
                    try {
                        mediaMetadataRetriever.release();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
                throw th;
            }
        } catch (RuntimeException e2) {
            e2.printStackTrace();
        }
        try {
            mediaMetadataRetriever.setDataSource(context, uri);
            bitmap = mediaMetadataRetriever.getFrameAtTime(-1);
            try {
                try {
                    mediaMetadataRetriever.release();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (RuntimeException e3) {
                e3.printStackTrace();
            }
        } catch (IllegalArgumentException e4) {
            e4.printStackTrace();
            try {
                mediaMetadataRetriever.release();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            bitmap = null;
            if (bitmap == null) {
            }
        } catch (RuntimeException e5) {
            e5.printStackTrace();
            try {
                mediaMetadataRetriever.release();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            bitmap = null;
            if (bitmap == null) {
            }
        }
        if (bitmap == null) {
            return null;
        }
        int width2 = bitmap.getWidth();
        int height2 = bitmap.getHeight();
        int max = Math.max(width2, height2);
        if (max <= 512) {
            return bitmap;
        }
        float f = 512.0f / ((float) max);
        return Bitmap.createScaledBitmap(bitmap, Math.round(((float) width2) * f), Math.round(f * ((float) height2)), true);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        int i5 = 1;
        if (i3 > i2 || i4 > i) {
            int i6 = i3 / 2;
            int i7 = i4 / 2;
            while (i6 / i5 >= i2 && i7 / i5 >= i) {
                i5 *= 2;
            }
        }
        return i5;
    }

    public static boolean verification_permission(final Activity activity, final String[] strArr, String str, String str2, final int i, Exportactivity.Permission_Listener permission_Listener) {
        boolean z = true;
        boolean z2 = false;
        for (String str3 : strArr) {
            if (ContextCompat.checkSelfPermission(activity, str3) != 0) {
                z = false;
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, str3)) {
                z2 = true;
            }
        }
        if (z) {
            permission_Listener.onPermissionGranted();
        } else if (z2) {
            new AlertDialog.Builder(activity).setTitle(str).setMessage(str2).setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() { 
                @Override 
                public void onClick(DialogInterface dialogInterface, int i2) {
                    ActivityCompat.requestPermissions(activity, strArr, i);
                }
            }).create().show();
        } else {
            ActivityCompat.requestPermissions(activity, strArr, i);
        }
        return z;
    }

    public static byte[] bitmapToBytes(Bitmap bitmap, int i) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, i, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Bitmap bytesToBitmap(byte[] bArr) {
        return BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
    }

    public static Bitmap get_masked_image(Bitmap bitmap, Bitmap bitmap2, Bitmap.Config config) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
        Canvas canvas = new Canvas(createBitmap);
        if (bitmap2 != null) {
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
            canvas.drawBitmap(bitmap2, 0.0f, 0.0f, paintMask);
        }
        return createBitmap;
    }

    public static Bitmap getImage_without_Mask(Bitmap bitmap, Bitmap bitmap2, Bitmap.Config config) {
        Bitmap copy = bitmap.copy(config, true);
        if (!(bitmap == null || bitmap2 == null)) {
            Canvas canvas = new Canvas(copy);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
            canvas.drawBitmap(bitmap2, 0.0f, 0.0f, pSemMask);
        }
        return copy;
    }

    public static File getPublicAlbumStorageDir(Context context, String str) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), str);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static void scannFile(Context context, File file) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }

    public static String getImgPath(Context context, Uri uri) {
        Cursor query = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_data"}, null, null, "_id DESC");
        try {
            query.moveToFirst();
            return query.getString(query.getColumnIndexOrThrow("_data"));
        } finally {
            query.close();
        }
    }

    public static Uri writeImageAndGetPathUri(Context context, Bitmap bitmap, String str) throws Exception {
        File privateAlbumStorageDir = getPrivateAlbumStorageDir(context, context.getResources().getString(R.string.images_folder));
        File file = new File(privateAlbumStorageDir, str + ".png");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        fileOutputStream.close();
        return Uri.parse(file.getPath());
    }

    public static File getPrivateAlbumStorageDir(Context context, String str) {
      //  File file = new File(String.valueOf(new StorageUtils(context).getCreateStorageDirectoryPath(str)));


        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + str);

        if (!file.exists()) {
            file.mkdirs();
        }
        file.mkdirs();
        return file;
    }

    public static void copyFile(File file, File file2) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read > 0) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileOutputStream.close();
                    fileInputStream.close();
                    return;
                }
            }
        } catch (Throwable unused) {
            fileInputStream.close();
        }
    }

    public static Drawable getDrawable(Context context, int i, int i2) {
        Drawable drawable = getDrawable(context, i);
        Paint_Icon(drawable, i2);
        return drawable;
    }

    public static int getColor(Context context, int i) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.getColor(i);
        }
        return ResourcesCompat.getColor(context.getResources(), i, null);
    }

    public static Drawable getDrawable(Context context, int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            return context.getDrawable(i);
        }
        return ResourcesCompat.getDrawable(context.getResources(), i, null);
    }

    public static void Paint_Icon(Drawable drawable, int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            drawable.setTint(i);
        } else {
            drawable.setColorFilter(i, PorterDuff.Mode.SRC_IN);
        }
    }

    private static boolean contains_Transparent(Bitmap bitmap) {
        boolean z = false;
        for (int i = 0; i < bitmap.getWidth(); i++) {
            int i2 = 0;
            while (true) {
                if (i2 >= bitmap.getHeight()) {
                    break;
                } else if (bitmap.getPixel(i, i2) == Color.argb(255, 0, 0, 0)) {
                    z = true;
                    break;
                } else {
                    i2++;
                }
            }
            if (z) {
                break;
            }
        }
        return z;
    }

    public static void Infinity_edge(Bitmap bitmap) {
        while (contains_Transparent(bitmap)) {
            for (int i = 0; i < bitmap.getWidth(); i++) {
                for (int i2 = 0; i2 < bitmap.getHeight(); i2++) {
                    int pixel = bitmap.getPixel(i, i2);
                    if (pixel != Color.argb(255, 0, 0, 0)) {
                        int max = Math.max(i2 - 1, 0);
                        int min = Math.min(i + 1, bitmap.getWidth() - 1);
                        int min2 = Math.min(i2 + 1, bitmap.getHeight() - 1);
                        for (int max2 = Math.max(i - 1, 0); max2 <= min; max2++) {
                            for (int i3 = max; i3 <= min2; i3++) {
                                if (!(max2 == i && i3 == i2) && bitmap.getPixel(max2, i3) == Color.argb(255, 0, 0, 0)) {
                                    bitmap.setPixel(max2, i3, pixel);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
