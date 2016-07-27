package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.model.Category;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public interface CategoryDAO {

    //insert data into db
    int addCategory(Category category);

    //read data from db
    List<Category> getAllCategories();

    Category getCategoryById(int categoryId);

    Category getCategoryByTitle(String categoryTitle);

    //update data in db
    boolean updateCategory(Category category);

    //delete data from db
    boolean deleteCategory(int categoryId);

    boolean deleteAllCategories();
}
