package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.*;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by hermine on 7/1/16.
 */
public class EventDAOImpl extends GenericDAO implements EventDAO {

    //CREATE
    //?????????????
    public boolean createEvent(Event event, int creatorId) {
        Connection conn = null;
        PreparedStatement stmtInsertEvent = null;
        PreparedStatement stmtInsertOrganizer = null;
        PreparedStatement stmtInsertDates = null;
        PreparedStatement stmtInsertInvitations = null;
        int affectedRows = 0;
        try {

            conn = DataSourceManager.getInstance().getConnection();
            conn.setAutoCommit(false);

            String insertEvent = "INSERT INTO event "
                    + "(title, short_desc, full_desc, location, lat, lng, file_path, image_path, "
                    + "category_id, public_access, guests_allowed) VALUES "
                    + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
            stmtInsertEvent = conn.prepareStatement(insertEvent);
            stmtInsertEvent.setString(1, event.getTitle());
            stmtInsertEvent.setString(2, event.getShortDesc());
            stmtInsertEvent.setString(3, event.getFullDesc());
            stmtInsertEvent.setString(4, event.getLocation());
            stmtInsertEvent.setFloat(5, event.getLat());
            stmtInsertEvent.setFloat(6, event.getLng());
            stmtInsertEvent.setString(7, event.getFilePath());
            stmtInsertEvent.setString(8, event.getImagePath());
            stmtInsertEvent.setInt(9, event.getCategory().getId());
            stmtInsertEvent.setBoolean(10, event.isPublicAccess());
            stmtInsertEvent.setBoolean(11, event.isGuestsAllowed());
            stmtInsertEvent.executeUpdate();

            String insertOrganizer = "INSERT INTO event_invitation "
                    + "(event_id, user_id, user_role) VALUES "
                    + "(?, ?, ? )";
            stmtInsertOrganizer = conn.prepareStatement(insertEvent);
            stmtInsertOrganizer.setInt(1, event.getId());
            stmtInsertOrganizer.setInt(2, creatorId);
            stmtInsertOrganizer.setString(3, "Organizer");
            stmtInsertEvent.executeUpdate();

            if (event.getDates() != null) {
                String insertDates = "INSERT INTO event_date "
                        + "(event_id, start, end) VALUES "
                        + "(?, ?, ? )";
                stmtInsertDates = conn.prepareStatement(insertDates);
                List<DateRange> dates = event.getDates();
                for (DateRange range : dates) {
                    stmtInsertDates.setInt(1, event.getId());
                    stmtInsertDates.setTimestamp(2, new Timestamp(range.getStart().getTime()));
                    stmtInsertDates.setTimestamp(3, new Timestamp(range.getEnd().getTime()));
                    stmtInsertDates.executeUpdate();
                }
            }

            if (event.getInvitations() != null) {
                String insertInvitations = "INSERT INTO event_invitation "
                        + "(event_id, user_id, user_role) VALUES "
                        + "(?, ?, ? )";
                for (EventInvitation invitation : event.getInvitations()) {
                    stmtInsertInvitations = conn.prepareStatement(insertInvitations);
                    stmtInsertInvitations.setInt(1, invitation.getEventId());
                    stmtInsertInvitations.setInt(2, invitation.getUser().getId());
                    stmtInsertInvitations.setString(3, invitation.getUserRole());
                    stmtInsertInvitations.executeUpdate();
                }
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

            if (stmtInsertEvent != null) {
                try {
                    stmtInsertEvent.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmtInsertOrganizer != null) {
                try {
                    stmtInsertOrganizer.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmtInsertDates != null) {
                try {
                    stmtInsertDates.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmtInsertInvitations != null) {
                try {
                    stmtInsertInvitations.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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
                    "FROM (event LEFT JOIN event_category " +
                    "ON event.category_id = event_category.id) " +
                    "LEFT JOIN event_date ON event.id = event_date.event_id ";
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
            String sqlStr = "SELECT * " +
                    "FROM (event LEFT JOIN event_category " +
                    "ON event.category_id = event_category.id) " +
                    "LEFT JOIN event_date ON event.id = event_date.event_id " +
                    "WHERE event.id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, eventId);
            rs = stmt.executeQuery();
            event = createEventFromRS(rs);
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
                    "LEFT JOIN event_date ON event.id = event_date.event_id " +
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

    public List<Event> getEventsByOrganizerId(int organizerId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Event> eventsList = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT event_participant.user_id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, organizerId);
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

    public List<Event> getInvitedEventsByUserId(int uderId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Event> eventsList = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT * FROM ((event_participant " +
                    "LEFT JOIN event ON event_participant.event_id = event.id) " +
                    "LEFT JOIN event_category ON event.category_id = event_category.id) " +
                    "LEFT JOIN event_date ON event.id = event_date.event_id " +
                    "WHERE event_participant.user_id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, uderId);
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

    public List<Event> getParticipatedEventsByUserId(int uderId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Event> eventsList = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT * FROM ((event_participant " +
                    "LEFT JOIN event ON event_participant.event_id = event.id) " +
                    "LEFT JOIN event_category ON event.category_id = event_category.id) " +
                    "LEFT JOIN event_date ON event.id = event_date.event_id " +
                    "WHERE event_participant.user_id = ? && checked_in = 1";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, uderId);
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

    public List<Event> getAcceptedEventsByUserId(int uderId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Event> eventsList = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT * FROM ((event_participant " +
                    "LEFT JOIN event ON event_participant.event_id = event.id) " +
                    "LEFT JOIN event_category ON event.category_id = event_category.id) " +
                    "LEFT JOIN event_date ON event.id = event_date.event_id " +
                    "WHERE event_participant.user_id = ? && response = \"Yes\"";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, uderId);
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
            String sqlStr = "UPDATE event SET title = ?, short_desc = ?, full_desc = ?, location = ?, " +
                    "lat = ?, lng = ?, file_path = ?, image_path = ?, " +
                    "category_id = ?, public_access = ?, guests_allowed = ?, last_modified = now() " +
                    "WHERE id = ?";
            Timestamp date = new Timestamp(new Date().getTime());
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setString(1, event.getTitle());
            preparedStatement.setString(2, event.getShortDesc());
            preparedStatement.setString(3, event.getFullDesc());
            preparedStatement.setString(4, event.getLocation());
            preparedStatement.setFloat(5, event.getLat());
            preparedStatement.setFloat(6, event.getLng());
            preparedStatement.setString(7, event.getFilePath());
            preparedStatement.setString(8, event.getImagePath());
            preparedStatement.setInt(9, event.getCategory().getId());
            preparedStatement.setBoolean(10, event.isPublicAccess());
            preparedStatement.setBoolean(11, event.isGuestsAllowed());
            preparedStatement.setInt(12, event.getId());
            affectedRows = preparedStatement.executeUpdate();
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
    private Event createEventFromRS(ResultSet rs) throws SQLException {
        List<DateRange> dates = new ArrayList<DateRange>();
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
                        .setPublicAccess(rs.getBoolean("public_access"))
                        .setGuestsAllowed(rs.getBoolean("guests_allowed"))
                        .setCreationDate(rs.getTimestamp("event.creation_date"))
                        .setLastModified(rs.getTimestamp("last_modified"));
                event.setCategory(category);
            }
            DateRange dateRange = new DateRange();
            dateRange.setStart(rs.getTimestamp("start"));
            dateRange.setEnd(rs.getTimestamp("end"));
            dates.add(dateRange);
            event.setDates(dates);
        }
        return event;
    }

    private List<Event> createEventsListFromRS(ResultSet rs) throws SQLException {
        Map<Integer, Event> eventsMap = new HashMap<Integer, Event>();
        while (rs.next()) {
            int eventId = rs.getInt("event.id");
            if (!eventsMap.containsKey(eventId)) {
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
                        .setPublicAccess(rs.getBoolean("public_access"))
                        .setGuestsAllowed(rs.getBoolean("guests_allowed"))
                        .setCreationDate(rs.getTimestamp("event.creation_date"))
                        .setLastModified(rs.getTimestamp("last_modified"));
                event.setCategory(category);
                DateRange dateRange = new DateRange(rs.getTimestamp("start"), rs.getTimestamp("end"));
                List<DateRange> dates = new ArrayList<DateRange>();
                dates.add(dateRange);
                event.setDates(dates);
                eventsMap.put(eventId, event);
            } else {
                Event event = eventsMap.get(eventId);
                DateRange dateRange = new DateRange(rs.getTimestamp("start"), rs.getTimestamp("end"));
                event.getDates().add(dateRange);
            }
        }
        return new ArrayList<Event>(eventsMap.values());
    }

}
