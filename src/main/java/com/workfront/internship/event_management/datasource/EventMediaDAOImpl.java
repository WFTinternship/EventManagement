package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventMedia;
import com.workfront.internship.event_management.model.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hermine on 7/2/16.
 */
public class EventMediaDAOImpl extends GenericDAO implements EventMediaDAO {
    public List<EventMedia> getAllMedia() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EventMedia> mediaList = null;
        try {
            DataSourceManager dsManager = DataSourceManager.getInstance();
            conn = dsManager.getConnection();
            String sqlStr = "SELECT * FROM event_media";
            stmt = conn.prepareStatement(sqlStr);
            rs = stmt.executeQuery();
            mediaList = new ArrayList<EventMedia>();
            while (rs.next()) {
                EventMedia media = new EventMedia(rs.getInt("media_id"),
                        rs.getString("media_path"),
                        rs.getString("media_type"),
                        rs.getInt("event_id"),
                        rs.getInt("owner_id"));
                mediaList.add(media);
            }
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }

        return mediaList;
    }

    public List<EventMedia> getMediaByEvent(int eventId) {
        return getMediaByField("event_id", eventId);
    }

    public List<EventMedia> getMediaByType(String type) {
        return getMediaByField("type", type);
    }

    public List<EventMedia> getMediaByOwner(int eventId) {
        return getMediaByField("event_id", eventId);
    }

    public void insertMedia(EventMedia media) {

    }

    public void updateMedia(EventMedia media) {
        
    }

    public void deleteMedia(int mediaId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            DataSourceManager dsManager = DataSourceManager.getInstance();
            conn = dsManager.getConnection();
            String sqlStr = "DELETE event_media WHERE media_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setInt(1, mediaId);
            preparedStatement.executeUpdate();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    private List<EventMedia> getMediaByField(String columnName, Object columnValue) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EventMedia> mediaList = null;
        try {
            DataSourceManager dsManager = DataSourceManager.getInstance();
            conn = dsManager.getConnection();
            String sqlStr = "SELECT * FROM event_media where ? = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setString(1, columnName);
            stmt.setObject(2, columnValue);
            rs = stmt.executeQuery();
            mediaList = new ArrayList<EventMedia>();
            while (rs.next()) {
                EventMedia media = new EventMedia(rs.getInt("media_id"),
                        rs.getString("media_path"),
                        rs.getString("media_type"),
                        rs.getInt("event_id"),
                        rs.getInt("owner_id"));
                mediaList.add(media);
            }
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return mediaList;
    }
}
