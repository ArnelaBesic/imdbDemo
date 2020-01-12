package com.demo.imdb.controllers;

import com.demo.imdb.json.MovieResponse;
import com.demo.imdb.model.Movie;
import com.demo.imdb.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/imdb/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;
    //TODO: log important info
    private static final Logger logger = Logger.getLogger(MovieController.class.getName());

    //http://localhost:8080/imdb/movies/list/
    @GetMapping(path = "/list/", produces = "application/json")
    @ResponseBody
    public List<MovieResponse> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        return movies.stream().map(MovieResponse::new).collect(Collectors.toList());

    }

    @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addMovie(@RequestBody Movie movie) {
        Movie savedMovie = movieService.createOrUpdateMovie(movie);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedMovie.getImdbID())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}