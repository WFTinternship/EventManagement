package com.workfront.internship.event_management.model;

/**
 * Created by Hermine Turshujyan 7/21/16.
 */
public class UserResponse {

    private int id;
    private String title;

    public UserResponse() {
    }

    public UserResponse(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
