package com.pk.tmdbapp.application;

import android.app.Application;

/**
 * Created by ace on 10/04/2017.
 */

public class TMDbApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initializeApplicationComponent();
    }

    private void initializeApplicationComponent() {

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
