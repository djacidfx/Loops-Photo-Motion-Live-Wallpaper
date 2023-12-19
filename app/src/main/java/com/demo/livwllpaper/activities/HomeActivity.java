package com.demo.livwllpaper.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.livwllpaper.AdAdmob;
import com.demo.livwllpaper.R;
import com.google.android.material.snackbar.Snackbar;
import com.demo.livwllpaper.Interfaces.WallpapersClicked;
import com.demo.livwllpaper.Interfaces.onPreviewClicked;
import com.demo.livwllpaper.Interfaces.onposclickedadapter;
import com.demo.livwllpaper.Utilsx.StorageUtils;
import com.demo.livwllpaper.Utilsx.Utils;
import com.demo.livwllpaper.adapters.WallpaperAdapter;
import com.demo.livwllpaper.adapters.Whomescreenadapter;
import com.demo.livwllpaper.beans.Project;
import com.demo.livwllpaper.controllers.AnimationController;
import com.demo.livwllpaper.controllers.HistoryController;
import com.demo.livwllpaper.databases.DatabaseHandler;
import com.demo.livwllpaper.models.VideoFileModel;
import com.demo.livwllpaper.wallpapermodule.LPMLWApplication;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    public static int PICK_IMAGE_REQUEST = 2;
    public WallpaperAdapter LPMLW_wallpaperAdapter;
    Whomescreenadapter adapter_imageshm;
    public ArrayList<String> assets_playlist;
    TextView cv_my_creation_home_screen;
    private DatabaseHandler db;
    private File file;
    FrameLayout frame_layout_loader;
    LinearLayout iv_empty_creation;
    ImageView iv_loader;
    ImageView iv_more;
    ImageView iv_tutorial_home_screen;
    ArrayList<String> listItems;
    private List<Project> listProjects;
    public ArrayList<String> list_images;
    public ArrayList<String> list_video_path;
    LinearLayout ll_creation_empty;
    LinearLayout ll_creation_notempty;
    LinearLayout ll_for_menu;
    PopupWindow pwindow;
    RecyclerView rv_creation_list;
    RecyclerView rv_listing_assets;

    TextView tv_header_home_screen;
    TextView tv_mycreation_count;
    TextView tv_mycreation_title_hm;
    private Uri uri;
    private int RC_STORAGE_PERM = 101;
    public boolean open_gallery = false;
    public boolean load_data = false;
    private long mLastClickTime = 0;
    public int PREVIEW_REQUEST_CODE = 100;
    WallpapersClicked LPMLW_wallpapers_clicked = new WallpapersClicked() {
        @Override
        public void on_clicked_position(int i, String str) {
            long uptimeMillis = SystemClock.uptimeMillis();
            long j = uptimeMillis - HomeActivity.this.mLastClickTime;
            HomeActivity.this.mLastClickTime = uptimeMillis;
            if (i != 0) {
                Intent intent = new Intent(HomeActivity.this, DisplayWallpaperactivity.class);
                intent.putExtra("list_images", HomeActivity.this.list_images);
                intent.putExtra("wallaper_video_list", HomeActivity.this.list_video_path);
                int i2 = i - 1;
                intent.putExtra("asset_path", HomeActivity.this.assets_playlist.get(i2));
                intent.putExtra("current_pos", i2);
                HomeActivity.this.startActivity(intent);
            } else if (j > 400) {
                HomeActivity.this.open_gallery = true;
                HomeActivity.this.load_data = false;

                if(Build.VERSION.SDK_INT >=33){
                    HomeActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), HomeActivity.PICK_IMAGE_REQUEST);

                } else if (HomeActivity.this.hasStoragePermission("android.permission.WRITE_EXTERNAL_STORAGE") || HomeActivity.this.hasStoragePermission("android.permission.READ_EXTERNAL_STORAGE")) {
                    HomeActivity.this.startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), HomeActivity.PICK_IMAGE_REQUEST);
                } else if (Build.VERSION.SDK_INT > 29) {
                    HomeActivity HomeActivity = com.demo.livwllpaper.activities.HomeActivity.this;
                    HomeActivity.requestmyPermission(HomeActivity.RC_STORAGE_PERM, "android.permission.WRITE_EXTERNAL_STORAGE");
                } else {
                    HomeActivity HomeActivity = HomeActivity.this;
                    HomeActivity.requestmyPermission(HomeActivity.RC_STORAGE_PERM, "android.permission.WRITE_EXTERNAL_STORAGE");
                }
            }
        }
    };

   onPreviewClicked LPMLWon_preview_clicked = new onPreviewClicked() {
        @Override
        public void on_item_clicked(String str, int i) {
            Utils.perform_Action = 0;
            Intent intent = new Intent(HomeActivity.this, PreviewActivity.class);
            intent.putExtra("video_path", str);
            intent.putParcelableArrayListExtra("project_list", (ArrayList) HomeActivity.this.listProjects);
            intent.putExtra("position", i);
            intent.putExtra("from_java_activity", "from_hm");
            HomeActivity.this.startActivity(intent);
            HomeActivity.this.finish();
        }

        @Override
        public void on_item_clicked(String str, List<Project> list, int i) {
            Utils.perform_Action = 0;
            Intent intent = new Intent(HomeActivity.this, PreviewActivity.class);
            intent.putExtra("video_path", str);
            intent.putParcelableArrayListExtra("project_list", (ArrayList) list);
            intent.putExtra("position", i);
            intent.putExtra("from_java_activity", "from_hm");
            HomeActivity.this.startActivity(intent);
            HomeActivity.this.finish();
        }
    };




    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_home_modified);

        AdAdmob adAdmob = new AdAdmob( this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.banner), this);


        try {
            this.rv_listing_assets = (RecyclerView) findViewById(R.id.rv_listing_assets);
            this.rv_creation_list = (RecyclerView) findViewById(R.id.rv_creation_list);
            this.tv_mycreation_title_hm = (TextView) findViewById(R.id.tv_mycreation_title_hm);
            this.cv_my_creation_home_screen = (TextView) findViewById(R.id.cv_my_creation_home_screen);
            this.tv_mycreation_count = (TextView) findViewById(R.id.tv_mycreation_count);
            this.ll_creation_empty = (LinearLayout) findViewById(R.id.ll_creation_empty);
            this.iv_empty_creation = (LinearLayout) findViewById(R.id.iv_empty_creation);
            this.ll_creation_notempty = (LinearLayout) findViewById(R.id.ll_creation_notempty);
            this.tv_header_home_screen = (TextView) findViewById(R.id.tv_header_home_screen);
            this.iv_more = (ImageView) findViewById(R.id.iv_more);
            this.ll_for_menu = (LinearLayout) findViewById(R.id.ll_for_menu);
            this.frame_layout_loader = (FrameLayout) findViewById(R.id.frame_layout_loader);
            this.iv_loader = (ImageView) findViewById(R.id.iv_loader);
            this.iv_tutorial_home_screen = (ImageView) findViewById(R.id.iv_tutorial_home_screen);
            String str = "content://" + getPackageName() + "/asset_1.mp4";
            String str2 = "content://" + getPackageName() + "/asset_2.mp4";
            String str3 = "content://" + getPackageName() + "/asset_3.mp4";
            String str4 = "content://" + getPackageName() + "/asset_4.mp4";
            String str5 = "content://" + getPackageName() + "/asset_5.mp4";
            String str6 = "content://" + getPackageName() + "/asset_6.mp4";
            String str7 = "content://" + getPackageName() + "/asset_7.mp4";
            String str8 = "content://" + getPackageName() + "/asset_8.mp4";
            String str9 = "content://" + getPackageName() + "/asset_9.mp4";
            this.list_images = new ArrayList<>();
            this.list_video_path = new ArrayList<>();
            this.assets_playlist = new ArrayList<>();
            String[] names = getNames("wallpapers");
            int length = names.length;
            int i = 0;
            while (i < length) {
                this.list_images.add(names[i]);
                i++;
                length = length;
                names = names;
            }
            this.list_video_path.addAll(Arrays.asList(LPMLWApplication.INTERNAL_WALLPAPER_VIDEO_PATH, "asset_2.mp4", "asset_3.mp4", "asset_4.mp4", "asset_5.mp4", "asset_6.mp4", "asset_7.mp4", "asset_8.mp4", "asset_9.mp4"));
            this.assets_playlist.addAll(Arrays.asList(str, str2, str3, str4, str5, str6, str7, str8, str9));
            this.rv_listing_assets.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (HomeActivity.this.rv_listing_assets.getMeasuredWidth() > 0) {
                        HomeActivity.this.rv_listing_assets.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        int measuredWidth = HomeActivity.this.rv_listing_assets.getMeasuredWidth();
                        int measuredHeight = HomeActivity.this.rv_listing_assets.getMeasuredHeight();
                        HomeActivity.this.rv_listing_assets.setLayoutManager(new LinearLayoutManager(HomeActivity.this, RecyclerView.HORIZONTAL, false));
                        HomeActivity HomeActivity = com.demo.livwllpaper.activities.HomeActivity.this;
                        com.demo.livwllpaper.activities.HomeActivity HomeActivity2 = com.demo.livwllpaper.activities.HomeActivity.this;
                        HomeActivity.LPMLW_wallpaperAdapter = new WallpaperAdapter(HomeActivity2, HomeActivity2.list_images, com.demo.livwllpaper.activities.HomeActivity.this.LPMLW_wallpapers_clicked, measuredWidth, measuredHeight);
                        com.demo.livwllpaper.activities.HomeActivity.this.rv_listing_assets.setAdapter(com.demo.livwllpaper.activities.HomeActivity.this.LPMLW_wallpaperAdapter);
                    }
                }
            });
            this.cv_my_creation_home_screen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HomeActivity.this.open_gallery = false;
                    HomeActivity.this.load_data = false;
                    if (HomeActivity.this.hasStoragePermission("android.permission.WRITE_EXTERNAL_STORAGE") || HomeActivity.this.hasStoragePermission("android.permission.READ_EXTERNAL_STORAGE")) {
                        new Async_for_loading().execute(new String[0]);
                    } else if (Build.VERSION.SDK_INT > 29) {
                        HomeActivity HomeActivity = com.demo.livwllpaper.activities.HomeActivity.this;
                        HomeActivity.requestmyPermission(HomeActivity.RC_STORAGE_PERM, "android.permission.WRITE_EXTERNAL_STORAGE");
                    } else {
                        HomeActivity HomeActivity = HomeActivity.this;
                        HomeActivity.requestmyPermission(HomeActivity.RC_STORAGE_PERM, "android.permission.WRITE_EXTERNAL_STORAGE");
                    }
                }
            });
            this.iv_tutorial_home_screen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HomeActivity.this.startActivity(new Intent(HomeActivity.this, TutorialActivity.class));
                    HomeActivity.this.finish();
                }
            });
            this.iv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HomeActivity.this.pwindow = new PopupWindow(HomeActivity.this);
                    HomeActivity.this.pwindow.setBackgroundDrawable(new BitmapDrawable());
                    View inflate = HomeActivity.this.getLayoutInflater().inflate(R.layout.popup_hm, (ViewGroup) null);
                    HomeActivity.this.pwindow.setContentView(inflate);
                    LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.ll_mainpopup);
                    HomeActivity.this.getWindow().setStatusBarColor(HomeActivity.this.getResources().getColor(R.color.popup_bg));
                    HomeActivity.this.pwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            HomeActivity.this.getWindow().setStatusBarColor(HomeActivity.this.getResources().getColor(R.color.colorPrimaryDark));
                        }
                    });
                    linearLayout.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view2, MotionEvent motionEvent) {
                            HomeActivity.this.pwindow.dismiss();
                            return true;
                        }
                    });
                    linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view2) {
                            HomeActivity.this.pwindow.dismiss();
                        }
                    });
                    ((LinearLayout) inflate.findViewById(R.id.llSetting)).setOnClickListener(HomeActivity.this);
                    ((LinearLayout) inflate.findViewById(R.id.llRate)).setOnClickListener(HomeActivity.this);
                    ((LinearLayout) inflate.findViewById(R.id.llPrivacy)).setOnClickListener(HomeActivity.this);
                    HomeActivity.this.pwindow.setFocusable(true);
                    HomeActivity.this.pwindow.setWindowLayoutMode(-1, -1);
                    HomeActivity.this.pwindow.setOutsideTouchable(true);
                    HomeActivity.this.pwindow.showAsDropDown(view, 0, 0);
                }
            });
        } catch (Exception unused) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(HomeActivity.this, "Insufficient memory", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public String[] getNames(String str) {
        String[] strArr;
        try {
            strArr = getResources().getAssets().list(str);
        } catch (IOException e) {
            e.printStackTrace();
            strArr = null;
        }
        for (int i = 0; i < strArr.length; i++) {
            strArr[i] = str + "/" + strArr[i];
        }
        return strArr;
    }

    public void load_data_from_mycreation() {
        this.listItems = new ArrayList<>();
        ArrayList<VideoFileModel> fileslist = getFileslist();
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        this.db = databaseHandler;
        this.listProjects = databaseHandler.get_All_Projects();
        if (fileslist.size() == 0) {
            this.ll_creation_empty.setVisibility(View.VISIBLE);
            this.ll_creation_notempty.setVisibility(View.GONE);
            this.tv_mycreation_count.setVisibility(View.GONE);
            set_view_of_imageview();
            return;
        }
        for (int i = 0; i < fileslist.size(); i++) {
            this.listItems.add(fileslist.get(i).getPath());
        }
        this.ll_creation_empty.setVisibility(View.GONE);
        this.ll_creation_notempty.setVisibility(View.VISIBLE);
        this.tv_mycreation_count.setVisibility(View.VISIBLE);
        TextView textView = this.tv_mycreation_count;
        textView.setText(String.valueOf("(" + fileslist.size() + ")"));
        if (fileslist.size() > 3) {
            this.cv_my_creation_home_screen.setVisibility(View.VISIBLE);
        } else {
            this.cv_my_creation_home_screen.setVisibility(View.GONE);
        }
        this.rv_creation_list.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (HomeActivity.this.rv_creation_list.getMeasuredWidth() > 0) {
                    HomeActivity.this.rv_creation_list.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int measuredWidth = HomeActivity.this.rv_creation_list.getMeasuredWidth();
                    int measuredHeight = HomeActivity.this.rv_creation_list.getMeasuredHeight();
                    HomeActivity.this.rv_creation_list.setLayoutManager(new LinearLayoutManager(HomeActivity.this, RecyclerView.HORIZONTAL, false));
                    HomeActivity HomeActivity = com.demo.livwllpaper.activities.HomeActivity.this;
                    com.demo.livwllpaper.activities.HomeActivity HomeActivity2 = com.demo.livwllpaper.activities.HomeActivity.this;


                    HomeActivity.adapter_imageshm = new Whomescreenadapter(HomeActivity2, HomeActivity2.listItems, measuredWidth, measuredHeight, com.demo.livwllpaper.activities.HomeActivity.this.LPMLWon_preview_clicked);
                    com.demo.livwllpaper.activities.HomeActivity.this.rv_creation_list.setAdapter(com.demo.livwllpaper.activities.HomeActivity.this.adapter_imageshm);
                }
            }
        });
    }
    public void set_view_of_imageview() {
        this.ll_creation_empty.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (HomeActivity.this.ll_creation_empty.getMeasuredWidth() > 0) {
                    HomeActivity.this.ll_creation_empty.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    HomeActivity.this.iv_empty_creation.setLayoutParams(new LinearLayout.LayoutParams(HomeActivity.this.ll_creation_empty.getMeasuredWidth() / 3, (int) (((double) HomeActivity.this.ll_creation_empty.getMeasuredHeight()) / 1.1d)));
                }
            }
        });
    }

    public String get_old_path() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + getResources().getString(R.string.project_folder);
    }

    public ArrayList<VideoFileModel> getFileslist() {
        String str = get_old_path();
        File file = new File(str);
        if (!file.exists()) {
            file.mkdirs();
        }
        ArrayList<VideoFileModel> arrayList = new ArrayList<>();
        File file2 = new File(str);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        File[] listFiles = file2.listFiles();
        String[] list = file2.list();
        if (!(listFiles == null || list == null)) {
            for (File file3 : listFiles) {
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                try {
                    mediaMetadataRetriever.setDataSource(getApplicationContext(), Uri.fromFile(file3));
                    String extractMetadata = mediaMetadataRetriever.extractMetadata(17);
                    if (!extractMetadata.equalsIgnoreCase("") && !extractMetadata.equalsIgnoreCase(null)) {
                        arrayList.add(new VideoFileModel(file3, new Date(file3.lastModified())));
                    }
                } catch (Exception unused) {
                }
            }
        }
        if (arrayList.size() > 0) {
            Collections.sort(arrayList, new Comparator<VideoFileModel>() {
                public int compare(VideoFileModel VideoFileModel, VideoFileModel VideoFileModel2) {
                    return VideoFileModel2.getLast_modify().compareTo(VideoFileModel.getLast_modify());
                }
            });
        }
        return arrayList;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        this.pwindow.dismiss();
        switch (id) {
            case R.id.llPrivacy :
                startActivity(new Intent(this,PrivacyPolicyActivity.class));
                finish();
                return;
            case R.id.llRate :
                rateusDailogeMsg();
                return;
            case R.id.llSetting :
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.SUBJECT", getString(R.string.app_name));
                intent.putExtra("android.intent.extra.TEXT", "Download " + getString(R.string.app_name) + " app from   - https://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(Intent.createChooser(intent, "Share Application"));
                return;
            default:
                return;
        }
    }

    @SuppressLint("ResourceType")
    public void rateusDailogeMsg() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.rate_us_layout_hm);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
            }
        });
        ((TextView) dialog.findViewById(R.id.never)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ((TextView) dialog.findViewById(R.id.rate_now)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity HomeActivity = com.demo.livwllpaper.activities.HomeActivity.this;
                HomeActivity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + com.demo.livwllpaper.activities.HomeActivity.this.getPackageName())));
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.frame_layout_loader.getVisibility() == View.VISIBLE) {
            this.frame_layout_loader.setVisibility(View.GONE);
        }
        init_view();
    }

    public void init_view() {

        if(Build.VERSION.SDK_INT >= 33){
            load_data_from_mycreation();
            return;
        }

        if (Build.VERSION.SDK_INT >= 33) {
            load_data_from_mycreation();
            return;
        } else if (hasStoragePermission("android.permission.WRITE_EXTERNAL_STORAGE") || hasStoragePermission("android.permission.READ_EXTERNAL_STORAGE")) {
            load_data_from_mycreation();
            return;
        }
        this.load_data = true;
        if (Build.VERSION.SDK_INT > 29) {
            requestmyPermission(this.RC_STORAGE_PERM, "android.permission.WRITE_EXTERNAL_STORAGE");
        } else {
            requestmyPermission(this.RC_STORAGE_PERM, "android.permission.WRITE_EXTERNAL_STORAGE");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.frame_layout_loader.getVisibility() == View.VISIBLE) {
            this.frame_layout_loader.setVisibility(View.GONE);
        }
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
        } else if (i == PICK_IMAGE_REQUEST) {
            if (i2 == -1) {
                Uri data = intent.getData();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                Utils.width = displayMetrics.widthPixels;
                Utils.height = displayMetrics.heightPixels;
                CropImage.activity(data).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(Utils.width, Utils.height).start(this);
            }
        } else if (i != 1 && i == 203) {
            CropImage.ActivityResult activityResult = CropImage.getActivityResult(intent);
            if (i2 == -1) {
                Uri uri = activityResult.getUri();
                Intent intent2 = new Intent(this, Exportactivity.class);
                intent2.putExtra(Project.COLUMN_URI, uri.toString());
                intent2.putExtra("From_Main_Activity", "yes");
                startActivity(intent2);
                finish();
            }
        }
    }

    public boolean hasStoragePermission(String... strArr) {
        return EasyPermissions.hasPermissions(this, strArr);
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (String str : strArr) {
            if (EasyPermissions.hasPermissions(this, str)) {
                arrayList.add(str);
            } else {
                arrayList2.add(str);
            }
        }
        arrayList2.size();
        if (EasyPermissions.somePermissionPermanentlyDenied(this, arrayList2)) {
            //new AppSettingsDialog.Builder(this).build().show();
        } else {
            EasyPermissions.onRequestPermissionsResult(i, strArr, iArr, this);
        }
    }

    @Override
    public void onPermissionsGranted(int i, List<String> list) {
        if (this.load_data) {
            init_view();
            this.load_data = false;
        } else if (this.open_gallery) {
            startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), PICK_IMAGE_REQUEST);
            this.open_gallery = false;
            this.load_data = false;
        } else {
            new Async_for_loading().execute(new String[0]);
            this.open_gallery = false;
            this.load_data = false;
        }
    }

    @Override
    public void onPermissionsDenied(int i, List<String> list) {
        this.open_gallery = false;
        this.load_data = false;
        if (EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
           // new AppSettingsDialog.Builder(this).build().show();
        }
    }

    public void requestmyPermission(int i, String... strArr) {
        EasyPermissions.requestPermissions(this, "This needs permission to access", i, strArr);
    }


    public class Async_for_loading extends AsyncTask<String, String, String> {
        public String doInBackground(String... strArr) {
            return null;
        }

        public Async_for_loading() {
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            HomeActivity.this.frame_layout_loader.setVisibility(View.VISIBLE);
        }

        public void onPostExecute(String str) {
            super.onPostExecute( str);
            HomeActivity.this.startActivity(new Intent(HomeActivity.this, CreationActivity.class));
            HomeActivity.this.finish();
        }
    }
}