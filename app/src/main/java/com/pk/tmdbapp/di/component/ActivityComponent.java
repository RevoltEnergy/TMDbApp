package com.pk.tmdbapp.di.component;

import com.pk.tmdbapp.di.module.ActivityModule;
import com.pk.tmdbapp.di.scope.PerActivity;

import dagger.Component;

/**
 * Created by ace on 10/04/2017.
 */
@PerActivity
@Component(modules = ActivityModule.class, dependencies = ApplicationComponent.class)
public interface ActivityComponent {

}
