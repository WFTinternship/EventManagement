package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.model.Category;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public interface CategoryDAO {

    //insert data into db
    int addCategory(Category category) throws DuplicateEntryException, DAOException;

    //read data from db
    List<Category> getAllCategories() throws DAOException;

    Category getCategoryById(int categoryId) throws ObjectNotFoundException, DAOException;

    //update data in db
    boolean updateCategory(Category category) throws DuplicateEntryException, DAOException, ObjectNotFoundException;

    //delete data from db
    boolean deleteCategory(int categoryId) throws ObjectNotFoundException, DAOException;

    void deleteAllCategories() throws DAOException;
}
