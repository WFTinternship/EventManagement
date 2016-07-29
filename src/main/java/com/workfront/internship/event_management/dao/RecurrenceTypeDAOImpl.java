package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.model.RecurrenceOption;
import com.workfront.internship.event_management.model.RecurrenceType;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hermine Turshujyan 7/11/16.
 */
public class RecurrenceTypeDAOImpl extends GenericDAO implements RecurrenceTypeDAO {

    static final Logger LOGGER = Logger.getLogger(RecurrenceTypeDAOImpl.class);
    private DataSourceManager dataSourceManager;

    public RecurrenceTypeDAOImpl(DataSourceManager dataSourceManager) throws Exception {
        super(dataSourceManager);
        this.dataSourceManager = dataSourceManager;
    }

    public RecurrenceTypeDAOImpl() throws DAOException {
        try {
            this.dataSourceManager = DataSourceManager.getInstance();
        } catch (IOException | SQLException e) {
            LOGGER.error("Could not instantiate data source manager", e);
            throw new DAOException("Could not instantiate data source manager", e);
        }
    }

    @Override
    public int addRecurrenceType(RecurrenceType recurrenceType) throws DAOException, DuplicateEntryException {
        Connection conn = null;
        int id = 0;

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //execute query and get generated id
            id = addRecurrenceType(recurrenceType, conn);
        } catch (SQLIntegrityConstraintViolationException e) {
            LOGGER.error("Duplicate recurrence type entry", e);
            throw new DuplicateEntryException("Recurrence type with title " + recurrenceType.getTitle() + " already exists!", e);
        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(conn);
        }
        return id;
    }

    @Override
    public int addRecurrenceTypeWithOptions(RecurrenceType recurrenceType) throws DuplicateEntryException, DAOException {
        Connection conn = null;
        int id = 0;

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //start transaction
            conn.setAutoCommit(false);

            //insert recurrence type
            int recurrenceTypeId = addRecurrenceType(recurrenceType, conn);

            //set generated recurrence type id to recurrence options
            List<RecurrenceOption> options = recurrenceType.getRecurrenceOptions();
            for (RecurrenceOption option : options) {
                option.setRecurrenceTypeId(id);
            }

            //add recurrence options into db
            RecurrenceOptionDAO recurrenceOptionDAO = new RecurrenceOptionDAOImpl();
            if (recurrenceType.getRecurrenceOptions() != null && recurrenceType.getRecurrenceOptions().size() != 0) {
                ((RecurrenceOptionDAOImpl) recurrenceOptionDAO).addRecurrenceOptions(options, conn);
            }

            //commit transaction
            conn.commit();
        } catch (SQLIntegrityConstraintViolationException e) {
            LOGGER.error("Duplicate recurrence type entry", e);
            throw new DuplicateEntryException("Recurrence type with title " + recurrenceType.getTitle() + " already exists!", e);
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException re) {
                LOGGER.error("Transaction failed!", e);
                throw new DAOException("Could not insert recurrence type into db. Transaction failed!", e);
            }
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(conn);
        }
        return id;
    }

    @Override
    public List<RecurrenceType> getAllRecurrenceTypes() throws DAOException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String query = "SELECT * FROM recurrence_type ";
        List<RecurrenceType> recurrenceTypeList = new ArrayList<>();
        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create statement
            stmt = conn.prepareStatement(query);

            //execute query
            rs = stmt.executeQuery();

            //get results
            recurrenceTypeList = createRecurrenceTypeListFromRS(rs);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception ", e);
            throw new DAOException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return recurrenceTypeList;
    }

    @Override
    public RecurrenceType getRecurrenceTypeById(int id) throws DAOException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        RecurrenceType recurrenceType = null;
        String query = "SELECT * FROM recurrence_type WHERE recurrence_type.id = ?";
        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);

            //execute query
            rs = stmt.executeQuery();

            //get result
            List<RecurrenceType> recurrenceTypes = createRecurrenceTypeListFromRS(rs);
            if (recurrenceTypes != null && recurrenceTypes.size() == 1) {
                recurrenceType = recurrenceTypes.get(0);
            }
        } catch (SQLException e) {
            LOGGER.error("SQL Exception ", e);
            throw new DAOException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return recurrenceType;
    }

    @Override
    public RecurrenceType getRecurrenceTypeWithOptionsById(int id) throws DAOException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        RecurrenceType recurrenceType = null;
        String query = "SELECT * FROM recurrence_type " +
                "LEFT JOIN recurrence_option ON recurrence_type.id = recurrence_option.recurrence_type_id " +
                "WHERE recurrence_type.id = ?";

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);

            //execute query
            rs = stmt.executeQuery();

            //get result
            recurrenceType = createRecurrenceTypeWithOptionsFromRS(rs);

        } catch (SQLException e) {
            LOGGER.error("SQL Exception ", e);
            throw new DAOException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return recurrenceType;
    }

    @Override
    public void updateRecurrenceType(RecurrenceType recurrenceType) throws DAOException, DuplicateEntryException, ObjectNotFoundException {
        Connection conn = null;
        PreparedStatement stmt = null;

        int affectedRows = 0;
        String query = "UPDATE recurrence_type SET title = ?, interval_unit = ? WHERE id = ?";

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setString(1, recurrenceType.getTitle());
            stmt.setString(2, recurrenceType.getIntervalUnit());
            stmt.setInt(3, recurrenceType.getId());

            //execute query
            affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new ObjectNotFoundException("Recurrence type with id " + recurrenceType.getId() + " not found!");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            LOGGER.error("Duplicate recurrence type entry", e);
            throw new DuplicateEntryException("Recurrence type with title " + recurrenceType.getTitle() + " already exists!", e);
        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(stmt, conn);
        }
    }

    @Override
    public void deleteRecurrenceType(int id) throws ObjectNotFoundException, DAOException {
        deleteRecordById("recurrence_type", id);
    }

    @Override
    public void deleteAllRecurrenceTypes() throws DAOException {
        deleteAllRecords("recurrence_type");
    }

    //helper methods
    public int addRecurrenceType(RecurrenceType recurrenceType, Connection conn) throws DAOException, DuplicateEntryException {
        PreparedStatement stmt = null;

        int id = 0;
        String query = "INSERT INTO recurrence_type "
                + "(title, interval_unit) VALUES (?, ?) ";
        try {
            //create and initialize statement
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, recurrenceType.getTitle());
            stmt.setString(2, recurrenceType.getIntervalUnit());

            //execute query
            stmt.executeUpdate();

            //get generated id
            id = getInsertedId(stmt);
        } catch (SQLIntegrityConstraintViolationException e) {
            LOGGER.error("Duplicate recurrence type entry", e);
            throw new DuplicateEntryException("Recurrence type with title " + recurrenceType.getTitle() + " already exists!", e);
        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(stmt);
        }
        return id;
    }

    private List<RecurrenceType> createRecurrenceTypeListFromRS(ResultSet rs) throws SQLException {

        List<RecurrenceType> recurrenceTypeList = new ArrayList<>();

        while (rs.next()) {
            RecurrenceType recurrenceType = new RecurrenceType();
            recurrenceType.setId(rs.getInt("id"))
                    .setTitle(rs.getString("title"))
                    .setIntervalUnit(rs.getString("interval_unit"));

            recurrenceTypeList.add(recurrenceType);
        }
        return recurrenceTypeList;
    }

    private RecurrenceType createRecurrenceTypeWithOptionsFromRS(ResultSet rs) throws SQLException {

        RecurrenceType recurrenceType = new RecurrenceType();

        while (rs.next()) {
            if (rs.getInt("recurrence_option.id") != 0) {
                RecurrenceOption recurrenceOption = new RecurrenceOption();
                recurrenceOption.setId(rs.getInt("recurrence_option.id"))
                        .setRecurrenceTypeId(rs.getInt("recurrence_type.id"))
                        .setTitle(rs.getString("recurrence_option.title"))
                        .setAbbreviation(rs.getString("recurrence_option.abbreviation"));
            }

            recurrenceType.setId(rs.getInt("recurrence_type.id"))
                    .setTitle(rs.getString("recurrence_type.title"))
                    .setIntervalUnit(rs.getString("recurrence_type.interval_unit"));

        }
        return recurrenceType;
    }
}
