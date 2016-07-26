package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.exception.OperationFailedException;
import com.workfront.internship.event_management.model.Category;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */
interface CategoryService {

    //CRUD operations with category
    boolean addCategory(Category category) throws OperationFailedException;

    Category getCategoryById(int categoryId) throws OperationFailedException;

    Category getCategoryByTitle(String categoryTitle) throws OperationFailedException;

    boolean editCategory(Category category) throws OperationFailedException;

    boolean deleteCategory(int categoryId) throws OperationFailedException;

    //operations with all categories
    List<Category> getAllCategories() throws OperationFailedException;

    boolean deleteAllCategories() throws OperationFailedException;

}
