-- create schema pet_app;

-- select 'drop table ' || table_name || ' cascade;' from information_schema.tables where table_schema='pet_app';


drop table pet cascade;
drop table pet_owner cascade;
drop table person cascade;
/*
	block comment
*/
create table status (
	id serial primary key,
	name varchar(50)
);

create table pet (
	id serial primary key,
	name varchar(100) not null,
	species varchar(100) not null,
	description varchar(250) not null,
	age integer not null,
	status_id integer not null references status
);

create table user_role (
	id serial primary key,
	name varchar(50)
);

cReAte tABle peRsOn (
	id serial primary key,
	full_name varchar(100) not null,
	username varchar(30) unique not null,
	passwd varchar(45) not null,
	role_id integer not null references user_role
);

create table pet_owner (
	pet_id integer references pet,
	owner_id integer references person
);

-- alter table person add favorite_color varchar(10) not null;
-- drop table person cascade;
-- truncate table person;