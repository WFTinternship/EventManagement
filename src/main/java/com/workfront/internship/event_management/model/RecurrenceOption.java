package com.workfront.internship.event_management.model;

/**
 * Created by Hermine Turshujyan 7/14/16.
 */
public class RecurrenceOption {

    private int id;
    private int recurrenceTypeId;
    private String title;
    private String abbreviation;

    public RecurrenceOption() {
    }

    public RecurrenceOption(RecurrenceOption recurrenceOption) {
        this.id = recurrenceOption.id;
        this.recurrenceTypeId = recurrenceOption.recurrenceTypeId;
        this.title = recurrenceOption.title;
        this.abbreviation = recurrenceOption.abbreviation;
    }
    public int getId() {
        return id;
    }

    public RecurrenceOption setId(int id) {
        this.id = id;
        return this;
    }

    public int getRecurrenceTypeId() {
        return recurrenceTypeId;
    }

    public RecurrenceOption setRecurrenceTypeId(int recurrenceTypeId) {
        this.recurrenceTypeId = recurrenceTypeId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public RecurrenceOption setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public RecurrenceOption setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
        return this;
    }
}
