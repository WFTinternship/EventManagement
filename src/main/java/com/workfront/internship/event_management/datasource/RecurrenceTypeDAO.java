package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.datehelpers.RecurrenceType;

import java.util.List;

/**
 * Created by hermine on 7/11/16.
 */
public interface RecurrenceTypeDAO {

     //Create
     int insertRecurrenceType(RecurrenceType recurrenceType);

     //Read
     List<RecurrenceType> getAllRecurrenceTypes();
     RecurrenceType getRecurrenceTypeById(int id);

     //Delete
     boolean deleteRecurrenceType(int id);

}
