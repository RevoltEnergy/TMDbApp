package com.pk.tmdbapp.di.component;

import android.content.Context;

import com.pk.tmdbapp.MainActivity;
import com.pk.tmdbapp.application.TMDbApplication;
import com.pk.tmdbapp.di.module.ApplicationModule;
import com.pk.tmdbapp.di.module.DatabaseModule;
import com.pk.tmdbapp.di.module.MovieModule;
import com.pk.tmdbapp.di.qualifier.AppContext;
import com.pk.tmdbapp.di.scope.PerApplication;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;
import retrofit2.Retrofit;

/**
 * Created by ace on 10/04/2017.
 */

@PerApplication
//@Singleton
@Component(modules = {ApplicationModule.class, DatabaseModule.class, MovieModule.class})
public interface ApplicationComponent {

//    @AppContext Context appContext();

    void inject(TMDbApplication application);
    void inject(MainActivity activity);

    //@Inject Retrofit exposeRetrofit();

    //@Inject Context exposeContext();

    //void inject(TMDbApplication application);
}
