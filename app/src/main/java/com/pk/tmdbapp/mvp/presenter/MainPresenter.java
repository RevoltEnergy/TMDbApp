package com.pk.tmdbapp.mvp.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.pk.tmdbapp.BuildConfig;
import com.pk.tmdbapp.api.MovieAPIService;
import com.pk.tmdbapp.application.TMDbApplication;
import com.pk.tmdbapp.di.component.ApplicationComponent;
import com.pk.tmdbapp.di.component.DaggerMovieComponent;
import com.pk.tmdbapp.di.module.MovieModule;
import com.pk.tmdbapp.mvp.model.MovieModel;
import com.pk.tmdbapp.mvp.model.MoviesResponse;
import com.pk.tmdbapp.mvp.view.main.MainActivity;
import com.pk.tmdbapp.mvp.view.main.MainView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import retrofit2.Retrofit;

/**
 * Created by ace on 10/04/2017.
 */

public class MainPresenter implements Observer<MoviesResponse> {

    @Inject protected MainView mainView;
    @Inject protected MovieAPIService movieAPIService;
    @Inject protected Retrofit retrofit;
    @Inject protected Realm realm;
    @Inject protected Context context;

    List<MovieModel> movies = new ArrayList<>();

    @Inject
    public MainPresenter(MainView mainView, MovieAPIService movieAPIService, Retrofit retrofit, Realm realm) {
        this.mainView = mainView;
        this.movieAPIService = movieAPIService;
        this.retrofit = retrofit;
        this.realm = realm;
    }

    public void loadTopRatedMoviesJSON() {
        try {
            movieAPIService = retrofit.create(MovieAPIService.class);
            Observable<MoviesResponse> listObservable = movieAPIService.getTopRatedMoviesObs(BuildConfig.TMDB_API_KEY);
            subscribe(listObservable, this);
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            getMainView().onShowToast(e.toString());
        }
        getMainView().onShowToast("Top Rated Movies");
    }

    public void loadPopularMoviesJSON() {
        try {
            movieAPIService = retrofit.create(MovieAPIService.class);
            Observable<MoviesResponse> listObservable = movieAPIService.getPopularMoviesObs(BuildConfig.TMDB_API_KEY);
            subscribe(listObservable, this);
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            getMainView().onShowToast(e.toString());
        }
        getMainView().onShowToast("Most Popular Movies");
    }

    protected MainView getMainView() {
        return mainView;
    }

    protected <T> void subscribe(Observable<T> observable, Observer<T> observer) {
        observable
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(15, TimeUnit.SECONDS, Observable.error(new TimeoutException()))
                .onErrorResumeNext(Observable.empty())
                .subscribe(observer);
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull MoviesResponse moviesResponse) {
        movies.clear();
        movies.addAll(moviesResponse.getResults());
    }

    @Override
    public void onError(@NonNull Throwable e) {
        getMainView().doOnRetrofitError(e);
    }

    @Override
    public void onComplete() {
        getMainView().doOnRetrofitComplete(movies);
    }
}
