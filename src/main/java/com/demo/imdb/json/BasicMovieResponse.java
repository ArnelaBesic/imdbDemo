package com.demo.imdb.json;

import com.demo.imdb.model.Movie;

public class BasicMovieResponse implements Response {
    private String imdbId;
    private String title;
    private Short releaseYear;

    public BasicMovieResponse() {
    }

    public BasicMovieResponse(Movie movie) {
        this.imdbId = movie.getImdbId();
        this.title = movie.getTitle();
        this.releaseYear = movie.getReleaseYear();
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Short getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Short releaseYear) {
        this.releaseYear = releaseYear;
    }
}
