import com.workfront.internship.event_management.datasource.RecurrenceTypeDAO;
import com.workfront.internship.event_management.datasource.RecurrenceTypeDAOImpl;
import com.workfront.internship.event_management.model.datehelpers.RecurrenceType;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Created by hermine on 7/11/16.
 */
public class TestRecurrenceTypeDAOImpl {
    private static RecurrenceTypeDAO recTypeDAO = null;
    RecurrenceType testRecurrenceType = null;


    @BeforeClass
    public static void setUpClass(){
        recTypeDAO = new RecurrenceTypeDAOImpl();
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

}
