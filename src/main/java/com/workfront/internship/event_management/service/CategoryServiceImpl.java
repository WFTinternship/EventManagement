package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.CategoryDAO;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.Category;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.workfront.internship.event_management.service.util.Validator.isValidCategory;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */
@Component
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = Logger.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryDAO categoryDAO;

    @Override
    public Category addCategory(Category category) {
        //check if category object is valid
        if (!isValidCategory(category)) {
            throw new InvalidObjectException("Invalid category");
        }

        try {
            //insert category into db
            int categoryId = categoryDAO.addCategory(category);

            //set generated it to category
            category.setId(categoryId);
        } catch (DuplicateEntryException e) {
            logger.error(e.getMessage(), e);
            throw new OperationFailedException("Category with title " + category.getTitle() + " already exists!", e);
        }
        return category;
    }

    @Override
    public Category getCategoryById(int categoryId) {
        if (categoryId < 1) {
            throw new InvalidObjectException("Invalid category id");
        }

        Category category = categoryDAO.getCategoryById(categoryId);
        if (category == null) {
            throw new ObjectNotFoundException("Category not found");
        }
        return category;
    }

    @Override
    public boolean editCategory(Category category) {
        //check if category object is valid
        if (!isValidCategory(category)) {
            throw new InvalidObjectException("Invalid category");
        }

        //update category in db
        boolean success;
        try {
            success = categoryDAO.updateCategory(category);
        } catch (DuplicateEntryException e) {
            logger.error(e.getMessage(), e);
            throw new OperationFailedException("Category with title " + category.getTitle() + " already exists!", e);
        }

        if (!success) {
            throw new ObjectNotFoundException("Category not found!");
        }
        return success;
    }


    @Override
    public boolean deleteCategory(int categoryId) {
        if (categoryId < 1) {
            throw new InvalidObjectException("Invalid user id");
        }

        return categoryDAO.deleteCategory(categoryId);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    @Override
    public void deleteAllCategories() {
        categoryDAO.deleteAllCategories();
    }
}
