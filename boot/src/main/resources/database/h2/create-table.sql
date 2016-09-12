CREATE TABLE `users` (
`username`  varchar(50) NULL ,
`password`  varchar(50) NULL ,
`password_salt`  varchar(50) NULL 
)
;

CREATE TABLE `user_roles` (
`username`  varchar(50) NULL ,
`role_name`  varchar(50) NULL 
)
;

CREATE TABLE `roles_permissions` (
`permission`  varchar(50) NULL ,
`role_name`  varchar(50) NULL 
)
;

CREATE TABLE `oauth2_client` (
`client_name`  varchar(50) NULL ,
`client_id`  varchar(50) NULL ,
`client_secret`  varchar(50) NULL 
)
;

insert into users(username,password,password_salt) values ('u1','p1','ps1'),('u2','p2','ps2');


insert into user_roles(username,role_name) values ('u1','r1'),('u2','r2');

insert into roles_permissions(role_name,permission) values ('r1','p1'),('r2','p2'),('r1','p2'),('r2','p3');

insert into oauth2_client(client_name,client_id,client_secret) values ('cn1','ci1','cs1'),('cn2','ci2','cs2');




