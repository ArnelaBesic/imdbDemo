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

    public Image createImage(Image image) {
        return imageRepository.save(image);
    }

    /**
     * For all images, check if they are already present in database.
     * If yes, add them to the list. Otherwise create new Image and
     * add to the list.
     *
     * @param imageResponses - json wrapper for Image entity
     * @param responses      - reference to list of all response messages that will be sent back to client
     * @return List<Image> - fully persisted list of image
     */
    public List<Image> convertAndPersistAsImage(List<ImageResponse> imageResponses, List<Response> responses) {
        if (CollectionUtils.isEmpty(imageResponses)) {
            return new ArrayList<>();
        }
        List<Image> images = new ArrayList<>();
        for (ImageResponse imageResponse : imageResponses) {
            Long imageId = imageResponse.getImageId();
            if (imageId == null) {
                Blob content;
                try {
                    content = new SerialBlob(imageResponse.getContent());
                } catch (Exception exc) {
                    Message message = new Message(String.format(Messages.IMAGE_CONVERSION_ERROR, imageResponse.getName()), Status.ERROR);
                    responses.add(message);
                    logger.log(Level.INFO, String.format(Messages.IMAGE_CONVERSION_ERROR, imageResponse.getName(), exc.getCause()));
                    continue;
                }
                Image newImage = new Image(imageResponse.getName(), content, imageResponse.getUploadDate());
                Image persistedImage = createImage(newImage);
                images.add(persistedImage);
            } else {
                Optional<Image> image = findImageById(imageId);
                if (!image.isPresent()) {
                    Message message = new Message(String.format(Messages.IMAGE_NOT_FOUND, imageId), Status.ERROR);
                    responses.add(message);
                    logger.log(Level.INFO, String.format(Messages.IMAGE_NOT_FOUND, imageId));
                }
                image.ifPresent(images::add);
            }
        }
        return images;
    }
}
