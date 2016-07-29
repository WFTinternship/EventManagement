package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.Category;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */
interface CategoryService {

    //CRUD operations with category
    Category addCategory(Category category);

    Category getCategoryById(int categoryId);

    Category getCategoryByTitle(String categoryTitle);

    List<Category> getAllCategories();

    void editCategory(Category category);

    void deleteCategory(int categoryId);

    void deleteAllCategories();
}
