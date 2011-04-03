if OBJECT_ID(N'[rce].[vw_user_property]') is not null
	drop view rce.vw_user_property;
go
create view rce.vw_user_property with schemabinding
as
select
	property_name,
	property_value,
	property_type,
	create_ts,
	update_ts
from rce.property
where property_name like 'user.%'
go
