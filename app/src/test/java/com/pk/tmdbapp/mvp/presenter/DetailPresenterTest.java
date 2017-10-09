package com.pk.tmdbapp.mvp.presenter;

import com.pk.tmdbapp.db.DBService;
import com.pk.tmdbapp.db.realmmodel.RealmMovie;
import com.pk.tmdbapp.mvp.model.MovieModel;
import com.pk.tmdbapp.mvp.view.activities.DetailView;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Observable;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by ace on 10/09/2017.
 */
@RunWith(PowerMockRunner.class)
public class DetailPresenterTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock private DetailView detailView;
    @Mock private DBService dbService;
    @Mock private Realm realm;
    @Mock private MovieModel mockedModel;

    @InjectMocks private DetailPresenter detailPresenter;

    private ArrayList<RealmMovie> realmMovies;
    private RealmMovie realmMovie;
    private MovieModel movieModel;

    @BeforeClass
    public static void setUpClass() {

        // Override the default "out of the box" AndroidSchedulers.mainThread() Scheduler
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(__ -> Schedulers.trampoline());
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        movieModel = new MovieModel();
        movieModel.setTitle("Nice Movie");
        realmMovies = new ArrayList<>();
        realmMovie = new RealmMovie();
        realmMovie.setTitle("Nice Movie");
        realmMovies.add(realmMovie);
    }

    @Test
    public void removeFavorite() throws Exception {
        //doNothing().when(detailPresenter).removeFavorite(Mockito.any(MovieModel.class));
        //when(detailPresenter.saveFavorite(mockedModel)).thenAnswer()

        /*detailPresenter.removeFavorite(Mockito.any(MovieModel.class));

        verify(dbService, times(1)).remove(realm, Mockito.any(RealmMovie.class));*/
    }

    @Test
    public void saveFavorite() throws Exception {
        //when(dbService.save(realm, Mockito.any(RealmMovie.class), RealmMovie.class)).thenAnswer()
        //doNothing().when(detailPresenter).saveFavorite(Mockito.any(MovieModel.class));

        /*detailPresenter.saveFavorite(Mockito.any(MovieModel.class));

        verify(dbService, times(1)).save(realm, Mockito.any(RealmMovie.class), RealmMovie.class);*/

        //when(detailPresenter.saveFavorite(Mockito.any())).thenReturn(new Observable());
    }

    @AfterClass
    public static void tearDownClass() {
        // Not strictly necessary because we can't reset the value set by setInitMainThreadSchedulerHandler,
        // but it doesn't hurt to clean up anyway.
        RxAndroidPlugins.reset();
    }

}