package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.model.RecurrenceType;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/11/16.
 */
public interface RecurrenceTypeDAO {

     //insert data into db
     int addRecurrenceType(RecurrenceType recurrenceType);

     //read data from db
     List<RecurrenceType> getAllRecurrenceTypes();
     RecurrenceType getRecurrenceTypeById(int id);

     //update record in db
     boolean updateRecurrenceType(RecurrenceType recurrenceType);

     //delete data from db
     boolean deleteRecurrenceType(int id) throws DAOException;

    boolean deleteAllRecurrenceTypes() throws DAOException;

}
