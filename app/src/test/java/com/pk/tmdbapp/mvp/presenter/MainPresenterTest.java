package com.pk.tmdbapp.mvp.presenter;

import android.content.Context;
import android.os.Looper;

import com.pk.tmdbapp.api.MovieAPIService;
import com.pk.tmdbapp.db.DBService;
import com.pk.tmdbapp.db.realmmodel.RealmMovie;
import com.pk.tmdbapp.mvp.model.MovieModel;
import com.pk.tmdbapp.mvp.model.MoviesResponse;
import com.pk.tmdbapp.mvp.view.main.MainView;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import retrofit2.Retrofit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by ace on 10/10/2017.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Observable.class, AndroidSchedulers.class, Looper.class, MoviesResponse.class})
public class MainPresenterTest {

    public static final String TEST_ERROR_MESSAGE = "error_message";

    @Mock private MainView mainView;
    @Mock private MovieAPIService movieAPIService;
    @Mock private DBService dbService;
    @Mock private Realm realm;
    @Mock private Context context;
    @Mock private Observable<MoviesResponse> mObservable;

    @InjectMocks private MainPresenter mainPresenter;

    @BeforeClass
    public static void setUpClass() {
        // Override the default "out of the box" AndroidSchedulers.mainThread() Scheduler
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(__ -> Schedulers.trampoline());
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        List<RealmMovie> realmMovies = new ArrayList<>();
        RealmMovie realmMovie = new RealmMovie();
        realmMovies.add(realmMovie);

        List<MovieModel> movieModels = new ArrayList<>();
        MovieModel movieModel = new MovieModel();
        movieModels.add(movieModel);
        MoviesResponse moviesResponse = new MoviesResponse();
        moviesResponse.setResults(movieModels);

        Observable<List<RealmMovie>> listRealmObservable = Observable.just(realmMovies);
        Observable<MoviesResponse> listResponseObservable = Observable.just(moviesResponse);

        when(dbService.getAll()).thenReturn(listRealmObservable);
        when(dbService.save(Mockito.any(RealmMovie.class))).thenReturn(Observable.just(realmMovie));
        when(dbService.remove(Mockito.any(RealmMovie.class))).thenReturn(Observable.just(realmMovie));
        when(dbService.removeAll()).thenReturn(Observable.empty());

        when(movieAPIService.getPopularMoviesObs(Mockito.anyString())).thenReturn(listResponseObservable);
        when(movieAPIService.getTopRatedMoviesObs(Mockito.anyString())).thenReturn(listResponseObservable);
    }

    @Test
    public void loadTopRatedMoviesJSON() throws Exception {

    }

    @Test
    public void loadPopularMoviesJSON() throws Exception {

    }

    @Test
    public void loadFavoriteMovies() throws Exception {

    }

    @Test
    public void onNext() throws Exception {

    }

    @Test
    public void onError() throws Exception {
        Throwable throwable = new Throwable(TEST_ERROR_MESSAGE);
        mainPresenter.onError(throwable);
        verify(mainView, times(1)).doOnRetrofitError(throwable);
    }

    @Test
    public void onComplete() throws Exception {
        mainPresenter.onComplete();
        verify(mainView, times(1)).doOnRetrofitComplete(Mockito.anyList());
    }

    @AfterClass
    public static void tearDownClass() {
        RxAndroidPlugins.reset();
    }
}