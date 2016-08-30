package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.RecurrenceOptionDAO;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.RecurrenceOption;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.workfront.internship.event_management.service.util.Validator.isEmptyCollection;
import static com.workfront.internship.event_management.service.util.Validator.isValidRecurrenceOption;

/**
 * Created by Hermine Turshujyan 7/28/16.
 */
@Component
public class RecurrenceOptionServiceImpl implements RecurrenceOptionService {

    private static final Logger logger = Logger.getLogger(RecurrenceTypeServiceImpl.class);

    @Autowired
    private RecurrenceOptionDAO recurrenceOptionDAO;

    @Override
    public RecurrenceOption addRecurrenceOption(RecurrenceOption option) {
        if (!isValidRecurrenceOption(option)) {
            throw new InvalidObjectException("Invalid recurrence option");
        }

        try {
            //insert recurrence option into db
            int recurrenceOptionId = recurrenceOptionDAO.addRecurrenceOption(option);

            //set generated it to recurrence type
            option.setId(recurrenceOptionId);
        } catch (DuplicateEntryException e) {
            logger.error(e.getMessage(), e);
            throw new OperationFailedException("Recurrence option with title " + option.getTitle() + " already exists!", e);
        }
        return option;
    }

    @Override
    public void addRecurrenceOptions(List<RecurrenceOption> options) {
        if (isEmptyCollection(options)) {
            throw new InvalidObjectException("Empty recurrence option list");
        }

        for (RecurrenceOption option : options) {
            if (!isValidRecurrenceOption(option)) {
                throw new InvalidObjectException("Invalid recurrence option");
            }
        }

        try {
            //insert recurrence options into db
            recurrenceOptionDAO.addRecurrenceOptions(options);
        } catch (DuplicateEntryException e) {
            logger.error(e.getMessage(), e);
            throw new OperationFailedException("Recurrence option already exists!", e);
        }
    }

    @Override
    public List<RecurrenceOption> getAllRecurrenceOptions() {
        return recurrenceOptionDAO.getAllRecurrenceOptions();
    }

    @Override
    public RecurrenceOption getRecurrenceOption(int optionId) {
        if (optionId < 0) {
            throw new InvalidObjectException("Invalid recurrence option id");
        }

        RecurrenceOption recurrenceOption = recurrenceOptionDAO.getRecurrenceOptionById(optionId);
        if (recurrenceOption == null) {
            throw new ObjectNotFoundException("Recurrence type not found");
        }

        return recurrenceOption;
    }

    @Override
    public List<RecurrenceOption> getRecurrenceOptionsByRecurrenceType(int recurrenceTypeId) {
        if (recurrenceTypeId < 0) {
            throw new InvalidObjectException("Invalid recurrence type id");
        }

        return recurrenceOptionDAO.getRecurrenceOptionsByRecurrenceType(recurrenceTypeId);
    }

    @Override
    public boolean editRecurrenceOption(RecurrenceOption option) {
        if (!isValidRecurrenceOption(option)) {
            throw new InvalidObjectException("Invalid recurrence option");
        }

        boolean success;
        try {
            success = recurrenceOptionDAO.updateRecurrenceOption(option);
        } catch (DuplicateEntryException e) {
            logger.error(e.getMessage(), e);
            throw new OperationFailedException("Recurrence option with title " + option.getTitle() + " already exists!", e);
        }

        if (!success) {
            throw new ObjectNotFoundException("Recurrence option not found");
        }
        return success;
    }

    @Override
    public void editRecurrenceOptionList(int recurrenceTypeId, List<RecurrenceOption> recurrenceOptions) {
        //if recurrence option list is empty, delete existing options from db
        if (isEmptyCollection(recurrenceOptions)) {
            deleteRecurrenceOptionsByRecurrenceType(recurrenceTypeId);
        } else {
            //get recurrence type's current options from db
            List<RecurrenceOption> dbOptions = getRecurrenceOptionsByRecurrenceType(recurrenceTypeId);

            //if there is no options in db, insert new options list
            if (isEmptyCollection(dbOptions)) {
                addRecurrenceOptions(recurrenceOptions);
            } else {
                // compare option's list from db with new option's list
                for (RecurrenceOption dbOption : dbOptions) {
                    int optionId = dbOption.getId();

                    if (getRecurrenceOptionWithId(recurrenceOptions, optionId) == null) {
                        deleteRecurrenceOption(optionId);
                    } else if (dbOption != getRecurrenceOptionWithId(recurrenceOptions, optionId)) {
                        editRecurrenceOption(getRecurrenceOptionWithId(recurrenceOptions, optionId));
                    }
                }

                for (RecurrenceOption option : recurrenceOptions) {
                    int optionId = option.getId();

                    if (getRecurrenceOptionWithId(dbOptions, optionId) == null) {
                        addRecurrenceOption(option);
                    }
                }
            }
        }
    }

    @Override
    public boolean deleteRecurrenceOption(int optionId) {
        if (optionId < 1) {
            throw new InvalidObjectException("Invalid recurrence option id");
        }
        return recurrenceOptionDAO.deleteRecurrenceOption(optionId);
    }

    @Override
    public void deleteRecurrenceOptionsByRecurrenceType(int recurrenceTypeId) {
        if (recurrenceTypeId < 1) {
            throw new InvalidObjectException("Invalid recurrence type id");
        }
        recurrenceOptionDAO.deleteRecurrenceOptionsByRecurrenceType(recurrenceTypeId);
    }

    @Override
    public void deleteAllRecurrenceOptions() {
        recurrenceOptionDAO.deleteAllRecurrenceOptions();
    }


    //helper methods
    private RecurrenceOption getRecurrenceOptionWithId(List<RecurrenceOption> recurrenceOptionList, int id) {
        for (RecurrenceOption recurrenceOption : recurrenceOptionList) {
            if (recurrenceOption.getId() == id) {
                return recurrenceOption;
            }
        }
        return null;
    }

}
