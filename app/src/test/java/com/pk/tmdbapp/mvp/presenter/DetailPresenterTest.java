package com.pk.tmdbapp.mvp.presenter;

import com.pk.tmdbapp.db.DBService;
import com.pk.tmdbapp.mvp.model.MovieModel;
import com.pk.tmdbapp.mvp.view.activities.DetailView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.realm.Realm;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by ace on 10/09/2017.
 */

public class DetailPresenterTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock private DetailView detailView;
    @Mock private DBService dbService;
    @Mock private Realm realm;

    @InjectMocks private DetailPresenter detailPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void removeFavorite() throws Exception {
        detailPresenter = spy(detailPresenter);

        doNothing().when(detailPresenter).removeFavorite(Mockito.any(MovieModel.class));

        detailPresenter.removeFavorite(Mockito.any(MovieModel.class));

        verify(detailPresenter, times(1)).removeFavorite(Mockito.any(MovieModel.class));
    }

    @Test
    public void saveFavorite() throws Exception {
        detailPresenter = spy(detailPresenter);

        doNothing().when(detailPresenter).saveFavorite(Mockito.any(MovieModel.class));

        detailPresenter.saveFavorite(Mockito.any(MovieModel.class));

        verify(detailPresenter, times(1)).saveFavorite(Mockito.any(MovieModel.class));
    }

}