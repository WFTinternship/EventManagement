package com.workfront.internship.event_management.model;

import java.util.Date;
import java.util.List;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public class Event {
    private int id;
    private String title;
    private String shortDescription;
    private String fullDescription;
    private String location;
    private double lat;
    private double lng;
    private String filePath;
    private String imagePath;
    private Category category;
    private Date startDate;
    private Date endDate;
    private boolean publicAccessed;
    private boolean guestsAllowed;
    private Date creationDate;
    private Date lastModifiedDate;
    private List<Recurrence> eventRecurrences;
    private List<Invitation> invitations;
    private List<Media> media;

    public Event() {
    }

    public int getId() {
        return id;
    }

    public Event setId(int id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Event setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public Event setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public Event setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public Event setLocation(String location) {
        this.location = location;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public Event setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLng() {
        return lng;
    }

    public Event setLng(double lng) {
        this.lng = lng;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public Event setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Event setImagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public Event setCategory(Category category) {
        this.category = category;
        return this;
    }

    public List<Recurrence> getEventRecurrences() {
        return eventRecurrences;
    }

    public Event setEventRecurrences(List<Recurrence> eventRecurrences) {
        this.eventRecurrences = eventRecurrences;
        return this;
    }

    public boolean isPublicAccessed() {
        return publicAccessed;
    }

    public Event setPublicAccessed(boolean publicAccessed) {
        this.publicAccessed = publicAccessed;
        return this;
    }

    public boolean isGuestsAllowed() {
        return guestsAllowed;
    }

    public Event setGuestsAllowed(boolean guestsAllowed) {
        this.guestsAllowed = guestsAllowed;
    return this;
}


    public Date getCreationDate() {
        return creationDate;
    }

    public Event setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Event setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }


    public List<Invitation> getInvitations() {
        return invitations;
    }

    public Event setInvitations(List<Invitation> invitations) {
        this.invitations = invitations;
        return this;
    }

    public List<Media> getMedia() {
        return media;
    }

    public Event setMedia(List<Media> media) {
        this.media = media;
        return this;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Event setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Event setEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }
}
