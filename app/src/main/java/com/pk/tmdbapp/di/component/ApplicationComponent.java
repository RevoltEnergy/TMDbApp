package com.pk.tmdbapp.di.component;

import com.pk.tmdbapp.MainActivity;
import com.pk.tmdbapp.activities.DetailActivity;
import com.pk.tmdbapp.activities.NoInternetActivity;
import com.pk.tmdbapp.activities.SettingsActivity;
import com.pk.tmdbapp.application.TMDbApplication;
import com.pk.tmdbapp.di.module.ApplicationModule;
import com.pk.tmdbapp.di.scope.PerApplication;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ace on 10/04/2017.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(TMDbApplication application);
    void inject(MainActivity mainActivity);
    void inject(DetailActivity detailActivity);
    void inject(SettingsActivity settingsActivity);
    void inject(NoInternetActivity activity);
}
