/*
Outerloop query:

  SELECT fname,lname, ssn 
  FROM employee, department 
  WHERE dname = ? and dno =dnumber

  Innerloop query:
    SELECT dependent_name
    FROM dependent
    WHERE essn = ?

In static non-procedural SQL format, the above is equivalent to
  the following query:
    SELECT dependent_name
    FROM dependent
    WHERE essn IN  
        (SELECT ssn 
         FROM employee, department 
         WHERE dname = ? and dno =dnumber)
  OR
*/

    SELECT e.fname, e.lname, dd.dependent_name
    FROM dependent dd, employee e, department dp
    WHERE dp.dname = 'Research'
      and dp.dnumber = e.dno
      and e.ssn = dd.essn;

/* 
  In embedded query, the user can receive a selected output
   by specifying the parameters (such as department name)
     interactively through a host language program
  rather than receiving all output in a big table.
*/
