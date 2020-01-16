package com.demo.imdb.json;

import com.demo.imdb.model.Actor;

import java.util.List;
import java.util.stream.Collectors;

public class FullActorResponse extends BasicActorResponse {
    private List<ImageResponse> images;
    private List<BasicMovieResponse> movies;

    public FullActorResponse() {
        super();
    }

    public FullActorResponse(Actor actor) {
        super(actor);
        this.images = actor.getImages().stream().map(ImageResponse::new).collect(Collectors.toList());
        this.movies = actor.getMovies().stream().map(BasicMovieResponse::new).collect(Collectors.toList());
    }

    public List<ImageResponse> getImages() {
        return images;
    }

    public void setImages(List<ImageResponse> images) {
        this.images = images;
    }

    public List<BasicMovieResponse> getMovies() {
        return movies;
    }

    public void setMovies(List<BasicMovieResponse> movies) {
        this.movies = movies;
    }
}
