package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventCategory;
import com.workfront.internship.event_management.model.User;

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
            DataSourceManager dsManager = DataSourceManager.getInstance();
            conn = dsManager.getConnection();
            String query = "SELECT * FROM event_category";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            categoriesList = new ArrayList<EventCategory>();
            EventCategory category = new EventCategory();
            while (rs.next()) {
                category.setId(rs.getInt("id"))
                        .setTitle(rs.getString("title"))
                        .setCreationDate(rs.getTimestamp("creation_date"));
                String description = rs.getString("description");
                if (description != null) {
                    category.setDescription(description);
                }
                categoriesList.add(category);
            }
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
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
            DataSourceManager dsManager = DataSourceManager.getInstance();
            conn = dsManager.getConnection();
            String query = "SELECT * FROM event_category";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            category = new EventCategory();
            while (rs.first()) {
                category.setId(rs.getInt("id"))
                        .setTitle(rs.getString("title"))
                        .setCreationDate(rs.getTimestamp("creation_date"));
                String description = rs.getString("description");
                if (description != null) {
                    category.setDescription(description);
                }
            }
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return category;
    }

    public void insertCategory(EventCategory category) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            DataSourceManager dsManager = DataSourceManager.getInstance();
            conn = dsManager.getConnection();
            String sqlStr = "INSERT INTO event_category "
                    + "(title, description) VALUES "
                    + "(?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setString(1, category.getTitle());
            preparedStatement.setString(2, category.getDescription());
            preparedStatement.executeUpdate();

        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    public void updateCategory(EventCategory category) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            DataSourceManager dsManager = DataSourceManager.getInstance();
            conn = dsManager.getConnection();
            String sqlStr = "UPDATE event_category SET title = ?, description = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setString(1, category.getTitle());
            preparedStatement.setString(2, category.getDescription());
            preparedStatement.setInt(3, category.getId());
            preparedStatement.executeUpdate();

        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    public void deleteCategory(int categoryId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            DataSourceManager dsManager = DataSourceManager.getInstance();
            conn = dsManager.getConnection();
            String sqlStr = "DELETE event_category WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setInt(1, categoryId);
            preparedStatement.executeUpdate();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
    }
}
