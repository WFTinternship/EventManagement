package com.workfront.internship.event_management.model;

import java.util.Date;
import java.util.List;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public class Event {
    private int id;
    private String title;
    private String shortDesc;
    private String fullDesc;
    private String location;
    private float lat;
    private float lng;
    private String filePath;
    private String imagePath;
    private Category category;
    private DateRange dateRange;
    private boolean publicAccessed;
    private boolean guestsAllowed;
    private Date creationDate;
    private Date lastModifiedDate;
    private List<Recurrence> recurrences;
    private List<Invitation> invitations;
    private List<Media> media;

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

    public String getShortDesc() {
        return shortDesc;
    }

    public Event setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
        return this;
    }

    public String getFullDesc() {
        return fullDesc;
    }

    public Event setFullDesc(String fullDesc) {
        this.fullDesc = fullDesc;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public Event setLocation(String location) {
        this.location = location;
        return this;
    }

    public float getLat() {
        return lat;
    }

    public Event setLat(float lat) {
        this.lat = lat;
        return this;
    }

    public float getLng() {
        return lng;
    }

    public Event setLng(float lng) {
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

    public DateRange getDateRange() {
        return dateRange;
    }

    public Event setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
        return this;
    }

    public List<Recurrence> getRecurrences() {
        return recurrences;
    }

    public Event setRecurrences(List<Recurrence> recurrences) {
        this.recurrences = recurrences;
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
}
