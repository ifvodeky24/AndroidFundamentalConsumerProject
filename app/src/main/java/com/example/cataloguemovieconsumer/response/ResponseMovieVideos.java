package com.example.cataloguemovieconsumer.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.example.cataloguemovieconsumer.model.MovieVideos;

import java.util.List;

public class ResponseMovieVideos {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<MovieVideos> results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MovieVideos> getResults() {
        return results;
    }

    public void setResults(List<MovieVideos> results) {
        this.results = results;
    }

}
