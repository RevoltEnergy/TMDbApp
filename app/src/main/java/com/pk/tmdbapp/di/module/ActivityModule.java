package com.pk.tmdbapp.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.pk.tmdbapp.di.qualifier.ActivityContext;
import com.pk.tmdbapp.di.scope.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ace on 10/04/2017.
 */

@Module
public class ActivityModule {

    private final AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        mActivity = activity;
    }

    @Provides
    @PerActivity
    @ActivityContext
    Context provideActivityContext() {
        return mActivity;
    }
}