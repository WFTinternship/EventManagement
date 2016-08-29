package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.Category;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */
public interface CategoryService {

    //CRUD operations with category
    Category addCategory(Category category);

    Category getCategoryById(int categoryId);

    List<Category> getAllCategories();

    boolean editCategory(Category category);

    boolean deleteCategory(int categoryId);

    void deleteAllCategories();
}
