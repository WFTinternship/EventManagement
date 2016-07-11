package com.workfront.internship.event_management.model.datehelpers;

import java.util.List;

/**
 * Created by hermine on 7/10/16.
 */


public class RecurrenceType {

    private  int id;
    private  String title;
    private  String intervalUnit;
    private List<String> repeatOnValues;

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

    public String getTitle(){
        return title;
    }

    public RecurrenceType setIntervalUnit(String intervalUnit) {
        this.intervalUnit = intervalUnit;
        return this;
    }

    public String getIntervalUnit(){
        return intervalUnit;
    }


    public RecurrenceType setRepeatOnValues(List<String> repeatOnValues) {
        this.repeatOnValues = repeatOnValues;
        return this;
    }

    public List<String> getRepeatOnValues() {
        return repeatOnValues;
    }



/* RecurrenceType(int id, String title, String intervalUnit, String[] repeatOnValues ){
        this.id = id;
        this.title = title;
        this.intervalUnit = intervalUnit;
        this.repeatOnValues = repeatOnValues;
    }*/




}