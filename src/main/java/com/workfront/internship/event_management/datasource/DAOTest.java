package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.datehelpers.RecurrenceType;

/**
 * Created by Hermine Turshujyan on 6/29/16.
 */
public  class DAOTest {
    public static void main(String[] args) {


        RecurrenceTypeDAO recTypeDAO = new RecurrenceTypeDAOImpl();
        RecurrenceType recType = new RecurrenceType();
        //recType.setTitle("test_titleqg").setIntervalUnit("test_unit").setRepeatOnValues(new String[] {"val1", "val2"});
        //recTypeDAO.insertRecurrenceType(recType);
       // recTypeDAO.getAllRecurrenceTypes();
        recTypeDAO.getRecurrenceTypeById(2);
    }


}