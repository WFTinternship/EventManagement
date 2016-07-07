package com.workfront.internship.event_management.model;

import java.util.Date;
import java.util.List;

/**
 * Created by hermine on 7/1/16.
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
    private EventCategory category;
    private boolean publicAccess;
    private boolean guestsAllowed;
    private Date creationDate;
    private Date lastModified;
    private List<DateRange> dates;
    private List<EventParticipant> participants;
    private List<EventMedia> media;

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

    public Date getCreationDate() {
        return creationDate;
    }

    public Event setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public Event setLastModified(Date lastModified) {
        this.lastModified = lastModified;
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

    public EventCategory getCategory() {
        return category;
    }

    public Event setCategory(EventCategory category) {
        this.category = category;
        return this;
    }


    public boolean isPublicAccess() {
        return publicAccess;
    }

    public Event setPublicAccess(boolean publicAccess) {
        this.publicAccess = publicAccess;
        return this;
    }

    public boolean isGuestsAllowed() {
        return guestsAllowed;
    }

    public Event setGuestsAllowed(boolean guestsAllowed) {
        this.guestsAllowed = guestsAllowed;
    return this;
}
    public List<DateRange> getDates() {
        return dates;
    }

    public Event setDates(List<DateRange> dates) {
        this.dates = dates;
        return this;
    }

    public List<EventParticipant> getParticipants() {
        return participants;
    }

    public Event setParticipants(List<EventParticipant> participants) {
        this.participants = participants;
        return this;
    }

    public List<EventMedia> getMedia() {
        return media;
    }

    public Event setMedia(List<EventMedia> media) {
        this.media = media;
        return this;
    }
}
