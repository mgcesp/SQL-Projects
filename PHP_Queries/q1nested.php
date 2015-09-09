<?php
# php runtime errors can be seen at http://dev.cis.fiu.edu/....
# Illustrates a dynamic query execution with parameters using MDB2

print ("<br>");
$user = $_POST['user'];
$passwd = $_POST['passwd'];
$host = $_POST['host'];
$dbname = $_POST['dbname'];
$dependent_name = $_POST['dependent_name'];

if (!($user && $passwd && $host && $dbname && $dependent_name)) {
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
    if (! $dependent_name) {
       $dependent_namemsg = 'Please enter employees dependent name to search';
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
 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 
 <font color= 'red'>$dependent_namemsg</font><br>
 Employee dependent_name: <input type="text" name="dependent_name" size="15" value="$dependent_name">
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
  
  # Select the employee's name related with his/her dependent and manager's name
  # nested query approach of q1.php
  $querystring = "SELECT DISTINCT e1.fname, e1.minit, e1.lname,
					e2.fname, e2.minit, e2.lname	
					FROM employee e1, employee e2
					WHERE e1.dno = e2.dno 
						AND EXISTS
							(SELECT d.essn
							 FROM dependent d
							 WHERE e1.ssn = d.essn AND EXISTS
								(SELECT dp.mgrssn, dp.dnumber
									 FROM department dp
									 WHERE d.dependent_name LIKE 'Alice' 
									 AND dp.mgrssn = e2.ssn AND e2.dno = dp.dnumber))";
  $q = $d->query($querystring);
  print("Employee's dependent name(s): <br>");
  if ($r = $q->numRows() < 1){  
	print("Invalid dependent name $dependent_name <br>");
	}
  else{
	while($r = $q->fetchRow()){ 
		print(" &nbsp; &nbsp; &nbsp; &nbsp; <br> Dependent: $dependent_name <br>
												 Employee: $r[0] $r[1] $r[2] <br> 
												 Manager: $r[3] $r[4] $r[5] <br>");
	print("<br>");	
	}
 }
}
?>
