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
        boolean success = false;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            success = insertInvitation(invitation, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, null, conn);
        }
        return success;
    }

    public boolean insertInvitation(EventInvitation invitation, Connection conn) {
        PreparedStatement stmt = null;
        int affectedRows = 0;
        try {
            String sqlStr = "INSERT INTO event_invitation "
                    + "(event_id, user_id, user_role, user_response, attendees_count, participated) VALUES "
                    + "(?, ?, ?, ?, ?, ? )";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, invitation.getEventId());
            stmt.setInt(2, invitation.getUser().getId());
            stmt.setString(3, invitation.getUserRole());
            stmt.setString(4, invitation.getUserResponse());
            stmt.setInt(5, invitation.getAttendeesCount());
            stmt.setBoolean(6, invitation.isParticipated());
            affectedRows = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }        }
        return affectedRows != 0;
    }

    public boolean insertInvitations(List<EventInvitation> invitations) {
        Connection conn = null;
        boolean success = false;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            success = insertInvitations(invitations, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, null, conn);
        }
        return success;    }

    public boolean insertInvitations(List<EventInvitation> invitations, Connection conn) {
        PreparedStatement stmt = null;
        int affectedRows = 0;
        try {
            String sqlStr = "INSERT INTO event_invitation "
                    + "(event_id, user_id, user_role, user_response, attendees_count, participated) VALUES "
                    + "(?, ?, ?, ?, ?, ? )";
            stmt = conn.prepareStatement(sqlStr);
          //  conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sqlStr);
            for (EventInvitation invitation : invitations) {
                stmt.setInt(1, invitation.getEventId());
                stmt.setInt(2, invitation.getUser().getId());
                stmt.setString(3, invitation.getUserRole());
                stmt.setString(4, invitation.getUserResponse());
                stmt.setInt(5, invitation.getAttendeesCount());
                stmt.setBoolean(6, invitation.isParticipated());
                stmt.addBatch();
            }
            affectedRows = stmt.executeBatch().length;
          //  conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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
            invitationsList = createInvitationsFromRS(rs);
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

    public boolean updateInvitation(EventInvitation invitation) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "UPDATE event_invitation SET user_role = ? , user_response = ?, attendees_count = ?, " +
                    "participated = ? WHERE event_id = ? AND user_id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setString(1, invitation.getUserRole());
            stmt.setString(2, invitation.getUserResponse());
            stmt.setInt(3, invitation.getAttendeesCount());
            stmt.setBoolean(4, invitation.isParticipated());
            stmt.setInt(5, invitation.getEventId());
            stmt.setInt(6, invitation.getUser().getId());
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
            String sqlStr = "DELETE FROM event_invitation WHERE event_id = ? AND  user_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setInt(1, eventId);
            preparedStatement.setInt(2, userId);
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
    private List<EventInvitation> createInvitationsFromRS(ResultSet rs) throws SQLException {
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
                    .setParticipated(rs.getBoolean("participated"));
            invitationsList.add(invitation);
        }
        return invitationsList;
    }

}
