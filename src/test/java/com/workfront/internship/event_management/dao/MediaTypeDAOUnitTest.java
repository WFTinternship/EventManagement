package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.ObjectNotFoundException;
import com.workfront.internship.event_management.model.MediaType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
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
 * Created by Hermine Turshujyan 7/21/16.
 */
public class MediaTypeDAOUnitTest {

    private static DataSource dataSource;
    private static Connection connection;
    private static MediaTypeDAO mediaTypeDAO;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setUpClass() throws Exception {

        dataSource = Mockito.mock(DataSource.class);
        connection = Mockito.mock(Connection.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);
        when(connection.prepareStatement(any(String.class))).thenThrow(SQLException.class);

        mediaTypeDAO = new MediaTypeDAOImpl();
        Whitebox.setInternalState(mediaTypeDAO, "dataSource", dataSource);

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        mediaTypeDAO = null;
        dataSource = null;
        connection = null;
    }

    @Test(expected = DAOException.class)
    public void addMediaType_dbError() throws DAOException, DuplicateEntryException {
        mediaTypeDAO.addMediaType(new MediaType());
    }

    @Test(expected = DAOException.class)
    public void getAllMediaTypes_dbError() throws DAOException {
        mediaTypeDAO.getAllMediaTypes();
    }

    @Test(expected = DAOException.class)
    public void getMediaTypeById_dbError() throws DAOException, ObjectNotFoundException {
        mediaTypeDAO.getMediaTypeById(1);
    }

    @Test(expected = DAOException.class)
    public void updateMediaType_dbError() throws DAOException, DuplicateEntryException, ObjectNotFoundException {
        mediaTypeDAO.updateMediaType(new MediaType());
    }

    @Test(expected = DAOException.class)
    public void deleteMediaType_dbError() throws DAOException, ObjectNotFoundException {
        mediaTypeDAO.deleteMediaType(1);
    }

    @Test(expected = DAOException.class)
    public void deleteAllMediaTypes_dbError() throws DAOException {
        mediaTypeDAO.deleteAllMediaTypes();
    }
}
