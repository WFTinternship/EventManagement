package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.*;
import com.workfront.internship.event_management.model.DateRange;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public class EventDAOImpl extends GenericDAO implements EventDAO {

    @Override
    public int addEvent(Event event) {

        Connection conn = null;
        int eventId = 0;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //start transaction
            conn.setAutoCommit(false);

            //insert event main info and get generated event id
            eventId = addEventMainInfo(event, conn);
            event.setId(eventId);

            //insert event invitations (included organizer)
            if (event.getInvitations() != null && !event.getInvitations().isEmpty()) {
                InvitationDAO invitationDAO = new InvitationDAOImpl();
                for (Invitation invitation : event.getInvitations()) {
                    invitation.setEventId(eventId);
                    invitationDAO.addInvitation(invitation, conn);
                }
            }

            //insert event recurrence info
            if (event.getEventRecurrences() != null && !event.getInvitations().isEmpty()) {
                for (EventRecurrence recurrence : event.getEventRecurrences()) {
                    recurrence.setEventId(eventId);
                    EventRecurrenceDAO recurrenceDAO = new EventRecurrenceDAOImpl();
                    recurrenceDAO.addEventRecurrence(recurrence, conn);
                }
            }

            //commit transaction
            conn.commit();

        } catch (IOException | SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LOGGER.error("Commit/rollback exception...", e);
            }
            LOGGER.error("Exception...", e);
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

        String query = "SELECT * FROM event " +
                "LEFT JOIN event_category ON event.category_id = event_category.id ";

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create statement
            stmt = conn.prepareStatement(query);

            //execute query
            rs = stmt.executeQuery();

            //get results
            eventsList = createEventListFromRS(rs);

        } catch (SQLException | IOException e) {
            LOGGER.error("Exception...", e);
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
        String query = "SELECT * FROM (event LEFT JOIN event_category " +
                "ON event.category_id = event_category.id) WHERE event.id = ?";

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, eventId);

            //execute query
            rs = stmt.executeQuery();

            //get results from event table
            List<Event> eventList = createEventListFromRS(rs);
            if (eventList != null && !eventList.isEmpty()) {
                event = eventList.get(0);
            }
            //get invitations list
            InvitationDAO invitationDAO = new InvitationDAOImpl();
            List<Invitation> invitations = invitationDAO.getInvitationsByEventId(eventId);
            if (invitations != null && !invitations.isEmpty()) {
                event.setInvitations(invitations);
            }

            //get event recurrences
            EventRecurrenceDAO recurrenceDAO = new EventRecurrenceDAOImpl();
            List<EventRecurrence> recurrences = recurrenceDAO.getEventRecurrencesByEventId(eventId);
            if (recurrences != null && !recurrences.isEmpty()) {
                event.setEventRecurrences(recurrences);
            }

            //get media list
            MediaDAO mediaDAO = new MediaDAOImpl();
            List<Media> media = mediaDAO.getMediaByEventId(eventId);
            if (media != null && !media.isEmpty()) {
                event.setMedia(media);
            }

        } catch (SQLException | IOException e) {
            LOGGER.error("Exception ", e);
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
        String query = "SELECT * FROM (event LEFT JOIN event_category " +
                "ON event.category_id = event_category.id) " +
                "WHERE event.category_id = ?";
        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, categoryId);

            //execute query
            rs = stmt.executeQuery();

            //get results
            eventsList = createEventListFromRS(rs);

        } catch (SQLException | IOException e) {
            LOGGER.error("Exception ", e);
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
            eventsList = createEventListFromRS(rs);

        } catch (SQLException | IOException e) {
            LOGGER.error("Exception ", e);
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

            eventsList = createEventListFromRS(rs);
        } catch (SQLException | IOException e) {
            LOGGER.error("Exception ", e);
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

            eventsList = createEventListFromRS(rs);
        } catch (SQLException | IOException e) {
            LOGGER.error("Exception ", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return eventsList;
    }

    @Override
    public boolean updateEvent(Event event) {

        Connection conn = null;
        PreparedStatement stmt = null;

        int affectedRows = 0;
        String query = "UPDATE event SET " +
                "title = ?, short_desc = ?, full_desc = ?, location = ?, lat = ?, lng = ?, " +
                "file_path = ?, image_path = ?, category_id = ?, public_accessed = ?, guests_allowed = ?, " +
                "start = ?, end = ?, creation_date = ?, last_modified = ?" +
                "WHERE id = ?";

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

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
            LOGGER.error("Exception ", e);
        } finally {
            closeResources(null, stmt, conn);
        }
        return affectedRows != 0;
    }

    @Override
    public boolean deleteEvent(int eventId) {
        return deleteRecordById("event", eventId);
    }

    @Override
    public boolean deleteAllEvents() {
        return deleteAllRecords("event");
    }

    //helper methods
    private int addEventMainInfo(Event event, Connection conn) {

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

            //execute query
            stmt.executeUpdate();

            //get inserted id
            id = getInsertedId(stmt);

        } catch (SQLException e) {
            LOGGER.error("Exception ", e);
        } finally {
            // closeResources(stmt);
        }
        return id;
    }

    private List<Event> createEventListFromRS(ResultSet rs) throws SQLException {
        List<Event> eventsList = new ArrayList<Event>();

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
                    .setDateRange(new DateRange(rs.getTimestamp("start"), rs.getTimestamp("end")))
                    .setCategory(category);

            eventsList.add(event);
        }
        return eventsList;
    }


}
