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

    public Observable<RealmMovie> save(RealmMovie object) {

        RealmMovie movie = realm.where(RealmMovie.class).equalTo("title", object.getTitle()).findFirst();

        if (movie != null) {
            if (object.getTitle().equals(movie.getTitle())) {
                return Observable.just(object).doOnSubscribe(disposable -> {});
            }
        }

        long id = 0L;

        try {
            id = realm.where(RealmMovie.class).max("id").intValue() + 1;
        } catch (Exception e) {
            Log.d("DB", e.getMessage());
        }

        object.setId(id);

        return Observable.just(object)
                .flatMap(t -> Observable.just(t)
                        .doOnSubscribe(disposable -> realm.executeTransaction(realm1 -> realm1.insertOrUpdate(object)))
                );
    }

    public Observable<RealmMovie> remove(RealmMovie object) {

        return Observable.just(object)
                .flatMap(t -> Observable.just(t)
                        .doOnSubscribe(disposable -> realm.executeTransaction(realm1 -> {
                            RealmResults<RealmMovie> row = realm1.where(RealmMovie.class)
                                    .equalTo("title",object.getTitle())
                                    .findAll();
                            row.deleteAllFromRealm();
                        }))
                );
    }

    public Observable<List<RealmMovie>> getAll() {
        return Observable.just(RealmMovie.class)
                .flatMap(t -> Observable.just(t)
                        .doOnSubscribe(disposable -> realm.executeTransaction(realm1 -> realm1.where(RealmMovie.class).findAll()))
                        .onErrorResumeNext((ObservableSource<? extends Class<RealmMovie>>) observer -> Observable.empty())
                        .map(all -> realm.where(RealmMovie.class).findAll())
                );
    }

    public Observable removeAll() {
        return Observable.empty().doOnSubscribe(disposable -> realm.executeTransaction(realm1 -> realm1.deleteAll()));
    }
}
