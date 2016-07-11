import com.workfront.internship.event_management.datasource.DataSourceManager;
import com.workfront.internship.event_management.datasource.RecurrenceTypeDAO;
import com.workfront.internship.event_management.datasource.RecurrenceTypeDAOImpl;
import com.workfront.internship.event_management.model.datehelpers.RecurrenceType;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by hermine on 7/11/16.
 */
public class TestRecurrenceTypeDAOImpl {
    private static RecurrenceTypeDAO recTypeDAO = null;
    RecurrenceType testRecurrenceType = null;
    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet rs;

    @BeforeClass
    public static void setUpClass(){
        recTypeDAO = new RecurrenceTypeDAOImpl();
    }

    @Before
    public void setUp() {
      //  testRecurrenceType =
        try{
            conn = DataSourceManager.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        testRecurrenceType = null;
            TestHelper.closeResources(rs, stmt, conn);

    }

}
