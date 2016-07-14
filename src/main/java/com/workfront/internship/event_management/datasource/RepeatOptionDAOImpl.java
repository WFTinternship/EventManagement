package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.RepeatOption;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Hermine Turshujyan 7/14/16.
 */
public class RepeatOptionDAOImpl extends  GenericDAO implements RepeatOptionDAO {

    @Override
    public int insertRepeatOption(RepeatOption option) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int id = 0;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            String query = "INSERT INTO repeat_option " +
                    "(recurrence_type_id, title, abbreviation) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, option.getRecurrenceTypeId());
            stmt.setString(2, option.getTitle());
            stmt.setString(3, option.getAbbreviation());

            //execute query
            stmt.executeUpdate();

            //get generated id
            id = getInsertedId(stmt);

        } catch (SQLException | IOException e) {
            logger.error("Exception...", e);
        } finally {
            closeResources(stmt, conn);
        }
        return id;    }

    @Override
    public int insertRepeatOptionsList(List<RepeatOption> options) {
        return 0;
    }

    @Override
    public RepeatOption getRepeatOption(int optionId) {
        return null;
    }

    @Override
    public List<RepeatOption> getRepeatOptionsByRepeatType(int repeatTypeId) {
        return null;
    }

    @Override
    public boolean updateRepeatOption(int optionId) {
        return false;
    }

    @Override
    public boolean deleteRepeatOption(int optionId) {
        return false;
    }

}
