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
import org.springframework.util.StringUtils;
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
@RequestMapping(path = "/imdb/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ActorService actorService;
    private static final Logger logger = Logger.getLogger(MovieController.class.getName());

    /**
     * e.g. http://localhost:8080/imdb/movies/list/
     *
     * @return List<Response>  - all movies from database if present.
     * Otherwise, return message that there is no records.
     * HTTP Status in both cases is 200
     */
    @GetMapping(path = "/all/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Response> findAll() {
        List<Movie> movies = movieService.findAll();
        if (CollectionUtils.isEmpty(movies)) {
            return Collections.singletonList(new Message(Messages.NO_DATA, Status.OK));
        }
        return movies.stream().map(FullMovieResponse::new).collect(Collectors.toList());
    }

    /**
     * e.g. http://localhost:8080/imdb/movies/paginate/
     *
     * @return
     */
    @GetMapping(path = "/paginate/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Response> findAllPaginated() {
        //TODO: pagination
        List<Movie> movies = movieService.findAll();
        if (CollectionUtils.isEmpty(movies)) {
            return Collections.singletonList(new Message(Messages.NO_DATA, Status.OK));
        }
        return movies.stream().map(FullMovieResponse::new).collect(Collectors.toList());
    }

    /**
     * Find movie with IMDB id same as path variable, e.g.  http://localhost:8080/imdb/movies/movie/tt5275828
     *
     * @param imdbId by which movie will be searched for
     * @return ResponseEntity<Object> containing Movie if present and HTTP status 200.
     * Otherwise, status NOT_FOUND and appropriate message
     */
    @GetMapping(path = "/movie/{imdbId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<? extends Response> findByImdbId(@PathVariable("imdbId") String imdbId) {
        Optional<Movie> movie = movieService.findByImdbId(imdbId);
        if (movie.isPresent()) {
            FullMovieResponse fullMovieResponse = new FullMovieResponse(movie.get());
            return ResponseEntity.ok(fullMovieResponse);
        }
        Message message = new Message(String.format(Messages.MOVIE_NOT_FOUND, imdbId), Status.ERROR);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    /**
     * Find movie with title and releaseYear id same as request parameters
     * e.g.   http://localhost:8080/imdb/movies/?title=Lo&year=2016
     * If no parameters are sent, returns all movies.
     *
     * @param title by which movie will be searched for
     * @param year  release year by which movie will be searched for
     * @return List<Response> containing list of movies any is found and HTTP status 200.
     * Otherwise, status OK and message that data is not found
     */
    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Response> findByTitleAndReleaseYear(@RequestParam(required = false) String title, @RequestParam(required = false) Short year) {
        List<Movie> movies = movieService.findByTitleAndReleaseYear(title, year);
        if (CollectionUtils.isEmpty(movies)) {
            return Collections.singletonList(new Message(Messages.NO_DATA, Status.OK));
        }
        return movies.stream().map(FullMovieResponse::new).collect(Collectors.toList());
    }

    /**
     * Creates new movie with parameters from request body,
     * e.g. http://localhost:8080/imdb/movies/.
     * For all sent images, check if they are already present in database.
     * If yes, add them to the actor. Otherwise create new Image and
     * than add to the actor.
     * Actors cannot be created with this call.
     * If actor is present in database, it is added to the movie, otherwise it is skipped.
     *
     * @param movieResponse json wrapper for Movie entity
     * @return ResponseEntity<Object> :
     * - HTTP status METHOD_NOT_ALLOWED in case that movie id was sent in actorResponse.
     * - list of errors connected to the actor and images validations
     */
    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> create(@RequestBody FullMovieResponse movieResponse) {
        List<Response> responses = new ArrayList<>();
        boolean shouldNotProceed = !StringUtils.isEmpty(movieResponse.getImdbId());
        if (shouldNotProceed) {
            Message message = new Message(Messages.USE_PUT_FOR_RECORD_UPDATE, Status.ERROR);
            responses.add(message);
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(responses);
        }
        Movie movie = persist(movieResponse, responses);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/movie/{id}")
                .buildAndExpand(movie.getImdbId())
                .toUri();
        return ResponseEntity.created(location).body(responses);
    }

    /**
     * Updates movie with parameters from request body,
     * e.g. http://localhost:8080/imdb/movies/
     * For all sent images, check if they are already present in database.
     * If yes, add them to the actor. Otherwise create new Image and
     * than add to the actor.
     * Actors cannot be created with this call.
     * If actor is present in database, it is added to the movie, otherwise it is skipped.
     *
     * @param movieResponse - json wrapper for Movie entity
     * @return ResponseEntity<Object> :
     * - HTTP status METHOD_NOT_ALLOWED in case that movie id was not sent in movieResponse.
     * - list of errors connected to the actor and images validations
     */
    @PutMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> update(@RequestBody FullMovieResponse movieResponse) {
        List<Response> responses = new ArrayList<>();
        boolean shouldNotProceed = StringUtils.isEmpty(movieResponse.getImdbId()) || !movieService.findByImdbId(movieResponse.getImdbId()).isPresent();
        if (shouldNotProceed) {
            Message message = new Message(Messages.USE_POST_FOR_RECORD_CREATION, Status.ERROR);
            responses.add(message);
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(responses);
        }
        Movie movie = persist(movieResponse, responses);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/movie/{id}")
                .buildAndExpand(movie.getImdbId())
                .toUri();
        return ResponseEntity.status(HttpStatus.OK).location(location).body(responses);
    }

    /**
     * Deletes movie with id same as path variable, e.g. http://localhost:8080/imdb/movies/movie/tt5275828
     *
     * @param imdbId by which movie will be deleted
     * @return ResponseEntity<Object> :
     * - Http status NOT_FOUND and appropriate message when movie with requested id is not found in database
     * - HTTP status INTERNAL_SERVER_ERROR and appropriate message when deletion was unsuccessful
     * - HTTP status OK and appropriate message when deletion was successful
     */
    @DeleteMapping(path = "/movie/{imdbId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> deleteById(@PathVariable("imdbId") String imdbId) {
        try {
            Optional<Movie> optionalMovie = movieService.findByImdbId(imdbId);
            if (!optionalMovie.isPresent()) {
                Message message = new Message(String.format(Messages.MOVIE_NOT_FOUND, imdbId), Status.ERROR);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
            }
            movieService.deleteById(imdbId);
        } catch (ConstraintViolationException exc) {
            logger.log(Level.INFO, String.format(Messages.DELETE_EXCEPTION, Movie.class.getSimpleName(), imdbId, exc.getCause()));
            Message message = new Message(String.format(Messages.CONSTRAINT_VIOLATION_EXCEPTION, imdbId), Status.ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        } catch (EmptyResultDataAccessException exc) {
            logger.log(Level.INFO, String.format(Messages.DELETE_EXCEPTION, Movie.class.getSimpleName(), imdbId, exc.getCause()));
            Message message = new Message(String.format(Messages.MOVIE_NOT_FOUND, imdbId), Status.ERROR);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        Message message = new Message(String.format(Messages.SUCCESSFUL_DELETION, imdbId), Status.OK);
        return ResponseEntity.ok(message);
    }

    /**
     * Executes conversion from json wrapper for Movie to Movie entity that can be saved to
     * database. Same is done for connected objects Image and Actor.
     *
     * @param movieResponse - json wrapper for Movie entity
     * @param responses-    reference to list of all response messages that will be sent back to client
     * @return Movie that is fully persisted in database
     */
    private Movie persist(FullMovieResponse movieResponse, List<Response> responses) {
        List<Image> images = imageService.convertAndPersistAsImage(movieResponse.getImages(), responses);
        List<Actor> actors = convertAndPersistAsActor(movieResponse.getCast(), responses);

        Movie movie = new Movie(movieResponse.getImdbId(), movieResponse.getTitle(), movieResponse.getDescription(), movieResponse.getReleaseYear(), movieResponse.getLength(), images, actors);
        return movieService.createOrUpdate(movie);
    }

    /**
     * Actor cannot be created with this method.
     * If actor is present in database, it is added to the list of actors, otherwise it is skipped.
     *
     * @param actorResponses - json wrapper for Actor entity
     * @param responses      - reference to list of all response messages that will be sent back to client
     * @return List<Actor> - fully persisted list of actors
     */
    //TODO: should this be in service?
    private List<Actor> convertAndPersistAsActor(List<BasicActorResponse> actorResponses, List<Response> responses) {
        if (CollectionUtils.isEmpty(actorResponses)) {
            return new ArrayList<>();
        }
        List<Actor> actors = new ArrayList<>();
        for (BasicActorResponse actorResponse : actorResponses) {
            Long id = actorResponse.getActorId();
            if (id == null) {
                Message message = new Message(String.format(Messages.USE_ACTOR_API_TO_CREATE_NEW_ACTOR, actorResponse.getFullName()), Status.ERROR);
                responses.add(message);
            } else {
                Optional<Actor> actor = actorService.findById(id);
                if (!actor.isPresent()) {
                    Message message = new Message(String.format(Messages.ACTOR_NOT_FOUND, id), Status.ERROR);
                    responses.add(message);
                    logger.log(Level.INFO, String.format(Messages.ACTOR_NOT_FOUND, id));
                }
                actor.ifPresent(actors::add);
            }
        }
        return actors;
    }
}