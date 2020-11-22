use homeapps_sec;

drop table authorities;
drop table users;
create table users(
	username varchar(50) not null primary key,
	password varchar(68) not null,
	enabled boolean not null
);

create table if not exists authorities (
	username varchar(50) not null,
	authority varchar(50) not null,
	constraint fk_authorities_users 
	foreign key(username) 
		references users(username)
        on delete cascade
);
        
create unique index ix_auth_username 
on authorities (username,authority);

insert into users 
values ("test", 
		"{bcrypt}$2y$12$FXwy9NvH6780O4rJmlSf5OPvbb0doAmdV8mlMBW934345vVc9kJSK",
		1),
        ("test2", "test", 1);
        
insert into authorities
values ("test", "ROLE_USER"),
("test2", "ROLE_USER")