package com.workfront.internship.event_management.model;

import java.util.Date;

/**
 * Created by hermine on 7/1/16.
 */
public class EventMedia {

    private int id;
    private String type;
    private String path;
    private String description;
    private int eventId;
    private int ownerId;
    private Date uploadDate;

    public int getId() {
        return id;
    }

    public EventMedia setId(int id) {
        this.id = id;
        return this;
    }

    public String getType() {
        return type;
    }

    public EventMedia setType(String type) {
        this.type = type;
        return this;
    }

    public String getPath() {
        return path;
    }

    public EventMedia setPath(String path) {
        this.path = path;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public EventMedia setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getEventId() {
        return eventId;
    }

    public EventMedia setEventId(int eventId) {
        this.eventId = eventId;
        return this;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public EventMedia setOwnerId(int ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public EventMedia setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
        return this;
    }
}
