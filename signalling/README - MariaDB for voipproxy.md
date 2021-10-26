# MariaDB for VoIP proxyserver
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
MariaDB [mysql]> create user 'skychatuc'@'%' identified by 'skychatucrw';
MariaDB [mysql]> grant all privileges on kamailio.* to 'skychatuc'@'%';

*** Show user info.
MariaDB [mysql]> show grants for 'skychatuc'@'%';

*** Delete user
MariaDB> drop user 'user1'@'%';
MariaDB [mysql]> revoke all on kamailio.subscriber from 'skychatuc'@'%';
MariaDB [mysql]> revoke drop on kamailio.subcriber from `skychatuc`@`%`;
```
