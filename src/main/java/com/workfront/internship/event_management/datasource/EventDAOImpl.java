package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.*;
import com.workfront.internship.event_management.model.datehelpers.DateRange;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hermine on 7/1/16.
 */
public class EventDAOImpl extends GenericDAO implements EventDAO {


    //CREATE
    public boolean insertEvent(Event event, int organizerId) {
        Connection conn = null;
        int affectedRows = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            conn.setAutoCommit(false);

            //insert event main info and get inserted event id
            int eventId = insertEventMainInfo(event, conn);
            event.setId(eventId);

            //insert event invitations
            if (event.getInvitations() != null) {
                for (EventInvitation invitation : event.getInvitations()) {
                    invitation.setEventId(eventId);
                }
                EventInvitationDAO invitationDAO = new EventInvitationDAOImpl();
                invitationDAO.insertInvitations(event.getInvitations(), conn);
            }

            //insert event recurrence info
            if (event.getRecurrences() != null) {
                for (EventRecurrence recurrence : event.getRecurrences()) {
                    recurrence.setEventId(eventId);
                }
                EventRecurrenceDAO recurrenceDAO = new EventRecurrenceDAOImpl();
                recurrenceDAO.insertEventRecurrences(event.getRecurrences(), conn);
            }
            conn.commit();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
            try {

                conn.rollback();
            } catch (SQLException re) {
                System.out.println("SQLException " + e.getMessage());

            }
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(null, null, conn);
        }
        return affectedRows != 0;
    }

    //READ
    public List<Event> getAllEvents() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Event> eventsList = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT * " +
                    "FROM event " +
                    "LEFT JOIN event_category " +
                    "ON event.category_id = event_category.id ";
            stmt = conn.prepareStatement(sqlStr);
            rs = stmt.executeQuery();
            eventsList = createEventsListFromRS(rs);
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return eventsList;
    }

    public Event getEventById(int eventId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Event event = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            //get event main info
            String sqlStr = "SELECT * " +
                    "FROM (event LEFT JOIN event_category " +
                    "ON event.category_id = event_category.id) " +
                    "WHERE event.id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, eventId);
            rs = stmt.executeQuery();
            event = createEventFromRS(rs);

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

        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return event;
    }

    public List<Event> getEventsByCategory(int categoryId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Event> eventsList = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT * " +
                    "FROM (event LEFT JOIN event_category " +
                    "ON event.category_id = event_category.id) " +
                    "WHERE event.category_id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, categoryId);
            rs = stmt.executeQuery();
            eventsList = createEventsListFromRS(rs);
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return eventsList;
    }

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
            rs = stmt.executeQuery();
            eventsList = createEventsListFromRS(rs);
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return eventsList;
    }

    public List<Event> getParticipatedEventsByUserId(int userId) {
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
                    "WHERE user_id = ? AND participated = true ";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            eventsList = createEventsListFromRS(rs);
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return eventsList;
    }

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
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return eventsList;
    }

    //UPDATE
    //????????
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
            affectedRows = stmt.executeUpdate();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(null, stmt, conn);
        }
        return affectedRows != 0;
    }

    //DELETE
    public boolean deleteEvent(int eventId) {
        return deleteEntryById("event", eventId);
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
            e.printStackTrace();
        }
        return eventId;
    }

    private Event createEventFromRS(ResultSet rs) throws SQLException {
        Event event = null;
        EventCategory category = null;
        while (rs.next()) {
            if (event == null) {
                category = new EventCategory();
                category.setId(rs.getInt("event_category.id"))
                        .setTitle(rs.getString("event_category.title"))
                        .setDescription(rs.getString("event_category.description"))
                        .setCreationDate(rs.getTimestamp("event_category.creation_date"));
                event = new Event();
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
                        .setLastModifiedDate(rs.getTimestamp("last_modified"))
                        .setDateRange(new DateRange(rs.getTimestamp("start"), rs.getTimestamp("end")));
                event.setCategory(category);
            }
            EventRecurrence eventRecurrence = new EventRecurrence();
            eventRecurrence.setEventId(rs.getInt("event_id"));
        }
        return event;
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
