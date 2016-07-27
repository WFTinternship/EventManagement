package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.Category;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */
interface CategoryService {

    //CRUD operations with category
    int addCategory(Category category);

    Category getCategoryById(int categoryId);

    Category getCategoryByTitle(String categoryTitle);

    boolean editCategory(Category category);

    boolean deleteCategory(int categoryId);

    //operations with all categories
    List<Category> getAllCategories();

    boolean deleteAllCategories();

}
