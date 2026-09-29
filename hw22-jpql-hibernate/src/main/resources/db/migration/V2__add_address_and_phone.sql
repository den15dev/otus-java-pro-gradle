create sequence address_seq start with 1 increment by 1;
create sequence phone_seq start with 1 increment by 1;

create table address
(
    id     bigint not null primary key,
    street varchar(255)
);

alter table client
    add column address_id bigint;

alter table client
    add constraint fk_client_address
        foreign key (address_id)
            references address (id);

create table phone
(
    id        bigint not null primary key,
    number    varchar(50),
    client_id bigint not null,

    constraint fk_phone_client
        foreign key (client_id)
            references client (id)
);