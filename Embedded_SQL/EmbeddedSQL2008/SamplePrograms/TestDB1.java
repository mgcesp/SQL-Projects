
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;

/**
 * Microsoft SQL Server JDBC test program1 Tests the status of database
 * connectivity
 */
public class TestDB1 {
	public TestDB1() throws Exception {
		String url, username, password;

		url = "jdbc:jtds:sqlserver://teachms.cs.fiu.edu:1433";
		//url = "jdbc:jtds:sqlserver://localhost:1433";
		//url = "jdbc:jtds:sqlserver://myserver:1433";
		username = "cgs4366"; // *** Edit this line with database your
		// username
		password = "4366"; // *** Edit this line with your database password

		// Get connection
		DriverManager
				.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
		//java.lang.Class.forName("net.sourceforge.jtds.jdbc.Driver");
		Connection connection = DriverManager.getConnection(url, username,
				password);

		if (connection != null) {
			System.out.println();
			System.out.println("Successfully connected");
			System.out.println();
			// Meta data
			DatabaseMetaData meta = connection.getMetaData();
			System.out.println("\nDriver Information");
			System.out.println("Driver Name: " + meta.getDriverName());
			System.out.println("Driver Version: " + meta.getDriverVersion());
			System.out.println("\nDatabase Information ");
			System.out.println("Database Name: "
					+ meta.getDatabaseProductName());
			System.out.println("Database Version: "
					+ meta.getDatabaseProductVersion());
		}
	}

	// Test
	public static void main(String args[]) throws Exception {
		TestDB1 test = new TestDB1();
		System.exit(0);
	}
}
