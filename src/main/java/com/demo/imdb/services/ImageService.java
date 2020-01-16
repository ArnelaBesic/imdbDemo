package com.demo.imdb.services;

import com.demo.imdb.model.Image;
import com.demo.imdb.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public Optional<Image> findImageById(Long id) {
        return imageRepository.findById(id);
    }

    public List<Image> findAllById(List<Long> ids) {
        return imageRepository.findAllById(ids);
    }

    public Image createImage(Image image) {
        return imageRepository.save(image);
    }
}
