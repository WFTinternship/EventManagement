package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.RecurrenceOption;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/28/16.
 */
public interface RecurrenceOptionService {

    //Create
    RecurrenceOption addRecurrenceOption(RecurrenceOption option);

    void addRecurrenceOptions(List<RecurrenceOption> options);

    //Read
    List<RecurrenceOption> getAllRecurrenceOptions();

    RecurrenceOption getRecurrenceOption(int optionId);

    List<RecurrenceOption> getRecurrenceOptionsByRecurrenceType(int recurrenceTypeId);

    //Update
    void editRecurrenceOption(RecurrenceOption option);

    void editRecurrenceOptionList(int recurrenceTypeId, List<RecurrenceOption> options);

    //Delete
    void deleteRecurrenceOption(int optionId);

    void deleteRecurrenceOptionsByRecurrenceType(int recurrenceTypeId);

    void deleteAllRecurrenceOptions();
}
