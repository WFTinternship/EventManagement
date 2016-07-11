package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventRecurrence;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hermine on 7/11/16.
 */
public class EventRecurrenceDAOImpl extends GenericDAO implements EventRecurrenceDAO{

    public boolean insertEventRecurrence(EventRecurrence recurrence) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
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

    public List<EventRecurrence> getEventRecurrencesByEventId(int eventId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EventRecurrence> recurrencesList  = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT * FROM  event_recurrence where event_id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, eventId);
            rs = stmt.executeQuery();
            recurrencesList = createEvenRecurrencesFromRS(rs);
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return recurrencesList;
    }

    public boolean updateEventRecurrence(EventRecurrence recurrence) {
return false;
    }
    public boolean deleteEventRecurrece(int id){
return false;
    }

    //helper methods

    private List<EventRecurrence> createEvenRecurrencesFromRS(ResultSet rs) throws SQLException {
        List<EventRecurrence> recurrencesList = new ArrayList<EventRecurrence>();
        while (rs.next()) {
            EventRecurrence recurrence = new EventRecurrence();
            recurrence.setEventId(rs.getInt("id"));
             /*       .setTitle(rs.getString("title"))
                    .setDescription(rs.getString("description"))
                    .setCreationDate(rs.getTimestamp("creation_date"));
            recurrencesList.add(caterecurrencegory);*/
        }
        return recurrencesList;
    }
}
