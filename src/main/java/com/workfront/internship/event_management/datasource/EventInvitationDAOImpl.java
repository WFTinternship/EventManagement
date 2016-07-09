package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventInvitation;
import com.workfront.internship.event_management.model.User;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hermine on 7/8/16.
 */
public class EventInvitationDAOImpl extends GenericDAO implements EventInvitationDAO {

    public boolean insertInvitation(EventInvitation invitation) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "INSERT INTO event_invitation "
                    + "(event_id, user_id, user_role) VALUES "
                    + "(?, ?, ? )";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, invitation.getEventId());
            stmt.setInt(1, invitation.getUser().getId());
            stmt.setString(1, invitation.getUserRole());
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

    public boolean insertInvitationsList(List<EventInvitation> invitations) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "INSERT INTO event_invitation "
                    + "(event_id, user_id, user_role) VALUES "
                    + "(?, ?, ? )";
            stmt = conn.prepareStatement(sqlStr);
            conn.setAutoCommit(false);
            for (EventInvitation invitation : invitations) {
                stmt.setInt(1, invitation.getEventId());
                stmt.setInt(2, invitation.getUser().getId());
                stmt.setString(3, invitation.getUserRole());
                stmt.addBatch();
            }
            affectedRows = stmt.executeBatch().length;
            conn.commit();
            conn.setAutoCommit(true);
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

    public List<EventInvitation> getInvitationsByEventId(int eventId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EventInvitation> invitationsList = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT * FROM event_invitation LEFT JOIN user " +
                    "ON event_invitation.user_id = user.id " +
                    "WHERE event_invitation.event_id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, eventId);
            rs = stmt.executeQuery();
            invitationsList = createInvitationsListFromRS(rs);
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return invitationsList;
    }

    public boolean updateInvitation(EventInvitation ivitation) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "UPDATE event_invitation SET user_response = ?, attendees_count = ?, " +
                    "real_participation = ? WHERE event_id = ? && user_id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setString(1, ivitation.getUserResponse());
            stmt.setInt(2, ivitation.getAttendeesCount());
            stmt.setBoolean(3, ivitation.isRealParticipation());
            stmt.setInt(4, ivitation.getEventId());
            stmt.setInt(5, ivitation.getUser().getId());
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

    public boolean deleteInvitation(int eventId, int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "DELETE FROM event_invitation WHERE user_id = ? && event_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(1, eventId);
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
    private List<EventInvitation> createInvitationsListFromRS(ResultSet rs) throws SQLException {
        List<EventInvitation> invitationsList = new ArrayList<EventInvitation>();
        while (rs.next()) {
            EventInvitation invitation = new EventInvitation();
            User user = new User();
            user.setId(rs.getInt("id"))
                    .setFirstName(rs.getString("first_name"))
                    .setLastName(rs.getString("last_name"))
                    .setUsername(rs.getString("username"))
                    .setUsername(rs.getString("password"))
                    .setEmail(rs.getString("email"))
                    .setAvatarPath(rs.getString("avatar_path"))
                    .setPhoneNumber(rs.getString("phone_number"))
                    .setVerified(rs.getBoolean("verified"))
                    .setRegistrationDate(rs.getTimestamp("registration_date"));
            invitation.setUser(user)
                    .setEventId(rs.getInt("event_id"))
                    .setAttendeesCount(rs.getInt("attendees_count"))
                    .setUserResponse(rs.getString("user_response"))
                    .setUserRole(rs.getString("user_role"))
                    .setRealParticipation(rs.getBoolean("real_participation"));
            invitationsList.add(invitation);
        }
        return invitationsList;
    }

}
