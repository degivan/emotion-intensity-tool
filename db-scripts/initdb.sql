create table tweets (
    id varchar(255) not null,
    posted_at date not null,
    text varchar(255) not null,
    intensities varchar(255) default null,
    primary key (id)
);