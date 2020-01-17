package com.demo.imdb.services;

import com.demo.imdb.model.Actor;
import com.demo.imdb.model.Movie;
import com.demo.imdb.repositories.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActorService {
    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private MovieService movieService;

    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    //TODO
    public List<Actor> getAllActorsWithPagination() {
        return actorRepository.findAll();
    }

    public Actor createOrUpdateActor(Actor actor) {
        Actor savedActor = actorRepository.save(actor);
        for (Movie movie : savedActor.getMovies()) {
            if (!movie.getCast().contains(savedActor)) {
                movie.getCast().add(savedActor);
                movieService.createOrUpdateMovie(movie);
            }
        }
        return savedActor;
    }

    public void deleteActorByID(Long id) {
        actorRepository.deleteById(id);
    }

    public Optional<Actor> findActorById(Long id) {
        return actorRepository.findById(id);
    }
}
