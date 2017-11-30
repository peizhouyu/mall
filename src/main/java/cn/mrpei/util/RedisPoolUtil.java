package cn.mrpei.util;

import cn.mrpei.cache.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Date:2017/11/29
 * Time:15:19
 *
 * @author 裴周宇
 */
@Slf4j
public class RedisPoolUtil {

    /**
     * 设置 key的有效期 单位是秒
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key, int exTime){
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.expire(key,exTime);
        } catch (Exception e) {
            log.error("setEx key:{}  error",key,e);
            RedisPool.returnBrokenResource(jedis);
            return null;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    //ex单位是秒
    public static String setEx(String key, String value, int exTime){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.setex(key,exTime,value);
        } catch (Exception e) {
            log.error("setEx key:{} value:{} error",key,value,e);
            RedisPool.returnBrokenResource(jedis);
            return null;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static String set(String key, String value){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.set(key,value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error",key,value,e);
            RedisPool.returnBrokenResource(jedis);
            return null;
        }
        RedisPool.returnResource(jedis);
        return result;
    }


    public static String get(String key){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("set key:{} error",key,e);
            RedisPool.returnBrokenResource(jedis);
            return null;
        }
        RedisPool.returnResource(jedis);
        return result;
    }


    public static Long del(String key){
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error",key,e);
            RedisPool.returnBrokenResource(jedis);
            return null;
        }
        RedisPool.returnResource(jedis);
        return result;
    }


//    public static void main(String[] args) {
//        Jedis jedis = RedisPool.getJedis();
//
//        RedisPoolUtil.set("keyTest","pei");
//        String value = RedisPoolUtil.get("keyTest");
//        System.out.println(value);
//
//        RedisPoolUtil.setEx("keyex","keyexxx",60*10);
//
//        RedisPoolUtil.expire("keyTest",60*20);
//
//        RedisPoolUtil.del("keyex");
//
//        System.out.println("end");
//    }




}
