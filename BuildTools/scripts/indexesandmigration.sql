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