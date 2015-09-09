
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 * Microsoft SQL Server JDBC test program2 Interactively accepts SQL username &
 * password
 */
public class TestDB2 {
	public TestDB2() throws Exception {
		String url, username, password;
		// Get user password without echoing on the display
		JPasswordField pass = new JPasswordField();
		JOptionPane.showMessageDialog(null, pass, "Enter SQL Password",
				JOptionPane.INFORMATION_MESSAGE);
		password = new String(pass.getPassword());

		Scanner console = new Scanner(System.in);

		url = "jdbc:jtds:sqlserver://teachms.cs.fiu.edu:1433";
		//url = "jdbc:jtds:sqlserver://localhost:1433";
		//url = "jdbc:jtds:sqlserver://myserver:1433";
		System.out.print("\nEnter SQL username: ");
		username = console.nextLine();

		// Get connection
		DriverManager
				.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
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
		TestDB2 test = new TestDB2();
		System.exit(0);
	}
}

