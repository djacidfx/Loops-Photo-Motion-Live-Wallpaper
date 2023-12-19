package com.demo.livwllpaper.wallpapermodule;

import android.content.Context;
import android.opengl.GLSurfaceView;
import com.google.android.exoplayer2.SimpleExoPlayer;


abstract class WallpaperRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "LPMLWWallpaperRenderer";
    final Context context;

    
    public abstract void setOffset(float f, float f2);

    
    public abstract void setScreenSize(int i, int i2);

    
    public abstract void setSourcePlayer(SimpleExoPlayer simpleExoPlayer);

    
    public abstract void setVideoSizeAndRotation(int i, int i2, int i3);

    
    public WallpaperRenderer(Context context) {
        this.context = context;
    }

    protected Context getContext() {
        return this.context;
    }
}
