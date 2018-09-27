package cn.ZHtochs.web;

import cn.ZHtochs.dto.Exposer;
import cn.ZHtochs.dto.SeckillExecution;
import cn.ZHtochs.dto.SeckillResult;
import cn.ZHtochs.entity.Seckill;
import cn.ZHtochs.enums.SeckillStatEnum;
import cn.ZHtochs.exception.RepeatKillException;
import cn.ZHtochs.exception.SeckillColseException;
import cn.ZHtochs.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @program: seckill
 * @author: zhuhe
 * @create: 2018-09-26 15:15
 **/
@Controller
@RequestMapping(value = "/seckill")
public class SeckillController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        return "list";//web-inf/jsp/list.jsp
    }

    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckilledId, Model m) {
        if (seckilledId == null)
            return "redirect:/seckill/list";
        Seckill seckill = seckillService.getById(seckilledId);
        if (seckill == null)
            return "forward:/seckill/list";
        m.addAttribute("seckill", seckill);
        return "detail";
    }

    //ajax接口，返回json
    @ResponseBody
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = {"application/json;chartset=UTF-8"})
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new SeckillResult<Exposer>(false, e.getMessage());

        }
        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                  @PathVariable("md5") String md5,
                                                  @CookieValue(value = "killPhone", required = false) Long killPhone) {
        if (killPhone == null) {
            return new SeckillResult<SeckillExecution>(false, "未注册");
        }
        SeckillResult<SeckillExecution> result;
        try {
            SeckillExecution execution = seckillService.executeSeckill(seckillId,killPhone,md5);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (RepeatKillException e1) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (SeckillColseException e2) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
            return new SeckillResult<SeckillExecution>(true, execution);
        }
        catch (Exception e3) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(true, execution);
        }
    }
    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date date=new Date();
        return new SeckillResult<Long>(true,date.getTime());

    }
}