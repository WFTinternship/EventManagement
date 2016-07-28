package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.model.RecurrenceOption;
import com.workfront.internship.event_management.model.RecurrenceType;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/27/16.
 */
public class RecurrenceTypeServiceImpl implements RecurrenceTypeService {
    @Override
    public int addRecurrenceType(RecurrenceType recurrenceType) {
        return 0;
    }

    @Override
    public int addRecurrenceTypeWithOptions(RecurrenceType recurrenceType) {
        return 0;
    }

    @Override
    public List<RecurrenceType> getAllRecurrenceTypes() {
        return null;
    }

    @Override
    public RecurrenceType getRecurrenceTypeById(int id) {
        return null;
    }

    @Override
    public RecurrenceType getRecurrenceTypeWithOptionsById(int id) {
        return null;
    }

    @Override
    public boolean updateRecurrenceType(RecurrenceType recurrenceType) {
        return false;
    }

    @Override
    public boolean deleteRecurrenceType(int id) throws DAOException {
        return false;
    }

    @Override
    public boolean deleteAllRecurrenceTypes() throws DAOException {
        return false;
    }
}
