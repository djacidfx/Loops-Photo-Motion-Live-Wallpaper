package com.demo.livwllpaper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.demo.livwllpaper.Interfaces.OnRateusClicked;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class Appraterclass {
    public static int DAYS_UNTIL_PROMPT = 1;
    public static final int MAX_NEVER_PROMPT = 2;
    public static final int MAX_RATE_PROMPT = 2;
    public static final int MAX_REMIND_PROMPT = 5;
    public static Context context;
    public static Long first_launch_date_time;
    public static Long launch_date_time;
    public static int never_count;
    public static int rate_count;
    public static int total_launch_count;
    OnRateusClicked rate_us_dialog_clicked;
    public static String getPath(Context context,String fileName) throws Exception {
        String filePath = null;
        AssetManager assetManager = context.getAssets();
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

        try {
            
            InputStream inputStream = assetManager.open(fileName);
            File file = new File(context.getCacheDir(), fileName);
            OutputStream outputStream = new FileOutputStream(file);
            try {
                byte[] buffer = new byte[4 * 1024]; 
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, read);
                }
                outputStream.flush();
            } finally {
                outputStream.close();
                inputStream.close();
            }
            
             filePath = file.getAbsolutePath();





        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TAG", "getPath: IOException = > " +e);
        } finally {
            mediaMetadataRetriever.release();
        }
        return filePath;
    }


    public static String removeExtension(String fileName) {
        if (fileName.indexOf(".") > 0) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        } else {
            return fileName;
        }
    }

    public static void app_launched(Context context2, int i, int i2, int i3, int i4, OnRateusClicked lPMLWOnRateusClicked) {
        context = context2;
        SharedPreferences sharedPreferences = context2.getSharedPreferences("app_rater", 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        total_launch_count = sharedPreferences.getInt("total_launch_count", 1);
        never_count = sharedPreferences.getInt("never_count", 1);
        rate_count = sharedPreferences.getInt("rate_count", 1);
        if (!sharedPreferences.getBoolean("do_not_show_again", false)) {
            Long valueOf = Long.valueOf(sharedPreferences.getLong("first_launch_date_time", 0));
            first_launch_date_time = valueOf;
            if (valueOf.longValue() == 0) {
                Long valueOf2 = Long.valueOf(System.currentTimeMillis());
                first_launch_date_time = valueOf2;
                edit.putLong("first_launch_date_time", valueOf2.longValue());
            }
            launch_date_time = Long.valueOf(sharedPreferences.getLong("launch_date_time", 0));
            if (System.currentTimeMillis() >= launch_date_time.longValue() + 86400000 && DAYS_UNTIL_PROMPT <= 5) {
                edit.putLong("launch_date_time", System.currentTimeMillis());
                DAYS_UNTIL_PROMPT++;
            }
            int i5 = total_launch_count;
            if (i5 <= 5) {
                if (edit != null) {
                    edit.putInt("total_launch_count", i5 + 1);
                    edit.commit();
                }
                if (total_launch_count == 1) {
                    showRateDialog(context, i, i2, i3, i4, lPMLWOnRateusClicked);
                } else if (System.currentTimeMillis() >= launch_date_time.longValue() + 86400000) {
                    showRateDialog(context, i, i2, i3, i4, lPMLWOnRateusClicked);
                }
            }
            edit.commit();
        }
    }

    public static void showRateDialog(final Context context2, int i, int i2, int i3, int i4, final OnRateusClicked lPMLWOnRateusClicked) {
        SharedPreferences sharedPreferences = context2.getSharedPreferences("app_rater", 0);
        final SharedPreferences.Editor edit = sharedPreferences.edit();
        if (!sharedPreferences.getBoolean("do_not_show_again", false)) {
            final Dialog dialog = new Dialog(context2);
            dialog.requestWindowFeature(1);
            View inflate = ((Activity) context).getLayoutInflater().inflate(i, (ViewGroup) null);
            inflate.setBackgroundColor(context.getResources().getColor(R.color.color_transaprent));
            View findViewById = inflate.findViewById(i2);
            View findViewById2 = inflate.findViewById(i3);
            View findViewById3 = inflate.findViewById(i4);
            inflate.findViewById(i4);
            findViewById.setOnClickListener(new View.OnClickListener() { 
                @Override 
                public void onClick(View view) {
                    if (Appraterclass.never_count <= 2) {
                        SharedPreferences.Editor editor = edit;
                        if (editor != null) {
                            editor.putInt("never_count", Appraterclass.never_count + 1);
                            edit.commit();
                        }
                    } else {
                        SharedPreferences.Editor editor2 = edit;
                        if (editor2 != null) {
                            editor2.putBoolean("do_not_show_again", true);
                            edit.commit();
                        }
                    }
                    lPMLWOnRateusClicked.on_rate_clicked();
                    dialog.dismiss();
                }
            });
            findViewById2.setOnClickListener(new View.OnClickListener() { 
                @Override 
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            findViewById3.setOnClickListener(new View.OnClickListener() { 
                @Override 
                public void onClick(View view) {
                    if (Appraterclass.rate_count <= 2) {
                        SharedPreferences.Editor editor = edit;
                        if (editor != null) {
                            editor.putInt("rate_count", Appraterclass.rate_count + 1);
                            edit.commit();
                        }
                        Context context3 = context2;
                        context3.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + Appraterclass.context.getPackageName())));
                    } else {
                        SharedPreferences.Editor editor2 = edit;
                        if (editor2 != null) {
                            editor2.putBoolean("do_not_show_again", true);
                            edit.commit();
                        }
                    }
                    dialog.dismiss();
                }
            });
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() { 
                @Override 
                public boolean onKey(DialogInterface dialogInterface, int i5, KeyEvent keyEvent) {
                    if (i5 != 4) {
                        return true;
                    }
                    dialog.dismiss();
                    lPMLWOnRateusClicked.on_rate_clicked();
                    return true;
                }
            });
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(inflate);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255, 255, 255)));
            dialog.show();
        }
    }
}
