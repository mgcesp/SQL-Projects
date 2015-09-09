/* SQL command file to delete all tables and structures of the COMPANY DB
   The order of deleting tables is significant
   because a table cannot be deleted when another table references it.
   Normally this is a reverse sequence of the creation of tables.
 */
drop table dependent;
drop table works_on;
drop table project;
drop table dept_locations;
drop table employee;
drop table department;


