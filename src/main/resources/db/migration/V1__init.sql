create sequence paper_sequence start with 1 increment by 50;

create table temp_account
(
    id                 int8         not null,
    email              varchar(255) not null,
    password           varchar(255) not null,
    username           varchar(16)  not null,
    verification_token varchar(255) not null,
    primary key (id)
);

create table account
(
    id       int8         not null,
    email    varchar(255) not null,
    password varchar(255) not null,
    greeting varchar(255) not null,
    role     varchar(5)   not null,
    username varchar(16)  not null,
    primary key (id)
);

create table persistent_logins
(
    series    varchar(64) not null,
    last_used timestamp   not null,
    token     varchar(64) not null,
    username  varchar(64) not null,
    primary key (series)
);

create table notification
(
    id         int8         not null,
    checked    boolean      not null,
    created_at timestamp    not null,
    message    varchar(255) not null,
    receiver   varchar(16)  not null,
    account_id int8,
    primary key (id)
);

create table magazine
(
    id                int8        not null,
    created_at        timestamp   not null,
    name              varchar(16) not null,
    point             int4        not null,
    title             varchar(20) not null,
    long_explanation  varchar(255),
    short_explanation varchar(30) not null,
    count             int4        not null,
    primary key (id)
);

create table exclusive_magazine
(
    id int8 not null,
    primary key (id)
);

create table managed_magazine
(
    id int8 not null,
    primary key (id)
);

create table member
(
    id          int8        not null,
    name        varchar(16) not null,
    magazine_id int8,
    primary key (id)
);

create table open_magazine
(
    id int8 not null,
    primary key (id)
);

create table writer
(
    id          int8        not null,
    count       int4        not null,
    name        varchar(16) not null,
    magazine_id int8,
    primary key (id)
);

create table topic
(
    id                int8        not null,
    created_at        timestamp   not null,
    name              varchar(16) not null,
    point             int4        not null,
    title             varchar(20) not null,
    count             int4        not null,
    long_explanation  varchar(255),
    short_explanation varchar(30) not null,
    magazine_id       int8,
    primary key (id)
);

create table article
(
    id         int8        not null,
    created_at timestamp   not null,
    name       varchar(16) not null,
    point      int4        not null,
    title      varchar(20) not null,
    content    text,
    publicized boolean     not null,
    read       int4        not null,
    topic_id   int8,
    primary key (id)
);

create table reply
(
    id         int8         not null,
    content    varchar(255) not null,
    created_at timestamp,
    name       varchar(16)  not null,
    article_id int8,
    primary key (id)
);

create table likes
(
    id         int8        not null,
    username   varchar(16) not null,
    article_id int8,
    primary key (id)
);

create table article_tag
(
    id         int8 not null,
    article_id int8,
    tag_id     int8,
    primary key (id)
);

create table tag
(
    id   int8        not null,
    name varchar(16) not null,
    primary key (id)
);

alter table if exists temp_account
    add constraint UK_trontjpqt87axli22ixkxoqk1 unique (email);

alter table if exists temp_account
    add constraint UK_oaev618l3ome0p23lsvemiqwq unique (username);

alter table if exists account
    add constraint UK_q0uja26qgu1atulenwup9rxyr unique (email);

alter table if exists account
    add constraint UK_gex1lmaqpg0ir5g1f5eftyaa1 unique (username);

alter table if exists tag
    add constraint UK_1wdpsed5kna2y38hnbgrnhi5b unique (name);

alter table if exists notification
    add constraint FKj0b1ncedmpl7sx7t7o54t26v2
    foreign key (account_id)
    references account;

alter table if exists exclusive_magazine
    add constraint FK5ah94j746hxi7k200efy056m8
    foreign key (id)
    references magazine;

alter table if exists managed_magazine
    add constraint FKnbo1ij1gsgxv1qwtdm9aeljd0
    foreign key (id)
    references magazine;

alter table if exists member
    add constraint FKs7785eat8ldp6k65t31ulua0t
    foreign key (magazine_id)
    references managed_magazine;

alter table if exists open_magazine
    add constraint FK293ds32mi4p3xingfjrgurh6y
    foreign key (id)
    references magazine;

alter table if exists writer
    add constraint FKmjiujo6cs8n0m3vx1xtmdmrgi
    foreign key (magazine_id)
    references open_magazine;

alter table if exists topic
    add constraint FKebscssdj4dldjk6sg75ll9xm0
    foreign key (magazine_id)
    references magazine;

alter table if exists article
    add constraint FK6x3cr4vpqhjktvuju4u1f77q1
    foreign key (topic_id)
    references topic;

alter table if exists reply
    add constraint FK77b1kppqpd0yxs2wl8u8fddma
    foreign key (article_id)
    references article;

alter table if exists likes
    add constraint FK1hlv6urq91y6fqfg6bds5gvp9
    foreign key (article_id)
    references article;

alter table if exists article_tag
    add constraint FKenqeees0y8hkm7x1p1ittuuye
    foreign key (article_id)
    references article;

alter table if exists article_tag
    add constraint FKesqp7s9jj2wumlnhssbme5ule
    foreign key (tag_id)
    references tag;
