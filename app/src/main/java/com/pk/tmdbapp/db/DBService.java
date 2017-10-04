package com.pk.tmdbapp.db;

import android.util.Log;

import com.pk.tmdbapp.db.migration.RealmMovieMigration;
import com.pk.tmdbapp.db.models.MovieModel;


import java.util.List;

import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by ace on 10/03/2017.
 */

public class DBService {
    private RealmConfiguration mConfig = new RealmConfiguration.Builder()
            .schemaVersion(1)
            .migration(new RealmMovieMigration())
            .build();

    public <T extends RealmObject> Observable<T> save(T object, Class<T> clazz) {
        Realm realm = Realm.getInstance(mConfig);

        long id = 0L;

        try {
            id = realm.where(clazz).max("id").intValue() + 1;
        } catch (Exception e) {
            Log.d("DB", e.getMessage());
        }

        ((MovieModel) object).setId(id);

        return Observable.just(object)
                .flatMap(t -> Observable.just(t)
                        .doOnSubscribe(lol -> {
                            realm.beginTransaction();
                            realm.commitTransaction();
                            realm.close();
                        })
                        .doOnNext(realm::copyToRealm)
                );
    }

    public <T extends RealmObject> Observable<T> remove(T object, Class<T> clazz) {
        Realm realm = Realm.getInstance(mConfig);

        /*realm.executeTransaction(realm1 -> {
            RealmResults<MovieModel> row = realm1.where(MovieModel.class)
                    .equalTo("title",((MovieModel) object).getTitle())
                    .findAll();
            row.deleteAllFromRealm();
        });*/

        return Observable.just(object)
                .flatMap(t -> Observable.just(t)
                        .doOnSubscribe(lol -> realm.executeTransaction(realm1 -> {
                            RealmResults<MovieModel> row = realm1.where(MovieModel.class)
                                    .equalTo("title",((MovieModel) object).getTitle())
                                    .findAll();
                            row.deleteAllFromRealm();
                        }))
                );
    }

    public <T extends RealmObject> Observable<List<T>> getAll(Class<T> clazz) {
        Realm realm = Realm.getInstance(mConfig);

        return Observable.just(clazz)
                .flatMap(t -> Observable.just(t)
                        .doOnSubscribe(lol -> {
                            realm.beginTransaction();
                            realm.commitTransaction();
                            realm.close();
                        })
                        .map(type -> realm.where(type).findAll())
                );
    }
}
