package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.RecurrenceType;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/27/16.
 */
public interface RecurrenceTypeService {

    //CRUD methods for recurrence type
    int addRecurrenceType(RecurrenceType recurrenceType);

    int addRecurrenceTypeWithOptions(RecurrenceType recurrenceType);

    List<RecurrenceType> getAllRecurrenceTypes();

    RecurrenceType getRecurrenceTypeById(int id);

    RecurrenceType getRecurrenceTypeWithOptionsById(int id);

    boolean updateRecurrenceType(RecurrenceType recurrenceType);

    boolean deleteRecurrenceType(int id);

    boolean deleteAllRecurrenceTypes();

}
