package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.model.Category;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */
interface CategoryService {

    //CRUD operations with category

    int addCategory(Category category) throws DuplicateEntryException, DAOException;

    Category getCategoryById(int categoryId) throws DAOException, ObjectNotFoundException;

    Category getCategoryByTitle(String categoryTitle);

    List<Category> getAllCategories();

    void editCategory(Category category);

    void deleteCategory(int categoryId);


    void deleteAllCategories();

}
