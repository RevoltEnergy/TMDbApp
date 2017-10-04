package com.pk.tmdbapp.di.component;

import android.content.Context;

import com.pk.tmdbapp.MainActivity;
import com.pk.tmdbapp.activities.DetailActivity;
import com.pk.tmdbapp.activities.SettingsActivity;
import com.pk.tmdbapp.di.module.ActivityModule;
import com.pk.tmdbapp.di.module.DatabaseModule;
import com.pk.tmdbapp.di.qualifier.ActivityContext;
import com.pk.tmdbapp.di.scope.PerActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ace on 10/04/2017.
 */
@PerActivity
//@Singleton
@Component(modules = {ActivityModule.class, DatabaseModule.class}, dependencies = ApplicationComponent.class)
public interface ActivityComponent extends ApplicationComponent {

    @ActivityContext Context activityContext();

    /*void inject(MainActivity activity);
    void inject(DetailActivity activity);
    void inject(SettingsActivity activity);*/
}
