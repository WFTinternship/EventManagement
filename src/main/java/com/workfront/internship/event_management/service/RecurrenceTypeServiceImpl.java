package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.RecurrenceTypeDAO;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.RecurrenceType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.workfront.internship.event_management.service.util.Validator.isEmptyCollection;
import static com.workfront.internship.event_management.service.util.Validator.isValidRecurrenceType;

/**
 * Created by Hermine Turshujyan 7/27/16.
 */

@Component
class RecurrenceTypeServiceImpl implements RecurrenceTypeService {

    private static final Logger logger = Logger.getLogger(RecurrenceTypeServiceImpl.class);

    @Autowired
    private RecurrenceTypeDAO recurrenceTypeDAO;
    @Autowired
    private RecurrenceOptionService recurrenceOptionService;

    @Override
    public RecurrenceType addRecurrenceType(RecurrenceType recurrenceType) {
        if (!isValidRecurrenceType(recurrenceType)) {
            throw new InvalidObjectException("Invalid recurrence type");
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
            logger.error(e.getMessage(), e);
            throw new OperationFailedException("Recurrence type with title " + recurrenceType.getTitle() + " already exists!", e);
        }
        return recurrenceType;
    }

    @Override
    public RecurrenceType getRecurrenceTypeById(int id) {
        if (id < 0) {
            throw new InvalidObjectException("Invalid recurrence type id");
        }

        RecurrenceType recurrenceType = recurrenceTypeDAO.getRecurrenceTypeById(id);
        if (recurrenceType == null) {
            throw new ObjectNotFoundException("Recurrence type not found!");
        }

        return recurrenceType;
    }

    @Override
    public List<RecurrenceType> getAllRecurrenceTypes() {
            return recurrenceTypeDAO.getAllRecurrenceTypes();
    }

    @Override
    public boolean editRecurrenceType(RecurrenceType recurrenceType) {
        if (!isValidRecurrenceType(recurrenceType)) {
            throw new InvalidObjectException("Invalid recurrence type");
        }

        boolean success;
        try {
            //update recurrence type in db
            success = recurrenceTypeDAO.updateRecurrenceType(recurrenceType);
            if (success) {
                //update recurrence type options
                recurrenceOptionService.editRecurrenceOptionList(recurrenceType.getId(), recurrenceType.getRecurrenceOptions());
            }
        } catch (DuplicateEntryException e) {
            logger.error(e.getMessage(), e);
            throw new OperationFailedException("Recurrence type with title " + recurrenceType.getTitle() + " already exists!", e);
        }

        if (!success) {
            throw new ObjectNotFoundException("Recurrence type not found!");
        }

        return success;
    }

    @Override
    public boolean deleteRecurrenceType(int recurrenceTypeId) {
        if (recurrenceTypeId < 1) {
            throw new InvalidObjectException("Invalid recurrence type id");
        }

        return recurrenceTypeDAO.deleteRecurrenceType(recurrenceTypeId);
    }

    @Override
    public void deleteAllRecurrenceTypes() {
        recurrenceTypeDAO.deleteAllRecurrenceTypes();
    }
}
