package com.demo.imdb.json;

import com.demo.imdb.model.Actor;
import com.demo.imdb.model.Movie;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ActorResponse {
    private Long actorID;
    private String givenName;
    private String lastName;
    private LocalDate birthDate;
    private List<ImageResponse> images;
    private List<String> movies;

    public ActorResponse() {
    }

    public ActorResponse(Actor actor) {
        this.actorID = actor.getActorID();
        this.givenName = actor.getGivenName();
        this.lastName = actor.getLastName();
        this.birthDate = actor.getBirthDate();
        this.images = actor.getImages().stream().map(ImageResponse::new).collect(Collectors.toList());
        this.movies = actor.getMovies().stream().map(Movie::getFullName).collect(Collectors.toList());
    }

    public Long getActorID() {
        return actorID;
    }

    public void setActorID(Long actorID) {
        this.actorID = actorID;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<ImageResponse> getImages() {
        return images;
    }

    public void setImages(List<ImageResponse> images) {
        this.images = images;
    }

    public List<String> getMovies() {
        return movies;
    }

    public void setMovies(List<String> movies) {
        this.movies = movies;
    }
}
