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

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    //TODO
    public List<Movie> findAllWithPagination() {
        return movieRepository.findAll();
    }

    public Optional<Movie> findByImdbId(String imdbId) {
        return movieRepository.findById(imdbId);
    }

    public List<Movie> findByTitleAndReleaseYear(String title, Short releaseYear) {
        return movieRepository.findByTitleAndReleaseYear(title, releaseYear);
    }

    public Movie createOrUpdate(Movie movie) {
        return movieRepository.save(movie);
    }

    public void deleteById(String id) {
        movieRepository.deleteById(id);
    }
}