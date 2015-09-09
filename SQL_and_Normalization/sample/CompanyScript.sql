/* Company database SQL data definition.
   Ordering of tables is important
   since a table can reference tables that are defined before.
   Since date type is not available in MS_SQL,
     create a new user defined data type date as char(10).
*/

/* Phase 1: Drop all six tables of Company database if they exist
    in the correct sequence without violating foreign key constraints
*/

-- if dependent table already exists, remove the table
if exists
    (select *
     from sysobjects
     where id = object_id(N'dependent')
           and OBJECTPROPERTY(id, N'IsUserTable') = 1
    )
  drop table dependent
GO

-- if works_on table already exists, remove the tablema
if exists
    (select *
     from sysobjects
     where id = object_id(N'works_on')
           and OBJECTPROPERTY(id, N'IsUserTable') = 1
    )
  drop table works_on
GO

-- if project table already exists, remove the table
if exists
    (select *
     from sysobjects
     where id = object_id(N'project')
           and OBJECTPROPERTY(id, N'IsUserTable') = 1
    )
  drop table project
GO

-- if dept_locations table already exists, remove the table
if exists
    (select *
     from sysobjects
     where id = object_id(N'dept_locations')
           and OBJECTPROPERTY(id, N'IsUserTable') = 1
    )
  drop table dept_locations
GO

-- if employee table already exists, remove the table
if exists
    (select *
     from sysobjects
     where id = object_id(N'employee')
           and OBJECTPROPERTY(id, N'IsUserTable') = 1
    )
  drop table employee
GO

-- if department table already exists, remove the table
if exists
    (select *
     from sysobjects
     where id = object_id(N'department')
           and OBJECTPROPERTY(id, N'IsUserTable') = 1
    )
  drop table department
GO


/* Phase 2: Create all six empty tables of Company database */

-- create a new empty table department
create table department
( dname		varchar(15)	not null,
  dnumber	int		not null,
  mgrssn	char(9)		not null,
  mgrstartdate	char(10),
  constraint deptpk
    primary key (dnumber),
  constraint deptsk
    unique (dname)
/*constraint deptmgrfk
    foreign key (mgrssn) references employee(ssn)
  This foreign key has been commented out since the dbms does not
  support mutual foreign keys between department and employee tables */
);

-- create a new empty table employee
create table employee
( fname		varchar(15)	not null,
  minit		char,
  lname		varchar(15)	not null,
  ssn		char(9)		not null,
  bdate		char(10),
  address	varchar(30),
  sex		char,
  salary	decimal(10,2),
  superssn	char(9),
  dno		int		not null,
  constraint emppk
    primary key (ssn),
/*constraint empsuperfk
    foreign key (superssn) references employee(ssn),
  This foreign key constraint among columns of the same table
   has not been supported by MS-SQL */
  constraint empdeptfk
    foreign key (dno) references department(dnumber)
);

-- create a new empty table dept_locations
create table dept_locations
( dnumber	int		not null,
  dlocation	varchar(15)	not null,
  constraint dlocpk
    primary key (dnumber, dlocation),
  constraint dlocdeptfk
    foreign key (dnumber) references department(dnumber)
);

-- create a new empty table project
create table project
( pname		varchar(15)	not null,
  pnumber	int		not null,
  plocation	varchar(15),
  dnum		int		not null,
  constraint projpk
    primary key (pnumber),
  constraint projsk
    unique (pname),
  constraint projdeptfk
    foreign key (dnum) references department(dnumber)
);

-- create a new empty table works_on
create table works_on
( essn		char(9)		not null,
  pno		int		not null,
  hours		decimal(3,1)	not null,
  constraint workpk
    primary key (essn, pno),
  constraint workempfk
    foreign key (essn) references employee(ssn),
  constraint workprojfk
    foreign key (pno) references project(pnumber)
);

-- create a new empty table dependent
create table dependent
( essn		char(9)		not null,
  dependent_name varchar(15)	not null,
  sex		char,
  bdate		char(10),
  relationship	varchar(8),
  constraint deppk
    primary key (essn, dependent_name),
  constraint depempfk
    foreign key (essn) references employee(ssn)
);


/* End of Company database script */
