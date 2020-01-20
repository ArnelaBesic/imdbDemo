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


    /**
     * e.g. http://localhost:8080/imdb/actors/all/
     *
     * @return List<Response>  - all actors from database if present.
     * Otherwise, return message that there is no records.
     * HTTP Status in both cases is 200
     */
    @GetMapping(path = "/all/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Response> findAll() {
        List<Actor> actors = actorService.findAll();
        if (CollectionUtils.isEmpty(actors)) {
            return Collections.singletonList(new Message(Messages.NO_DATA, Status.OK));
        }
        return actors.stream().map(FullActorResponse::new).collect(Collectors.toList());
    }

    /**
     * e.g. http://localhost:8080/imdb/actors/paginate/
     *
     * @return
     */
    @GetMapping(path = "/paginate/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Response> findAllPaginated() {
        //TODO: add pagination
        List<Actor> actors = actorService.findAll();
        if (CollectionUtils.isEmpty(actors)) {
            return Collections.singletonList(new Message(Messages.NO_DATA, Status.OK));
        }
        return actors.stream().map(FullActorResponse::new).collect(Collectors.toList());
    }

    /**
     * Find actor with id same as path variable, e.g. http://localhost:8080/imdb/actors/actor/1
     *
     * @param id by which actor will be searched for
     * @return ResponseEntity<Object> containing Actor if present and HTTP status 200.
     * Otherwise, status NOT_FOUND and appropriate message
     */
    @GetMapping(path = "/actor/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<? extends Response> findById(@PathVariable("id") Long id) {
        Optional<Actor> actor = actorService.findById(id);
        if (actor.isPresent()) {
            FullActorResponse fullActorResponse = new FullActorResponse(actor.get());
            return ResponseEntity.ok(fullActorResponse);
        }
        Message message = new Message(String.format(Messages.ACTOR_NOT_FOUND, id), Status.ERROR);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    /**
     * Creates new actor with parameters from request body,
     * e.g. http://localhost:8080/imdb/actors/.
     * For all sent images, check if they are already present in database.
     * If yes, add them to the actor. Otherwise create new Image and
     * than add to the actor.
     * Movies cannot be created with this call.
     * If movie is present in database, it is added to the actor, otherwise it is skipped.
     *
     * @param actorResponse - json wrapper for Actor entity
     * @return ResponseEntity<Object> :
     * - HTTP status METHOD_NOT_ALLOWED in case that actor id was sent in actorResponse.
     * - list of errors connected to the movie and images validations
     */
    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> create(@RequestBody FullActorResponse actorResponse) {
        List<Response> responses = new ArrayList<>();
        if (actorResponse.getActorId() != null) {
            Message message = new Message(Messages.USE_PUT_FOR_RECORD_UPDATE, Status.ERROR);
            responses.add(message);
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(responses);
        }

        Actor actor = persist(actorResponse, responses);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/actor/{id}")
                .buildAndExpand(actor.getActorId())
                .toUri();
        return ResponseEntity.created(location).body(responses);
    }

    /**
     * Updates actor with parameters from request body,
     * e.g. http://localhost:8080/imdb/actors/.
     * For all sent images, check if they are already present in database.
     * If yes, add them to the actor. Otherwise create new Image and
     * than add to the actor.
     * Movies cannot be created with this call.
     * If movie is present in database, it is added to the actor, otherwise it is skipped.
     *
     * @param actorResponse - json wrapper for Actor entity
     * @return ResponseEntity<Object> :
     * - HTTP status METHOD_NOT_ALLOWED in case that actor id was not sent in actorResponse.
     * - list of errors connected to the movie and images validations
     */
    @PutMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> update(@RequestBody FullActorResponse actorResponse) {
        List<Response> responses = new ArrayList<>();
        boolean shouldNotProceed = actorResponse.getActorId() == null || !actorService.findById(actorResponse.getActorId()).isPresent();
        if (shouldNotProceed) {
            Message message = new Message(Messages.USE_POST_FOR_RECORD_CREATION, Status.ERROR);
            responses.add(message);
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(responses);
        }
        Actor actor = persist(actorResponse, responses);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/actor/{id}")
                .buildAndExpand(actor.getActorId())
                .toUri();
        return ResponseEntity.status(HttpStatus.OK).location(location).body(responses);
    }

    /**
     * Deletes actor with id same as path variable, e.g. http://localhost:8080/imdb/actors/actor/1
     *
     * @param id by which actor will be deleted
     * @return ResponseEntity<Object> :
     * - Http status NOT_FOUND and appropriate message when actor with requested id is not found in database
     * - HTTP status INTERNAL_SERVER_ERROR and appropriate message when deletion was unsuccessful
     * - HTTP status OK and appropriate message when deletion was successful
     */
    @DeleteMapping(path = "/actor/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteById(@PathVariable("id") Long id) {
        try {
            Optional<Actor> optionalActor = actorService.findById(id);
            if (!optionalActor.isPresent()) {
                Message message = new Message(String.format(Messages.ACTOR_NOT_FOUND, id), Status.ERROR);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
            }
            Actor actor = optionalActor.get();
            List<Movie> movies = actor.getMovies();
            for (Movie movie : movies) {
                movie.getCast().remove(actor);
                movieService.createOrUpdate(movie);
            }
            actorService.deleteById(id);
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

    /**
     * Executes conversion from json wrapper for Actor to Actor entity that can be saved to
     * database. Same is done for connected objects Image and Movie.
     *
     * @param actorResponse - json wrapper for Actor entity
     * @param responses-    reference to list of all response messages that will be sent back to client
     * @return Actor that is fully persisted in database
     */
    private Actor persist(FullActorResponse actorResponse, List<Response> responses) {
        List<Image> images = imageService.convertAndPersistAsImage(actorResponse.getImages(), responses);
        List<Movie> movies = convertAndPersistAsMovie(actorResponse.getMovies(), responses);

        Actor actor = new Actor(actorResponse.getActorId(), actorResponse.getGivenName(), actorResponse.getLastName(), actorResponse.getBirthDate(), images, movies);
        return actorService.createOrUpdate(actor);
    }

    /**
     * Movies cannot be created with this method.
     * If movie is present in database, it is added to the list of movies, otherwise it is skipped.
     *
     * @param movieResponses - json wrapper for Movie entity
     * @param responses      - reference to list of all response messages that will be sent back to client
     * @return List<Movie> - fully persisted list of movies
     */
    private List<Movie> convertAndPersistAsMovie(List<BasicMovieResponse> movieResponses, List<Response> responses) {
        if (CollectionUtils.isEmpty(movieResponses)) {
            return new ArrayList<>();
        }
        List<Movie> movies = new ArrayList<>();
        for (BasicMovieResponse movieResponse : movieResponses) {
            String movieId = movieResponse.getImdbId();
            if (movieId == null) {
                Message message = new Message(String.format(Messages.USE_MOVIE_API_TO_CREATE_NEW_MOVIE, movieResponse.getTitle()), Status.ERROR);
                responses.add(message);
            } else {
                Optional<Movie> movie = movieService.findByImdbId(movieId);
                if (!movie.isPresent()) {
                    Message message = new Message(String.format(Messages.MOVIE_NOT_FOUND, movieId), Status.ERROR);
                    responses.add(message);
                    logger.log(Level.INFO, String.format(Messages.MOVIE_NOT_FOUND, movieId));
                }
                movie.ifPresent(movies::add);
            }
        }
        return movies;
    }
}