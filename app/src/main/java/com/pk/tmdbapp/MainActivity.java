package com.pk.tmdbapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.pk.tmdbapp.activities.SettingsActivity;
import com.pk.tmdbapp.adapter.MoviesAdapter;
import com.pk.tmdbapp.api.Client;
import com.pk.tmdbapp.api.MovieAPIService;
import com.pk.tmdbapp.application.TMDbApplication;
import com.pk.tmdbapp.db.DBService;
import com.pk.tmdbapp.mvp.model.MovieModel;
import com.pk.tmdbapp.mvp.model.MoviesResponse;
import com.pk.tmdbapp.mvp.view.MainView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener, MainView {

    private SharedPreferences preferences;

    @Inject protected Realm mRealm;
    //@Inject protected Retrofit mRetrofit;

    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private List<MovieModel> movieList;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeContainer;
    public static final String LOG_TAG = MoviesAdapter.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TMDbApplication) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        initViews();
    }

    private void initViews() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching movies...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        movieList = new ArrayList<>();
        adapter = new MoviesAdapter(this, movieList);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.main_content);
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeContainer.setOnRefreshListener(() -> {
            initViews();
            Toast.makeText(MainActivity.this, "Movies Refreshed", Toast.LENGTH_SHORT).show();
        });

        checkSortOrder();
    }

    private void loadFavoriteMovies() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        movieList = new ArrayList<>();

        DBService dbService = new DBService();

        movieList.clear();

        dbService.getAll(mRealm, MovieModel.class).subscribe(movieModels -> movieList.addAll(movieModels));

        if (movieList.isEmpty()) {
            Toast.makeText(MainActivity.this, "You have no favorite movie added", Toast.LENGTH_SHORT).show();
            loadPopularMoviesJSON();
        }

        adapter = new MoviesAdapter(this, movieList);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        progressDialog.dismiss();

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void loadPopularMoviesJSON() {
        try {
            if (BuildConfig.TMDB_API_KEY.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please obtain API Key from themoviedb.com",
                        Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }

            MovieAPIService apiMovieService = Client.getClient().create(MovieAPIService.class);
            //MovieAPIService apiMovieService = mRetrofit.create(MovieAPIService.class);
            Call<MoviesResponse> call = apiMovieService.getPopularMovies(BuildConfig.TMDB_API_KEY);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                    List<MovieModel> movies = response.body().getResults();
                    recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
                    recyclerView.smoothScrollToPosition(0);
                    if (swipeContainer.isRefreshing()) {
                        swipeContainer.setRefreshing(false);
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error Fetching Data", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadTopRatedMoviesJSON() {
        try {
            if (BuildConfig.TMDB_API_KEY.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please obtain API Key from themoviedb.com",
                        Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }

            MovieAPIService apiMovieService = Client.getClient().create(MovieAPIService.class);
            Call<MoviesResponse> call = apiMovieService.getTopRatedMovies(BuildConfig.TMDB_API_KEY);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                    List<MovieModel> movies = response.body().getResults();
                    recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
                    recyclerView.smoothScrollToPosition(0);
                    if (swipeContainer.isRefreshing()) {
                        swipeContainer.setRefreshing(false);
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error Fetching Data", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public Activity getActivity() {
        Context context = this;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_most_popular:
                updatePreferences(
                        this.getString(R.string.pref_sort_order_key),
                        this.getString(R.string.pref_most_popular));
                return true;
            case R.id.menu_highest_rated:
                updatePreferences(
                        this.getString(R.string.pref_sort_order_key),
                        this.getString(R.string.pref_highest_rated));
                return true;
            case R.id.menu_favorite:
                updatePreferences(
                        this.getString(R.string.pref_sort_order_key),
                        this.getString(R.string.pref_favorite));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updatePreferences(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString(key, value);
        editor.apply();
        checkSortOrder();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(LOG_TAG, "Preferences updated");
        checkSortOrder();
    }

    private void checkSortOrder() {

        String sortOrder = preferences.getString(
                this.getString(R.string.pref_sort_order_key),
                this.getString(R.string.pref_most_popular)
        );
        if (sortOrder.equals(this.getString(R.string.pref_most_popular))) {
            Log.d(LOG_TAG, "Sorting by most popular");
            loadPopularMoviesJSON();
        } else if (sortOrder.equals(this.getString(R.string.pref_favorite))) {
            Log.d(LOG_TAG, "Sorting by favorite");
            loadFavoriteMovies();
        } else {
            Log.d(LOG_TAG, "Sorting by top rated");
            loadTopRatedMoviesJSON();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (movieList.isEmpty()) {
            checkSortOrder();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRealm != null) {
            mRealm.close();
        }
    }
}
