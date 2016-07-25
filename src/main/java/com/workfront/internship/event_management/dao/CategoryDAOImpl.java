package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.exception.DuplicateEntryException;
import com.workfront.internship.event_management.model.Category;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public class CategoryDAOImpl extends GenericDAO implements CategoryDAO {

    private DataSourceManager dataSourceManager;

    public CategoryDAOImpl(DataSourceManager dataSourceManager) throws Exception {
        super(dataSourceManager);
        this.dataSourceManager = dataSourceManager;
    }

    public CategoryDAOImpl() throws DAOException {
        try {
            this.dataSourceManager = DataSourceManager.getInstance();
        } catch (IOException | SQLException e) {
            LOGGER.error("Could not instantiate data source manager for CategoryDAO", e);
            throw new DAOException();
        }
    }

    @Override
    public int addCategory(Category category) throws DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;

        int id = 0;
        String query = "INSERT INTO event_category " +
                "(title, description, creation_date) VALUES (?, ?, ?)";

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, category.getTitle());
            stmt.setString(2, category.getDescription());

            if (category.getCreationDate() != null) {
                stmt.setTimestamp(3, new Timestamp(category.getCreationDate().getTime()));
            } else {
                stmt.setTimestamp(3, null);
            }

            //execute query
            stmt.executeUpdate();

            //get generated id
            id = getInsertedId(stmt);

        } catch (SQLIntegrityConstraintViolationException e) {
            LOGGER.error("Duplicate category entry", e);
            throw new DuplicateEntryException();
        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException();
        } finally {
            closeResources(stmt, conn);
        }
        return id;
    }

    @Override
    public List<Category> getAllCategories() throws DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Category> categoriesList = new ArrayList<>();

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create statement
            String query = "SELECT * FROM event_category";
            stmt = conn.prepareStatement(query);

            //execute query
            rs = stmt.executeQuery();

            //get results
            categoriesList = createEventCategoryListFromRS(rs);

        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException();
        } finally {
            closeResources(rs, stmt, conn);
        }
        return categoriesList;
    }

    @Override
    public Category getCategoryById(int id) throws DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Category category = null;

        try {
            //acquire connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            String query = "SELECT * FROM event_category where id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);

            //execute query
            rs = stmt.executeQuery();

            //get results
            List<Category> categoryList = createEventCategoryListFromRS(rs);
            if (!categoryList.isEmpty()) {
                category = categoryList.get(0);
            }
        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException();
        } finally {
            closeResources(rs, stmt, conn);
        }
        return category;
    }

    @Override
    public Category getCategoryByTitle(String categoryTitle) throws DAOException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Category category = null;

        try {
            //acquire connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            String query = "SELECT * FROM event_category WHERE title = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, categoryTitle);

            //execute query
            rs = stmt.executeQuery();

            //get results
            List<Category> categoryList = createEventCategoryListFromRS(rs);
            if (!categoryList.isEmpty()) {
                category = categoryList.get(0);
            }
        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException();
        } finally {
            closeResources(rs, stmt, conn);
        }
        return category;
    }

    @Override
    public boolean updateCategory(Category category) throws DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            String sqlStr = "UPDATE event_category SET title = ?, description = ? WHERE id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setString(1, category.getTitle());
            stmt.setString(2, category.getDescription());
            stmt.setInt(3, category.getId());

            //execute query
            affectedRows = stmt.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            LOGGER.error("Duplicate category entry", e);
            throw new DuplicateEntryException();
        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException();
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;
    }

    @Override
    public boolean deleteCategory(int categoryId) throws DAOException {
        return deleteRecordById("event_category", categoryId);
    }

    @Override
    public boolean deleteAllCategories() throws DAOException {
        return deleteAllRecords("event_category");
    }

    //helper methods
    private List<Category> createEventCategoryListFromRS(ResultSet rs) throws SQLException {

        List<Category> categoryList = new ArrayList<>();

        while (rs.next()) {
            Category category = new Category();
            category.setId(rs.getInt("id"))
                    .setTitle(rs.getString("title"))
                    .setDescription(rs.getString("description"))
                    .setCreationDate(rs.getTimestamp("creation_date"));

            categoryList.add(category);
        }

        return categoryList;
    }
}
