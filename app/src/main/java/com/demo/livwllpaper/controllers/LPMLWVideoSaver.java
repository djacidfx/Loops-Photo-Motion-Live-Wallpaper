package com.demo.livwllpaper.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.text.SpannableString;

import com.demo.livwllpaper.R;
import com.demo.livwllpaper.Interfaces.Onvideodownloaded;
import com.demo.livwllpaper.Utilsx.Utils;
import com.demo.livwllpaper.beans.Point;
import com.demo.livwllpaper.beans.Project;
import com.demo.livwllpaper.delanauy.Delaunay;
import java.io.File;


public class LPMLWVideoSaver extends AsyncTask<Object, Integer, Object> {
    public static ProgressDialog dialog;
    Onvideodownloaded LPMLWOnvideodownloaded;
    private Project LPMLWProject;
    private Context context;
    private String path;
    private int resolution;
    private VideoSaveListener saveListener;
    private int totalFrames;
    private File videoFile;
    private boolean comLogo = true;
    private boolean saving = false;
    private int Animation_time = 2000;

    
    public interface VideoSaveListener {
        void onError(String str);

        void onSaved(File file);

        void onSaving(int i);

        void onStartSave(int i);
    }

    public LPMLWVideoSaver(Context context, Project lPMLWProject, Onvideodownloaded lPMLWOnvideodownloaded) {
        this.context = context;
        this.LPMLWProject = lPMLWProject;
        this.resolution = lPMLWProject.getWidth() > lPMLWProject.getHeight() ? lPMLWProject.getWidth() : lPMLWProject.getHeight();
        this.LPMLWOnvideodownloaded = lPMLWOnvideodownloaded;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean is_Saving() {
        return this.saving;
    }

    public void showProgress() {
        ProgressDialog progressDialog = dialog;
        if (progressDialog == null) {
            ProgressDialog progressDialog2 = new ProgressDialog(this.context);
            dialog = progressDialog2;
            progressDialog2.setProgressStyle(1);
            dialog.setCancelable(false);
            dialog.setProgress(0);
            dialog.setMax(100);
            dialog.setTitle(this.context.getResources().getString(R.string.save_video_pd));
            dialog.setCanceledOnTouchOutside(false);
        } else if (progressDialog != null) {
            ProgressDialog progressDialog3 = new ProgressDialog(this.context);
            dialog = progressDialog3;
            progressDialog3.setProgressStyle(1);
            dialog.setCancelable(false);
            dialog.setProgress(0);
            dialog.setMax(100);
            String string = this.context.getString(R.string.save_video_pd);
            new SpannableString(string).setSpan(Typeface.createFromAsset(this.context.getAssets(), "fira_sans_regular.ttf"), 0, string.length(), 33);
            dialog.setTitle(this.context.getResources().getString(R.string.save_video_pd));
            dialog.setCanceledOnTouchOutside(false);
        }
        dialog.show();
    }

    public void dismissProgress() {
        ProgressDialog progressDialog = dialog;
        if (progressDialog != null && progressDialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void set_Resolution(int i) {
        this.resolution = i;
    }

    public void set_Time_Animation(int i) {
        this.Animation_time = i;
    }

    public void set_with_Logo(boolean z) {
        this.comLogo = z;
    }

    public void setSaveListener(VideoSaveListener videoSaveListener) {
        this.saveListener = videoSaveListener;
    }

    @Override 
    protected void onPreExecute() {
        super.onPreExecute();
        int round = Math.round(((float) (this.Animation_time * 30)) / 1000.0f);
        this.totalFrames = round;
        VideoSaveListener videoSaveListener = this.saveListener;
        if (videoSaveListener != null) {
            videoSaveListener.onStartSave(round);
        }
        showProgress();
        this.saving = true;
    }

    @Override 
    protected Object doInBackground(Object... objArr) {
        this.path = (String) objArr[0];
        Bitmap bitmap = objArr.length > 1 ? (Bitmap) objArr[1] : null;
        File file = new File(this.path);
        this.videoFile = file;
        if (file.exists()) {
            this.videoFile.delete();
        }
        try {
            int round = this.LPMLWProject.getWidth() > this.LPMLWProject.getHeight() ? this.resolution : Math.round(((float) this.resolution) * (((float) this.LPMLWProject.getWidth()) / ((float) this.LPMLWProject.getHeight())));
            if (round % 2 != 0) {
                round++;
            }
            int round2 = this.LPMLWProject.getWidth() < this.LPMLWProject.getHeight() ? this.resolution : Math.round(((float) this.resolution) * (((float) this.LPMLWProject.getHeight()) / ((float) this.LPMLWProject.getWidth())));
            if (round2 % 2 != 0) {
                round2++;
            }
            MySequenceEncoder2 lPMLWMySequenceEncoder2 = new MySequenceEncoder2(this.videoFile, round, round2, 30, this.Animation_time);
            bitmap.getHeight();
            bitmap.getWidth();
            float f = (round > round2 ? (float) round : (float) round2) * 0.15f;
            if (f < 80.0f) {
                f = 80.0f;
            }
            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, 60, 60, true);
            Paint paint = new Paint();
            paint.setFilterBitmap(true);
            new Paint(1).setColor(0);
            Bitmap createScaledBitmap2 = Bitmap.createScaledBitmap(this.LPMLWProject.getImagem(), round, round2, true);
            float width = ((float) round) / ((float) this.LPMLWProject.getWidth());
            Bitmap createScaledBitmap3 = this.LPMLWProject.get_Mask() != null ? Bitmap.createScaledBitmap(this.LPMLWProject.get_Mask(), Math.round(((float) this.LPMLWProject.get_Mask().getWidth()) * width), Math.round(((float) this.LPMLWProject.get_Mask().getHeight()) * width), true) : null;
            Delaunay lPMLWDelaunay = new Delaunay(Utils.getImage_without_Mask(createScaledBitmap2, createScaledBitmap3, Bitmap.Config.ARGB_8888));
            for (Point lPMLWPoint : this.LPMLWProject.get_Points_List()) {
                lPMLWDelaunay.add_point(lPMLWPoint.get_Copy(width));
            }
            lPMLWDelaunay.setImagemDelaunay(Utils.getImage_without_Mask(createScaledBitmap2, createScaledBitmap3, Bitmap.Config.ARGB_8888));
            Bitmap createBitmap = Bitmap.createBitmap(lPMLWDelaunay.get_Static_Triangles(Bitmap.Config.ARGB_8888));
            new Canvas(createBitmap).drawBitmap(Utils.get_masked_image(createScaledBitmap2, createScaledBitmap3, Bitmap.Config.ARGB_8888), 0.0f, 0.0f, (Paint) null);
            Paint paint2 = new Paint(1);
            paint2.setFilterBitmap(true);
            paint2.setAntiAlias(true);
            Bitmap createBitmap2 = Bitmap.createBitmap(round, round2, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap2);
            canvas.drawBitmap(createScaledBitmap2, 0.0f, 0.0f, (Paint) null);
            for (int i = 0; i < this.totalFrames; i++) {
                float f2 = (((float) i) * 1000.0f) / 30.0f;
                Bitmap bitmap2 = FrameController.getInstance().get_Frame_On_Move(lPMLWDelaunay, this.Animation_time, 30, f2, Bitmap.Config.ARGB_8888);
                int i2 = this.Animation_time;
                float f3 = ((((float) i2) / 2.0f) + f2) % ((float) i2);
                Bitmap bitmap3 = FrameController.getInstance().get_Frame_On_Move(lPMLWDelaunay, this.Animation_time, 30, f3, Bitmap.Config.ARGB_8888);
                paint2.setAlpha(FrameController.getInstance().getAlpha(this.Animation_time, f2));
                canvas.drawBitmap(bitmap2, 0.0f, 0.0f, (Paint) null);
                paint2.setAlpha(FrameController.getInstance().getAlpha(this.Animation_time, f3));
                canvas.drawBitmap(bitmap3, 0.0f, 0.0f, paint2);
                canvas.drawBitmap(createBitmap, 0.0f, 0.0f, (Paint) null);
                if (this.comLogo && createScaledBitmap != null) {
                    float f4 = 0.1f * f;
                    canvas.drawBitmap(createScaledBitmap, f4, f4, paint);
                }
                try {
                    lPMLWMySequenceEncoder2.encodeFrame(createBitmap2);
                } catch (Exception unused) {
                }
                publishProgress(Integer.valueOf(i), Integer.valueOf(this.totalFrames));
            }
            lPMLWMySequenceEncoder2.finish();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            VideoSaveListener videoSaveListener = this.saveListener;
            if (videoSaveListener == null) {
                return null;
            }
            videoSaveListener.onError(e.getMessage());
            cancel(true);
            return null;
        }
    }

    @Override 
    protected void onCancelled() {
        super.onCancelled();
        this.saving = true;
        VideoSaveListener videoSaveListener = this.saveListener;
        if (videoSaveListener != null) {
            videoSaveListener.onError("Cancelled");
        }
        ProgressDialog progressDialog = dialog;
        if (progressDialog != null && progressDialog.isShowing()) {
            dialog.dismiss();
        }
    }

    
    public void onProgressUpdate(Integer... numArr) {
        super.onProgressUpdate( numArr);
        ProgressDialog progressDialog = dialog;
        if (progressDialog != null) {
            progressDialog.setProgress(Math.round((((float) (numArr[0].intValue() + 1)) / ((float) this.totalFrames)) * 100.0f));
        }
        VideoSaveListener videoSaveListener = this.saveListener;
        if (videoSaveListener != null) {
            videoSaveListener.onSaving(numArr[0].intValue() + 1);
        }
    }

    @Override 
    protected void onPostExecute(Object obj) {
        super.onPostExecute(obj);
        ProgressDialog progressDialog = dialog;
        if (progressDialog != null && progressDialog.isShowing()) {
            dialog.dismiss();
        }
        dialog.getWindow().clearFlags(16);
        MediaScannerConnection.scanFile(this.context, new String[]{this.videoFile.getAbsolutePath()}, new String[]{"video/*"}, null);
        this.LPMLWOnvideodownloaded.on_downloaded(this.videoFile.getAbsolutePath());
    }
}
