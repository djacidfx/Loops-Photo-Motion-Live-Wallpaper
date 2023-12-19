package com.demo.livwllpaper.wallpapermodule;

import android.graphics.Bitmap;
import android.net.Uri;
import java.util.Objects;
import org.json.JSONException;
import org.json.JSONObject;


public class WallpaperCardModel {
    private static final String TAG = "LPMLWWallpaperCardModel";
    private String name;
    private String path;
    private final Bitmap thumbnail;
    private final Type type;
    private Uri uri;
    private boolean valid = true;

    
    public enum Type {
        INTERNAL,
        EXTERNAL
    }

    public WallpaperCardModel(String str, String str2, Uri uri, Type type, Bitmap bitmap) {
        setName(str);
        setPath(str2);
        setUri(uri);
        this.type = type;
        this.thumbnail = bitmap;
    }

    public String getName() {
        return this.name;
    }

    private void setName(String str) {
        this.name = str;
    }

    public String getPath() {
        return this.path;
    }

    private void setPath(String str) {
        this.path = str;
    }

    
    public Uri getUri() {
        return this.uri;
    }

    private void setUri(Uri uri) {
        this.uri = uri;
    }

    Bitmap getThumbnail() {
        return this.thumbnail;
    }

    
    public boolean equals(WallpaperCardModel lPMLWWallpaperCardModel) {
        return Objects.equals(getPath(), lPMLWWallpaperCardModel.getPath());
    }

    boolean isRemovable() {
        return this.type == Type.EXTERNAL;
    }

    boolean isCurrent() {
        return LPMLWApplication.isCurrentWallpaperCard(this);
    }

    boolean isValid() {
        return this.valid;
    }

    
    public void setInvalid() {
        this.valid = false;
    }

    
    public Type getType() {
        return this.type;
    }

    
    public JSONObject toJSON() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("name", getName());
        jSONObject.put("path", getPath());
        int i = AnonymousClass1.$SwitchMap$com$livwllpaper$loppphtodemo$wallpapermodule$LPMLWWallpaperCardModel$Type[getType().ordinal()];
        if (i == 1) {
            jSONObject.put("type", "INTERNAL");
        } else if (i == 2) {
            jSONObject.put("type", "EXTERNAL");
        }
        return jSONObject;
    }

    
    
    
    public static  class AnonymousClass1 {
        static final  int[] $SwitchMap$com$livwllpaper$loppphtodemo$wallpapermodule$LPMLWWallpaperCardModel$Type;

        static {
            int[] iArr = new int[Type.values().length];
            $SwitchMap$com$livwllpaper$loppphtodemo$wallpapermodule$LPMLWWallpaperCardModel$Type = iArr;
            try {
                iArr[Type.INTERNAL.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$livwllpaper$loppphtodemo$wallpapermodule$LPMLWWallpaperCardModel$Type[Type.EXTERNAL.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }
}
