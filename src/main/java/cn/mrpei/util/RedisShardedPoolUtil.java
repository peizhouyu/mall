package cn.mrpei.util;

import cn.mrpei.cache.RedisPool;
import cn.mrpei.cache.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

/**
 * Date:2017/11/29
 * Time:15:19
 *
 * @author 裴周宇
 */
@Slf4j
public class RedisShardedPoolUtil {

    /**
     * 设置 key的有效期 单位是秒
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key, int exTime){
        ShardedJedis jedis = null;
        Long result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.expire(key,exTime);
        } catch (Exception e) {
            log.error("setEx key:{}  error",key,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    //ex单位是秒
    public static String setEx(String key, String value, int exTime){
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.setex(key,exTime,value);
        } catch (Exception e) {
            log.error("setEx key:{} value:{} error",key,value,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static String set(String key, String value){
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.set(key,value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error",key,value,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }


    public static String get(String key){
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("set key:{} error",key,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }


    public static Long del(String key){
        ShardedJedis jedis = null;
        Long result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error",key,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static Long setnx(String key, String value){
        ShardedJedis jedis = null;
        Long result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.setnx(key,value);
        } catch (Exception e) {
            log.error("setnx key:{} value:{} error",key,value,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static String getSet(String key, String value){
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.getSet(key,value);
        } catch (Exception e) {
            log.error("getSet key:{} value:{} error",key,value,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return null;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }






}
