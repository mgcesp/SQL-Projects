(:
  For each project, output
  a. project name
  b. name of the controlling department
  c. firstname, lastname of the manager of the department
  d. total number of employee-work assignments (number of worker-project assignments from works_on) for the project
:)

<q4_results>

{
  for $p in doc("../company/project.xml")//project,
      $d in doc("../company/department.xml")//department,
      $m in doc("../company/employee.xml")//employee

  let $w := doc("../company/works_on.xml")//works_on[pno = $p/pnumber]

  where $d/dnumber = $p/dnum and $d/mgrssn = $m/ssn
  return
    <ans
      projectname = "{ $p/pname }"
      department = "{ $d/dname }"
      mngr_firstname = "{ $m/fname }"
      mngr_lastname = "{ $m/lname }"
      projectAssignments = "{ count($w//pno) }"
    />
}

</q4_results>








