package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.model.RecurrenceType;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/11/16.
 */
public interface RecurrenceTypeDAO {

    //insert data into db
    int addRecurrenceType(RecurrenceType recurrenceType) throws DAOException, DuplicateEntryException;

    int addRecurrenceTypeWithOptions(RecurrenceType recurrenceType) throws DuplicateEntryException, DAOException;

    //read data from db
    List<RecurrenceType> getAllRecurrenceTypes() throws DAOException;

    RecurrenceType getRecurrenceTypeById(int id) throws DAOException;

    RecurrenceType getRecurrenceTypeWithOptionsById(int id) throws DAOException;

    //update record in db
    void updateRecurrenceType(RecurrenceType recurrenceType) throws DAOException, DuplicateEntryException, ObjectNotFoundException;

    //delete data from db
    void deleteRecurrenceType(int id) throws DAOException, ObjectNotFoundException;

    void deleteAllRecurrenceTypes() throws DAOException;

}
