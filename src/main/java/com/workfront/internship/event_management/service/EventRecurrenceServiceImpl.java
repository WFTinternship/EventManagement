package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.exception.DAOException;
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
    public boolean updateEventRecurrence(EventRecurrence recurrence) {
        return false;
    }

    @Override
    public boolean deleteEventRecurrence(int id) throws DAOException {
        return false;
    }

    @Override
    public boolean deleteAllEventRecurrences() throws DAOException {
        return false;
    }
}
