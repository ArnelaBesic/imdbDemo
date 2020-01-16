package com.demo.imdb.repositories;

import com.demo.imdb.model.Movie;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class CustomMovieRepositoryImpl implements CustomMovieRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Movie> findByTitleAndReleaseYear(String title, Short releaseYear) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Movie> query = criteriaBuilder.createQuery(Movie.class);
        Root<Movie> movie = query.from(Movie.class);

        List<Predicate> predicates = new ArrayList<>();
        if (!StringUtils.isEmpty(title)) {
            Path<String> titlePath = movie.get("title");
            predicates.add(criteriaBuilder.like(titlePath, "%" + title + "%"));
        }
        if (releaseYear != null) {
            Path<Short> releaseYearPath = movie.get("releaseYear");
            predicates.add(criteriaBuilder.equal(releaseYearPath, releaseYear));
        }
        query.select(movie)
                .where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));

        return entityManager.createQuery(query)
                .getResultList();
    }
}
