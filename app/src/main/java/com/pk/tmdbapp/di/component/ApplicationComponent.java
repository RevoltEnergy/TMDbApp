package com.pk.tmdbapp.di.component;

import android.content.Context;
import android.content.SharedPreferences;

import com.pk.tmdbapp.api.MovieAPIService;
import com.pk.tmdbapp.db.DBService;
import com.pk.tmdbapp.mvp.model.MoviesResponse;
import com.pk.tmdbapp.mvp.presenter.MainPresenter;
import com.pk.tmdbapp.mvp.view.main.MainActivity;
import com.pk.tmdbapp.mvp.view.activities.DetailActivity;
import com.pk.tmdbapp.mvp.view.activities.NoInternetActivity;
import com.pk.tmdbapp.mvp.view.activities.SettingsActivity;
import com.pk.tmdbapp.application.TMDbApplication;
import com.pk.tmdbapp.di.module.ApplicationModule;
import com.pk.tmdbapp.mvp.view.main.MainView;

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

    //MovieAPIService exposeMovieAPIService();

    DBService exposeDBService();

    //MainPresenter exposeMainPresenter();

    Context exposeContext();

    void inject(DetailActivity detailActivity);

    //MainView exposeMainView();

    /*void inject(TMDbApplication application);

    void inject(MainActivity mainActivity);
    void inject(DetailActivity detailActivity);
    void inject(SettingsActivity settingsActivity);
    void inject(NoInternetActivity noInternetActivity);*/
}
