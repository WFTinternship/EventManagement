package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.RecurrenceOptionDAO;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.ObjectNotFoundException;
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

    private static final Logger LOGGER = Logger.getLogger(RecurrenceTypeServiceImpl.class);

    @Autowired
    private RecurrenceOptionDAO recurrenceOptionDAO;

    @Override
    public RecurrenceOption addRecurrenceOption(RecurrenceOption option) {
        if (!isValidRecurrenceOption(option)) {
            throw new OperationFailedException("Invalid recurrence option");
        }

        try {
            //insert recurrence option into db
            int recurrenceOptionId = recurrenceOptionDAO.addRecurrenceOption(option);

            //set generated it to recurrence type
            option.setId(recurrenceOptionId);
        } catch (DuplicateEntryException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Recurrence option with title " + option.getTitle() + " already exists!", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
        return option;
    }

    @Override
    public void addRecurrenceOptions(List<RecurrenceOption> options) {
        if (isEmptyCollection(options)) {
            throw new OperationFailedException("Empty recurrence option list");
        }

        for (RecurrenceOption option : options) {
            if (!isValidRecurrenceOption(option)) {
                throw new OperationFailedException("Invalid recurrence option");
            }
        }

        try {
            //insert recurrence options into db
            recurrenceOptionDAO.addRecurrenceOptions(options);
        } catch (DuplicateEntryException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Recurrence option already exists!", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public List<RecurrenceOption> getAllRecurrenceOptions() {
        try {
            return recurrenceOptionDAO.getAllRecurrenceOptions();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public RecurrenceOption getRecurrenceOption(int optionId) {
        if (optionId < 0) {
            throw new OperationFailedException("Invalid recurrence option id");
        }

        try {
            return recurrenceOptionDAO.getRecurrenceOptionById(optionId);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Recurrence type not found");
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public List<RecurrenceOption> getRecurrenceOptionsByRecurrenceType(int recurrenceTypeId) {
        if (recurrenceTypeId < 0) {
            throw new OperationFailedException("Invalid recurrence type id");
        }

        try {
            return recurrenceOptionDAO.getRecurrenceOptionsByRecurrenceType(recurrenceTypeId);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void editRecurrenceOption(RecurrenceOption option) {
        if (!isValidRecurrenceOption(option)) {
            throw new OperationFailedException("Invalid recurrence option");
        }

        try {
            //update recurrence type in db
            recurrenceOptionDAO.updateRecurrenceOption(option);
        } catch (DuplicateEntryException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Recurrence option with title " + option.getTitle() + " already exists!", e);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Recurrence option not found", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
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
    public void deleteRecurrenceOption(int optionId) {
        if (optionId < 1) {
            throw new OperationFailedException("Invalid recurrence option id");
        }
        try {
            recurrenceOptionDAO.deleteRecurrenceOption(optionId);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Recurrence option not found", e);
        }
    }

    @Override
    public void deleteRecurrenceOptionsByRecurrenceType(int recurrenceTypeId) {
        if (recurrenceTypeId < 1) {
            throw new OperationFailedException("Invalid recurrence type id");
        }

        try {
            recurrenceOptionDAO.deleteRecurrenceOptionsByRecurrenceType(recurrenceTypeId);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Recurrence options not found", e);
        }
    }

    @Override
    public void deleteAllRecurrenceOptions() {
        try {
            recurrenceOptionDAO.deleteAllRecurrenceOptions();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
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
