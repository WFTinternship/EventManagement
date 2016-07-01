package com.workfront.internship.event_management.model;

/**
 * Created by hermine on 7/1/16.
 */
public class EventCategory {
    public int getId() {
        return id;
    }

    public EventCategory setId(int id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public EventCategory setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public EventCategory setDescription(String description) {
        this.description = description;
        return this;
    }

    private int id;
    private String title;
    private String description;

}
