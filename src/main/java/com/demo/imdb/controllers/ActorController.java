package com.demo.imdb.controllers;

import com.demo.imdb.json.*;
import com.demo.imdb.model.Actor;
import com.demo.imdb.model.Image;
import com.demo.imdb.model.Movie;
import com.demo.imdb.services.ActorService;
import com.demo.imdb.services.ImageService;
import com.demo.imdb.services.MovieService;
import com.demo.imdb.util.Messages;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/imdb/actors")
public class ActorController {
    @Autowired
    private ActorService actorService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private MovieService movieService;
    private static final Logger logger = Logger.getLogger(ActorController.class.getName());


    //e.g. http://localhost:8080/imdb/actors/all/
    @GetMapping(path = "/all/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Response> getAllActors() {
        List<Actor> actors = actorService.getAllActors();
        if (CollectionUtils.isEmpty(actors)) {
            return Collections.singletonList(new Message(Messages.NO_DATA, Status.OK));
        }
        return actors.stream().map(FullActorResponse::new).collect(Collectors.toList());
    }

    //e.g. http://localhost:8080/imdb/actors/paginatedList/
    @GetMapping(path = "/paginate/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Response> getPaginatedActorList() {
        List<Actor> actors = actorService.getAllActors();
        if (CollectionUtils.isEmpty(actors)) {
            return Collections.singletonList(new Message(Messages.NO_DATA, Status.OK));
        }
        return actors.stream().map(FullActorResponse::new).collect(Collectors.toList());
    }

    //e.g. http://localhost:8080/imdb/actors/actor/1
    @GetMapping(path = "/actor/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<? extends Response> findActorById(@PathVariable("id") Long id) {
        Optional<Actor> actor = actorService.findActorById(id);
        if (actor.isPresent()) {
            FullActorResponse fullActorResponse = new FullActorResponse(actor.get());
            return ResponseEntity.ok(fullActorResponse);
        }
        Message message = new Message(String.format(Messages.ACTOR_NOT_FOUND, id), Status.ERROR);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    //e.g. http://localhost:8080/imdb/actors/
    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createActor(@RequestBody FullActorResponse actorResponse) {
        List<Response> responses = new ArrayList<>();
        if (actorResponse.getActorID() != null) {
            Message message = new Message(Messages.USE_PUT_FOR_RECORD_UPDATE, Status.ERROR);
            responses.add(message);
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(responses);
        }

        Actor savedActor = persist(actorResponse, responses);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedActor.getActorID())
                .toUri();
        return ResponseEntity.created(location).body(responses);
    }

    //e.g. http://localhost:8080/imdb/actors/
    @PutMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateActor(@RequestBody FullActorResponse actor) {
        List<Response> responses = new ArrayList<>();
        boolean shouldNotProceed = actor.getActorID() == null || !actorService.findActorById(actor.getActorID()).isPresent();
        if (shouldNotProceed) {
            Message message = new Message(Messages.USE_POST_FOR_RECORD_CREATION, Status.ERROR);
            responses.add(message);
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(responses);
        }
        Actor savedActor = persist(actor, responses);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedActor.getActorID())
                .toUri();
        return ResponseEntity.status(HttpStatus.OK).location(location).body(responses);
    }

    //e.g. http://localhost:8080/imdb/actors/actor/1
    @DeleteMapping(path = "/actor/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteActorByID(@PathVariable("id") Long id) {
        try {
            Optional<Actor> optionalActor = actorService.findActorById(id);
            if (!optionalActor.isPresent()) {
                Message message = new Message(String.format(Messages.ACTOR_NOT_FOUND, id), Status.ERROR);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
            }
            Actor actor = optionalActor.get();
            List<Movie> movies = actor.getMovies();
            for (Movie movie : movies) {
                movie.getCast().remove(actor);
                movieService.createOrUpdateMovie(movie);
            }
            actorService.deleteActorByID(id);
        } catch (ConstraintViolationException exc) {
            logger.log(Level.INFO, String.format(Messages.DELETE_EXCEPTION, Actor.class.getSimpleName(), id, exc.getCause()));
            Message message = new Message(String.format(Messages.CONSTRAINT_VIOLATION_EXCEPTION, id), Status.ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        } catch (EmptyResultDataAccessException exc) {
            logger.log(Level.INFO, String.format(Messages.DELETE_EXCEPTION, Actor.class.getSimpleName(), id, exc.getCause()));
            Message message = new Message(String.format(Messages.ACTOR_NOT_FOUND, id), Status.ERROR);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        Message message = new Message(String.format(Messages.SUCCESSFUL_DELETION, id), Status.OK);
        return ResponseEntity.ok(message);
    }

    private Actor persist(FullActorResponse fullActorResponse, List<Response> responses) {
        List<Image> images = imageService.convertAndPersistAsImage(fullActorResponse.getImages(), responses);
        List<Movie> movies = convertAndPersistAsMovie(fullActorResponse.getMovies(), responses);

        Actor actor = new Actor(fullActorResponse.getActorID(), fullActorResponse.getGivenName(), fullActorResponse.getLastName(), fullActorResponse.getBirthDate(), images, movies);
        return actorService.createOrUpdateActor(actor);
    }

    private List<Movie> convertAndPersistAsMovie(List<BasicMovieResponse> movieResponses, List<Response> messages) {
        if (CollectionUtils.isEmpty(movieResponses)) {
            return new ArrayList<>();
        }
        List<Movie> movies = new ArrayList<>();
        for (BasicMovieResponse movieResponse : movieResponses) {
            String movieID = movieResponse.getImdbID();
            if (movieID == null) {
                Message message = new Message(String.format(Messages.USE_MOVIE_API_TO_CREATE_NEW_MOVIE, movieResponse.getTitle()), Status.ERROR);
                messages.add(message);
                continue;

            } else {
                Optional<Movie> movie = movieService.findMovieByImdbID(movieID);
                if (!movie.isPresent()) {
                    Message message = new Message(String.format(Messages.MOVIE_NOT_FOUND, movieID), Status.ERROR);
                    messages.add(message);
                    logger.log(Level.INFO, String.format(Messages.MOVIE_NOT_FOUND, movieID));
                }
                movie.ifPresent(movies::add);
            }
        }
        return movies;
    }
}