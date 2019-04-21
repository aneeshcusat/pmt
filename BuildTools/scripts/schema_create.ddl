create user bops identified by bops1234;
grant create session, grant any privilege to bops;
grant create table to bops;
grant unlimited tablespace to bops;
grant ALL PRIVILEGES to bops;

C:\apps\mysql5.6\bin>mysqld.exe --skip-grant-tables
C:\apps\mysql5.6\bin>mysql.exe -u root -p
>no password
update mysql.user set password=PASSWORD('bops1234') where user='root';
---root password bops1234

show variables like 'max_connections';
SET GLOBAL max_connections = 10000;



mysql 8 installation (https://dev.mysql.com/doc/refman/8.0/en/data-directory-initialization.html)
donwload zip
C:\apps\mysql-8.0.15-winx64\bin>mysqld.exe --initialize-insecure
C:\apps\mysql-8.0.15-winx64\bin>mysql -u root --skip-password
---mysql> ALTER USER 'root'@'localhost' IDENTIFIED BY 'bops1234';

to support workbench login
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'bops1234';

CREATE USER 'bops'@'%' IDENTIFIED BY 'bops1234';
ALTER USER 'bops'@'%' IDENTIFIED WITH mysql_native_password BY 'bops1234';

CREATE DATABASE bops;
GRANT ALL PRIVILEGES ON *.* TO 'bops'@'%' WITH GRANT OPTION;
SET GLOBAL max_connections = 10000;