package com.demo.imdb.repositories;

import com.demo.imdb.model.Movie;

import java.util.List;

public interface CustomMovieRepository {
    List<Movie> findByTitleAndReleaseYear(String title, Short releaseYear);
}
