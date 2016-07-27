package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.model.RecurrenceOption;
import com.workfront.internship.event_management.model.RecurrenceType;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/27/16.
 */
public interface RecurrenceTypeService {

    //CRUD methods for recurrence type
    int addRecurrenceType(RecurrenceType recurrenceType);

    List<RecurrenceType> getAllRecurrenceTypes();

    RecurrenceType getRecurrenceTypeById(int id);

    boolean updateRecurrenceType(RecurrenceType recurrenceType);

    boolean deleteRecurrenceType(int id) throws DAOException;

    boolean deleteAllRecurrenceTypes() throws DAOException;

    //CRUD methods for recurrence option
    int addRecurrenceOption(RecurrenceOption option);

    List<RecurrenceOption> getAllRecurrenceOptions();

    RecurrenceOption getRecurrenceOption(int optionId);

    List<RecurrenceOption> getRecurrenceOptionsByRecurrenceType(int recurrenceTypeId);

    boolean updateRecurrenceOption(RecurrenceOption option);

    boolean deleteRecurrenceOption(int optionId) throws DAOException;

    boolean deleteRecurrenceOptionsByRecurrenceType(int recurrenceTypeId) throws DAOException;

    boolean deleteAllRecurrenceOptions() throws DAOException;

}
