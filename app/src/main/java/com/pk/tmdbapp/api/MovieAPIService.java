package com.pk.tmdbapp.api;

import com.pk.tmdbapp.mvp.model.MoviesResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ace on 10/02/2017.
 */

public interface MovieAPIService {

    @GET("movie/popular")
    Observable<MoviesResponse> getPopularMoviesObs(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Observable<MoviesResponse> getTopRatedMoviesObs(@Query("api_key") String apiKey);
}
