package com.demo.imdb.controllers;

import com.demo.imdb.json.ImageResponse;
import com.demo.imdb.json.Message;
import com.demo.imdb.json.Status;
import com.demo.imdb.model.Image;
import com.demo.imdb.services.ImageService;
import com.demo.imdb.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/imdb/images")
public class ImageController {
    @Autowired
    private ImageService imageService;

    /**
     * Find Image with id same as path variable, e.g. http://localhost:8080/imdb/images/image/1
     *
     * @param id by which image will be searched for
     * @return ResponseEntity<Object> containing Image if present and HTTP status 200.
     * Otherwise, status NOT_FOUND and appropriate message
     */
    @GetMapping(path = "/image/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<Object> getImageById(@PathVariable("id") Long id) {
        Optional<Image> image = imageService.findImageById(id);
        if (image.isPresent()) {
            ImageResponse response = new ImageResponse(image.get());
            return ResponseEntity.ok(response.getContent());
        }
        Message message = new Message(String.format(Messages.IMAGE_NOT_FOUND, id), Status.ERROR);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
}
