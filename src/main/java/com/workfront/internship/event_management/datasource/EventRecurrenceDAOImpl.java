package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventRecurrence;
import com.workfront.internship.event_management.model.datehelpers.RecurrenceType;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hermine on 7/11/16.
 */
public class EventRecurrenceDAOImpl extends GenericDAO implements EventRecurrenceDAO {

    public boolean insertEventRecurrence(EventRecurrence recurrence) {
        Connection conn = null;
        boolean success = false;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            success = insertEventRecurrence(recurrence, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, null, conn);
        }
        return success;
    }

    public boolean insertEventRecurrence(EventRecurrence recurrence, Connection conn) {
        PreparedStatement stmt = null;
        int affectedRows = 0;
        try {
            String sqlStr = "INSERT INTO event_recurrence "
                    + "(event_id, recurrence_type, repeat_on, repeat_interval, repeat_end) "
                    + "VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, recurrence.getEventId());
            stmt.setInt(2, recurrence.getRecurrenceType().getId());
            stmt.setString(3, recurrence.getRepeatOn());
            stmt.setInt(4, recurrence.getRepeatInterval());
            stmt.setTimestamp(5, new Timestamp(recurrence.getRepeatEndDate().getTime()));
            affectedRows = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return affectedRows != 0;
    }

    public boolean insertEventRecurrences(List<EventRecurrence> recurrences) {
        Connection conn = null;
        boolean success = false;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            success = insertEventRecurrences(recurrences, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, null, conn);
        }
        return success;
    }

    public boolean insertEventRecurrences(List<EventRecurrence> recurrences, Connection conn) {
        PreparedStatement stmt = null;
        int affectedRows = 0;
        try {
            String sqlStr = "INSERT INTO event_recurrence "
                    + "(event_id, recurrence_type_id, repeat_on, repeat_interval, repeat_end) "
                    + "VALUES (?, ?, ?, ?, ?)";
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
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return affectedRows != 0;
    }

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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, conn);
        }
        return recurrencesList;
    }

    public boolean updateEventRecurrence(EventRecurrence recurrence) {
        return false;
    }

    public boolean deleteEventRecurrece(int id) {
        return deleteEntryById("event_recurrence", id);
    }

    //helper methods

    private List<EventRecurrence> createEventRecurrencesFromRS(ResultSet rs) throws SQLException {
        List<EventRecurrence> recurrencesList = new ArrayList<EventRecurrence>();
        RecurrenceType recType = null;
        while (rs.next()) {
            EventRecurrence recurrence = new EventRecurrence();
            if (recType == null) {
                recType = new RecurrenceType();
                recType.setTitle(rs.getString("recurrence_type.title"))
                        .setIntervalUnit(rs.getString("recurrence_type.interval_unit"))
                        .setId(rs.getInt(rs.getInt("recurrence_type.id")));
            }
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
