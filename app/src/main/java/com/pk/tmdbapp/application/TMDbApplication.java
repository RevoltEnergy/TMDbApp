package com.pk.tmdbapp.application;

import android.app.Application;

import com.pk.tmdbapp.di.component.ApplicationComponent;
import com.pk.tmdbapp.di.component.DaggerApplicationComponent;
import com.pk.tmdbapp.di.module.ApplicationModule;

/**
 * Created by ace on 10/04/2017.
 */

public class TMDbApplication extends Application {

    ApplicationComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public ApplicationComponent getAppComponent() {
        return appComponent;
    }
}
