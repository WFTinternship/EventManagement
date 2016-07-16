package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.Recurrence;

import java.sql.Connection;
import java.util.List;

/**
 * Created by Hermine Turshujyan 7/11/16.
 */
public interface RecurrenceDAO {

    //CREATE
    boolean addEventRecurrences(List<Recurrence> recurrences, Connection conn);

    boolean addEventRecurrences(List<Recurrence> recurrences);

    int addEventRecurrence(Recurrence recurrence, Connection conn);

    int addEventRecurrence(Recurrence recurrence);

    //READ
    List<Recurrence> getEventRecurrencesByEventId(int eventId);

    //UPDATE
    boolean updateEventRecurrence(Recurrence recurrence);

    //DELETE
    boolean deleteEventRecurrece(int id);
}
