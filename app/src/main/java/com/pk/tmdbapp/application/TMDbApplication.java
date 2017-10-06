package com.pk.tmdbapp.application;

import android.app.Activity;
import android.app.Application;

import com.pk.tmdbapp.di.component.ApplicationComponent;
import com.pk.tmdbapp.di.component.DaggerApplicationComponent;
import com.pk.tmdbapp.di.module.ApplicationModule;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * Created by ace on 10/04/2017.
 */

public class TMDbApplication extends Application /*implements HasActivityInjector*/ {

    /*@Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;*/

    ApplicationComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        //DaggerApplicationComponent.builder().build().inject(this);
        appComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        //appComponent.inject(this);
    }

    public ApplicationComponent getAppComponent() {
        return appComponent;
    }

    /*@Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }*/
}
