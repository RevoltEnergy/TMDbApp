package com.pk.tmdbapp.db;

import android.util.Log;

import com.pk.tmdbapp.mvp.model.MovieModel;

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

    public <T extends RealmObject> Observable<T> save(Realm mRealm, T object, Class<T> clazz) {

        Realm realm = Realm.getDefaultInstance();

        if (realm.where(clazz).equalTo("originalTitle", ((MovieModel) object).getOriginalTitle()).findFirst() != null) {
            return Observable.just(object).doOnSubscribe(disposable -> {});
        }

        long id = 0L;

        try {
            id = realm.where(clazz).max("id").intValue() + 1;
        } catch (Exception e) {
            Log.d("DB", e.getMessage());
        }

        ((MovieModel) object).setId(id);

        return Observable.just(object)
                .flatMap(t -> Observable.just(t)
                        .doOnSubscribe(disposable -> realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(object)))
                );
    }

    public <T extends RealmObject> Observable<T> remove(Realm mRealm, T object) {
        Realm realm = Realm.getDefaultInstance();
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

    public <T extends RealmObject> Observable<List<T>> getAll(Realm realm, Class<T> clazz) {
        return Observable.just(clazz)
                .flatMap(t -> Observable.just(t)
                        .doOnSubscribe(lol -> realm.executeTransaction(realm1 ->
                                realm1.where(MovieModel.class).findAll()))
                        .onErrorResumeNext((ObservableSource<? extends Class<T>>) observer -> Observable.empty())
                        .map(r -> realm.where(r).findAll())
                );
    }
}
