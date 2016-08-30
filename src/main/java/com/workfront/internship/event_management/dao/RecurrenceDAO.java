package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.model.Recurrence;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/11/16.
 */
public interface RecurrenceDAO {

    //insert data into db
    int addRecurrence(Recurrence recurrence);

    void addRecurrences(List<Recurrence> recurrenceList);

    //read data from db
    Recurrence getRecurrenceById(int id);

    List<Recurrence> getRecurrencesByEventId(int eventId);

    List<Recurrence> getAllRecurrences();

    //update db record
    boolean updateRecurrence(Recurrence recurrence);

    //delete data from db
    boolean deleteRecurrence(int id);

    void deleteRecurrencesByEventId(int eventId);

    void deleteAllRecurrences() throws DAOException;
}
