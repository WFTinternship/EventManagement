package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventCategory;

import java.util.List;

/**
 * Created by hermine on 7/1/16.
 */
public interface EventCategoryDAO  {
     int insertCategory(EventCategory category);
     List<EventCategory> getAllCategories();
     EventCategory getCategoryById(int id);
     boolean updateCategory(EventCategory category);
     boolean deleteCategory(int categoryId);
}
