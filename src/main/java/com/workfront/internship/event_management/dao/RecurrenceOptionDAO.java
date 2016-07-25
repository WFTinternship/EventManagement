package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.model.RecurrenceOption;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/14/16.
 */
public interface RecurrenceOptionDAO {

    //insert data into db
    int addRecurrenceOption(RecurrenceOption option);

    //read data from db
    List<RecurrenceOption> getAllRecurrenceOptions();

    RecurrenceOption getRecurrenceOption(int optionId);

    List<RecurrenceOption> getRecurrenceOptionsByRecurrenceType(int recurrenceTypeId);

    //update data in db
    boolean updateRecurrenceOption(RecurrenceOption option);

    //delete data from db
    boolean deleteRecurrenceOption(int optionId) throws DAOException;

    boolean deleteRecurrenceOptionsByRecurrenceType(int recurrenceTypeId) throws DAOException;

    boolean deleteAllRecurrenceOptions() throws DAOException;

}
