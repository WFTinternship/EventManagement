package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.model.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
@Component
public class EventDAOImpl extends GenericDAO implements EventDAO {

    static final Logger LOGGER = Logger.getLogger(EventDAOImpl.class);

    @Override
    public int addEvent(Event event) throws DAOException {

        Connection conn = null;
        int eventId = 0;

        try {
            //get connection
            conn = dataSource.getConnection();

            //insert event info and get generated event id
            eventId = addEvent(event, conn);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(conn);
        }
        return eventId;
    }

    @Override
    public int addEventWithRecurrences(Event event) throws DAOException {

        Connection conn = null;
        int eventId = 0;

        try {
            //get connection
            conn = dataSource.getConnection();

            //start transaction
            conn.setAutoCommit(false);

            //insert event main info and get generated event id
            eventId = addEvent(event, conn);
            event.setId(eventId);

            //set event id to recurrences
            for (Recurrence recurrence : event.getEventRecurrences()) {
                recurrence.setEventId(eventId);
            }

            //insert event recurrence info
            RecurrenceDAO recurrenceDAO = new RecurrenceDAOImpl();
            ((RecurrenceDAOImpl) recurrenceDAO).addEventRecurrences(event.getEventRecurrences(), conn);

            //commit transaction
            conn.commit();
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException re) {
                LOGGER.error("Transaction failed!", e);
                throw new DAOException("Could not insert event into db. Transaction failed!", e);
            }
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(conn);
        }
        return eventId;
    }

    @Override
    public Event getEventById(int eventId) throws DAOException, ObjectNotFoundException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Event event = null;
        String query = "SELECT * FROM (event LEFT JOIN event_category " +
                "ON event.category_id = event_category.id) WHERE event.id = ?";
        try {
            //get connection
            conn = dataSource.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, eventId);

            //execute query
            rs = stmt.executeQuery();

            //get results from event table
            List<Event> eventList = createEventListFromRS(rs);
            if (eventList.isEmpty()) {
                throw new ObjectNotFoundException("Event with id " + eventId + " not found!");
            }

            event = eventList.get(0);

            //get invitations list
            InvitationDAO invitationDAO = new InvitationDAOImpl();
            List<Invitation> invitations = invitationDAO.getInvitationsByEventId(eventId);
            if (!invitations.isEmpty()) {
                event.setInvitations(invitations);
            }

            //get event recurrences
            RecurrenceDAO recurrenceDAO = new RecurrenceDAOImpl();
            List<Recurrence> recurrences = recurrenceDAO.getRecurrencesByEventId(eventId);
            if (!recurrences.isEmpty()) {
                event.setEventRecurrences(recurrences);
            }

            //get media list
            MediaDAO mediaDAO = new MediaDAOImpl();
            List<Media> media = mediaDAO.getMediaByEventId(eventId);
            if (!media.isEmpty()) {
                event.setMedia(media);
            }
        } catch (SQLException e) {
            LOGGER.error("SQL Exception ", e);
            throw new DAOException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return event;
    }

    @Override
    public List<Event> getEventsByCategory(int categoryId) throws DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Event> eventsList = null;
        String query = "SELECT * FROM (event LEFT JOIN event_category " +
                "ON event.category_id = event_category.id) " +
                "WHERE event.category_id = ?";
        try {
            //get connection
            conn = dataSource.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, categoryId);

            //execute query
            rs = stmt.executeQuery();

            //get results
            eventsList = createEventListFromRS(rs);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return eventsList;
    }

    @Override
    public User getEventOrganizer(int eventId) throws DAOException, ObjectNotFoundException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        User user;
        String query = "SELECT * FROM (event LEFT JOIN user " +
                "ON event.user_id = user.id) " +
                "WHERE event.user_role = \'Organizer\' AND event.id = ?";
        try {
            //get connection
            conn = dataSource.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, eventId);

            //execute query
            rs = stmt.executeQuery();

            //get results
            user = createUserFromRS(rs);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return user;
    }

    @Override
    public List<Event> getUserOrganizedEvents(int userId) throws DAOException {
        return getUserEventsByField(userId, "user_role", "Organizer");
    }

    @Override
    public List<Event> getUserParticipatedEvents(int userId) throws DAOException {
        return getUserEventsByField(userId, "participated", true);
    }

    @Override
    public List<Event> getUserEventsByResponse(int userId, String userResponse) throws DAOException {
        return getUserEventsByField(userId, "user_response.title", userResponse);
    }

    @Override
    public List<Event> getAllEvents() throws DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Event> eventsList = null;

        String query = "SELECT * FROM event " +
                "LEFT JOIN event_category ON event.category_id = event_category.id ";
        try {
            //get connection
            conn = dataSource.getConnection();

            //create statement
            stmt = conn.prepareStatement(query);

            //execute query
            rs = stmt.executeQuery();

            //get results
            eventsList = createEventListFromRS(rs);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return eventsList;
    }

    @Override
    public void updateEvent(Event event) throws DAOException, ObjectNotFoundException {

        Connection conn = null;
        PreparedStatement stmt = null;

        String query = "UPDATE event SET " +
                "title = ?, short_desc = ?, full_desc = ?, location = ?, lat = ?, lng = ?, " +
                "file_path = ?, image_path = ?, category_id = ?, public_accessed = ?, guests_allowed = ?, " +
                "start = ?, end = ?, creation_date = ?, last_modified = ?" +
                "WHERE id = ?";
        try {
            //get connection
            conn = dataSource.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setString(1, event.getTitle());
            stmt.setString(2, event.getShortDescription());
            stmt.setString(3, event.getFullDescription());
            stmt.setString(4, event.getLocation());
            stmt.setDouble(5, event.getLat());
            stmt.setDouble(6, event.getLng());
            stmt.setString(7, event.getFilePath());
            stmt.setString(8, event.getImagePath());

            if (event.getCategory() != null) {
                stmt.setInt(9, event.getCategory().getId());
            } else {
                stmt.setObject(9, null);
            }

            stmt.setBoolean(10, event.isPublicAccessed());
            stmt.setBoolean(11, event.isGuestsAllowed());

            if (event.getStartDate() != null)
                stmt.setTimestamp(12, new Timestamp(event.getStartDate().getTime()));
            else
                stmt.setObject(12, null);


            if (event.getEndDate() != null)
                stmt.setTimestamp(13, new Timestamp(event.getEndDate().getTime()));
            else
                stmt.setObject(13, null);


            if (event.getCreationDate() != null) {
                stmt.setTimestamp(14, new Timestamp(event.getCreationDate().getTime()));
            } else {
                stmt.setObject(14, null);
            }

            if (event.getLastModifiedDate() != null) {
                stmt.setTimestamp(15, new Timestamp(event.getLastModifiedDate().getTime()));
            } else {
                stmt.setObject(15, null);
            }
            stmt.setInt(16, event.getId());

            //execute query
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new ObjectNotFoundException("Event with id " + event.getId() + " not found!");
            }
        } catch (SQLException e) {
            LOGGER.error("SQL Exception ", e);
            throw new DAOException(e);
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    @Override
    public void deleteEvent(int eventId) throws DAOException, ObjectNotFoundException {
        deleteRecordById("event", eventId);
    }

    @Override
    public void deleteAllEvents() throws DAOException {
        deleteAllRecords("event");
    }

    //helper methods
    private int addEvent(Event event, Connection conn) throws DAOException {

        PreparedStatement stmt = null;

        int id = 0;
        String insertEvent = "INSERT INTO event "
                + "(title, short_desc, full_desc, location, lat, lng, file_path, image_path, "
                + "category_id, public_accessed, guests_allowed, start, end, creation_date) VALUES "
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

        try {
            //create and initialize statement
            stmt = conn.prepareStatement(insertEvent, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, event.getTitle());
            stmt.setString(2, event.getShortDescription());
            stmt.setString(3, event.getFullDescription());
            stmt.setString(4, event.getLocation());
            stmt.setDouble(5, event.getLat());
            stmt.setDouble(6, event.getLng());
            stmt.setString(7, event.getFilePath());
            stmt.setString(8, event.getImagePath());
            stmt.setInt(9, event.getCategory().getId());
            stmt.setBoolean(10, event.isPublicAccessed());
            stmt.setBoolean(11, event.isGuestsAllowed());

            if (event.getStartDate() != null)
                stmt.setTimestamp(12, new Timestamp(event.getStartDate().getTime()));
            else
                stmt.setObject(12, null);


            if (event.getEndDate() != null)
                stmt.setTimestamp(13, new Timestamp(event.getEndDate().getTime()));
            else
                stmt.setObject(13, null);


            if (event.getCreationDate() != null) {
                stmt.setTimestamp(14, new Timestamp(event.getCreationDate().getTime()));
            } else {
                stmt.setObject(14, null);
            }

            //execute query
            stmt.executeUpdate();

            //get inserted id
            id = getInsertedId(stmt);

        } catch (SQLException e) {
            LOGGER.error("Exception ", e);
            throw new DAOException(e);
        } finally {
            closeResources(stmt);
        }
        return id;
    }

    private List<Event> getUserEventsByField(int userId, String columnName, Object columnValue) throws DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Event> eventsList = null;
        String sqlStr = "SELECT event.*, event_category.* FROM event " +
                "LEFT JOIN event_category ON event.category_id = event_category.id " +
                "LEFT JOIN event_invitation ON event_invitation.event_id = event.id " +
                "LEFT JOIN user_response ON event_invitation.user_response = user_response.id " +
                "WHERE event_invitation.user_id = ? AND " + columnName + " = ?";
        try {
            //get connection
            conn = dataSource.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, userId);
            stmt.setObject(2, columnValue);

            //execute query
            rs = stmt.executeQuery();

            //get results
            eventsList = createEventListFromRS(rs);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return eventsList;
    }

    private List<Event> createEventListFromRS(ResultSet rs) throws SQLException {
        List<Event> eventsList = new ArrayList<>();

        while (rs.next()) {

            Category category = new Category();
            category.setId(rs.getInt("event_category.id"))
                    .setTitle(rs.getString("event_category.title"))
                    .setDescription(rs.getString("event_category.description"))
                    .setCreationDate(rs.getTimestamp("event_category.creation_date"));

            Event event = new Event();
            event.setId(rs.getInt("event.id"))
                    .setTitle(rs.getString("event.title"))
                    .setShortDescription(rs.getString("short_desc"))
                    .setFullDescription(rs.getString("full_desc"))
                    .setLocation(rs.getString("location"))
                    .setLat(rs.getDouble("lat"))
                    .setLng(rs.getDouble("lng"))
                    .setFilePath(rs.getString("file_path"))
                    .setImagePath(rs.getString("image_path"))
                    .setPublicAccessed(rs.getBoolean("public_accessed"))
                    .setGuestsAllowed(rs.getBoolean("guests_allowed"))
                    .setCreationDate(rs.getTimestamp("event.creation_date"))
                    .setLastModifiedDate(rs.getTimestamp("event.last_modified"))
                    .setStartDate(rs.getTimestamp("start"))
                    .setEndDate(rs.getTimestamp("end"))
                    .setCategory(category);

            eventsList.add(event);
        }
        return eventsList;
    }

    private User createUserFromRS(ResultSet rs) throws SQLException {
        User user = new User();

        while (rs.next()) {
            user.setId(rs.getInt("id"))
                    .setFirstName(rs.getString("first_name"))
                    .setLastName(rs.getString("last_name"))
                    .setPassword(rs.getString("password"))
                    .setEmail(rs.getString("email"))
                    .setPhoneNumber(rs.getString("phone_number"))
                    .setAvatarPath(rs.getString("avatar_path"))
                    .setVerified(rs.getBoolean("verified"))
                    .setRegistrationDate(rs.getTimestamp("registration_date"));
        }
        return user;
    }
}
