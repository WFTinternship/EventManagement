package datasource;

import com.workfront.internship.event_management.DAO.DataSourceManager;
import com.workfront.internship.event_management.DAO.MediaDAO;
import com.workfront.internship.event_management.DAO.MediaDAOImpl;
import com.workfront.internship.event_management.model.Media;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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

    private DataSourceManager dataSourceManager;
    private MediaDAO mediaDAO;

    //@SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {

        dataSourceManager = Mockito.mock(DataSourceManager.class);
        Connection connection = Mockito.mock(Connection.class);

        when(dataSourceManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);
        when(connection.prepareStatement(any(String.class))).thenThrow(SQLException.class);

        mediaDAO = new MediaDAOImpl(dataSourceManager);
    }


    @Test(expected = RuntimeException.class)
    public void addMedia_dbError() {
        mediaDAO.addMedia(new Media());
    }

    @Test(expected = RuntimeException.class)
    public void getMediaById_dbError() {
        mediaDAO.getMediaById(1);
    }

    @Test(expected = RuntimeException.class)
    public void getMediaByEventId_dbError() {
        mediaDAO.getMediaByEventId(1);

    }

    @Test(expected = RuntimeException.class)
    public void getMediaByType_dbError() {
        mediaDAO.getMediaByType("Image");

    }

    @Test(expected = RuntimeException.class)
    public void getMediaByUploaderId_dbError() {
        mediaDAO.getMediaByUploaderId(1);
    }

    @Test(expected = RuntimeException.class)
    public void getAllMedia_dbError() {
        mediaDAO.getAllMedia();
    }

    @Test(expected = RuntimeException.class)
    public void updateMediaDescription_dbError() {
        mediaDAO.updateMediaDescription(1, "description");
    }

    @Test(expected = RuntimeException.class)
    public void deleteMedia_dbError() {
        mediaDAO.deleteMedia(1);
    }

    @Test(expected = RuntimeException.class)
    public void deleteAllMedia_dbError() {
        mediaDAO.deleteAllMedia();

    }

}
