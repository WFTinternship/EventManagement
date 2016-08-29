package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.model.Invitation;
import com.workfront.internship.event_management.model.User;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hermine Turshujyan 7/8/16.
 */
@Component
public class InvitationDAOImpl extends GenericDAO implements InvitationDAO {

    static final Logger LOGGER = Logger.getLogger(InvitationDAOImpl.class);

    @Override
    public int addInvitation(Invitation invitation) {

        Connection conn = null;
        PreparedStatement stmt = null;

        int id = 0;
        String query = "INSERT INTO event_invitation "
                + "(event_id, user_id, user_role, user_response, attendees_count, participated) VALUES "
                + "(?, ?, ?, ?, ?, ? )";
        try {
            //get connection
            conn = dataSource.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, invitation.getEventId());
            if (invitation.getUser() != null) {
                stmt.setInt(2, invitation.getUser().getId());
            } else {
                stmt.setInt(2, 0);
            }
            stmt.setString(3, invitation.getUserRole());
            stmt.setString(4, invitation.getUserResponse());

            stmt.setInt(5, invitation.getAttendeesCount());
            stmt.setBoolean(6, invitation.isParticipated());

            //execute query
            stmt.executeUpdate();

            id = getInsertedId(stmt);

        } catch (SQLIntegrityConstraintViolationException e) {
            LOGGER.error("Duplicate invitation entry", e);
            throw new DuplicateEntryException("Invitation for user with id "
                    + invitation.getUser().getId() + " and event with id " + invitation.getEventId() + " already exists", e);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(stmt, conn);
        }
        return id;
    }

    @Override
    public void addInvitations(List<Invitation> invitationList) throws DAOException, DuplicateEntryException {

        Connection conn = null;
        PreparedStatement stmt = null;

        int id = 0;
        String query = "INSERT INTO event_invitation "
                + "(event_id, user_id, user_role, user_response, attendees_count, participated) VALUES "
                + "(?, ?, ?, ?, ?, ? )";
        try {
            //get connection
            conn = dataSource.getConnection();

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
                    stmt.setString(4, invitation.getUserResponse());
                } else {
                    stmt.setInt(4, 0);
                }

                stmt.setInt(5, invitation.getAttendeesCount());
                stmt.setBoolean(6, invitation.isParticipated());

                stmt.addBatch();
            }
            //execute query
            stmt.executeBatch();
        } catch (SQLIntegrityConstraintViolationException e) {
            LOGGER.error("Duplicate invitation entry", e);
            throw new DuplicateEntryException("Invitation already exists", e);
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
            conn = dataSource.getConnection();

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
    public boolean updateInvitation(Invitation invitation) throws ObjectNotFoundException, DuplicateEntryException, DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        String sqlStr = "UPDATE event_invitation SET user_role = ? , user_response = ?, attendees_count = ?, " +
                "participated = ? WHERE id = ?";

        try {
            //get connection
            conn = dataSource.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(sqlStr);
            stmt.setString(1, invitation.getUserRole());
            stmt.setString(2, invitation.getUserResponse());
            stmt.setInt(3, invitation.getAttendeesCount());
            stmt.setBoolean(4, invitation.isParticipated());
            stmt.setInt(5, invitation.getId());

            //execute query
            int affectedRows = stmt.executeUpdate();
            if (affectedRows != 0) {
                success = true;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            LOGGER.error("Duplicate invitation entry", e);
            throw new DuplicateEntryException("Invitation for user with id "
                    + invitation.getUser().getId() + " and event with id " + invitation.getEventId() + " already exists", e);
        } catch (SQLException e) {
            LOGGER.error("SQL Exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(stmt, conn);
        }
        return success;
    }

    @Override
    public boolean deleteInvitation(int invitationId) throws DAOException, ObjectNotFoundException {
        return deleteRecordById("event_invitation", invitationId);
    }

    @Override
    public void deleteInvitationsByEventId(int eventId) throws DAOException {
        try {
            deleteRecord("event_invitation", "event_id", eventId);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(eventId + " not found", e);
        }
    }

    @Override
    public void deleteInvitationsByUserId(int userId) throws DAOException {
        try {
            deleteRecord("event_invitation", "user_id", userId);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(userId + " not found", e);
        }
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
                "WHERE " + columnName + " = ?";
        try {
            //get connection
            conn = dataSource.getConnection();

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


            Invitation invitation = new Invitation();
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
