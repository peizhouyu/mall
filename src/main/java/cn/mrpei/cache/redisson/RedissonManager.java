package cn.mrpei.cache.redisson;

import cn.mrpei.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Date:2017/12/19
 * Time:20:04
 *
 * @author 裴周宇
 */
@Component
@Slf4j
public class RedissonManager {

    private Config config = new Config();

    private Redisson redisson = null;

    public Redisson getRedisson() {
        return redisson;
    }

    //IP
    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");
    //port
    private static Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));
    //password
    private static String redis1Password = PropertiesUtil.getProperty("redis1.password");


    @PostConstruct
    private void init(){
        try {
            config.useSingleServer().setAddress(new StringBuilder().append(redis1Ip).append(":").append(redis1Port).toString()).setPassword(redis1Password);
            redisson = (Redisson) Redisson.create(config);
            log.info("初始化Redisson结束");
        } catch (Exception e) {
            log.info("redisson init error", e);
        }
    }

}
