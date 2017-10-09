package com.pk.tmdbapp.mvp.presenter;

import com.pk.tmdbapp.api.MovieAPIService;
import com.pk.tmdbapp.mvp.model.MoviesResponse;
import com.pk.tmdbapp.mvp.view.main.MainView;

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

public class MainPresenter<V extends MainView> implements Observer<MoviesResponse> {

    @Inject protected V mainView;
    @Inject protected MovieAPIService movieAPIService;
    @Inject protected Retrofit retrofit;
    @Inject protected Realm realm;

    @Inject
    public MainPresenter() {
        //movieAPIService = retrofit.create(MovieAPIService.class);
    }

    protected V getMainView() {
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

    }

    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
