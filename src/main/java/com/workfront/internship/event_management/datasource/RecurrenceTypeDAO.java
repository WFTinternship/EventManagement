package com.workfront.internship.event_management.datasource;

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

     //delete data from db
     boolean deleteRecurrenceType(int id);
     boolean deleteAllRecurrenceTypes();

}
