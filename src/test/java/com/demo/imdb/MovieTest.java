package com.demo.imdb;

import com.demo.imdb.model.Actor;
import com.demo.imdb.model.Movie;
import com.demo.imdb.services.ActorService;
import com.demo.imdb.services.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MovieTest {

    @Autowired
    private MovieService movieService;
    @Autowired
    private ActorService actorService;

    @Test
    public void whenApplicationStarts_thenHibernateCreatesInitialRecords() {
        List<Movie> movies = movieService.getAllMovies();
//        List<Movie> m = movieService.findMoviesByReleaseYear(Long.valueOf(122));
        List<Actor> actors = actorService.getAllActors();
        int size = movies.size();
    }
}
