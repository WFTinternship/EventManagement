package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventInvitation;
import com.workfront.internship.event_management.model.User;

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

    @Override
    public int addInvitation(EventInvitation invitation) {

        Connection conn = null;
        int id = 0;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //insert invitation and get generated id
            id = addInvitation(invitation, conn);
        } catch (SQLException | IOException e) {
            logger.error("Exception ", e);
        } finally {
            closeResources(conn);
        }
        return id;
    }

    @Override
    public int addInvitation(EventInvitation invitation, Connection conn) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        int id = 0;
        String sqlStr = "INSERT INTO event_invitation "
                + "(event_id, user_id, user_role, user_response, attendees_count, participated) VALUES "
                + "(?, ?, ?, ?, ?, ? )";

        try {
            //create and initialize statement
            stmt = conn.prepareStatement(sqlStr, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, invitation.getEventId());
            stmt.setInt(2, invitation.getUser().getId());
            stmt.setString(3, invitation.getUserRole());
            stmt.setString(4, invitation.getUserResponse());
            stmt.setInt(5, invitation.getAttendeesCount());
            stmt.setBoolean(6, invitation.isParticipated());

            //execute query
            stmt.executeUpdate();

            //get generated id
            id = getInsertedId(stmt);

        } catch (SQLException e) {
            logger.error("Exception ", e);
            throw new RuntimeException(e);
        } finally {
            closeResources(rs, stmt);
        }
        return id;
    }

    @Override
    public EventInvitation getInvitationById(int invitationId) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        EventInvitation invitation = null;
        String sqlStr = "SELECT * FROM event_invitation LEFT JOIN user " +
                "ON event_invitation.user_id = user.id " +
                "WHERE event_invitation.id = ?";

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, invitationId);

            //execute query
            rs = stmt.executeQuery();

            //get results
            List<EventInvitation> invitationList = createInvitationsFromRS(rs);
            if (!invitationList.isEmpty()) {
                invitation = invitationList.get(0);
            }
        } catch (SQLException | IOException e) {
            logger.error("Exception ", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return invitation;
    }

    @Override
    public List<EventInvitation> getInvitationsByEventId(int eventId) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<EventInvitation> invitationsList = null;
        String sqlStr = "SELECT * FROM event_invitation LEFT JOIN user " +
                "ON event_invitation.user_id = user.id " +
                "WHERE event_invitation.event_id = ?";

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, eventId);

            //execute query
            rs = stmt.executeQuery();

            //get results
            invitationsList = createInvitationsFromRS(rs);
        } catch (SQLException | IOException e) {
            logger.error("Exception ", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return invitationsList;
    }

    @Override
    public List<EventInvitation> getInvitationsByUserId(int userId) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<EventInvitation> invitationsList = null;
        String sqlStr = "SELECT * FROM event_invitation LEFT JOIN user " +
                "ON event_invitation.user_id = user.id " +
                "WHERE event_invitation.user_id = ?";

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, userId);

            //execute query
            rs = stmt.executeQuery();

            //get results
            invitationsList = createInvitationsFromRS(rs);

        } catch (SQLException | IOException e) {
            logger.error("Exception ", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return invitationsList;
    }

    @Override
    public boolean updateInvitation(EventInvitation invitation) {

        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;
        String sqlStr = "UPDATE event_invitation SET user_role = ? , user_response = ?, attendees_count = ?, " +
                    "participated = ? WHERE id = ?";

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(sqlStr);
            stmt.setString(1, invitation.getUserRole());
            stmt.setString(2, invitation.getUserResponse());
            stmt.setInt(3, invitation.getAttendeesCount());
            stmt.setBoolean(4, invitation.isParticipated());
            stmt.setInt(5, invitation.getId());

            //execute query
            affectedRows = stmt.executeUpdate();

        } catch (SQLException | IOException e) {
            logger.error("Exception ", e);
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;
    }

    @Override
    public boolean deleteInvitation(int invitationId) {
        return deleteRecordById("event_invitation", invitationId);
    }

    @Override
    public boolean deleteInvitationsByEventId(int eventId){
        return deleteRecord("event_invitation", "event_id", eventId);
    }

    @Override
    public boolean deleteInvitationsByUserId(int userId) {
        return deleteRecord("event_invitation", "user_id", userId);
    }

    @Override
    public boolean deleteAllInvitations() {
        return deleteAllRecords("event_invitation");
    }

    //helper methods
    private List<EventInvitation> createInvitationsFromRS(ResultSet rs) throws SQLException {

        List<EventInvitation> invitationsList = new ArrayList<EventInvitation>();

        while (rs.next()) {

            User user = new User();
            user.setId(rs.getInt("user.id"))
                    .setFirstName(rs.getString("first_name"))
                    .setLastName(rs.getString("last_name"))
                    .setUsername(rs.getString("username"))
                    .setUsername(rs.getString("password"))
                    .setEmail(rs.getString("email"))
                    .setAvatarPath(rs.getString("avatar_path"))
                    .setPhoneNumber(rs.getString("phone_number"))
                    .setVerified(rs.getBoolean("verified"))
                    .setRegistrationDate(rs.getTimestamp("registration_date"));

            EventInvitation invitation = new EventInvitation();
            invitation.setUser(user)
                    .setId(rs.getInt("event_invitation.id"))
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
