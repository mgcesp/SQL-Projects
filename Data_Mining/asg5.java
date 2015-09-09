// COP 4722 - Assignment 5
// Part II

package asg5;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 *
 * @author Manuel Gustavo Cespedes PI# 1730088
 */

public class Asg5 {
    
    public Asg5() throws Exception {
        // login variables
        String url, databaseName, username, password;
        // file and scanner variables
        String fileName, lineInfo;
        // JPasswordField object 
        JPasswordField pass = new JPasswordField();
        // Show dialogbox
        JOptionPane.showMessageDialog(null, pass, "Enter SQL Password", JOptionPane.INFORMATION_MESSAGE);
        // store password from input
        password = new String(pass.getPassword());
        // scanner object
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
        
        // query to delete all rows from table
        String query = "DELETE FROM purchase";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.execute();
        // prepare file and scanner objects
        File file = new File(fileName);
        Scanner data = new Scanner(file);
        // store support value
        String support = data.next();
        System.out.println("Minimum support value: " + support + "\n");
        
        while(data.hasNext()){
            // obtain strings from text file
            String customer = data.next();
            String item = data.next();
            // create insert statement
            String insert = "INSERT INTO purchase (Customer, ItemBought) VALUES('" + customer + "','" + item + "')";
            PreparedStatement insertStmt = connection.prepareStatement(insert);
            insertStmt.execute();            
        }
        // prepare query statements
        int count = 0;
        boolean firstSet = true;
        String select = "";
        String from = "";
        String where = "";
        String group = "";
        String having = "";

        // update query statements
        select = setSelect(count, select, firstSet);
        from = setFrom(count, from, firstSet);
        where = setWhere(count, where, firstSet);
        group = setGroupBy(count, group, firstSet);
        having = setHaving(having, support);
        // build complete query
        String finalQuery = select + "\n" + from + "\n" + where + "\n" + group + "\n" + having;
        System.out.println("Query for L-" + (count+1) + " Itemset: \n" + finalQuery);

        // call complete query
        stmt = connection.prepareStatement(finalQuery);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            // increment count
            count++;
            // print result set
            printResultSet(rs);
            // update query statements
            select = setSelect(count, select, !firstSet);
            from = setFrom(count, from, !firstSet);
            where = setWhere(count, where, !firstSet);
            group = setGroupBy(count, group, !firstSet);
            // build complete query
            finalQuery = select + "\n" + from + "\n" + where + "\n" + group + "\n" + having;
            System.out.println("Query for L-" + (count+1) + " Itemset: \n" + finalQuery);
            // call complete query
            stmt = connection.prepareStatement(finalQuery);
            rs = stmt.executeQuery();
        }
        // close connections
        stmt.close();
        connection.close();
    }
    
    // helper methods
    private String setSelect(int count, String statement, boolean firsSet) {
        if (firsSet) {
            statement = "SELECT DISTINCT COUNT(A" + count + ".Customer),A" + count + ".ItemBought ";
        } else {
            statement += ",A" + count + ".ItemBought ";
        }
        return statement;
    }

    private String  setFrom(int count, String statement, boolean firstSet) {
        if (firstSet) {
            statement += "FROM Purchase A" + count + " ";
        } else {
            statement += ", Purchase A" + count + " ";
        }
        return statement;
    }

    private String  setWhere(int count, String statement, boolean firstSet) {
        if (firstSet) {
            statement = "WHERE 1=1 ";
        } else if (count == 1) {
            statement += "AND A" + (count - 1) + ".ItemBought < A" + count + ".ItemBought ";
            statement += "AND A" + (count - 1) + ".Customer = A" + count + ".Customer ";
        } else {
            statement += "AND A" + (count - 1) + ".ItemBought < A" + count + ".ItemBought ";
            statement += "AND A" + (count - 1) + ".Customer = A" + count + ".Customer ";
        }
        return statement;
    }

    private String  setGroupBy(int count, String statement, boolean firstSet) {
        if (firstSet) {
            statement = "GROUP BY A" + count + ".ItemBought ";
        } else {
            statement += ", A" + count + ".ItemBought ";
        }
        return statement;
    }

    private String  setHaving(String statement, String minimumSupportValue) {

        statement = "HAVING ( CAST(COUNT(A0.Customer) AS FLOAT)/(SELECT DISTINCT COUNT(DISTINCT CUSTOMER) FROM Purchase) ) > " + minimumSupportValue + "\n";
        return statement;
    }
    
    private void printResultSet(ResultSet rs) throws SQLException{
        ResultSetMetaData rsmd = rs.getMetaData();
        int colNumber = rsmd.getColumnCount();
        System.out.println("Result of L-" + (colNumber - 1) + " itemset:");
        do {
            for (int i = 1; i <= colNumber; i++) {
                String colValue = rs.getString(i);
                System.out.print(colValue + " ");
            }
            System.out.println();
        } while (rs.next());   
        
        System.out.println("*****************************************************************");
    }

    public static void main(String[] args) throws Exception {
        Asg5 test = new Asg5();
        System.exit(0);
    }
}