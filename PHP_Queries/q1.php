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
  
  # Select the employee's name related with his/her dependent
  $querystring_1 = "SELECT e.ssn, e.dno, e.fname, e.minit, e.lname
					FROM employee e
					WHERE e.ssn IN
						(SELECT d.essn
						 FROM dependent d
						 WHERE d.dependent_name LIKE '$dependent_name' AND d.essn = e.ssn)";
  $q1 = $d->query($querystring_1);
  print("Employee's dependent name(s): <br>");
  if ($r1 = $q1->numRows() < 1){  
	print("Invalid dependent name $dependent_name <br>");
	}								# Select the employee's manager
  else							 	# Since:								
	  while($r1 = $q1->fetchRow()){ # a person can be a dependent of several employees and 									
		$ssn = $r1[0];				# employees with the same dependent can have different managers	
		$dno = $r1[1];				# per each employee find his/her manager
		$querystring_2 = "SELECT mng.fname, mng.minit, mng.lname
							FROM employee mng
							WHERE  mng.ssn IN
								(SELECT d.mgrssn
								 FROM department d
								 WHERE d.mgrssn = mng.ssn AND EXISTS
									(SELECT e.dno
									 FROM employee e
									 WHERE e.ssn LIKE '$ssn' AND d.dnumber LIKE '$dno'))";
		$q2 = $d->query($querystring_2);
		if ($r2 = $q2->numRows() < 1){  
		print("Connection problem in database $dbname $<br>");
		}
		else{
			while($r2 = $q2->fetchRow())# Print query result(S).....	
			print(" &nbsp; &nbsp; &nbsp; &nbsp; <br> Dependent: $dependent_name <br>
													 Employee: $r1[2] $r1[3] $r1[4] <br> 
													 Manager: $r2[0] $r2[1] $r2[2] <br>");
		print("<br>");
		}
	} 
}
?>
