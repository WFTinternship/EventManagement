package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventCategory;

import java.util.List;

/**
 * Created by hermine on 7/1/16.
 */
public interface EventCategoryDAO  {
    public boolean insertCategory(EventCategory category);
    public List<EventCategory> getAllCategories();
    public EventCategory getCategoryById(int id);
    public boolean updateCategory(EventCategory category);
    public boolean deleteCategory(int categoryId);
}
