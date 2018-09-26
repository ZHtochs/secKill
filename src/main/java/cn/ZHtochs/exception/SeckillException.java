package cn.ZHtochs.exception;

/**秒杀相关业务异常
 * @program: seckill
 * @author: zhuhe
 * @create: 2018-09-25 14:53
 **/
public class SeckillException extends RuntimeException{
    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeckillException(String message) {

        super(message);
    }
}