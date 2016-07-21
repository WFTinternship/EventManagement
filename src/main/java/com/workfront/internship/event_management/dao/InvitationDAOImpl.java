package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.model.Invitation;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.model.UserResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hermine Turshujyan 7/8/16.
 */
public class InvitationDAOImpl extends GenericDAO implements InvitationDAO {

    private DataSourceManager dataSourceManager;

    public InvitationDAOImpl(DataSourceManager dataSourceManager) throws Exception {
        super(dataSourceManager);
        this.dataSourceManager = dataSourceManager;
    }

    public InvitationDAOImpl() {
        try {
            this.dataSourceManager = DataSourceManager.getInstance();
        } catch (IOException | SQLException e) {
            LOGGER.error("Exception...", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public int addInvitation(Invitation invitation) {

        Connection conn = null;
        int id = 0;

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //insert invitation and get generated id
            id = addInvitation(invitation, conn);
        } catch (SQLException e) {
            LOGGER.error("Exception ", e);
            throw new RuntimeException(e);
        } finally {
            closeResources(conn);
        }
        return id;
    }

    int addInvitation(Invitation invitation, Connection conn) {

        PreparedStatement stmt = null;

        int id = 0;
        String sqlStr = "INSERT INTO event_invitation "
                + "(event_id, user_id, user_role, user_response_id, attendees_count, participated) VALUES "
                + "(?, ?, ?, ?, ?, ? )";

        try {
            //create and initialize statement
            stmt = conn.prepareStatement(sqlStr, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, invitation.getEventId());

            if (invitation.getUser() != null) {
                stmt.setInt(2, invitation.getUser().getId());
            } else {
                stmt.setInt(2, 0);
            }
            stmt.setString(3, invitation.getUserRole());

            if (invitation.getUserResponse() != null) {
                stmt.setInt(4, invitation.getUserResponse().getId());
            } else {
                stmt.setInt(4, 0);
            }

            stmt.setInt(5, invitation.getAttendeesCount());
            stmt.setBoolean(6, invitation.isParticipated());

            //execute query
            stmt.executeUpdate();

            //get generated id
            id = getInsertedId(stmt);

        } catch (SQLException e) {
            LOGGER.error("Exception ", e);
            throw new RuntimeException(e);
        } finally {
            closeResources(stmt);
        }
        return id;
    }

    @Override
    public Invitation getInvitationById(int invitationId) {
        Invitation invitation = null;

        List<Invitation> invitationList = getInvitationsByField("event_invitation.id", invitationId);
        if (invitationList != null && !invitationList.isEmpty()) {
            invitation = invitationList.get(0);
        }
        return invitation;
    }

    @Override
    public List<Invitation> getAllInvitations() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Invitation> invitationsList = new ArrayList<>();
        String sqlStr = "SELECT * FROM event_invitation LEFT JOIN user " +
                "ON event_invitation.user_id = user.id ";
        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(sqlStr);

            //execute query
            rs = stmt.executeQuery();

            //get results
            invitationsList = createInvitationsFromRS(rs);
        } catch (SQLException e) {
            LOGGER.error("Exception ", e);
            throw new RuntimeException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return invitationsList;
    }

    @Override
    public List<Invitation> getInvitationsByEventId(int eventId) {
        return getInvitationsByField("event_id", eventId);
    }

    @Override
    public List<Invitation> getInvitationsByUserId(int userId) {
        return getInvitationsByField("user_id", userId);
    }

    @Override
    public boolean updateInvitation(Invitation invitation) {

        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;
        String sqlStr = "UPDATE event_invitation SET user_role = ? , user_response_id = ?, attendees_count = ?, " +
                    "participated = ? WHERE id = ?";

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(sqlStr);
            stmt.setString(1, invitation.getUserRole());
            stmt.setInt(2, invitation.getUserResponse().getId());
            stmt.setInt(3, invitation.getAttendeesCount());
            stmt.setBoolean(4, invitation.isParticipated());
            stmt.setInt(5, invitation.getId());

            //execute query
            affectedRows = stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error("Exception ", e);
            throw new RuntimeException(e);
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
    private List<Invitation> getInvitationsByField(String columnName, int id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Invitation> invitationsList = new ArrayList<>();
        String sqlStr = "SELECT * FROM event_invitation " +
                "LEFT JOIN user ON event_invitation.user_id = user.id " +
                "LEFT JOIN user_response ON event_invitation.user_response_id = user_response.id" +
                "WHERE " + columnName + " = ?";

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, id);

            //execute query
            rs = stmt.executeQuery();

            //get results
            invitationsList = createInvitationsFromRS(rs);

        } catch (SQLException e) {
            LOGGER.error("Exception ", e);
            throw new RuntimeException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return invitationsList;

    }

    private List<Invitation> createInvitationsFromRS(ResultSet rs) throws SQLException {

        List<Invitation> invitationsList = new ArrayList<>();

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

            UserResponse userResponse = new UserResponse(rs.getInt("user_response.id"), rs.getString("user_response.title"));

            Invitation invitation = new Invitation();
            invitation.setUser(user)
                    .setId(rs.getInt("event_invitation.id"))
                    .setEventId(rs.getInt("event_id"))
                    .setAttendeesCount(rs.getInt("attendees_count"))
                    .setUserResponse(userResponse)
                    .setUserRole(rs.getString("user_role"))
                    .setParticipated(rs.getBoolean("participated"));

            invitationsList.add(invitation);
        }
        return invitationsList;
    }

}
