package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventMedia;

import java.beans.PropertyVetoException;
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

    //CREATE
    public boolean insertMedia(EventMedia media) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "INSERT INTO event_media "
                    + "(path, type, description, event_id, uploader_id) VALUES "
                    + "(?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setString(1, media.getPath());
            stmt.setString(2, media.getType());
            stmt.setString(3, media.getDescription());
            stmt.setInt(4, media.getEventId());
            stmt.setInt(5, media.getUploaderId());
            affectedRows = stmt.executeUpdate();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;
    }

    public boolean insertMediaList(List<EventMedia> mediaList) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "INSERT INTO event_media "
                    + "(path, type, description, event_id, uploader_id) VALUES "
                    + "(?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sqlStr);
            conn.setAutoCommit(false);
            for(EventMedia media: mediaList) {
                stmt.setString(1, media.getPath());
                stmt.setString(2, media.getType());
                stmt.setString(3, media.getDescription());
                stmt.setInt(4, media.getEventId());
                stmt.setInt(5, media.getUploaderId());
                stmt.addBatch();
            }
            affectedRows = stmt.executeBatch().length;
            conn.commit();
            conn.setAutoCommit(true);
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != mediaList.size() ;
    }

    //READ
    public List<EventMedia> getAllMedia() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EventMedia> mediaList = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT * FROM event_media";
            stmt = conn.prepareStatement(sqlStr);
            rs = stmt.executeQuery();
            mediaList = createMediaListFromRS(rs);
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
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

    public List<EventMedia> getMediaByUploader(int eventId) {
        return getMediaByField("id", eventId);
    }

    //UPDATE
    public boolean updateMediaDescription(int mediaId, String desc) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "UPDATE event_media SET description = ? WHERE id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setString(1, desc);
            stmt.setInt(2, mediaId);
            affectedRows = stmt.executeUpdate();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;
    }

    //DELETE
    public boolean deleteMedia(int mediaId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "DELETE event_media WHERE id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, mediaId);
            affectedRows = stmt.executeUpdate();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;
    }

    // helper methods
    private List<EventMedia> getMediaByField(String columnName, Object columnValue) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EventMedia> mediaList = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT * FROM event_media where " + columnName + " = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setObject(1, columnValue);
            rs = stmt.executeQuery();
            mediaList = createMediaListFromRS(rs);
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return mediaList;
    }

    private List<EventMedia> createMediaListFromRS(ResultSet rs) throws SQLException {
        List<EventMedia> mediaList = new ArrayList<EventMedia>();
        while (rs.next()) {
            EventMedia media = new EventMedia();
            media.setId(rs.getInt("id"))
                    .setEventId(rs.getInt("event_id"))
                    .setPath(rs.getString("path"))
                    .setType("type")
                    .setUploaderId(rs.getInt("uploader_id"))
                    .setUploadDate(rs.getTimestamp("upload_date"))
                    .setDescription(rs.getString("description"));
            mediaList.add(media);
        }
        return mediaList;
    }
}
