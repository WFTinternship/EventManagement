package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventRecurrence;
import com.workfront.internship.event_management.model.RecurrenceType;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hermine Turshujyan 7/11/16.
 */
public class EventRecurrenceDAOImpl extends GenericDAO implements EventRecurrenceDAO {

    @Override
    public int addEventRecurrence(EventRecurrence recurrence) {

        Connection conn = null;
        int id = 0;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            id = addEventRecurrence(recurrence, conn);

        } catch (SQLException | IOException e) {
            logger.error("Exception ", e);
            throw new RuntimeException(e);
        } finally {
            closeResources(conn);
        }
        return id;
    }

    @Override
    public EventRecurrence getEventRecurrenceById(int id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        EventRecurrence eventRecurrence = null;
        String sqlStr = "SELECT * FROM  event_recurrence LEFT JOIN recurrence_type ON " +
                "event_recurrence.recurrence_type_id = recurrence_type.id WHERE event_recurrence.id = ?";

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, id);

            //execute query
            rs = stmt.executeQuery();

            //get results
            List<EventRecurrence> eventRecurrenceList = createEventRecurrencesFromRS(rs);
            if (!eventRecurrenceList.isEmpty()) {
                eventRecurrence = eventRecurrenceList.get(0);
            }

        } catch (SQLException | IOException e) {
            logger.error("Exception ", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return eventRecurrence;
    }

    @Override
    public int addEventRecurrence(EventRecurrence recurrence, Connection conn) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        int id = 0;
        String query = "INSERT INTO event_recurrence "
                + "(event_id, recurrence_type_id, repeat_on, repeat_interval, repeat_end) "
                + "VALUES (?, ?, ?, ?, ?)";

        try {
            //get connection
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            //create and initialize statement
            stmt.setInt(1, recurrence.getEventId());
            stmt.setInt(2, recurrence.getRecurrenceType().getId());
            stmt.setString(3, recurrence.getRepeatOn());
            stmt.setInt(4, recurrence.getRepeatInterval());
            if(recurrence.getRepeatEndDate()!= null) {
                stmt.setTimestamp(5, new Timestamp(recurrence.getRepeatEndDate().getTime()));
            } else {
                stmt.setTimestamp(5, null);
            }

            //execute query
            stmt.executeUpdate();

            id = getInsertedId(stmt);

        } catch (SQLException e) {
            logger.error("Exception ", e);
            throw new RuntimeException(e);
        } finally {
            closeResources(rs, stmt);
        }
        return id;
    }

    @Override
    public List<EventRecurrence> getEventRecurrencesByEventId(int eventId) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<EventRecurrence> recurrencesList = null;
        String sqlStr = "SELECT * FROM  event_recurrence LEFT JOIN recurrence_type ON " +
                "event_recurrence.recurrence_type_id = recurrence_type.id WHERE event_id = ?";

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, eventId);

            //execute query
            rs = stmt.executeQuery();

            //get results
            recurrencesList = createEventRecurrencesFromRS(rs);

        } catch (SQLException | IOException e) {
            logger.error("Exception ", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return recurrencesList;
    }

    @Override
    public List<EventRecurrence> getAllEventRecurrences() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<EventRecurrence> recurrencesList = null;
        String sqlStr = "SELECT * FROM  event_recurrence LEFT JOIN recurrence_type ON " +
                "event_recurrence.recurrence_type_id = recurrence_type.id";

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create statement
            stmt = conn.prepareStatement(sqlStr);

            //execute query
            rs = stmt.executeQuery();

            //get results
            recurrencesList = createEventRecurrencesFromRS(rs);

        } catch (SQLException | IOException e) {
            logger.error("Exception ", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return recurrencesList;
    }

    @Override
    public boolean updateEventRecurrence(EventRecurrence recurrence) {

        Connection conn = null;
        PreparedStatement stmt = null;

        int affectedRows = 0;
        String query = "UPDATE event_recurrence SET event_id = ?, recurrence_type_id = ?, " +
                "repeat_on = ?, repeat_interval = ?, repeat_end = ? WHERE id = ?";

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, recurrence.getEventId());
            if (recurrence.getRecurrenceType() != null) {
                stmt.setInt(2, recurrence.getRecurrenceType().getId());
            } else {
                stmt.setInt(2, 0);

            }
            stmt.setString(3, recurrence.getRepeatOn());
            stmt.setInt(4, recurrence.getRepeatInterval());

            if(recurrence.getRepeatEndDate()!= null) {
                stmt.setTimestamp(5, new Timestamp(recurrence.getRepeatEndDate().getTime()));
            } else {
                stmt.setTimestamp(5, null);
            }

            stmt.setInt(6, recurrence.getId());

            //execute query
            affectedRows = stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Exception ", e);
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;    }

    @Override
    public boolean deleteEventRecurrece(int id) {
        return deleteRecordById("event_recurrence", id);
    }

    @Override
    public boolean deleteAllEventRecurrences() {
        return deleteAllRecords("event_recurrence");
    }

    //helper methods

    private List<EventRecurrence> createEventRecurrencesFromRS(ResultSet rs) throws SQLException {

        List<EventRecurrence> recurrencesList = new ArrayList<EventRecurrence>();

        while (rs.next()) {

            RecurrenceType recurrenceType = new RecurrenceType();
            recurrenceType.setTitle(rs.getString("recurrence_type.title"))
                        .setIntervalUnit(rs.getString("recurrence_type.interval_unit"))
                    .setId(rs.getInt("recurrence_type.id"));

            EventRecurrence recurrence = new EventRecurrence();
            recurrence.setId(rs.getInt("event_recurrence.id"))
                    .setEventId(rs.getInt("event_recurrence.event_id"))
                    .setRepeatInterval(rs.getInt("event_recurrence.repeat_interval"))
                    .setRepeatOn(rs.getString("event_recurrence.repeat_on"))
                    .setRepeatEndDate(rs.getTimestamp("event_recurrence.repeat_end"))
                    .setRecurrenceType(recurrenceType);

            recurrencesList.add(recurrence);
        }
        return recurrencesList;
    }
}
