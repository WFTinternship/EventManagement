package com.workfront.internship.event_management.DAO;

import com.workfront.internship.event_management.model.RecurrenceOption;
import com.workfront.internship.event_management.model.RecurrenceType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hermine on 7/11/16.
 */
public class RecurrenceTypeDAOImpl extends GenericDAO implements RecurrenceTypeDAO {

    @Override
    public int addRecurrenceType(RecurrenceType recurrenceType) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int id = 0;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //start transaction
            conn.setAutoCommit(false);

            //create and initialize statement
            String query = "INSERT INTO recurrence_type "
                    + "(title, interval_unit) values (?, ?) ";
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, recurrenceType.getTitle());
            stmt.setString(2, recurrenceType.getIntervalUnit());

            //execute query
            stmt.executeUpdate();

            //get generated id
            id = getInsertedId(stmt);

            //add data into recurrence_option table
            if (recurrenceType.getRecurrenceOptions() != null) {

                RecurrenceOptionDAO recurrenceOptionDAO = new RecurrenceOptionDAOImpl();
                List<RecurrenceOption> options = recurrenceType.getRecurrenceOptions();
                for (RecurrenceOption option : options) {
                    option.setRecurrenceTypeId(id);
                    recurrenceOptionDAO.addRecurrenceOption(option, conn);
                }

            }
            conn.commit();
        } catch (IOException e) {
            LOGGER.error("Exception ", e);
            throw new RuntimeException(e);
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException re) {
                LOGGER.error("Transaction failed! ", e);
            }
            throw new RuntimeException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return id;
    }

   @Override
   public List<RecurrenceType> getAllRecurrenceTypes() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<RecurrenceType> recurrenceTypeList = null;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            String query = "SELECT * FROM recurrence_type ";
            stmt = conn.prepareStatement(query);

            //execute query
            rs = stmt.executeQuery();

            //get results
            recurrenceTypeList = createRecurrenceTypeListFromRS(rs);

        } catch (IOException | SQLException e) {
            LOGGER.error("Exception ", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return recurrenceTypeList;
    }

    @Override
    public RecurrenceType getRecurrenceTypeById(int id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        RecurrenceType recurrenceType = null;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            String query = "SELECT * FROM recurrence_type "
                    + "WHERE recurrence_type.id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);

            //execute query
            rs = stmt.executeQuery();

            //get result
            List<RecurrenceType> recurrenceTypes = createRecurrenceTypeListFromRS(rs);
            if (recurrenceTypes != null & recurrenceTypes.size() == 1) {
                recurrenceType = recurrenceTypes.get(0);

                //read recurrenceType options from another table
                RecurrenceOptionDAO recurrenceOptionDAO = new RecurrenceOptionDAOImpl();
                List<RecurrenceOption> recurrenceOptionList = recurrenceOptionDAO.getRecurrenceOptionsByRecurrenceType(id);

                //set result to recurrenceType object
                recurrenceType.setRecurrenceOptions(recurrenceOptionList);
            }

        } catch (SQLException | IOException e) {
            LOGGER.error("Exception ", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return recurrenceType;
    }

    @Override
    public boolean updateRecurrenceType(RecurrenceType recurrenceType) {

        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            String sqlStr = "UPDATE recurrence_type SET title = ?, interval_unit = ? WHERE id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setString(1, recurrenceType.getTitle());
            stmt.setString(2, recurrenceType.getIntervalUnit());
            stmt.setInt(3, recurrenceType.getId());

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
    public boolean deleteRecurrenceType(int id) {
        return deleteRecordById("recurrence_type", id);
    }

    @Override
    public boolean deleteAllRecurrenceTypes() {
        return deleteAllRecords("recurrence_type");
    }


    //helper methods
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

}
