Customer Micro-service

1. Execute the following sql queries to create the database in MySQL

create table customers (id bigint not null auto_increment, email varchar(255), firstname varchar(255), lastname varchar(255), username varchar(255), primary key (id)) engine=InnoDB
alter table customers add constraint UK_bepynu3b6l8k2ppuq6b33xfxc unique (username)

2. Update the username and password in the properties files accordingly

3. Supply the profile(dev or qa) and run the service as Spring Boot application