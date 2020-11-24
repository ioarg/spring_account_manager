use homeapps_sec;

drop table if exists users;
drop table if exists authorities;

create table authorities (
    id int not null primary key auto_increment,
	roles varchar(50) not null
);

create table users(
    id int not null primary key auto_increment,
	username varchar(50) not null,
	pass_word varchar(68) not null,
	first_name varchar(50) not null,
	last_name varchar(50),
	enabled boolean not null,
    authority_id int not null,
	constraint fk_user_authorities
	foreign key(authority_id)
	    references authorities(id)
	    on delete cascade
);

insert into authorities
values (1, "ROLE_USER");

insert into users 
values (1,
		"test",
		"$2a$12$s8E4se4znoYOrIwQK7jQbeXMgKxvvb5QkSSbsOdH7EjQNQZrB5z82",
		"John",
		"Mayers",
		1,
        1);
        
