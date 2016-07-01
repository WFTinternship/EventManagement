package com.workfront.internship.event_management.model;

/**
 * Created by hermine on 7/1/16.
 */
public class EventMedia {

    public EventMedia(int id, String mediaType, String mediaPath, int eventId, int ownerId) {
        this.id = id;
        this.mediaType = mediaType;
        this.mediaPath = mediaPath;
        this.eventId = eventId;
        this.ownerId = ownerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
    private int id;
    private String mediaType;
    private String mediaPath;
    private String description;
    private int eventId;
    private int ownerId;


}
