package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.RecurrenceOption;

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

    @Override
    public int addRecurrenceOption(RecurrenceOption recurrenceOption) {

        Connection conn = null;
        int id = 0;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //insert recurrenceOption and get generated id
            id = addRecurrenceOption(recurrenceOption, conn);

        } catch (SQLException | IOException e) {
            LOGGER.error("Exception ", e);
        } finally {
            closeResources(conn);
        }
        return id;
    }

    @Override
    public int addRecurrenceOption(RecurrenceOption option, Connection conn) {

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
            LOGGER.error("Exception...", e);

            throw new RuntimeException(e);
        } finally {
            closeResources(stmt);
        }
        return id;
    }

    @Override
    public List<RecurrenceOption> getAllRecurrenceOptions() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<RecurrenceOption> optionList = new ArrayList<>();

        try {
            //acquire connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            String query = "SELECT * FROM recurrence_option ";
            stmt = conn.prepareStatement(query);

            //execute query
            rs = stmt.executeQuery();

            //get results
            optionList = createRecurrenceOptionListFromRS(rs);

        } catch (SQLException | IOException e) {
            LOGGER.error("Exception...", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return optionList;
    }

    @Override
    public RecurrenceOption getRecurrenceOption(int optionId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        RecurrenceOption option = null;

        try {
            //acquire connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            String query = "SELECT * FROM recurrence_option WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, optionId);

            //execute query
            rs = stmt.executeQuery();

            //get results
            List<RecurrenceOption> optionsList = createRecurrenceOptionListFromRS(rs);
            if (!optionsList.isEmpty()) {
                option = optionsList.get(0);
            }
        } catch (SQLException | IOException e) {
            LOGGER.error("Exception...", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return option;
    }

    @Override
    public List<RecurrenceOption> getRecurrenceOptionsByRecurrenceType(int recurrenceTypeId) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<RecurrenceOption> optionList = new ArrayList<>();

        try {
            //acquire connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            String query = "SELECT * FROM recurrence_option WHERE recurrence_type_id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, recurrenceTypeId);

            //execute query
            rs = stmt.executeQuery();

            //get results
            optionList = createRecurrenceOptionListFromRS(rs);

        } catch (SQLException | IOException e) {
            LOGGER.error("Exception...", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return optionList;
    }

    @Override
    public boolean updateRecurrenceOption(RecurrenceOption option) {
        Connection conn = null;
        PreparedStatement stmt = null;

        String query = "UPDATE  recurrence_option SET " +
                "recurrence_type_id = ?,  title = ?,  abbreviation = ? WHERE id = ?";
        int affectedRows = 0;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, option.getRecurrenceTypeId());
            stmt.setString(2, option.getTitle());
            stmt.setString(3, option.getAbbreviation());
            stmt.setInt(4, option.getId());

            //execute query
            affectedRows = stmt.executeUpdate();

        } catch (SQLException | IOException e) {
            LOGGER.error("Exception...", e);
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;
    }

    @Override
    public boolean deleteRecurrenceOption(int optionId) {
        return deleteRecordById("recurrence_option", optionId);
    }

    @Override
    public boolean deleteRecurrenceOptionsByRecurrenceType(int recurrenceTypeId) {
        return deleteRecord("recurrence_option", "recurrence_type_id", recurrenceTypeId);
    }

    @Override
    public boolean deleteAllRecurrenceOptions() {
        return deleteAllRecords("recurrence_option");
    }

    //helper methods
    private List<RecurrenceOption> createRecurrenceOptionListFromRS(ResultSet rs) throws SQLException {

        List<RecurrenceOption> optionList = new ArrayList<RecurrenceOption>();

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
