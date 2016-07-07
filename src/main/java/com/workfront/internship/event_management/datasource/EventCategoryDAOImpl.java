package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventCategory;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hermine on 7/1/16.
 */
public class EventCategoryDAOImpl extends GenericDAO implements  EventCategoryDAO {

    public List<EventCategory> getAllCategories() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EventCategory> categoriesList = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String query = "SELECT * FROM event_category";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            categoriesList = createEventCategoryListFromRS(rs);
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
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
            conn = DataSourceManager.getInstance().getConnection();
            String query = "SELECT * FROM event_category";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            category = createEventCategoryFromRS(rs);
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return category;
    }

    public boolean insertCategory(EventCategory category) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int affectedRows = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "INSERT INTO event_category "
                    + "(title, description) VALUES "
                    + "(?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setString(1, category.getTitle());
            preparedStatement.setString(2, category.getDescription());
            affectedRows = preparedStatement.executeUpdate();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return affectedRows != 0;
    }

    public boolean updateCategory(EventCategory category) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int affectedRows = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "UPDATE event_category SET title = ?, description = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setString(1, category.getTitle());
            preparedStatement.setString(2, category.getDescription());
            preparedStatement.setInt(3, category.getId());
            affectedRows = preparedStatement.executeUpdate();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return affectedRows != 0;
    }

    public boolean deleteCategory(int categoryId) {
        return deleteEntryById("event_category", categoryId);
        /*Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int affectedRows = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "DELETE event_category WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setInt(1, categoryId);
            affectedRows = preparedStatement.executeUpdate();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return affectedRows != 0;*/
    }

    //helper methods
    private EventCategory createEventCategoryFromRS(ResultSet rs) throws SQLException {
        EventCategory category = new EventCategory();
        while (rs.next()) {
            category.setId(rs.getInt("id"))
                    .setTitle(rs.getString("title"))
                    .setDescription("description")
                    .setCreationDate(rs.getTimestamp("creation_date"));
        }
        return category;
    }

    private List<EventCategory> createEventCategoryListFromRS(ResultSet rs) throws SQLException {
        List<EventCategory> categoryList = new ArrayList<EventCategory>();
        while (rs.next()) {
            EventCategory category = new EventCategory();
            category.setId(rs.getInt("id"))
                    .setTitle(rs.getString("title"))
                    .setDescription("description")
                    .setCreationDate(rs.getTimestamp("creation_date"));
        }
        return categoryList;
    }
}
