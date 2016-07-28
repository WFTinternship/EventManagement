package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.model.RecurrenceOption;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hermine Turshujyan 7/14/16.
 */
public class RecurrenceOptionDAOImpl extends GenericDAO implements RecurrenceOptionDAO {

    static final Logger LOGGER = Logger.getLogger(RecurrenceOptionDAOImpl.class);
    private DataSourceManager dataSourceManager;

    public RecurrenceOptionDAOImpl(DataSourceManager dataSourceManager) throws Exception {
        super(dataSourceManager);
        this.dataSourceManager = dataSourceManager;
    }

    public RecurrenceOptionDAOImpl() throws DAOException {
        try {
            this.dataSourceManager = DataSourceManager.getInstance();
        } catch (IOException | SQLException e) {
            LOGGER.error("Could not instantiate data source manager", e);
            throw new DAOException("Could not instantiate data source manager", e);
        }
    }

    @Override
    public int addRecurrenceOption(RecurrenceOption recurrenceOption) throws DAOException {
        Connection conn = null;
        int id = 0;

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //insert recurrenceOption and get generated id
            id = addRecurrenceOption(recurrenceOption, conn);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(conn);
        }
        return id;
    }

    @Override
    public void addRecurrenceOptions(List<RecurrenceOption> options) throws DAOException {
        Connection conn = null;
        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //insert recurrenceOption and get generated id
            addRecurrenceOptions(options, conn);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(conn);
        }
    }

    private int addRecurrenceOption(RecurrenceOption option, Connection conn) throws DAOException {
        PreparedStatement stmt = null;

        int id = 0;
        String query = "INSERT INTO recurrence_option " +
                "(recurrence_type_id, title, abbreviation) VALUES (?, ?, ?)";
        try {
            //create and initialize statement
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, option.getRecurrenceTypeId());
            stmt.setString(2, option.getTitle());
            stmt.setString(3, option.getAbbreviation());

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

    void addRecurrenceOptions(List<RecurrenceOption> options, Connection conn) throws DAOException {
        PreparedStatement stmt = null;

        String query = "INSERT INTO recurrence_option " +
                "(recurrence_type_id, title, abbreviation) VALUES (?, ?, ?)";
        try {
            //create and initialize statement
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            for (RecurrenceOption option : options) {
                stmt.setInt(1, option.getRecurrenceTypeId());
                stmt.setString(2, option.getTitle());
                stmt.setString(3, option.getAbbreviation());
                stmt.addBatch();
            }

            //execute query
            stmt.executeBatch();
        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(stmt);
        }
    }

    @Override
    public List<RecurrenceOption> getAllRecurrenceOptions() throws DAOException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<RecurrenceOption> optionList = new ArrayList<>();

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            String query = "SELECT * FROM recurrence_option ";
            stmt = conn.prepareStatement(query);

            //execute query
            rs = stmt.executeQuery();

            //get results
            optionList = createRecurrenceOptionListFromRS(rs);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return optionList;
    }

    @Override
    public RecurrenceOption getRecurrenceOption(int optionId) throws DAOException, ObjectNotFoundException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        RecurrenceOption option = null;
        String query = "SELECT * FROM recurrence_option WHERE id = ?";

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, optionId);

            //execute query
            rs = stmt.executeQuery();

            //get results
            List<RecurrenceOption> optionsList = createRecurrenceOptionListFromRS(rs);
            if (optionsList.isEmpty()) {
                throw new ObjectNotFoundException("Recurrence option with id " + optionId + " not found!");
            } else {
                option = optionsList.get(0);
            }
        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return option;
    }

    @Override
    public List<RecurrenceOption> getRecurrenceOptionsByRecurrenceType(int recurrenceTypeId) throws DAOException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String query = "SELECT * FROM recurrence_option WHERE recurrence_type_id = ?";
        List<RecurrenceOption> optionList = new ArrayList<>();

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, recurrenceTypeId);

            //execute query
            rs = stmt.executeQuery();

            //get results
            optionList = createRecurrenceOptionListFromRS(rs);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return optionList;
    }

    @Override
    public void updateRecurrenceOption(RecurrenceOption option) throws DAOException, ObjectNotFoundException {
        Connection conn = null;
        PreparedStatement stmt = null;

        String query = "UPDATE  recurrence_option SET " +
                "recurrence_type_id = ?,  title = ?,  abbreviation = ? WHERE id = ?";
        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, option.getRecurrenceTypeId());
            stmt.setString(2, option.getTitle());
            stmt.setString(3, option.getAbbreviation());
            stmt.setInt(4, option.getId());

            //execute query
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new ObjectNotFoundException("Recurrence option with id " + option.getId() + " not found!");
            }
        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(stmt, conn);
        }
    }

    @Override
    public void deleteRecurrenceOption(int optionId) throws ObjectNotFoundException, DAOException {
        deleteRecordById("recurrence_option", optionId);
    }

    @Override
    public void deleteRecurrenceOptionsByRecurrenceType(int recurrenceTypeId) throws ObjectNotFoundException, DAOException {
        deleteRecord("recurrence_option", "recurrence_type_id", recurrenceTypeId);
    }

    @Override
    public void deleteAllRecurrenceOptions() throws DAOException {
        deleteAllRecords("recurrence_option");
    }

    //helper methods
    private List<RecurrenceOption> createRecurrenceOptionListFromRS(ResultSet rs) throws SQLException {
        List<RecurrenceOption> optionList = new ArrayList<>();
        while (rs.next()) {
            RecurrenceOption option = new RecurrenceOption();
            option.setId(rs.getInt("id"))
                    .setTitle(rs.getString("title"))
                    .setRecurrenceTypeId(rs.getInt("recurrence_type_id"))
                    .setAbbreviation(rs.getString("abbreviation"));

            optionList.add(option);
        }
        return optionList;
    }

}
