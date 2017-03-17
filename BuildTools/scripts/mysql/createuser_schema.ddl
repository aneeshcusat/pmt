CREATE SCHEMA 'bops';
CREATE USER 'bops'@'localhost' IDENTIFIED BY 'bops';
GRANT ALL PRIVILEGES ON * . * TO 'bops'@'localhost';
FLUSH PRIVILEGES;


Insert into bops.user_info (ID,DESIGNATION,FIRST_NAME,HASHKEY,MOBILE_NUMBER,PASSWORD,USER_ID,USER_ROLE,DOB,GENDER,LAST_NAME,QUALIFICATION,CREATED_BY,CREATED_DATE,MODIFIED_BY,LAST_MODIFIED_DATE,USER_TEAM,REPORTERTING_MANAGER,NEED_PASSWORD_RESET) values (1000,'SuperAdmin','superAdmin','BM7-5BjYdhCkFUrhOf-0p0qrAx8cVLNl','9686301304','1GOPbCCKZlGolkKrnN8ZDQ==','admin@famstack.com','SUPERADMIN',null,'male','kumar','Qualification 1',112333,null,1244,null,'Team 1',1000,0);
Insert into bops.user_info (ID,DESIGNATION,FIRST_NAME,HASHKEY,MOBILE_NUMBER,PASSWORD,USER_ID,USER_ROLE,DOB,GENDER,LAST_NAME,QUALIFICATION,CREATED_BY,CREATED_DATE,MODIFIED_BY,LAST_MODIFIED_DATE,USER_TEAM,REPORTERTING_MANAGER,NEED_PASSWORD_RESET) values (1001,'Manager','Admin','BM7-5BjYdhCkFUrhOf-0p0qrAx8cVLNl','9686301304','1GOPbCCKZlGolkKrnN8ZDQ==','manager@famstack.com','ADMIN',null,'male','kumar','Qualification 1',112333,null,1244,null,'Team 1',1000,0);
