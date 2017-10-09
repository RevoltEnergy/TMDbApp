package com.pk.tmdbapp.db;

import android.util.Log;

import com.pk.tmdbapp.db.realmmodel.RealmMovie;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by ace on 10/03/2017.
 */

public class DBService {

    private Realm realm;

    public DBService(Realm realm) {
        this.realm = realm;
    }

    public <T extends RealmObject> Observable<T> save(/*Realm realm, */T object, Class<T> clazz) {

        RealmMovie movie = realm.where(RealmMovie.class).equalTo("title", ((RealmMovie) object).getTitle()).findFirst();

        if (movie != null) {
            if (((RealmMovie) object).getTitle().equals(movie.getTitle())) {
                return Observable.just(object).doOnSubscribe(disposable -> {});
            }
        }

        long id = 0L;

        try {
            id = realm.where(clazz).max("id").intValue() + 1;
        } catch (Exception e) {
            Log.d("DB", e.getMessage());
        }

        ((RealmMovie) object).setId(id);

        return Observable.just(object)
                .flatMap(t -> Observable.just(t)
                        .doOnSubscribe(disposable -> realm.executeTransaction(realm1 -> realm1.insertOrUpdate(object)))
                );
    }

    public <T extends RealmObject> Observable<T> remove(/*Realm realm, */T object) {

        return Observable.just(object)
                .flatMap(t -> Observable.just(t)
                        .doOnSubscribe(disposable -> realm.executeTransaction(realm1 -> {
                            RealmResults<RealmMovie> row = realm1.where(RealmMovie.class)
                                    .equalTo("title",((RealmMovie) object).getTitle())
                                    .findAll();
                            row.deleteAllFromRealm();
                        }))
                );
    }

    public <T extends RealmObject> Observable<List<T>> getAll(/*Realm realm, */Class<T> clazz) {
        return Observable.just(clazz)
                .flatMap(t -> Observable.just(t)
                        .doOnSubscribe(lol -> realm.executeTransaction(realm1 -> realm1.where(RealmMovie.class).findAll()))
                        .onErrorResumeNext((ObservableSource<? extends Class<T>>) observer -> Observable.empty())
                        .map(r -> realm.where(r).findAll())
                );
    }

    public <T extends RealmObject> Observable removeAll(/*Realm realm*/) {
        return Observable.empty().doOnSubscribe(disposable -> realm.executeTransaction(realm1 -> realm1.deleteAll()));
    }
}
