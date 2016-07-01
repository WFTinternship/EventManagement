package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventCategory;

import java.util.List;

/**
 * Created by hermine on 7/1/16.
 */
public interface EventCategoryDAO  {
    public List<EventCategory> getAllCategories();
    public EventCategory getCategoryById(int id);
    public void insertCategory(EventCategory category);
    public void updateCategory(EventCategory category);
    public void deleteCategory(int categoryId);
}
