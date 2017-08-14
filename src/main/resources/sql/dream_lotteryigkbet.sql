create table lotteryigkbet
(
	id int auto_increment
		primary key,
	pid varchar(20) not null,
	acode varchar(50) not null,
	atime datetime not null,
	lotteryId int not null comment '彩票id'
)
;
