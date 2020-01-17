package com.demo.imdb.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "Actor")
public class Actor {
    private Long actorId;
    private String givenName;
    private String lastName;
    private LocalDate birthDate;
    private List<Image> images;
    private List<Movie> movies;

    public Actor() {
    }

    public Actor(Long actorId, String givenName, String lastName, LocalDate birthDate, List<Image> images, List<Movie> movies) {
        this.actorId = actorId;
        this.givenName = givenName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.images = images;
        this.movies = movies;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getActorId() {
        return actorId;
    }

    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    @NotNull
    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    @NotNull
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

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinTable(name = "ActorImage", joinColumns = @JoinColumn(name = "actorId"), inverseJoinColumns = @JoinColumn(name = "imageId"))
    @Fetch(value = FetchMode.SUBSELECT)
    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "cast")
    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    @Transient
    public String getFullName() {
        //givenName and lastName are @NotNull
        return givenName.concat(" ").concat(lastName);
    }
}
