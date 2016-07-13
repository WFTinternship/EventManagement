package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventCategory;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hermine on 7/1/16.
 */
public class EventCategoryDAOImpl extends GenericDAO implements  EventCategoryDAO {

    public int insertCategory(EventCategory category) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int id = 0;

        try {
            //acquire connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            String query = "INSERT INTO event_category "
                    + "(title, description, creation_date) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, category.getTitle());
            stmt.setString(2, category.getDescription());

            if (category.getCreationDate() != null) {
                stmt.setTimestamp(3, new Timestamp(category.getCreationDate().getTime()));
            } else {
                stmt.setTimestamp(3, null);
            }

            //execute statement
            stmt.executeUpdate();

            //get generated id
            id = getInsertedId(stmt);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, conn);
        }
        return id;
    }

    //read
    public List<EventCategory> getAllCategories() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<EventCategory> categoriesList = null;

        try {
            //acquire connection
            conn = DataSourceManager.getInstance().getConnection();

            //create statement
            String query = "SELECT * FROM event_category";
            stmt = conn.prepareStatement(query);

            //execute statement
            rs = stmt.executeQuery();

            //get results
            categoriesList = createEventCategoryListFromRS(rs);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, conn);
        }
        return categoriesList;
    }

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

            //execute statement and get results
            rs = stmt.executeQuery();
            category = createEventCategoryListFromRS(rs).get(0);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, conn);
        }
        return category;
    }

    //update
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

            //execute statement
            affectedRows = stmt.executeUpdate();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;
    }

    //delete
    public boolean deleteCategory(int categoryId) {

        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;

        try {
            //acquire connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            String sqlStr = "DELETE FROM event_category WHERE id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, categoryId);

            //execute update
            affectedRows = stmt.executeUpdate();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;
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
