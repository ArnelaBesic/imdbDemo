package com.demo.imdb.controllers;

import com.demo.imdb.json.ImageResponse;
import com.demo.imdb.model.Image;
import com.demo.imdb.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/imdb/images")
public class ImageController {
    @Autowired
    private ImageService imageService;
    //TODO: log important info
    private static final Logger logger = Logger.getLogger(ImageController.class.getName());

    //e.g. http://localhost:8080/imdb/images/image/1
    @GetMapping(path = "/image/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] getImageById(@PathVariable("id") Long id) {
        Optional<Image> image = imageService.getImageById(id);
        if (image.isPresent()) {
            ImageResponse response = new ImageResponse(image.get());
            return response.getContent();
        }
        return null;
    }
}
