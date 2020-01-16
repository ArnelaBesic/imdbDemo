package com.demo.imdb.controllers;

import com.demo.imdb.json.FullMovieResponse;
import com.demo.imdb.model.Movie;
import com.demo.imdb.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
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
    @GetMapping(path = "/all/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<FullMovieResponse> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        return movies.stream().map(FullMovieResponse::new).collect(Collectors.toList());

    }

    @GetMapping(path = "/paginate/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<FullMovieResponse> getPaginatedMovieList() {
        List<Movie> movies = movieService.getAllMovies();
        return movies.stream().map(FullMovieResponse::new).collect(Collectors.toList());

    }

    @GetMapping(path = "/movie/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public FullMovieResponse findMovieByImdbID(@PathVariable String imdbID) {
        Optional<Movie> movie = movieService.findMovieByImdbID(imdbID);
        if (movie.isPresent()) {
            return new FullMovieResponse(movie.get());
        }
        return null;
    }

    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<FullMovieResponse> findMoviesByTitle(@RequestParam(required = false) String title, @RequestParam(required = false) Long releaseYear) {
        List<Movie> movies = movieService.findMoviesByTitle(title);
        List<Movie> movies2 = movieService.findMoviesByReleaseYear(releaseYear);
        //or all paginated
        return movies.stream().map(FullMovieResponse::new).collect(Collectors.toList());

    }

    //e.g. http://localhost:8080/imdb/movies/
    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createMovie(@RequestBody FullMovieResponse fullMovieResponse) {
        //TODO: request body entity? , validation, response
//Actor savedActor = actorService.createOrUpdateActor(actor);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand("1")
                .toUri();

        return ResponseEntity.created(location).build();
    }

    //e.g. http://localhost:8080/imdb/movies/
    @PutMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateMovie(@RequestBody Movie movie) {
        //TODO: request body entity? , validation, response
        Movie savedMovie = movieService.createOrUpdateMovie(movie);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(movie.getImdbID())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    //e.g. http://localhost:8080/imdb/movies/
    @DeleteMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteMovie(@RequestBody Movie movie) {
        movieService.deleteMovie(movie);
    }

    //e.g. http://localhost:8080/imdb/movies/movie/1
    @DeleteMapping(path = "/movie/{imdbID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteMovieByID(@PathVariable("imdbID") String imdbID) {
        movieService.deleteMovieByID(imdbID);
    }
}