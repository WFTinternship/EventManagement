package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.model.Media;
import com.workfront.internship.event_management.model.MediaType;
import com.workfront.internship.event_management.model.User;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hermine Turshujyan 7/2/16.
 */
@Component
public class MediaDAOImpl extends GenericDAO implements MediaDAO {

    static final Logger LOGGER = Logger.getLogger(MediaTypeDAOImpl.class);

    @Override
    public int addMedia(Media media) throws DuplicateEntryException, DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;

        int id = 0;
        String query = "INSERT INTO event_media (event_id, path, media_type_id, description, uploader_id, upload_date) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            //get connection
            conn = dataSource.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, media.getEventId());
            stmt.setString(2, media.getName());
            stmt.setInt(3, media.getType().getId());
            stmt.setString(4, media.getDescription());
            stmt.setInt(5, media.getUploader().getId());
            if(media.getUploadDate() != null) {
                stmt.setTimestamp(6, new Timestamp(media.getUploadDate().getTime()));
            } else {
                stmt.setObject(6, null);
            }

            //execute query
             stmt.executeUpdate();

            //get inserted id
            id = getInsertedId(stmt);
        } catch (SQLIntegrityConstraintViolationException e) {
            LOGGER.error("Duplicate media entry", e);
            throw new DuplicateEntryException("Media with path " + media.getName() + " already exists!", e);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(stmt, conn);
        }
        return id;
    }

    @Override
    public void addMediaList(List<Media> mediaList) throws DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;

        String query = "INSERT INTO event_media (event_id, path, media_type_id, description, uploader_id, upload_date) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            //get connection
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            //create and initialize statement
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            for (Media media : mediaList) {
                stmt.setInt(1, media.getEventId());
                stmt.setString(2, media.getName());
                stmt.setInt(3, media.getType().getId());
                stmt.setString(4, media.getDescription());
                stmt.setInt(5, media.getUploader().getId());
                if (media.getUploadDate() != null) {
                    stmt.setTimestamp(6, new Timestamp(media.getUploadDate().getTime()));
                } else {
                    stmt.setObject(6, null);
                }
                stmt.addBatch();
            }

            //execute query
            stmt.executeBatch();

            conn.commit();
        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(stmt, conn);
        }
    }

    @Override
    public Media getMediaById(int mediaId) throws ObjectNotFoundException, DAOException {

        List<Media> mediaList = getMediaByField("id", mediaId);
        Media media = null;

        if (!mediaList.isEmpty()) {
            media = mediaList.get(0);
        }

        return media;
    }

    @Override
    public List<Media> getMediaByUploaderId(int uploaderId) throws DAOException {
        return getMediaByField("uploader_id", uploaderId);
    }

    @Override
    public List<Media> getMediaByEventId(int eventId) throws DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Media> mediaList = new ArrayList<>();
        String query = "SELECT * FROM event_media " +
                "LEFT JOIN media_type ON event_media.media_type_id = media_type.id " +
                "LEFT JOIN USER ON event_media.uploader_id = user.id " +
                "WHERE event_id = ?";

        try {
            //get connection
            conn = dataSource.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, eventId);

            //execute query
            rs = stmt.executeQuery();

            //get results
            mediaList = createMediaListFromRS(rs);

        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return mediaList;
    }

    @Override
    public List<Media> getAllMedia() throws DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Media> mediaList = new ArrayList<>();
        String query = "SELECT * FROM event_media " +
                "LEFT JOIN media_type ON event_media.media_type_id = media_type.id " +
                "LEFT JOIN user ON event_media.uploader_id = user.id ";


        try {
            //acquire connection
            conn = dataSource.getConnection();

            //create statement
            stmt = conn.prepareStatement(query);

            //execute statement
            rs = stmt.executeQuery();

            //get results
            mediaList = createMediaListFromRS(rs);

        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return mediaList;
    }

    @Override
    public boolean updateMediaDescription(int mediaId, String desc) throws DAOException, ObjectNotFoundException {

        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        String query = "UPDATE event_media SET description = ? WHERE id = ?";

        try {
            //get connection
            conn = dataSource.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setString(1, desc);
            stmt.setInt(2, mediaId);

            //execute query
            int affectedRows = stmt.executeUpdate();
            if (affectedRows != 0) {
                success = true;
            }
        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(null, stmt, conn);
        }
        return success;
    }

    @Override
    public boolean deleteMedia(int mediaId) throws DAOException, ObjectNotFoundException {
        return deleteRecordById("event_media", mediaId);
    }

    @Override
    public void deleteAllMedia() throws DAOException {
        deleteAllRecords("event_media");
    }

    // helper methods
    private List<Media> getMediaByField(String columnName, Object columnValue) throws DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Media> mediaList = new ArrayList<>();
        String query = "SELECT * FROM event_media " +
                "LEFT JOIN media_type ON event_media.media_type_id = media_type.id " +
                "LEFT JOIN user on user.id = event_media.uploader_id " +
                "WHERE event_media." + columnName + " = ?";
        try {
            //get connection
            conn = dataSource.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setObject(1, columnValue);

            //execute statement
            rs = stmt.executeQuery();

            //get results
            mediaList = createMediaListFromRS(rs);

        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return mediaList;
    }

    private List<Media> createMediaListFromRS(ResultSet rs) throws SQLException {

        List<Media> mediaList = new ArrayList<>();

        while (rs.next()) {
            MediaType mediaType = new MediaType(rs.getInt("media_type.id"), rs.getString("media_type.title"));

            User user = new User();
            user.setId(rs.getInt("user.id"))
                    .setFirstName(rs.getString("first_name"))
                    .setLastName(rs.getString("last_name"))
                    .setPassword(rs.getString("password"))
                    .setEmail(rs.getString("email"))
                    .setAvatarPath(rs.getString("avatar_path"))
                    .setPhoneNumber(rs.getString("phone_number"))
                    .setVerified(rs.getBoolean("verified"))
                    .setRegistrationDate(rs.getTimestamp("registration_date"));

            Media media = new Media();
            media.setId(rs.getInt("id"))
                    .setEventId(rs.getInt("event_id"))
                    .setType(mediaType)
                    .setName(rs.getString("path"))
                    .setDescription(rs.getString("description"))
                    .setUploader(user)
                    .setUploadDate(rs.getTimestamp("upload_date"));

            mediaList.add(media);
        }
        return mediaList;
    }

}
