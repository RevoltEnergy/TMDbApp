package com.pk.tmdbapp.di.component;

import com.pk.tmdbapp.MainActivity;
import com.pk.tmdbapp.di.module.MovieModule;
import com.pk.tmdbapp.di.scope.PerActivity;

import dagger.Component;
import io.realm.Realm;

/**
 * Created by ace on 10/04/2017.
 */

@PerActivity
@Component(modules = MovieModule.class, dependencies = ApplicationComponent.class)
public interface MovieComponent {

    //void inject(MainActivity activity);

    // exposeRealm();
}
