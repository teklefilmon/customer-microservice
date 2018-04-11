drop table if exists USERS;

create table USERS (ID int GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL, EMAIL varchar(255), FIRSTNAME varchar(255), LASTNAME varchar(255), USERNAME varchar(255), primary key (ID));
alter table USERS add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (USERNAME);


insert into USERS (FIRSTNAME, LASTNAME, EMAIL, USERNAME) values ('James', 'Alexander', 'james.alexander@nice.com', 'james.alexander');
insert into USERS (FIRSTNAME, LASTNAME, EMAIL, USERNAME) values ('Lucas', 'Samuel', 'lucas.samuel@nice.com', 'lucas.samuel');
insert into USERS (FIRSTNAME, LASTNAME, EMAIL, USERNAME) values ('Michael', 'Anthony', 'michael.anthony@nice.com', 'michael.anthony');