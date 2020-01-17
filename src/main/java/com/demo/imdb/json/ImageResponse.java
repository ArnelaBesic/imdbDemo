package com.demo.imdb.json;

import com.demo.imdb.model.Image;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageResponse {
    private Long imageId;
    private String name;
    private byte[] content;
    private LocalDateTime uploadDate;
    private static final Logger logger = Logger.getLogger(ImageResponse.class.getName());

    public ImageResponse() {
    }

    public ImageResponse(Image image) {
        this.imageId = image.getImageId();
        this.name = image.getName();

        try {
            this.content = IOUtils.toByteArray(image.getContent().getBinaryStream());
        } catch (IOException e) {
            logger.log(Level.INFO, String.format("IOException during conversion of image content [id: %s, name: %s] to byte array", imageId, name));
            e.printStackTrace();
        } catch (SQLException e) {
            logger.log(Level.INFO, String.format("SQLException during conversion of image content [id: %s, name: %s] to byte array", imageId, name));
            e.printStackTrace();
        }
        this.uploadDate = image.getUploadDate();
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }
}
