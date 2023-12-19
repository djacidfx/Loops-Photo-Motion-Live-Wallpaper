package com.demo.livwllpaper.activities;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.demo.livwllpaper.R;
import com.demo.livwllpaper.Interfaces.onPreviewClicked;
import com.demo.livwllpaper.Utilsx.StorageUtils;
import com.demo.livwllpaper.Utilsx.Utils;
import com.demo.livwllpaper.adapters.Creationadapter;
import com.demo.livwllpaper.beans.Project;
import com.demo.livwllpaper.databases.DatabaseHandler;
import com.demo.livwllpaper.models.VideoFileModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
public class CreationActivity extends AppCompatActivity {
    public static Creationadapter adapter;
    public static LinearLayout iv_multiple_delete;
    public static TextView tv_count;
    public static ImageView unselectall;
    onPreviewClicked LPMLWon_preview_clicked = new onPreviewClicked() { 
        @Override 
        public void on_item_clicked(String str, List<Project> list, int i) {
        }

        @Override 
        public void on_item_clicked(String str, int i) {
            Utils.perform_Action = 0;
            Intent intent = new Intent(CreationActivity.this, PreviewActivity.class);
            intent.putExtra("video_path", str);
            intent.putParcelableArrayListExtra("project_list", (ArrayList) CreationActivity.this.listLPMLWProjects);
            intent.putExtra("position", i);
            intent.putExtra("from_java_activity", "no");
            CreationActivity.this.startActivity(intent);
            CreationActivity.this.finish();
        }
    };
    private DatabaseHandler db;
    ArrayList<String> listItems;
    private List<Project> listLPMLWProjects;
    public LinearLayout ll_back_btn;
    public LinearLayout ll_loading_albums;
    private LinearLayout ll_not_empty;
    private LinearLayout llnotfound;
    private RecyclerView mGridView;

    


    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_display_creation);
        this.ll_back_btn = (LinearLayout) findViewById(R.id.ll_back_btn);
        this.ll_loading_albums = (LinearLayout) findViewById(R.id.tv_loading_albums);
        this.ll_not_empty = (LinearLayout) findViewById(R.id.ll_not_empty);
        iv_multiple_delete = (LinearLayout) findViewById(R.id.iv_multiple_delete);
        tv_count = (TextView) findViewById(R.id.tv_count);
        unselectall = (ImageView) findViewById(R.id.unselectall);
        this.mGridView = (RecyclerView) findViewById(R.id.gridview);
        this.llnotfound = (LinearLayout) findViewById(R.id.llnotfound);
        this.ll_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                CreationActivity.this.onBackPressed();
            }
        });
        unselectall.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                if (Creationadapter.delete_video_list == null) {
                    return;
                }
                if (Creationadapter.delete_video_list.size() == CreationActivity.this.listItems.size()) {
                    CreationActivity.hide_delete_icon();
                    CreationActivity.adapter.unselect_all();
                    CreationActivity.unselectall.setImageResource(R.drawable.iv_uncheck);
                    return;
                }
                CreationActivity.adapter.select_all();
                CreationActivity.unselectall.setImageResource(R.drawable.iv_check);
            }
        });
        iv_multiple_delete.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                if (Creationadapter.delete_video_list != null && Creationadapter.delete_video_list.size() > 0) {
                    CreationActivity lPMLWCreationActivity = CreationActivity.this;
                    lPMLWCreationActivity.show_deleteDialog(lPMLWCreationActivity);
                }
            }
        });
    }

    @Override 
    protected void onResume() {
        if (Creationadapter.delete_video_list == null) {
            new Fetch_Video_Of_Folder().execute(new Void[0]);
        } else if (Creationadapter.delete_video_list.size() <= 0) {
            new Fetch_Video_Of_Folder().execute(new Void[0]);
        }
        super.onResume();
    }

    
    public class Fetch_Video_Of_Folder extends AsyncTask<Void, Void, ArrayList<VideoFileModel>> {

        @Override 
        public void onPreExecute() {
            CreationActivity.this.ll_loading_albums.setVisibility(View.VISIBLE);
            CreationActivity.this.ll_not_empty.setVisibility(View.GONE);
            super.onPreExecute();
        }

        public ArrayList<VideoFileModel> doInBackground(Void... voidArr) {
            try {
                CreationActivity.this.db = new DatabaseHandler(CreationActivity.this);
                CreationActivity lPMLWCreationActivity = CreationActivity.this;
                lPMLWCreationActivity.listLPMLWProjects = lPMLWCreationActivity.db.get_All_Projects();
                return CreationActivity.this.getFileslist();
            } catch (Exception unused) {
                return null;
            }
        }

        public void onPostExecute(ArrayList<VideoFileModel> arrayList) {
            CreationActivity.this.listItems = new ArrayList<>();
            for (int i = 0; i < arrayList.size(); i++) {
                CreationActivity.this.listItems.add(arrayList.get(i).getPath());
            }
            if (arrayList.size() > 0) {
                CreationActivity.this.ll_loading_albums.setVisibility(View.GONE);
                CreationActivity.this.ll_not_empty.setVisibility(View.VISIBLE);
                CreationActivity.this.mGridView.setLayoutManager(new GridLayoutManager(CreationActivity.this, 3));
                CreationActivity.this.mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { 
                    @Override 
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT < 16) {
                            CreationActivity.this.mGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            CreationActivity.this.mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        int measuredWidth = CreationActivity.this.mGridView.getMeasuredWidth();
                        CreationActivity.this.mGridView.getMeasuredHeight();
                        CreationActivity.adapter = new Creationadapter(CreationActivity.this, CreationActivity.this.listItems, measuredWidth, CreationActivity.this.LPMLWon_preview_clicked);
                        CreationActivity.this.llnotfound.setVisibility(View.GONE);
                        CreationActivity.this.mGridView.setAdapter(CreationActivity.adapter);
                    }
                });
                return;
            }
            CreationActivity.this.ll_loading_albums.setVisibility(View.GONE);
            CreationActivity.this.ll_not_empty.setVisibility(View.GONE);
            CreationActivity.this.llnotfound.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("ResourceType")
    public void show_deleteDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.getWindow().setLayout(-1, -2);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        TextView textView = (TextView) dialog.findViewById(R.id.tv_title);
        TextView textView2 = (TextView) dialog.findViewById(R.id.txt2);
        TextView textView3 = (TextView) dialog.findViewById(R.id.tv_title_delete);
        LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.btn_no);
        if (Creationadapter.delete_video_list != null) {
            if (Creationadapter.delete_video_list.size() > 1) {
                textView.setText("Delete Videos ");
                textView2.setText("Do you want to delete " + Creationadapter.delete_video_list.size() + " videos ?");
            } else {
                textView.setText("Delete Video ");
                textView2.setText("Do you want to delete " + Creationadapter.delete_video_list.size() + " video ?");
            }
        }
        textView3.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                if (Creationadapter.delete_video_list != null && Creationadapter.delete_video_list.size() > 0) {
                    CreationActivity lPMLWCreationActivity = CreationActivity.this;
                    new Delete_Task(lPMLWCreationActivity).execute(new String[0]);
                }
                dialog.dismiss();
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    
    public class Delete_Task extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog dialog;

        public Delete_Task(Context context) {
            this.dialog = new ProgressDialog(context);
        }

        @Override 
        public void onPreExecute() {
            this.dialog.setMessage("Deleting Files ");
            this.dialog.show();
        }

        public Boolean doInBackground(String... strArr) {
            try {
                if (CreationActivity.this.listLPMLWProjects.size() > 0) {
                    for (int i = 0; i < CreationActivity.this.listLPMLWProjects.size(); i++) {
                        Project lPMLWProject = (Project) CreationActivity.this.listLPMLWProjects.get(i);
                        CreationActivity lPMLWCreationActivity = CreationActivity.this;
                        lPMLWProject.reloadBitmapUri(lPMLWCreationActivity, lPMLWCreationActivity.db);
                        if (Creationadapter.delete_video_list.size() == CreationActivity.this.listItems.size()) {
                            for (int i2 = 0; i2 < Creationadapter.delete_video_list.size(); i2++) {
                                CreationActivity.this.delete_position(i2, lPMLWProject);
                            }
                        } else {
                            for (int i3 = 0; i3 < Creationadapter.delete_video_list.size(); i3++) {
                                CreationActivity.this.delete_position(i3, lPMLWProject);
                            }
                        }
                    }
                }
                return true;
            } catch (Exception unused) {
                return false;
            }
        }

        public void onPostExecute(Boolean bool) {
            Creationadapter.delete_video_list.clear();
            CreationActivity.hide_delete_icon();
            new Fetch_Video_Of_Folder().execute(new Void[0]);
            if (!bool.booleanValue()) {
                Toast.makeText(CreationActivity.this.getApplicationContext(), "Error ", Toast.LENGTH_LONG).show();
            }
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
        }
    }

    public void delete_position(int i, Project lPMLWProject) {
        String str = Creationadapter.delete_video_list.get(i);
        String substring = str.substring(str.lastIndexOf("/") + 1);
        if (lPMLWProject.get_Title().equalsIgnoreCase(substring.substring(0, substring.lastIndexOf(".")))) {
            lPMLWProject.deleteProject(this.db);
            File file = new File(str);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public static void show_delete_icon() {
        iv_multiple_delete.setVisibility(View.VISIBLE);
        unselectall.setVisibility(View.VISIBLE);
        tv_count.setVisibility(View.VISIBLE);
        TextView textView = tv_count;
        textView.setText("( " + String.valueOf(Creationadapter.delete_video_list.size()) + " )");
    }

    public static void set_selectall_icon() {
        unselectall.setImageResource(R.drawable.iv_check);
        TextView textView = tv_count;
        textView.setText("( " + String.valueOf(Creationadapter.delete_video_list.size()) + " )");
    }

    public static void unset_selectall_icon() {
        unselectall.setImageResource(R.drawable.iv_uncheck);
    }

    public static void hide_delete_icon() {
        iv_multiple_delete.setVisibility(View.GONE);
        unselectall.setVisibility(View.GONE);
        tv_count.setVisibility(View.GONE);
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
                public int compare(VideoFileModel lPMLWVideoFileModel, VideoFileModel lPMLWVideoFileModel2) {
                    return lPMLWVideoFileModel2.getLast_modify().compareTo(lPMLWVideoFileModel.getLast_modify());
                }
            });
        }
        return arrayList;
    }

    @Override 
    public void onBackPressed() {
        if (Creationadapter.delete_video_list == null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else if (Creationadapter.delete_video_list.size() > 0) {
            Creationadapter.delete_video_list.clear();
            hide_delete_icon();
            Creationadapter lPMLWCreationadapter = adapter;
            if (lPMLWCreationadapter != null) {
                lPMLWCreationadapter.unselect_all();
                unselectall.setImageResource(R.drawable.iv_uncheck);
            }
        } else {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }
}
