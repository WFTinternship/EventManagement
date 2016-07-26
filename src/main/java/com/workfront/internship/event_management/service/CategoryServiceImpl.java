package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.CategoryDAO;
import com.workfront.internship.event_management.dao.CategoryDAOImpl;
import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.exception.DuplicateEntryException;
import com.workfront.internship.event_management.exception.OperationFailedException;
import com.workfront.internship.event_management.model.Category;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */
public class CategoryServiceImpl implements CategoryService {

    private CategoryDAO categoryDAO;

    public CategoryServiceImpl() throws OperationFailedException {
        try {
            categoryDAO = new CategoryDAOImpl();
        } catch (DAOException e) {
            throw new OperationFailedException("Database error!");
        }
    }

    @Override
    public boolean addCategory(Category category) throws OperationFailedException {

        boolean success = false;

        //check if category object is valid
        if (isValid(category)) {

            try {
                //check if category with this title already exists
                Category existingCategory = categoryDAO.getCategoryByTitle(category.getTitle());

                if (existingCategory == null) {
                    int userId = categoryDAO.addCategory(category);
                    if (userId != 0) {
                        success = true;
                    }
                } else {
                    throw new OperationFailedException("Category with this title already exists!");
                }
            } catch (DAOException e) {
                throw new OperationFailedException("Database error!");
            }
        }
        return success;
    }

    @Override
    public Category getCategoryById(int categoryId) throws OperationFailedException {
        Category category;
        try {
            category = categoryDAO.getCategoryById(categoryId);
        } catch (DAOException e) {
            throw new OperationFailedException("Database error!");
        }

        return category;
    }

    @Override
    public Category getCategoryByTitle(String categoryTitle) throws OperationFailedException {
        Category category;
        try {
            category = categoryDAO.getCategoryByTitle(categoryTitle);
        } catch (DAOException e) {
            throw new OperationFailedException("Database error!");
        }

        return category;
    }

    @Override
    public boolean editCategory(Category category) throws OperationFailedException {

        boolean success = false;

        if (isValid(category)) {
            try {
                success = categoryDAO.updateCategory(category);
               /* if(!success) {
                    throw new OperationFailedException("Non existing user.");
                }*/
            } catch (DuplicateEntryException e) {
                throw new OperationFailedException("Category with this title already exists!");
            } catch (DAOException e) {
                throw new OperationFailedException("Database error!");
            }
        }

        return success;
    }

    @Override
    public boolean deleteCategory(int categoryId) throws OperationFailedException {
        boolean success = false;
        if (categoryId > 0) {
            try {
                success = categoryDAO.deleteCategory(categoryId);
            /*  if(!success) {
                    throw new OperationFailedException("Non existing user.");
                }*/
            } catch (DAOException e) {
                throw new OperationFailedException("Database error!");
            }
        }
        return success;
    }

    @Override
    public List<Category> getAllCategories() throws OperationFailedException {
        List<Category> categoryList;

        try {
            categoryList = categoryDAO.getAllCategories();
        } catch (DAOException e) {
            throw new OperationFailedException("Database error!");
        }
        return categoryList;
    }

    @Override
    public boolean deleteAllCategories() throws OperationFailedException {
        boolean success;
        try {
            success = categoryDAO.deleteAllCategories();
        } catch (DAOException e) {
            throw new OperationFailedException("Database error!");
        }
        return success;
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
