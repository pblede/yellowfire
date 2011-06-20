
if exists (select * from sys.objects where object_id = OBJECT_ID(N'[cde].[answer_text]') and type in (N'U'))
	drop table cde.answer_text;

if exists (select * from sys.objects where object_id = OBJECT_ID(N'[cde].[answer_boolean]') and type in (N'U'))
	drop table cde.answer_boolean;

if exists (select * from sys.objects where object_id = OBJECT_ID(N'[cde].[answer_number]') and type in (N'U'))
	drop table cde.answer_number;

if exists (select * from sys.objects where object_id = OBJECT_ID(N'[cde].[answer]') and type in (N'U'))
	drop table cde.answer;

if exists (select * from sys.objects where object_id = OBJECT_ID(N'[cde].[questionaire_question]') and type in (N'U'))
	drop table cde.questionaire_question;
	
if exists (select * from sys.objects where object_id = OBJECT_ID(N'[cde].[questionaire]') and type in (N'U'))
	drop table cde.questionaire;
	
if exists (select * from sys.objects where object_id = OBJECT_ID(N'[cde].[question]') and type in (N'U'))
	drop table cde.question;
	
if exists (select * from sys.objects where object_id = OBJECT_ID(N'[cde].[question_type]') and type in (N'U'))
	drop table cde.question_type;

if  exists (select * from sys.objects where object_id = OBJECT_ID(N'[cde].[venue]') and type in (N'U'))
	drop table cde.venue;

if  exists (select * from sys.objects where object_id = OBJECT_ID(N'[cde].[vw_user_property]') and type in (N'V'))
	drop view cde.vw_user_property; 

if  exists (select * from sys.objects where object_id = OBJECT_ID(N'[cde].[vw_system_property]') and type in (N'V'))
	drop view cde.vw_system_property; 
		
if OBJECT_ID(N'[cde].[vw_jaas_person_group]') is not null
	drop view cde.vw_jaas_person_group;
	
if OBJECT_ID(N'[cde].[vw_jaas_person]') is not null
	drop view cde.vw_jaas_person;
	
IF  EXISTS (SELECT * FROM SYS.OBJECTS WHERE OBJECT_ID = OBJECT_ID(N'[cde].[person]') AND TYPE IN (N'U'))
	DROP TABLE cde.person;
IF  EXISTS (SELECT * FROM SYS.OBJECTS WHERE OBJECT_ID = OBJECT_ID(N'[cde].[person_group]') AND TYPE IN (N'U'))
	DROP TABLE cde.person_group;
	

create table cde.person_group (
	person_group_name varchar(32) not null,
	version int null
);
alter table cde.[person_group] add constraint pk_person_group_name primary key clustered ([person_group_name]);

insert into cde.person_group (person_group_name) values ('Blue');
insert into cde.person_group (person_group_name) values ('Silver');
insert into cde.person_group (person_group_name) values ('Gold');
insert into cde.person_group (person_group_name) values ('Platinum');
insert into cde.person_group (person_group_name) values ('Administrator');

create table cde.person (
	person_id bigint not null identity(1, 1),
	person_name varchar(32) not null,
	person_firstname varchar(64) null,
	person_lastname varchar(64) null,
	person_password varchar(512) not null,
	person_email varchar(512) not null,
	person_dob datetime null,
	person_group_name varchar(32) null,
	person_verification_key varchar(128) null,
	person_verified bit null,
	club_id bigint null,
	create_ts datetime null,
	update_ts datetime null,
	verified_ts datetime null,
	version int null
);

alter table cde.person add constraint pk_person_id primary key clustered ([person_id]);
alter table cde.person add constraint fk_person_group_member foreign key (person_group_name) references cde.person_group (person_group_name);
--alter table cde.person add constraint fk_person_club_id foreign key (club_id) references rce.club (club_id);
alter table cde.person add constraint ix_person_unique unique nonclustered([person_name])
alter table cde.person add constraint df_person_create_ts default(getdate()) FOR [create_ts];
alter table cde.person add constraint df_person_group_name default('Blue') FOR [person_group_name];
alter table cde.person add constraint df_person_verified default(0) FOR [person_verified];

insert into cde.person ([person_name], person_password, person_email, person_dob, person_group_name, person_firstname, person_lastname, person_verified) values ('system', 'password1*', 'mark@connext.co.za', '1974-05-31', 'Administrator', null, null, 1);
insert into cde.person ([person_name], person_password, person_email, person_dob, person_group_name, person_firstname, person_lastname, person_verified) values ('admin', 'adminadmin', 'mark@connext.co.za', '1974-05-31', 'Administrator', null, null, 1);


create table cde.venue (
	venue_id bigint not null identity(1, 1),
	venue_name varchar(128) null,
	venue_address varchar(512) null,
	venue_gps varchar(128) null,
	venue_longitude decimal(10, 7),
	venue_latitude decimal(10, 7),
	venue_type smallint,
	create_ts datetime null,
	update_ts datetime null,
	version int null
)

alter table cde.venue add constraint pk_venue_id primary key clustered (venue_id);
alter table cde.venue add constraint df_venue_create_ts default(getdate()) FOR [create_ts];


create table cde.question_type (
	question_type_id int not null,
	question_type_description varchar(32) not null,
	create_ts datetime null,
	update_ts datetime null,
	version int null
);
alter table cde.question_type add constraint pk_question_type_id primary key clustered ([question_type_id]);
alter table cde.question_type add constraint df_question_type_create_ts default(getdate()) FOR [create_ts];

insert into cde.question_type (question_type_id, question_type_description) values (0, 'Number');
insert into cde.question_type (question_type_id, question_type_description) values (1, 'Boolean');
insert into cde.question_type (question_type_id, question_type_description) values (2, 'Text');

create table cde.question (
	question_id bigint not null identity(1, 1),
	question_type_id int not null,
	question_text varchar(1024) not null,
	create_ts datetime null,
	update_ts datetime null,
	version int null
);
alter table cde.question add constraint pk_question_id primary key clustered ([question_id]);
alter table cde.question add constraint fk_question_question_type_id foreign key (question_type_id) references cde.question_type (question_type_id);
alter table cde.question add constraint df_question_create_ts default(getdate()) FOR [create_ts];

create table cde.questionaire (
	questionaire_id bigint not null identity(1, 1),
	questionaire_name varchar(64) not null,
	create_ts datetime null,
	update_ts datetime null,
	version int null
);
alter table cde.questionaire add constraint pk_questionaire_id primary key clustered ([questionaire_id]);
alter table cde.questionaire add constraint df_questionaire_create_ts default(getdate()) FOR [create_ts];

create table cde.questionaire_question (
	questionaire_id bigint not null,
	question_id bigint not null,
	create_ts datetime null,
	update_ts datetime null,
	version int null
);
alter table cde.questionaire_question add constraint pk_questionaire_question_id primary key clustered ([questionaire_id], [question_id]);
alter table cde.questionaire_question add constraint df_questionaire_question_create_ts default(getdate()) FOR [create_ts];
alter table cde.questionaire_question add constraint fk_questionaire_question_question_id foreign key (question_id) references cde.question (question_id);
alter table cde.questionaire_question add constraint fk_questionaire_question_questionaire_id foreign key (questionaire_id) references cde.questionaire (questionaire_id);

create table cde.answer (
	answer_id bigint not null identity(1, 1),
	questionaire_id bigint not null,
	question_id bigint not null,
	person_id bigint not null,
	create_ts datetime null,
	update_ts datetime null,
	version int null
);
alter table cde.answer add constraint pk_answer_id primary key clustered ([answer_id]);
alter table cde.answer add constraint df_answer_create_ts default(getdate()) FOR [create_ts];
alter table cde.answer add constraint fk_answer_question_id foreign key (question_id) references cde.question (question_id);
alter table cde.answer add constraint fk_answer_questionaire_id foreign key (questionaire_id) references cde.questionaire (questionaire_id);
alter table cde.answer add constraint fk_answer_person_id foreign key (person_id) references cde.person (person_id);

create table cde.answer_number (
	answer_id bigint not null,
	answer_value int null,
	create_ts datetime null,
	update_ts datetime null,
	version int null
);
alter table cde.answer_number add constraint df_answer_number_create_ts default(getdate()) FOR [create_ts];
alter table cde.answer_number add constraint fk_answer_number_answer_id foreign key (answer_id) references cde.answer (answer_id);

create table cde.answer_boolean (
	answer_id bigint not null,
	answer_value int null,
	create_ts datetime null,
	update_ts datetime null,
	version int null
);
alter table cde.answer_boolean add constraint df_answer_boolean_create_ts default(getdate()) FOR [create_ts];
alter table cde.answer_boolean add constraint fk_answer_boolean_answer_id foreign key (answer_id) references cde.answer (answer_id);

create table cde.answer_text (
	answer_id bigint not null,
	answer_value int null,
	create_ts datetime null,
	update_ts datetime null,
	version int null
);
alter table cde.answer_text add constraint df_answer_text_create_ts default(getdate()) FOR [create_ts];
alter table cde.answer_text add constraint fk_answer_text_answer_id foreign key (answer_id) references cde.answer (answer_id);

create table cde.property (
	property_name varchar(256) not null,
	property_value varchar(1024) null,
	property_type varchar(1024) null,
	create_ts datetime null,
	update_ts datetime null,
	version int null
)

alter table cde.property add constraint pk_property_name primary key clustered(property_name);
alter table cde.property add constraint df_property_type default('java.lang.String') FOR [property_type];
alter table cde.property add constraint df_property_create_ts default(getdate()) FOR [create_ts];

go
create view cde.vw_jaas_person with schemabinding
as
select 
	person_name,
	person_password
from cde.person
go
create view cde.vw_jaas_person_group with schemabinding
as
select 
	person_name,
	person_group_name
from cde.person
go
if OBJECT_ID(N'[cde].[vw_user_property]') is not null
	drop view cde.vw_user_property;
go
create view cde.vw_user_property with schemabinding
as
select
	property_name,
	property_value,
	property_type,
	create_ts,
	update_ts,
	[version]
from rce.property
where property_name like 'user.%'
go
create view cde.vw_system_property with schemabinding
as
select
	property_name,
	property_value,
	property_type,
	create_ts,
	update_ts,
	[version]
from rce.property
where property_name like 'system.%'
go
insert into cde.vw_system_property (property_name, property_value, [version]) values ('system.timezone', 'CAT', 1);
go
create table cde.notification (
	notification_id bigint not null identity(1, 1),
	notification_type varchar(32) not null,
	notification_from varchar(128) not null,
	notification_to varchar(256) not null,
	notification_cc varchar(256) null,
	notification_bcc varchar(256) null,
	notification_subject varchar(256) null,
	notification_body varchar(max) null,
	notification_sent datetime null,
	create_ts datetime null,
	update_ts datetime null,
	version int null,
	archived bit null,
	
)

alter table cde.notification add constraint pk_notification_id primary key clustered ([notification_id]);
alter table cde.notification add constraint df_property_archived default(0) FOR [archived];