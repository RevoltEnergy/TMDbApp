package com.pk.tmdbapp.di.component;

import com.pk.tmdbapp.MainActivity;
import com.pk.tmdbapp.activities.DetailActivity;
import com.pk.tmdbapp.application.TMDbApplication;
import com.pk.tmdbapp.di.module.ApplicationModule;
import com.pk.tmdbapp.di.scope.PerApplication;

import dagger.Component;

/**
 * Created by ace on 10/04/2017.
 */

@PerApplication
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(TMDbApplication application);
    void inject(MainActivity mainActivity);
    void inject(DetailActivity detailActivity);
}
