package com.workfront.internship.event_management.model;

import java.util.Date;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public class Media {

    private int id;
    private MediaType type;
    private String path;
    private String description;
    private int eventId;
    private int uploaderId;
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

    public String getPath() {
        return path;
    }

    public Media setPath(String path) {
        this.path = path;
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

    public int getUploaderId() {
        return uploaderId;
    }

    public Media setUploaderId(int uploaderId) {
        this.uploaderId = uploaderId;
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
