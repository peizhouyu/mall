package cn.mrpei.task;

import cn.mrpei.cache.redisson.RedissonManager;
import cn.mrpei.common.Const;
import cn.mrpei.service.OrderService;
import cn.mrpei.util.PropertiesUtil;
import cn.mrpei.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * Date:2017/12/19
 * Time:14:17
 *  定时关闭订单 任务
 * @author 裴周宇
 */
@Component
@Slf4j
public class CloseOrderTask {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedissonManager redissonManager;

    //采用非杀死进程的方式关闭tomcat前的回调 让Tomcat关闭前释放锁 防止死锁
    @PreDestroy
    public void delLock(){
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TAST_LOCK);
    }


    //单应用处理  非分布式方法
    //@Scheduled(cron = "0 */1 * * * ?") //1分钟的整数倍执行
    public void closeOrderTaskV1(){
        log.info("关闭订单定时任务启动");
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
       // orderService.closeOrder(hour);
        log.info("关闭订单定时任务结束");
    }


    //支持分布式系统 但是在一定情况下(尽管已经做了一定的防死锁处理delLock() ) 仍然会造成死锁 ex：kill tomcat 进程
    //@Scheduled(cron = "0 */1 * * * ?") //1分钟的整数倍执行
    public void closeOrderTaskV2(){
        log.info("关闭订单定时任务启动");
        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout","5000"));
        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TAST_LOCK,String.valueOf(System.currentTimeMillis() + lockTimeout));
        if (setnxResult != null && setnxResult.intValue() == 1){
            //如果是1 代表设置成功，获取锁
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TAST_LOCK);
        }else {
            log.info("没有获得分布式锁:{}", Const.REDIS_LOCK.CLOSE_ORDER_TAST_LOCK);
        }
        log.info("关闭订单定时任务结束");
    }

    //@Scheduled(cron = "0 */1 * * * ?") //1分钟的整数倍执行
    public void closeOrderTaskV3(){
        log.info("关闭订单定时任务启动");
        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout","5000"));
        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TAST_LOCK,String.valueOf(System.currentTimeMillis() + lockTimeout));
        if (setnxResult != null && setnxResult.intValue() == 1) {
            //如果是1 代表设置成功，获取锁
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TAST_LOCK);
        }else {
            //未获取到锁，继续判断，判断时间戳，看是否可以重置并获取到锁
            String lockValueStr = RedisShardedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TAST_LOCK);
            if (lockValueStr != null && System.currentTimeMillis() > Long.parseLong(lockValueStr)){
                String getSetResult = RedisShardedPoolUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TAST_LOCK,String.valueOf(System.currentTimeMillis() + lockTimeout));
                //再次用当前时间戳getSet
                if (getSetResult == null || (getSetResult != null && StringUtils.equals(lockValueStr,getSetResult))){
                    //获取到分布式锁
                    closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TAST_LOCK);
                }else {
                    log.info("没有获取到分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TAST_LOCK);
                }

            }else{
                log.info("没有获取到分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TAST_LOCK);
            }
        }
        log.info("关闭订单定时任务结束");
    }




    @Scheduled(cron = "0 */1 * * * ?") //1分钟的整数倍执行
    public void closeOrderTaskV4(){
        RLock lock = redissonManager.getRedisson().getLock(Const.REDIS_LOCK.CLOSE_ORDER_TAST_LOCK);
        boolean getLock = false;
        try {
            if (getLock = lock.tryLock(0,50, TimeUnit.SECONDS)){
                log.info("Redisson获取到分布式锁:{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TAST_LOCK,Thread.currentThread().getName());
                int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
                orderService.closeOrder(hour);
            }else {
                log.info("Redisson没有获取到分布式锁:{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TAST_LOCK,Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("Redisson分布式锁获取异常",e);
        }finally {
            if (!getLock){
                return;
            }
            lock.unlock();
            log.info("Redisson分布式锁释放");
        }
    }

    private void closeOrder(String lockName){
        //防止死锁
        RedisShardedPoolUtil.expire(lockName,50);
        log.info("获取{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TAST_LOCK,Thread.currentThread().getName());
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
        orderService.closeOrder(hour);
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TAST_LOCK);
        log.info("释放{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TAST_LOCK,Thread.currentThread().getName());
        log.info("====================================");
    }

}
