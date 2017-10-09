package com.pk.tmdbapp.mvp.presenter;

import com.pk.tmdbapp.db.DBService;
import com.pk.tmdbapp.db.realmmodel.RealmMovie;
import com.pk.tmdbapp.mvp.model.MovieModel;
import com.pk.tmdbapp.mvp.view.activities.DetailView;
import com.pk.tmdbapp.util.RealmMapper;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by ace on 10/09/2017.
 */

public class DetailPresenter {

    @Inject protected DetailView detailView;
    @Inject protected DBService dbService;
    @Inject protected Realm realm;

    @Inject
    public DetailPresenter(DetailView detailView, DBService dbService, Realm realm) {
        this.detailView = detailView;
        this.dbService = dbService;
        this.realm = realm;
    }

    public void removeFavorite(MovieModel movieModel) {
        dbService.remove(realm, RealmMapper.mapToRealmMovie(movieModel))
                .subscribe(movieModelConsumer -> detailView.onShowToast(movieModelConsumer.getTitle() + " Removed"));
    }

    public void saveFavorite(MovieModel movieModel) {
        dbService.save(realm, RealmMapper.mapToRealmMovie(movieModel), RealmMovie.class)
                .subscribe(movieModelConsumer -> detailView.onShowToast(movieModelConsumer.getTitle() + " Added"));
    }
}
