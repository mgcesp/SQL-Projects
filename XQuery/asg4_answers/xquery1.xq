(:
   For each dependent, ouput
   a. dependent name
   b. firstname, lastname of corresponding employee
   c. firstname, lastname of the manager for that employee
:)

<q1_results>
{
  for $e in doc("../company/employee.xml")//employee,
      $m in doc("../company/employee.xml")//employee,
      $d in doc("../company/dependent.xml")//dependent
  where $d/essn = $e/ssn and $e/superssn = $m/ssn
  return
    <row
      dependent_name="{$d/dependent_name}"
      fname="{ $e/fname }"
      lname="{ $e/lname }"
      m_fname="{ $m/fname }"
      m_lname="{ $m/lname }"
    />
}
</q1_results>
