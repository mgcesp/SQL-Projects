<?php
# php runtime errors can be seen at http://dev.cis.fiu.edu/....
# Illustrates a dynamic query execution with parameters using MDB2

print ("<br>");
$user = $_POST['user'];
$passwd = $_POST['passwd'];
$host = $_POST['host'];
$dbname = $_POST['dbname'];
$department_num = $_POST['department_num'];

if (!($user && $passwd && $host && $dbname && $department_num)) {
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
    if (! $department_num) {
       $department_numemsg = 'Please enter department number to search';
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
 <font color= 'red'>$department_numemsg</font><br>
 Employee department_num: <input type="text" name="department_num" size="1" value="$department_num">
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
  # For a given department number: name of the department, # of proj(s) controled by dept., total worked hours
  $querystring = "SELECT dp.dname, COUNT(DISTINCT p.pnumber) AS num_projs, SUM(w.hours) AS hours
				  FROM department dp, project p, works_on w
				  WHERE dp.dnumber LIKE '$department_num' AND dp.dnumber = p.dnum AND p.pnumber = w.pno
				  GROUP BY dp.dname";
  $q = $d->query($querystring);
  print("Department number: <br>");
  if ($r = $q->numRows() < 1){  
	print("Invalid department number $department_num <br>");
	}
  else{
	while ($r = $q->fetchRow()) 
		print(" &nbsp; &nbsp; &nbsp; &nbsp; <br> Department:  $r[0] <br> 
												 Number of project(s):  $r[1] <br> 
												 Total hours:  $r[2] <br>");
	print("<br>");
  }
}
?>