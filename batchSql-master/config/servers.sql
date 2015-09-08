create table IF NOT EXISTS servers (
	id integer primary key AutoIncrement,
	name varchar(64) not null,
	host varchar(32) not null,
	port integer not null,
	user varchar(32) not null,
	pwd varchar(64) not null,
	db varchar(32) not null
)