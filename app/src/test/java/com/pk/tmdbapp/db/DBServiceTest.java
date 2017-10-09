package com.pk.tmdbapp.db;

import android.content.Context;

import com.pk.tmdbapp.db.realmmodel.RealmMovie;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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

import edu.emory.mathcs.backport.java.util.Arrays;
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
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
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

    //@Rule
    //public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    private Realm mockRealm;
    private RealmResults<RealmMovie> realmMovies;

    private DBService dbService;

    private List<RealmMovie> movies = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockStatic(RealmCore.class);
        mockStatic(RealmLog.class);
        mockStatic(Realm.class);
        mockStatic(RealmConfiguration.class);
        Realm.init(RuntimeEnvironment.application);

        final Realm mockRealm = mock(Realm.class);
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

        /*RealmConfiguration config =
                new RealmConfiguration.Builder().inMemory().name("test-mockRealm").build();
        mockRealm = Realm.getInstance(config);*/

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
    public void save() throws Exception {
        this.mockRealm = Realm.getDefaultInstance();
        this.dbService = new DBService(mockRealm);

        //doCallRealMethod().when(mockRealm).executeTransaction(Mockito.any(Realm.Transaction.class));

        // Verify that two Realm.getInstance() calls took place.
        verifyStatic(times(1));
        Realm.getDefaultInstance();

        /*when(dbService.save(any(RealmMovie.class), RealmMovie.class))
                .thenAnswer(invocationOnMock -> {
                    RealmMovie movie = invocationOnMock.getArgumentAt(0, RealmMovie.class);
                    movies.add(movie);
                    return null;
                });*/
    }

    @Test
    public void remove() throws Exception {
        /*when(dbService.remove(mockRealm, Mockito.any(RealmMovie.class)))
                .thenAnswer(invocationOnMock -> movies.get(invocationOnMock.getArgument(0)));*/
    }

    @Test
    public void getAll() throws Exception {

    }

    @Test
    public void removeAll() throws Exception {

    }

    @SuppressWarnings("unchecked")
    private <T extends RealmObject> RealmQuery<T> mockRealmQuery() {
        return mock(RealmQuery.class);
    }

    @SuppressWarnings("unchecked")
    private <T extends RealmObject> RealmResults<T> mockRealmResults() {
        return mock(RealmResults.class);
    }

    @After
    public void tearDown() throws Exception {
        mockRealm.close();
    }

    @AfterClass
    public static void tearDownClass() {
        RxAndroidPlugins.reset();
    }

}