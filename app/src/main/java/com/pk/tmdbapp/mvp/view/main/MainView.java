package com.pk.tmdbapp.mvp.view.main;

import com.pk.tmdbapp.mvp.model.MovieModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by ace on 10/04/2017.
 */

public interface MainView {

    void onShowToast(String message);

    void doOnRetrofitComplete(List<MovieModel> movies);

    void doOnRetrofitError(Throwable throwable);

    void doOnLoadDataComplete(List<MovieModel> movies);

    void updateSortPreferences(String key, String value);

    void checkSortOrder();

    void loadFavoriteMovies();

    void loadPopularMoviesJSON();

    void loadTopRatedMoviesJSON();

    boolean apiKeyIsObtained();
}
