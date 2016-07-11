package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.datehelpers.RecurrenceType;

import java.util.List;

/**
 * Created by hermine on 7/11/16.
 */
public interface RecurrenceTypeDAO {

    public boolean insertRecurrenceType(RecurrenceType recurrenceType);
    public List<RecurrenceType> getAllRecurrenceTypes();
    public RecurrenceType getRecurrenceTypeById(int id);
    public boolean deleteRecurrenceType(int id);

}
