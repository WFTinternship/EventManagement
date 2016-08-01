package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.EventRecurrenceDAO;
import com.workfront.internship.event_management.dao.EventRecurrenceDAOImpl;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.EventRecurrence;
import org.apache.log4j.Logger;

import java.util.List;

import static com.workfront.internship.event_management.service.util.Validator.isEmptyCollection;
import static com.workfront.internship.event_management.service.util.Validator.isValidEventRecurrence;

/**
 * Created by Hermine Turshujyan 7/26/16.
 */
public class EventRecurrenceServiceImpl implements EventRecurrenceService {

    private static final Logger LOGGER = Logger.getLogger(EventRecurrenceServiceImpl.class);
    private EventRecurrenceDAO recurrenceDAO;

    public EventRecurrenceServiceImpl() throws OperationFailedException {
        try {
            recurrenceDAO = new EventRecurrenceDAOImpl();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public EventRecurrence addEventRecurrence(EventRecurrence recurrence) {
        if (!isValidEventRecurrence(recurrence)) {
            throw new OperationFailedException("Invalid event recurrence");
        }

        try {
            int recurrenceId = recurrenceDAO.addEventRecurrence(recurrence);

            recurrence.setId(recurrenceId);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
        return recurrence;
    }

    @Override
    public void addEventRecurrences(List<EventRecurrence> recurrenceList) {
        if (isEmptyCollection(recurrenceList)) {
            throw new OperationFailedException("Empty recurrence list");
        }

        try {
            recurrenceDAO.addEventRecurrences(recurrenceList);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public EventRecurrence getEventRecurrence(int recurrenceId) {
        if (recurrenceId < 0) {
            throw new OperationFailedException("Invalid recurrence id");
        }

        try {
            return recurrenceDAO.getEventRecurrenceById(recurrenceId);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Recurrence not found");
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public List<EventRecurrence> getEventRecurrencesByEventId(int eventId) {
        if (eventId < 0) {
            throw new OperationFailedException("Invalid event id");
        }

        try {
            return recurrenceDAO.getEventRecurrencesByEventId(eventId);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public List<EventRecurrence> getAllEventRecurrences() {
        try {
            return recurrenceDAO.getAllEventRecurrences();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void updateEventRecurrence(EventRecurrence recurrence) {
        if (!isValidEventRecurrence(recurrence)) {
            throw new OperationFailedException("Invalid event recurrence");
        }

        try {
            //insert invitation into db
            recurrenceDAO.updateEventRecurrence(recurrence);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Event recurrence not found", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void updateEventRecurrences(int eventId, List<EventRecurrence> recurrenceList) {
        if (!isEmptyCollection(recurrenceList)) {
            for (EventRecurrence recurrence : recurrenceList) {
                if (!isValidEventRecurrence(recurrence)) {
                    throw new OperationFailedException("Invalid event recurrence");
                }
            }
        }

        //if recurrence list is empty, delete existing recurrences from db
        if (isEmptyCollection(recurrenceList)) {
            deleteEventRecurrencesByEventId(eventId);
        } else {
            //get current recurrences from db
            List<EventRecurrence> dbRecurrenceList = getEventRecurrencesByEventId(eventId);

            //if there is no recurrences in db, insert new recurrence list
            if (isEmptyCollection(dbRecurrenceList)) {
                addEventRecurrences(recurrenceList);
            } else {
                // compare recurrence list from db with new recurrence list
                for (EventRecurrence dbRecurrence : dbRecurrenceList) {
                    if (!recurrenceList.contains(dbRecurrence)) {
                        deleteEventRecurrence(dbRecurrence.getId());
                    } else if (dbRecurrence != recurrenceList.get(dbRecurrence.getId())) {
                        updateEventRecurrence(recurrenceList.get(dbRecurrence.getId()));
                    }
                }

                for (EventRecurrence recurrence : dbRecurrenceList) {
                    if (!dbRecurrenceList.contains(recurrence)) {
                        addEventRecurrence(recurrence);
                    }
                }
            }
        }
    }

    @Override
    public void deleteEventRecurrence(int recurrenceId) {
        if (recurrenceId < 1) {
            throw new OperationFailedException("Invalid recurrence id");
        }

        try {
            recurrenceDAO.deleteEventRecurrence(recurrenceId);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Event not found", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteEventRecurrencesByEventId(int eventId) {
        if (eventId < 1) {
            throw new OperationFailedException("Invalid event id");
        }

        try {
            recurrenceDAO.deleteEventRecurrence(eventId);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Recurrences not found", e);
        }
    }

    @Override
    public void deleteAllEventRecurrences() {
        try {
            recurrenceDAO.deleteAllEventRecurrences();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

}
