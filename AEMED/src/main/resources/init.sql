drop table if exists aemet;
create table if not exists aemet(
    id char(36) primary key,
    date date not null,
    city varchar(255) not null,
    province varchar(255) not null,
    max_temp float not null,
    max_temp_time time not null,
    min_temp float not null,
    min_temp_time time not null,
    precipitation float not null
);

select * from aemet;