create database demo;

use demo;

create table customer(
	id bigint primary key,
	name varchar(100),
	contact varchar(100) comment '联系人',
	telephone varchar(20),
	email varchar(50),
	remark varchar(200)
)comment='客户表';

insert into customer values (1, 'zhangsan', '张三', '17798749587', '12345@126.com', ''),
(2, 'lisi', '李四', '18198749585', 'lisi@126.com', '');