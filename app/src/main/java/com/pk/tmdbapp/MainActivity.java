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
import com.pk.tmdbapp.db.realmmodel.RealmMovie;
import com.pk.tmdbapp.mvp.model.MovieModel;
import com.pk.tmdbapp.mvp.model.MoviesResponse;
import com.pk.tmdbapp.mvp.view.MainView;
import com.pk.tmdbapp.util.RealmMapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener, MainView {

    private SharedPreferences preferences;
    private boolean favorite_menu = false;

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

        DBService dbService = new DBService();

        List<MovieModel> movies = new ArrayList<>();

        dbService.getAll(mRealm, RealmMovie.class).subscribe(movieModels ->
                movies.addAll(RealmMapper.mapToMovieModelList(movieModels)));

        if (movies.isEmpty()) {
            Toast.makeText(MainActivity.this, "You have no favorite movie added", Toast.LENGTH_SHORT).show();
            updateSortPreferences(
                    this.getString(R.string.pref_sort_order_key),
                    this.getString(R.string.pref_most_popular));
            checkSortOrder();
        }
        recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.smoothScrollToPosition(0);
        if (swipeContainer.isRefreshing()) {
            swipeContainer.setRefreshing(false);
        }
        progressDialog.dismiss();

        Toast.makeText(MainActivity.this, "Favorite Movies", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(MainActivity.this, "Most Popular Movies", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(MainActivity.this, "Top Rated Movies", Toast.LENGTH_SHORT).show();
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (favorite_menu) {
            getMenuInflater().inflate(R.menu.menu_favorite, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_most_popular:
                updateSortPreferences(
                        this.getString(R.string.pref_sort_order_key),
                        this.getString(R.string.pref_most_popular));
                return true;
            case R.id.menu_highest_rated:
                updateSortPreferences(
                        this.getString(R.string.pref_sort_order_key),
                        this.getString(R.string.pref_highest_rated));
                return true;
            case R.id.menu_favorite:
                updateSortPreferences(
                        this.getString(R.string.pref_sort_order_key),
                        this.getString(R.string.pref_favorite));
                return true;
            case R.id.menu_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSortPreferences(String key, String value) {
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
            favorite_menu = false;
            invalidateOptionsMenu();
            loadPopularMoviesJSON();
        } else if (sortOrder.equals(this.getString(R.string.pref_favorite))) {
            Log.d(LOG_TAG, "Sorting by favorite");
            favorite_menu = true;
            invalidateOptionsMenu();
            loadFavoriteMovies();
        } else {
            Log.d(LOG_TAG, "Sorting by top rated");
            favorite_menu = false;
            invalidateOptionsMenu();
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
