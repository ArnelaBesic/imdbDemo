package com.demo.imdb.json;

import com.demo.imdb.model.Movie;

public class BasicMovieResponse implements Response {
    private String imdbID;
    private String title;
    private Short releaseYear;

    public BasicMovieResponse() {
    }

    public BasicMovieResponse(String imdbID, String title, Short releaseYear) {
        this.imdbID = imdbID;
        this.title = title;
        this.releaseYear = releaseYear;
    }

    public BasicMovieResponse(Movie movie) {
        this.imdbID = movie.getImdbID();
        this.title = movie.getTitle();
        this.releaseYear = movie.getReleaseYear();
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
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
