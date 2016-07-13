package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventCategory;

import java.util.List;

/**
 * Created by hermine on 7/1/16.
 */
public interface EventCategoryDAO  {

     //create methods
     int insertCategory(EventCategory category);

     //read methods
     List<EventCategory> getAllCategories();
     EventCategory getCategoryById(int categoryId);

     //update methods
     boolean updateCategory(EventCategory category);

     //delete methods
     boolean deleteCategory(int categoryId);
}
