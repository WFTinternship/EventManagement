package com.workfront.internship.event_management.model;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/10/16.
 */
public class RecurrenceType {

    private int id;
    private String title;
    private String intervalUnit;
    private List<RecurrenceOption> recurrenceOptions;

    public RecurrenceType setId(int id) {
        this.id = id;
        return this;
    }

    public int getId() {
        return id;
    }

    public RecurrenceType setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public RecurrenceType setIntervalUnit(String intervalUnit) {
        this.intervalUnit = intervalUnit;
        return this;
    }

    public String getIntervalUnit() {
        return intervalUnit;
    }


    public RecurrenceType setRecurrenceOptions(List<RecurrenceOption> recurrenceOptions) {
        this.recurrenceOptions = recurrenceOptions;
        return this;
    }

    public List<RecurrenceOption> getRecurrenceOptions() {
        return recurrenceOptions;
    }


}