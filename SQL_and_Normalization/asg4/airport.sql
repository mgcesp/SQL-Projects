/* 1) An SQL script for for the Airport E-R diagram */

/* Phase 1: Drop all thirteen tables of Airport database if they exist
    in the correct sequence without violating foreign key constraints
*/

-- if maintains table already exists, remove the table
if exists
    (select *
     from sysobjects
     where id = object_id(N'maintains')
           and OBJECTPROPERTY(id, N'IsUserTable') = 1
    )
  drop table maintains
GO
-- if works_on table already exists, remove the table
if exists
    (select *
     from sysobjects
     where id = object_id(N'service')
           and OBJECTPROPERTY(id, N'IsUserTable') = 1
    )
  drop table service
GO
-- if owns table already exists, remove the table
if exists
    (select *
     from sysobjects
     where id = object_id(N'owns')
           and OBJECTPROPERTY(id, N'IsUserTable') = 1
    )
  drop table owns
GO
-- if flies table already exists, remove the table
if exists
    (select *
     from sysobjects
     where id = object_id(N'flies')
           and OBJECTPROPERTY(id, N'IsUserTable') = 1
    )
  drop table flies
GO
-- if works_on table already exists, remove the table
if exists
    (select *
     from sysobjects
     where id = object_id(N'works_on')
           and OBJECTPROPERTY(id, N'IsUserTable') = 1
    )
  drop table works_on
GO
-- if airplane table already exists, remove the table
if exists
    (select *
     from sysobjects
     where id = object_id(N'airplane')
           and OBJECTPROPERTY(id, N'IsUserTable') = 1
    )
  drop table airplane
GO
-- if hagar table already exists, remove the table
if exists
    (select *
     from sysobjects
     where id = object_id(N'hagar')
           and OBJECTPROPERTY(id, N'IsUserTable') = 1
    )
  drop table hagar
GO
-- if plane_type table already exists, remove the table
if exists
    (select *
     from sysobjects
     where id = object_id(N'plane_type')
           and OBJECTPROPERTY(id, N'IsUserTable') = 1
    )
  drop table plane_type
GO
-- if pilot table already exists, remove the table
if exists
    (select *
     from sysobjects
     where id = object_id(N'pilot')
           and OBJECTPROPERTY(id, N'IsUserTable') = 1
    )
  drop table pilot
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
-- if person table already exists, remove the table
if exists
    (select *
     from sysobjects
     where id = object_id(N'person')
           and OBJECTPROPERTY(id, N'IsUserTable') = 1
    )
  drop table person
GO
-- if corporation table already exists, remove the table
if exists
    (select *
     from sysobjects
     where id = object_id(N'corporation')
           and OBJECTPROPERTY(id, N'IsUserTable') = 1
    )
  drop table corporation
GO
-- if owner table already exists, remove the table
if exists
    (select *
     from sysobjects
     where id = object_id(N'owner')
           and OBJECTPROPERTY(id, N'IsUserTable') = 1
    )
  drop table owner
GO/                                                                                          

/* Phase 2: Create empty tables of Airport database*/

-- creat a new empty table owner
create table owner
(	ownerID char(4) not null,
	constraint ownerpk
		primary key (ownerID),
);
-- creat a new empty table person
create table person
(	SSN char(9) not null,
	pName varchar(15) not null,	
	pAddress varchar(20) not null,
	pPhone char(10) not null,
	ownerID char(4),
	constraint perpk
		primary key (ssn),
	constraint perownerfk
		foreign key (ownerID) references owner(ownerID)
);
-- creat a new empty table corporation
create table corporation
(	cName varchar(15) not null,
	cAddress varchar(20) not null,
	cPhone char(10) not null,
	ownerID char(4),
	constraint corpk
		primary key (cName),
	constraint corpownerfk
		foreign key (ownerID) references owner(ownerID)
);
-- creat a new empty table pilot
create table pilot
(	pSSN char(9) not null,
	licNum char(7) not null,
	restr varchar(10) not null,
	constraint pilotpk
		primary key (pSSN),
	constraint pilotperfk
		foreign key (pSSN) references person(SSN)
);
-- creat a new empty table employee
create table employee
(	eSSN char(9) not null,
	eShift varchar(10) not null,
	eSalary int not null,
	constraint emppk
		primary key (eSSN),
	constraint empperfk
		foreign key (eSSN) references person(SSN)
);
-- creat a new empty table plane_type
create table plane_type
(	pModel char(7) not null,
	pCapacity int not null,
	pWeight int not null,
	constraint plapk
		primary key (pModel),
);
-- creat a new empty table hagar
create table hagar
(	hNum char(7) not null,
	hCapacity int not null,
	hLocation varchar(15) not null,
	constraint hagpk
		primary key (hNum)
);
-- creat a new empty table airplane
create table airplane
(	regNum char(7) not null,
	aModel char(7) not null,
	hagarNum char(7) not null,
	constraint airpk
		primary key (regNum),
	constraint airtypefk
		foreign key (aModel) references plane_type (pModel),
	constraint airhagarfk
		foreign key (hagarNum) references hagar (hNum)
);
-- creat a new empty table works_on
create table works_on
(	eSSN char(9) not null,
	pModel char(7) not null,
	constraint worpk
		primary key (eSSN),
	constraint workempfk
		foreign key (eSSN) references employee (eSSN),
	constraint workplanefk
		foreign key (pModel) references plane_type(pModel)
);
-- creat a new empty table flies
create table flies
(	pSSN char(9) not null,
	pModel char(7) not null,
	constraint fliespk
		primary key (pSSN),
	constraint fliespilotfk
		foreign key (pSSN) references pilot (pSSN),
	constraint fliesplanefk
		foreign key (pModel) references plane_type (pModel)
);
-- creat a new empty table owns
create table owns
(	ownerID char(4) not null,
	regNum char(7) not null,
	pDate char(10) not null,
	constraint ownpk
		primary key (ownerID),
	constraint ownsownerfk
		foreign key (ownerID) references owner (ownerID),
	constraint ownsairplanefk
		foreign key (regNum) references airplane (regNum)
);
-- creat a new empty table service
create table service
(	workCode char(7) not null,
	regNum char(7) not null,
	sDate char(10) not null,
	sHours int,
	constraint serpk
		primary key (workCode, regNum),
	constraint servairfk
		foreign key (regNum) references airplane (regNum),
);
-- creat a new empty table maintains
create table maintains
(	eSSN char(9) not null,
	workCode char(7) not null,
	regNum char(7) not null,
	constraint mainpk
		primary key (eSSN),
	constraint mainempfk
		foreign key (eSSN) references employee (eSSN),
	constraint mainservfk
		foreign key (workCode, regNum) references service (workCode, regNum)
);
