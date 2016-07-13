package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.datehelpers.RecurrenceType;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hermine on 7/11/16.
 */
public class RecurrenceTypeDAOImpl extends GenericDAO implements RecurrenceTypeDAO {

    public int insertRecurrenceType(RecurrenceType recurrenceType) {
        Connection conn = null;
        PreparedStatement stmtInsertRecType = null;
        PreparedStatement stmtInsertRepeatOn = null;
        ResultSet rs = null;
        int recTypeId = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            conn.setAutoCommit(false);

            String insertRecType = "INSERT INTO recurrence_type "
                    + "(title, interval_unit) values (?, ?) ";
            stmtInsertRecType = conn.prepareStatement(insertRecType, Statement.RETURN_GENERATED_KEYS);
            stmtInsertRecType.setString(1, recurrenceType.getTitle());
            stmtInsertRecType.setString(2, recurrenceType.getIntervalUnit());
            stmtInsertRecType.executeUpdate();

            rs = stmtInsertRecType.getGeneratedKeys();

            if (rs.next()) {
                recTypeId = rs.getInt(1);
            }

            if (recurrenceType.getRepeatOnValues() != null) {
                String insertRepeatOnValues = "INSERT INTO repeat_on_value "
                        + "(recurrence_type_id, title) VALUES "
                        + "(?, ?)";
                stmtInsertRepeatOn = conn.prepareStatement(insertRepeatOnValues);
                List<String> values = recurrenceType.getRepeatOnValues();
                for (String value : values) {
                    stmtInsertRepeatOn.setInt(1, recTypeId);
                    stmtInsertRepeatOn.setString(2, value);
                    stmtInsertRepeatOn.addBatch();
                }
                stmtInsertRepeatOn.executeBatch();
            }
            conn.commit();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException re) {
                System.out.println("SQLException " + e.getMessage());
            }
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(rs);
            closeResources(stmtInsertRepeatOn);
            closeResources(stmtInsertRecType);
        }
        return recTypeId;
    }

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
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return recurrenceTypeList;
    }

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
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return recurrenceType;
    }

    public boolean deleteRecurrenceType(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "DELETE FROM recurrence_type WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setInt(1, id);
            affectedRows = preparedStatement.executeUpdate();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(null, stmt, conn);
        }
        return affectedRows != 0;
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
                    recType.setRepeatOnValues(repeatOnValues);
                }

                recurrenceTypeMap.put(recTypeId, recType);
            } else {
                RecurrenceType recType = recurrenceTypeMap.get(recTypeId);
                recType.getRepeatOnValues().add(rs.getString("repeat_on_value.title"));
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
                    recType.setRepeatOnValues(repeatOnValues);
                }
            } else {
                recType.getRepeatOnValues().add(rs.getString("repeat_on_value.title"));
            }
        }
        return recType;
    }
}
