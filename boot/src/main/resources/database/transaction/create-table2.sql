 DROP TABLE IF EXISTS `oauth2_client`;

CREATE TABLE `oauth2_client` (
`client_name`  varchar(50) NULL ,
`client_id`  varchar(50) NULL ,
`client_secret`  varchar(50) NULL 
)
;



insert into oauth2_client(client_name,client_id,client_secret) values ('d2cn1','ci1','cs1'),('d2cn2','ci2','cs2');




