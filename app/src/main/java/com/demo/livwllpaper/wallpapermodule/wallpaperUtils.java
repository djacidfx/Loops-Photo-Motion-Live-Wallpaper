package com.demo.livwllpaper.wallpapermodule;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


class wallpaperUtils {
    private static final String TAG = "LPMLWwallpaperUtils";

    
    public static void debug(String str, String str2) {
    }

    wallpaperUtils() {
    }

    
    
    
    static Bitmap createVideoThumbnailFromUri(Context context, Uri uri) {
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
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int max = Math.max(width, height);
        if (max <= 512) {
            return bitmap;
        }
        float f = 512.0f / ((float) max);
        return Bitmap.createScaledBitmap(bitmap, Math.round(((float) width) * f), Math.round(f * ((float) height)), true);
    }

    public static int compileShaderResourceGLES30(Context context, int i, int i2) throws RuntimeException {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(i2)));
            StringBuilder sb = new StringBuilder();
            while (true) {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    sb.append(readLine);
                    sb.append('\n');
                } catch (IOException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
            String sb2 = sb.toString();
            int glCreateShader = GLES30.glCreateShader(i);
            if (glCreateShader != 0) {
                GLES30.glShaderSource(glCreateShader, sb2);
                GLES30.glCompileShader(glCreateShader);
                int[] iArr = new int[1];
                GLES30.glGetShaderiv(glCreateShader, 35713, iArr, 0);
                if (iArr[0] != 0) {
                    return glCreateShader;
                }
                String glGetShaderInfoLog = GLES30.glGetShaderInfoLog(glCreateShader);
                GLES30.glDeleteShader(glCreateShader);
                throw new RuntimeException(glGetShaderInfoLog);
            }
            throw new RuntimeException("Failed to create shader");
        } catch (Exception e2) {
            Log.e("in_shader", "Exception_0  " + e2);
            return 0;
        }
    }

    
    public static int linkProgramGLES30(int i, int i2) throws RuntimeException {
        try {
            int glCreateProgram = GLES30.glCreateProgram();
            if (glCreateProgram != 0) {
                GLES30.glAttachShader(glCreateProgram, i);
                GLES30.glAttachShader(glCreateProgram, i2);
                GLES30.glLinkProgram(glCreateProgram);
                int[] iArr = new int[1];
                GLES30.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
                if (iArr[0] != 0) {
                    return glCreateProgram;
                }
                String glGetProgramInfoLog = GLES30.glGetProgramInfoLog(glCreateProgram);
                GLES30.glDeleteProgram(glCreateProgram);
                throw new RuntimeException(glGetProgramInfoLog);
            }
            throw new RuntimeException("Failed to create program");
        } catch (Exception e) {
            Log.e("in_shader", "Exception_1  " + e);
            return 0;
        }
    }

    
    public static int compileShaderResourceGLES20(Context context, int i, int i2) throws RuntimeException {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(i2)));
            StringBuilder sb = new StringBuilder();
            while (true) {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    sb.append(readLine);
                    sb.append('\n');
                } catch (IOException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
            String sb2 = sb.toString();
            int glCreateShader = GLES20.glCreateShader(i);
            if (glCreateShader != 0) {
                GLES20.glShaderSource(glCreateShader, sb2);
                GLES20.glCompileShader(glCreateShader);
                int[] iArr = new int[1];
                GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
                if (iArr[0] != 0) {
                    return glCreateShader;
                }
                String glGetShaderInfoLog = GLES20.glGetShaderInfoLog(glCreateShader);
                GLES20.glDeleteShader(glCreateShader);
                throw new RuntimeException(glGetShaderInfoLog);
            }
            throw new RuntimeException("Failed to create shader");
        } catch (Exception e2) {
            Log.e("in_shader", "Exception_2  " + e2);
            return 0;
        }
    }

    
    public static int linkProgramGLES20(int i, int i2) throws RuntimeException {
        try {
            int glCreateProgram = GLES20.glCreateProgram();
            if (glCreateProgram != 0) {
                GLES20.glAttachShader(glCreateProgram, i);
                GLES20.glAttachShader(glCreateProgram, i2);
                GLES20.glLinkProgram(glCreateProgram);
                int[] iArr = new int[1];
                GLES20.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
                if (iArr[0] != 0) {
                    return glCreateProgram;
                }
                String glGetProgramInfoLog = GLES20.glGetProgramInfoLog(glCreateProgram);
                GLES20.glDeleteProgram(glCreateProgram);
                throw new RuntimeException(glGetProgramInfoLog);
            }
            throw new RuntimeException("Failed to create program");
        } catch (Exception e) {
            Log.e("in_shader", "Exception_3  " + e);
            return 0;
        }
    }
}
