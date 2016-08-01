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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        RecurrenceOption recurrenceOption = (RecurrenceOption) obj;

        if (getId() != recurrenceOption.getId())
            return false;

        if (getRecurrenceTypeId() != recurrenceOption.getRecurrenceTypeId())
            return false;

        if (getTitle() != null ? !getTitle().equals(recurrenceOption.getTitle()) : recurrenceOption.getTitle() != null)
            return false;

        return (getAbbreviation() != null ? !getAbbreviation().equals(recurrenceOption.getAbbreviation()) : recurrenceOption.getAbbreviation() != null);
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getRecurrenceTypeId();
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = 31 * result + (getAbbreviation() != null ? getAbbreviation().hashCode() : 0);
        return result;
    }
}
