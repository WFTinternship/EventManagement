package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.RepeatOption;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/14/16.
 */
public interface RepeatOptionDAO {

    //insert data into db
    int insertRepeatOption(RepeatOption option);
    int insertRepeatOptionsList(List<RepeatOption> options);

    //read data from db
    RepeatOption getRepeatOption(int optionId);
    List<RepeatOption> getRepeatOptionsByRepeatType(int repeatTypeId);

    //update data in db
    boolean updateRepeatOption(int optionId);

    //delete data from db
    boolean deleteRepeatOption(int optionId);

}
