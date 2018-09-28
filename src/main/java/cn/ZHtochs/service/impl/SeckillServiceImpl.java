package cn.ZHtochs.service.impl;

import cn.ZHtochs.dao.SeckillDao;
import cn.ZHtochs.dao.SuccessKilledDao;
import cn.ZHtochs.dao.cache.RedisDao;
import cn.ZHtochs.dto.Exposer;
import cn.ZHtochs.dto.SeckillExecution;
import cn.ZHtochs.entity.Seckill;
import cn.ZHtochs.entity.SuccessKilled;
import cn.ZHtochs.enums.SeckillStatEnum;
import cn.ZHtochs.exception.RepeatKillException;
import cn.ZHtochs.exception.SeckillColseException;
import cn.ZHtochs.exception.SeckillException;
import cn.ZHtochs.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * @program: seckill
 * @author: zhuhe
 * @create: 2018-09-25 15:03
 **/
@Service
public class SeckillServiceImpl implements SeckillService {
    //日志
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    //注入service依赖
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
    @Autowired
    private RedisDao redisDao;
    //用于混淆md5
    private final String slat = "dsfs@#$NJ@!L<)L. EA+*/";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        //使用redis缓存，降低MySQL压力
        //1.访问redi
        Seckill seckill=redisDao.getSeckill(seckillId);
        if (seckill == null) {
            //2.访问数据库
            seckill=seckillDao.queryById(seckillId);
            if (seckill==null){
                return new Exposer(false,seckillId);
            }else {
                redisDao.putSeckill(seckill);
            }
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //当前系统时间
        Date nowTime = new Date();
        //秒杀还没开始和秒杀已经结束
        if (nowTime.getTime() < startTime.getTime()
                || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());

        }
        //转化特定字符串的过程，不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        System.out.println(base);
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    /**
     * 执行秒杀的方法
     *使用注解控制事务
     * @Param: [seckillId, userPhone, md5]
     * @return: cn.ZHtochs.dto.SeckillExecution
     */

    @Override
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillColseException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        //执行秒杀逻辑。减库存+记录购买行为
        Date nowTime = new Date();
        int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
        try {
            if (updateCount <= 0) {
                throw new SeckillColseException("seckill is colsed");

            } else {
                //记录购买行为,用的insert ignore，当已经有记录的时候为0
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                if (insertCount <= 0) {
                    throw new RepeatKillException("seckill repeated");
                } else {
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillColseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch
                (Exception e) {
            logger.error(e.getMessage(), e);
            //所有编译期异常转换为运行期异常
            throw new SeckillException("seckill inner error" + e.getMessage());
        }
    }
}