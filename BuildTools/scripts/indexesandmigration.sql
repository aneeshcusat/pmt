CREATE INDEX user_activity_info_indexes ON user_activity_info (created_date);
CREATE INDEX user_task_activity_info_indexes ON user_task_activity_info (task_id);
CREATE INDEX project_info_indexes ON project_info (start_time,completion_time,name);

update user_task_activity_info p set p.duration = p.duration * 60;


select * from bops.task_info where actual_time_taken < 0;

update bops.task_info set actual_time_taken = actual_time_taken*-1 where actual_time_taken < 0;

select * from bops.user_task_activity_info where duration < 60;