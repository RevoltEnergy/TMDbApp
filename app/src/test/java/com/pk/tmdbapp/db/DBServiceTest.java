package com.pk.tmdbapp.db;

import android.content.Context;

import com.pk.tmdbapp.db.realmmodel.RealmMovie;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.internal.RealmCore;
import io.realm.log.RealmLog;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.doCallRealMethod;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by ace on 10/09/2017.
 */

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(RobolectricTestRunner.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@SuppressStaticInitializationFor("io.mockRealm.internal.Util")
@PrepareForTest({Realm.class, RealmConfiguration.class, RealmQuery.class, RealmResults.class, RealmCore.class, RealmLog.class})
public class DBServiceTest {

    @Rule public PowerMockRule rule = new PowerMockRule();

    private Realm mockRealm;
    private RealmResults<RealmMovie> realmMovies;

    @Mock private DBService dbService;

    private List<RealmMovie> movies = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockStatic(RealmCore.class);
        mockStatic(RealmLog.class);
        mockStatic(Realm.class);
        mockStatic(RealmConfiguration.class);
        Realm.init(RuntimeEnvironment.application);

        Realm mockRealm = PowerMockito.mock(Realm.class);
        final RealmConfiguration mockRealmConfig = mock(RealmConfiguration.class);

        doNothing().when(RealmCore.class);
        RealmCore.loadLibrary(any(Context.class));

        whenNew(RealmConfiguration.class).withAnyArguments().thenReturn(mockRealmConfig);
        when(Realm.getDefaultInstance()).thenReturn(mockRealm);
        when(mockRealm.createObject(RealmMovie.class)).thenReturn(new RealmMovie());

        RealmMovie movie1 = new RealmMovie();
        RealmMovie movie2 = new RealmMovie();
        RealmMovie movie3 = new RealmMovie();
        RealmMovie movie4 = new RealmMovie();

        movie1.setTitle("Movie 1");
        movie2.setTitle("Movie 2");
        movie3.setTitle("Movie 3");
        movie4.setTitle("Movie 4");

        List<RealmMovie> movieList = new ArrayList<>();

        movieList.add(movie1);
        movieList.add(movie2);
        movieList.add(movie3);
        movieList.add(movie4);

        RealmQuery<RealmMovie> realmMoviesQuery = mockRealmQuery();

        when(realmMoviesQuery.findFirst()).thenReturn(movieList.get(0));

        when(mockRealm.where(RealmMovie.class)).thenReturn(realmMoviesQuery);

        when(realmMoviesQuery.equalTo(anyString(), anyInt())).thenReturn(realmMoviesQuery);

        mockStatic(RealmResults.class);

        RealmResults<RealmMovie> realmMovies = mockRealmResults();

        when(mockRealm.where(RealmMovie.class).findAll()).thenReturn(realmMovies);

        when(realmMoviesQuery.between(anyString(), anyInt(), anyInt())).thenReturn(realmMoviesQuery);

        when(realmMoviesQuery.beginsWith(anyString(), anyString())).thenReturn(realmMoviesQuery);

        when(realmMoviesQuery.findAll()).thenReturn(realmMovies);

        when(realmMovies.iterator()).thenReturn(movieList.iterator());

        when(realmMovies.size()).thenReturn(movieList.size());

        when(Realm.getDefaultInstance()).thenReturn(mockRealm);

        this.mockRealm = mockRealm;
        this.realmMovies = realmMovies;
    }

    @BeforeClass
    public static void setUpClass() {
        // Override the default "out of the box" AndroidSchedulers.mainThread() Scheduler
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(__ -> Schedulers.trampoline());
    }

    @Test
    public void shouldBeAbleToGetDefaultInstance() {
        assertThat(Realm.getDefaultInstance(), is(mockRealm));
    }

    @Test
    public void shouldBeAbleToMockRealmMethods() {
        when(mockRealm.isAutoRefresh()).thenReturn(true);
        assertThat(mockRealm.isAutoRefresh(), is(true));

        when(mockRealm.isAutoRefresh()).thenReturn(false);
        assertThat(mockRealm.isAutoRefresh(), is(false));
    }

    @Test
    public void shouldBeAbleToCreateARealmObject() {
        RealmMovie movie = new RealmMovie();
        when(mockRealm.createObject(RealmMovie.class)).thenReturn(movie);
        RealmMovie output = mockRealm.createObject(RealmMovie.class);

        assertThat(output, is(movie));
    }

    @Test
    public void shouldVerifyThatObjectWasCreated() {
        Realm mockRealm = Mockito.mock(Realm.class);
        RealmMovie movie = Mockito.mock(RealmMovie.class);
        Mockito.when(mockRealm.createObject(RealmMovie.class)).thenReturn(movie);
        mockRealm.createObject(RealmMovie.class);

        Mockito.verify(mockRealm, times(1)).createObject(RealmMovie.class);
    }

    @Test
    public void save() throws Exception {
        doCallRealMethod().when(mockRealm).executeTransaction(Mockito.any(Realm.Transaction.class));
        PowerMockito.when(dbService.save(any(RealmMovie.class)))
                .thenAnswer(invocationOnMock -> {
                    RealmMovie movie = invocationOnMock.getArgumentAt(0, RealmMovie.class);
                    movies.add(movie);
                    return null;
                });
        dbService.save(any(RealmMovie.class));

        Mockito.verify(dbService, times(1)).save(any(RealmMovie.class));
        assertEquals(movies.size(), 1);
    }

    @Test
    public void remove() throws Exception {
        doCallRealMethod().when(mockRealm).executeTransaction(Mockito.any(Realm.Transaction.class));
        PowerMockito.when(dbService.remove(Mockito.any(RealmMovie.class)))
                .thenAnswer(invocationOnMock -> {
                    movies.clear();
                    return null;
                });
        dbService.remove(any(RealmMovie.class));

        Mockito.verify(dbService, times(1)).remove(any(RealmMovie.class));
        assertEquals(movies.size(), 0);
    }

    @Test
    public void getAll() throws Exception {
        Mockito.when(dbService.getAll()).thenReturn(Observable.empty());
        dbService.removeAll();
        Mockito.verify(dbService, times(1)).removeAll();
    }

    @Test
    public void removeAll() throws Exception {
        Mockito.when(dbService.removeAll()).thenReturn(Observable.empty());
        dbService.removeAll();
        Mockito.verify(dbService, times(1)).removeAll();
    }

    @SuppressWarnings("unchecked")
    private <T extends RealmObject> RealmQuery<T> mockRealmQuery() {
        return mock(RealmQuery.class);
    }

    @SuppressWarnings("unchecked")
    private <T extends RealmObject> RealmResults<T> mockRealmResults() {
        return mock(RealmResults.class);
    }

    @AfterClass
    public static void tearDownClass() {
        RxAndroidPlugins.reset();
    }

}