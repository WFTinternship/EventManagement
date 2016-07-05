package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hermine on 7/1/16.
 */
public class EventDAOImpl extends GenericDAO implements EventDAO {
    public Map<Integer, Event> getAllEvents() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Map<Integer, Event> eventsList = null;
        try {
            DataSourceManager dsManager = DataSourceManager.getInstance();
            conn = dsManager.getConnection();
            String sqlStr = "";
            stmt = conn.prepareStatement(sqlStr);
            rs = stmt.executeQuery();
            Event event = null;
            while (rs.next()) {
                Integer eventId = rs.getInt("event_id");
                if (!eventsList.containsKey(eventId)) {
                    event = new Event();

                }

                eventsList.put(eventId, event);
            }
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return eventsList;
    }

    public List<Event> getEventsByCategory(int categoryId) {
        return null;
    }

    public Event getEventById(int eventId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Event event = null;
        try {
            DataSourceManager dsManager = DataSourceManager.getInstance();
            conn = dsManager.getConnection();
            String sqlStr = "SELECT " +
                    "event.*, event_category.title AS cat_title, event_category.description AS cat_desc, " +
                    "event_category.creation_date AS cat_creation_date " +
                    "event_date.start, event_date.end " +
                    "FROM (event LEFT JOIN event_category " +
                    "ON event.category_id = event_category.id) " +
                    "LEFT JOIN event_date ON event.id = event_date.event_id " +
                    "WHERE event.id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, eventId);
            rs = stmt.executeQuery();
            List<DateRange> dates = new ArrayList<DateRange>();
            DateRange dateRange = new DateRange();
            while (rs.next()) {
                EventCategory category = new EventCategory();
                category.setId(rs.getInt("category_id"))
                        .setTitle(rs.getString("cat_title"))
                        .setDescription(rs.getString("cat_desc"))
                        .setCreationDate(rs.getTimestamp("cat_creation_date"));
                event = new Event();
                event.setId(rs.getInt("id"))
                        .setTitle(rs.getString("title"))
                        .setShortDesc(rs.getString("short_desc"))
                        .setFullDesc(rs.getString("full_desc"))
                        .setCreationDate(rs.getTimestamp("creation_date"))
                        .setLastModified(rs.getTimestamp("last_modified"))
                        .setLocation(rs.getString("location"))
                        .setLat(rs.getFloat("lat"))
                        .setLng(rs.getFloat("lng"))
                        .setFilePath(rs.getString("file_path"))
                        .setImagePath(rs.getString("image_path"))
                        .setCategory(category)
                        .setAccess(rs.getString("access"));

                dateRange.setStart(rs.getTimestamp("start"));
                dateRange.setEnd(rs.getTimestamp("end"));
                dates.add(dateRange);
            }
            event.setDates(dates);
            event.setParticipants(getParticipantsByEventId(eventId));
            event.setMedia(getMediaByEventId(eventId));
            // organizerId ???

        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return event;
    }

    public void updateEvent(Event event) {

    }

    public void insertEvent(Event event) {

    }

    public void deleteEvent(int eventId) {

    }

    //////////
    public List<EventMedia> getMediaByEventId(int eventId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EventMedia> mediaList = null;
        try {
            DataSourceManager dsManager = DataSourceManager.getInstance();
            conn = dsManager.getConnection();
            String sqlStr = "SELECT * FROM event_media " +
                    "WHERE event_id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, eventId);
            rs = stmt.executeQuery();
            mediaList = new ArrayList<EventMedia>();
            EventMedia media = new EventMedia();
            while (rs.next()) {
                media.setId(rs.getInt("id"))
                        .setType(rs.getString("type"))
                        .setPath(rs.getString("path"))
                        .setDescription(rs.getString("description"))
                        .setEventId(rs.getInt("event_id"))
                        .setOwnerId(rs.getInt("owner_id"))
                        .setUploadDate(rs.getTimestamp("upload_date"));
                mediaList.add(media);
            }

        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return mediaList;
    }

    public List<Participant> getParticipantsByEventId(int eventId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Participant> participantsList = null;
        try {
            DataSourceManager dsManager = DataSourceManager.getInstance();
            conn = dsManager.getConnection();
            String sqlStr = "SELECT * FROM event_participation LEFT JOIN user " +
                    "ON event_participation.user_id = user.id " +
                    "WHERE event_participation.event_id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, eventId);
            rs = stmt.executeQuery();
            participantsList = new ArrayList<Participant>();
            Participant participant = new Participant();
            while (rs.next()) {
                participant.setId(rs.getInt("id"))
                        .setFirstName(rs.getString("first_name"))
                        .setLastName(rs.getString("last_name"))
                        .setUsername(rs.getString("username"))
                        .setEmail(rs.getString("email"))
                        .setRegistrationDate(rs.getTimestamp("registration_date"))
                        .setAvatarPath(rs.getString("avatar_path"))
                        .setPhoneNumber(rs.getString("phone_number"))
                        .setVerified(rs.getBoolean("is_verified"));
                participant.setParticipantsCount(rs.getInt("participants_count"))
                        .setResponse(rs.getString("response"))
                        .setRole(rs.getString("role"))
                        .setCheckedIn(rs.getBoolean("checked_in"));
                participantsList.add(participant);
            }
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return participantsList;
    }
}
