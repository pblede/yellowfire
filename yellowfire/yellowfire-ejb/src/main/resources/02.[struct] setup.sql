use race;


if  exists (select * from sys.objects where object_id = OBJECT_ID(N'[rce].[vw_user_property]') and type in (N'V'))
	drop view rce.vw_user_property; 
if  exists (select * from sys.objects where object_id = OBJECT_ID(N'[rce].[vw_jaas_person]') and type in (N'V'))
	drop view rce.vw_jaas_person; 
if  exists (select * from sys.objects where object_id = OBJECT_ID(N'[rce].[vw_jaas_person_group]') and type in (N'V'))
	drop view rce.vw_jaas_person_group
go
if exists (select * from sys.objects where object_id = OBJECT_ID(N'[rce].[result]') and type in (N'U'))
	drop table rce.result;
if exists (select * from sys.objects where object_id = OBJECT_ID(N'[rce].[result_type]') and type in (N'U'))
	drop table rce.result_type;
if exists (select * from sys.objects where object_id = OBJECT_ID(N'[rce].[race_registration]') and type in (N'U'))
	drop table rce.race_registration;
if  exists (select * from sys.objects where object_id = OBJECT_ID(N'[rce].[category_prize]') and type in (N'U'))
	drop table rce.category_prize;
if  exists (select * from sys.objects where object_id = OBJECT_ID(N'[rce].[category_fee]') and type in (N'U'))
	drop table rce.category_fee;
if  exists (select * from sys.objects where object_id = OBJECT_ID(N'[rce].[course_category]') and type in (N'U'))
	drop table rce.course_category;
if  exists (select * from sys.objects where object_id = OBJECT_ID(N'[rce].[course]') and type in (N'U'))
	drop table rce.course;
if  exists (select * from sys.objects where object_id = OBJECT_ID(N'[rce].[note]') and type in (N'U'))
	drop table rce.note;
if  exists (select * from sys.objects where object_id = OBJECT_ID(N'[rce].[race_sponsor]') and type in (N'U'))
	drop table rce.race_sponsor;
if  exists (select * from sys.objects where object_id = OBJECT_ID(N'[rce].[race]') and type in (N'U'))
	drop table rce.race;
if  exists (select * from sys.objects where object_id = OBJECT_ID(N'[rce].[graphic_link]') and type in (N'U'))
	drop table rce.graphic_link;
if  exists (select * from sys.objects where object_id = OBJECT_ID(N'[rce].[property]') and type in (N'U'))
	drop table rce.property;
if exists (select * from sys.objects where object_id = OBJECT_ID(N'[rce].[club]') and type in (N'U'))
	drop table rce.club;
if exists (select * from sys.objects where object_id = OBJECT_ID(N'[rce].[district]') and type in (N'U'))
	drop table rce.district;
go


create table rce.district (
	district_id bigint not null identity(1, 1),
	district_code varchar(12),
	district_name varchar(1024),
	create_ts datetime null,
	update_ts datetime null,
	version int null
);
alter table rce.district add constraint pk_district_id primary key clustered ([district_id]);
alter table rce.district add constraint df_district_create_ts default(getdate()) FOR [create_ts];

insert into rce.district (district_code, district_name) values ('CG', 'Central Gauteng');
insert into rce.district (district_code, district_name) values ('GN', 'Gauteng North');

create table rce.club (
	club_id bigint not null identity(1, 1),
	district_id bigint not null,
	club_name varchar(1024),
	create_ts datetime null,
	update_ts datetime null,
	version int null
);

alter table rce.club add constraint pk_club_id primary key clustered ([club_id]);
alter table rce.club add constraint fk_club_district_id foreign key (district_id) references rce.district (district_id);
alter table rce.club add constraint df_club_create_ts default(getdate()) FOR [create_ts];

create table rce.graphic_link (
	graphic_link_id bigint not null identity(1, 1),
	graphic_link_url varchar(512) not null,
	graphic_link_image_url varchar(512) null,
	graphic_link_scale_percentage int null,
	graphic_link_alt_text varchar(128) null,
	create_ts datetime null,
	version int null
);

alter table rce.graphic_link add constraint pk_graphic_link_id primary key clustered (graphic_link_id);
alter table rce.graphic_link add constraint df_graphic_link_scale_percentage default(100) FOR [graphic_link_scale_percentage];
alter table rce.graphic_link add constraint df_graphic_link_create_ts default(getdate()) FOR [create_ts];

create table rce.race (
	race_id bigint not null identity(1, 1),
	race_name varchar(256) not null,
	race_date date not null,
	venue_id bigint not null,
	district_id bigint not null,
	club_id bigint null,
	graphic_link_id bigint null,
	create_ts datetime,
	version int null
)

alter table rce.race add constraint pk_race_id primary key clustered(race_id);
alter table rce.race add constraint fk_race_venue_id foreign key (venue_id) references cde.venue (venue_id);
alter table rce.race add constraint fk_race_graphic_link_id foreign key (graphic_link_id) references rce.graphic_link (graphic_link_id);
alter table rce.race add constraint fk_race_district_id foreign key (district_id) references rce.district (district_id);
alter table rce.race add constraint fk_race_club_id foreign key (club_id) references rce.club (club_id);
alter table rce.race add constraint ix_race_unique unique nonclustered(race_name, race_date)
alter table rce.race add constraint df_race_create_ts default(getdate()) FOR [create_ts];

create table rce.course (
	course_id bigint not null identity(1, 1),
	race_id bigint not null,
	course_starttime time not null,
	course_distance varchar(32) not null,
	course_description varchar(max),
	create_ts datetime,
	version int null
)

alter table rce.course add constraint pk_course_id primary key clustered (course_id);
alter table rce.course add constraint fk_course_race_id foreign key (race_id) references rce.race (race_id);
alter table rce.course add constraint df_course_create_ts default(getdate()) FOR [create_ts];

create table rce.course_category (
	category_id bigint not null identity(1, 1),
	course_id bigint not null,
	race_id bigint not null,
	category_name varchar(32) not null,
	create_ts datetime null,
	version int null
)

alter table rce.course_category add constraint pk_category_id primary key clustered (category_id);
alter table rce.course_category add constraint fk_category_course_id foreign key (race_id) references rce.race (race_id);
alter table rce.course_category add constraint df_course_category_create_ts default(getdate()) FOR [create_ts];

create table rce.category_fee (
	category_fee_id bigint not null identity(1, 1),
	category_id bigint not null,
	course_id bigint not null,
	race_id bigint not null,
	fee_description varchar(024),
	fee_value decimal(10, 2) not null,
	create_ts datetime null,
	version int null
)

alter table rce.category_fee add constraint pk_category_fee_id primary key clustered (category_fee_id);
alter table rce.category_fee add constraint fk_category_fee_category_id foreign key (category_id) references rce.course_category (category_id);
alter table rce.category_fee add constraint df_category_fee_create_ts default(getdate()) FOR [create_ts];

create table rce.category_prize (
	category_prize_id bigint not null identity(1, 1),
	category_id bigint not null,
	course_id bigint not null,
	race_id bigint not null,
	prize_description varchar(1024),
	prize_value varchar(1024) not null,
	create_ts datetime null,
	version int null
)

alter table rce.category_prize add constraint pk_category_prize_id primary key clustered (category_prize_id);
alter table rce.category_prize add constraint fk_category_prize_category_id foreign key (category_id) references rce.course_category (category_id);
alter table rce.category_prize add constraint df_category_prize_create_ts default(getdate()) FOR [create_ts];

create table rce.note (
	note_id bigint not null identity(1, 1),
	note_type varchar(32) not null,
	note_text varchar(max) null,
	race_id bigint not null,
	create_ts datetime null,
	version int null
)

alter table rce.note add constraint pk_note_id primary key clustered (note_id);
alter table rce.note add constraint fk_note_race_id foreign key (race_id) references rce.race (race_id);
alter table rce.note add constraint df_note_create_ts default(getdate()) FOR [create_ts];

create table rce.race_sponsor (
	race_sponsor_id bigint not null identity(1, 1),
	race_sponsor_name varchar(256) not null,
	race_id bigint not null,
	graphic_link_id bigint null,
	create_ts datetime null,
	version int null
)

alter table rce.race_sponsor add constraint pk_race_sponsor_id primary key clustered(race_sponsor_id);
alter table rce.race_sponsor add constraint fk_race_sponsor_race_id foreign key (race_id) references rce.race (race_id);
alter table rce.race_sponsor add constraint fk_race_sponsor_graphic_link_id foreign key (graphic_link_id) references rce.graphic_link (graphic_link_id);
alter table rce.race_sponsor add constraint df_race_sponsor_create_ts default(getdate()) FOR [create_ts];

create table rce.race_registration (
	race_registration_id bigint not null identity(1, 1),
	race_id bigint not null,
	person_id bigint not null,
	course_id bigint null,
	category_id bigint null,
	category_fee_id bigint not null,
	create_ts datetime null,
	update_ts datetime null,
	version int null
)

alter table rce.race_registration add constraint pk_race_registration_id primary key clustered(race_registration_id);
alter table rce.race_registration add constraint fk_race_registration_race_id foreign key (race_id) references rce.race (race_id);
alter table rce.race_registration add constraint fk_race_registration_person_id foreign key (person_id) references cde.person (person_id);
alter table rce.race_registration add constraint fk_race_registration_course_id foreign key (course_id) references rce.course (course_id);
alter table rce.race_registration add constraint fk_race_registration_category_id foreign key (category_id) references rce.course_category (category_id);
alter table rce.race_registration add constraint fk_race_registration_category_fee_id foreign key (category_fee_id) references rce.category_fee (category_fee_id);
alter table rce.race_registration add constraint df_race_registration_create_ts default(getdate()) FOR [create_ts];


create table rce.result_type (
	result_type_id int not null,
	result_type_description varchar(32) not null,
	create_ts datetime null,
	update_ts datetime null,
	version int null
);

alter table rce.result_type add constraint pk_result_type_id primary key clustered(result_type_id);
alter table rce.result_type add constraint df_result_type_create_ts default(getdate()) FOR [create_ts];


insert into rce.result_type (result_type_id, result_type_description) values (0, 'Training');
insert into rce.result_type (result_type_id, result_type_description) values (1, 'Race');

create table rce.result (
	result_id bigint not null identity(1, 1),
	person_id bigint not null,
	result_name varchar(1024) not null,
	result_type_id int not null,
	result_start datetime not null,
	result_end datetime not null,
	result_time_hours int null,
	result_time_minutes int null,
	result_time_seconds int null,
	result_pace decimal(10, 2) null,
	result_distance decimal(10, 2) null,
	result_notes varchar(256) null,
	race_id bigint null,
	create_ts datetime null,
	update_ts datetime null,
	version int null
);

alter table rce.result add constraint pk_result_id primary key clustered(result_id);
alter table rce.result add constraint fk_result_result_type_id foreign key (result_type_id) references rce.result_type (result_type_id);
alter table rce.result add constraint fk_result_person_id foreign key (person_id) references cde.person (person_id);
alter table rce.result add constraint fk_result_race_id foreign key (race_id) references rce.race (race_id);
alter table rce.result add constraint df_result_create_ts default(getdate()) FOR [create_ts];
alter table rce.result add constraint df_result_name default('Training') FOR [result_name];

