<?php
# php runtime errors can be seen at http://dev.cis.fiu.edu/....
# Illustrates a dynamic query execution with parameters using MDB2

print ("<br>");
$user = $_POST['user'];
$passwd = $_POST['passwd'];
$host = $_POST['host'];
$dbname = $_POST['dbname'];

if (!($user && $passwd && $host && $dbname)) {
  if ($_POST['visited']) {    
    if (! $user) {
       $usermsg = 'Please complete [SQL authentication] database username';
    }
    if (! $passwd) {
       $passmsg = 'Please complete [SQL authentication] database password';
    }
    if (! $host) {
       $hostmsg = 'Please complete [SQL Server] hostname';
    }
    if (! $dbname) {
       $dbnmmsg = 'Please complete [SQL Server] database name';
    }
  }

 // printing the form to enter the user input
 print <<<_HTML_
 <FORM method="POST" action="{$_SERVER['PHP_SELF']}">
 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 
 <font color= 'red'>$usermsg</font><br>
 SQL user name: <input type="text" name="user" size="15" value="$user">
 <br/>
 <br>
 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp; 
 <font color= 'red'>$passmsg</font><br>
 SQL user password: <input type="password" name="passwd" size="15" value="$passwd">
 <br/>
 <br>
 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 
 <font color= 'red'>$hostmsg</font><br>
 SQL server host: <input type="text" name="host" size="15" value="$host">
 <br/>
 <br>
 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 
 <font color= 'red'>$dbnmmsg</font><br>
 Database name: <input type="text" name="dbname" size="15" value="$dbname">
 <br/>
 <br>
 <INPUT type="submit" value=" Submit ">
 <INPUT type="hidden" name="visited" value="true">
 </FORM>
_HTML_;
 
}
else {
  require 'MDB2.php';
  $dbstring= "mssql://$user:$passwd@$host/$dbname";
  $d = MDB2::connect($dbstring);
  if (MDB2::isError($d)) {
    die("cannot connect - " . $d->getMessage());
  }
  $d->setErrorHandling(PEAR_ERROR_DIE);
  # note that text value within a query must be enclosed with single quotes
  $querystring = "SELECT DISTINCT w1.pno, e1.fname +' '+ e1.lname AS emp_A,
				  e2.fname +' '+ e2.lname AS emp_B
				  FROM employee e1, employee e2, works_on w1
				  WHERE e1.ssn = w1.essn AND e1.ssn < e2.ssn AND EXISTS
					(SELECT w2.pno
					 FROM works_on w2
					 WHERE e2.ssn = w2.essn AND w1.pno = w2.pno) 
				  ORDER BY w1.pno";
  $q = $d->query($querystring);
  print("Employees who worked on the same project(s): <br>");
  if($r = $q->numRows() < 1){  
	print("There is any pair of employees who have worked on the same project <br>");
	}
  else{
	echo "<table border='1'>
	<tr>
	<th>Project num</th>
	<th>First employee</th>
	<th>Second employee</th>
	</tr>";
	while ($r = $q->fetchRow()){
		echo "<td>" . $r[0] . "</td>";
		echo "<td>" . $r[1] . "</td>";
		echo "<td>" . $r[2] . "</td>";
		echo "</tr>";
	}
	echo "</table>";
  }
}
?>  