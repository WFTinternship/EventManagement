package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.model.Category;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public interface CategoryDAO {

    //insert data into db
    int addCategory(Category category) throws DAOException;

    //read data from db
    List<Category> getAllCategories() throws DAOException;

    Category getCategoryById(int categoryId) throws DAOException;

    Category getCategoryByTitle(String categoryTitle) throws DAOException;

    //update data in db
    boolean updateCategory(Category category) throws DAOException;

    //delete data from db
    boolean deleteCategory(int categoryId) throws DAOException;

    boolean deleteAllCategories() throws DAOException;
}
