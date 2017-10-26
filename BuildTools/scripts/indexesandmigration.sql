CREATE INDEX user_activity_info_indexes ON user_activity_info (created_date);
CREATE INDEX user_task_activity_info_indexes ON user_task_activity_info (task_id);
CREATE INDEX project_info_indexes ON project_info (start_time,completion_time,name);

update user_task_activity_info p set p.duration = p.duration * 60;
