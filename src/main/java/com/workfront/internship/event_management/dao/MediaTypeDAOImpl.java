package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.model.MediaType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hermine Turshujyan 7/21/16.
 */

public class MediaTypeDAOImpl extends GenericDAO implements MediaTypeDAO {

    private DataSourceManager dataSourceManager;

    public MediaTypeDAOImpl(DataSourceManager dataSourceManager) throws Exception {
        super(dataSourceManager);
        this.dataSourceManager = dataSourceManager;
    }

    public MediaTypeDAOImpl() {
        try {
            this.dataSourceManager = DataSourceManager.getInstance();
        } catch (IOException | SQLException e) {
            LOGGER.error("Exception...", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public int addMediaType(MediaType mediaType) {

        Connection conn = null;
        PreparedStatement stmt = null;

        int id = 0;
        String query = "INSERT INTO media_type (title) VALUES (?)";

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, mediaType.getTitle());

            //execute query
            stmt.executeUpdate();

            //get generated id
            id = getInsertedId(stmt);

        } catch (SQLException e) {
            LOGGER.error("Exception...", e);
            throw new RuntimeException(e);
        } finally {
            closeResources(stmt, conn);
        }
        return id;
    }

    @Override
    public List<MediaType> getAllMediaTypes() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<MediaType> mediaTypesList = new ArrayList<>();

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create statement
            String query = "SELECT * FROM media_type";
            stmt = conn.prepareStatement(query);

            //execute query
            rs = stmt.executeQuery();

            //get results
            mediaTypesList = createMediaTypeListFromRS(rs);

        } catch (SQLException e) {
            LOGGER.error("Exception...", e);
            throw new RuntimeException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return mediaTypesList;
    }

    @Override
    public MediaType getMediaTypeById(int mediaTypeId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        MediaType mediaType = null;

        try {
            //acquire connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            String query = "SELECT * FROM media_type WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, mediaTypeId);

            //execute query
            rs = stmt.executeQuery();

            //get results
            List<MediaType> mediaTypeList = createMediaTypeListFromRS(rs);
            if (!mediaTypeList.isEmpty()) {
                mediaType = mediaTypeList.get(0);
            }
        } catch (SQLException e) {
            LOGGER.error("Exception...", e);
            throw new RuntimeException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return mediaType;
    }

    @Override
    public boolean updateMediaType(MediaType mediaType) {

        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            String sqlStr = "UPDATE media_type SET title = ? WHERE id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setString(1, mediaType.getTitle());
            stmt.setString(2, mediaType.getTitle());

            //execute query
            affectedRows = stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error("Exception...", e);
            throw new RuntimeException(e);
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;
    }

    @Override
    public boolean deleteMediaType(int mediaTypeId) {
        return deleteRecordById("media_type", mediaTypeId);
    }

    @Override
    public boolean deleteAllMediaTypes() {
        return deleteAllRecords("media_type");
    }

    //helper methods
    private List<MediaType> createMediaTypeListFromRS(ResultSet rs) throws SQLException {

        List<MediaType> mediaTypeList = new ArrayList<>();

        while (rs.next()) {
            MediaType mediaType = new MediaType(rs.getInt("id"), rs.getString("title"));
            mediaTypeList.add(mediaType);
        }

        return mediaTypeList;
    }
}
