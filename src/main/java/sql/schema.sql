--创建数据库seckill
create DATABASE seckill
--创建数据表
CREATE TABLE `seckill` (
  `seckill_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
  `name` varchar(120) NOT NULL COMMENT '商品名称',
  `number` int(11) NOT NULL COMMENT '数量',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '秒杀开始时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '秒杀结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`seckill_id`),
  KEY `idx_start_time` (`start_time`) USING BTREE,
  KEY `idx_end_time` (`end_time`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品库存表'
----插入测试数据，秒杀商品列表
insert into seckill(name,number,start_time,end_time) values
('1000元秒杀iphone6',100,'2016-05-30 00:00:00','2016-06-30 00:00:00'),
('300元秒杀小米4',200,'2016-05-30 00:00:00','2016-06-30 00:00:00'),
('200元秒杀红米note',300,'2016-05-30 00:00:00','2016-06-30 00:00:00');
	
	
-- 秒杀成功明细表
CREATE TABLE `success_killed` (
  `seckill_id` int(10) unsigned NOT NULL COMMENT '秒杀商品id',
  `user_phone` bigint(11) NOT NULL COMMENT '用户手机号',
  `state` tinyint(1) NOT NULL DEFAULT '-1' COMMENT '秒杀状态 -1：无效 0：成功 1：已付款 2：已发货',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`seckill_id`,`user_phone`),
  KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表'

-- 连接数据库控制台
mysql -uroot -p '123456'

-- ddl记录每次数据库表结构处理，如增删索引等
TRUNCATE `seckill`.`seckill`;--2016-05-26
TRUNCATE `seckill`.`success_killed`;--2016-05-26
--查看表结构及相关数据
show create table seckill
show create table success_killed
select seckill_id,name,number,start_time,end_time,create_time from seckill;
-- 简单优化秒杀操作，使用数据库存储过程来减少客户端执行事务时间和gc操作
-- 存储过程 
-- 参数：in 代表输入参数 name 名称 type 类型 out输出参数可以被赋值，调用时可以获取
-- row_count()返回上一条修改sql的影响行数 0:表示未修改数据 >0：表示修改行数 <0:未执行sql
delimiter $$

CREATE PROCEDURE `seckill`.`execute_seckill` (IN v_seckill_id bigint,in v_phone bigint,in v_kill_time timestamp,out r_result int)
BEGIN
    DECLARE insert_count int DEFAULT 0 ;
    START TRANSACTION;
    insert ignore into success_killed(seckill_id,user_phone,state,create_time) values(
        v_seckill_id,v_phone,0,v_kill_time);
    select row_count() into insert_count;
    IF (insert_count = 0) THEN
        ROLLBACK;
        set r_result = -1;
    ELSEIF (insert_count < 0) THEN
        ROLLBACK;
        set r_result = -2;
    ELSE 
        update seckill set number=number-1 where seckill_id=v_seckill_id
        and end_time > v_kill_time 
        and start_time <v_kill_time
        and number >0;
        select row_count() into insert_count;
        IF(insert_count =0) THEN 
            ROLLBACK;
            set r_result=0;
        ELSEIF (insert_count <0) THEN
            ROLLBACK;
            set r_result=-2;
        ELSE
            COMMIT;
            set r_result=1;
        END IF;
    END IF;
END;
$$
delimiter ;
set @r_result=-3;
--执行储存过程
call execute_seckill(1,13423435374,now(),@r_result);
select @r_result
--查看存储过程定义
show create procedure execute_seckill\G;
-- 储存过程优化事务行级锁持有时间
-- 不要过度依赖储存过程

-- 2016-06-15开始用户登录功能
create table `user`(
	`user_id` int(10) unsigned not null auto_increment comment '用户表自增主键',
	`name` varchar(120) not null comment '用户名',
	`nickname` varchar(120) null comment '用户昵称',
	`password` varchar(100) not null comment '密文密码',
	`icon` varchar(100) not null comment '用户头像',
	`in_used` tinyint(1) not null default 0 comment '是否有效',
	`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	primary key(`user_id`),
	key `idx_create_time` (`create_time`) using BTREE,
	key `idx_name` (`name`),
	key `idx_nickname` (`nickname`)
)engine=InnoDB default charset=utf8 comment '用户基本信息表';


select * from user;
select * from local_auth;

insert ignore into user(username,nickname,icon,in_used) values(
'jack','jack','/user/default/icon.icon',0)

insert ignore into local_auth(user_id,username,password) values(
1,'jack','123456')