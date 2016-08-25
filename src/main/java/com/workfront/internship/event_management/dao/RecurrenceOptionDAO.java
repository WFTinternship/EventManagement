package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.model.RecurrenceOption;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/14/16.
 */
public interface RecurrenceOptionDAO {

    //insert data into db
    int addRecurrenceOption(RecurrenceOption option) throws DAOException, DuplicateEntryException;

    void addRecurrenceOptions(List<RecurrenceOption> options) throws DAOException, DuplicateEntryException;

    //read data from db
    List<RecurrenceOption> getAllRecurrenceOptions() throws DAOException;

    RecurrenceOption getRecurrenceOptionById(int optionId) throws DAOException, ObjectNotFoundException;

    List<RecurrenceOption> getRecurrenceOptionsByRecurrenceType(int recurrenceTypeId) throws DAOException;

    //update data in db
    void updateRecurrenceOption(RecurrenceOption option) throws DAOException, ObjectNotFoundException, DuplicateEntryException;

    //delete data from db
    void deleteRecurrenceOption(int optionId) throws ObjectNotFoundException, DAOException;

    void deleteRecurrenceOptionsByRecurrenceType(int recurrenceTypeId) throws ObjectNotFoundException, DAOException;

    void deleteAllRecurrenceOptions() throws DAOException;
}
