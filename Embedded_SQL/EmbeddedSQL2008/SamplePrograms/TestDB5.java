
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLWarning;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 * Microsoft SQL Server JDBC test program5 Demonstrates a nested cursor loop
 * with dynamic sql queries Retrieves dependent names of employees who work for
 * a specific department (department name) from the user given database
 */
public class TestDB5 {
	static private boolean debug = true;

	public TestDB5() throws Exception {
		String url, databaseName, username, password, inpdname;
		// Get user password without echoing on the display
		JPasswordField pass = new JPasswordField();
		JOptionPane.showMessageDialog(null, pass, "Enter SQL Password",
				JOptionPane.INFORMATION_MESSAGE);
		password = new String(pass.getPassword());

		Scanner console = new Scanner(System.in);

		System.out.print("\nEnter database name: ");
		databaseName = console.nextLine();
		url = "jdbc:jtds:sqlserver://teachms.cs.fiu.edu:1433/" + databaseName;
		// url = "jdbc:jtds:sqlserver://myserver:1433/" + databaseName;
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
			String outerQuery = "SELECT fname,lname, ssn "
					+ "FROM employee, department "
					+ "WHERE dname = ? and dno =dnumber";
			PreparedStatement stmt1 = connection.prepareStatement(outerQuery);
			String innerQuery = "SELECT dependent_name FROM dependent WHERE essn = ?";
			PreparedStatement stmt2 = connection.prepareStatement(innerQuery);

			System.out
					.print("\nEnter a department name (Research/Administration/Headquarters): ");
			inpdname = console.nextLine();

			stmt1.clearParameters();
			stmt1.setString(1, inpdname); // replaces the first argument (?)
										  // with inpdname value
			//if (debug) // stmt1 is a PreparedStatement object and cannot be
			// displayed
			//  System.out.println("SQL was:\n "+stmt1.toString());
			ResultSet rs1 = stmt1.executeQuery();
			System.out.println("\nOuter loop output:");
			while (rs1.next()) {
				String FirstName = rs1.getString(1);
				String LastName = rs1.getString(2);
				String EmpSSN = rs1.getString(3);

				SQLWarning warning = stmt1.getWarnings();
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
				System.out.println("  " + FirstName + " " + LastName + " "
						+ EmpSSN);
				stmt2.clearParameters();
				stmt2.setString(1, EmpSSN); // replaces the first argument (?)
											// with EmpSSN value
				ResultSet rs2 = stmt2.executeQuery();
				System.out.println("    Inner loop output:");
				while (rs2.next()) {
					String dependentName = rs2.getString(1);
					warning = stmt2.getWarnings();
					if (warning != null) {
						System.out.println("\n---Warning---\n");
						while (warning != null) {
							System.out.println("Message: "
									+ warning.getMessage());
							System.out.println("SQLState: "
									+ warning.getSQLState());
							System.out.print("Vendor error code: ");
							System.out.println(warning.getErrorCode());
							System.out.println("");
							warning = warning.getNextWarning();
						}
					}
					System.out.println("      " + dependentName);
				}

			}
			stmt2.close();
			stmt1.close();
			connection.close();
		}
	}

	// Test
	public static void main(String args[]) throws Exception {
		TestDB5 test = new TestDB5();
		System.exit(0);
	}
}

