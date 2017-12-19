package cn.mrpei.cache;

import cn.mrpei.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2017/12/3
 * Time:13:06
 *      集群是个物理形态，分布式是个工作方式。
 * @author 裴周宇
 */

public class RedisShardedPool {

    //Shardedjedis 连接池
    private static ShardedJedisPool pool;

//    IP
    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");
    //port
    private static Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));
    //password
    private static String redis1Password = PropertiesUtil.getProperty("redis1.password");

    //IP
    private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");
    //port
    private static Integer redis2Port = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));
    //password
    private static String redis2Password = PropertiesUtil.getProperty("redis2.password");

//    @Value("${redis1.ip}")
//    private static String redis1Ip;
//    @Value("${redis1.port}")
//    private static Integer redis1Port;
//    @Value("${redis1.password}")
//    private static String redis1Password;
//
//    @Value("${redis2.ip}")
//    private static String redis2Ip;
//    @Value("${redis2.port}")
//    private static Integer redis2Port;
//    @Value("${redis2.password}")
//    private static String redis2Password;

    //最大连接数
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20"));
    //最大空闲连接数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10"));
    //最小空闲连接数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","2"));
    //获取连接实例前是否 验证连接实例可用性 true为可用
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow","true"));
    //归还连接实例前是否 验证连接实例可用性 true为可用
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return","true"));

    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
//        System.out.println(redis1Ip);
//        System.out.println(redis1Port);
//        System.out.println(redis1Password);
//        System.out.println(redis2Ip);
//        System.out.println(redis2Port);
//        System.out.println(redis2Password);

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        //连接耗尽时是否阻塞， false 会抛出异常 ，默认为 true 阻塞直到超时 超时异常
        config.setBlockWhenExhausted(true);

        JedisShardInfo info1 = new JedisShardInfo(redis1Ip,redis1Port,1000*2);
        info1.setPassword(redis1Password);
        JedisShardInfo info2 = new JedisShardInfo(redis2Ip,redis2Port,1000*2);
        info2.setPassword(redis2Password);

        List<JedisShardInfo> jedisShardInfoList = new ArrayList<JedisShardInfo>(2);
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);

        pool = new ShardedJedisPool(config,jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);

    }

    static {
        initPool();
    }

    public static ShardedJedis getJedis(){
        return pool.getResource();
    }

    public static void returnResource(ShardedJedis jedis){
        //if (jedis != null) 源码已做判断 下同
        pool.returnResource(jedis);
    }

    public static void returnBrokenResource(ShardedJedis jedis){
        pool.returnBrokenResource(jedis);
    }




}
