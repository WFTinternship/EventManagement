package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.model.Category;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public class CategoryDAOImpl extends GenericDAO implements CategoryDAO {

    static final Logger LOGGER = Logger.getLogger(CategoryDAOImpl.class);
    private DataSourceManager dataSourceManager;

    public CategoryDAOImpl(DataSourceManager dataSourceManager) throws Exception {
        super(dataSourceManager);
        this.dataSourceManager = dataSourceManager;
    }

    public CategoryDAOImpl() throws DAOException {
        try {
            this.dataSourceManager = DataSourceManager.getInstance();
        } catch (IOException | SQLException e) {
            LOGGER.error("Could not instantiate data source manager", e);
            throw new DAOException("Could not instantiate data source manager", e);
        }
    }

    @Override
    public int addCategory(Category category) throws DuplicateEntryException, DAOException {
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
            throw new DuplicateEntryException("Category with title " + category.getTitle() + " already exists!", e);
        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException(e);
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

        List<Category> categoryList = new ArrayList<>();
        String query = "SELECT * FROM event_category";

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create statement
            stmt = conn.prepareStatement(query);

            //execute query
            rs = stmt.executeQuery();

            //get results
            categoryList = createEventCategoryListFromRS(rs);
        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return categoryList;
    }

    @Override
    public Category getCategoryById(int categoryId) throws ObjectNotFoundException, DAOException {
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
            stmt.setInt(1, categoryId);

            //execute query
            rs = stmt.executeQuery();

            //get results
            List<Category> categoryList = createEventCategoryListFromRS(rs);
            if (categoryList.isEmpty()) {
                throw new ObjectNotFoundException("Category with id " + categoryId + " not found!");
            } else {
                category = categoryList.get(0);
            }
        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return category;
    }

    @Override
    public void updateCategory(Category category) throws DuplicateEntryException, DAOException, ObjectNotFoundException {
        Connection conn = null;
        PreparedStatement stmt = null;

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
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new ObjectNotFoundException("Category with id " + category.getId() + " not found!");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            LOGGER.error("Duplicate category entry", e);
            throw new DuplicateEntryException("Category with title " + category.getTitle() + " already exists!", e);
        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(stmt, conn);
        }
    }

    @Override
    public void deleteCategory(int categoryId) throws ObjectNotFoundException, DAOException {
        deleteRecordById("event_category", categoryId);
    }

    @Override
    public void deleteAllCategories() throws DAOException {
        deleteAllRecords("event_category");
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
