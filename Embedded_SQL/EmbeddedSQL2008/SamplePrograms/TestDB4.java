
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLWarning;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 * Microsoft SQL Server JDBC test program4 Demonstrates dynamic sql query
 * (runtime synthesized string) Displays Employee information from the user
 * specified database
 */
public class TestDB4 {
	static private boolean debug = true;

	public TestDB4() throws Exception {
		String url, databaseName, username, password, inpssn;
		// Get user password without echoing on the display
		JPasswordField pass = new JPasswordField();
		JOptionPane.showMessageDialog(null, pass, "Enter SQL Password",
				JOptionPane.INFORMATION_MESSAGE);
		password = new String(pass.getPassword());

		Scanner console = new Scanner(System.in);

		System.out.print("\nEnter database name: ");
		databaseName = console.nextLine();
		url = "jdbc:jtds:sqlserver://teachms.cs.fiu.edu:1433/" + databaseName;
		//url = "jdbc:jtds:sqlserver://myserver:1433/" + databaseName;
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
			String selectString = "SELECT fname,lname FROM employee WHERE ssn = ?";
			PreparedStatement stmt = connection.prepareStatement(selectString);
			System.out.print("\nEnter a social security number: ");
			inpssn = console.nextLine();
			stmt.clearParameters();
			stmt.setString(1, inpssn); // replaces the first argument (?) with
									   // inpssn value
			//if (debug) // stmt is a PreparedStatement object and cannot be
			// displayed
			//  System.out.println("SQL was:\n "+stmt.toString());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String FirstName = rs.getString(1);
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
				System.out
						.println("\nOutput:\n  " + FirstName + " " + LastName);
			}
			stmt.close();
			connection.close();
		}
	}

	// Test
	public static void main(String args[]) throws Exception {
		TestDB4 test = new TestDB4();
		System.exit(0);
	}
}

