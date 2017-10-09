package com.pk.tmdbapp.di.component;

import android.content.SharedPreferences;

import com.pk.tmdbapp.mvp.presenter.MainPresenter;
import com.pk.tmdbapp.mvp.view.main.MainActivity;
import com.pk.tmdbapp.mvp.view.activities.DetailActivity;
import com.pk.tmdbapp.mvp.view.activities.NoInternetActivity;
import com.pk.tmdbapp.mvp.view.activities.SettingsActivity;
import com.pk.tmdbapp.application.TMDbApplication;
import com.pk.tmdbapp.di.module.ApplicationModule;

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

    void inject(TMDbApplication application);

    void inject(MainActivity mainActivity);
    void inject(DetailActivity detailActivity);
    void inject(SettingsActivity settingsActivity);
    void inject(NoInternetActivity noInternetActivity);
}
