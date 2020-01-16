package com.demo.imdb.services;

import com.demo.imdb.model.Movie;
import com.demo.imdb.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    //TODO
    public List<Movie> getAllMoviesWithPagination() {
        return movieRepository.findAll();
    }

    public Optional<Movie> findMovieByImdbID(String imdbID) {
        return movieRepository.findById(imdbID);
    }

    public List<Movie> findByTitleAndReleaseYear(String title, Short releaseYear) {
        return movieRepository.findByTitleAndReleaseYear(title, releaseYear);
    }

    public Movie createOrUpdateMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public void deleteMovieByID(String id) {
        movieRepository.deleteById(id);
    }
}