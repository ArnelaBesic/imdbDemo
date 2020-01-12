package com.demo.imdb.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/*Fields that are marked as NotNull are minimal requirement for movie creation. Other fields are not not marked as NotNull so that we can handle
case when movie is not yet released and not all data is known.
 */
@Entity(name = "Movie")
public class Movie {
    //as identifier use imdbID. example imdbID = tt5275828
    private String imdbID;
    private String title;
    private String description;
    private Short releaseYear;
    //length in minutes
    private Short length;
    private List<Image> images;
    private List<Actor> cast;

    public Movie() {
    }

    //TODO: how to create ID
    @Id
    @Column(unique = true)
    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    @NotNull
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

    @NotNull
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

    //TODO: eager
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name = "MovieImage", joinColumns = @JoinColumn(name = "imdbID"), inverseJoinColumns = @JoinColumn(name = "imageID"))
    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    //TODO etch = FetchType.LAZY
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "MovieActor", joinColumns = @JoinColumn(name = "imdbID"), inverseJoinColumns = @JoinColumn(name = "actorID"))
    public List<Actor> getCast() {
        return cast;
    }

    public void setCast(List<Actor> cast) {
        this.cast = cast;
    }

    @Transient
    public String getFullName() {
        return title.concat(" (").concat(String.valueOf(releaseYear)).concat(")");
    }
}
