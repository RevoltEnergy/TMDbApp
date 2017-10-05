package com.pk.tmdbapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.pk.tmdbapp.R;
import com.pk.tmdbapp.application.TMDbApplication;
import com.pk.tmdbapp.db.DBService;
import com.pk.tmdbapp.db.realmmodel.RealmMovie;
import com.pk.tmdbapp.mvp.model.MovieModel;
import com.pk.tmdbapp.util.RealmMapper;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by ace on 10/02/2017.
 */

public class DetailActivity extends AppCompatActivity {

    @Inject Realm mRealm;

    TextView nameOfMovie;
    TextView plotSynopsis;
    TextView userRating;
    TextView releaseDate;
    ImageView imageView;
    ProgressBar progressBar;
    ThreeBounce threeBounce;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ((TMDbApplication) getApplication()).getAppComponent().inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initCollapsingToolbar();

        DBService dbService = new DBService();

        MovieModel movieModel = new MovieModel();

        imageView = (ImageView) findViewById(R.id.thumbnail_image_header);
        nameOfMovie = (TextView) findViewById(R.id.title);
        plotSynopsis = (TextView) findViewById(R.id.plot_synopsis);
        userRating = (TextView) findViewById(R.id.user_rating);
        releaseDate = (TextView) findViewById(R.id.release_date);

        progressBar = (ProgressBar) findViewById(R.id.progress_2);
        threeBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(threeBounce);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("original_title")) {
            String thumbnail = getIntent().getExtras().getString("poster_path");
            String movieName = getIntent().getExtras().getString("original_title");
            String synopsis = getIntent().getExtras().getString("overview");
            String rating = getIntent().getExtras().getString("vote_average");
            String dateOfRelease = getIntent().getExtras().getString("release_date");

            movieModel.setTitle(getIntent().getExtras().getString("title"));
            movieModel.setOriginalTitle(getIntent().getExtras().getString("original_title"));
            movieModel.setVoteAverage(Double.valueOf(rating));
            movieModel.setReleaseDate(dateOfRelease);
            movieModel.setPosterPath(thumbnail);
            movieModel.setOverview(synopsis);

            String baseImageUrl = "https://image.tmdb.org/t/p/w500";

            Glide.with(this)
                    .load(baseImageUrl + thumbnail)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageView);

            nameOfMovie.setText(movieName);
            plotSynopsis.setText(synopsis);
            userRating.setText(rating);
            releaseDate.setText(dateOfRelease);
        } else {
            Toast.makeText(this, "No API Data", Toast.LENGTH_SHORT).show();
        }

        MaterialFavoriteButton materialFavoriteButton =
                (MaterialFavoriteButton) findViewById(R.id.favorite_button);

        materialFavoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            SharedPreferences.Editor editor =
                    getSharedPreferences("com.pk.tmdbapp.activities.DetailActivity", MODE_PRIVATE).edit();
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {

                if (favorite) {
                    editor.putBoolean("Favorite Added", true);
                    editor.apply();
                    saveFavorite(dbService, movieModel);
                    Snackbar.make(buttonView, "Added to Favorite", Snackbar.LENGTH_SHORT).show();
                } else {
                    editor.putBoolean("Favorite Removed", true);
                    editor.apply();
                    removeFavorite(dbService, movieModel);
                    Snackbar.make(buttonView, "Removed from Favorite", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void removeFavorite(DBService dbService, MovieModel movieModel) {
        dbService.remove(mRealm, RealmMapper.mapToRealmMovie(movieModel))
                .subscribe(movieModelConsumer -> Toast.makeText(getBaseContext(), "Removed", Toast.LENGTH_SHORT).show());
    }

    private void saveFavorite(DBService dbService, MovieModel movieModel) {
        dbService.save(mRealm, RealmMapper.mapToRealmMovie(movieModel), RealmMovie.class)
                .subscribe(movieModelConsumer -> Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_SHORT).show());
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShown = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(getString(R.string.movie_details));
                    isShown = true;
                } else if (isShown) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShown = false;
                }
            }
        });
    }
}
