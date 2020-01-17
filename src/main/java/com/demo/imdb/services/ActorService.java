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

    public List<Actor> findAll() {
        return actorRepository.findAll();
    }

    //TODO
    public List<Actor> findAllWithPagination() {
        return actorRepository.findAll();
    }

    public Actor createOrUpdate(Actor actor) {
        Actor savedActor = actorRepository.save(actor);
        for (Movie movie : savedActor.getMovies()) {
            if (!movie.getCast().contains(savedActor)) {
                movie.getCast().add(savedActor);
                movieService.createOrUpdate(movie);
            }
        }
        return savedActor;
    }

    public void deleteById(Long id) {
        actorRepository.deleteById(id);
    }

    public Optional<Actor> findById(Long id) {
        return actorRepository.findById(id);
    }
}
