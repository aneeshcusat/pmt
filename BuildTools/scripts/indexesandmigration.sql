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

update user_task_activity_info p set p.duration = p.duration * 60;

SET FOREIGN_KEY_CHECKS=0;
select assignee, start_time, completion_time, actual_time_taken,task_id from bops.task_info where actual_time_taken < 0;

select * from bops.user_info where user_id in ('7',3905,3905,3802,1503,3802);

select * from bops.user_task_activity_info where task_id in (select task_id from bops.task_info where actual_time_taken < 0);
);

update bops.task_info set actual_time_taken = 0 where actual_time_taken < 0;
update bops.user_task_activity_info set duration = 0 where duration < 0;


//select * from bops.user_task_activity_info where duration < 60;