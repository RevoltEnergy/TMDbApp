package com.pk.tmdbapp.di.module;

import android.content.Context;

import com.pk.tmdbapp.application.TMDbApplication;
import com.pk.tmdbapp.di.scope.PerApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by ace on 10/04/2017.
 */
//@PerApplication
@Module(includes = ApplicationModule.class)
public class DatabaseModule {
    public DatabaseModule(Context context) {
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("TMDb.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
        if (Realm.getDefaultInstance() != null) {
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ init realm in App");
        } else {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }

//    @Singleton
    @Provides
    Realm provideRealm() {
        return Realm.getDefaultInstance();
    }
}
