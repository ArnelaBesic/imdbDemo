package com.demo.imdb.json;

import com.demo.imdb.model.Actor;
import com.demo.imdb.model.Movie;

import java.util.List;
import java.util.stream.Collectors;

public class MovieResponse {
    private String imdbID;
    private String title;
    private String description;
    private Short releaseYear;
    //length in minutes
    private Short length;
    private List<ImageResponse> images;
    private List<String> cast;

    public MovieResponse() {
    }

    public MovieResponse(Movie movie) {
        this.imdbID = movie.getImdbID();
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.releaseYear = movie.getReleaseYear();
        this.length = movie.getLength();
        this.images = movie.getImages().stream().map(ImageResponse::new).collect(Collectors.toList());
        this.cast = movie.getCast().stream().map(Actor::getFullName).collect(Collectors.toList());
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Short getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Short releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Short getLength() {
        return length;
    }

    public void setLength(Short length) {
        this.length = length;
    }

    public List<ImageResponse> getImages() {
        return images;
    }

    public void setImages(List<ImageResponse> images) {
        this.images = images;
    }

    public List<String> getCast() {
        return cast;
    }

    public void setCast(List<String> cast) {
        this.cast = cast;
    }
}
