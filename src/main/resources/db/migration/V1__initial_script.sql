create sequence author_seq start with 1 increment by 50;
create sequence book_seq start with 1 increment by 50;
create sequence order_item_seq start with 1 increment by 50;
create sequence orders_seq start with 1 increment by 50;
create sequence recipient_seq start with 1 increment by 50;
create sequence upload_seq start with 1 increment by 50;
create sequence users_seq start with 1 increment by 50;
create table author
(
    id         bigint not null,
    uuid       varchar(255),
    version    bigint not null,
    created_at timestamp(6),
    name       varchar(255),
    primary key (id)
);
create table book
(
    id         bigint not null,
    uuid       varchar(255),
    version    bigint not null,
    available  bigint,
    cover_id   bigint,
    created_at timestamp(6),
    price      numeric(38, 2),
    title      varchar(255),
    updated_at timestamp(6),
    year       integer,
    primary key (id)
);
create table book_authors
(
    books_id   bigint not null,
    authors_id bigint not null,
    primary key (books_id, authors_id)
);
create table order_item
(
    id       bigint  not null,
    uuid     varchar(255),
    version  bigint  not null,
    quantity integer not null,
    book_id  bigint,
    order_id bigint,
    primary key (id)
);
create table orders
(
    id           bigint not null,
    uuid         varchar(255),
    version      bigint not null,
    created_at   timestamp(6),
    delivery     varchar(255),
    status       varchar(255),
    updated_at   timestamp(6),
    recipient_id bigint,
    primary key (id)
);
create table recipient
(
    id       bigint not null,
    uuid     varchar(255),
    version  bigint not null,
    city     varchar(255),
    email    varchar(255),
    name     varchar(255),
    phone    varchar(255),
    street   varchar(255),
    zip_code varchar(255),
    primary key (id)
);
create table upload
(
    id           bigint not null,
    uuid         varchar(255),
    version      bigint not null,
    content_type varchar(255),
    created_at   timestamp(6),
    file         bytea,
    filename     varchar(255),
    primary key (id)
);
create table users
(
    id         bigint not null,
    uuid       varchar(255),
    version    bigint not null,
    created_at timestamp(6),
    password   varchar(255),
    updated_at timestamp(6),
    username   varchar(255),
    primary key (id)
);
create table users_roles
(
    user_id bigint not null,
    role    varchar(255)
);
alter table if exists book add constraint UK_g0286ag1dlt4473st1ugemd0m unique (title);
alter table if exists book_authors add constraint FK551i3sllw1wj7ex6nir16blsm foreign key (authors_id) references author;
alter table if exists book_authors add constraint FKmuhqocx8etx13u6jrtutnumek foreign key (books_id) references book;
alter table if exists order_item add constraint FKb033an1f8qmpbnfl0a6jb5njs foreign key (book_id) references book;
alter table if exists order_item add constraint FKt4dc2r9nbvbujrljv3e23iibt foreign key (order_id) references orders;
alter table if exists orders add constraint FKcxwo1jbmo15jih4b5qjclvye8 foreign key (recipient_id) references recipient;
alter table if exists users_roles add constraint FK2o0jvgh89lemvvo17cbqvdxaa foreign key (user_id) references users;
