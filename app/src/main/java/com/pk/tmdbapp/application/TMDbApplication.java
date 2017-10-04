package com.pk.tmdbapp.application;

import android.app.Application;

import com.pk.tmdbapp.db.migration.RealmMovieMigration;
import com.pk.tmdbapp.di.component.ApplicationComponent;
import com.pk.tmdbapp.di.component.DaggerApplicationComponent;
import com.pk.tmdbapp.di.module.ApplicationModule;
import com.pk.tmdbapp.di.module.DatabaseModule;
import com.pk.tmdbapp.di.module.MovieModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by ace on 10/04/2017.
 */

public class TMDbApplication extends Application {

    ApplicationComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .databaseModule(new DatabaseModule(this))
                .movieModule(new MovieModule())
                .build();
        appComponent.inject(this);

        //initRealm();
        //initializeApplicationComponent();
        //setupDependencyInjection();
    }

    private void initRealm() {
//        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .migration(new RealmMovieMigration())
                //.deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
        if (Realm.getDefaultInstance() != null) {
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ init realm in TMDbApp");
        } else {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }

    private void initializeApplicationComponent() {

    }

    private void setupDependencyInjection() {
        appComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        appComponent.inject(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public ApplicationComponent getAppComponent() {
        return appComponent;
    }
}
