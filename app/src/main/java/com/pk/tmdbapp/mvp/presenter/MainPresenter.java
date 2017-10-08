package com.pk.tmdbapp.mvp.presenter;

import com.pk.tmdbapp.mvp.view.main.MainView;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ace on 10/04/2017.
 */

public class MainPresenter<V extends MainView> {

    @Inject protected V mView;

    protected V getmView() {
        return mView;
    }

    protected <T> void subscribe(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
