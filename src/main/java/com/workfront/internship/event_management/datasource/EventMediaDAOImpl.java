package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventMedia;

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
            EventMedia media = new EventMedia();
            while (rs.next()) {
                media.setId(rs.getInt("id"))
                        .setPath(rs.getString("path"))
                        .setType("type")
                        .setEventId(rs.getInt("event_id"))
                        .setOwnerId(rs.getInt("owner_id"))
                        .setUploadDate(rs.getTimestamp("upload_date"));
                if(rs.getString("description") != null) {
                    media.setDescription(rs.getString("description"));
                }
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
        return getMediaByField("id", eventId);
    }

    public List<EventMedia> getMediaByType(String type) {
        return getMediaByField("type", type);
    }

    public List<EventMedia> getMediaByOwner(int eventId) {
        return getMediaByField("id", eventId);
    }

    public void insertMedia(EventMedia media) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            DataSourceManager dsManager = DataSourceManager.getInstance();
            conn = dsManager.getConnection();
            String sqlStr = "INSERT INTO event_media "
                    + "(path, type, description, event_id, owner_id) VALUES "
                    + "(?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setString(1, media.getPath());
            preparedStatement.setString(2, media.getType());
            preparedStatement.setString(3, media.getDescription());
            preparedStatement.setInt(4, media.getEventId());
            preparedStatement.setInt(5, media.getOwnerId());
            preparedStatement.executeUpdate();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    public void updateMedia(EventMedia media) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            DataSourceManager dsManager = DataSourceManager.getInstance();
            conn = dsManager.getConnection();
            String sqlStr = "UPDATE event_media SET path = ?, type = ?, " +
                    "description = ?, event_id = ?, owner_id = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setString(1, media.getPath());
            preparedStatement.setString(2, media.getType());
            preparedStatement.setString(3, media.getDescription());
            preparedStatement.setInt(4, media.getEventId());
            preparedStatement.setInt(5, media.getOwnerId());
            preparedStatement.setInt(6, media.getId());
            preparedStatement.executeUpdate();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
    }
    public void updateMediaDescription(int mediaId, String desc) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            DataSourceManager dsManager = DataSourceManager.getInstance();
            conn = dsManager.getConnection();
            String sqlStr = "UPDATE event_media SET description = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setString(1, desc);
            preparedStatement.setInt(2, mediaId);
            preparedStatement.executeUpdate();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    public void deleteMedia(int mediaId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            DataSourceManager dsManager = DataSourceManager.getInstance();
            conn = dsManager.getConnection();
            String sqlStr = "DELETE event_media WHERE id = ?";
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
            EventMedia media = new EventMedia();
            while (rs.next()) {
                media.setId(rs.getInt("id"))
                        .setPath(rs.getString("path"))
                        .setType("type")
                        .setEventId(rs.getInt("id"))
                        .setOwnerId(rs.getInt("owner_id"))
                        .setUploadDate(rs.getTimestamp("upload_date"));
                if(rs.getString("description") != null) {
                    media.setDescription(rs.getString("description"));
                }
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
