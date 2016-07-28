package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.model.Invitation;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.model.UserResponse;
import org.apache.log4j.Logger;

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

    static final Logger LOGGER = Logger.getLogger(InvitationDAOImpl.class);
    private DataSourceManager dataSourceManager;

    public InvitationDAOImpl(DataSourceManager dataSourceManager) throws Exception {
        super(dataSourceManager);
        this.dataSourceManager = dataSourceManager;
    }

    public InvitationDAOImpl() throws DAOException {
        try {
            this.dataSourceManager = DataSourceManager.getInstance();
        } catch (IOException | SQLException e) {
            LOGGER.error("Could not instantiate data source manager.", e);
            throw new DAOException("Could not instantiate data source manager.", e);
        }
    }

    @Override
    public int addInvitation(Invitation invitation) throws DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;

        int id = 0;
        String query = "INSERT INTO event_invitation "
                + "(event_id, user_id, user_role, user_response_id, attendees_count, participated) VALUES "
                + "(?, ?, ?, ?, ?, ? )";
        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
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

        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(stmt, conn);
        }
        return id;
    }

    @Override
    public void addInvitations(List<Invitation> invitationList) throws DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;

        int id = 0;
        String query = "INSERT INTO event_invitation "
                + "(event_id, user_id, user_role, user_response_id, attendees_count, participated) VALUES "
                + "(?, ?, ?, ?, ?, ? )";
        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //start transaction
            conn.setAutoCommit(false);

            //create statement
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            //initialize statement
            for (Invitation invitation : invitationList) {

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

                stmt.addBatch();
            }
            //execute query
            stmt.executeBatch();
        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(stmt, conn);
        }
    }

    @Override
    public Invitation getInvitationById(int invitationId) throws ObjectNotFoundException, DAOException {

        List<Invitation> invitationList = getInvitationsByField("event_invitation.id", invitationId);
        if (invitationList.isEmpty()) {
            throw new ObjectNotFoundException("Invitation with id " + invitationId + " not found!");
        }

        return invitationList.get(0);
    }

    @Override
    public List<Invitation> getAllInvitations() throws DAOException {

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
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return invitationsList;
    }

    @Override
    public List<Invitation> getInvitationsByEventId(int eventId) throws DAOException {
        return getInvitationsByField("event_id", eventId);
    }

    @Override
    public List<Invitation> getInvitationsByUserId(int userId) throws DAOException {
        return getInvitationsByField("user_id", userId);
    }

    @Override
    public void updateInvitation(Invitation invitation) throws ObjectNotFoundException {

        Connection conn = null;
        PreparedStatement stmt = null;

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
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new ObjectNotFoundException("Invitation with id " + invitation.getId() + " not found!");
            }
        } catch (SQLException e) {
            LOGGER.error("Exception ", e);
            throw new RuntimeException(e);
        } finally {
            closeResources(stmt, conn);
        }
    }

    @Override
    public void deleteInvitation(int invitationId) throws DAOException, ObjectNotFoundException {
        deleteRecordById("event_invitation", invitationId);
    }

    @Override
    public void deleteInvitationsByEventId(int eventId) throws DAOException, ObjectNotFoundException {
        deleteRecord("event_invitation", "event_id", eventId);
    }

    @Override
    public void deleteInvitationsByUserId(int userId) throws DAOException, ObjectNotFoundException {
        deleteRecord("event_invitation", "user_id", userId);
    }

    @Override
    public void deleteAllInvitations() throws DAOException {
        deleteAllRecords("event_invitation");
    }

    //helper methods
    private List<Invitation> getInvitationsByField(String columnName, int id) throws DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Invitation> invitationsList = new ArrayList<>();
        String sqlStr = "SELECT * FROM event_invitation " +
                "LEFT JOIN user ON event_invitation.user_id = user.id " +
                "LEFT JOIN user_response ON event_invitation.user_response_id = user_response.id " +
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
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
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
                    .setPassword(rs.getString("password"))
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
