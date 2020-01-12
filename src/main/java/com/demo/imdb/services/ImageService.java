package com.demo.imdb.services;

import com.demo.imdb.model.Image;
import com.demo.imdb.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public Optional<Image> getImageById(Long id) {
        return imageRepository.findById(id);
    }
}
