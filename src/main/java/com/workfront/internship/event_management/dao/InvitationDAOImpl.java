package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.model.Invitation;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.model.UserResponse;
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
                + "(event_id, user_id, user_response_id, attendees_count, participated) VALUES "
                + "(?, ?, ?, ?, ? )";
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
            stmt.setInt(3, invitation.getUserResponse().getId());

            stmt.setInt(4, invitation.getAttendeesCount());
            stmt.setBoolean(5, invitation.isParticipated());

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
                + "(event_id, user_id, user_response_id, attendees_count, participated) VALUES "
                + "(?, ?, ?, ?, ? )";
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

                if (invitation.getUserResponse() != null) {
                    stmt.setInt(3, invitation.getUserResponse().getId());
                } else {
                    stmt.setInt(3, 0);
                }

                stmt.setInt(4, invitation.getAttendeesCount());
                stmt.setBoolean(5, invitation.isParticipated());

                stmt.addBatch();
            }
            //execute query
            stmt.executeBatch();
            conn.commit();
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
        if (!invitationList.isEmpty()) {
            return invitationList.get(0);
        } else {
            return null;
        }

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

        String sqlStr = "UPDATE event_invitation SET user_response_id = ?, attendees_count = ?, " +
                "participated = ? WHERE id = ?";

        try {
            //get connection
            conn = dataSource.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, invitation.getUserResponse().getId());
            stmt.setInt(2, invitation.getAttendeesCount());
            stmt.setBoolean(3, invitation.isParticipated());
            stmt.setInt(4, invitation.getId());

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
    public boolean updateInvitationResponse(int eventId, int userId, int responseId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        String sqlStr = "UPDATE event_invitation SET user_response_id = ? " +
            "WHERE event_id = ? AND user_id = ?";

        try {
            //get connection
            conn = dataSource.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, responseId);
            stmt.setInt(2, eventId);
            stmt.setInt(3, userId);

            //execute query
            int affectedRows = stmt.executeUpdate();
            if (affectedRows != 0) {
                success = true;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            LOGGER.error("Duplicate invitation entry", e);
            throw new DuplicateEntryException("Invitation for user with id "
                +userId + " and event with id " + eventId + " already exists", e);
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
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        String query = "DELETE FROM event_invitation WHERE event_id = ? ";
        try {
            //get connection
            conn = dataSource.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, eventId);

            //execute query
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(stmt, conn);
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
                "LEFT JOIN user_response ON event_invitation.user_response_id = user_response.id " +
                "WHERE " + columnName + " = ? AND event_invitation.user_response_id != 4";
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

            UserResponse userResponse = new UserResponse();
            userResponse.setId(rs.getInt("user_response.id"));
            userResponse.setTitle(rs.getString("user_response.title"));

            Invitation invitation = new Invitation();
            invitation.setUser(user)
                    .setId(rs.getInt("event_invitation.id"))
                    .setEventId(rs.getInt("event_id"))
                    .setAttendeesCount(rs.getInt("attendees_count"))
                    .setUserResponse(userResponse)
                    .setParticipated(rs.getBoolean("participated"));

            invitationsList.add(invitation);
        }
        return invitationsList;
    }

}
