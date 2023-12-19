package com.demo.livwllpaper.activities;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.demo.livwllpaper.AdAdmob;
import com.demo.livwllpaper.R;
import com.demo.livwllpaper.Appraterclass;
import com.demo.livwllpaper.wallpapermodule.LPMLWApplication;
import com.demo.livwllpaper.wallpapermodule.LPMLWWallpaperService;
import com.demo.livwllpaper.wallpapermodule.WallpaperCardModel;

import java.io.IOException;
import java.util.ArrayList;


public class DisplayWallpaperactivity extends AppCompatActivity {
    public static Context context;
    public static int curr_pos;
    public int PREVIEW_REQUEST_CODE = 100;
    String TAG = "Full_ads";
    public String asset_path;
    LinearLayout back;
    TextView cv_set_wallpaper_system;
    public String final_path;
    public String image_path;
    public CardView ll_view_wallpapers;
    VideoView video_view_wallpapers;
    public String videopath;
    public ArrayList<String> wallaper_video_list;
    public ArrayList<String> wallpapers_list;


    @Override
    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        context = getApplicationContext();
        setContentView(R.layout.activity_display_systemwallpapers);


        AdAdmob adAdmob = new AdAdmob( this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.banner), this);
        adAdmob.FullscreenAd(this);


        this.back = (LinearLayout) findViewById(R.id.back);
        this.video_view_wallpapers = (VideoView) findViewById(R.id.video_view_wallpapers);
        this.ll_view_wallpapers = (CardView) findViewById(R.id.ll_view_wallpapers_act);
        this.cv_set_wallpaper_system = (TextView) findViewById(R.id.cv_set_wallpaper_system);
        this.back.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                DisplayWallpaperactivity.this.onBackPressed();
            }
        });
        this.cv_set_wallpaper_system.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                Bitmap bitmap;
                try {
                    bitmap = BitmapFactory.decodeStream(DisplayWallpaperactivity.this.getAssets().open(DisplayWallpaperactivity.this.image_path));
                } catch (IOException e) {
                    e.printStackTrace();
                    bitmap = null;
                }
                String str = DisplayWallpaperactivity.this.final_path;
                String str2 = DisplayWallpaperactivity.this.videopath;
                WallpaperCardModel lPMLWWallpaperCardModel = new WallpaperCardModel(str, str2, Uri.parse("file:///android_asset/" + DisplayWallpaperactivity.this.videopath), WallpaperCardModel.Type.INTERNAL, bitmap);
                WallpaperManager.getInstance(DisplayWallpaperactivity.this).getWallpaperInfo();
                LPMLWApplication.setPreviewLPMLWWallpaperCardModel(lPMLWWallpaperCardModel);
                Intent intent = new Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER");
                intent.putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT", new ComponentName(DisplayWallpaperactivity.this, LPMLWWallpaperService.class));
                DisplayWallpaperactivity DisplayWallpaperactivity = DisplayWallpaperactivity.this;
                DisplayWallpaperactivity.startActivityForResult(intent, DisplayWallpaperactivity.PREVIEW_REQUEST_CODE);
            }
        });
        this.video_view_wallpapers.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { 
            @Override 
            public void onCompletion(MediaPlayer mediaPlayer) {
                DisplayWallpaperactivity.this.video_view_wallpapers.start();
            }
        });
    }

    @Override 
    protected void onResume() {

        try {
            this.wallpapers_list = getIntent().getStringArrayListExtra("list_images");
            this.asset_path = getIntent().getStringExtra("asset_path");
            this.wallaper_video_list = getIntent().getStringArrayListExtra("wallaper_video_list");
            int intExtra = getIntent().getIntExtra("current_pos", 0);
            curr_pos = intExtra;
            String str = this.wallaper_video_list.get(intExtra);
            this.videopath = str;
            this.final_path = str.substring(str.lastIndexOf("/") + 1);
            this.image_path = this.wallpapers_list.get(curr_pos + 1);
            Log.e(TAG, "onResume videopath : " + videopath);
            Log.e(TAG, "onResume final_path: " + final_path);
            Log.e(TAG, "onResume image_path: " + image_path);
            Log.e(TAG, "onResume asset_path: " + asset_path);

            Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/" + Appraterclass.removeExtension(final_path));
            video_view_wallpapers.setVideoURI(video);
            this.ll_view_wallpapers.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { 
                @Override 
                public void onGlobalLayout() {
                    if (DisplayWallpaperactivity.this.ll_view_wallpapers.getMeasuredWidth() > 0) {
                        DisplayWallpaperactivity.this.ll_view_wallpapers.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        DisplayWallpaperactivity.this.video_view_wallpapers.setLayoutParams(new FrameLayout.LayoutParams(DisplayWallpaperactivity.this.ll_view_wallpapers.getMeasuredWidth(), DisplayWallpaperactivity.this.ll_view_wallpapers.getMeasuredHeight()));
                        DisplayWallpaperactivity.this.video_view_wallpapers.start();
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "onResume: " + e);
        }

        super.onResume();
    }

    @Override 
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == this.PREVIEW_REQUEST_CODE) {
            if (i2 == -1) {
                Toast.makeText(this, getResources().getString(R.string.wallpaper_applied_mssg), Toast.LENGTH_SHORT).show();
                LPMLWApplication.setCurrentWallpaperCard(this, LPMLWApplication.getPreviewLPMLWWallpaperCardModel());
            }
            LPMLWApplication.setPreviewLPMLWWallpaperCardModel(null);
        }
    }

    
    public static class LibraryObject {
        private int mRes;
        private String mTitle;

        public LibraryObject(int i, String str) {
            this.mRes = i;
            this.mTitle = str;
        }

        public String getTitle() {
            return this.mTitle;
        }

        public void setTitle(String str) {
            this.mTitle = str;
        }

        public int getRes() {
            return this.mRes;
        }

        public void setRes(int i) {
            this.mRes = i;
        }
    }

    @Override 
    public void onBackPressed() {
        super.onBackPressed();
    }
}