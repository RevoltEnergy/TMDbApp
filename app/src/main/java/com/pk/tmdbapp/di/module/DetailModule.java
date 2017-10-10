package com.pk.tmdbapp.di.module;

import com.pk.tmdbapp.di.scope.PerActivity;
import com.pk.tmdbapp.mvp.view.activities.DetailView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ace on 10/09/2017.
 */

@Module
public class DetailModule {

    private DetailView detailView;

    public DetailModule(DetailView detailView) {
        this.detailView = detailView;
    }

    @PerActivity
    @Provides
    DetailView provideDetailView() {
        return detailView;
    }
}
