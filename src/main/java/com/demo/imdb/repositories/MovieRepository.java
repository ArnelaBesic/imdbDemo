package com.demo.imdb.repositories;

import com.demo.imdb.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String>, CustomMovieRepository {
}
