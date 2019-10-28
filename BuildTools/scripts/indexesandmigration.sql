drop table account_info;
drop table account_team_multi;
drop table app_conf_info;
drop table app_conf_val_info;
drop table client_info;
drop table configuration_settings;
drop table group_info;
drop table group_message_info;
drop table group_subscribers;
drop table id_seq;
drop table project_activity_info;
drop table project_comments;
drop table project_info;
drop table project_subteam_info;
drop table project_team_info;
drop table recurring_prj_info;
drop table task_info;
drop table user_activity_info;
drop table user_group_item;
drop table user_info;
drop table user_task_activity_info;

CREATE INDEX user_activity_info_indexes ON user_activity_info (created_date);
CREATE INDEX user_task_activity_info_indexes ON user_task_activity_info (task_id);
CREATE INDEX project_info_indexes ON project_info (start_time,completion_time,name);

CREATE INDEX user_task_activity_info_indexes_s ON user_task_activity_info (actual_start_time);
CREATE INDEX user_task_activity_info_indexes_e ON user_task_activity_info (actual_end_time);
CREATE INDEX user_activity_info_indexes_s ON user_activity_info (calender_date);
CREATE INDEX user_activity_info_indexes_u ON user_activity_info (id);
CREATE INDEX user_info_indexes_u ON user_info (user_id);
CREATE INDEX task_info_indexes_c ON user_info (completion_time);

  


update user_task_activity_info p set p.duration = p.duration * 60;

SET FOREIGN_KEY_CHECKS=0;
select assignee, start_time, completion_time, actual_time_taken,task_id from bops.task_info where actual_time_taken < 0;

select * from bops.user_info where user_id in ('7',3905,3905,3802,1503,3802);

select * from bops.user_task_activity_info where task_id in (select task_id from bops.task_info where actual_time_taken < 0);
);

update bops.task_info set actual_time_taken = 0 where actual_time_taken < 0;
update bops.user_task_activity_info set duration = 0 where duration < 0;


update task_info set priority='HIGH' where priority='HIGHT';

//select * from bops.user_task_activity_info where duration < 60;

update user_task_activity_info utai set task_act_category="Business Development", type="OTHER" where type="BD";
update user_task_activity_info utai set task_act_category="Training", type="OTHER" where type="TRAINING";
update user_task_activity_info utai set task_act_category="Admin", type="OTHER" where type="ADMIN";

SET SQL_SAFE_UPDATES = 0;
update user_task_activity_info utai set task_act_category="Leave" where  BINARY task_act_category="LEAVE";
update user_task_activity_info set task_act_category='Meeting' where BINARY task_act_category='MEETING';



--update time
------------
UPDATE `bops`.`user_activity_info` SET `calender_date` = '2018-12-27 12:00:00' WHERE (`user_act_id` = '581189');

UPDATE user_task_activity_info
SET 
actual_start_time = DATE_FORMAT(actual_start_time ,'2019-%m-%d %H:%i'), 
actual_end_time = DATE_FORMAT(actual_end_time ,'2019-%m-%d %H:%i'),
start_time = DATE_FORMAT(start_time ,'2019-%m-%d %H:%i'),
recorded_start_time = DATE_FORMAT(recorded_start_time ,'2019-%m-%d %H:%i'),
recorded_end_time = DATE_FORMAT(recorded_end_time ,'2019-%m-%d %H:%i')
where user_tsk_act_id in (1045003,1045004,1044150,1045005,1044138,1044151,1044139,1045002,1044140,1044141,1044142,1044143,1044144);


UPDATE user_activity_info
SET 
calender_date = DATE_FORMAT(calender_date ,'2019-%m-%d %H:%i')
where user_act_id in ('1044954','1044956','1044955','1042596','1042597','1042598','1042599','1042600','1042601','1044952');


select * from task_info where task_id in (
select task_id from user_task_activity_info where actual_start_time is null and user_grp_id=1018);

update recurring_prj_info set type='PROJECT';


INSERT INTO `bops`.`auto_report_info` (`id`, `cc_list`, `created_date`, `cron_exp`, `last_modified_date`, `last_run`, `name`, `next_run`, `to_lit`, `type`, `user_grp_id`, `no_past_days`) VALUES ('3', 'famstack.bops@gmail.com', '2019-08-05 12:00:00', '0 0 15,17,19 * * ?', '2019-09-08 04:00:31', '2019-9-8 15:00:00', 'Daily user activity', '2019-9-9 15:00:00', 'manyuni.sahu@course5i.com', 'USER_SITE_ACTIVITY', '1002', '0');
INSERT INTO `bops`.`auto_report_info` (`id`, `cc_list`, `created_date`, `cron_exp`, `last_modified_date`, `last_run`, `name`, `next_run`, `to_lit`, `type`, `user_grp_id`, `no_past_days`) VALUES ('4', 'famstack.bops@gmail.com', '2019-08-05 12:00:00', '0 0 15,17,19 * * ?', '2019-09-08 04:00:31', '2019-9-8 15:00:00', 'Daily utilization', '2019-9-9 15:00:00', 'manyuni.sahu@course5i.com', 'USER_UTILIZATION', '1002', '0');


--UPDATE user_info SET user_id=CONCAT('temp_',user_id) where user_grp_id != '99999';
