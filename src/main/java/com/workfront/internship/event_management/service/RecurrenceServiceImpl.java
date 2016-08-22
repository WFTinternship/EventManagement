package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.RecurrenceDAO;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.Recurrence;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.workfront.internship.event_management.service.util.Validator.isEmptyCollection;
import static com.workfront.internship.event_management.service.util.Validator.isValidRecurrence;

/**
 * Created by Hermine Turshujyan 7/26/16.
 */
@Component
public class RecurrenceServiceImpl implements RecurrenceService {

    private static final Logger LOGGER = Logger.getLogger(RecurrenceServiceImpl.class);

    @Autowired
    private RecurrenceDAO recurrenceDAO;

    @Override
    public Recurrence addRecurrence(Recurrence recurrence) {
        if (!isValidRecurrence(recurrence)) {
            throw new OperationFailedException("Invalid event recurrence");
        }

        try {
            int recurrenceId = recurrenceDAO.addRecurrence(recurrence);

            recurrence.setId(recurrenceId);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
        return recurrence;
    }

    @Override
    public void addRecurrences(List<Recurrence> recurrenceList) {
        if (isEmptyCollection(recurrenceList)) {
            throw new OperationFailedException("Empty recurrence list");
        }

        try {
            recurrenceDAO.addRecurrences(recurrenceList);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public Recurrence getRecurrenceById(int recurrenceId) {
        if (recurrenceId < 0) {
            throw new OperationFailedException("Invalid recurrence id");
        }

        try {
            return recurrenceDAO.getRecurrenceById(recurrenceId);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Recurrence not found");
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public List<Recurrence> getRecurrencesByEventId(int eventId) {
        if (eventId < 0) {
            throw new OperationFailedException("Invalid event id");
        }

        try {
            return recurrenceDAO.getRecurrencesByEventId(eventId);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public List<Recurrence> getAllRecurrences() {
        try {
            return recurrenceDAO.getAllRecurrences();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void editRecurrence(Recurrence recurrence) {
        if (!isValidRecurrence(recurrence)) {
            throw new OperationFailedException("Invalid event recurrence");
        }

        try {
            //insert invitation into db
            recurrenceDAO.updateRecurrence(recurrence);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Event recurrence not found", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void editRecurrenceList(int eventId, List<Recurrence> recurrenceList) {
        //if recurrence list is empty, delete existing recurrences from db
        if (isEmptyCollection(recurrenceList)) {
            deleteRecurrencesByEventId(eventId);
        } else {
            //get current recurrences from db
            List<Recurrence> dbRecurrenceList = getRecurrencesByEventId(eventId);

            //if there is no recurrences in db, insert new recurrence list
            if (isEmptyCollection(dbRecurrenceList)) {
                addRecurrences(recurrenceList);
            } else {
                // compare recurrence list from db with new recurrence list
                for (Recurrence dbRecurrence : dbRecurrenceList) {
                    int recurrenceId = dbRecurrence.getId();

                    if (getRecurrenceWithIdFromList(recurrenceList, recurrenceId) == null) {
                        deleteRecurrence(recurrenceId);
                    } else if (dbRecurrence != getRecurrenceWithIdFromList(recurrenceList, recurrenceId)) {
                        editRecurrence(getRecurrenceWithIdFromList(recurrenceList, recurrenceId));
                    }
                }

                for (Recurrence recurrence : recurrenceList) {
                    int recurrenceId = recurrence.getId();

                    if (getRecurrenceWithIdFromList(dbRecurrenceList, recurrenceId) == null) {
                        addRecurrence(recurrence);
                    }
                }
            }
        }
    }

    @Override
    public void deleteRecurrence(int recurrenceId) {
        if (recurrenceId < 1) {
            throw new OperationFailedException("Invalid recurrence id");
        }

        try {
            recurrenceDAO.deleteRecurrence(recurrenceId);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Event not found", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteRecurrencesByEventId(int eventId) {
        if (eventId < 1) {
            throw new OperationFailedException("Invalid event id");
        }

        try {
            recurrenceDAO.deleteRecurrencesByEventId(eventId);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Recurrences not found", e);
        }
    }

    @Override
    public void deleteAllRecurrences() {
        try {
            recurrenceDAO.deleteAllRecurrences();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    //helper methods
    private Recurrence getRecurrenceWithIdFromList(List<Recurrence> recurrenceList, int id) {
        for (Recurrence recurrence : recurrenceList) {
            if (recurrence.getId() == id) {
                return recurrence;
            }
        }
        return null;
    }
}
