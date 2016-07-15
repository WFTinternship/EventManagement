package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventRecurrence;
import com.workfront.internship.event_management.model.RecurrenceType;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hermine on 7/11/16.
 */
public class EventRecurrenceDAOImpl extends GenericDAO implements EventRecurrenceDAO {

    //Create
    @Override
    public int addEventRecurrence(EventRecurrence recurrence) {
        Connection conn = null;
        int id = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            id = addEventRecurrence(recurrence, conn);
        } catch (SQLException | IOException e) {
            logger.error("Exception ", e);
        } finally {
            closeResources(conn);
        }
        return id;
    }

    @Override
    public int addEventRecurrence(EventRecurrence recurrence, Connection conn) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int id = 0;
        String sqlStr = "INSERT INTO event_recurrence "
                + "(event_id, recurrence_type_id, repeat_on, repeat_interval, repeat_end) "
                + "VALUES (?, ?, ?, ?, ?)";
        try {
            stmt = conn.prepareStatement(sqlStr, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, recurrence.getEventId());
            stmt.setInt(2, recurrence.getRecurrenceType().getId());
            stmt.setString(3, recurrence.getRepeatOn());
            stmt.setInt(4, recurrence.getRepeatInterval());
            if(recurrence.getRepeatEndDate()!= null) {
                stmt.setTimestamp(5, new Timestamp(recurrence.getRepeatEndDate().getTime()));
            } else {
                stmt.setTimestamp(5, null);
            }
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("Exception ", e);
        } finally {
            closeResources(rs, stmt);
        }
        return id;
    }

    @Override
    public boolean addEventRecurrences(List<EventRecurrence> recurrences) {
        Connection conn = null;
        boolean success = false;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            success = addEventRecurrences(recurrences, conn);
        } catch (SQLException | IOException e) {
            logger.error("Exception ", e);
        } finally {
            closeResources(conn);
        }
        return success;
    }

    @Override
    public boolean addEventRecurrences(List<EventRecurrence> recurrences, Connection conn) {
        PreparedStatement stmt = null;
        int affectedRows = 0;
        String sqlStr = "INSERT INTO event_recurrence "
                    + "(event_id, recurrence_type_id, repeat_on, repeat_interval, repeat_end) "
                    + "VALUES (?, ?, ?, ?, ?)";
        try {
            stmt = conn.prepareStatement(sqlStr);
            for (EventRecurrence recurrence : recurrences) {
                stmt.setInt(1, recurrence.getEventId());
                stmt.setInt(2, recurrence.getRecurrenceType().getId());
                stmt.setString(3, recurrence.getRepeatOn());
                stmt.setInt(4, recurrence.getRepeatInterval());
                if (recurrence.getRepeatEndDate() != null) {
                    stmt.setTimestamp(5, new Timestamp(recurrence.getRepeatEndDate().getTime()));
                } else {
                    stmt.setObject(5, null);
                }
                stmt.addBatch();
            }
            affectedRows = stmt.executeBatch().length;
        } catch (SQLException e) {
            logger.error("Exception ", e);
        } finally {
            closeResources(stmt);
        }
        return affectedRows != 0;
    }

    //READ
    @Override
    public List<EventRecurrence> getEventRecurrencesByEventId(int eventId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EventRecurrence> recurrencesList = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
             String sqlStr = "SELECT * FROM  event_recurrence LEFT JOIN recurrence_type ON " +
                    "event_recurrence.recurrence_type_id = recurrence_type.id where event_id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, eventId);
            rs = stmt.executeQuery();
            recurrencesList = createEventRecurrencesFromRS(rs);
        } catch (SQLException | IOException e) {
            logger.error("Exception ", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return recurrencesList;
    }

    //UPDATE
    @Override
    public boolean updateEventRecurrence(EventRecurrence recurrence) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "UPDATE event_recurrence SET event_id = ?, recurrence_type_id = ?, " +
                    "repeat_on = ?, repeat_interval = ?, repeat_end = ? WHERE id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, recurrence.getEventId());
            stmt.setInt(2, recurrence.getRecurrenceType().getId());
            stmt.setString(3, recurrence.getRepeatOn());
            stmt.setInt(4, recurrence.getRepeatInterval());
            if(recurrence.getRepeatEndDate()!= null) {
                stmt.setTimestamp(5, new Timestamp(recurrence.getRepeatEndDate().getTime()));
            } else {
                stmt.setTimestamp(5, null);
            }
            stmt.setInt(6, recurrence.getId());
            affectedRows = stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Exception ", e);
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;    }

    //Delete
    @Override
    public boolean deleteEventRecurrece(int id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;

        try {
            //acquire connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            String sqlStr = "DELETE FROM event_recurrence WHERE id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, id);

            //execute statement
            affectedRows = stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Exception ", e);
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;
    }

    //helper methods

    private List<EventRecurrence> createEventRecurrencesFromRS(ResultSet rs) throws SQLException {

        List<EventRecurrence> recurrencesList = new ArrayList<EventRecurrence>();
        RecurrenceType recType = null;

        while (rs.next()) {


            if (recType == null) {
                recType = new RecurrenceType();
                recType.setTitle(rs.getString("recurrence_type.title"))
                        .setIntervalUnit(rs.getString("recurrence_type.interval_unit"))
                        .setId(rs.getInt(rs.getInt("recurrence_type.id")));
            }

            EventRecurrence recurrence = new EventRecurrence();
            recurrence.setId(rs.getInt("event_recurrence.id"))
                    .setEventId(rs.getInt("event_recurrence.event_id"))
                    .setRepeatInterval(rs.getInt("event_recurrence.repeat_interval"))
                    .setRepeatOn(rs.getString("event_recurrence.repeat_on"))
                    .setRepeatEndDate(rs.getTimestamp("event_recurrence.repeat_end"))
                    .setRecurrenceType(recType);

            recurrencesList.add(recurrence);
        }
        return recurrencesList;
    }
}
