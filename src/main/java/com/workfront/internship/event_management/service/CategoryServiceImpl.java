package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.CategoryDAO;
import com.workfront.internship.event_management.dao.CategoryDAOImpl;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.Category;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */
public class CategoryServiceImpl implements CategoryService {

    private CategoryDAO categoryDAO;

    public CategoryServiceImpl() {
        try {
            categoryDAO = new CategoryDAOImpl();
        } catch (DAOException e) {
            throw new OperationFailedException("Database error!");
        }
    }

    @Override
    public int addCategory(Category category) throws DuplicateEntryException, DAOException {

        int categoryId = 0;

        //check if category object is valid
        if (isValid(category)) {
            categoryId = categoryDAO.addCategory(category);
        }

        return categoryId;
    }

    @Override
    public Category getCategoryById(int categoryId) throws DAOException, ObjectNotFoundException {

        if (categoryId > 0) {
            return categoryDAO.getCategoryById(categoryId);
        } else {
            throw new OperationFailedException("Invalid category id!");
        }
    }

    @Override
    public Category getCategoryByTitle(String categoryTitle) {
        Category category;
        try {
            return categoryDAO.getCategoryByTitle(categoryTitle);
        } catch (DAOException e) {
            throw new OperationFailedException("Database error!");
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void editCategory(Category category) {

        boolean success = false;

        if (isValid(category)) {
            try {
                categoryDAO.updateCategory(category);
               /* if(!success) {
                    throw new OperationFailedException("Non existing user.");
                }*/
            } catch (DuplicateEntryException e) {
                throw new OperationFailedException("Category with this title already exists!");
            } catch (DAOException e) {
                throw new OperationFailedException("Database error!");
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteCategory(int categoryId) {
        boolean success = false;
        if (categoryId > 0) {
            try {
                categoryDAO.deleteCategory(categoryId);
            /*  if(!success) {
                    throw new OperationFailedException("Non existing user.");
                }*/
            } catch (DAOException e) {
                throw new OperationFailedException("Database error!");
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categoryList;

        try {
            categoryList = categoryDAO.getAllCategories();
        } catch (DAOException e) {
            throw new OperationFailedException("Database error!");
        }
        return categoryList;
    }

    @Override
    public void deleteAllCategories() {
        boolean success;
        try {
            categoryDAO.deleteAllCategories();
        } catch (DAOException e) {
            throw new OperationFailedException("Database error!");
        }
    }

    //helper methods
    private boolean isValid(Category category) {

        boolean valid = false;

        if (category != null) {
            if (category.getTitle() != null) {
                valid = true;
            }

        }
        return valid;
    }
}
