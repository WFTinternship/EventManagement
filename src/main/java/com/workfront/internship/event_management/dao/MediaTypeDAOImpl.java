package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.model.MediaType;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hermine Turshujyan 7/21/16.
 */
@Component
public class MediaTypeDAOImpl extends GenericDAO implements MediaTypeDAO {

    static final Logger LOGGER = Logger.getLogger(MediaTypeDAOImpl.class);

    @Override
    public int addMediaType(MediaType mediaType) throws DAOException, DuplicateEntryException {
        Connection conn = null;
        PreparedStatement stmt = null;

        int id = 0;
        String query = "INSERT INTO media_type (title) VALUES (?)";

        try {
            //get connection
            conn = dataSource.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, mediaType.getTitle());

            //execute query
            stmt.executeUpdate();

            //get generated id
            id = getInsertedId(stmt);
        } catch (SQLIntegrityConstraintViolationException e) {
            LOGGER.error("Duplicate media type entry", e);
            throw new DuplicateEntryException("Media type with title " + mediaType.getTitle() + " already exists!", e);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(stmt, conn);
        }
        return id;
    }

    @Override
    public List<MediaType> getAllMediaTypes() throws DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<MediaType> mediaTypesList = new ArrayList<>();
        String query = "SELECT * FROM media_type";

        try {
            //get connection
            conn = dataSource.getConnection();

            //create statement
            stmt = conn.prepareStatement(query);

            //execute query
            rs = stmt.executeQuery();

            //get results
            mediaTypesList = createMediaTypeListFromRS(rs);

        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return mediaTypesList;
    }

    @Override
    public MediaType getMediaTypeById(int mediaTypeId) throws ObjectNotFoundException, DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        MediaType mediaType = null;
        String query = "SELECT * FROM media_type WHERE id = ?";

        try {
            //acquire connection
            conn = dataSource.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, mediaTypeId);

            //execute query
            rs = stmt.executeQuery();

            //get results
            List<MediaType> mediaTypeList = createMediaTypeListFromRS(rs);
            if (mediaTypeList.isEmpty()) {
                throw new ObjectNotFoundException("Media type with id " + mediaTypeId + " not found!");
            }
            mediaType = mediaTypeList.get(0);

        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return mediaType;
    }

    @Override
    public void updateMediaType(MediaType mediaType) throws DuplicateEntryException, DAOException, ObjectNotFoundException {

        Connection conn = null;
        PreparedStatement stmt = null;

        int affectedRows = 0;
        String sqlStr = "UPDATE media_type SET title = ? WHERE id = ?";

        try {
            //get connection
            conn = dataSource.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(sqlStr);
            stmt.setString(1, mediaType.getTitle());
            stmt.setInt(2, mediaType.getId());

            //execute query
            affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new ObjectNotFoundException("Media type with id " + mediaType.getId() + " not found!");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            LOGGER.error("Duplicate media type entry", e);
            throw new DuplicateEntryException("Media type with title " + mediaType.getTitle() + " already exists!", e);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(stmt, conn);
        }
    }

    @Override
    public void deleteMediaType(int mediaTypeId) throws ObjectNotFoundException, DAOException {
        deleteRecordById("media_type", mediaTypeId);
    }

    @Override
    public void deleteAllMediaTypes() throws DAOException {
        deleteAllRecords("media_type");
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
