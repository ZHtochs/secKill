package cn.ZHtochs.exception;

/**秒杀关闭异常
 * @program: seckill
 * @author: zhuhe
 * @create: 2018-09-25 14:51
 **/
public class SeckillColseException extends RuntimeException {
    public SeckillColseException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeckillColseException(String message) {

        super(message);
    }
}