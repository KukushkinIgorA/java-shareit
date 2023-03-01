drop table if exists items, users, bookings, comments, requests;

create table if not exists users
(
    user_id  integer generated always as identity
        constraint "pk_users"
            primary key,
    name     varchar(32) not null,
    email    varchar(255) not null,
    constraint uq_user_email unique (email)
);

create table if not exists requests
(
    request_id     integer generated always as identity
        constraint pk_requests
            primary key,
    created timestamp without time zone not null,
    description         varchar(512) not null,
    requestor_id      integer     not null
        constraint requests_users_null_fk
            references users (user_id)
);

create table if not exists items
(
    item_id     integer generated always as identity
        constraint pk_items
            primary key,
    name        varchar(32) not null,
    description varchar(255),
    available   boolean default false,
    owner_id    integer     not null
        constraint items_users_null_fk
            references users (user_id),
    request_id integer
        constraint items_requests_null_fk
            references requests (request_id)
);

create table if not exists bookings
(
    booking_id     integer generated always as identity
        constraint pk_bookings
            primary key,
    start_date     timestamp without time zone not null,
    end_date       timestamp without time zone not null,
    status         varchar(16) not null,
    item_id        integer     not null
        constraint bookings_items_null_fk
            references items (item_id),
    booker_id      integer     not null
        constraint bookings_users_null_fk
            references users (user_id)
);

create table if not exists comments
(
    comment_id     integer generated always as identity
        constraint pk_comments
            primary key,
    created timestamp without time zone not null,
    text         varchar(512) not null,
    item_id        integer     not null
        constraint comments_items_null_fk
            references items (item_id),
    author_id      integer     not null
        constraint comments_users_null_fk
            references users (user_id)
);