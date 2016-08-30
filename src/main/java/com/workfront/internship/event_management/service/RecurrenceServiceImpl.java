package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.RecurrenceDAO;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
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

    private static final Logger logger = Logger.getLogger(RecurrenceServiceImpl.class);

    @Autowired
    private RecurrenceDAO recurrenceDAO;

    @Override
    public Recurrence addRecurrence(Recurrence recurrence) {
        if (!isValidRecurrence(recurrence)) {
            throw new InvalidObjectException("Invalid event recurrence");
        }

        int recurrenceId = recurrenceDAO.addRecurrence(recurrence);
        recurrence.setId(recurrenceId);

        return recurrence;
    }

    @Override
    public void addRecurrences(List<Recurrence> recurrenceList) {
        if (isEmptyCollection(recurrenceList)) {
            throw new InvalidObjectException("Empty recurrence list");
        }

        recurrenceDAO.addRecurrences(recurrenceList);
    }

    @Override
    public Recurrence getRecurrenceById(int recurrenceId) {
        if (recurrenceId < 0) {
            throw new InvalidObjectException("Invalid recurrence id");
        }

        Recurrence recurrence = recurrenceDAO.getRecurrenceById(recurrenceId);
        if (recurrence == null) {
            throw new ObjectNotFoundException("Recurrence not found");
        }
        return recurrence;
    }

    @Override
    public List<Recurrence> getRecurrencesByEventId(int eventId) {
        if (eventId < 0) {
            throw new InvalidObjectException("Invalid event id");
        }

        return recurrenceDAO.getRecurrencesByEventId(eventId);
    }

    @Override
    public List<Recurrence> getAllRecurrences() {
        return recurrenceDAO.getAllRecurrences();
    }

    @Override
    public boolean editRecurrence(Recurrence recurrence) {
        if (!isValidRecurrence(recurrence)) {
            throw new InvalidObjectException("Invalid event recurrence");
        }

        boolean success = recurrenceDAO.updateRecurrence(recurrence);

        if (!success) {
            throw new ObjectNotFoundException("Recurrence not found!");
        }
        return success;
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
    public boolean deleteRecurrence(int recurrenceId) {
        if (recurrenceId < 1) {
            throw new InvalidObjectException("Invalid recurrence id");
        }

        return recurrenceDAO.deleteRecurrence(recurrenceId);
    }

    @Override
    public void deleteRecurrencesByEventId(int eventId) {
        if (eventId < 1) {
            throw new InvalidObjectException("Invalid event id");
        }

        recurrenceDAO.deleteRecurrencesByEventId(eventId);
    }

    @Override
    public void deleteAllRecurrences() {
        recurrenceDAO.deleteAllRecurrences();
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
