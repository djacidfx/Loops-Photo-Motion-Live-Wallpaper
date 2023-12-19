package com.demo.livwllpaper.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.livwllpaper.R;
import com.demo.livwllpaper.Utilsx.Utils;
import com.demo.livwllpaper.beans.Project;
import com.demo.livwllpaper.controllers.LPMLWVideoSaver;
import com.demo.livwllpaper.databases.DatabaseHandler;
import java.io.File;
import java.text.NumberFormat;


public class SaveActivity extends AppCompatActivity {
    public static String INTENT_Project = "LPMLWProject";
    public static int MAX_RESOLUTION_SAVE = 1080;
    public static int MAX_RESOLUTION_SAVE_FREE = (600 + 1080) / 2;
    public static int MIN_RESOLUTION_SAVE = 600;
    private Project LPMLWProjectToSave;
    private LPMLWVideoSaver LPMLWVideoSaver;
    private float Original_resolution;
    private Button btn_save_with_logo;
    private Button btn_save_without_logo;
    ImageView iv_bitmap;
    private NumberFormat nf;
    private SeekBar seek_Resolution;
    private SeekBar seek_Time;
    private TextView tv_Save_Time;
    private TextView tv_status_loaded;
    private TextView tx_Resolution;
    private DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this);
    private boolean tentouVerPropaganda = false;

    
    @Override 
    public void onCreate(Bundle bundle) {
        LPMLWVideoSaver lPMLWVideoSaver;
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.activity_save);



        this.tv_status_loaded = (TextView) findViewById(R.id.tv_status_loaded);
        this.iv_bitmap = (ImageView) findViewById(R.id.iv_bitmap);
        getResources().getDisplayMetrics();
        Project lPMLWProject = (Project) getIntent().getParcelableExtra(INTENT_Project);
        this.LPMLWProjectToSave = lPMLWProject;
        Project project = this.databaseHandler.getProject(lPMLWProject.getId());
        this.LPMLWProjectToSave = project;
        project.reloadBitmapUri(this, this.databaseHandler);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekbar_save_duration);
        this.seek_Time = seekBar;
        seekBar.setMax(8000);
        this.tv_Save_Time = (TextView) findViewById(R.id.tv_Resolution_Ideal_size);
        this.seek_Time.setOnSeekBarChangeListener(new duration_click_listener());
        NumberFormat instance = NumberFormat.getInstance();
        this.nf = instance;
        instance.setMaximumFractionDigits(1);
        this.LPMLWProjectToSave.get_Time_Save();
        SeekBar seekBar2 = (SeekBar) findViewById(R.id.seekbar_resolution_Save);
        this.seek_Resolution = seekBar2;
        seekBar2.setMax(MAX_RESOLUTION_SAVE - MIN_RESOLUTION_SAVE);
        this.tx_Resolution = (TextView) findViewById(R.id.tv_Resolution_Saved);
        this.seek_Resolution.setOnSeekBarChangeListener(new seekbar_resolution_change_listener());
        float min = (float) Math.min(Math.max(this.LPMLWProjectToSave.getHeight(), this.LPMLWProjectToSave.getWidth()), MAX_RESOLUTION_SAVE);
        this.Original_resolution = min;
        if (min % 2.0f != 0.0f) {
            min += 1.0f;
        }
        this.Original_resolution = min;
        int i = this.LPMLWProjectToSave.get_Save_Resolution();
        if (i > MAX_RESOLUTION_SAVE_FREE) {
            this.seek_Resolution.setProgress(1);
            this.seek_Resolution.setProgress(2);
            this.seek_Resolution.setProgress(i - MIN_RESOLUTION_SAVE);
            ((TextView) findViewById(R.id.tv_Resolution_Ideal_size)).setOnClickListener(new resolution_on_click_listener());
            Button button = (Button) findViewById(R.id.btn_save_without_logo);
            this.btn_save_without_logo = button;
            button.setOnClickListener(new View.OnClickListener() { 
                @Override 
                public void onClick(View view) {
                    SaveActivity.this.Save_video_in_gallery(false);
                }
            });
            Button button2 = (Button) findViewById(R.id.btn_save_with_logo);
            this.btn_save_with_logo = button2;
            button2.setOnClickListener(new View.OnClickListener() { 
                @Override 
                public void onClick(View view) {
                    SaveActivity.this.Save_video_in_gallery(true);
                }
            });
            setFinishOnTouchOutside(true);
            lPMLWVideoSaver = (LPMLWVideoSaver) getLastCustomNonConfigurationInstance();
        } else {
            this.seek_Resolution.setProgress(1);
            this.seek_Resolution.setProgress(2);
            this.seek_Resolution.setProgress(i - MIN_RESOLUTION_SAVE);
            ((TextView) findViewById(R.id.tv_Resolution_Ideal_size)).setOnClickListener(new resolution_on_click_listener());
            this.btn_save_without_logo = (Button) findViewById(R.id.btn_save_without_logo);
            this.btn_save_with_logo = (Button) findViewById(R.id.btn_save_with_logo);
            this.btn_save_without_logo.setOnClickListener(new View.OnClickListener() { 
                @Override 
                public void onClick(View view) {
                    SaveActivity.this.Save_video_in_gallery(false);
                }
            });
            this.btn_save_with_logo.setOnClickListener(new View.OnClickListener() { 
                @Override 
                public void onClick(View view) {
                    SaveActivity.this.Save_video_in_gallery(true);
                }
            });
            setFinishOnTouchOutside(true);
            lPMLWVideoSaver = (LPMLWVideoSaver) getLastCustomNonConfigurationInstance();
        }
        if (lPMLWVideoSaver != null) {
            this.LPMLWVideoSaver = lPMLWVideoSaver;
            lPMLWVideoSaver.setContext(this);
            if (this.LPMLWVideoSaver.is_Saving()) {
                setFinishOnTouchOutside(false);
                this.LPMLWVideoSaver.showProgress();
            }
        }
    }

    
    class duration_click_listener implements SeekBar.OnSeekBarChangeListener {
        @Override 
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        duration_click_listener() {
        }

        @Override 
        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            SaveActivity.this.LPMLWProjectToSave.set_Time_Save(10000 - Math.round((((float) i) / ((float) seekBar.getMax())) * 8000.0f));
            TextView textView = SaveActivity.this.tv_Save_Time;
            textView.setText(SaveActivity.this.nf.format((double) (((float) SaveActivity.this.LPMLWProjectToSave.get_Time_Save()) / 1000.0f)) + " " + SaveActivity.this.getResources().getString(R.string.text_save_time_seconds));
        }

        @Override 
        public void onStopTrackingTouch(SeekBar seekBar) {
            SaveActivity.this.LPMLWProjectToSave.updateProject(SaveActivity.this.databaseHandler);
        }
    }

    
    class seekbar_resolution_change_listener implements SeekBar.OnSeekBarChangeListener {
        @Override 
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        seekbar_resolution_change_listener() {
        }

        @Override 
        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            String str;
            int round = SaveActivity.MIN_RESOLUTION_SAVE + Math.round((((float) i) / ((float) seekBar.getMax())) * ((float) (SaveActivity.MAX_RESOLUTION_SAVE - SaveActivity.MIN_RESOLUTION_SAVE)));
            if (round % 2 != 0) {
                round++;
            }
            if (SaveActivity.this.LPMLWProjectToSave.getWidth() > SaveActivity.this.LPMLWProjectToSave.getHeight()) {
                str = round + "x" + Math.round(((float) round) * (((float) SaveActivity.this.LPMLWProjectToSave.getHeight()) / ((float) SaveActivity.this.LPMLWProjectToSave.getWidth())));
            } else {
                str = Math.round(((float) round) * (((float) SaveActivity.this.LPMLWProjectToSave.getWidth()) / ((float) SaveActivity.this.LPMLWProjectToSave.getHeight()))) + " x " + round;
            }
            SaveActivity.this.tx_Resolution.setText(str);
            SaveActivity.this.paint_Ideal_Text(round);
            SaveActivity.this.LPMLWProjectToSave.set_Resolution_Save(round);
        }

        @Override 
        public void onStopTrackingTouch(SeekBar seekBar) {
            SaveActivity.this.LPMLWProjectToSave.updateProject(SaveActivity.this.databaseHandler);
        }
    }

    
    class resolution_on_click_listener implements View.OnClickListener {
        resolution_on_click_listener() {
        }

        @Override 
        public void onClick(View view) {
            SaveActivity.this.seek_Resolution.setProgress(((int) SaveActivity.this.Original_resolution) - SaveActivity.MIN_RESOLUTION_SAVE);
        }
    }

    
    class save_video_listener implements LPMLWVideoSaver.VideoSaveListener {
        @Override 
        public void onSaving(int i) {
        }

        @Override 
        public void onStartSave(int i) {
        }

        save_video_listener() {
        }

        @Override 
        public void onSaved(File file) {
            SaveActivity.this.LPMLWVideoSaver = null;
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("video/*");
            intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(file));
            SaveActivity saveActivity = SaveActivity.this;
            saveActivity.startActivity(Intent.createChooser(intent, saveActivity.getResources().getString(R.string.Share_video)));
            SaveActivity.this.setFinishOnTouchOutside(true);
            SaveActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            Utils.scannFile(SaveActivity.this, file);
        }

        @Override 
        public void onError(String str) {
            SaveActivity.this.setFinishOnTouchOutside(true);
            SaveActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    
    public void paint_Ideal_Text(int i) {
        TextView textView = (TextView) findViewById(R.id.tv_Resolution_Ideal_size);
        if (i == ((int) this.Original_resolution)) {
            textView.setTextColor(Utils.getColor(this, R.color.colorPrimary));
        } else {
            textView.setTextColor(Utils.getColor(this, R.color.colorAccent));
        }
    }

    @Override 
    protected void onPause() {
        super.onPause();
    }

    @Override 
    public void onDestroy() {
        super.onDestroy();
    }

    @Override 
    protected void onResume() {
        super.onResume();
    }

    @Override 
    public Object onRetainCustomNonConfigurationInstance() {
        LPMLWVideoSaver lPMLWVideoSaver = this.LPMLWVideoSaver;
        if (lPMLWVideoSaver != null) {
            lPMLWVideoSaver.dismissProgress();
        }
        return this.LPMLWVideoSaver;
    }

    
    public void Save_video_in_gallery(boolean z) {
        setFinishOnTouchOutside(false);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getBitmap(R.drawable.gallery_selection);
    }

    private Bitmap getBitmap(int i) {
        Drawable drawable = getResources().getDrawable(i);
        Canvas canvas = new Canvas();
        Bitmap createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(createBitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return createBitmap;
    }
}
