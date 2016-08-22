package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.model.Media;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * Created by Hermine Turshujyan 7/18/16.
 */
public class MediaDAOUnitTest {

    private DataSource dataSource;
    private MediaDAO mediaDAO;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {

        dataSource = Mockito.mock(DataSource.class);
        Connection connection = Mockito.mock(Connection.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);
        when(connection.prepareStatement(any(String.class))).thenThrow(SQLException.class);

        mediaDAO = new MediaDAOImpl();
        Whitebox.setInternalState(mediaDAO, "dataSource", dataSource);

    }


    @Test(expected = DAOException.class)
    public void addMedia_dbError() throws DuplicateEntryException, DAOException {
        mediaDAO.addMedia(new Media());
    }

    @Test(expected = DAOException.class)
    public void getMediaById_dbError() throws DAOException, ObjectNotFoundException {
        mediaDAO.getMediaById(1);
    }

    @Test(expected = DAOException.class)
    public void getMediaByEventId_dbError() throws DAOException {
        mediaDAO.getMediaByEventId(1);

    }

    @Test(expected = DAOException.class)
    public void getMediaByUploaderId_dbError() throws DAOException {
        mediaDAO.getMediaByUploaderId(1);
    }

    @Test(expected = DAOException.class)
    public void getAllMedia_dbError() throws DAOException {
        mediaDAO.getAllMedia();
    }

    @Test(expected = DAOException.class)
    public void updateMediaDescription_dbError() throws ObjectNotFoundException, DAOException {
        mediaDAO.updateMediaDescription(1, "description");
    }

    @Test(expected = DAOException.class)
    public void deleteMedia_dbError() throws DAOException, ObjectNotFoundException {
        mediaDAO.deleteMedia(1);
    }

    @Test(expected = DAOException.class)
    public void deleteAllMedia_dbError() throws DAOException {
        mediaDAO.deleteAllMedia();

    }
}
