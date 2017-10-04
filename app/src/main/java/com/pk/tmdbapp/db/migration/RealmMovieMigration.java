package com.pk.tmdbapp.db.migration;

import android.support.annotation.NonNull;

import io.realm.DynamicRealm;
import io.realm.RealmSchema;

/**
 * Created by ace on 10/03/2017.
 */

public class RealmMovieMigration implements io.realm.RealmMigration {

    @Override
    public void migrate(@NonNull DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();

        if (oldVersion == 0) {
            schema.create("MovieModel")
                    .addField("Title", String.class);

            oldVersion++;
        }
    }
}
