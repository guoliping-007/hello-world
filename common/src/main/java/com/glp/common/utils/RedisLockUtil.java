
package com.glp.common.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import com.google.common.collect.Lists;


public class RedisLockUtil {
    // 锁过期时间为1min
    private static final int EXPIRE_TIME = 60;

    private static final Logger log = LoggerFactory.getLogger(RedisLockUtil.class);
    // redis锁的lua脚本

    protected static final String LOCK_SCRIPT_NAME = "redisLock.lua";

    @Autowired
    private RedisTemplate<Serializable, Serializable> redisTemplateLock;

    /*
     * 新的redis分布锁： 1.使用lua脚本加锁，先用SETNX 检查是否有锁，再使用expire设置失效时间
     */

    public boolean lock(final String key) {
        return lock(key, EXPIRE_TIME);
    }

    /**
     * 锁使用lua脚本，使用setex与expire
     * @param key - 锁名
     * @param expire - 过期时间，单位为秒
     */
    public boolean lock(final String key, int expire) {
        Clock clock = new Clock();
        DefaultRedisScript<Long> script = new DefaultRedisScript<Long>();
        script.setScriptText(ReadFileUtil.getLuaScript(LOCK_SCRIPT_NAME));
        script.setResultType(Long.class);
        List<Serializable> keys = Lists.newArrayList();
        keys.add(key);
        keys.add(expire + "");
        keys.add("locked@" + DateUtil.format("yyyyMMddHHmmss"));
        List<Serializable> args = Lists.newArrayList();
        Long count = redisTemplateLock.execute(script, keys, args.toArray());
        Boolean isLock = count == 1L;
        if (isLock) {
            //log.info("lock success!!@expire:" + EXPIRE_TIME + ", key:" + key + clock.nanoTag());
            return true;
        } else {
            //log.info("lock falied!!lock is exist@key:" + key + clock.nanoTag());
            return false;
        }
    }


    /**
     * 返回加锁成功后的锁定秒数
     * @date 2018年5月31日 下午7:35:58
     */
    public long getLockedSeconds(final String key) {
        String data = StringUtil.trim(redisTemplateLock.opsForValue().get(key));
        int inx = data.indexOf('@');
        if (inx == -1) {
            return -1;
        }

        Date date = DateUtil.getDate(data.substring(inx + 1), "yyyyMMddHHmmss");
        if (date == null) {
            return -1;
        }

        long millis = System.currentTimeMillis();
        long offset = (millis - date.getTime()) / 1000;
        return Math.max(0, offset);
    }


    /**
     * 解锁操作
     * 
     * @param key
     */
    public void unLock(final String key) {
        try {
            @SuppressWarnings("unused")
            Long count = redisTemplateLock.execute(new RedisCallback<Long>() {
                @Override
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] keyBytes = redisTemplateLock.getStringSerializer().serialize(key);
                    return connection.del(keyBytes);
                }
            });
            //log.info("unLock success!!@key:" + key + " @count:" + count);
        } catch (Exception e) {
            log.error("unLock ERROR!!@key:" + key);
        }
    }
}
