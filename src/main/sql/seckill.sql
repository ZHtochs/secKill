-- 秒杀执行的存储过程
DELIMITER $$ -- ;console ;转换$$
-- 定义存储过程
-- 参数：in输入参数 out 输出参数
-- count返回上一条修改类型sql的影响行数
-- row_count 0未修改数据 >0表示修改的行数，<0表示sql错误/未执行sql
CREATE PROCEDURE `seckill`.`execute_seckill`
  (
    IN  v_seckill_id BIGINT,
    IN  v_phone      BIGINT,
    IN  v_kill_time  TIMESTAMP,
    OUT r_result     INT)
  BEGIN
    DECLARE insert_count INT DEFAULT 0;
    START TRANSACTION;
    INSERT IGNORE INTO success_killed
    (seckill_id, user_phone, create_time)
    VALUES (v_seckill_id, v_phone, v_kill_time);
    SELECT row_count()
    INTO insert_count;
    IF (insert_count = 0)
    THEN
      ROLLBACK;
      SET r_result = -1;
    -- 重复
    ELSEIF (insert_count < 0)
      THEN
        ROLLBACK;
        SET r_result = -2;
    -- 出错
    ELSE
      UPDATE seckill
      SET number = number - 1
      WHERE seckill_id = v_seckill_id
            AND end_time > v_kill_time
            AND start_time < v_kill_time
            AND number > 0;
      SELECT row_count
      INTO insert_count;
      IF (insert_count = 0)
      THEN
        ROLLBACK;
        SET r_result = 0;
      ELSEIF (insert_count < 0)
        THEN
          ROLLBACK;
          SET r_result = -2;
      ELSE
        COMMIT;
        SET r_result = 1;
      END IF;
    END IF;
  END;
$$
-- 存储过程定义结束
DELIMITER ;
SET @r_result = -3;
-- 执行存储过程
CALL execute_seckill(1003, 11111222223, now(), @r_result);
-- 获取结果
SELECT  @r_result;