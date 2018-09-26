package cn.ZHtochs.service;

import cn.ZHtochs.dto.Exposer;
import cn.ZHtochs.dto.SeckillExecution;
import cn.ZHtochs.entity.Seckill;
import cn.ZHtochs.exception.RepeatKillException;
import cn.ZHtochs.exception.SeckillColseException;
import cn.ZHtochs.exception.SeckillException;

import java.util.List;

/**
 * 业务接口：站在使用者的角度
 *
 * @program: seckill
 * @author: zhuhe
 * @create: 2018-09-24 23:29
 **/
public interface SeckillService {
    /**
     * 查询所有秒杀记录
     *
     * @Param: []
     * @return: java.util.List<cn.ZHtochs.entity.Seckill>
     */

    List<Seckill> getSeckillList();

    /**查询单个秒杀记录
     * @Param: [seckillId]
     * @return: cn.ZHtochs.entity.Seckill
    */

    Seckill getById(long seckillId);

    /**
     * 输出秒杀的接口地址否则输出系统时间和秒杀时间
     *
     * @Param: [seckillId]
     * @return: void
     */
    Exposer exportSeckillUrl(long seckillId);
    /**
     * 执行秒杀操作
    * @Param: [seckillId, userPhone, md5]
    * @return: void
     */

    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws  RepeatKillException, SeckillColseException,SeckillException;

}