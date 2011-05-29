
if exists (select * from sys.objects where object_id = OBJECT_ID(N'[trn].[course_date]') and type in (N'U'))
	drop table trn.course_date;
go
if exists (select * from sys.objects where object_id = OBJECT_ID(N'[trn].[course]') and type in (N'U'))
	drop table trn.course;
go
if exists (select * from sys.objects where object_id = OBJECT_ID(N'[trn].[outcome]') and type in (N'U'))
	drop table trn.outcome;

if exists (select * from sys.objects where object_id = OBJECT_ID(N'[trn].[skill_area]') and type in (N'U'))
	drop table trn.skill_area;

if exists (select * from sys.objects where object_id = OBJECT_ID(N'[trn].[course_content_type]') and type in (N'U'))
	drop table trn.course_content_type;

if exists (select * from sys.objects where object_id = OBJECT_ID(N'[trn].[content_type]') and type in (N'U'))
	drop table trn.content_type;

if exists (select * from sys.objects where object_id = OBJECT_ID(N'[trn].[course_category]') and type in (N'U'))
	drop table trn.course_category;

if exists (select * from sys.objects where object_id = OBJECT_ID(N'[trn].[training_provider]') and type in (N'U'))
	drop table trn.training_provider;

if exists (select * from sys.objects where object_id = OBJECT_ID(N'[trn].[course_length_type]') and type in (N'U'))
	drop table trn.course_length_type;

if exists (select * from sys.objects where object_id = OBJECT_ID(N'[trn].[duration_type]') and type in (N'U'))
	drop table trn.duration_type;
	
if exists (select * from sys.objects where object_id = OBJECT_ID(N'[trn].[course_charge_type]') and type in (N'U'))
	drop table trn.course_charge_type;
	
go

create table trn.outcome (
    outcome_id bigint not null identity(1, 1),
	outcome_description varchar(64) not null,
	create_ts datetime null,
	update_ts datetime null,
	version int null
);
alter table trn.outcome add constraint pk_outcome_id primary key clustered ([outcome_id]);
alter table trn.outcome add constraint df_outcome_create_ts default(getdate()) FOR [create_ts];

create table trn.skill_area (
    skill_area_id bigint not null identity(1, 1),
	skill_area_description varchar(64) not null,
	create_ts datetime null,
	update_ts datetime null,
	version int null
);
alter table trn.skill_area add constraint pk_skill_area_id primary key clustered ([skill_area_id]);
alter table trn.skill_area add constraint df_skill_area_create_ts default(getdate()) FOR [create_ts];

create table trn.content_type (
    content_type_id bigint not null identity(1, 1),
	content_type_description varchar(64) not null,
	create_ts datetime null,
	update_ts datetime null,
	version int null
);
alter table trn.content_type add constraint pk_content_type_id primary key clustered ([content_type_id]);
alter table trn.content_type add constraint df_content_type_create_ts default(getdate()) FOR [create_ts];

insert into trn.content_type (content_type_description, create_ts, update_ts, [version]) values ('Computer Based', GETDATE(), GETDATE(), 1);
insert into trn.content_type (content_type_description, create_ts, update_ts, [version]) values ('Instructor Led', GETDATE(), GETDATE(), 1);
insert into trn.content_type (content_type_description, create_ts, update_ts, [version]) values ('Self Paced', GETDATE(), GETDATE(), 1);

create table trn.course_category (
    course_category_id bigint not null identity(1, 1),
	course_category_description varchar(64) not null,
	create_ts datetime null,
	update_ts datetime null,
	version int null
);
alter table trn.course_category add constraint pk_course_category_id primary key clustered ([course_category_id]);
alter table trn.course_category add constraint df_course_category_create_ts default(getdate()) FOR [create_ts];

create table trn.training_provider (
    training_provider_id bigint not null identity(1, 1),
	training_provider_description varchar(64) not null,
	training_provider_code varchar(25) null,
	create_ts datetime null,
	update_ts datetime null,
	version int null
);
alter table trn.training_provider add constraint pk_training_provider_id primary key clustered ([training_provider_id]);
alter table trn.training_provider add constraint df_training_provider_create_ts default(getdate()) FOR [create_ts];

create table trn.duration_type (
	duration_type_id int not null identity(1, 1),
	duration_type_description varchar(64) not null,
	create_ts datetime null,
	update_ts datetime null,
	version int null	
);
alter table trn.duration_type add constraint pk_duration_type_id primary key clustered ([duration_type_id]);
alter table trn.duration_type add constraint df_duration_type_create_ts default(getdate()) FOR [create_ts];

set identity_insert trn.duration_type on
insert into trn.duration_type(duration_type_id, duration_type_description, [version]) values (0, 'Hours', 1);
insert into trn.duration_type(duration_type_id, duration_type_description, [version]) values (1, 'Days', 1);
set identity_insert trn.duration_type off

create table trn.course_charge_type (
	course_charge_type_id bigint not null identity(1, 1),
	course_charge_type_description varchar(64) not null,
	create_ts datetime null,
	update_ts datetime null,
	version int null	
);
alter table trn.course_charge_type add constraint pk_course_charge_type_id primary key clustered ([course_charge_type_id]);
alter table trn.course_charge_type add constraint df_course_charge_type_create_ts default(getdate()) FOR [create_ts];

set identity_insert trn.course_charge_type on
insert into trn.course_charge_type(course_charge_type_id, course_charge_type_description, [version]) values (0, 'Delegate', 1);
insert into trn.course_charge_type(course_charge_type_id, course_charge_type_description, [version]) values (1, 'Course', 1);
set identity_insert trn.course_charge_type off


create table trn.course (
	course_id bigint not null identity(1, 1),
	course_duration int not null,
	course_duration_type_id int not null,
	training_provider_id bigint not null,
	course_accrediting_body varchar(64) null,
	course_title varchar(64) not null,
	course_overview varchar(256) null,
	course_contact_person varchar(64) null,
	course_contact_no varchar(24) null,
	course_internal bit null,
	course_core bit null,
	skill_area_id bigint null,
	content_type_id bigint null,
	course_category_id bigint null,
	create_ts datetime null,
	update_ts datetime null,
	version int null
);

alter table trn.course add constraint pk_course_id primary key clustered ([course_id]);
alter table trn.course add constraint df_course_create_ts default(getdate()) FOR [create_ts];
alter table trn.course add constraint df_course_internal default(1) FOR [course_internal];
alter table trn.course add constraint df_course_core default(1) FOR [course_core];
alter table trn.course add constraint fk_course_course_duration_type_id foreign key (course_duration_type_id) references trn.duration_type (duration_type_id);
alter table trn.course add constraint fk_course_training_provider_id foreign key (training_provider_id) references trn.training_provider (training_provider_id);
alter table trn.course add constraint fk_course_skill_area_id foreign key (skill_area_id) references trn.skill_area (skill_area_id);
alter table trn.course add constraint fk_course_content_type_id foreign key (content_type_id) references trn.content_type (content_type_id);
alter table trn.course add constraint fk_course_course_category_id foreign key (course_category_id) references trn.course_category (course_category_id);

create table trn.course_date (
	course_date_id bigint not null identity(1, 1),
	course_id bigint not null,
	venue_id bigint not null,
	
	course_start_date date not null,
	course_end_date date not null,
	course_start_time time not null,
	course_end_time time not null,
	
	course_charge decimal(12, 2) null,
	course_charge_type_id bigint null, 
	
	create_ts datetime null,
	update_ts datetime null,
	version int null
);

alter table trn.course_date add constraint pk_course_date_id primary key clustered ([course_date_id]);
alter table trn.course_date add constraint df_course_date_create_ts default(getdate()) FOR [create_ts];
alter table trn.course_date add constraint fk_course_date_course_id foreign key (course_id) references trn.course (course_id);
alter table trn.course_date add constraint fk_course_date_venue_id foreign key (venue_id) references cde.venue (venue_id);
alter table trn.course_date add constraint fk_course_date_course_charge_type_id foreign key (course_charge_type_id) references trn.course_charge_type (course_charge_type_id);
alter table trn.course_date add constraint df_course_date_course_charge_type_id default(2) FOR [course_charge_type_id];

go