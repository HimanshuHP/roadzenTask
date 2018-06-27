package com.himanshuph.roadzentask;

import android.app.Application;
import android.support.annotation.NonNull;

public class App extends Application {
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @NonNull
    public static App getInstance() {
        return instance;
    }


}
