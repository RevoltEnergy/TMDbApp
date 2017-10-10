package com.pk.tmdbapp.di.component;

import com.pk.tmdbapp.di.module.DetailModule;
import com.pk.tmdbapp.di.scope.PerActivity;
import com.pk.tmdbapp.mvp.view.activities.DetailActivity;
import com.pk.tmdbapp.mvp.view.activities.DetailView;
import com.pk.tmdbapp.mvp.view.main.MainActivity;

import dagger.Component;

/**
 * Created by ace on 10/09/2017.
 */

@PerActivity
@Component(modules = DetailModule.class, dependencies = ApplicationComponent.class)
public interface DetailComponent {

    void inject(DetailActivity activity);
}
