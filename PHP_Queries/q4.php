<?php
# php runtime errors can be seen at http://dev.cis.fiu.edu/....
# Illustrates a dynamic query execution with parameters using MDB2

print ("<br>");
$user = $_POST['user'];
$passwd = $_POST['passwd'];
$host = $_POST['host'];
$dbname = $_POST['dbname'];
$department_name = $_POST['department_name'];

if (!($user && $passwd && $host && $dbname && $department_name)) {
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
    if (! $department_name) {
       $department_namemsg = 'Please enter department name to search';
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
 <font color= 'red'>$department_namemsg</font><br>
 Employee department_name: <input type="text" name="department_name" size="15" value="$department_name">
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
  # Select manager's department number and manager's name
  $querystring_1 = "SELECT mng.dno, mng.fname, mng.minit, mng.lname
					FROM employee mng
					WHERE mng.ssn IN
						(SELECT d.mgrssn
						 FROM department d
						 WHERE d.dname LIKE '$department_name' AND mng.ssn = d.mgrssn)";
  
  $q1 = $d->query($querystring_1);
  print("Department info: <br>");
  if ($r1 = $q1->numRows() < 1){  
	print("Invalid department name $department_name <br>");
	}
  else{
	$r1 = $q1->fetchRow();
	$dno = $r1[0];
	# Compute the number of dependent(s) of all employees who work for the department
	$querystring_2 = "SELECT COUNT(d.essn) AS dependents
						FROM dependent d
						WHERE d.essn IN
							(SELECT e.ssn
							 FROM employee e
							 WHERE e.dno LIKE '$dno' AND e.ssn = d.essn)";
	$q2 = $d->query($querystring_2);
	if ($r2 = $q2->numRows() < 1){  
	print("Connection problem in database $dbname $<br>");
	}
	else{
		while ($r2 = $q2->fetchRow())
			print(" &nbsp; &nbsp; &nbsp; &nbsp; <br> Department: $department_name <br> 
													 Manager: $r1[1] $r1[2] $r1[3] <br> 
													 Dependent(s): $r2[0] <br>");
	print("<br>");
	}
  }
}
?>