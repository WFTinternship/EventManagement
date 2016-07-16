package com.workfront.internship.event_management.model;

import java.util.Date;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public class Category {

    private int id;
    private String title;
    private String description;
    private Date creationDate;

    public int getId() {
        return id;
    }

    public Category setId(int id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Category setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Category setDescription(String description) {
        this.description = description;
        return this;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Category setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }
}
