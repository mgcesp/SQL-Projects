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
  # nested query approach of q4.php
  $querystring = "SELECT mng.fname, mng.minit, mng.lname, COUNT(d.essn)
					FROM employee mng, dependent d
					WHERE EXISTS
						(SELECT dp.mgrssn 
						 FROM department dp
						 WHERE mng.dno = dp.dnumber AND dp.mgrssn = mng.ssn 
						 AND EXISTS
							(SELECT e.ssn
							 FROM employee e
							 WHERE dp.dname LIKE '$department_name' 
							 AND mng.dno = e.dno AND e.ssn = d.essn))
					GROUP BY mng.fname, mng.minit, mng.lname";
  
  $q = $d->query($querystring);
  print("Department info: <br>");
  if ($r = $q->numRows() < 1){  
	print("Invalid department name $department_name <br>");
	}
  else{
		while ($r = $q->fetchRow())
			print("&nbsp; &nbsp; &nbsp; &nbsp; <br> Department: $department_name <br> 
													 Manager: $r[0] $r[1] $r[2] <br> 
													 Dependent(s): $r[3] <br>");
	print("<br>");
  }
}
?>