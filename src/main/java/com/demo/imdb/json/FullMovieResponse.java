package com.demo.imdb.json;

import com.demo.imdb.model.Movie;

import java.util.List;
import java.util.stream.Collectors;

public class FullMovieResponse extends BasicMovieResponse {
    private String description;
    //length in minutes
    private Short length;
    private List<ImageResponse> images;
    private List<BasicActorResponse> cast;

    public FullMovieResponse() {
        super();
    }

    public FullMovieResponse(Movie movie) {
        super(movie);
        this.description = movie.getDescription();
        this.length = movie.getLength();
        this.images = movie.getImages().stream().map(ImageResponse::new).collect(Collectors.toList());
        this.cast = movie.getCast().stream().map(BasicActorResponse::new).collect(Collectors.toList());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<BasicActorResponse> getCast() {
        return cast;
    }

    public void setCast(List<BasicActorResponse> cast) {
        this.cast = cast;
    }
}
