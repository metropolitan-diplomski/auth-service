create table role
(
    id   varchar(255) not null
        constraint role_pkey
            primary key,
    role_name varchar(255),
    role varchar(255)
);

create table users
(
    id          varchar(255) not null
        constraint users_pkey
            primary key,
    email       varchar(255),
    full_name   varchar(255),
    password    varchar(255),
    username    varchar(255),
    create_data timestamp ,
    modified_date timestamp
);

create table user_role
(
    user_id varchar(255) not null
        constraint fkj345gk1bovqvfame88rcx7yyx
            references users,
    role_id varchar(255) not null
        constraint fka68196081fvovjhkek5m97n3y
            references role,
    constraint user_role_pkey
        primary key (user_id, role_id)
);