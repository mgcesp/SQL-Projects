(:
  Output the firstname and lastname of each pair of employees who work for the same project.
  There should not be any duplicate reversed pair and no employee be paired with the same employee
:)

<q2_results>
{

  for $n in distinct-values(doc("../company/works_on.xml")//works_on/pno)
  
  for $wo1 in doc("../company/works_on.xml")//works_on
  for $wo2 in doc("../company/works_on.xml")//works_on

  let $e1 := doc("../company/employee.xml")//employee[ssn = $wo1/essn]
  let $e2 := doc("../company/employee.xml")//employee[ssn = $wo2/essn]

  where $wo1/pno = $wo2/pno and $wo2/pno = $n and $wo1/essn < $wo2/essn  
  return  
    <row>
      {$wo1/pno, $e1/lname, $e1/fname, $e2/lname, $e2/fname} 
    </row>

}
</q2_results>
