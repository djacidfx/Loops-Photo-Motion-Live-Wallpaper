package com.demo.livwllpaper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.livwllpaper.R;
import com.demo.livwllpaper.Utilsx.Utils;


public class SplashActivity extends AppCompatActivity {
    long COUNTER = 4;
    long secondsRemaining = 0;

    public void createTimer(long j) {
        new CountDownTimer(j * 1000, 1000) {
            @Override
            public void onTick(long j2) {
                SplashActivity.this.secondsRemaining = (j2 / 1000) + 1;
            }

            @Override
            public void onFinish() {
                if (Boolean.valueOf(SplashActivity.this.getSharedPreferences(Utils.MY_PREFS_NAME, 0).getBoolean("tutorial_seen", false)).booleanValue()) {
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    return;
                }
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, TutorialActivity.class));
                SplashActivity.this.finish();
            }
        }.start();
    }
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash_screen);
        createTimer(this.COUNTER);
    }
}
