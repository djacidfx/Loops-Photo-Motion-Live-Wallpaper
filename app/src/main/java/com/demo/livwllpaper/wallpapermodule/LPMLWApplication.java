package com.demo.livwllpaper.wallpapermodule;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.demo.livwllpaper.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.json.JSONArray;
import org.json.JSONException;


public class LPMLWApplication extends Application {
    private static final String CURRENT_CARD_PREF = "currentLPMLWWallpaperCardModel";
    public static final String INTERNAL_WALLPAPER_IMAGE_PATH = "wallpapers/assetimg_1.jpg";
    public static final String INTERNAL_WALLPAPER_VIDEO_PATH = "asset_1.mp4";
    public static final String JSON_FILE_NAME = "data.json";
    public static final String OPTIONS_PREF = "options";
    public static final String SLIDE_WALLPAPER_KEY = "slideWallpaper";
    private static final String TAG = "LPMLWApplication";
    private static List<WallpaperCardModel> cards;
    private static WallpaperCardModel currentLPMLWWallpaperCardModel;
    private static WallpaperCardModel previewLPMLWWallpaperCardModel;

    public static List<WallpaperCardModel> getCards(Context context) {
        if (cards == null) {
            initCards(context);
        }
        return cards;
    }

    public static WallpaperCardModel getCurrentWallpaperCard(Context context) {
        if (currentLPMLWWallpaperCardModel == null) {
            currentLPMLWWallpaperCardModel = loadWallpaperCardPreference(context);
        }
        return currentLPMLWWallpaperCardModel;
    }

    public static void setCurrentWallpaperCard(Context context, WallpaperCardModel lPMLWWallpaperCardModel) {
        currentLPMLWWallpaperCardModel = lPMLWWallpaperCardModel;
        if (lPMLWWallpaperCardModel != null) {
            saveWallpaperCardPreference(context, lPMLWWallpaperCardModel);
        }
    }

    public static boolean isCurrentWallpaperCard(WallpaperCardModel lPMLWWallpaperCardModel) {
        WallpaperCardModel lPMLWWallpaperCardModel2 = currentLPMLWWallpaperCardModel;
        return lPMLWWallpaperCardModel2 != null && lPMLWWallpaperCardModel.equals(lPMLWWallpaperCardModel2);
    }

    public static WallpaperCardModel getPreviewLPMLWWallpaperCardModel() {
        return previewLPMLWWallpaperCardModel;
    }

    public static void setPreviewLPMLWWallpaperCardModel(WallpaperCardModel lPMLWWallpaperCardModel) {
        previewLPMLWWallpaperCardModel = lPMLWWallpaperCardModel;
    }

    public static JSONArray getCardsJSONArray() throws JSONException {
        JSONArray jSONArray = new JSONArray();
        List<WallpaperCardModel> list = cards;
        if (list != null) {
            for (WallpaperCardModel lPMLWWallpaperCardModel : list) {
                if (lPMLWWallpaperCardModel.getType() != WallpaperCardModel.Type.INTERNAL) {
                    jSONArray.put(lPMLWWallpaperCardModel.toJSON());
                }
            }
        }
        return jSONArray;
    }

    private static void saveWallpaperCardPreference(Context context, WallpaperCardModel lPMLWWallpaperCardModel) {
        SharedPreferences.Editor edit = context.getSharedPreferences(CURRENT_CARD_PREF, 0).edit();
        edit.putString("name", lPMLWWallpaperCardModel.getName());
        edit.putString("path", lPMLWWallpaperCardModel.getPath());
        int i = AnonymousClass1.$SwitchMap$com$livwllpaper$loppphtodemo$wallpapermodule$LPMLWWallpaperCardModel$Type[lPMLWWallpaperCardModel.getType().ordinal()];
        if (i == 1) {
            edit.putString("type", "INTERNAL");
        } else if (i == 2) {
            edit.putString("type", "EXTERNAL");
        }
        edit.apply();
    }
    public static class AnonymousClass1 {
        static final int[] $SwitchMap$com$livwllpaper$loppphtodemo$wallpapermodule$LPMLWWallpaperCardModel$Type;

        static {
            int[] iArr = new int[WallpaperCardModel.Type.values().length];
            $SwitchMap$com$livwllpaper$loppphtodemo$wallpapermodule$LPMLWWallpaperCardModel$Type = iArr;
            try {
                iArr[WallpaperCardModel.Type.INTERNAL.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$livwllpaper$loppphtodemo$wallpapermodule$LPMLWWallpaperCardModel$Type[WallpaperCardModel.Type.EXTERNAL.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    private static WallpaperCardModel loadWallpaperCardPreference(Context context) {
        WallpaperCardModel.Type type;
        Uri uri;
        SharedPreferences sharedPreferences = context.getSharedPreferences(CURRENT_CARD_PREF, 0);
        String string = sharedPreferences.getString("name", null);
        String string2 = sharedPreferences.getString("path", null);
        if (string == null || string2 == null) {
            return null;
        }
        WallpaperCardModel.Type type2 = WallpaperCardModel.Type.EXTERNAL;
        if (Objects.equals(sharedPreferences.getString("type", null), "INTERNAL")) {
            WallpaperCardModel.Type type3 = WallpaperCardModel.Type.INTERNAL;
            uri = Uri.parse("file:///android_asset/" + string2);
            type = type3;
        } else {
            uri = Uri.parse(string2);
            type = type2;
        }
        return new WallpaperCardModel(string, string2, uri, type, null);
    }

    private static void initCards(Context context) {
        Bitmap bitmap;
        cards = new ArrayList();
        try {
            bitmap = BitmapFactory.decodeStream(context.getAssets().open(INTERNAL_WALLPAPER_IMAGE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
            bitmap = null;
        }
        cards.add(new WallpaperCardModel(context.getResources().getString(R.string.title_invalid), INTERNAL_WALLPAPER_VIDEO_PATH, Uri.parse("file:///android_asset/asset_1.mp4"), WallpaperCardModel.Type.INTERNAL, bitmap));
    }
}
