package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventMedia;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hermine on 7/2/16.
 */
public class EventMediaDAOImpl extends GenericDAO implements EventMediaDAO {

    @Override
    public int insertMedia(EventMedia media) {

        Connection conn = null;
        PreparedStatement stmt = null;
        int id = 0;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            String sqlStr = "INSERT INTO event_media (event_id, path, type, description, uploader_id, upload_date) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sqlStr, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, media.getEventId());
            stmt.setString(2, media.getPath());
            stmt.setString(3, media.getType());
            stmt.setString(4, media.getDescription());
            stmt.setInt(5, media.getUploaderId());
            if(media.getUploadDate() != null) {
                stmt.setTimestamp(6, new Timestamp(media.getUploadDate().getTime()));
            } else {
                stmt.setObject(6, null);
            }

            //execute query
             stmt.executeUpdate();

            //get inserted id
            id = getInsertedId(stmt);

        } catch (SQLException | IOException e) {
            logger.error("Exception...", e);
            throw new RuntimeException(e);
        } finally {
            closeResources(stmt, conn);
        }
        return id;
    }

    @Override
    public EventMedia getMediaById(int mediaId) {

        List<EventMedia> mediaList = getMediaByField("id", mediaId);

        if (!mediaList.isEmpty()) {
            return mediaList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<EventMedia> getMediaByType(String type) {
        return getMediaByField("type", type);
    }

    @Override
    public List<EventMedia> getMediaByUploaderId(int uploaderId) {
        return getMediaByField("uploader_id", uploaderId);
    }

    @Override
    public List<EventMedia> getMediaByEventId(int eventId) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<EventMedia> mediaList = new ArrayList<>();

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            String sqlStr = "SELECT * FROM event_media WHERE event_id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, eventId);

            //execute query
            rs = stmt.executeQuery();

            //get results
            mediaList = createMediaListFromRS(rs);

        } catch (SQLException | IOException e) {
            logger.error("Exception...", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return mediaList;
    }

    @Override
    public List<EventMedia> getAllMedia() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EventMedia> mediaList = new ArrayList<>();

        try {
            //acquire connection
            conn = DataSourceManager.getInstance().getConnection();

            //create statement
            String sqlStr = "SELECT * FROM event_media";
            stmt = conn.prepareStatement(sqlStr);

            //execute statement
            rs = stmt.executeQuery();

            //get results
            mediaList = createMediaListFromRS(rs);

        } catch (SQLException | IOException e) {
            logger.error("Exception...", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return mediaList;
    }

    @Override
    public boolean updateMediaDescription(int mediaId, String desc) {

        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            String sqlStr = "UPDATE event_media SET description = ? WHERE id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setString(1, desc);
            stmt.setInt(2, mediaId);

            //execute query
            affectedRows = stmt.executeUpdate();

        } catch (SQLException | IOException e) {
            logger.error("Exception...", e);
        } finally {
            closeResources(null, stmt, conn);
        }
        return affectedRows != 0;
    }

    @Override
    public boolean deleteMedia(int mediaId) {
        return deleteRecordById("event_media", mediaId);
    }

    @Override
    public boolean deleteAllMedia() {
        return deleteAllRecords("event_media");
    }

    // helper methods
    private List<EventMedia> getMediaByField(String columnName, Object columnValue) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<EventMedia> mediaList = new ArrayList<>();

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            String sqlStr = "SELECT * FROM event_media where " + columnName + " = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setObject(1, columnValue);

            //execute statement
            rs = stmt.executeQuery();

            //get results
            mediaList = createMediaListFromRS(rs);

        } catch (SQLException | IOException e) {
            logger.error("Exception...", e);
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
                    .setType(rs.getString("type"))
                    .setPath(rs.getString("path"))
                    .setDescription(rs.getString("description"))
                    .setUploaderId(rs.getInt("uploader_id"))
                    .setUploadDate(rs.getTimestamp("upload_date"));

            mediaList.add(media);
        }
        return mediaList;
    }

}
