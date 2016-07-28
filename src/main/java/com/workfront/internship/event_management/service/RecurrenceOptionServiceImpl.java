package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.RecurrenceOption;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/28/16.
 */
public class RecurrenceOptionServiceImpl implements RecurrenceOptionService {
    @Override
    public int addRecurrenceOption(RecurrenceOption option) {
        return 0;
    }

    @Override
    public int addRecurrenceOptions(List<RecurrenceOption> options) {
        return 0;
    }

    @Override
    public List<RecurrenceOption> getAllRecurrenceOptions() {
        return null;
    }

    @Override
    public RecurrenceOption getRecurrenceOption(int optionId) {
        return null;
    }

    @Override
    public List<RecurrenceOption> getRecurrenceOptionsByRecurrenceType(int recurrenceTypeId) {
        return null;
    }

    @Override
    public boolean updateRecurrenceOption(RecurrenceOption option) {
        return false;
    }

    @Override
    public boolean deleteRecurrenceOption(int optionId) {
        return false;
    }

    @Override
    public boolean deleteRecurrenceOptionsByRecurrenceType(int recurrenceTypeId) {
        return false;
    }

    @Override
    public boolean deleteAllRecurrenceOptions() {
        return false;
    }
}
