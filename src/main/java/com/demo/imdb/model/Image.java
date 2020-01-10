package com.demo.imdb.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Blob;
import java.time.LocalDateTime;

@Entity(name = "Image")
public class Image {
    private Long imageID;
    private String name;
    private Blob content;
    private LocalDateTime uploadDate;

    public Image() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getImageID() {
        return imageID;
    }

    public void setImageID(Long imageID) {
        this.imageID = imageID;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    @Lob
    public Blob getContent() {
        return content;
    }

    public void setContent(Blob content) {
        this.content = content;
    }

    @NotNull
    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }
}
