package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.RecurrenceTypeDAO;
import com.workfront.internship.event_management.dao.RecurrenceTypeDAOImpl;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.RecurrenceType;
import org.apache.log4j.Logger;

import java.util.List;

import static com.workfront.internship.event_management.service.util.Validator.isEmptyCollection;
import static com.workfront.internship.event_management.service.util.Validator.isValidRecurrenceType;

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
        if (id < 0) {
            throw new OperationFailedException("Invalid recurrence type id");
        }

        try {
            return recurrenceTypeDAO.getRecurrenceTypeById(id);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Recurrence type not found");
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
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
        if (!isValidRecurrenceType(recurrenceType)) {
            throw new OperationFailedException("Invalid recurrence type");
        }

        try {
            //update recurrence type in db
            recurrenceTypeDAO.updateRecurrenceType(recurrenceType);

            //update recurrence type options
            RecurrenceOptionService recurrenceOptionService = new RecurrenceOptionServiceImpl();
            recurrenceOptionService.editRecurrenceOptionList(recurrenceType.getId(), recurrenceType.getRecurrenceOptions());
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
