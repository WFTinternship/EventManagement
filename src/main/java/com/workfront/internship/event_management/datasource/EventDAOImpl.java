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

    public List<Event> getEventsByOrganizerId(int organizerId) { //????????
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Event> eventsList = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT event.* " +
                    "FROM event_participant LEFT JOIN event " +
                    "ON event_participant.event_id = event.id" +
                    "WHERE event_participant.role = Organizer && event_participant.user_id = ?";
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

    public List<Event> getEventsByDateRange(DateRange range) { //???????
        return null;
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

    public boolean updateEvent(Event event) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
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
            closeResources(rs, stmt, conn);
        }
        return affectedRows != 0;
    }

    public boolean updateParticipantsList() {
        return false;
    }

    public boolean insertEvent(Event event) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int affectedRows = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "INSERT INTO event "
                    + "(title, short_desc, full_desc, location, lat, lng, file_path, image_path, "
                    + "category_id, public_access, guests_allowed) VALUES "
                    + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
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
            affectedRows = preparedStatement.executeUpdate();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return affectedRows != 0;
    }

    public boolean deleteEvent(int eventId) {
        return deleteEntryById("event", eventId);
    }

    public List<EventParticipant> getParticipantsByEventId(int eventId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EventParticipant> participantsList = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT * FROM event_participant LEFT JOIN user " +
                    "ON event_participant.user_id = user.id " +
                    "WHERE event_participant.event_id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, eventId);
            rs = stmt.executeQuery();
            participantsList = createParticipantsListFromRS(rs);
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return participantsList;
    }


    public List<EventMedia> getMediaByEventId(int eventId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EventMedia> mediaList = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT * FROM event_media " +
                    "WHERE event_id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, eventId);
            rs = stmt.executeQuery();
            mediaList = createMediaFromRS(rs);
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

    private List<EventMedia> createMediaFromRS(ResultSet rs) throws SQLException {
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

    private List<EventParticipant> createParticipantsListFromRS(ResultSet rs) throws SQLException {
        List<EventParticipant> participantsList = new ArrayList<EventParticipant>();
        while (rs.next()) {
            EventParticipant participant = new EventParticipant();
            participant.setId(rs.getInt("id"))
                    .setFirstName(rs.getString("first_name"))
                    .setLastName(rs.getString("last_name"))
                    .setUsername(rs.getString("username"))
                    .setEmail(rs.getString("email"))
                    .setAvatarPath(rs.getString("avatar_path"))
                    .setPhoneNumber(rs.getString("phone_number"))
                    .setVerified(rs.getBoolean("verified"))
                    .setRegistrationDate(rs.getTimestamp("registration_date"));
            participant.setPeopleCount(rs.getInt("people_count"))
                    .setResponse(rs.getString("response"))
                    .setRole(rs.getString("role"))
                    .setCheckedIn(rs.getBoolean("checked_in"));
            participantsList.add(participant);
        }
        return participantsList;
    }


}
