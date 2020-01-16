package com.demo.imdb.json;

import com.demo.imdb.model.Actor;

import java.time.LocalDate;

public class BasicActorResponse implements Response {
    private Long actorID;
    private String givenName;
    private String lastName;
    private LocalDate birthDate;

    public BasicActorResponse() {
    }

    public BasicActorResponse(Long actorID, String givenName, String lastName, LocalDate birthDate) {
        this.actorID = actorID;
        this.givenName = givenName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public BasicActorResponse(Actor actor) {
        this.actorID = actor.getActorID();
        this.givenName = actor.getGivenName();
        this.lastName = actor.getLastName();
        this.birthDate = actor.getBirthDate();
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
}
