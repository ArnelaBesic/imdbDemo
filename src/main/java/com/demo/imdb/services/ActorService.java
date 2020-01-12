package com.demo.imdb.services;

import com.demo.imdb.model.Actor;
import com.demo.imdb.repositories.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActorService {
    @Autowired
    private ActorRepository actorRepository;

    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    //TODO
    public List<Actor> getAllActorsWithPagination() {
        return actorRepository.findAll();
    }

    //TODO
    public Actor createOrUpdateActor(Actor actor) {
        return actorRepository.save(actor);
        //if new, check if exists by name, last name and year
    }

    //TODO: odgovor, try catch
    public void deleteActor(Actor actor) {
        actorRepository.delete(actor);
    }

    public void deleteActorByID(Long id) {
        actorRepository.deleteById(id);
    }

    public Optional<Actor> findActorById(Long id) {
        return actorRepository.findById(id);
    }
}
