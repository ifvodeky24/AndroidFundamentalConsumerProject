package com.example.cataloguemovieconsumer.rest;

import com.example.cataloguemovieconsumer.response.ResponseMovieDetail;
import com.example.cataloguemovieconsumer.response.ResponseMovieVideos;
import com.example.cataloguemovieconsumer.response.ResponseTvShowDetail;
import com.example.cataloguemovieconsumer.response.ResponseTvShowVideos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("movie/{movie_id}?append_to_response=credits,reviews")
    Call<ResponseMovieDetail> getMovieDetails(
            @Path("movie_id") int id,
            @Query("api_key") String api_key,
            @Query("language") String language
    );

    @GET("tv/{tv_id}?append_to_response=credits,reviews")
    Call<ResponseTvShowDetail> getTvDetails(
            @Path("tv_id") int id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{movie_id}/videos")
    Call<ResponseMovieVideos> getMovieVideos(
            @Path("movie_id") int id,
            @Query("api_key") String api_key
    );

    @GET("tv/{tv_id}/videos")
    Call<ResponseTvShowVideos> getTvVideos(
            @Path("tv_id") int id,
            @Query("api_key") String api_key
    );
}
