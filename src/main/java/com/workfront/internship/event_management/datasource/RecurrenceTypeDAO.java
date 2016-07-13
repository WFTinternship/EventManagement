package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.datehelpers.RecurrenceType;

import java.util.List;

/**
 * Created by hermine on 7/11/16.
 */
public interface RecurrenceTypeDAO {

     int insertRecurrenceType(RecurrenceType recurrenceType);
     List<RecurrenceType> getAllRecurrenceTypes();
     RecurrenceType getRecurrenceTypeById(int id);
     boolean deleteRecurrenceType(int id);

}
