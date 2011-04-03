declare @person_id bigint = null;
select @person_id = person_id from rce.person where person_name = 'marka';

print @person_id


	insert into rce.result (
		result_name,  
		result_start, 
		result_end,
		result_type_id,
		result_pace, 
		person_id, 
		race_id 
	) values (
		'Bobbies',
		'2011-01-23 07:00',
		'2011-01-23 07:53',
		1,
		5.3,
		@person_id,
		null
	);

update rce.result set result_type_id = 1
update rce.result set race_id = 3 
--SELECT result_id, create_ts, result_date, result_end, result_pace, result_start, result_type_id, update_ts, person_id, race_id FROM rce.result WHERE (((person_id = NULL) AND (result_start >= 2010-12-26 00:00:00:000)) AND (result_start <= 2011-02-06 00:00:00:000))
select * from rce.result
--delete from rce.result where result_id = 2

SELECT result_id, create_ts, result_end, result_name, result_pace, result_start, result_type_id, update_ts, person_id, race_id FROM rce.result WHERE (((person_id = NULL) AND (result_start >= '2010-12-26 00:00:00:000')) AND (result_start <= '2011-02-06 00:00:00:000'))