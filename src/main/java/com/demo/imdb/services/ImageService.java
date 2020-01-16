package com.demo.imdb.services;

import com.demo.imdb.json.ImageResponse;
import com.demo.imdb.json.Message;
import com.demo.imdb.json.Response;
import com.demo.imdb.json.Status;
import com.demo.imdb.model.Image;
import com.demo.imdb.repositories.ImageRepository;
import com.demo.imdb.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;
    private static final Logger logger = Logger.getLogger(ImageService.class.getName());

    public Optional<Image> findImageById(Long id) {
        return imageRepository.findById(id);
    }

    public List<Image> findAllById(List<Long> ids) {
        return imageRepository.findAllById(ids);
    }

    public Image createImage(Image image) {
        return imageRepository.save(image);
    }

    public List<Image> convertToPersistedImages(List<ImageResponse> imageResponses, List<Response> messages) {
        if (CollectionUtils.isEmpty(imageResponses)) {
            return new ArrayList<>();
        }
        List<Image> images = new ArrayList<>();
        for (ImageResponse imageResponse : imageResponses) {
            Long imageID = imageResponse.getImageID();
            if (imageID == null) {
                Blob content;
                try {
                    content = new SerialBlob(imageResponse.getContent());
                } catch (Exception exc) {
                    Message message = new Message(String.format(Messages.IMAGE_CONVERSION_ERROR, imageResponse.getName()), Status.ERROR);
                    messages.add(message);
                    logger.log(Level.INFO, String.format(Messages.IMAGE_CONVERSION_ERROR, imageResponse.getName(), exc.getCause()));
                    continue;
                }
                Image newImage = new Image(imageResponse.getName(), content, imageResponse.getUploadDate());
                Image persistedImage = createImage(newImage);
                images.add(persistedImage);
            } else {
                Optional<Image> image = findImageById(imageID);
                if (!image.isPresent()) {
                    Message message = new Message(String.format(Messages.IMAGE_NOT_FOUND, imageID), Status.ERROR);
                    messages.add(message);
                    logger.log(Level.INFO, String.format(Messages.IMAGE_NOT_FOUND, imageID));
                }
                image.ifPresent(images::add);
            }
        }
        return images;
    }
}
