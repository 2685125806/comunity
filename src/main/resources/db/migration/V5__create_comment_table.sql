create table comment
(
	id BIGINT auto_increment,
	parent_id BIGINT not null,
	type int not null,
	commentor int not null,
	gmt_create BIGINT not null,
	gmt_update BIGINT not null,
	likeCount BIGINT default 0 ,
	constraint comment_pk
		primary key (id)
);