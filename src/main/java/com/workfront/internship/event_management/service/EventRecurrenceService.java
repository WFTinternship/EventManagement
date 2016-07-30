package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.EventRecurrence;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/26/16.
 */
public interface EventRecurrenceService {

    //CRUD methods for event recurrence
    int addEventRecurrence(EventRecurrence recurrence);

    int addEventRecurrences(List<EventRecurrence> recurrenceList);

    EventRecurrence getEventRecurrenceById(int id);

    List<EventRecurrence> getEventRecurrencesByEventId(int eventId);

    List<EventRecurrence> getAllEventRecurrences();

    void updateEventRecurrence(EventRecurrence recurrence);

    void updateEventRecurrences(List<EventRecurrence> recurrences);

    void deleteEventRecurrence(int id);

    void deleteEventRecurrencesByEventId(int eventId);


    void deleteAllEventRecurrences();

}
