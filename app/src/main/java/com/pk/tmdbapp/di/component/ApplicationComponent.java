package com.pk.tmdbapp.di.component;

import android.content.Context;

import com.pk.tmdbapp.application.TMDbApplication;
import com.pk.tmdbapp.di.module.ApplicationModule;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by ace on 10/04/2017.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    //@Inject Retrofit exposeRetrofit();

    //@Inject Context exposeContext();

    //void inject(TMDbApplication application);
}
