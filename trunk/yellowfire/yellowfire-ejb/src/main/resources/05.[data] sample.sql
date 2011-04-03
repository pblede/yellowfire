declare @district_id bigint;
select @district_id = district_id from rce.district where district_code = 'CG';
	
declare @club_id bigint;
select @club_id = null;

declare @event_link_id bigint = null;
declare @sponsor_link_id1 bigint = null;
declare @sponsor_link_id2 bigint = null;

insert into cde.venue(venue_name, venue_address, venue_gps, [version]) values ('Bobbies', 'Mayfair', '-25.767593, 28.217685', 1);
declare @venue_id bigint = @@IDENTITY;

insert into rce.race (race_name, race_date, venue_id, graphic_link_id, district_id, club_id) values ('Bobbies', '2011-01-23', @venue_id, @event_link_id, @district_id, @club_id);
declare @race_id bigint = @@IDENTITY;

insert into rce.race_sponsor(race_id, race_sponsor_name, graphic_link_id) values (@race_id, 'ASA', @sponsor_link_id1);
insert into rce.race_sponsor(race_id, race_sponsor_name, graphic_link_id) values (@race_id, 'Nike', @sponsor_link_id2);

/**
 * Course 1
 */
insert into rce.course (course_starttime, course_distance, course_description, race_id) values ('07:00', '10km', '10km Run', @race_id);
declare @course_id bigint = @@IDENTITY;

insert into rce.course_category (course_id, race_id, category_name) values (@course_id, @race_id, 'Open');
declare @category_id bigint = @@IDENTITY;

insert into rce.category_fee (category_id, course_id, race_id, fee_description, fee_value) values (@category_id, @course_id, @race_id, 'Entry', 62.70);
insert into rce.category_fee (category_id, course_id, race_id, fee_description, fee_value) values (@category_id, @course_id, @race_id, 'Temporary License', 26.00);

insert into rce.category_prize (category_id, course_id, race_id, prize_description, prize_value) values (@category_id, @course_id, @race_id, '1st', 'R 1400');
insert into rce.category_prize (category_id, course_id, race_id, prize_description, prize_value) values (@category_id, @course_id, @race_id, '2nd', 'R 700');
insert into rce.category_prize (category_id, course_id, race_id, prize_description, prize_value) values (@category_id, @course_id, @race_id, '3rd', 'R 450');
                
/**
 * Course 2
 */               
insert into rce.course (course_starttime, course_distance, course_description, race_id) values ('07:00', '25km', '25km Run', @race_id);
set @course_id = @@IDENTITY

insert into rce.course_category (course_id, race_id, category_name) values (@course_id, @race_id, 'Open');
set @category_id = @@IDENTITY;

insert into rce.category_fee (category_id, course_id, race_id, fee_description, fee_value) values (@category_id, @course_id, @race_id, 'Entry', 41.00);
insert into rce.category_fee (category_id, course_id, race_id, fee_description, fee_value) values (@category_id, @course_id, @race_id, 'Temporary License', 26.00);

insert into rce.category_prize (category_id, course_id, race_id, prize_description, prize_value) values (@category_id, @course_id, @race_id, '1st', 'R 1400');
insert into rce.category_prize (category_id, course_id, race_id, prize_description, prize_value) values (@category_id, @course_id, @race_id, '2nd', 'R 700');
insert into rce.category_prize (category_id, course_id, race_id, prize_description, prize_value) values (@category_id, @course_id, @race_id, '3rd', 'R 450');
                
             
/*
 * Race Notes
 */
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'Registration', 'Pre-entry race numbers &amp; CGA Temp licences to be collected at the venue 14h00 - 17h00 on the afternoon before the race and from 05h00 on race day.', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'General', 'Sponsors Special Handout to first 2700 Pre-entries Only.', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'Registration', 'Late entries will be taken from 14h00 - 17h00 on the afternoon before the race and from 05h00 on race day, at NO extra charge', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'General', 'T-shirts to the first 3000 finishers', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'General', 'Medals to all finishers, 3000 on the day, rest to be distributed to clubs.', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'General', 'Goody Bags of assorted sponsors products to first 2700 Finishers.', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'General', 'Special Handout comprises a sponsored Bag', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'Prizes', 'Prize giving of the 21.1km race is at 09h00 and includes 2x lucky draw voucher prizes', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'Prizes', 'Prize giving of the 42.2km race is at 11h00 and includes 2x lucky draw voucher prizes', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'General', 'Cut off time for both 21.1km and 42.2km is strictly 11H30', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'Rules', 'The race is held under the rules of ASA, CGA.', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'Rules', 'Runners participate at their own risk.', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'Rules', 'All runners, both temporary licenced and registered athletes competing in the marathon amd half marathon must wear the issued race number on the front of their vests.', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'Rules', 'Half-marathon runners will be identified by a BLACK coloured race number, marathon race number is RED.', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'Rules', 'Registered athletes must belong to a club affiliated to ASA and must wear club colours and thier current licence on the back of their vests.', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'Rules', 'Non-registered runners may purchase a temporary licence @ R20 for the 21km half marathon or R30 for the 42km marathon.', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'Rules', 'Temporary licenced athletes must wear plain clothing with the race number on the front and the temp licence on the back of their vest.', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'Rules', 'Minimum age for competitors is: 21.1km - 16yrs of age and 42.2km - 20yrs of age.', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'Rules', 'Numerical age category tags must be visible on both front and back of vests. Registered runners without tags back and front will not qualify for any age prize.', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'Rules', 'Only athletes who have filled in All information on the race numder tear-off strip will be eligible for prizes', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'Rules', 'No seconding and no bicycles allowed on the route. Ample refreshments stations will be provided.', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'Rules', 'Runners indemnify the National, Provincial, and Regional bodies, sponsors, and organisers of the race against all or any actions of whatever nature that may occur during the race.', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'Rules', 'Proof of age for prize winners will be required to be presented to referees before prize giving. Prizes will be held unitil confirmed.', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'Rules', 'In the event of a pre-entrant not participating, he/she may not allow another runner to use his/her number without prior permission from the race organiser - which must be obtained at least 24 hrs in advance.', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'Rules', 'All traffic officers and marshals instructions must be obeyed. Failure to comply may lead to disqualification.', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'Rules', 'Regret, athletes not allowed to participate with pets, no wheelchair athletes, cyclists, blades or mechanically operated devices may participate.', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'Rules', 'No refund of entry fees will be considered.', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'Rules', 'International athletes to provide Clearamce Letter to the referees in the event of winning a prize.', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'General', 'Walkers are welcome, on the Half-Marathon only', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'General', 'Cut-off time for both 21.1km and 42.2km is strictly 11H30', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'General', 'Runners will be asked to leave the route after 11h30 as support will be withdrawn', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'General', 'Full race results will be published on the internet at www.raceresults.co.za', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'General', 'Results will be posted to those completing a RESULTS envelope, available at the start at R5,00 each', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'General', 'Sponsors SPECIAL HANDOUT will be given to the first 2700 Pre-Entries ONLY', GETDATE());
insert into rce.note (race_id, note_type, note_text, create_ts) values (@race_id, 'General', 'Please help reduce wastage and abuse by taking no more than 1 coke and 2 water sachets per table', GETDATE());

/*
select * from rce.race;
select * from rce.course;
select * from rce.course_category;
select * from cde.venue
select * from rce.note
*/

--update rce.venue set venue_gps = '-26.040955, 28.226076'
--update rce.Note set note_type = 'Rules' where note_type = 'Rule'
--update rce.race set race_name = 'Medihelp Sunrise Monster 2010' where race_id = 3
