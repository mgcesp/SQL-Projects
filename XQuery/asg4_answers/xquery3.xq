(:
  For each department, output
  a. name of the department
  b. number of projects controlled by this project
  c. total number of hours worked by all employees for all these projects
:)

<q3_results>

{
  for $d in doc("../company/department.xml")//department

  let $p := doc("../company/project.xml")//project[dnum = $d/dnumber]
  let $w := doc("../company/works_on.xml")//works_on[pno = $p/pnumber]

  return
    <ans
      depname="{ $d/dname }"
      numprojects="{ count($p) }"
      hours="{ sum($w//hours) }"
    />
}

</q3_results>




