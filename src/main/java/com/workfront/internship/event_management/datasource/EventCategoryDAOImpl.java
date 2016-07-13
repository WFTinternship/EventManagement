package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventCategory;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public class EventCategoryDAOImpl extends GenericDAO implements  EventCategoryDAO {

    @Override
    public int insertCategory(EventCategory category) {

        Connection conn = null;
        PreparedStatement stmt = null;
        int id = 0;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            String query = "INSERT INTO events_category " +
                    "(title, description, creation_date) VALUES (?, ?, ?)";
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

        } catch (SQLException | IOException e) {
            logger.error("Exception...", e);
        } finally {
            closeResources(stmt, conn);
        }
        return id;
    }

    @Override
    public List<EventCategory> getAllCategories() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EventCategory> categoriesList = null;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create statement
            String query = "SELECT * FROM event_category";
            stmt = conn.prepareStatement(query);

            //execute query
            rs = stmt.executeQuery();

            //get results
            categoriesList = createEventCategoryListFromRS(rs);

        } catch (SQLException | IOException e) {
            logger.error("Exception...", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return categoriesList;
    }

    @Override
    public EventCategory getCategoryById(int id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        EventCategory category = null;

        try {
            //acquire connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            String query = "SELECT * FROM event_category where id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);

            //execute query
            rs = stmt.executeQuery();

            //get results
            category = createEventCategoryListFromRS(rs).get(0);

        } catch (SQLException | IOException e) {
            logger.error("Exception...", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return category;
    }

    @Override
    public boolean updateCategory(EventCategory category) {

        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;

        try {
            //acquire connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            String sqlStr = "UPDATE event_category SET title = ?, description = ? WHERE id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setString(1, category.getTitle());
            stmt.setString(2, category.getDescription());
            stmt.setInt(3, category.getId());

            //execute query
            affectedRows = stmt.executeUpdate();

        } catch (SQLException | IOException e) {
            logger.error("Exception...", e);
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;
    }

    @Override
    public boolean deleteCategory(int categoryId) {
        return deleteRecordById("event_category", categoryId);
    }

    @Override
    public boolean deleteAllCategories() {
        return deleteAllRecords("event_category");
    }

    //helper methods
    private List<EventCategory> createEventCategoryListFromRS(ResultSet rs) throws SQLException {

        List<EventCategory> categoryList = new ArrayList<EventCategory>();

        while (rs.next()) {
            EventCategory category = new EventCategory();
            category.setId(rs.getInt("id"))
                    .setTitle(rs.getString("title"))
                    .setDescription(rs.getString("description"))
                    .setCreationDate(rs.getTimestamp("creation_date"));

            categoryList.add(category);
        }

        return categoryList;
    }
}
