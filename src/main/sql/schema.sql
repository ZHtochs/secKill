CREATE DATABASE secKill;
USE secKill;
CREATE TABLE secKill (
  secKill_id  BIGINT       NOT NULL AUTO_INCREMENT
  COMMENT '商品库存id',
  name        VARCHAR(120) NOT NULL
  COMMENT '商品名称',
number      INT          NOT NULL
  COMMENT '库存数量',
  create_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  end_time    TIMESTAMP    NOT NULL
  COMMENT '秒杀结束时间',
  start_time  TIMESTAMP    NOT NULL
  COMMENT '秒杀开启时间',
  PRIMARY KEY (secKill_id),
  KEY idx_start_time(start_time),
  KEY idx_end_time(end_time),
  KEY idx_create_time(create_time)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8
  COMMENT ='秒杀库存表';
  /*初始化数据*/
insert INTO seckill(name, number,  end_time, start_time)
    VALUES
      ('1000元秒杀1',100,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
      ('2000元秒杀2',200,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
      ('3000元秒杀3',300,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
      ('4000元秒杀4',400,'2015-11-01 00:00:00','2015-11-02 00:00:00');

/*--秒杀登录*/
CREATE  TABLE success_killed(
  secKill_id BIGINT NOT NULL COMMENT '秒杀活动表主键',
  user_phone BIGINT NOT NULL COMMENT '用户电话',
  state TINYINT NOT NULL DEFAULT -1 COMMENT '秒杀状态:-1无效，0， 成功,1 已付款 2 已发货',
  create_time TIMESTAMP NOT NULL  COMMENT '创建时间',
  PRIMARY KEY (secKill_id,user_phone),/*联合主键*/
  KEY idx_create_time(create_time)
)ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT '秒杀成功表';

-- connect msql

