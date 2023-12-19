package com.demo.livwllpaper;
import android.app.Activity;
import android.os.Bundle;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import java.util.Date;


public class Application extends android.app.Application {
    private static Application instance;

    public static Application getInstance() {
        if (instance == null) {
            synchronized (Application.class) {
                if (instance == null) {
                    instance = new Application();
                }
            }
        }
        return instance;
    }

    @Override 
    public void onCreate() {
        super.onCreate();
    }

}
