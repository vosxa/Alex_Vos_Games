package be.belfius.Vos_Alex_Games;

import org.dbunit.DatabaseUnitException;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.csv.CsvDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.*;

import be.belfius.Vos_Alex_Games.services.Borrower;
import be.belfius.Vos_Alex_Games.util.TestDatabase;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.junit.Assert.fail;

public class VosAlexGamesDbTest {
    private static DatabaseConnection connection;
    private IDataSet dataSetA;
    private IDataSet dataSetB;
    private DefaultDataSet dataSet = new DefaultDataSet();
    private Borrower borrower = new Borrower();
    private static JdbcDatabaseTester jdbcDatabaseTester;

    @BeforeClass
    public static void createConnection() throws SQLException, DatabaseUnitException, ClassNotFoundException {
        Connection jdbcConnection = new TestDatabase().createConnection();
        DatabaseMetaData metaData = jdbcConnection.getMetaData();
        connection = new DatabaseConnection(jdbcConnection);
        jdbcDatabaseTester = new TestDatabase().createIDatabaseTesterConnection();
        jdbcDatabaseTester.setSetUpOperation(DatabaseOperation.UPDATE);
        jdbcDatabaseTester.setTearDownOperation(DatabaseOperation.CLEAN_INSERT);
    }

    @Before
    public void setup() throws Exception {
        URL url = getClass().getClassLoader().getResource("data");

        IDataSet csvDataSet = new CsvDataSet(new File(url.toURI()));
        DatabaseOperation.REFRESH.execute(connection, csvDataSet);
        dataSetA = connection.createDataSet(new String[]{"Animal"});
        jdbcDatabaseTester.setDataSet(dataSetA);
        jdbcDatabaseTester.onSetup();
    }

    @After
    public void after() throws Exception {
        jdbcDatabaseTester.onTearDown();
    }

//    @Test
//    public void assertDeleteAnimals() throws DatabaseUnitException, MalformedURLException, SQLException {
//        ITable animalTable = dataSetA.getTable("Animal");
//        Assert.assertEquals(1, animalTable.getValue(0, "id"));
//        animalService.deleteAnimal(new Animal.Builder(AnimalType.BEAR).withId(1).build());
//        Assert.assertNotEquals(1, animalTable.getValue(0, "id"));
//    }

    @Test
    public void dbUnitDatabaseTester() {
    }

}
