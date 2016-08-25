package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.ObjectNotFoundException;
import com.workfront.internship.event_management.model.Recurrence;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/11/16.
 */
public interface RecurrenceDAO {

    //insert data into db
    int addRecurrence(Recurrence recurrence) throws DAOException;

    void addRecurrences(List<Recurrence> recurrenceList) throws DAOException;

    //read data from db
    Recurrence getRecurrenceById(int id) throws ObjectNotFoundException, DAOException;

    List<Recurrence> getRecurrencesByEventId(int eventId) throws DAOException;

    List<Recurrence> getAllRecurrences() throws DAOException;

    //update db record
    void updateRecurrence(Recurrence recurrence) throws ObjectNotFoundException, DAOException;

    //delete data from db
    void deleteRecurrence(int id) throws ObjectNotFoundException, DAOException;

    void deleteRecurrencesByEventId(int eventId) throws ObjectNotFoundException, DAOException;

    void deleteAllRecurrences() throws DAOException;
}
