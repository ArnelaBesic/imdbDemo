package com.demo.imdb.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "Actor")
public class Actor {
    private Long actorID;
    private String givenName;
    private String lastName;
    private LocalDate birthDate;
    private List<Image> images;
    private List<Movie> movies;

    public Actor() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getActorID() {
        return actorID;
    }

    public void setActorID(Long actorID) {
        this.actorID = actorID;
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

    //TODO: lazy
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinTable(name = "ActorImage", joinColumns = @JoinColumn(name = "actorID"), inverseJoinColumns = @JoinColumn(name = "imageID"))
    @Fetch(value = FetchMode.SUBSELECT)
    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "cast")
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
