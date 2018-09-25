package cn.ZHtochs.dao;

import cn.ZHtochs.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() {
        long id=1000L;
        long phone =13717556875L;
        int insertSuccess =successKilledDao.insertSuccessKilled(id,phone);
        System.out.println(insertSuccess+"**********************");
    }

    @Test
    public void queryByIdWithSeckill() {
        long id=1000L;
        long phone =13717556875L;
        SuccessKilled successKilled=successKilledDao.queryByIdWithSeckill(id,phone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }
}