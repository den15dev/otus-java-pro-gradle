-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence client_seq start with 1 increment by 1;

create table client
(
    id bigint not null primary key,
    email varchar(100) not null unique,
    password varchar(100) not null,
    role varchar(100) default 'user',
    name varchar(100)
);
