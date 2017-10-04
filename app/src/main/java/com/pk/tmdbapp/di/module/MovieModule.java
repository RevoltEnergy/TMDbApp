package com.pk.tmdbapp.di.module;

import com.pk.tmdbapp.api.MovieAPIService;
import com.pk.tmdbapp.di.scope.PerActivity;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by ace on 10/04/2017.
 */

@Module
public class MovieModule {

    @PerActivity
    @Provides
    MovieAPIService provideMovieAPIService(Retrofit retrofit){
        return retrofit.create(MovieAPIService.class);
    }
}
