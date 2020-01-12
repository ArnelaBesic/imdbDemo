package com.demo.imdb.repositories;

import com.demo.imdb.model.Actor;
import com.demo.imdb.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
