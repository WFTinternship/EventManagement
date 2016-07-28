package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.RecurrenceOption;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/28/16.
 */
public interface RecurrenceOptionService {

    //CRUD methods for recurrence option
    int addRecurrenceOption(RecurrenceOption option);

    int addRecurrenceOptions(List<RecurrenceOption> options);

    List<RecurrenceOption> getAllRecurrenceOptions();

    RecurrenceOption getRecurrenceOption(int optionId);

    List<RecurrenceOption> getRecurrenceOptionsByRecurrenceType(int recurrenceTypeId);

    boolean updateRecurrenceOption(RecurrenceOption option);

    boolean deleteRecurrenceOption(int optionId);

    boolean deleteRecurrenceOptionsByRecurrenceType(int recurrenceTypeId);

    boolean deleteAllRecurrenceOptions();
}
