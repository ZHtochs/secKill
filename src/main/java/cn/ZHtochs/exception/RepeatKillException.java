package cn.ZHtochs.exception;

/**重复秒杀异常，应该是运行时异常
 * @program: seckill
 * @author: zhuhe
 * @create: 2018-09-25 11:33
 **/
public class RepeatKillException extends RuntimeException{
    public RepeatKillException(String message) {

        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}