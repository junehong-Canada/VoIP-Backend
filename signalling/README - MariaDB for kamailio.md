# MariaDB for Kamailio
```
$ mysql -u root -p
Enter password: *****

MariaDB> show databases;

MariaDB> create database *****;

MariaDB> use mysql;
MariaDB> select host, user, password from user;
+-----------+--------------+-------------------------------------------+
| Host      | User         | Password                                  |
+-----------+--------------+-------------------------------------------+
| localhost | mariadb.sys  |                                           |
| localhost | root         | *1F16FA302FD89C60F2FC5826159F389D8D81610D |
| june-pc   | root         | *CBDFB35C15B43BA412BB375DB5D39C4120371C4A |
| 127.0.0.1 | root         | *CBDFB35C15B43BA412BB375DB5D39C4120371C4A |
| ::1       | root         | *CBDFB35C15B43BA412BB375DB5D39C4120371C4A |
| %         | root         | *CBDFB35C15B43BA412BB375DB5D39C4120371C4A |
| localhost | mariadb_user | *645C22F3B7F72880489B4E14C4B13B413FD0D27D |
+-----------+--------------+-------------------------------------------+

*** Add user
MariaDB> create user '계정아이디'@'접속위치' identified by '패스워드';
ex. create user 'user1'@'%' identified by 'user!@#$';
MariaDB [mysql]> create user 'skychatuc'@'%' identified by 'skychatucrw';

MariaDB> grant all privileges on DB이름.테이블 to '계정아이디'@'접속위치';
ex. grant all privileges on testDB.* to 'user1'@'localhost'; //localhost 는 내부에서만 접속가능
    grant all privileges on testDB.* to 'user1'@'%';
MariaDB [mysql]> grant all privileges on kamailio.* to 'skychatuc'@'%';

*** Show user info.
MariaDB> show grants for 'user1'@'접속위치';
MariaDB [mysql]> show grants for 'skychatuc'@'%';

*** Delete user
MariaDB> drop user '계정아이디'@'접속위치';
ex. drop user 'user1'@'%';

권한 삭제
MariaDB> revoke all on DB이름.테이블 FROM '계정아이디'@'접속위치';
MariaDB [mysql]> revoke all on kamailio.subscriber from 'skychatuc'@'%';
MariaDB [mysql]> revoke drop on kamailio.subcriber from `skychatuc`@`%`;

*** Reset All databases and add access account for proxy
sudo systemctl stop mysql
sudo rm -rf /var/lib/mysql/*
sudo systemctl start mysql
```
