package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.RecurrenceOption;

import java.sql.Connection;
import java.util.List;

/**
 * Created by Hermine Turshujyan 7/14/16.
 */
public interface RecurrenceOptionDAO {

    //insert data into db
    int addRecurrenceOption(RecurrenceOption option);

    int addRecurrenceOption(RecurrenceOption option, Connection conn);


    //read data from db
    List<RecurrenceOption> getAllRecurrenceOptions();

    RecurrenceOption getRecurrenceOption(int optionId);

    List<RecurrenceOption> getRecurrenceOptionsByRecurrenceType(int recurrenceTypeId);

    //update data in db
    boolean updateRecurrenceOption(RecurrenceOption option);

    //delete data from db
    boolean deleteRecurrenceOption(int optionId);

    boolean deleteRecurrenceOptionsByRecurrenceType(int recurrenceTypeId);

    boolean deleteAllRecurrenceOptions();

}
