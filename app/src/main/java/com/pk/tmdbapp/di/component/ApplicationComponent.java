package com.pk.tmdbapp.di.component;

import android.content.Context;
import android.content.SharedPreferences;

import com.pk.tmdbapp.db.DBService;
import com.pk.tmdbapp.di.module.ApplicationModule;
import com.pk.tmdbapp.mvp.view.activities.SettingsActivity;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;
import retrofit2.Retrofit;

/**
 * Created by ace on 10/04/2017.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Retrofit exposeRetrofit();

    SharedPreferences exposeSharedPreferences();

    Realm exposeRealm();

    DBService exposeDBService();

    Context exposeContext();

    void inject(SettingsActivity settingsActivity);
}
