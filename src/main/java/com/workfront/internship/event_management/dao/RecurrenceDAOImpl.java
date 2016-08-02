package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.model.Recurrence;
import com.workfront.internship.event_management.model.RecurrenceType;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hermine Turshujyan 7/11/16.
 */
public class RecurrenceDAOImpl extends GenericDAO implements RecurrenceDAO {

    static final Logger LOGGER = Logger.getLogger(RecurrenceDAOImpl.class);
    private DataSourceManager dataSourceManager;

    public RecurrenceDAOImpl(DataSourceManager dataSourceManager) throws Exception {
        super(dataSourceManager);
        this.dataSourceManager = dataSourceManager;
    }

    public RecurrenceDAOImpl() throws DAOException {
        try {
            this.dataSourceManager = DataSourceManager.getInstance();
        } catch (IOException | SQLException e) {
            LOGGER.error("Could not instantiate data source manager for RecurrenceDAOImpl .", e);
            throw new DAOException("Could not instantiate data source manager", e);
        }
    }

    @Override
    public int addRecurrence(Recurrence recurrence) throws DAOException {

        Connection conn = null;
        int id = 0;

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            id = addEventRecurrence(recurrence, conn);
        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(conn);
        }
        return id;
    }

    @Override
    public void addRecurrences(List<Recurrence> recurrenceList) throws DAOException {
        Connection conn = null;
        try {
            //get connection
            conn = dataSourceManager.getConnection();

            addEventRecurrences(recurrenceList, conn);
        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(conn);
        }
    }

    public int addEventRecurrences(List<Recurrence> recurrenceList, Connection conn) throws DAOException {
        PreparedStatement stmt = null;

        int id = 0;
        String query = "INSERT INTO event_recurrence "
                + "(event_id, recurrence_type_id, recurrence_option_id, repeat_interval, repeat_end) "
                + "VALUES (?, ?, ?, ?, ?)";
        try {
            //get connection
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            conn.setAutoCommit(false);

            //create and initialize statement
            for (Recurrence recurrence : recurrenceList) {
                stmt.setInt(1, recurrence.getEventId());
                stmt.setInt(2, recurrence.getRecurrenceType().getId());
                stmt.setInt(3, recurrence.getRecurrenceOption().getId());
                stmt.setInt(4, recurrence.getRepeatInterval());
                if (recurrence.getRepeatEndDate() != null) {
                    stmt.setTimestamp(5, new Timestamp(recurrence.getRepeatEndDate().getTime()));
                } else {
                    stmt.setTimestamp(5, null);
                }
                stmt.addBatch();
            }

            //execute query
            stmt.executeBatch();

            //get generated id
            id = getInsertedId(stmt);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(stmt);
        }
        return id;
    }

    @Override
    public Recurrence getRecurrenceById(int id) throws ObjectNotFoundException, DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Recurrence eventRecurrence = null;
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
            List<Recurrence> eventRecurrenceList = createEventRecurrencesFromRS(rs);

            if (eventRecurrenceList.isEmpty()) {
                throw new ObjectNotFoundException("Event recurrence with id " + id + " was not found!");
            }

            eventRecurrence = eventRecurrenceList.get(0);

        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return eventRecurrence;
    }

    int addEventRecurrence(Recurrence recurrence, Connection conn) throws DAOException {

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
            stmt.setInt(3, recurrence.getRecurrenceOption().getId());
            stmt.setInt(4, recurrence.getRepeatInterval());
            if(recurrence.getRepeatEndDate()!= null) {
                stmt.setTimestamp(5, new Timestamp(recurrence.getRepeatEndDate().getTime()));
            } else {
                stmt.setTimestamp(5, null);
            }

            //execute query
            stmt.executeUpdate();

            //get generated id
            id = getInsertedId(stmt);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(stmt);
        }
        return id;
    }

    @Override
    public List<Recurrence> getRecurrencesByEventId(int eventId) throws DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Recurrence> recurrencesList = null;
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
            throw new DAOException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return recurrencesList;
    }

    @Override
    public List<Recurrence> getAllRecurrences() throws DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Recurrence> recurrencesList = new ArrayList<>();

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
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return recurrencesList;
    }

    @Override
    public void updateRecurrence(Recurrence recurrence) throws ObjectNotFoundException, DAOException {

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
            if (recurrence.getRecurrenceOption() != null) {
                stmt.setInt(3, recurrence.getRecurrenceOption().getId());
            } else {
                stmt.setInt(3, 0);
            }

            stmt.setInt(4, recurrence.getRepeatInterval());

            if (recurrence.getRepeatEndDate() != null) {
                stmt.setTimestamp(5, new Timestamp(recurrence.getRepeatEndDate().getTime()));
            } else {
                stmt.setTimestamp(5, null);
            }

            stmt.setInt(6, recurrence.getId());

            //execute query
            affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new ObjectNotFoundException("Event recurrence not found!");
            }
        } catch (SQLException e) {
            LOGGER.error("SQL Exception ", e);
            throw new DAOException(e);
        } finally {
            closeResources(stmt, conn);
        }
    }

    @Override
    public void deleteRecurrence(int id) throws DAOException, ObjectNotFoundException {
        deleteRecordById("event_recurrence", id);
    }

    @Override
    public void deleteRecurrencesByEventId(int eventId) throws ObjectNotFoundException, DAOException {
        deleteRecord("event_recurrence", "event_id", eventId);

    }

    @Override
    public void deleteAllRecurrences() throws DAOException {
        deleteAllRecords("event_recurrence");
    }

    //helper methods

    private List<Recurrence> createEventRecurrencesFromRS(ResultSet rs) throws SQLException {

        List<Recurrence> recurrencesList = new ArrayList<>();

        while (rs.next()) {

            RecurrenceType recurrenceType = new RecurrenceType();
            recurrenceType.setTitle(rs.getString("recurrence_type.title"))
                        .setIntervalUnit(rs.getString("recurrence_type.interval_unit"))
                    .setId(rs.getInt("recurrence_type.id"));

            Recurrence recurrence = new Recurrence();
            recurrence.setId(rs.getInt("event_recurrence.id"))
                    .setEventId(rs.getInt("event_recurrence.event_id"))
                    .setRepeatInterval(rs.getInt("event_recurrence.repeat_interval"))
                    // .setRecurrenceOption(rs.getInt("event_recurrence.recurrence_option_id")) // TODO: 7/28/16 correct
                    .setRepeatEndDate(rs.getTimestamp("event_recurrence.repeat_end"))
                    .setRecurrenceType(recurrenceType);

            recurrencesList.add(recurrence);
        }
        return recurrencesList;
    }
}
