package com.pk.tmdbapp.db;

import android.util.Log;

import com.pk.tmdbapp.db.migration.RealmMovieMigration;

import java.util.List;
import java.util.Stack;

import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

import com.pk.tmdbapp.mvp.model.MovieModel;

import javax.inject.Inject;

/**
 * Created by ace on 10/03/2017.
 */

public class DBService {
    //@Inject Realm realm;
    private RealmConfiguration mConfig = new RealmConfiguration.Builder()
            .schemaVersion(1)
            .migration(new RealmMovieMigration())
            //.deleteRealmIfMigrationNeeded()
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
                        .doOnSubscribe(lol -> realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(object)))
                );
    }

    public <T extends RealmObject> Observable<T> remove(T object, Class<T> clazz) {
        Realm realm = Realm.getInstance(mConfig);

        return Observable.just(object)
                .flatMap(t -> Observable.just(t)
                        .doOnSubscribe(disposable -> realm.executeTransaction(realm1 -> {
                            RealmResults<MovieModel> row = realm1.where(MovieModel.class)
                                    .equalTo("title",((MovieModel) object).getTitle())
                                    .findAll();
                            row.deleteAllFromRealm();
                        }))
                );
    }

    public <T extends RealmObject> Observable<List<T>> getAll(Class<T> clazz) {
        Realm realm = Realm.getInstance(mConfig);
        System.out.println("###########################################################################################");
        if (realm != null) {
            if (realm.isClosed()) {
                System.out.println("realm cosed DS");
            }
        } else {
            System.out.println("realm is null DS");
        }
        System.out.println("###########################################################################################");
        /*return Observable.just(clazz)
                .flatMap(t -> Observable.just(t)
                        .doOnSubscribe(lol -> {
                            realm.beginTransaction();
                            realm.commitTransaction();
                            //realm.close();
                        })
                        .map(type -> realm.where(type).findAll())
                );*/
        return Observable.just(clazz)
                .flatMap(t -> Observable.just(t)
                        .doOnSubscribe(lol -> {
                            realm.executeTransaction(realm1 -> {
                            realm1.where(MovieModel.class).findAll();
                                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
                                        + realm1.where(MovieModel.class).findAll().first().getOriginalTitle());
                        });
                        }).map(r -> realm.where(r).findAll())
                );
    }
}
