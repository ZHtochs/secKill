package cn.ZHtochs.service;

import cn.ZHtochs.dto.Exposer;
import cn.ZHtochs.dto.SeckillExecution;
import cn.ZHtochs.entity.Seckill;

import java.util.List;

/**
 * 业务接口：站在使用者的角度
 *
 * @program: seckill
 * @author: zhuhe
 * @create: 2018-09-24 23:29
 **/
public interface SeckillService {
    List<Seckill> getSeckillList();

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

    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws Exception;

}