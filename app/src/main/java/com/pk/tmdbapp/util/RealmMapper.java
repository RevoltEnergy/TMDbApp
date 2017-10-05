package com.pk.tmdbapp.util;

import com.pk.tmdbapp.db.realmmodel.RealmMovie;
import com.pk.tmdbapp.mvp.model.MovieModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ace on 10/05/2017.
 */

public class RealmMapper {

    public static RealmMovie mapToRealmMovie(MovieModel movieModel) {

        RealmMovie realmMovie = new RealmMovie();
        realmMovie.setPosterPath(movieModel.getPosterPath());
        realmMovie.setTitle(movieModel.getTitle());
        realmMovie.setOriginalTitle(movieModel.getOriginalTitle());
        realmMovie.setVoteAverage(movieModel.getVoteAverage());
        realmMovie.setReleaseDate(movieModel.getReleaseDate());
        realmMovie.setOverview(movieModel.getOverview());

        return realmMovie;
    }

    public static MovieModel mapToMovieModel(RealmMovie realmMovie) {

        MovieModel movieModel = new MovieModel();
        movieModel.setPosterPath(realmMovie.getPosterPath());
        movieModel.setTitle(realmMovie.getTitle());
        movieModel.setOriginalTitle(realmMovie.getOriginalTitle());
        movieModel.setVoteAverage(realmMovie.getVoteAverage());
        movieModel.setReleaseDate(realmMovie.getReleaseDate());
        movieModel.setOverview(realmMovie.getOverview());

        return movieModel;
    }

    public static List<RealmMovie> mapToRealmMovieList(List<MovieModel> movieModels) {
        List<RealmMovie> realmMovies = new ArrayList<>();
        for (MovieModel movieModel : movieModels) {
            realmMovies.add(RealmMapper.mapToRealmMovie(movieModel));
        }

        return realmMovies;
    }

    public static List<MovieModel> mapToMovieModelList(List<RealmMovie> realmMovies) {
        List<MovieModel> movieModels = new ArrayList<>();
        for (RealmMovie realmMovie : realmMovies) {
            movieModels.add(RealmMapper.mapToMovieModel(realmMovie));
        }

        return movieModels;
    }
}
