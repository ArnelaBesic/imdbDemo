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

    //http://localhost:8080/imdb/movies/list/
    @GetMapping(path = "/all/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Response> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        if (CollectionUtils.isEmpty(movies)) {
            return Collections.singletonList(new Message(Messages.NO_DATA, Status.OK));
        }
        return movies.stream().map(FullMovieResponse::new).collect(Collectors.toList());
    }

    @GetMapping(path = "/paginate/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Response> getPaginatedMovieList() {
        List<Movie> movies = movieService.getAllMovies();
        if (CollectionUtils.isEmpty(movies)) {
            return Collections.singletonList(new Message(Messages.NO_DATA, Status.OK));
        }
        return movies.stream().map(FullMovieResponse::new).collect(Collectors.toList());
    }

    @GetMapping(path = "/movie/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<? extends Response> findMovieByImdbID(@PathVariable String imdbID) {
        Optional<Movie> movie = movieService.findMovieByImdbID(imdbID);
        if (movie.isPresent()) {
            FullMovieResponse fullMovieResponse = new FullMovieResponse(movie.get());
            return ResponseEntity.ok(fullMovieResponse);
        }
        Message message = new Message(String.format(Messages.MOVIE_NOT_FOUND, imdbID), Status.ERROR);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    //e. g. http://localhost:8080/imdb/movies/?title=Lo&year=2016
    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Response> findMoviesByTitleAndReleaseYear(@RequestParam(required = false) String title, @RequestParam(required = false) Short year) {
        List<Movie> movies = movieService.findByTitleAndReleaseYear(title, year);
        if (CollectionUtils.isEmpty(movies)) {
            return Collections.singletonList(new Message(Messages.NO_DATA, Status.OK));
        }
        return movies.stream().map(FullMovieResponse::new).collect(Collectors.toList());
    }

    //e.g. http://localhost:8080/imdb/movies/
    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createMovie(@RequestBody FullMovieResponse movieResponse) {
        List<Response> responses = new ArrayList<>();
        if (movieResponse.getImdbID() != null) {
            Message message = new Message(Messages.USE_PUT_FOR_RECORD_UPDATE, Status.ERROR);
            responses.add(message);
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(responses);
        }
        Movie movie = persist(movieResponse, responses);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(movie.getImdbID())
                .toUri();
        return ResponseEntity.created(location).body(responses);
    }

    //e.g. http://localhost:8080/imdb/movies/
    @PutMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateMovie(@RequestBody FullMovieResponse movieResponse) {
        List<Response> responses = new ArrayList<>();
        if (StringUtils.isEmpty(movieResponse.getImdbID())) {
            Message message = new Message(Messages.USE_POST_FOR_RECORD_CREATION, Status.ERROR);
            responses.add(message);
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(responses);
        }
        Movie movie = persist(movieResponse, responses);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(movie.getImdbID())
                .toUri();
        return ResponseEntity.status(HttpStatus.OK).location(location).body(responses);
    }

    //e.g. http://localhost:8080/imdb/movies/movie/tt5275828
    @DeleteMapping(path = "/movie/{imdbID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> deleteMovieByID(@PathVariable("imdbID") String imdbID) {
        try {
            Optional<Movie> optionalMovie = movieService.findMovieByImdbID(imdbID);
            if (!optionalMovie.isPresent()) {
                Message message = new Message(String.format(Messages.MOVIE_NOT_FOUND, imdbID), Status.ERROR);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
            }
            movieService.deleteMovieByID(imdbID);
        } catch (ConstraintViolationException exc) {
            logger.log(Level.INFO, String.format(Messages.DELETE_EXCEPTION, Movie.class.getSimpleName(), imdbID, exc.getCause()));
            Message message = new Message(String.format(Messages.CONSTRAINT_VIOLATION_EXCEPTION, imdbID), Status.ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        } catch (EmptyResultDataAccessException exc) {
            logger.log(Level.INFO, String.format(Messages.DELETE_EXCEPTION, Movie.class.getSimpleName(), imdbID, exc.getCause()));
            Message message = new Message(String.format(Messages.MOVIE_NOT_FOUND, imdbID), Status.ERROR);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        Message message = new Message(String.format(Messages.SUCCESSFUL_DELETION, imdbID), Status.OK);
        return ResponseEntity.ok(message);
    }

    private Movie persist(FullMovieResponse movieResponse, List<Response> responses) {
        List<Image> images = imageService.convertToPersistedImages(movieResponse.getImages(), responses);
        List<Actor> actors = convertToPersistedActors(movieResponse.getCast(), responses);

        Movie movie = new Movie(movieResponse.getImdbID(), movieResponse.getTitle(), movieResponse.getDescription(), movieResponse.getReleaseYear(), movieResponse.getLength(), images, actors);
        return movieService.createOrUpdateMovie(movie);
    }

    private List<Actor> convertToPersistedActors(List<BasicActorResponse> basicActorResponses, List<Response> messages) {
        if (CollectionUtils.isEmpty(basicActorResponses)) {
            return new ArrayList<>();
        }
        List<Actor> actors = new ArrayList<>();
        for (BasicActorResponse actorResponse : basicActorResponses) {
            Long id = actorResponse.getActorID();
            if (id == null) {
                Message message = new Message(String.format(Messages.USE_ACTOR_API_TO_CREATE_NEW_ACTOR, actorResponse.getFullName()), Status.ERROR);
                messages.add(message);
                continue;

            } else {
                Optional<Actor> actor = actorService.findActorById(id);
                if (!actor.isPresent()) {
                    Message message = new Message(String.format(Messages.ACTOR_NOT_FOUND, id), Status.ERROR);
                    messages.add(message);
                    logger.log(Level.INFO, String.format(Messages.ACTOR_NOT_FOUND, id));
                }
                actor.ifPresent(actors::add);
            }
        }
        return actors;
    }
}