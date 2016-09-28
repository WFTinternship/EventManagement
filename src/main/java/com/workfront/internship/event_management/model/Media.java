package com.workfront.internship.event_management.model;

import java.util.Date;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public class Media {

    private int id;
    private MediaType type;
    private String name;
    private String description;
    private int eventId;
    private User uploader;
    private Date uploadDate;

    public int getId() {
        return id;
    }

    public Media setId(int id) {
        this.id = id;
        return this;
    }

    public MediaType getType() {
        return type;
    }

    public Media setType(MediaType type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public Media setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Media setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getEventId() {
        return eventId;
    }

    public Media setEventId(int eventId) {
        this.eventId = eventId;
        return this;
    }

    public User getUploader() {
        return uploader;
    }

    public Media setUploader(User uploader) {
        this.uploader = uploader;
        return this;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public Media setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
        return this;
    }
}
