import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
/**
 *
 * @author Manuel Cespedes
 */
public class asg5 {
    
    public asg5() throws Exception {
        // login variables
        String url, databaseName, username, password;
        // file and scanner variables
        String fileName, lineInfo;
        // JPasswordField object 
        JPasswordField pass = new JPasswordField();
        // Show dialogbox
        JOptionPane.showMessageDialog(null, pass, "Enter SQL Password", 
            JOptionPane.INFORMATION_MESSAGE);
        // store password from input
        password = new String(pass.getPassword());
        // new scanner object
        Scanner console = new Scanner(System.in);
        // get the database name from system.in
        System.out.print("\nEnter database name: ");
        databaseName = console.nextLine();
        //url = "jdbc:jtds:sqlserver://teachms.cs.fiu:1433/" + databaseName;
        url = "jdbc:jtds:sqlserver://MANOLO-PC/" + databaseName;
        // get the SQL username form system.in
        System.out.print("\nEnter SQL username: ");
        username = console.nextLine();
        // enter the login info into a connection
        DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
        Connection connection = DriverManager.getConnection(url, username, password);
        // if login info was ok
        if(connection != null){
            System.out.println("\nSuccessfully connected to DB: " + databaseName);   
        }
        else{
            System.out.println("Wrong password!");
            return;
        }
        // get the input file name and store in variable
        System.out.print("\nEnter input file name: ");
        fileName = console.nextLine();
        System.out.println();   
        
        try {
            // create a file from the input txt and then a scanner
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            // obtain the project numbers from the DB
            String query = "SELECT pnumber FROM project"; // prepare sql statement
            PreparedStatement stmt = connection.prepareStatement(query); // create prepared stmt
            ResultSet rs = stmt.executeQuery(); // execute sql statement and return the set
            
            // arraylist to store project numbers
            ArrayList<String> projectList = new ArrayList<>();
            // while the resultset has more rows
            while (rs.next()){
                // get the column value and add to array
                projectList.add(rs.getString(1));
            }
            // System.out.println(list + "\n"); // contents of array
            
            // arraylist to get average of assignments
            ArrayList<Integer> averageList = new ArrayList<>();
            
            System.out.println("Proj#\tProjName\t\tDepartment\t Manager\t  # of assignments");
            System.out.println("-----\t--------\t\t----------\t -------\t  ----------------");
            // while the input text file has more lines
            while (scanner.hasNext()) {
                
                lineInfo = scanner.nextLine(); // get the next line (project number)
                // if project number exists in DB
                if(projectList.contains(lineInfo)){                       
                    // prepare statements for queries
                    String query1 = "SELECT pnumber, pname, dnum FROM project WHERE pnumber = ?";
                    PreparedStatement stmt2 = connection.prepareStatement(query1);
                    String query2 = "SELECT dname, mgrssn FROM department WHERE dnumber = ?";
                    PreparedStatement stmt3 = connection.prepareStatement(query2);
                    String query3 = "SELECT fname, lname FROM employee WHERE ssn = ?";
                    PreparedStatement stmt4 = connection.prepareStatement(query3);
                    String query4 = "SELECT COUNT(pno) FROM works_on WHERE pno = ?";
                    PreparedStatement stmt5 = connection.prepareStatement(query4);
                    // prepare and execute query from project table
                    stmt2.clearParameters();
                    stmt2.setString(1, lineInfo);   // replace argument with project number  
                    ResultSet rs2 = stmt2.executeQuery();
                    rs2.next();
                    String pnumber = rs2.getString(1);
                    String pname = rs2.getString(2);     
                    String deptnum = rs2.getString(3);
                    // prepare and execute query from department table
                    stmt3.clearParameters();
                    stmt3.setString(1,deptnum);     // replace argument with deptnum
                    ResultSet rs3 = stmt3.executeQuery();
                    rs3.next();
                    String deptname = rs3.getString(1);
                    String mngrname = rs3.getString(2);
                    // prepare and execute query from employee table
                    stmt4.clearParameters();
                    stmt4.setString(1, mngrname);   // replace argument with mngrname
                    ResultSet rs4 = stmt4.executeQuery();
                    rs4.next();
                    String firstName = rs4.getString(1);
                    String lastName = rs4.getString(2);
                    // prepare and execute query from works_on table
                    stmt5.clearParameters();
                    stmt5.setString(1,pnumber);     // replace argument with pnumber
                    ResultSet rs5 = stmt5.executeQuery();
                    rs5.next();
                    String numAsg = rs5.getString(1);
                    averageList.add(Integer.parseInt(numAsg));  // add number of assignment to list
                    // print results
                    System.out.printf("%-7s %-23s %-16s %-8s %-8s %5s\n", pnumber, pname, deptname, firstName, lastName, numAsg);  
                }
                else{
                    System.out.println("Invalid input project number: " + lineInfo);
                }
            }
            // variable for assignment average
            double asgAverage = 0;
            // for the assignments in the averageList
            for(int i = 0; i < averageList.size(); i++){
                // get the sum
                asgAverage += averageList.get(i); 
            }
            asgAverage = asgAverage / (averageList.size()); // get average

            System.out.println("\nThe average number of work assignments per valid project is: " + asgAverage);
                
            scanner.close();
        } 
        catch (FileNotFoundException e){
                e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws Exception {
        asg5 test = new asg5();
        System.exit(0);
    }
}
