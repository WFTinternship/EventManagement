package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.Recurrence;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/26/16.
 */
public interface RecurrenceService {

    //CRUD methods for event recurrence
    Recurrence addRecurrence(Recurrence recurrence);

    void addRecurrences(List<Recurrence> recurrenceList);

    Recurrence getRecurrenceById(int recurrenceId);

    List<Recurrence> getRecurrencesByEventId(int eventId);

    List<Recurrence> getAllRecurrences();

    void editRecurrence(Recurrence recurrence);

    void editRecurrenceList(int eventId, List<Recurrence> recurrences);

    void deleteRecurrence(int id);

    void deleteRecurrencesByEventId(int eventId);

    void deleteAllRecurrences();
}
