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
        List<Movie> movies = movieService.findAll();
        List<Movie> movies1 = movieService.findByTitleAndReleaseYear("Lo and Behold: Reveries of the Connected World", (short) 2016);
        List<Movie> movies2 = movieService.findByTitleAndReleaseYear("Lo", (short) 1987);
        List<Movie> movies3 = movieService.findByTitleAndReleaseYear("nun", (short) 1987);
        List<Movie> movies4 = movieService.findByTitleAndReleaseYear("nun", (short) 2016);

        List<Movie> movies5 = movieService.findByTitleAndReleaseYear( null,(short) 2016);
        List<Movie> movies6 = movieService.findByTitleAndReleaseYear(null, (short) 1987);
        List<Movie> movies7 = movieService.findByTitleAndReleaseYear("nun", null);
        List<Movie> movies8 = movieService.findByTitleAndReleaseYear("Lo", null);
        List<Movie> movies9 = movieService.findByTitleAndReleaseYear(null, null);
        List<Movie> movies10 = movieService.findByTitleAndReleaseYear("Lo", (short) 2016);
        List<Actor> actors = actorService.findAll();
        int size = movies.size();
    }
}
