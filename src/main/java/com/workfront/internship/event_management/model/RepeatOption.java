package com.workfront.internship.event_management.model;

/**
 * Created by Hermine Turshujyan 7/14/16.
 */
public class RepeatOption {

    private int id;
    private int recurrenceTypeId;
    private String title;
    private String abbreviation;

    public int getId() {
        return id;
    }

    public RepeatOption setId(int id) {
        this.id = id;
        return this;
    }

    public int getRecurrenceTypeId() {
        return recurrenceTypeId;
    }

    public RepeatOption setRecurrenceTypeId(int recurrenceTypeId) {
        this.recurrenceTypeId = recurrenceTypeId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public RepeatOption setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public RepeatOption setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
        return this;
    }
}
