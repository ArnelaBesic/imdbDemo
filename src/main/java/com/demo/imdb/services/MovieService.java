package com.demo.imdb.services;

import com.demo.imdb.model.Movie;
import com.demo.imdb.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> list() {
        return movieRepository.findAll();
    }
}