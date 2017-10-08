package com.pk.tmdbapp.di.module;

import android.content.Context;

import com.pk.tmdbapp.mvp.view.main.MainActivity;
import com.pk.tmdbapp.di.qualifier.ActivityContext;
import com.pk.tmdbapp.di.scope.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ace on 10/04/2017.
 */

@Module
public class ActivityModule {

    private final MainActivity mActivity;

    public ActivityModule(MainActivity activity) {
        mActivity = activity;
    }

    @Provides
    @PerActivity
    @ActivityContext
    Context provideActivityContext() {
        return mActivity;
    }
}