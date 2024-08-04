
drop table if exists user_roles;
drop table if exists users;
drop table if exists roles;

create table if not exists roles (
    id int auto_increment,
    name varchar(100) not null,
    primary key (id)
);

create table if not exists users (
    id int auto_increment,
    email varchar(100) not null UNIQUE,
    name varchar(100) not null,
    password varchar(100) not null,
    primary key (id)
);

create table if not exists user_roles (
    user_id int,
    role_id int,
    foreign key (user_id) references users(id),
    foreign key (role_id) references roles(id),
    primary key(user_id, role_id)
);

# INSERT INTO users(name, password, email)
# values ('123', '123', '123@1');
#
# INSERT INTO roles(name)
# values ('ROLE_ADMIN'),
#        ('ROLE_USER');
#
# INSERT INTO user_roles(user_id, role_id)
# values
#     (1, 1),
#     (1, 2);


SELECT * FROM users;
SELECT * FROM user_roles;
SELECT * FROM roles;
