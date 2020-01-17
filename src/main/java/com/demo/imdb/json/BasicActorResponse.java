package com.demo.imdb.json;

import com.demo.imdb.model.Actor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;

public class BasicActorResponse implements Response {
    private Long actorId;
    private String givenName;
    private String lastName;
    private LocalDate birthDate;

    public BasicActorResponse() {
    }

    public BasicActorResponse(Actor actor) {
        this.actorId = actor.getActorId();
        this.givenName = actor.getGivenName();
        this.lastName = actor.getLastName();
        this.birthDate = actor.getBirthDate();
    }

    public Long getActorId() {
        return actorId;
    }

    public void setActorId(Long actorId) {
        this.actorId = actorId;
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

    @JsonIgnore
    public String getFullName() {
        return givenName.concat(" ").concat(lastName);
    }
}
