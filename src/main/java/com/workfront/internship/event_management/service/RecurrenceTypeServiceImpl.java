package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.RecurrenceTypeDAO;
import com.workfront.internship.event_management.dao.RecurrenceTypeDAOImpl;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.RecurrenceOption;
import com.workfront.internship.event_management.model.RecurrenceType;
import org.apache.log4j.Logger;

import java.util.List;

import static com.workfront.internship.event_management.util.Validator.*;

/**
 * Created by Hermine Turshujyan 7/27/16.
 */
public class RecurrenceTypeServiceImpl implements RecurrenceTypeService {

    private static final Logger LOGGER = Logger.getLogger(RecurrenceTypeServiceImpl.class);
    private RecurrenceTypeDAO recurrenceTypeDAO;

    public RecurrenceTypeServiceImpl() {
        try {
            recurrenceTypeDAO = new RecurrenceTypeDAOImpl();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public RecurrenceType addRecurrenceType(RecurrenceType recurrenceType) {
        if (!isValidRecurrenceType(recurrenceType)) {
            throw new OperationFailedException("Invalid recurrence type");
        }

        try {
            int recurrenceTypeId;

            //insert recurrence type into db
            if (isEmptyCollection(recurrenceType.getRecurrenceOptions())) {
                recurrenceTypeId = recurrenceTypeDAO.addRecurrenceType(recurrenceType);
            } else {
                recurrenceTypeId = recurrenceTypeDAO.addRecurrenceTypeWithOptions(recurrenceType);
            }

            //set generated it to recurrence type
            recurrenceType.setId(recurrenceTypeId);

        } catch (DuplicateEntryException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Recurrence type with title " + recurrenceType.getTitle() + " already exists!", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
        return recurrenceType;
    }

    @Override
    public RecurrenceType getRecurrenceTypeById(int id) {
       /* if (id < 0) {
            throw new OperationFailedException("Invalid recurrence type id");
        }

        try {
            return recurrenceTypeDAO.getRecurrenceTypeWithOptionsById(id);
        } catch (DAOException e) {
            e.printStackTrace();
        }*/
        return null;
    }


    @Override
    public List<RecurrenceType> getAllRecurrenceTypes() {
        try {
            return recurrenceTypeDAO.getAllRecurrenceTypes();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void updateRecurrenceType(RecurrenceType recurrenceType) {
        // TODO: 7/29/16 check
        if (!isValidRecurrenceType(recurrenceType)) {
            throw new OperationFailedException("Invalid recurrence type");
        }

        try {
            //update recurrence type in db
            recurrenceTypeDAO.updateRecurrenceType(recurrenceType);

            RecurrenceOptionService recurrenceOptionService = new RecurrenceOptionServiceImpl();

            //get new recurrence type's options
            List<RecurrenceOption> newOptions = recurrenceType.getRecurrenceOptions();

            //if new recurrenceType does not contain options, delete existing options from db
            if (isEmptyCollection(newOptions)) {
                recurrenceOptionService.deleteRecurrenceOptionsByRecurrenceType(recurrenceType.getId());
            } else {
                //get recurrence type's current options from db
                List<RecurrenceOption> dbOptions = recurrenceOptionService.getRecurrenceOptionsByRecurrenceType(recurrenceType.getId());

                //if there is no options in db, insert new options list
                if (isEmptyCollection(dbOptions)) {
                    recurrenceOptionService.addRecurrenceOptions(newOptions);
                } else {
                    // compare option's list from db with new option's list
                    for (RecurrenceOption option : dbOptions) {
                        if (!newOptions.contains(option)) {
                            recurrenceOptionService.deleteRecurrenceOption(option.getId());
                        } else if (option != newOptions.get(option.getId())) {
                            recurrenceOptionService.updateRecurrenceOption(newOptions.get(option.getId()));
                        }
                    }

                    for (RecurrenceOption option : newOptions) {
                        if (!dbOptions.contains(option)) {
                            recurrenceOptionService.addRecurrenceOption(option);
                        }
                    }


                }
            }


        } catch (DuplicateEntryException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Recurrence type with title " + recurrenceType.getTitle() + " already exists!", e);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Recurrence type not found", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteRecurrenceType(int recurrenceTypeId) {
        if (recurrenceTypeId < 1) {
            throw new OperationFailedException("Invalid recurrence type id");
        }

        try {
            recurrenceTypeDAO.deleteRecurrenceType(recurrenceTypeId);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Recurrence type not found", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteAllRecurrenceTypes() {
        try {
            recurrenceTypeDAO.deleteAllRecurrenceTypes();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }
}
