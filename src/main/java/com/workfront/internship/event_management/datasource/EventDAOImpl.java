package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.*;
import com.workfront.internship.event_management.model.DateRange;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hermine on 7/1/16.
 */
public class EventDAOImpl extends GenericDAO implements EventDAO {

    @Override
    public int insertEvent(Event event, int organizerId) {

        Connection conn = null;
        int eventId = 0;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //start transaction
            conn.setAutoCommit(false);

            //insert event main info and get generated event id
            eventId = insertEventMainInfo(event, conn);
            event.setId(eventId);

            //insert event organizer
/*            if (event.getInvitations() != null & event.getInvitations().size() == 1) {
                EventInvitationDAO invitationDAO = new EventInvitationDAOImpl();
                invitationDAO.addInvitation(event.getInvitations().get(0), conn);
            }
/*
            //insert event recurrence info
            if (event.getRecurrences() != null) {
                for (EventRecurrence recurrence : event.getRecurrences()) {
                    recurrence.setEventId(eventId);
                }
                EventRecurrenceDAO recurrenceDAO = new EventRecurrenceDAOImpl();
                recurrenceDAO.addEventRecurrences(event.getRecurrences(), conn);
            }*/

            //commit transaction
            conn.commit();

        } catch (IOException | SQLException e) { //todo add log4j
            try {
                conn.rollback();
            } catch (SQLException e1) {
                logger.error("Commit/rollback exception...", e);
            }
            logger.error("Exception...", e);
        } finally {
            closeResources(conn);
        }
        return eventId;
    }

    @Override
    public List<Event> getAllEvents() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Event> eventsList = null;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            String sqlStr = "SELECT * FROM event " +
                    "LEFT JOIN event_category ON event.category_id = event_category.id ";
            stmt = conn.prepareStatement(sqlStr);

            //execute query
            rs = stmt.executeQuery();

            //get results
            eventsList = createEventsListFromRS(rs);

        } catch (SQLException | IOException e) {
            logger.error("Exception...", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return eventsList;
    }

    @Override
    public Event getEventById(int eventId) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Event event = null;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create
            String sqlStr = "SELECT * FROM (event LEFT JOIN event_category " +
                    "ON event.category_id = event_category.id) WHERE event.id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, eventId);

            //execute query
            rs = stmt.executeQuery();
            //  event = createEventFromRS(rs);

            //get invitations list
            EventInvitationDAO invitationDAO = new EventInvitationDAOImpl();
            List<EventInvitation> invitations = invitationDAO.getInvitationsByEventId(eventId);
            event.setInvitations(invitations);

            //get event recurrence info
            EventRecurrenceDAO recurrenceDAO = new EventRecurrenceDAOImpl();
            List<EventRecurrence> recurrences = recurrenceDAO.getEventRecurrencesByEventId(eventId);
            event.setRecurrences(recurrences);

            //get media list
            EventMediaDAO mediaDAO = new EventMediaDAOImpl();
            List<EventMedia> media = mediaDAO.getMediaByEventId(eventId);
            event.setMedia(media);

        } catch (SQLException | IOException e) {
            logger.error("Exception ", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return event;
    }

    @Override
    public List<Event> getEventsByCategory(int categoryId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Event> eventsList = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT * FROM (event LEFT JOIN event_category " +
                    "ON event.category_id = event_category.id) " +
                    "WHERE event.category_id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, categoryId);
            rs = stmt.executeQuery();
            eventsList = createEventsListFromRS(rs);
        } catch (SQLException | IOException e) {
            logger.error("Exception ", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return eventsList;
    }

    @Override
    public List<Event> getEventsByUserId(String userRole, int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Event> eventsList = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT event.*, event_category.* " +
                    "FROM event " +
                    "LEFT JOIN event_invitation " +
                    "ON event_invitation.event_id = event.id " +
                    "LEFT JOIN event_category " +
                    "ON event.category_id = event_category.id " +
                    "WHERE user_id = ? AND user_role = ? ";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, userId);
            stmt.setString(2, userRole);

            //execute query
            rs = stmt.executeQuery();

            //get results
            eventsList = createEventsListFromRS(rs);

        } catch (SQLException | IOException e) {
            logger.error("Exception ", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return eventsList;
    }

    @Override
    public List<Event> getParticipatedEventsByUserId(int userId) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Event> eventsList = null;

        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT event.*, event_category.* FROM event " +
                    "LEFT JOIN event_invitation ON event_invitation.event_id = event.id " +
                    "LEFT JOIN event_category ON event.category_id = event_category.id " +
                    "WHERE user_id = ? AND participated = true ";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, userId);

            rs = stmt.executeQuery();

            eventsList = createEventsListFromRS(rs);
        } catch (SQLException | IOException e) {
            logger.error("Exception ", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return eventsList;
    }

    @Override
    public List<Event> getAcceptedEventsByUserId(int userId) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Event> eventsList = null;

        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT event.*, event_category.* " +
                    "FROM event " +
                    "LEFT JOIN event_invitation " +
                    "ON event_invitation.event_id = event.id " +
                    "LEFT JOIN event_category " +
                    "ON event.category_id = event_category.id " +
                    "WHERE user_id = ? AND user_response = \"Yes\"";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, userId);

            rs = stmt.executeQuery();

            eventsList = createEventsListFromRS(rs);
        } catch (SQLException | IOException e) {
            logger.error("Exception ", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return eventsList;
    }

    //UPDATE
    //????????
    @Override
    public boolean updateEvent(Event event) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "UPDATE event SET " +
                    "title = ?, short_desc = ?, full_desc = ?, location = ?, lat = ?, lng = ?, " +
                    "file_path = ?, image_path = ?, category_id = ?, public_accessed = ?, guests_allowed = ?, " +
                    "start = ?, end = ?, creation_date = ?, last_modified = ?" +
                    "WHERE id = ?";

            stmt = conn.prepareStatement(sqlStr);
            stmt.setString(1, event.getTitle());
            stmt.setString(2, event.getShortDesc());
            stmt.setString(3, event.getFullDesc());
            stmt.setString(4, event.getLocation());
            stmt.setFloat(5, event.getLat());
            stmt.setFloat(6, event.getLng());
            stmt.setString(7, event.getFilePath());
            stmt.setString(8, event.getImagePath());

            if (event.getCategory() != null) {
                stmt.setInt(9, event.getCategory().getId());
            } else {
                stmt.setObject(9, null);
            }

            stmt.setBoolean(10, event.isPublicAccessed());
            stmt.setBoolean(11, event.isGuestsAllowed());

            if (event.getDateRange() != null) {
                stmt.setTimestamp(12, new Timestamp(event.getDateRange().getStart().getTime()));
                stmt.setTimestamp(13, new Timestamp(event.getDateRange().getEnd().getTime()));
            } else {
                stmt.setObject(12, null);
                stmt.setObject(13, null);
            }

            if (event.getCreationDate() != null) {
                stmt.setTimestamp(14, new Timestamp(event.getCreationDate().getTime()));
            } else {
                stmt.setObject(14, null);
            }

            if (event.getLastModifiedDate() != null) {
                stmt.setTimestamp(14, new Timestamp(event.getLastModifiedDate().getTime()));
            } else {
                stmt.setObject(14, null);
            }
            stmt.setInt(15, event.getId());

            //execute query
            affectedRows = stmt.executeUpdate();

        } catch (IOException | SQLException e) {
                logger.error("Exception ", e);
        } finally {
            closeResources(null, stmt, conn);
        }
        return affectedRows != 0;
    }

    //DELETE
    @Override
    public boolean deleteEvent(int eventId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;

        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "DELETE FROM event WHERE id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, eventId);
            affectedRows = stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Exception ", e);
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;
    }

    @Override
    public boolean deleteAllEvents() {
        return deleteAllRecords("event");
    }

    //helper methods
    private int insertEventMainInfo(Event event, Connection conn) {
        int eventId = 0;
        PreparedStatement stmtInsertEvent = null;
        PreparedStatement stmtGetInsertedId = null;
        try {
            String insertEvent = "INSERT INTO event "
                    + "(title, short_desc, full_desc, location, lat, lng, file_path, image_path, "
                    + "category_id, public_accessed, guests_allowed, start, end, creation_date) VALUES "
                    + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
            stmtInsertEvent = conn.prepareStatement(insertEvent);
            stmtInsertEvent.setString(1, event.getTitle());
            stmtInsertEvent.setString(2, event.getShortDesc());
            stmtInsertEvent.setString(3, event.getFullDesc());
            stmtInsertEvent.setString(4, event.getLocation());
            stmtInsertEvent.setFloat(5, event.getLat());
            stmtInsertEvent.setFloat(6, event.getLng());
            stmtInsertEvent.setString(7, event.getFilePath());
            stmtInsertEvent.setString(8, event.getImagePath());

            if (event.getCategory() != null) {
                stmtInsertEvent.setInt(9, event.getCategory().getId());
            } else {
                stmtInsertEvent.setObject(9, null);
            }

            stmtInsertEvent.setBoolean(10, event.isPublicAccessed());
            stmtInsertEvent.setBoolean(11, event.isGuestsAllowed());

            if (event.getDateRange() != null) {
                stmtInsertEvent.setTimestamp(12, new Timestamp(event.getDateRange().getStart().getTime()));
                stmtInsertEvent.setTimestamp(13, new Timestamp(event.getDateRange().getEnd().getTime()));
            } else {
                stmtInsertEvent.setObject(12, null);
                stmtInsertEvent.setObject(13, null);
            }

            if (event.getCreationDate() != null) {
                stmtInsertEvent.setTimestamp(14, new Timestamp(event.getCreationDate().getTime()));
            } else {
                stmtInsertEvent.setObject(14, null);
            }

            stmtInsertEvent.executeUpdate();

            //getting inserted event id
            stmtGetInsertedId = conn.prepareStatement("SELECT LAST_INSERT_ID() as id");
            ResultSet rs = stmtGetInsertedId.executeQuery();
            if (rs.next()) {
                eventId = rs.getInt("id");
            }
        } catch (SQLException e) {
            logger.error("Exception ", e);
        } finally {

        }
        return eventId;
    }

    private List<Event> createEventsListFromRS(ResultSet rs) throws SQLException {
        List<Event> eventsList = new ArrayList<Event>();

        while (rs.next()) {

            EventCategory category = new EventCategory();
            category.setId(rs.getInt("event_category.id"))
                    .setTitle(rs.getString("event_category.title"))
                    .setDescription(rs.getString("event_category.description"))
                    .setCreationDate(rs.getTimestamp("event_category.creation_date"));

            Event event = new Event();
            event.setId(rs.getInt("event.id"))
                    .setTitle(rs.getString("event.title"))
                    .setShortDesc(rs.getString("short_desc"))
                    .setFullDesc(rs.getString("full_desc"))
                    .setLocation(rs.getString("location"))
                    .setLat(rs.getFloat("lat"))
                    .setLng(rs.getFloat("lng"))
                    .setFilePath(rs.getString("file_path"))
                    .setImagePath(rs.getString("image_path"))
                    .setPublicAccessed(rs.getBoolean("public_accessed"))
                    .setGuestsAllowed(rs.getBoolean("guests_allowed"))
                    .setCreationDate(rs.getTimestamp("event.creation_date"))
                    .setLastModifiedDate(rs.getTimestamp("event.last_modified"))
                    .setDateRange(new DateRange(rs.getTimestamp("start"), rs.getTimestamp("end")))
                    .setCategory(category);

            eventsList.add(event);
        }
        return eventsList;
    }


}
