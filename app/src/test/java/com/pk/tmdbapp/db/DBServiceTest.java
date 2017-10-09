package com.pk.tmdbapp.db;

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
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static org.mockito.Mockito.when;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by ace on 10/09/2017.
 */

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"org.mockito.*", "android.*"})
@PrepareForTest({ RealmConfiguration.class, Realm.class, RealmQuery.class, RealmResults.class, RealmList.class, RealmObject.class })
public class DBServiceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private Realm realm;

    private DBService dbService;

    private List<RealmMovie> movies = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockStatic(Realm.class);

        RealmConfiguration config =
                new RealmConfiguration.Builder().inMemory().name("test-realm").build();
        realm = Realm.getInstance(config);

        dbService = new DBService(realm);

    }

    @BeforeClass
    public static void setUpClass() {

        // Override the default "out of the box" AndroidSchedulers.mainThread() Scheduler
        //
        // This is necessary here because otherwise if the static initialization block in AndroidSchedulers
        // is executed before this, then the Android SDK dependent version will be provided instead.
        //
        // This would cause a java.lang.ExceptionInInitializerError when running the test as a
        // Java JUnit test as any attempt to resolve the default underlying implementation of the
        // AndroidSchedulers.mainThread() will fail as it relies on unavailable Android dependencies.

        // Comment out this line to see the java.lang.ExceptionInInitializerError
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(__ -> Schedulers.trampoline());
    }

    @Test
    public void save() throws Exception {
        System.out.println(dbService);
        System.out.println(realm);
        /*when(dbService.save(realm, Mockito.any(RealmMovie.class), RealmMovie.class))
                .thenAnswer(invocationOnMock -> {
                    RealmMovie movie = invocationOnMock.getArgumentAt(0, RealmMovie.class);
                    movies.add(movie);
                    return null;
                });*/
    }

    @Test
    public void remove() throws Exception {
        /*System.out.println(dbService);
        System.out.println(realm);
        when(dbService.remove(realm, Mockito.any(RealmMovie.class)))
                .thenAnswer(invocationOnMock -> movies.get(invocationOnMock.getArgument(0)));*/
    }

    @Test
    public void getAll() throws Exception {

    }

    @Test
    public void removeAll() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        realm.close();
    }

    @AfterClass
    public static void tearDownClass() {
        // Not strictly necessary because we can't reset the value set by setInitMainThreadSchedulerHandler,
        // but it doesn't hurt to clean up anyway.
        RxAndroidPlugins.reset();
    }

}