package com.demo.imdb.services;

import com.demo.imdb.model.Actor;
import com.demo.imdb.repositories.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActorService {
    @Autowired
    private ActorRepository actorRepository;

    //TODO: persistent
    public List<Actor> list() {
        return actorRepository.findAll();
    }
}
