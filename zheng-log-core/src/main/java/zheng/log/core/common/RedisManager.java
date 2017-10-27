package zheng.log.core.common;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by alan.zheng on 2017/10/27.
 */
@Component
public class RedisManager {
    private static Logger logger = LoggerFactory.getLogger(RedisManager.class);

    @Autowired
    private RedisTemplate redisTemplate;

    public void putObject(Object key, Object value) {
        Serializable serializeble = null;
        if (value instanceof java.io.Serializable) {
            serializeble = (Serializable) value;
        } else {
            logger.error("对象未序列化");
            return;
        }
        byte[] va = SerializationUtils.serialize(serializeble);
        redisTemplate.opsForValue().set(SerializationUtils.serialize(key.hashCode()), va);

    }


    public Object getObject(Object key) {
        Object value = null;
        Object o = redisTemplate.opsForValue().get(SerializationUtils
                .serialize(key.hashCode()));
        if (o!=null){
            value = SerializationUtils.deserialize((byte[])o);
        }
        return value;
    }

    public boolean removeObject(Object key) {
        try {
            return redisTemplate.expire(SerializationUtils.serialize(key.hashCode()), 0, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("removeObject redis 异常",e);
        }
        return false;
    }

    /**
     * 判断键是否存在
     *
     * @param key
     * @return
     */
    public boolean isExitByObjectKey(Object key) {
        try {
            return redisTemplate.hasKey(SerializationUtils.serialize(key.hashCode()));
        } catch (Exception e) {
            logger.error("isExitByObjectKey redis 异常",e);
        }
        return false;
    }

    /**
     * 判断键是否存在
     *
     * @param key
     * @return
     */
    public boolean isExit(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            logger.error("查询redis,失败key=" + key, e);
        }
        return false;
    }

    public void putString(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            logger.error("写入redis,失败key=" + key + " value=" + value, e);
        }
    }

    public String getString(String key) {
        try {
            Object obj = redisTemplate.opsForValue().get(key);
            if (obj!=null){
                return obj.toString();
            }
        } catch (Exception e) {
            logger.error("获取redis,失败key=" + key, e);
        }
        return null;
    }

    public void removeString(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            logger.error("redis,删除 key=" + key, e);
        }
    }

    /**
     * 设置 key 的有效时间，
     *
     * @param key
     * @param seconds 秒单位
     * @return
     */
    public boolean expire(String key, int seconds) {
        try {
            return redisTemplate.expire(key, seconds,TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("expire redis 异常",e);
        }
        return false;
    }

    public boolean hset(String key, String field, String xvalue) {
        try {
            redisTemplate.opsForHash().put(key, field, xvalue);
            return true;
        } catch (Exception e) {
            logger.error("hset redis,失败key=" + key, e);
            return false;
        }
    }

    public String hget(String key, String field) {
        String value = null;
        try {
            Object o = redisTemplate.opsForHash().get(key, field);
            value = o.toString();
        } catch (Exception e) {
            logger.error("hget redis,失败key=" + key, e);
        }
        return value;
    }


    public boolean set(String key, String xvalue) {
        try {
            redisTemplate.opsForValue().set(key, xvalue);
            return true;
        } catch (Exception e) {
            logger.error("set redis,失败key=" + key, e);
        }
        return false;
    }

    public long setnx(final String key,final String xvalue) {
        boolean borrowOrOprSuccess = true;
        long result = 0l;
        try {
            redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    RedisSerializer<String> redisSerializer = redisTemplate .getStringSerializer();
                    return redisConnection.setNX(SerializationUtils.serialize(key), SerializationUtils.serialize(xvalue)); } });
        } catch (Exception e) {
            logger.error("setnx redis 异常",e);
        }
        return result;
    }

    public String get(String key) {
        String value = null;
        try {
            Object o = redisTemplate.opsForValue().get(key);
            return o.toString();
        } catch (Exception e) {
            logger.error("get redis,失败key=" + key, e);
        }
        return value;
    }

    /**
     * 设置 key 的有效时间，
     *
     * @param key
     * @param seconds 秒单位
     * @return
     */
    public boolean expireObject(Object key, int seconds) {
        try {
            return redisTemplate.expire(SerializationUtils.serialize(key.hashCode()), seconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("设置redis key有效期异常",e);
            return false;
        }
    }
}
