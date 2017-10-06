package com.pk.tmdbapp.di.component;

import android.content.Context;

import com.pk.tmdbapp.application.TMDbApplication;
import com.pk.tmdbapp.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import retrofit2.Retrofit;

/**
 * Created by ace on 10/04/2017.
 */

@Singleton
@Component(modules = { AndroidInjectionModule.class, ApplicationModule.class})
public interface ApplicationComponent extends AndroidInjector<TMDbApplication> {


    Retrofit exposeRetrofit();

    Context exposeContext();

    /*void inject(TMDbApplication application);
    void inject(MainActivity mainActivity);
    void inject(DetailActivity detailActivity);
    void inject(SettingsActivity settingsActivity);
    void inject(NoInternetActivity activity);*/
}
