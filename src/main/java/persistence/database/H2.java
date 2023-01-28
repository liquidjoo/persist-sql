package persistence.database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.logging.Logger;

public class H2 implements Database {

    private static final Logger LOGGER = Logger.getLogger(H2.class.getName());

    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:~/test";

    private static final String USER = "sa";
    private static final String PASS = "";

    private DataSource dataSource;

    public H2(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public H2() {
    }

    @Override
    public void execute(String sql) {
        Connection conn = null;
        Statement stmt = null;
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            if (Objects.nonNull(dataSource)) {
                conn = dataSource.getConnection();
            }

            //STEP 3: Execute a io.github.liquidjoo.simplehiberante.query
            System.out.println("Creating table in given database...");
            stmt = conn.createStatement();

            stmt.executeUpdate(sql);
            System.out.println("Created table in given database...");

            // STEP 4: Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            } // nothing we can do
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            } //end finally try
        } //end try
        System.out.println("Goodbye!");
    }

}
