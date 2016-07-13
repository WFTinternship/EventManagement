package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventCategory;

import java.util.List;

/**
 * Created by hermine on 7/1/16.
 */
public interface EventCategoryDAO  {

     //insert data into db
     int insertCategory(EventCategory category);

     //read data from db
     List<EventCategory> getAllCategories();
     EventCategory getCategoryById(int categoryId);

     //update data in db
     boolean updateCategory(EventCategory category);

     //delete data from db
     boolean deleteCategory(int categoryId);
     boolean deleteAllCategories();
}
