package com.workfront.internship.event_management.model;

/**
 * Created by Hermine Turshujyan 7/21/16.
 */
public class MediaType {

    int id;
    String title;

    public MediaType() {
    }

    public MediaType(int id, String title) {
        this.id = id;
        this.title = title;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
