
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 * Microsoft SQL Server JDBC test program3 Demonstrates dynamic sql query (fixed
 * string) Displays Employee information from the user given database
 */
public class TestDB3 {
	static private boolean debug = true;

	public TestDB3() throws Exception {
		String url, databaseName, username, password;
		// Get user password without echoing on the display
		JPasswordField pass = new JPasswordField();
		JOptionPane.showMessageDialog(null, pass, "Enter SQL Password",
				JOptionPane.INFORMATION_MESSAGE);
		password = new String(pass.getPassword());

		Scanner console = new Scanner(System.in);

		System.out.print("\nEnter database name: ");
		databaseName = console.nextLine();
		System.out.print("\nEnter SQL username: ");
		username = console.nextLine();
		url = "jdbc:jtds:sqlserver://teachms.cs.fiu.edu:1433/" + databaseName
		       	+ ";user=" + username + ";password=" + password;
		//url = "jdbc:jtds:sqlserver://myserver:1433/" + databaseName
			// + ";user=" + username + ";password=" + password;

		// Get connection
		DriverManager
				.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
		Connection connection = DriverManager.getConnection(url);
		if (connection != null) {
			System.out.println();
			System.out.println("Successfully connected");
			System.out.println();
			String selectString = "SELECT ssn,lname FROM employee";
			Statement stmt = connection.createStatement();
			if (debug)
				System.out.println("SQL statement was:\n   " + selectString
						+ "\n\nOutput is");
			ResultSet rs = stmt.executeQuery(selectString);
			while (rs.next()) {
				String EmpSSN = rs.getString(1);
				String LastName = rs.getString(2);

				SQLWarning warning = stmt.getWarnings();
				if (warning != null) {
					System.out.println("\n---Warning---\n");
					while (warning != null) {
						System.out.println("Message: " + warning.getMessage());
						System.out
								.println("SQLState: " + warning.getSQLState());
						System.out.print("Vendor error code: ");
						System.out.println(warning.getErrorCode());
						System.out.println("");
						warning = warning.getNextWarning();
					}
				}
				System.out.println(EmpSSN + " - " + LastName);
			}
			stmt.close();
			connection.close();
		}
	}

	// Test
	public static void main(String args[]) throws Exception {
		TestDB3 test = new TestDB3();
		System.exit(0);
	}
}
