package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.RecurrenceOption;
import com.workfront.internship.event_management.model.RecurrenceType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hermine on 7/11/16.
 */
public class RecurrenceTypeDAOImpl extends GenericDAO implements RecurrenceTypeDAO {

    @Override
    public int addRecurrenceType(RecurrenceType recurrenceType) {

        Connection conn = null;
        PreparedStatement stmtInsertRecType = null;
        PreparedStatement stmtInsertRepeatOn = null;
        ResultSet rs = null;

        int id = 0;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();
            conn.setAutoCommit(false);

            //create and initialize statement
            String insertRecType = "INSERT INTO recurrence_type "
                    + "(title, interval_unit) values (?, ?) ";
            stmtInsertRecType = conn.prepareStatement(insertRecType, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtInsertRecType.setString(1, recurrenceType.getTitle());
            stmtInsertRecType.setString(2, recurrenceType.getIntervalUnit());

            //execute query
            stmtInsertRecType.executeUpdate();

            //get generated id
            id = getInsertedId(stmtInsertRecType);

            if (recurrenceType.getRepeatOptions() != null) {
                RecurrenceOptionDAO recurrenceOptionDAO = new RecurrenceOptionDAOImpl();
                List<RecurrenceOption> options = recurrenceType.getRepeatOptions();
                for (RecurrenceOption option : options) {
                    recurrenceOptionDAO.addRecurrenceOption(option);
                }
            }
            conn.commit();
        } catch (IOException e) {
                logger.error("Exception ", e);
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException re) {
                logger.error("Exception ", e);
            }
        } finally {
            closeResources(rs);
            closeResources(stmtInsertRepeatOn);
            closeResources(stmtInsertRecType);
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
            conn = DataSourceManager.getInstance().getConnection();
            String query = "SELECT * FROM recurrence_type LEFT JOIN repeat_on_value "
                    + "ON recurrence_type.id = repeat_on_value.recurrence_type_id";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            recurrenceTypeList = createRecurrenceTypesFromRS(rs);
        } catch (IOException | SQLException e) {
                logger.error("Exception ", e);
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
            conn = DataSourceManager.getInstance().getConnection();
            String query = "SELECT * FROM recurrence_type LEFT JOIN repeat_on_value "
                    + "ON recurrence_type.id = repeat_on_value.recurrence_type_id "
                    + "WHERE recurrence_type.id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            recurrenceType = createRecurrenceTypeFromRS(rs);
        } catch (SQLException | IOException e) {
            logger.error("Exception ", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return recurrenceType;
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
    private List<RecurrenceType> createRecurrenceTypesFromRS(ResultSet rs) throws SQLException {
        Map<Integer, RecurrenceType> recurrenceTypeMap = new HashMap<Integer, RecurrenceType>();
        while (rs.next()) {
            Integer recTypeId = rs.getInt("id");

            if (!recurrenceTypeMap.containsKey(recTypeId)) {
                RecurrenceType recType = new RecurrenceType();
                recType.setId(rs.getInt("id"))
                        .setTitle(rs.getString("title"))
                        .setIntervalUnit(rs.getString("interval_unit"));

                if (rs.getString("repeat_on_value.title") != null) {
                    List<String> repeatOnValues = new ArrayList<String>();
                    repeatOnValues.add(rs.getString("repeat_on_value.title"));
                  //  recType.setRepeatOptions(repeatOnValues);
                }

                recurrenceTypeMap.put(recTypeId, recType);
            } else {
                RecurrenceType recType = recurrenceTypeMap.get(recTypeId);
              //  recType.getRepeatOptions().add(rs.getString("repeat_on_value.title"));
            }
        }
        return new ArrayList<RecurrenceType>(recurrenceTypeMap.values());
    }

    private RecurrenceType createRecurrenceTypeFromRS(ResultSet rs) throws SQLException {
        RecurrenceType recType = null;
        while (rs.next()) {
            if (recType == null) {
                recType = new RecurrenceType();
                recType.setId(rs.getInt("id"))
                        .setTitle(rs.getString("title"))
                        .setIntervalUnit(rs.getString("interval_unit"));
                if (rs.getString("repeat_on_value.title") != null) {
                    List<String> repeatOnValues = new ArrayList<String>();
                    repeatOnValues.add(rs.getString("repeat_on_value.title"));
                   // recType.setRepeatOptions(repeatOnValues);
                }
            } else {
              //  recType.getRepeatOptions().add(rs.getString("repeat_on_value.title"));
            }
        }
        return recType;
    }
}
