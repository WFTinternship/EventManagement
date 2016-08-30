package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.RecurrenceType;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/27/16.
 */
interface RecurrenceTypeService {

    //CRUD methods for recurrence type
    RecurrenceType addRecurrenceType(RecurrenceType recurrenceType);

    List<RecurrenceType> getAllRecurrenceTypes();

    RecurrenceType getRecurrenceTypeById(int recurrenceTypeId);

    boolean editRecurrenceType(RecurrenceType recurrenceType);

    boolean deleteRecurrenceType(int id);

    void deleteAllRecurrenceTypes();

}
