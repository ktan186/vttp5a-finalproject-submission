drop database if exists finalproject;

create database finalproject;
use finalproject;

create table carparks (
    carpark_id varchar(64) not null,
    carpark_name varchar(128) not null,
    latitude double not null,
    longitude double not null,
    agency char(3) not null,

    constraint pk_carpark_id primary key(carpark_id)
);
 
create table users (
    username varchar(64) not null,
    email varchar(128) not null,
    password varchar(64) not null,
    role varchar(64) not null,

    constraint pk_user_id primary key(username)
);

create table favourite_carparks (
    username varchar(64) not null,
    carpark_id varchar(64) not null,

    constraint pk_user_id primary key(username, carpark_id), 
    constraint fk_user_id foreign key(username) references users(username) on delete cascade, 
    constraint fk_carpark_id foreign key(carpark_id) references carparks(carpark_id) on delete cascade
);

create table user_parking_sessions (
    session_id varchar(64) not null, 
    username varchar(64) not null, 

    constraint pk_session_id primary key(session_id),
    constraint fk_user_parking foreign key(username) references users(username) on delete cascade
);


create table parking_details (
    session_id varchar(64) not null, 
    carpark_location varchar(128), 
    level int, 
    deck char(10), 
    park_start_time timestamp, 
    notes text, 

    constraint pk_parking_session_id primary key(session_id),
    constraint fk_parking_session foreign key(session_id) references user_parking_sessions(session_id) on delete cascade
);


grant all privileges on finalproject.* to 'fred'@'%';
