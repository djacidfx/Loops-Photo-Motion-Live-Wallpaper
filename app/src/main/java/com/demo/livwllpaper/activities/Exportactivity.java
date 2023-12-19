package com.demo.livwllpaper.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.demo.livwllpaper.AdAdmob;
import com.demo.livwllpaper.R;
import com.demo.livwllpaper.Interfaces.Onvideodownloaded;
import com.demo.livwllpaper.Utilsx.StorageUtils;
import com.demo.livwllpaper.Utilsx.Utils;
import com.demo.livwllpaper.beans.Point;
import com.demo.livwllpaper.beans.Project;
import com.demo.livwllpaper.controllers.AnimationController;
import com.demo.livwllpaper.controllers.HistoryController;
import com.demo.livwllpaper.controllers.ToolsController;
import com.demo.livwllpaper.controllers.LPMLWVideoSaver;
import com.demo.livwllpaper.databases.DatabaseHandler;
import com.demo.livwllpaper.views.DynamicSeekBarView;
import com.demo.livwllpaper.views.WLupaImageView;
import com.demo.livwllpaper.views.ZoomImageView;

import java.io.File;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class Exportactivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public static AnimationController LPMLWAnimationController = null;
    public static HistoryController LPMLWHistoryController = null;
    public static LPMLWVideoSaver LPMLWVideoSaver = null;
    public static int MAX_RESOLUTION_SAVE = 1080;
    public static int MAX_RESOLUTION_SAVE_FREE = (600 + 1080) / 2;
    public static int MIN_RESOLUTION_SAVE = 600;
    public static final int PERMISSIONS_REQUEST_EXTERNAL_READ_AND_WRITE = 2;
    public static final int RESULT_LOAD_IMAGE = 1;
    public static LinearLayout cv_save_video;
    public static SharedPreferences.Editor editor;
    public static LinearLayout linearVertical;
    public static LinearLayout ll_main_save_screen;
    public static CardView ll_view;
    public static DisplayMetrics metrics;
    public static SharedPreferences prefs_eraser;
    public static RelativeLayout relative_layout_bottom_toolbar_functionalities;
    public WLupaImageView LPMLWLupaImageView;
    public ToolsController LPMLWToolsController;
    public float Original_resolution;
    public Menu Secondary_menu;
    LinearLayout btn_cancel;
    public Button btn_save_with_logo;
    public TextView btn_save_with_wallpaper;
    public TextView btn_save_without_logo;
    LinearLayout btn_share_with_download;
    public ImageView change_image_from_gallery;
    public ImageView close_preview;
    public Project current_LPMLW_project;
    public DatabaseHandler db;
    String from_main_activity;
    String from_my_creation;
    public ImageView image_Details_Tools;
    public Bitmap image_main_bitmap;
    public Bitmap imagemRepresentacao;
    public ZoomImageView imageview_zoom_image;
    public boolean is_zoom_tutorial_loaded;
    public ImageView iv_play_btn;
    public ImageView iv_preview;
    public Permission_Listener last_Request_Permission;
    public Bitmap maskCurrent_History;
    public Bitmap mask_Initial_History;
    public Menu menuPrincipal;
    public NumberFormat nf;
    RelativeLayout.LayoutParams params;
    public Point pontoInicial;
    public ImageView preview_video_speed_seekbar;
    TextView save_video;
    public DynamicSeekBarView seek_Resolution;
    public DynamicSeekBarView seek_Time;
    public int selected_eraser_option;
    public TextView tv_Resolution_Ideal_size;
    public TextView tv_Save_Time;
    public TextView tx_Resolution;
    public Dialog waitDialog;
    public float xInitMouse;
    public float yInitMouse;
    public boolean imageLoaded = false;
    public float mScaleFactor = 1.0f;
    public boolean mouseDragging = false;
    public List<Point> pontosSequencia = new CopyOnWriteArrayList();
    public boolean showDetalhes = false;
    DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this);
    public int RC_STORAGE_PERM = 101;
    ArrayList<Float> x_positioning_mask = new ArrayList<>();
    ArrayList<Float> y_positioning_mask = new ArrayList<>();
    public int tempoPreview = 10000;
    Onvideodownloaded LPMLWOnvideodownloaded = new Onvideodownloaded() {
        @Override
        public void on_downloaded(String str) {
            Intent intent = new Intent(Exportactivity.this, PreviewActivity.class);

            Log.e("in_export", "video_path : " + str);
            intent.putExtra("video_path", str);
            intent.putExtra("from_java_activity", "yes");
            intent.putExtra("current_project", Exportactivity.this.current_LPMLW_project);
            Exportactivity.this.startActivity(intent);
            Exportactivity.this.finish();
            Log.e("in_export", "project_exported->\nid : " + Exportactivity.this.current_LPMLW_project.getId() + "\ntitle " + Exportactivity.this.current_LPMLW_project.get_Title() + "\npath " + Exportactivity.this.current_LPMLW_project.getUri() + "\nbitmap " + Exportactivity.this.current_LPMLW_project.getImagem());
        }
    };


    public interface Permission_Listener {
        void onPermissionGranted();
    }

    public static boolean isInside(int i, int i2, int i3, int i4, int i5) {
        int i6 = i4 - i;
        int i7 = i5 - i2;
        return (i6 * i6) + (i7 * i7) <= i3 * i3;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void open_new_project() {
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(128);
        setContentView(R.layout.activity_java_modified);


        AdAdmob adAdmob = new AdAdmob( this);
        adAdmob.FullscreenAd(this);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", 0);
        prefs_eraser = sharedPreferences;
        editor = sharedPreferences.edit();
        this.from_main_activity = getIntent().getStringExtra("From_Main_Activity");
        this.from_my_creation = getIntent().getStringExtra("From_My_Creation");
        metrics = getResources().getDisplayMetrics();
        setSupportActionBar((Toolbar) findViewById(R.id.menuBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon_editscreen);
        cv_save_video = (LinearLayout) findViewById(R.id.cv_save_video);
        ll_view = (CardView) findViewById(R.id.ll_view);
        this.btn_cancel = (LinearLayout) findViewById(R.id.btn_cancel);
        linearVertical = (LinearLayout) findViewById(R.id.linearVertical);
        cv_save_video.setVisibility(View.GONE);
        linearVertical.setVisibility(View.VISIBLE);
        this.seek_Resolution = (DynamicSeekBarView) findViewById(R.id.seekbar_resolution_Save);
        DynamicSeekBarView lPMLWDynamicSeekBarView = (DynamicSeekBarView) findViewById(R.id.seekbar_save_duration);
        this.seek_Time = lPMLWDynamicSeekBarView;
        lPMLWDynamicSeekBarView.setMax(8000);
        this.tv_Save_Time = (TextView) findViewById(R.id.tv_Save_Time);
        this.tx_Resolution = (TextView) findViewById(R.id.tv_Resolution_Saved);
        this.btn_save_without_logo = (TextView) findViewById(R.id.btn_save_without_logo);
        this.btn_save_with_wallpaper = (TextView) findViewById(R.id.btn_save_with_wallpaper);
        this.btn_share_with_download = (LinearLayout) findViewById(R.id.btn_share_with_download);
        this.btn_save_with_logo = (Button) findViewById(R.id.btn_save_with_logo);
        this.tv_Resolution_Ideal_size = (TextView) findViewById(R.id.tv_Resolution_Ideal_size);
        this.imageview_zoom_image = (ZoomImageView) findViewById(R.id.imageview_zoom_image);
        this.change_image_from_gallery = (ImageView) findViewById(R.id.change_image_from_gallery);
        this.save_video = (TextView) findViewById(R.id.save_video);
        this.preview_video_speed_seekbar = (ImageView) findViewById(R.id.preview_video_speed_seekbar);
        this.close_preview = (ImageView) findViewById(R.id.close_preview);
        this.image_Details_Tools = (ImageView) findViewById(R.id.image_Details_Tools);
        this.iv_preview = (ImageView) findViewById(R.id.iv_preview);
        this.iv_play_btn = (ImageView) findViewById(R.id.iv_play_btn);
        this.image_Details_Tools.setVisibility(View.GONE);
        this.db = new DatabaseHandler(this);
        this.db = DatabaseHandler.getInstance(this);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout_bottom_toolbar_functionalities);
        relative_layout_bottom_toolbar_functionalities = relativeLayout;
        relativeLayout.setVisibility(View.GONE);
        WLupaImageView lPMLWLupaImageView = (WLupaImageView) findViewById(R.id.imageZoom);
        this.LPMLWLupaImageView = lPMLWLupaImageView;
        lPMLWLupaImageView.setVisibility(View.GONE);
        this.imageview_zoom_image.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        LPMLWHistoryController = LPMLWHistoryController.getInstance();
        ToolsController init = LPMLWToolsController.init(this);
        this.LPMLWToolsController = init;
        init.setToolsListener(new tools_controller_listener_after_initialising());
        this.imageview_zoom_image.setScaleListener(new zoom_image_scaling_listener());
        this.imageview_zoom_image.setOnTouchListener(new zoom_image_touch_listener());
        if (this.LPMLWToolsController.get_Tool_type() == 7) {
            this.imageview_zoom_image.set_Zoom_On(true);
        } else {
            this.imageview_zoom_image.set_Zoom_On(false);
        }
        if (this.image_Details_Tools.getVisibility() == View.VISIBLE) {
            this.save_video.setVisibility(View.GONE);
            this.preview_video_speed_seekbar.setVisibility(View.GONE);
        }
        if (this.from_main_activity.equals("yes")) {
            if (bundle == null) {
                loadImagem(Uri.parse(getIntent().getStringExtra(Project.COLUMN_URI)));
            }
        } else if (this.from_main_activity.equals("no")) {
            int intExtra = getIntent().getIntExtra("idProject", 0);
            Log.e("in_export", "id_open " + intExtra);
            loadProject(this.db.getProject((long) getIntent().getIntExtra("idProject", 0)));
        }

        if (Build.VERSION.SDK_INT >= 33) {
            this.LPMLWToolsController.sequence_tool_selected_by_default();
            boolean z = prefs_eraser.getBoolean("is_zoom_tutorial_showed", false);
            this.is_zoom_tutorial_loaded = z;
            if (!z) {
                show_zoom_tutorial(this);
            }
        } else if (hasStoragePermission("android.permission.WRITE_EXTERNAL_STORAGE") || hasStoragePermission("android.permission.READ_EXTERNAL_STORAGE")) {
            this.LPMLWToolsController.sequence_tool_selected_by_default();
            boolean z = prefs_eraser.getBoolean("is_zoom_tutorial_showed", false);
            this.is_zoom_tutorial_loaded = z;
            if (!z) {
                show_zoom_tutorial(this);
            }
        } else if (Build.VERSION.SDK_INT > 29) {
            requestmyPermission(this.RC_STORAGE_PERM, "android.permission.WRITE_EXTERNAL_STORAGE");
        } else {
            requestmyPermission(this.RC_STORAGE_PERM, "android.permission.WRITE_EXTERNAL_STORAGE");
        }
        this.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Exportactivity.cv_save_video.setVisibility(View.GONE);
                if (Exportactivity.this.LPMLWToolsController.get_Tool_type() == 0) {
                    Exportactivity.this.LPMLWToolsController.set_relative_layout_brush_size_visibility(false);
                } else {
                    Exportactivity.this.LPMLWToolsController.set_relative_layout_brush_size_visibility(true);
                }
                Exportactivity.this.imageview_zoom_image.setEnabled(true);
                Exportactivity.this.imageview_zoom_image.setClickable(true);
                Exportactivity.this.preview_video_speed_seekbar.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                Exportactivity.this.LPMLWToolsController.closePreview();
            }
        });
        this.save_video.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                LPMLWVideoSaver lPMLWVideoSaver = Exportactivity.LPMLWVideoSaver;
                if (LPMLWVideoSaver.dialog != null) {
                    LPMLWVideoSaver lPMLWVideoSaver2 = Exportactivity.LPMLWVideoSaver;
                }
                if (Exportactivity.cv_save_video.getVisibility() != View.VISIBLE) {
                    if (Exportactivity.this.from_main_activity.equals("yes")) {
                        if (Exportactivity.LPMLWAnimationController.get_All_Points_List().size() == 0) {
                            Exportactivity lPMLWExportactivity = Exportactivity.this;
                            lPMLWExportactivity.show_empty_dialog(lPMLWExportactivity);
                            return;
                        } else if (Utils.is_arrow_movement) {
                            Exportactivity.this.save_file_gallery();
                            return;
                        } else {
                            Exportactivity lPMLWExportactivity2 = Exportactivity.this;
                            lPMLWExportactivity2.show_sequence_text(lPMLWExportactivity2);
                            return;
                        }
                    } else if (Exportactivity.this.from_main_activity.equals("no")) {
                        List<Point> list = Exportactivity.LPMLWAnimationController.get_All_Points_List();
                        if (list.size() == 0 || list.isEmpty()) {
                            Exportactivity lPMLWExportactivity3 = Exportactivity.this;
                            lPMLWExportactivity3.show_empty_dialog(lPMLWExportactivity3);
                            return;
                        } else if (Utils.is_arrow_movement) {
                            try {
                                Exportactivity.this.save_file_gallery();
                                return;
                            } catch (Exception unused) {
                                Exportactivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                    }
                                });
                                return;
                            }
                        } else {
                            Exportactivity lPMLWExportactivity4 = Exportactivity.this;
                            lPMLWExportactivity4.show_save_dialog(lPMLWExportactivity4);
                            return;
                        }
                    } else {
                        return;
                    }
                }
                Exportactivity.this.imageview_zoom_image.setEnabled(false);
                Exportactivity.this.imageview_zoom_image.setClickable(false);
            }
        });
        this.close_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Exportactivity.this.LPMLWToolsController.isPlayingPreview()) {
                        Exportactivity.this.LPMLWToolsController.closePreview();
                        Exportactivity.this.preview_video_speed_seekbar.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    }
                } catch (Exception unused) {
                    Exportactivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }
            }
        });
        this.preview_video_speed_seekbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Exportactivity.this.preview_video_functionality();
            }
        });
        this.change_image_from_gallery.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                LPMLWVideoSaver lPMLWVideoSaver = Exportactivity.LPMLWVideoSaver;
                if (LPMLWVideoSaver.dialog != null) {
                    LPMLWVideoSaver lPMLWVideoSaver2 = Exportactivity.LPMLWVideoSaver;
                }
                if (Exportactivity.cv_save_video.getVisibility() != View.VISIBLE) {
                    Exportactivity.this.change_image_from_gallery.setEnabled(false);
                    Exportactivity.this.open_new_project();
                    return;
                }
                Exportactivity.this.imageview_zoom_image.setEnabled(false);
                Exportactivity.this.imageview_zoom_image.setClickable(false);
            }
        });
    }

    public void preview_video_functionality() {
        if (LPMLWVideoSaver.dialog == null || !LPMLWVideoSaver.dialog.isShowing()) {
            try {
                if (this.LPMLWToolsController.isPlayingPreview()) {
                    this.LPMLWToolsController.closePreview();
                    this.preview_video_speed_seekbar.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                } else {
                    try {
                        this.LPMLWToolsController.playPreview();
                        this.preview_video_speed_seekbar.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    } catch (Exception unused) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }
                }
            } catch (Exception unused2) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
            return;
        }
        this.imageview_zoom_image.setEnabled(false);
        this.imageview_zoom_image.setClickable(false);
    }

    public void preview_only_video() {
        try {
            if (this.LPMLWToolsController.isPlayingPreview()) {
                this.LPMLWToolsController.closePreview();
                this.iv_play_btn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            } else {
                try {
                    this.LPMLWToolsController.playPreview();
                    this.iv_play_btn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                } catch (Exception unused) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }
            }
        } catch (Exception unused2) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                }
            });
        }
    }

    public void save_file_gallery() {
        LPMLWVideoSaver lPMLWVideoSaver;
        this.seek_Time.setSeekBarChangeListener(new duration_click_listener());
        NumberFormat instance = NumberFormat.getInstance();
        this.nf = instance;
        instance.setMaximumFractionDigits(1);
        int i = this.current_LPMLW_project.get_Time_Save();
        this.seek_Time.setProgress(1);
        this.seek_Time.setProgress(2);
        this.seek_Time.setProgress(10000 - i);
        this.seek_Resolution.setMax(MAX_RESOLUTION_SAVE - MIN_RESOLUTION_SAVE);
        this.seek_Resolution.setSeekBarChangeListener(new seekbar_resolution_change_listener());
        float min = (float) Math.min(Math.max(this.current_LPMLW_project.getHeight(), this.current_LPMLW_project.getWidth()), MAX_RESOLUTION_SAVE);
        this.Original_resolution = min;
        if (min % 2.0f != 0.0f) {
            min += 1.0f;
        }
        this.Original_resolution = min;
        int i2 = this.current_LPMLW_project.get_Save_Resolution();
        if (i2 > MAX_RESOLUTION_SAVE_FREE) {
            this.seek_Resolution.setProgress(1);
            this.seek_Resolution.setProgress(2);
            this.seek_Resolution.setProgress(i2 - MIN_RESOLUTION_SAVE);
            this.tv_Resolution_Ideal_size.setOnClickListener(new resolution_on_click_listener());
            this.btn_save_without_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.perform_Action = 0;
                    Exportactivity.this.Save_video_in_gallery(false);
                }
            });
            this.btn_save_with_wallpaper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.perform_Action = 1;
                    Exportactivity.this.Save_video_in_gallery(false);
                }
            });
            this.btn_share_with_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.perform_Action = 2;
                    Exportactivity.this.Save_video_in_gallery(false);
                }
            });
            this.btn_save_with_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Exportactivity.this.Save_video_in_gallery(true);
                }
            });
            setFinishOnTouchOutside(true);
            lPMLWVideoSaver = (LPMLWVideoSaver) getLastCustomNonConfigurationInstance();
        } else {
            this.seek_Resolution.setProgress(1);
            this.seek_Resolution.setProgress(2);
            this.seek_Resolution.setProgress(i2 - MIN_RESOLUTION_SAVE);
            this.tv_Resolution_Ideal_size.setOnClickListener(new resolution_on_click_listener());
            this.btn_save_without_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.perform_Action = 0;
                    Exportactivity.this.Save_video_in_gallery(false);
                }
            });
            this.btn_save_with_wallpaper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.perform_Action = 1;
                    Exportactivity.this.Save_video_in_gallery(false);
                }
            });
            this.btn_share_with_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.perform_Action = 2;
                    Exportactivity.this.Save_video_in_gallery(false);
                }
            });
            this.btn_save_with_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Exportactivity.this.Save_video_in_gallery(true);
                }
            });
            setFinishOnTouchOutside(true);
            lPMLWVideoSaver = (LPMLWVideoSaver) getLastCustomNonConfigurationInstance();
        }
        this.iv_play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Exportactivity.this.preview_only_video();
            }
        });
        if (lPMLWVideoSaver != null) {
            LPMLWVideoSaver = lPMLWVideoSaver;
            lPMLWVideoSaver.setContext(this);
            if (LPMLWVideoSaver.is_Saving()) {
                setFinishOnTouchOutside(false);
                LPMLWVideoSaver.showProgress();
            }
        }
        this.LPMLWToolsController.delete_selection();
        this.LPMLWToolsController.closePreview();
        this.imageview_zoom_image.restartZoom();
        cv_save_video.setVisibility(View.VISIBLE);
        Glide.with((FragmentActivity) this).load(this.image_main_bitmap).into(this.iv_preview);
        ll_view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Exportactivity.ll_view.getMeasuredWidth() > 0) {
                    Exportactivity.ll_view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    Exportactivity.this.iv_preview.setLayoutParams(new FrameLayout.LayoutParams(Exportactivity.ll_view.getMeasuredWidth(), Exportactivity.ll_view.getMeasuredHeight()));
                }
            }
        });
        this.imageview_zoom_image.setEnabled(false);
        this.imageview_zoom_image.setClickable(false);
        Project project = this.databaseHandler.getProject(this.current_LPMLW_project.getId());
        this.current_LPMLW_project = project;
        project.reloadBitmapUri(this, this.databaseHandler);
        LPMLWToolsController.relative_layout_speed_settings.setVisibility(View.GONE);
        this.LPMLWToolsController.closePreview();
        this.iv_play_btn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
    }


    public class duration_click_listener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }


        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            if (!z) {
                Exportactivity.this.update_duration_seekbar(seekBar);
            }
            Log.e("in_export", "from_user_duration " + z);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Exportactivity.this.update_duration_seekbar(seekBar);
        }
    }

    public void update_duration_seekbar(SeekBar seekBar) {
        this.current_LPMLW_project.set_Time_Save(10000 - Math.round((((float) seekBar.getProgress()) / ((float) seekBar.getMax())) * 8000.0f));
        int i = (int) (((float) this.current_LPMLW_project.get_Time_Save()) / 1000.0f);
        DynamicSeekBarView lPMLWDynamicSeekBarView = this.seek_Time;
        lPMLWDynamicSeekBarView.setInfoText(i + " seconds", seekBar.getProgress());
        TextView textView = this.tv_Save_Time;
        textView.setText(String.valueOf(i) + " " + getResources().getString(R.string.text_save_time_seconds));
        this.tempoPreview = 10000 - Math.round((((float) seekBar.getProgress()) / ((float) seekBar.getMax())) * 8000.0f);
        NumberFormat instance = NumberFormat.getInstance();
        instance.setMaximumFractionDigits(1);
        Bitmap createBitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(1);
        paint.setFilterBitmap(true);
        paint.setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        paint.setStyle(Paint.Style.FILL);
        float width = (float) (createBitmap.getWidth() / 2);
        float height = (float) (createBitmap.getHeight() / 2);
        canvas.drawCircle(width, height, (float) ((canvas.getHeight() / 2) - 2), paint);
        paint.setColor(-1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4.0f);
        canvas.drawCircle(width, height, (float) ((canvas.getHeight() / 2) - 2), paint);
        Paint paint2 = new Paint(1);
        paint2.setColor(-1);
        paint2.setTextAlign(Paint.Align.CENTER);
        paint2.setTextSize(60.0f);
        paint2.setFakeBoldText(true);
        canvas.drawText(instance.format((double) (((float) this.tempoPreview) / 1000.0f)) + "s", (float) Math.round(((float) canvas.getWidth()) / 2.0f), (float) Math.round((((float) canvas.getHeight()) / 2.0f) - ((paint2.descent() + paint2.ascent()) / 2.0f)), paint2);
        this.LPMLWToolsController.set_speed_for_preview(this.tempoPreview);
        this.LPMLWToolsController.updated_speed_for_preview(this.tempoPreview);
        this.current_LPMLW_project.updateProject(this.databaseHandler);
    }


    public class seekbar_resolution_change_listener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }


        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            if (!z) {
                Exportactivity.this.update_resolution_seekbar(seekBar);
            }
            Log.e("in_export", "from_user " + z);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Exportactivity.this.update_resolution_seekbar(seekBar);
        }
    }

    public void update_resolution_seekbar(SeekBar seekBar) {
        String str;
        int round = SaveActivity.MIN_RESOLUTION_SAVE + Math.round((((float) seekBar.getProgress()) / ((float) seekBar.getMax())) * ((float) (SaveActivity.MAX_RESOLUTION_SAVE - SaveActivity.MIN_RESOLUTION_SAVE)));
        if (round % 2 != 0) {
            round++;
        }
        if (this.current_LPMLW_project.getWidth() > this.current_LPMLW_project.getHeight()) {
            str = round + " x " + Math.round(((float) round) * (((float) this.current_LPMLW_project.getHeight()) / ((float) this.current_LPMLW_project.getWidth())));
        } else {
            str = Math.round(((float) round) * (((float) this.current_LPMLW_project.getWidth()) / ((float) this.current_LPMLW_project.getHeight()))) + "  x  " + round;
        }
        this.seek_Resolution.setInfoText(str + "", seekBar.getProgress());
        this.tx_Resolution.setText(str);
        paint_Ideal_Text(round);
        this.current_LPMLW_project.set_Resolution_Save(round);
        this.current_LPMLW_project.updateProject(this.databaseHandler);
    }


    public class resolution_on_click_listener implements View.OnClickListener {


        @Override
        public void onClick(View view) {
            Exportactivity.this.seek_Resolution.setProgress(((int) Exportactivity.this.Original_resolution) - Exportactivity.MIN_RESOLUTION_SAVE);
        }
    }

    public void paint_Ideal_Text(int i) {
        TextView textView = (TextView) findViewById(R.id.tv_Resolution_Ideal_size);
        if (i == ((int) this.Original_resolution)) {
            textView.setTextColor(Utils.getColor(this, R.color.colorPrimary));
        } else {
            textView.setTextColor(Utils.getColor(this, R.color.colorPrimary));
        }
    }

    public String get_old_path() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + getResources().getString(R.string.project_folder);
    }

    public void Save_video_in_gallery(boolean z) {
        if (this.LPMLWToolsController.isPlayingPreview()) {
            this.LPMLWToolsController.closePreview();
            this.iv_play_btn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }
        setFinishOnTouchOutside(false);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        LPMLWVideoSaver lPMLWVideoSaver = new LPMLWVideoSaver(this, this.current_LPMLW_project, this.LPMLWOnvideodownloaded);
        LPMLWVideoSaver = lPMLWVideoSaver;
        lPMLWVideoSaver.set_Time_Animation(this.current_LPMLW_project.get_Time_Save());
        LPMLWVideoSaver.set_Resolution(this.current_LPMLW_project.get_Save_Resolution());
        LPMLWVideoSaver.set_with_Logo(z);
        getResources().getString(R.string.videos_folder);
        File file = new File(get_old_path());
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(file.getAbsolutePath(), this.current_LPMLW_project.get_Title() + ".mp4");
        LPMLWVideoSaver.setSaveListener(new save_video_listener());
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Utils.width = displayMetrics.widthPixels;
        Utils.height = displayMetrics.heightPixels;
        if (Utils.width % 2 != 0) {
            Utils.width--;
        }
        if (Utils.height % 2 != 0) {
            Utils.height--;
        }
        Bitmap bitmap = getBitmap(R.drawable.gallery_selection);
        if (bitmap != null) {
            LPMLWVideoSaver.execute(file2.getPath(), bitmap);
        }
    }

    public Bitmap getBitmap(int i) {
        Drawable drawable = getResources().getDrawable(i);
        Canvas canvas = new Canvas();
        Bitmap createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(createBitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return createBitmap;
    }


    public class save_video_listener implements LPMLWVideoSaver.VideoSaveListener {
        @Override
        public void onSaving(int i) {
        }

        @Override
        public void onStartSave(int i) {
        }


        @Override
        public void onSaved(File file) {
            Exportactivity.LPMLWVideoSaver = null;
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("video/*");
            intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(file));
            Exportactivity lPMLWExportactivity = Exportactivity.this;
            lPMLWExportactivity.startActivity(Intent.createChooser(intent, lPMLWExportactivity.getResources().getString(R.string.Share_video)));
            Exportactivity.this.setFinishOnTouchOutside(true);
            Exportactivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            Utils.scannFile(Exportactivity.this, file);
        }

        @Override
        public void onError(String str) {
            Exportactivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Exportactivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Exportactivity.relative_layout_bottom_toolbar_functionalities.setVisibility(View.VISIBLE);
                }
            });
            Exportactivity.this.setFinishOnTouchOutside(true);
            Exportactivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }


    class select_image_from_gallery_or_camera_listener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (!Exportactivity.this.imageLoaded) {
                Exportactivity.this.open_new_project();
            }
        }
    }


    class zoom_image_scaling_listener extends ScaleGestureDetector.SimpleOnScaleGestureListener {


        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            Exportactivity.this.loadImage_Representation();
            return super.onScale(scaleGestureDetector);
        }
    }


    class zoom_image_touch_listener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (Exportactivity.this.imageLoaded) {
                Rect bounds = Exportactivity.this.imageview_zoom_image.getDrawable().getBounds();
                int width = (Exportactivity.this.imageview_zoom_image.getWidth() - bounds.right) / 2;
                int height = (Exportactivity.this.imageview_zoom_image.getHeight() - bounds.bottom) / 2;
                if (!Exportactivity.this.LPMLWToolsController.isPlayingPreview()) {
                    int actionIndex = motionEvent.getActionIndex();
                    float[] fArr = {motionEvent.getX(actionIndex), motionEvent.getY(actionIndex)};
                    Matrix matrix = new Matrix();
                    Exportactivity.this.imageview_zoom_image.getImageMatrix().invert(matrix);
                    matrix.postTranslate((float) Exportactivity.this.imageview_zoom_image.getScrollX(), (float) Exportactivity.this.imageview_zoom_image.getScrollY());
                    matrix.mapPoints(fArr);
                    float f = fArr[0];
                    float f2 = fArr[1];
                    Point lPMLWPoint = new Point(f, f2, true);
                    Exportactivity.this.LPMLWToolsController.setSubToolVelocidadeVisibility(false);
                    Exportactivity.this.LPMLWToolsController.set_eraseroptions_visibility(false);
                    Paint paint = new Paint(1);
                    paint.setStyle(Paint.Style.FILL);
                    paint.setFilterBitmap(true);
                    paint.setColor(-65536);
                    int action = motionEvent.getAction();
                    int pointerCount = motionEvent.getPointerCount();
                    Exportactivity.this.selected_eraser_option = Exportactivity.prefs_eraser.getInt("selected_eraser_option", 2);
                    if (action == 0) {
                        if (pointerCount == 1) {
                            Exportactivity.this.imageview_zoom_image.set_Zoom_On(false);
                            Exportactivity.this.mouseDragging = true;
                            Exportactivity.this.xInitMouse = f;
                            Exportactivity.this.yInitMouse = f2;
                            Exportactivity.this.pontoInicial = new Point(Exportactivity.this.xInitMouse, Exportactivity.this.yInitMouse, true);
                            switch (Exportactivity.this.LPMLWToolsController.get_Tool_type()) {
                                case 1:
                                    Exportactivity.this.pontoInicial = new Point(Exportactivity.this.xInitMouse, Exportactivity.this.yInitMouse, false);
                                    if (!Exportactivity.LPMLWAnimationController.is_there_Point(Exportactivity.this.pontoInicial)) {
                                        Exportactivity.this.current_LPMLW_project.add_point(Exportactivity.this.db, Exportactivity.this.pontoInicial);
                                        Exportactivity.LPMLWAnimationController.add_point(Exportactivity.this.pontoInicial);
                                        break;
                                    }
                                    break;
                                case 2:
                                    Exportactivity.LPMLWAnimationController.add_point(lPMLWPoint);
                                    Exportactivity.this.current_LPMLW_project.add_point(Exportactivity.this.db, lPMLWPoint);
                                    Exportactivity.LPMLWHistoryController.add_history(lPMLWPoint, true);
                                    Exportactivity lPMLWExportactivity = Exportactivity.this;
                                    lPMLWExportactivity.reposition_magnifying_glass(lPMLWExportactivity.xInitMouse, Exportactivity.this.yInitMouse);
                                    Exportactivity.this.loadImage_Representation();
                                    Exportactivity.this.LPMLWLupaImageView.set_Position_Magnifying_glass(f, f2);
                                    Exportactivity.this.LPMLWLupaImageView.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    Exportactivity.this.reposition_magnifying_glass(f, f2);
                                    Exportactivity.this.LPMLWToolsController.set_Masking(true);
                                    Exportactivity lPMLWExportactivity2 = Exportactivity.this;
                                    ToolsController lPMLWToolsController = lPMLWExportactivity2.LPMLWToolsController;
                                    lPMLWExportactivity2.mask_Initial_History = LPMLWToolsController.getMask();
                                    if (Exportactivity.this.mask_Initial_History != null) {
                                        Exportactivity lPMLWExportactivity3 = Exportactivity.this;
                                        lPMLWExportactivity3.mask_Initial_History = lPMLWExportactivity3.mask_Initial_History.copy(Bitmap.Config.ARGB_8888, true);
                                        Exportactivity lPMLWExportactivity4 = Exportactivity.this;
                                        lPMLWExportactivity4.maskCurrent_History = Bitmap.createBitmap(lPMLWExportactivity4.mask_Initial_History.getWidth(), Exportactivity.this.mask_Initial_History.getHeight(), Bitmap.Config.ARGB_8888);
                                        new Canvas(Exportactivity.this.maskCurrent_History).drawCircle(Exportactivity.this.xInitMouse, Exportactivity.this.yInitMouse, ((float) LPMLWToolsController.get_Ray_Mask()) * (1.0f / Exportactivity.this.imageview_zoom_image.getZoomScale()), paint);
                                    }
                                    Exportactivity.this.LPMLWToolsController.addMask(Exportactivity.this.xInitMouse, Exportactivity.this.yInitMouse, Exportactivity.this.imageview_zoom_image.getZoomScale());
                                    Exportactivity.this.loadImage_Representation();
                                    Exportactivity.this.LPMLWLupaImageView.set_Position_Magnifying_glass(f, f2);
                                    Exportactivity.this.LPMLWLupaImageView.setVisibility(View.VISIBLE);
                                    break;
                                case 4:
                                    Exportactivity.this.LPMLWToolsController.delete_selection();
                                    break;
                                case 5:
                                    Exportactivity.this.pontoInicial = new Point(Exportactivity.this.xInitMouse, Exportactivity.this.yInitMouse, false);
                                    if (!Exportactivity.LPMLWAnimationController.is_there_Point(Exportactivity.this.pontoInicial)) {
                                        Exportactivity.this.current_LPMLW_project.add_point(Exportactivity.this.db, Exportactivity.this.pontoInicial);
                                        Exportactivity.LPMLWAnimationController.add_point(Exportactivity.this.pontoInicial);
                                        Exportactivity.this.pontosSequencia = new CopyOnWriteArrayList();
                                        Exportactivity.this.pontosSequencia.add(Exportactivity.this.pontoInicial);
                                        break;
                                    }
                                    break;
                                case 6:
                                    Exportactivity.this.LPMLWToolsController.set_Masking(true);
                                    Exportactivity lPMLWExportactivity5 = Exportactivity.this;
                                    ToolsController lPMLWToolsController2 = lPMLWExportactivity5.LPMLWToolsController;
                                    lPMLWExportactivity5.mask_Initial_History = LPMLWToolsController.getMask();
                                    if (Exportactivity.this.selected_eraser_option == 0) {
                                        if (Exportactivity.this.mask_Initial_History != null) {
                                            Exportactivity lPMLWExportactivity6 = Exportactivity.this;
                                            lPMLWExportactivity6.mask_Initial_History = lPMLWExportactivity6.mask_Initial_History.copy(Bitmap.Config.ARGB_8888, true);
                                            Exportactivity lPMLWExportactivity7 = Exportactivity.this;
                                            lPMLWExportactivity7.maskCurrent_History = Bitmap.createBitmap(lPMLWExportactivity7.mask_Initial_History.getWidth(), Exportactivity.this.mask_Initial_History.getHeight(), Bitmap.Config.ARGB_8888);
                                            new Canvas(Exportactivity.this.maskCurrent_History);
                                            float f3 = Exportactivity.this.xInitMouse;
                                            float f4 = Exportactivity.this.yInitMouse;
                                        }
                                        Exportactivity.this.LPMLWToolsController.deleteMask(Exportactivity.this.xInitMouse, Exportactivity.this.yInitMouse, Exportactivity.this.imageview_zoom_image.getZoomScale());
                                    } else if (Exportactivity.this.selected_eraser_option == 1) {
                                        Exportactivity.this.LPMLWToolsController.deletePointsMasking(f, f2, Exportactivity.this.imageview_zoom_image.getZoomScale());
                                    } else {
                                        if (Exportactivity.this.mask_Initial_History != null) {
                                            Exportactivity lPMLWExportactivity8 = Exportactivity.this;
                                            lPMLWExportactivity8.mask_Initial_History = lPMLWExportactivity8.mask_Initial_History.copy(Bitmap.Config.ARGB_8888, true);
                                            Exportactivity lPMLWExportactivity9 = Exportactivity.this;
                                            lPMLWExportactivity9.maskCurrent_History = Bitmap.createBitmap(lPMLWExportactivity9.mask_Initial_History.getWidth(), Exportactivity.this.mask_Initial_History.getHeight(), Bitmap.Config.ARGB_8888);
                                            new Canvas(Exportactivity.this.maskCurrent_History);
                                            float f5 = Exportactivity.this.xInitMouse;
                                            float f6 = Exportactivity.this.yInitMouse;
                                        }
                                        Exportactivity.this.LPMLWToolsController.deleteMask(Exportactivity.this.xInitMouse, Exportactivity.this.yInitMouse, Exportactivity.this.imageview_zoom_image.getZoomScale());
                                    }
                                    Exportactivity.this.reposition_magnifying_glass(f, f2);
                                    Exportactivity.this.loadImage_Representation();
                                    Exportactivity.this.LPMLWLupaImageView.set_Position_Magnifying_glass(f, f2);
                                    Exportactivity.this.LPMLWLupaImageView.setVisibility(View.VISIBLE);
                                    break;
                            }
                        } else {
                            Exportactivity.this.LPMLWToolsController.delete_selection();
                            Exportactivity.this.LPMLWToolsController.closePreview();
                            Exportactivity.this.LPMLWToolsController.deleteMask_zoom(Exportactivity.this.xInitMouse, Exportactivity.this.yInitMouse, Exportactivity.this.imageview_zoom_image.getZoomScale());
                            Exportactivity.this.LPMLWToolsController.set_Masking(false);
                            Exportactivity.this.mouseDragging = false;
                            Exportactivity.this.imageview_zoom_image.set_Zoom_On(true);
                            Exportactivity.this.LPMLWLupaImageView.setVisibility(View.GONE);
                        }
                        Exportactivity.this.loadImage_Representation();
                        return true;
                    } else if (action == 2) {
                        if (pointerCount == 1) {
                            Exportactivity.this.imageview_zoom_image.set_Zoom_On(false);
                            if (Exportactivity.this.mouseDragging) {
                                switch (Exportactivity.this.LPMLWToolsController.get_Tool_type()) {
                                    case 1:
                                        Utils.is_arrow_movement = true;
                                        Exportactivity.this.pontoInicial.set_Destination(f, f2);
                                        break;
                                    case 2:
                                        Exportactivity.this.reposition_magnifying_glass(motionEvent.getX(), motionEvent.getY());
                                        Exportactivity.this.loadImage_Representation();
                                        Exportactivity.this.LPMLWLupaImageView.set_Position_Magnifying_glass(f, f2);
                                        break;
                                    case 3:
                                        Exportactivity.this.reposition_magnifying_glass(motionEvent.getX(), motionEvent.getY());
                                        Exportactivity.this.x_positioning_mask.add(Float.valueOf(fArr[0]));
                                        Exportactivity.this.y_positioning_mask.add(Float.valueOf(fArr[1]));
                                        Exportactivity.this.LPMLWToolsController.addMask(f, f2, Exportactivity.this.imageview_zoom_image.getZoomScale());
                                        if (Exportactivity.this.mask_Initial_History != null) {
                                            new Canvas(Exportactivity.this.maskCurrent_History).drawCircle(f, f2, ((float) LPMLWToolsController.get_Ray_Mask()) * (1.0f / Exportactivity.this.imageview_zoom_image.getZoomScale()), paint);
                                        }
                                        Exportactivity.this.loadImage_Representation();
                                        Exportactivity.this.LPMLWLupaImageView.set_Position_Magnifying_glass(f, f2);
                                        break;
                                    case 4:
                                        Exportactivity.this.LPMLWToolsController.set_selecting(Exportactivity.this.pontoInicial, lPMLWPoint);
                                        if (!Exportactivity.LPMLWAnimationController.has_Selected_Point()) {
                                            Exportactivity.this.LPMLWToolsController.setDeleteToolVisibility(false);
                                            break;
                                        } else {
                                            Exportactivity.this.LPMLWToolsController.setDeleteToolVisibility(true);
                                            break;
                                        }
                                    case 5:
                                        Exportactivity.this.pontoInicial.set_Destination(f, f2);
                                        Utils.is_arrow_movement = true;
                                        if (Exportactivity.this.pontoInicial.distance_to(lPMLWPoint) >= ((double) (((float) Exportactivity.this.LPMLWToolsController.get_Tam_Movement_Sequence()) / Exportactivity.this.imageview_zoom_image.getZoomScale()))) {
                                            Exportactivity.this.current_LPMLW_project.update_Points(Exportactivity.this.db, Exportactivity.this.pontoInicial);
                                            Exportactivity.this.pontoInicial = new Point(f, f2, false);
                                            Exportactivity.this.current_LPMLW_project.add_point(Exportactivity.this.db, Exportactivity.this.pontoInicial);
                                            Exportactivity.LPMLWAnimationController.add_point(Exportactivity.this.pontoInicial);
                                            Exportactivity.this.pontosSequencia.add(Exportactivity.this.pontoInicial);
                                            break;
                                        }
                                        break;
                                    case 6:
                                        if (Exportactivity.this.selected_eraser_option == 0) {
                                            Exportactivity.this.LPMLWToolsController.deleteMask(f, f2, Exportactivity.this.imageview_zoom_image.getZoomScale());
                                        } else if (Exportactivity.this.selected_eraser_option == 1) {
                                            int round = Math.round(fArr[0]);
                                            int round2 = Math.round(fArr[1]);
                                            List<Point> list = Exportactivity.LPMLWAnimationController.get_All_Points_List();
                                            int zoomScale = (int) (((float) LPMLWToolsController.get_Ray_Mask()) * (1.0f / Exportactivity.this.imageview_zoom_image.getZoomScale()));
                                            for (int i = 0; i < list.size(); i++) {
                                                Point lPMLWPoint2 = Exportactivity.LPMLWAnimationController.get_All_Points_List().get(i);
                                                if (Exportactivity.isInside(round, round2, zoomScale, Math.round(lPMLWPoint2.getXInit()), Math.round(lPMLWPoint2.getYInit()))) {
                                                    lPMLWPoint2.set_Selected(true);
                                                }
                                            }
                                            List<Point> list2 = Exportactivity.LPMLWAnimationController.get_Selected_Points_List();
                                            if (list2 != null && !list2.isEmpty()) {
                                                Exportactivity.this.current_LPMLW_project.delete_Points(Exportactivity.this.db, list2);
                                                Exportactivity.LPMLWAnimationController.delete_Selected_Points();
                                                Exportactivity.LPMLWHistoryController.add_history(list2, false);
                                            }
                                            Exportactivity.this.LPMLWToolsController.deletePointsMasking(f, f2, Exportactivity.this.imageview_zoom_image.getZoomScale());
                                        } else {
                                            int round3 = Math.round(fArr[0]);
                                            int round4 = Math.round(fArr[1]);
                                            List<Point> list3 = Exportactivity.LPMLWAnimationController.get_All_Points_List();
                                            int zoomScale2 = (int) (((float) LPMLWToolsController.get_Ray_Mask()) * (1.0f / Exportactivity.this.imageview_zoom_image.getZoomScale()));
                                            for (int i2 = 0; i2 < list3.size(); i2++) {
                                                Point lPMLWPoint3 = Exportactivity.LPMLWAnimationController.get_All_Points_List().get(i2);
                                                if (Exportactivity.isInside(round3, round4, zoomScale2, Math.round(lPMLWPoint3.getXInit()), Math.round(lPMLWPoint3.getYInit()))) {
                                                    lPMLWPoint3.set_Selected(true);
                                                }
                                            }
                                            List<Point> list4 = Exportactivity.LPMLWAnimationController.get_Selected_Points_List();
                                            if (list4 != null && !list4.isEmpty()) {
                                                Exportactivity.this.current_LPMLW_project.delete_Points(Exportactivity.this.db, list4);
                                                Exportactivity.LPMLWAnimationController.delete_Selected_Points();
                                                Exportactivity.LPMLWHistoryController.add_history(list4, false);
                                            }
                                            Exportactivity.this.LPMLWToolsController.deleteMask(f, f2, Exportactivity.this.imageview_zoom_image.getZoomScale());
                                        }
                                        Exportactivity.this.reposition_magnifying_glass(motionEvent.getX(), motionEvent.getY());
                                        Exportactivity.this.loadImage_Representation();
                                        Exportactivity.this.LPMLWLupaImageView.set_Position_Magnifying_glass(f, f2);
                                        break;
                                }
                            }
                        } else {
                            Exportactivity.this.LPMLWToolsController.set_Masking(false);
                            if (Exportactivity.LPMLWAnimationController != null) {
                                Exportactivity.this.LPMLWToolsController.get_Tool_type();
                                if (Exportactivity.this.LPMLWToolsController.get_Tool_type() == 3) {
                                    if (Exportactivity.this.x_positioning_mask == null || Exportactivity.this.y_positioning_mask == null) {
                                        Exportactivity.this.LPMLWToolsController.deleteMask_zoom(f, f2, Exportactivity.this.imageview_zoom_image.getZoomScale());
                                    } else if (Exportactivity.this.x_positioning_mask.size() <= 0 || Exportactivity.this.y_positioning_mask.size() <= 0) {
                                        Exportactivity.this.LPMLWToolsController.deleteMask_zoom(f, f2, Exportactivity.this.imageview_zoom_image.getZoomScale());
                                    } else {
                                        for (int i3 = 0; i3 < Exportactivity.this.x_positioning_mask.size(); i3++) {
                                            Exportactivity.this.LPMLWToolsController.deleteMask_zoom(Exportactivity.this.x_positioning_mask.get(i3).floatValue(), Exportactivity.this.y_positioning_mask.get(i3).floatValue(), Exportactivity.this.imageview_zoom_image.getZoomScale());
                                        }
                                    }
                                } else if (Exportactivity.this.LPMLWToolsController.get_Tool_type() == 2) {
                                    List<Point> list5 = Exportactivity.LPMLWAnimationController.get_All_Points_List();
                                    if (list5.size() > 0) {
                                        Point lPMLWPoint4 = Exportactivity.LPMLWAnimationController.get_All_Points_List().get(list5.size() - 1);
                                        Exportactivity.LPMLWAnimationController.delete_point(lPMLWPoint4);
                                        Exportactivity.this.current_LPMLW_project.deletePonto(Exportactivity.this.db, lPMLWPoint4);
                                    }
                                } else if (Exportactivity.this.LPMLWToolsController.get_Tool_type() == 1 || Exportactivity.this.LPMLWToolsController.get_Tool_type() == 5) {
                                    List<Point> list6 = Exportactivity.LPMLWAnimationController.get_All_Points_List();
                                    if (list6.size() > 0) {
                                        Exportactivity.LPMLWAnimationController.delete_point(Exportactivity.LPMLWAnimationController.get_All_Points_List().get(list6.size() - 1));
                                    }
                                }
                            }
                            Exportactivity.this.LPMLWLupaImageView.setVisibility(View.GONE);
                            Exportactivity.this.LPMLWToolsController.delete_selection();
                            Exportactivity.this.LPMLWToolsController.closePreview();
                            Exportactivity.this.mouseDragging = false;
                            Exportactivity.this.imageview_zoom_image.set_Zoom_On(true);
                        }
                        Exportactivity.this.loadImage_Representation();
                        return true;
                    } else if (action == 1) {
                        Exportactivity.this.x_positioning_mask.clear();
                        Exportactivity.this.y_positioning_mask.clear();
                        Exportactivity.this.x_positioning_mask = new ArrayList<>();
                        Exportactivity.this.y_positioning_mask = new ArrayList<>();
                        if (pointerCount == 1) {
                            Exportactivity.this.imageview_zoom_image.set_Zoom_On(false);
                            Exportactivity.this.mouseDragging = false;
                            Exportactivity.this.LPMLWLupaImageView.setVisibility(View.GONE);
                            Exportactivity.this.LPMLWToolsController.setDelete_visibility(false);
                            try {
                                switch (Exportactivity.this.LPMLWToolsController.get_Tool_type()) {
                                    case 1:
                                        Exportactivity.this.pontoInicial.set_Destination(f, f2);
                                        Exportactivity.this.current_LPMLW_project.update_Points(Exportactivity.this.db, Exportactivity.this.pontoInicial);
                                        Exportactivity.LPMLWHistoryController.add_history(Exportactivity.this.pontoInicial, true);
                                        Exportactivity.this.LPMLWToolsController.set_relative_layout_brush_size_visibility(true);
                                        Exportactivity.this.LPMLWToolsController.setSubToolVelocidadeVisibility(true);
                                        break;
                                    case 2:
                                        Exportactivity.this.current_LPMLW_project.update_Points(Exportactivity.this.db, lPMLWPoint);
                                        Exportactivity.this.LPMLWToolsController.set_relative_layout_brush_size_visibility(true);
                                        Exportactivity.this.LPMLWToolsController.setSubToolVelocidadeVisibility(true);
                                        break;
                                    case 3:
                                        Exportactivity.this.LPMLWToolsController.set_Masking(false);
                                        ToolsController lPMLWToolsController3 = Exportactivity.this.LPMLWToolsController;
                                        Bitmap mask = LPMLWToolsController.getMask();
                                        Exportactivity.this.Update_Masking_Database(mask);
                                        if (Exportactivity.this.mask_Initial_History == null) {
                                            Exportactivity.LPMLWHistoryController.add_history(mask, true);
                                            break;
                                        } else {
                                            Paint paint2 = new Paint(1);
                                            paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                                            paint2.setFilterBitmap(true);
                                            paint2.setStyle(Paint.Style.FILL);
                                            new Canvas(Exportactivity.this.maskCurrent_History).drawBitmap(Exportactivity.this.mask_Initial_History, 0.0f, 0.0f, paint2);
                                            Exportactivity.LPMLWHistoryController.add_history(Exportactivity.this.maskCurrent_History, true);
                                            Exportactivity.this.LPMLWToolsController.set_relative_layout_brush_size_visibility(true);
                                            Exportactivity.this.LPMLWToolsController.setSubToolVelocidadeVisibility(true);
                                            break;
                                        }
                                    case 4:
                                        Exportactivity.this.LPMLWToolsController.set_selecting(Exportactivity.this.pontoInicial, lPMLWPoint);
                                        Exportactivity.this.LPMLWToolsController.set_selecting(false);
                                        Exportactivity.this.LPMLWToolsController.set_relative_layout_brush_size_visibility(true);
                                        Exportactivity.this.LPMLWToolsController.setSubToolVelocidadeVisibility(true);
                                        if (!Exportactivity.LPMLWAnimationController.has_Selected_Point()) {
                                            Exportactivity.this.LPMLWToolsController.setDeleteToolVisibility(false);
                                            break;
                                        } else {
                                            Exportactivity.this.LPMLWToolsController.setDeleteToolVisibility(true);
                                            break;
                                        }
                                    case 5:
                                        Exportactivity.this.pontoInicial.set_Destination(f, f2);
                                        Exportactivity.this.current_LPMLW_project.update_Points(Exportactivity.this.db, Exportactivity.this.pontoInicial);
                                        Exportactivity.LPMLWHistoryController.add_history(Exportactivity.this.pontosSequencia, true);
                                        Exportactivity.this.LPMLWToolsController.set_relative_layout_brush_size_visibility(true);
                                        Exportactivity.this.LPMLWToolsController.setSubToolVelocidadeVisibility(true);
                                        break;
                                    case 6:
                                        Exportactivity.this.LPMLWToolsController.set_Masking(false);
                                        Exportactivity lPMLWExportactivity10 = Exportactivity.this;
                                        ToolsController lPMLWToolsController4 = lPMLWExportactivity10.LPMLWToolsController;
                                        lPMLWExportactivity10.Update_Masking_Database(LPMLWToolsController.getMask());
                                        if (Exportactivity.this.mask_Initial_History != null) {
                                            Paint paint3 = new Paint(1);
                                            paint3.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                                            paint3.setFilterBitmap(true);
                                            paint3.setStyle(Paint.Style.FILL);
                                            new Canvas(Exportactivity.this.maskCurrent_History).drawBitmap(Exportactivity.this.mask_Initial_History, 0.0f, 0.0f, paint3);
                                            Exportactivity.LPMLWHistoryController.add_history(Exportactivity.this.maskCurrent_History, false);
                                            Exportactivity.this.LPMLWToolsController.set_relative_layout_brush_size_visibility(true);
                                            Exportactivity.this.LPMLWToolsController.setSubToolVelocidadeVisibility(true);
                                            break;
                                        }
                                        break;
                                }
                            } catch (Exception unused) {
                            }
                        } else {
                            Exportactivity.this.LPMLWToolsController.set_Masking(false);
                            Exportactivity.this.LPMLWLupaImageView.setVisibility(View.GONE);
                            Exportactivity.this.LPMLWToolsController.delete_selection();
                            Exportactivity.this.LPMLWToolsController.closePreview();
                            Exportactivity.this.mouseDragging = false;
                            Exportactivity.this.imageview_zoom_image.set_Zoom_On(true);
                        }
                        Exportactivity.this.loadImage_Representation();
                        return true;
                    }
                } else if (Exportactivity.this.LPMLWToolsController.isPlayingPreview()) {
                    Exportactivity.this.LPMLWToolsController.closePreview();
                    Exportactivity.this.preview_video_speed_seekbar.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                }
            }
            return false;
        }
    }


    public class Masking_Async extends AsyncTask {


        @Override
        public Object doInBackground(Object[] objArr) {
            Exportactivity.this.current_LPMLW_project.updateProject(Exportactivity.this.db);
            return null;
        }
    }


    class tools_controller_listener_after_initialising implements ToolsController.ToolsListener {


        @Override
        public void onPlayPreview() {
            Exportactivity.this.image_Details_Tools.setVisibility(View.GONE);
            Exportactivity.this.imageview_zoom_image.restartZoom();
            if (Exportactivity.this.LPMLWToolsController.get_Tool_type() == 7) {
                Exportactivity.this.imageview_zoom_image.set_Zoom_On(false);
            }
            Exportactivity.this.current_LPMLW_project.refresh_Time_Resolution(Exportactivity.this.db);
            Exportactivity.this.LPMLWToolsController.set_Time_Preview(Exportactivity.this.current_LPMLW_project.get_Time_Save());
            Exportactivity.LPMLWAnimationController.set_Time_Animation(Exportactivity.this.current_LPMLW_project.get_Time_Save());
            Exportactivity.LPMLWAnimationController.startAnimation();
        }

        @Override
        public void onStopPreview() {
            Exportactivity.this.preview_video_speed_seekbar.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            if (Exportactivity.this.LPMLWToolsController.get_Tool_type() == 7) {
                Exportactivity.this.imageview_zoom_image.set_Zoom_On(true);
            }
            if (Exportactivity.LPMLWAnimationController != null) {
                Exportactivity.LPMLWAnimationController.stopAnimation(Exportactivity.this);
            }
            Exportactivity.this.loadImage_Representation();
        }

        @Override
        public void on_Time_Changing(int i) {
            Exportactivity.LPMLWAnimationController.set_Time_Animation(i);
        }

        @Override
        public void on_Changed_Time(int i) {
            Exportactivity.LPMLWAnimationController.set_Time_Animation(i);
            Exportactivity.this.current_LPMLW_project.set_Time_Save(i);
            Exportactivity.this.current_LPMLW_project.updateProject(Exportactivity.this.db);
        }

        @Override
        public void on_Press_Stabilize() {
            Exportactivity.this.imageview_zoom_image.set_Zoom_On(false);
            Exportactivity.this.loadImage_Representation();
        }

        @Override
        public void on_Press_Selection_button() {
            Exportactivity.this.imageview_zoom_image.set_Zoom_On(false);
            Exportactivity.this.loadImage_Representation();
        }

        @Override
        public void onPressZoom() {
            Exportactivity.this.imageview_zoom_image.set_Zoom_On(true);
            Exportactivity.this.loadImage_Representation();
        }

        @Override
        public void on_Press_Single_Arrow_Movement() {
            Exportactivity.this.imageview_zoom_image.set_Zoom_On(false);
            Exportactivity.this.loadImage_Representation();
        }

        @Override
        public void onPress_Arrow_Movement_in_Sequence() {
            Exportactivity.this.imageview_zoom_image.set_Zoom_On(false);
            Exportactivity.this.loadImage_Representation();
        }

        @Override
        public void onChange_Size_of_Arrow_Mov_Sequence(int i, Bitmap bitmap) {
            if (bitmap != null) {
                Bitmap createBitmap = Bitmap.createBitmap(Exportactivity.this.imageview_zoom_image.getWidth(), 100, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                ToolsController lPMLWToolsController = Exportactivity.this.LPMLWToolsController;
                canvas.drawColor(Color.argb(LPMLWToolsController.getAlphaMask(), 0, 0, 0));
                canvas.drawBitmap(bitmap, (float) (((double) ((float) createBitmap.getWidth())) / 2.5d), 50.0f, (Paint) null);
                Exportactivity.this.image_Details_Tools.setImageBitmap(createBitmap);
                Exportactivity.this.image_Details_Tools.setVisibility(View.VISIBLE);
                return;
            }
            Exportactivity.this.imageview_zoom_image.set_Zoom_On(false);
            Exportactivity.this.loadImage_Representation();
        }

        @Override
        public void on_Masking_Pressed() {
            Exportactivity.this.imageview_zoom_image.set_Zoom_On(false);
            Exportactivity.this.loadImage_Representation();
        }

        @Override
        public void on_Change_Ray_Mask(int i, Bitmap bitmap) {
            if (bitmap != null) {
                Bitmap createBitmap = Bitmap.createBitmap(Exportactivity.this.imagemRepresentacao.getWidth(), Exportactivity.this.imagemRepresentacao.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                float width = (((float) createBitmap.getWidth()) / 2.0f) - (((float) bitmap.getWidth()) / 2.0f);
                float height = (((float) createBitmap.getHeight()) / 2.0f) - (((float) bitmap.getHeight()) / 2.0f);
                Paint paint = new Paint(1);
                paint.setFilterBitmap(true);
                ToolsController lPMLWToolsController = Exportactivity.this.LPMLWToolsController;
                paint.setAlpha(LPMLWToolsController.getAlphaMask());
                canvas.drawBitmap(bitmap, width, height, paint);
                Exportactivity.this.image_Details_Tools.setImageBitmap(createBitmap);
                Exportactivity.this.image_Details_Tools.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPress_Clear_Mask() {
            Exportactivity.this.imageview_zoom_image.set_Zoom_On(false);
            Exportactivity.this.loadImage_Representation();
        }

        @Override
        public void onPressDelete() {
            List<Point> list = Exportactivity.LPMLWAnimationController.get_Selected_Points_List();
            if (list == null || list.isEmpty()) {
                Exportactivity.this.LPMLWToolsController.setDeleteToolVisibility(false);
            } else {
                Exportactivity.this.current_LPMLW_project.delete_Points(Exportactivity.this.db, list);
                Exportactivity.LPMLWAnimationController.delete_Selected_Points();
                Exportactivity.LPMLWHistoryController.add_history(list, false);
            }
            Exportactivity.this.imageview_zoom_image.set_Zoom_On(false);
            Exportactivity.this.loadImage_Representation();
        }
    }


    public class request_permission_from_on_create_method implements Permission_Listener {


        @Override
        public void onPermissionGranted() {
            Exportactivity.this.open_Last_Project();
        }
    }


    public class request_permission_when_open_new_project_method_called implements Permission_Listener {


        @Override
        public void onPermissionGranted() {
            Intent intent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            intent.setType("image/*");
            if (intent.resolveActivity(Exportactivity.this.getPackageManager()) != null) {
                Exportactivity lPMLWExportactivity = Exportactivity.this;
                lPMLWExportactivity.startActivityForResult(Intent.createChooser(intent, lPMLWExportactivity.getResources().getString(R.string.Select_new_image)), 1);
            }
        }
    }


    public class on_animation_click_listener implements AnimationController.AnimateListener {


        @Override
        public void onAnimate(Bitmap bitmap) {
            if (Exportactivity.this.imageLoaded && Exportactivity.this.LPMLWToolsController.isPlayingPreview()) {
                Exportactivity.this.imageview_zoom_image.setImageBitmap(bitmap);
                Exportactivity.this.iv_preview.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    public void open_Last_Project() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), 0);
        long j = sharedPreferences.getLong("id_Last_Project", -1);
        if (j != -1) {
            Project project = this.db.getProject(j);
            if (project != null) {
                loadProject(project);
                return;
            }
            sharedPreferences.edit().remove("id_Last_Project").commit();
        }
        Project lPMLWProject = this.db.get_Last_Project();
        if (lPMLWProject != null) {
            loadProject(lPMLWProject);
            return;
        }
        this.image_Details_Tools.setVisibility(View.GONE);
        this.image_Details_Tools.setClickable(false);
    }

    public void Update_Masking_Database(Bitmap bitmap) {
        if (bitmap != null) {
            this.current_LPMLW_project.set_Mask(Bitmap.createScaledBitmap(bitmap, Math.round(((float) bitmap.getWidth()) * this.current_LPMLW_project.get_Proportion()), Math.round(((float) bitmap.getHeight()) * this.current_LPMLW_project.get_Proportion()), true));
            new Masking_Async().execute(new Object[0]);
        }
    }

    @Override
    public boolean onMenuOpened(int i, Menu menu) {
        return super.onMenuOpened(i, menu);
    }

    @Override
    public void onBackPressed() {
        if (LPMLWVideoSaver.dialog != null && LPMLWVideoSaver.dialog.isShowing()) {
            this.imageview_zoom_image.setEnabled(false);
            this.imageview_zoom_image.setClickable(false);
        } else if (cv_save_video.getVisibility() == View.VISIBLE) {
            cv_save_video.setVisibility(View.GONE);
            if (this.LPMLWToolsController.get_Tool_type() == 0) {
                this.LPMLWToolsController.set_relative_layout_brush_size_visibility(false);
            } else {
                this.LPMLWToolsController.set_relative_layout_brush_size_visibility(true);
            }
            this.imageview_zoom_image.setEnabled(true);
            this.imageview_zoom_image.setClickable(true);
            this.preview_video_speed_seekbar.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            this.LPMLWToolsController.closePreview();
        } else {
            show_back_dialog(this);
        }
    }

    public void show_zoom_tutorial(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.zoom_tutorial_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.color.color_transaprent);
        Display defaultDisplay = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        int i = displayMetrics.widthPixels;
        dialog.getWindow().setLayout((int) (((double) i) * 0.75d), (int) (((double) displayMetrics.heightPixels) * 0.65d));
        editor.putBoolean("is_zoom_tutorial_showed", true);
        editor.commit();
        ((LinearLayout) dialog.findViewById(R.id.ll_close_tutorial)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void show_back_dialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.back_dialog);
        dialog.getWindow().setLayout(-1, -2);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        TextView textView = (TextView) dialog.findViewById(R.id.tv_title);
        TextView textView2 = (TextView) dialog.findViewById(R.id.tv_subtitle);
        TextView textView3 = (TextView) dialog.findViewById(R.id.btn_yes);
        TextView textView4 = (TextView) dialog.findViewById(R.id.btn_no);
        if (this.from_main_activity.equalsIgnoreCase("yes")) {
            textView.setText("Discard Changes ?");
            textView2.setText("Do you want all your changes to get discarded ?");
        } else {
            textView.setText("Exit Alert ?");
            textView2.setText("Do you want to exit without saving the live photo ?");
        }
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Exportactivity.this.delete_current_project_without_saving();
                if (Exportactivity.this.from_main_activity.equals("yes")) {
                    Exportactivity.this.startActivity(new Intent(Exportactivity.this, HomeActivity.class));
                    Exportactivity.this.finish();
                } else if (Exportactivity.this.from_main_activity.equals("no")) {
                    Exportactivity.this.startActivity(new Intent(Exportactivity.this, CreationActivity.class));
                    Exportactivity.this.finish();
                }
                dialog.dismiss();
            }
        });
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void show_save_dialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.back_dialog);
        dialog.getWindow().setLayout(-1, -2);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        ((TextView) dialog.findViewById(R.id.tv_title)).setText("Save Changes ?");
        ((TextView) dialog.findViewById(R.id.tv_subtitle)).setText("Do you want to save image without performing any changes ?");
        ((TextView) dialog.findViewById(R.id.btn_yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Exportactivity.this.save_file_gallery();
                dialog.dismiss();
            }
        });
        ((TextView) dialog.findViewById(R.id.btn_no)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void show_empty_dialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.empty_dialog);
        dialog.getWindow().setLayout(-1, -2);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        ((TextView) dialog.findViewById(R.id.btn_yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void show_sequence_text(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.empty_dialog);
        dialog.getWindow().setLayout(-1, -2);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        ((TextView) dialog.findViewById(R.id.tv_title)).setText("Move your finger from one point to another for showing motion on photo");
        ((TextView) dialog.findViewById(R.id.btn_yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        delete_current_project_without_saving();
    }

    public void delete_current_project_without_saving() {
        String uri = this.current_LPMLW_project.getUri().toString();
        String substring = uri.substring(0, uri.lastIndexOf("."));
        String str = substring.substring(substring.lastIndexOf("/") + 1) + ".mp4";
        File file = new File(get_old_path());
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(file, str);
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        if (file2.exists()) {
            try {
                mediaMetadataRetriever.setDataSource(this, Uri.fromFile(file2));
                if (mediaMetadataRetriever.extractMetadata(17) == null) {
                    file2.delete();
                    this.current_LPMLW_project.deleteProject(this.db);
                    List<Point> list = LPMLWAnimationController.get_All_Points_List();
                    if (list != null && !list.isEmpty()) {
                        this.current_LPMLW_project.delete_Points(this.db, list);
                        LPMLWAnimationController.delete_All_Points();
                        LPMLWHistoryController.add_history(list, false);
                    }
                }
            } catch (Exception unused) {
            }
        } else {
            this.current_LPMLW_project.deleteProject(this.db);
            List<Point> list2 = LPMLWAnimationController.get_All_Points_List();
            if (list2 != null && !list2.isEmpty()) {
                this.current_LPMLW_project.delete_Points(this.db, list2);
                LPMLWAnimationController.delete_All_Points();
                LPMLWHistoryController.add_history(list2, false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if ((LPMLWVideoSaver.dialog != null && LPMLWVideoSaver.dialog.isShowing()) || cv_save_video.getVisibility() == View.VISIBLE) {
            this.imageview_zoom_image.setEnabled(false);
            this.imageview_zoom_image.setClickable(false);
        } else if (menuItem.getItemId() == 16908332) {
            show_back_dialog(this);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    public void reposition_magnifying_glass(float f, float f2) {
        int width = this.LPMLWLupaImageView.getWidth() / 2;
        if (f > ((float) ((this.imageview_zoom_image.getWidth() - this.LPMLWLupaImageView.getWidth()) - width)) && f2 < this.imageview_zoom_image.getY() + ((float) this.LPMLWLupaImageView.getHeight()) + ((float) width)) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.LPMLWLupaImageView.getLayoutParams();
            this.params = layoutParams;
            layoutParams.removeRule(21);
            this.params.addRule(20);
            this.LPMLWLupaImageView.setLayoutParams(this.params);
        }
        if (f < ((float) (this.LPMLWLupaImageView.getWidth() + width)) && f2 < this.imageview_zoom_image.getY() + ((float) this.LPMLWLupaImageView.getHeight()) + ((float) width)) {
            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.LPMLWLupaImageView.getLayoutParams();
            this.params = layoutParams2;
            layoutParams2.removeRule(20);
            this.params.addRule(21);
            this.LPMLWLupaImageView.setLayoutParams(this.params);
        }
    }

    @Override
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    public void loadImagem(Uri uri) {
        this.imageLoaded = false;
        LPMLWHistoryController.clear();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            InputStream openInputStream = getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(openInputStream, null, options);
            openInputStream.close();
            int i = options.outWidth > options.outHeight ? options.outWidth : options.outHeight;
            int i2 = MAX_RESOLUTION_SAVE;
            if (i > i2) {
                i = i2;
            }
            options.inSampleSize = Utils.calculateInSampleSize(options, i, i);
            options.inJustDecodeBounds = false;
            try {
                InputStream openInputStream2 = getContentResolver().openInputStream(uri);
                Bitmap decodeStream = BitmapFactory.decodeStream(openInputStream2, null, options);
                openInputStream2.close();
                String str = getResources().getString(R.string.project_folder) + "_" + ((int) (Math.random() * 1000000.0d));
                Project lPMLWProject = new Project(str, decodeStream.copy(Bitmap.Config.ARGB_8888, true), Utils.writeImageAndGetPathUri(this, decodeStream, str));
                lPMLWProject.set_Resolution_Save(i);
                lPMLWProject.addProject(this.db);
                loadProject(lPMLWProject);
            } catch (Exception unused) {
                Toast.makeText(this, getResources().getText(R.string.load_image_fail), Toast.LENGTH_LONG).show();
            }
        } catch (Exception unused2) {
            Toast.makeText(this, getResources().getText(R.string.load_image_fail), Toast.LENGTH_LONG).show();
        }
    }

    public void loadProject(Project lPMLWProject) {
        restartDefinitions();
        Log.e("in_export", "project_load_called");
        Log.e("in_export", "project_loaded->\nid : " + lPMLWProject.getId() + "\ntitle " + lPMLWProject.get_Title() + "\npath " + lPMLWProject.getUri() + "\nbitmap " + lPMLWProject.getImagem());
        try {
            if (lPMLWProject != null) {
                if (lPMLWProject.getImagem() == null && !lPMLWProject.reloadBitmapUri(this, this.db)) {
                    lPMLWProject.deleteProject(this.db);
                    return;
                }
                this.image_Details_Tools.setClickable(false);
                SharedPreferences.Editor edit = getSharedPreferences(getString(R.string.preference_file_key), 0).edit();
                edit.putLong("id_Last_Project", lPMLWProject.getId());
                edit.commit();
                this.current_LPMLW_project = lPMLWProject;
                this.imageLoaded = true;
                this.LPMLWToolsController.setEnabled(true);
                if (lPMLWProject.get_Mask() != null) {
                    this.LPMLWToolsController.set_Initial_Mask(Bitmap.createScaledBitmap(lPMLWProject.get_Mask(), Math.round(((float) lPMLWProject.get_Mask().getWidth()) * (1.0f / lPMLWProject.get_Proportion())), Math.round(((float) lPMLWProject.get_Mask().getHeight()) * (1.0f / lPMLWProject.get_Proportion())), true));
                }
                relative_layout_bottom_toolbar_functionalities.setVisibility(View.VISIBLE);
                AnimationController init = LPMLWAnimationController.init(this.current_LPMLW_project);
                LPMLWAnimationController = init;
                init.setOnAnimateListener(new on_animation_click_listener());
                loadImage_Representation();
                this.image_main_bitmap = lPMLWProject.getImagem();
                return;
            }
            Log.e("in_alreadycreated", "projectnotfound ");
        } catch (Exception e) {
            Log.e("in_alreadycreated", "exception " + e);
        }
    }

    public void restartDefinitions() {
        AnimationController lPMLWAnimationController = LPMLWAnimationController;
        if (lPMLWAnimationController != null) {
            lPMLWAnimationController.stopAnimation(this);
            this.LPMLWToolsController.closePreview();
        }
        this.imageLoaded = false;
        this.LPMLWToolsController.restartDefinitions();
        this.LPMLWToolsController.setEnabled(false);
        this.imageview_zoom_image.restartZoom();
    }

    @Override
    public void onResume() {


        if (Build.VERSION.SDK_INT >= 33) {
            if ((LPMLWVideoSaver.dialog == null || !LPMLWVideoSaver.dialog.isShowing()) && cv_save_video.getVisibility() != View.VISIBLE) {
                this.change_image_from_gallery.setEnabled(true);
            } else {
                this.imageview_zoom_image.setEnabled(false);
                this.imageview_zoom_image.setClickable(false);
                this.change_image_from_gallery.setEnabled(true);
            }


        } else if (hasStoragePermission("android.permission.WRITE_EXTERNAL_STORAGE") || hasStoragePermission("android.permission.READ_EXTERNAL_STORAGE")) {
            if ((LPMLWVideoSaver.dialog == null || !LPMLWVideoSaver.dialog.isShowing()) && cv_save_video.getVisibility() != View.VISIBLE) {
                this.change_image_from_gallery.setEnabled(true);
            } else {
                this.imageview_zoom_image.setEnabled(false);
                this.imageview_zoom_image.setClickable(false);
                this.change_image_from_gallery.setEnabled(true);
            }
        } else if (Build.VERSION.SDK_INT > 29) {
            requestmyPermission(this.RC_STORAGE_PERM, "android.permission.WRITE_EXTERNAL_STORAGE");
        } else {
            requestmyPermission(this.RC_STORAGE_PERM, "android.permission.WRITE_EXTERNAL_STORAGE");
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.LPMLWToolsController.closePreview();
        this.preview_video_speed_seekbar.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        this.iv_play_btn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    public void loadImage_Representation() {
        if (this.imageLoaded) {
            this.image_Details_Tools.setVisibility(View.GONE);
            this.save_video.setVisibility(View.VISIBLE);
            this.preview_video_speed_seekbar.setVisibility(View.VISIBLE);
            Bitmap imagemRepresentacao = LPMLWAnimationController.getImagemRepresentacao(this.showDetalhes, this.imageview_zoom_image.getZoomScale());
            this.imagemRepresentacao = imagemRepresentacao;
            this.LPMLWToolsController.to_paint(imagemRepresentacao, this.imageview_zoom_image.getZoomScale());
            if (this.LPMLWToolsController.get_Tool_type() == 3 || this.LPMLWToolsController.get_Tool_type() == 6 || this.LPMLWToolsController.get_Tool_type() == 2) {
                this.LPMLWLupaImageView.setImageBitmap(this.imagemRepresentacao, this.imageview_zoom_image.getZoomScale());
                this.LPMLWLupaImageView.invalidate();
            }
            this.imageview_zoom_image.setImageBitmap(this.imagemRepresentacao);
            this.imageview_zoom_image.invalidate();
        }
    }

    public boolean hasStoragePermission(String... strArr) {
        return EasyPermissions.hasPermissions(this, strArr);
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EasyPermissions.onRequestPermissionsResult(i, strArr, iArr, this);
    }

    @Override
    public void onPermissionsGranted(int i, List<String> list) {
        open_Last_Project();
    }

    @Override
    public void onPermissionsDenied(int i, List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    public void requestmyPermission(int i, String... strArr) {
        EasyPermissions.requestPermissions(this, "This needs permission to access", i, strArr);
    }
}
