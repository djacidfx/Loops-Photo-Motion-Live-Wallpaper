package com.demo.livwllpaper.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.demo.livwllpaper.AdAdmob;
import com.demo.livwllpaper.R;
import com.demo.livwllpaper.controllers.LPMLWVideoSaver;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.demo.livwllpaper.Interfaces.OnRateusClicked;
import com.demo.livwllpaper.Appraterclass;
import com.demo.livwllpaper.Utilsx.Utils;
import com.demo.livwllpaper.beans.Project;
import com.demo.livwllpaper.controllers.ToolsController;
import com.demo.livwllpaper.databases.DatabaseHandler;
import com.demo.livwllpaper.wallpapermodule.LPMLWApplication;
import com.demo.livwllpaper.wallpapermodule.LPMLWWallpaperService;
import com.demo.livwllpaper.wallpapermodule.WallpaperCardModel;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class PreviewActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public static final int PREVIEW_REQUEST_CODE = 7;
    private ToolsController LPMLWToolsController;
    LinearLayout cv_edit_preview;
    TextView cv_set_wallpaper;
    private DatabaseHandler db;
    Project db_project;
    String from_activity;
    long id;
    ImageView iv;
    LinearLayout iv_delete_preview;
    LinearLayout iv_share_all_preview;
    LinearLayout ll_back_btn;
    LinearLayout ll_loading_albums;
    LinearLayout ll_main;
    LinearLayout ll_main_preview;
    LinearLayout ll_menu_view;
    CardView ll_view;
    LinearLayout ll_without_permission;
    public ServiceConnection mConnection;
    String path;
    ImageView play_pause_btn;
    int position;
    public SharedPreferences prefs;
    File rootfile;
    Snackbar snackbar;
    VideoView video_view_preview;
    List<Project> list = new ArrayList();
    private int RC_STORAGE_PERM = 101;
    public boolean is_paused = false;
    public boolean is_rate_us_clicked = false;
    String TAG = "Full_ads";
    OnRateusClicked rateus_dialog_clicked = new OnRateusClicked() { 
        @Override 
        public void on_rate_clicked() {
            if (PreviewActivity.this.from_activity.equalsIgnoreCase("from_hm")) {
                PreviewActivity.this.startActivity(new Intent(PreviewActivity.this, HomeActivity.class));
                PreviewActivity.this.finish();
                PreviewActivity.this.is_paused = false;
                return;
            }
            PreviewActivity.this.startActivity(new Intent(PreviewActivity.this, CreationActivity.class));
            PreviewActivity.this.finish();
            PreviewActivity.this.is_paused = false;
        }
    };

    
    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_preview);

        AdAdmob adAdmob = new AdAdmob( this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.banner), this);
        adAdmob.FullscreenAd(this);


        this.ll_view = (CardView) findViewById(R.id.ll_view);
        this.ll_menu_view = (LinearLayout) findViewById(R.id.ll_menu_view);
        this.video_view_preview = (VideoView) findViewById(R.id.video_view_preview);
        this.play_pause_btn = (ImageView) findViewById(R.id.play_pause_btn);
        this.ll_back_btn = (LinearLayout) findViewById(R.id.ll_back_btn);
        this.iv_share_all_preview = (LinearLayout) findViewById(R.id.iv_share_all_preview);
        this.iv_delete_preview = (LinearLayout) findViewById(R.id.iv_delete_preview);
        this.ll_main_preview = (LinearLayout) findViewById(R.id.ll_main_preview);
        this.ll_without_permission = (LinearLayout) findViewById(R.id.ll_without_permission);
        this.ll_loading_albums = (LinearLayout) findViewById(R.id.ll_loading_albums);
        this.cv_edit_preview = (LinearLayout) findViewById(R.id.cv_edit_preview);
        this.cv_set_wallpaper = (TextView) findViewById(R.id.cv_set_wallpaper);
        this.iv = (ImageView) findViewById(R.id.iv);
        this.ll_main = (LinearLayout) findViewById(R.id.ll_main);
        this.db = new DatabaseHandler(this);
        this.from_activity = getIntent().getStringExtra("from_java_activity");
        this.video_view_preview.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
            }
        });
        this.play_pause_btn.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
            }
        });
        this.video_view_preview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { 
            @Override 
            public void onCompletion(MediaPlayer mediaPlayer) {
                PreviewActivity.this.video_view_preview.start();
            }
        });
        this.ll_back_btn.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                PreviewActivity.this.onBackPressed();
            }
        });
        this.iv_delete_preview.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                PreviewActivity lPMLWPreviewActivity = PreviewActivity.this;
                lPMLWPreviewActivity.show_deleteDialog(lPMLWPreviewActivity, lPMLWPreviewActivity.db_project, PreviewActivity.this.rootfile, PreviewActivity.this.position);
            }
        });
        this.iv_share_all_preview.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                PreviewActivity.this.share_video();
            }
        });
        this.cv_edit_preview.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                int id = (int) PreviewActivity.this.db_project.getId();
                Log.e("in_export", "project_again_edit->\nid : " + PreviewActivity.this.db_project.getId() + "\ntitle " + PreviewActivity.this.db_project.get_Title() + "\npath " + PreviewActivity.this.db_project.getUri() + "\nbitmap " + PreviewActivity.this.db_project.getImagem());
                Intent intent = new Intent(PreviewActivity.this, Exportactivity.class);
                intent.putExtra("idProject", id);
                intent.putExtra("From_Main_Activity", "no");
                PreviewActivity.this.startActivity(intent);
                PreviewActivity.this.finish();
            }
        });
        this.cv_set_wallpaper.setOnClickListener(new View.OnClickListener() { 
            public AlertDialog addDialog;

            @Override 
            public void onClick(View view) {
                PreviewActivity.this.set_wallpaper_from_app();
            }
        });
    }

    public void share_video() {
        if (this.video_view_preview.isPlaying()) {
            this.video_view_preview.pause();
        }
        Intent intent = new Intent("android.intent.action.SEND");
        Uri uriForFile = FileProvider.getUriForFile(this, PreviewActivity.this.getPackageName()+".provider", new File(this.rootfile.getPath()));
        intent.setType("video/*");
        intent.putExtra("android.intent.extra.STREAM", uriForFile);
        startActivity(Intent.createChooser(intent, "Share Video"));
    }

    @Override 
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 7) {
            if (i2 == -1) {
                LPMLWApplication.setCurrentWallpaperCard(this, LPMLWApplication.getPreviewLPMLWWallpaperCardModel());
                Toast.makeText(this, getResources().getString(R.string.wallpaper_applied_mssg),  Toast.LENGTH_SHORT).show();
            }
            LPMLWApplication.setPreviewLPMLWWallpaperCardModel(null);
        }
    }

    
    public class Load_Video_For_Editing extends AsyncTask<Void, Void, Project> {
        public Load_Video_For_Editing() {
        }

        @Override 
        public void onPreExecute() {
            PreviewActivity.this.ll_main_preview.setVisibility(View.GONE);
            PreviewActivity.this.ll_without_permission.setVisibility(View.GONE);
            PreviewActivity.this.ll_menu_view.setVisibility(View.GONE);
            PreviewActivity.this.ll_loading_albums.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        public Project doInBackground(Void... voidArr) {
            try {
                if (PreviewActivity.this.from_activity.equalsIgnoreCase("yes")) {
                    PreviewActivity lPMLWPreviewActivity = PreviewActivity.this;
                    lPMLWPreviewActivity.path = lPMLWPreviewActivity.getIntent().getStringExtra("video_path");
                    PreviewActivity lPMLWPreviewActivity2 = PreviewActivity.this;
                    lPMLWPreviewActivity2.db_project = (Project) lPMLWPreviewActivity2.getIntent().getExtras().getParcelable("current_project");
                    Log.e("in_export", "project_previewed_before->\nid : " + PreviewActivity.this.db_project.getId() + "\ntitle " + PreviewActivity.this.db_project.get_Title() + "\npath " + PreviewActivity.this.db_project.getUri() + "\nbitmap " + PreviewActivity.this.db_project.getImagem());
                } else if (PreviewActivity.this.from_activity.equalsIgnoreCase("no")) {
                    PreviewActivity lPMLWPreviewActivity3 = PreviewActivity.this;
                    lPMLWPreviewActivity3.path = lPMLWPreviewActivity3.getIntent().getStringExtra("video_path");
                    PreviewActivity lPMLWPreviewActivity4 = PreviewActivity.this;
                    lPMLWPreviewActivity4.list = (List) lPMLWPreviewActivity4.getIntent().getSerializableExtra("project_list");
                    PreviewActivity lPMLWPreviewActivity5 = PreviewActivity.this;
                    lPMLWPreviewActivity5.position = lPMLWPreviewActivity5.getIntent().getIntExtra("position", 0);
                    for (int i = 0; i < PreviewActivity.this.list.size(); i++) {
                        Project lPMLWProject = PreviewActivity.this.list.get(i);
                        PreviewActivity lPMLWPreviewActivity6 = PreviewActivity.this;
                        lPMLWProject.reloadBitmapUri(lPMLWPreviewActivity6, lPMLWPreviewActivity6.db);
                        String substring = PreviewActivity.this.path.substring(PreviewActivity.this.path.lastIndexOf("/") + 1);
                        if (lPMLWProject.get_Title().equalsIgnoreCase(substring.substring(0, substring.lastIndexOf(".")))) {
                            PreviewActivity lPMLWPreviewActivity7 = PreviewActivity.this;
                            lPMLWPreviewActivity7.db_project = lPMLWPreviewActivity7.list.get(i);

                        }
                    }
                } else if (PreviewActivity.this.from_activity.equalsIgnoreCase("from_hm")) {
                    PreviewActivity lPMLWPreviewActivity8 = PreviewActivity.this;
                    lPMLWPreviewActivity8.path = lPMLWPreviewActivity8.getIntent().getStringExtra("video_path");
                    PreviewActivity lPMLWPreviewActivity9 = PreviewActivity.this;
                    lPMLWPreviewActivity9.list = (List) lPMLWPreviewActivity9.getIntent().getSerializableExtra("project_list");
                    PreviewActivity lPMLWPreviewActivity10 = PreviewActivity.this;
                    lPMLWPreviewActivity10.position = lPMLWPreviewActivity10.getIntent().getIntExtra("position", 0);
                    for (int i2 = 0; i2 < PreviewActivity.this.list.size(); i2++) {
                        Project lPMLWProject2 = PreviewActivity.this.list.get(i2);
                        PreviewActivity lPMLWPreviewActivity11 = PreviewActivity.this;
                        lPMLWProject2.reloadBitmapUri(lPMLWPreviewActivity11, lPMLWPreviewActivity11.db);
                        String substring2 = PreviewActivity.this.path.substring(PreviewActivity.this.path.lastIndexOf("/") + 1);
                        if (lPMLWProject2.get_Title().equalsIgnoreCase(substring2.substring(0, substring2.lastIndexOf(".")))) {
                            PreviewActivity lPMLWPreviewActivity12 = PreviewActivity.this;
                            lPMLWPreviewActivity12.db_project = lPMLWPreviewActivity12.list.get(i2);
                        }
                    }
                }
                Log.e("in_export", "from_activity_preview  " + PreviewActivity.this.from_activity);
                Log.e("in_export", "project_previewed->\nid : " + PreviewActivity.this.db_project.getId() + "\ntitle " + PreviewActivity.this.db_project.get_Title() + "\npath " + PreviewActivity.this.db_project.getUri() + "\nbitmap " + PreviewActivity.this.db_project.getImagem());
                return PreviewActivity.this.db_project;
            } catch (Exception unused) {
                return null;
            }
        }

        public void onPostExecute(Project lPMLWProject) {
            PreviewActivity.this.ll_main_preview.setVisibility(View.VISIBLE);
            PreviewActivity.this.ll_menu_view.setVisibility(View.VISIBLE);
            PreviewActivity.this.ll_without_permission.setVisibility(View.GONE);
            PreviewActivity.this.ll_loading_albums.setVisibility(View.GONE);
            PreviewActivity.this.video_view_preview.setVideoURI(Uri.parse(PreviewActivity.this.path));
            PreviewActivity.this.ll_view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { 
                @Override 
                public void onGlobalLayout() {
                    if (PreviewActivity.this.ll_view.getMeasuredWidth() > 0) {
                        PreviewActivity.this.ll_view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        PreviewActivity.this.video_view_preview.setLayoutParams(new FrameLayout.LayoutParams(PreviewActivity.this.ll_view.getMeasuredWidth(), PreviewActivity.this.ll_view.getMeasuredHeight()));
                        PreviewActivity.this.video_view_preview.start();
                    }
                }
            });
            PreviewActivity.this.rootfile = new File(PreviewActivity.this.path);
            if (Utils.perform_Action != 0) {
                if (Utils.perform_Action == 1) {
                    PreviewActivity.this.set_wallpaper_from_app();
                } else if (Utils.perform_Action == 2) {
                    PreviewActivity.this.share_video();
                }
            }
        }
    }

    private void init_on_create() {
        new Load_Video_For_Editing().execute(new Void[0]);
    }

    public void set_wallpaper_from_app() {
        Bitmap createVideoThumbnailFromUri = Utils.createVideoThumbnailFromUri(this, Uri.parse(this.path));
        String str = this.path;
        String substring = str.substring(str.lastIndexOf("/") + 1);
        String str2 = this.path;


        Log.e(TAG, "1111 substring: " +substring);
        Log.e(TAG, "1111 str: " +str2);
        Log.e(TAG, "1111 str2: " +Uri.parse(str2).toString());


        WallpaperCardModel lPMLWWallpaperCardModel = new WallpaperCardModel(substring, str2,  Uri.parse(path), WallpaperCardModel.Type.EXTERNAL, createVideoThumbnailFromUri);
        WallpaperManager.getInstance(this).getWallpaperInfo();
        LPMLWApplication.setPreviewLPMLWWallpaperCardModel(lPMLWWallpaperCardModel);
        Intent intent = new Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER");
        intent.putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT", new ComponentName(this, LPMLWWallpaperService.class));
        startActivityForResult(intent, 7);
    }

    @Override 
    protected void onResume() {

        if (Build.VERSION.SDK_INT >= 33) {
            if (this.is_paused) {

                final Uri data = FileProvider.getUriForFile(PreviewActivity.this, PreviewActivity.this.getPackageName() + ".provider", new File(path));
                this.video_view_preview.setVideoURI(data);

                this.video_view_preview.start();
            } else {
                init_on_create();
            }
            Snackbar snackbar = this.snackbar;
            if (snackbar != null && snackbar.isShown()) {
                this.snackbar.dismiss();
            }

        } else if (hasStoragePermission("android.permission.WRITE_EXTERNAL_STORAGE") || hasStoragePermission("android.permission.READ_EXTERNAL_STORAGE")) {
            if (this.is_paused) {

                final Uri data = FileProvider.getUriForFile(PreviewActivity.this, PreviewActivity.this.getPackageName() + ".provider", new File(path));
                this.video_view_preview.setVideoURI(data);

                this.video_view_preview.start();
            } else {
                init_on_create();
            }
            Snackbar snackbar = this.snackbar;
            if (snackbar != null && snackbar.isShown()) {
                this.snackbar.dismiss();
            }
        } else {
            this.ll_without_permission.setVisibility(View.VISIBLE);
            this.ll_loading_albums.setVisibility(View.GONE);
            this.ll_menu_view.setVisibility(View.GONE);
            this.ll_main_preview.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT > 29) {
                requestmyPermission(this.RC_STORAGE_PERM, "android.permission.WRITE_EXTERNAL_STORAGE");
            } else {
                requestmyPermission(this.RC_STORAGE_PERM, "android.permission.WRITE_EXTERNAL_STORAGE");
            }
        }
        super.onResume();
    }

    @Override 
    protected void onPause() {
        this.is_paused = true;
        this.video_view_preview.pause();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PreviewActivity.this,HomeActivity.class));
        finish();
    }

    @SuppressLint("ResourceType")
    public void show_deleteDialog(Context context, final Project lPMLWProject, final File file, int i) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.getWindow().setLayout(-1, -2);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        TextView textView = (TextView) dialog.findViewById(R.id.txt2);
        ((TextView) dialog.findViewById(R.id.tv_title_delete)).setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                PreviewActivity.this.video_view_preview.pause();
                Log.e(TAG, "1:show_deleteDialog :"+lPMLWProject );
                Log.e(TAG, "2:show_deleteDialog :"+PreviewActivity.this.db);
                lPMLWProject.deleteProject(PreviewActivity.this.db);
                if (file.exists()) {
                    file.delete();
                }
                PreviewActivity.this.startActivity(new Intent(PreviewActivity.this, CreationActivity.class));
                PreviewActivity.this.finish();
                dialog.dismiss();
            }
        });
        ((LinearLayout) dialog.findViewById(R.id.btn_no)).setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                PreviewActivity.this.video_view_preview.start();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void delete_project_from_database(Project lPMLWProject, File file, int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete ?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() { 
            @Override 
            public void onClick(DialogInterface dialogInterface, int i2) {
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() { 
            @Override 
            public void onClick(DialogInterface dialogInterface, int i2) {
                dialogInterface.cancel();
                PreviewActivity.this.video_view_preview.start();
            }
        });
        AlertDialog create = builder.create();
        create.setCancelable(false);
        create.setCanceledOnTouchOutside(false);
        create.show();
    }

    private boolean hasStoragePermission(String... strArr) {
        return EasyPermissions.hasPermissions(this, strArr);
    }

    @Override 
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EasyPermissions.onRequestPermissionsResult(i, strArr, iArr, this);
    }

    @Override 
    public void onPermissionsGranted(int i, List<String> list) {
        init_on_create();
    }

    @Override 
    public void onPermissionsDenied(int i, List<String> list) {
        showSnackBar();
        if (EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    public void requestmyPermission(int i, String... strArr) {
        EasyPermissions.requestPermissions(this, "This needs permission to access", i, strArr);
    }

    public void showSnackBar() {
        Snackbar action = Snackbar.make(this.ll_without_permission, "No permission!", BaseTransientBottomBar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT > 29) {
                    PreviewActivity lPMLWPreviewActivity = PreviewActivity.this;
                    lPMLWPreviewActivity.requestmyPermission(lPMLWPreviewActivity.RC_STORAGE_PERM, "android.permission.READ_EXTERNAL_STORAGE");
                    return;
                }
                PreviewActivity lPMLWPreviewActivity2 = PreviewActivity.this;
                lPMLWPreviewActivity2.requestmyPermission(lPMLWPreviewActivity2.RC_STORAGE_PERM, "android.permission.WRITE_EXTERNAL_STORAGE");
            }
        });
        this.snackbar = action;
        action.setActionTextColor(-1);
        ((TextView) this.snackbar.getView().findViewById(R.id.snackbar_text)).setTextColor(-65536);
        this.snackbar.show();
    }
}
