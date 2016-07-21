package com.workfront.internship.event_management.dao;

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

    private DataSourceManager dataSourceManager;

    public EventRecurrenceDAOImpl(DataSourceManager dataSourceManager) throws Exception {
        super(dataSourceManager);
        this.dataSourceManager = dataSourceManager;
    }

    public EventRecurrenceDAOImpl() {
        try {
            this.dataSourceManager = DataSourceManager.getInstance();
        } catch (IOException | SQLException e) {
            LOGGER.error("Exception...", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public int addEventRecurrence(EventRecurrence recurrence) {

        Connection conn = null;
        int id = 0;

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            id = addEventRecurrence(recurrence, conn);

        } catch (SQLException e) {
            LOGGER.error("Exception ", e);
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
        String query = "SELECT * FROM  event_recurrence " +
                "LEFT JOIN recurrence_type ON event_recurrence.recurrence_type_id = recurrence_type.id " +
                "LEFT JOIN recurrence_option ON event_recurrence.recurrence_option_id = recurrence_option.id " +
                "WHERE event_recurrence.id = ?";

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);

            //execute query
            rs = stmt.executeQuery();

            //get results
            List<EventRecurrence> eventRecurrenceList = createEventRecurrencesFromRS(rs);
            if (!eventRecurrenceList.isEmpty()) {
                eventRecurrence = eventRecurrenceList.get(0);
            }

        } catch (SQLException e) {
            LOGGER.error("Exception ", e);
            throw new RuntimeException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return eventRecurrence;
    }

    int addEventRecurrence(EventRecurrence recurrence, Connection conn) {

        PreparedStatement stmt = null;

        int id = 0;
        String query = "INSERT INTO event_recurrence "
                + "(event_id, recurrence_type_id, recurrence_option_id, repeat_interval, repeat_end) "
                + "VALUES (?, ?, ?, ?, ?)";

        try {
            //get connection
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            //create and initialize statement
            stmt.setInt(1, recurrence.getEventId());
            stmt.setInt(2, recurrence.getRecurrenceType().getId());
            stmt.setInt(3, recurrence.getRecurrenceOptionId());
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
            LOGGER.error("Exception ", e);
            throw new RuntimeException(e);
        } finally {
            closeResources(stmt);
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
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, eventId);

            //execute query
            rs = stmt.executeQuery();

            //get results
            recurrencesList = createEventRecurrencesFromRS(rs);

        } catch (SQLException e) {
            LOGGER.error("Exception ", e);
            throw new RuntimeException(e);
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

        String query = "SELECT * FROM  event_recurrence " +
                "LEFT JOIN recurrence_type ON event_recurrence.recurrence_type_id = recurrence_type.id " +
                "LEFT JOIN recurrence_option ON event_recurrence.recurrence_option_id = recurrence_option.id " +
                "WHERE event_recurrence.id = ?";

        String sqlStr = "SELECT * FROM  event_recurrence " +
                "LEFT JOIN recurrence_type ON event_recurrence.recurrence_type_id = recurrence_type.id " +
                "LEFT JOIN recurrence_option ON event_recurrence.recurrence_option_id = recurrence_option.id";


        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create statement
            stmt = conn.prepareStatement(sqlStr);

            //execute query
            rs = stmt.executeQuery();

            //get results
            recurrencesList = createEventRecurrencesFromRS(rs);

        } catch (SQLException e) {
            LOGGER.error("Exception...", e);
            throw new RuntimeException(e);
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
                "recurrence_option_id = ?, repeat_interval = ?, repeat_end = ? WHERE id = ?";

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, recurrence.getEventId());
            if (recurrence.getRecurrenceType() != null) {
                stmt.setInt(2, recurrence.getRecurrenceType().getId());
            } else {
                stmt.setInt(2, 0);

            }
            stmt.setInt(3, recurrence.getRecurrenceOptionId());
            stmt.setInt(4, recurrence.getRepeatInterval());

            if(recurrence.getRepeatEndDate()!= null) {
                stmt.setTimestamp(5, new Timestamp(recurrence.getRepeatEndDate().getTime()));
            } else {
                stmt.setTimestamp(5, null);
            }

            stmt.setInt(6, recurrence.getId());

            //execute query
            affectedRows = stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Exception ", e);
            throw new RuntimeException(e);
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;    }

    @Override
    public boolean deleteEventRecurrence(int id) {
        return deleteRecordById("event_recurrence", id);
    }

    @Override
    public boolean deleteAllEventRecurrences() {
        return deleteAllRecords("event_recurrence");
    }

    //helper methods

    private List<EventRecurrence> createEventRecurrencesFromRS(ResultSet rs) throws SQLException {

        List<EventRecurrence> recurrencesList = new ArrayList<>();

        while (rs.next()) {

            RecurrenceType recurrenceType = new RecurrenceType();
            recurrenceType.setTitle(rs.getString("recurrence_type.title"))
                        .setIntervalUnit(rs.getString("recurrence_type.interval_unit"))
                    .setId(rs.getInt("recurrence_type.id"));

            EventRecurrence recurrence = new EventRecurrence();
            recurrence.setId(rs.getInt("event_recurrence.id"))
                    .setEventId(rs.getInt("event_recurrence.event_id"))
                    .setRepeatInterval(rs.getInt("event_recurrence.repeat_interval"))
                    .setRecurrenceOptionId(rs.getInt("event_recurrence.recurrence_option_id"))
                    .setRepeatEndDate(rs.getTimestamp("event_recurrence.repeat_end"))
                    .setRecurrenceType(recurrenceType);

            recurrencesList.add(recurrence);
        }
        return recurrencesList;
    }
}
