package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.EventRecurrence;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/26/16.
 */
public class EventRecurrenceServiceImpl implements EventRecurrenceService {
    @Override
    public int addEventRecurrence(EventRecurrence recurrence) {
        return 0;
    }

    @Override
    public int addEventRecurrences(List<EventRecurrence> recurrenceList) {
        return 0;
    }

    @Override
    public EventRecurrence getEventRecurrenceById(int id) {
        return null;
    }

    @Override
    public List<EventRecurrence> getEventRecurrencesByEventId(int eventId) {
        return null;
    }

    @Override
    public List<EventRecurrence> getAllEventRecurrences() {
        return null;
    }

    @Override
    public void updateEventRecurrence(EventRecurrence recurrence) {
    }

    @Override
    public void updateEventRecurrences(List<EventRecurrence> recurrences) {

    }

    @Override
    public void deleteEventRecurrence(int id) {
    }

    @Override
    public void deleteEventRecurrencesByEventId(int eventId) {

    }

    @Override
    public void deleteAllEventRecurrences() {
    }
}
