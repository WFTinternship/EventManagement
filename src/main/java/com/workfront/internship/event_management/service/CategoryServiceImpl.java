package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.CategoryDAO;
import com.workfront.internship.event_management.dao.CategoryDAOImpl;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.Category;
import org.apache.log4j.Logger;

import java.util.List;

import static com.workfront.internship.event_management.service.util.Validator.isValidCategory;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */
public class CategoryServiceImpl implements CategoryService {

    private static final Logger LOGGER = Logger.getLogger(CategoryServiceImpl.class);
    private CategoryDAO categoryDAO;

    public CategoryServiceImpl() {
        try {
            categoryDAO = new CategoryDAOImpl();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public Category addCategory(Category category) {
        //check if category object is valid
        if (!isValidCategory(category)) {
            throw new OperationFailedException("Invalid category");
        }

        try {
            //insert category into db
            int categoryId = categoryDAO.addCategory(category);

            //set generated it to category
            category.setId(categoryId);
        } catch (DuplicateEntryException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Category with title " + category.getTitle() + " already exists!", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
        return category;
    }

    @Override
    public Category getCategoryById(int categoryId) {
        if (categoryId < 1) {
            throw new OperationFailedException("Invalid category id");
        }

        try {
            //get category from db
            return categoryDAO.getCategoryById(categoryId);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Category not found", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void editCategory(Category category) {
        //check if category object is valid
        if (!isValidCategory(category)) {
            throw new OperationFailedException("Invalid category");
        }

        try {
            //update category in db
            categoryDAO.updateCategory(category);
        } catch (DuplicateEntryException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Category with title " + category.getTitle() + " already exists!", e);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Category not found", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteCategory(int categoryId) {
        if (categoryId < 1) {
            throw new OperationFailedException("Invalid user id");
        }

        try {
            categoryDAO.deleteCategory(categoryId);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("User not found", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public List<Category> getAllCategories() {
        try {
            return categoryDAO.getAllCategories();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteAllCategories() {
        try {
            categoryDAO.deleteAllCategories();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }
}
