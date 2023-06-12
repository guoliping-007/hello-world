package com.glp.common.dao;

import com.glp.common.support.RedisSupport;
import com.glp.common.utils.Convert;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import com.glp.common.utils.StringUtil;

import java.util.*;
import java.util.concurrent.TimeUnit;

public abstract class RedisBaseDao {

    private static final Logger log = LoggerFactory.getLogger(RedisBaseDao.class);

    private static final String REDIS_WAIT_RETRY_LOCK_PREFIX = "redis_wait_retry_lock_";

    protected StringRedisTemplate redisTemplate;

    public static final String LUA_LOCK = "" +
            /**/ " if redis.call('exists', KEYS[1])==0 then " +
            /**/ "   return redis.call('setex', KEYS[1], ARGV[1], ARGV[2]) " +
            /**/ " else " +
            /**/ "   return 'nil' " +
            /**/ " end ";

    public static String LUA_UNLOCK = "" +
            /**/ " if redis.call('get', KEYS[1])==ARGV[1] then " +
            /**/ "     return redis.call('del', KEYS[1]) " +
            /**/ " else" +
            /**/ "     return 0 " +
            /**/ " end ";

    /**
     * 获取redis服务器的时间（毫秒数）
     */
    public long time() {
        List<Object> list = redisTemplate.executePipelined((RedisConnection connection) -> {
            connection.time();
            return null;
        });
        return (long) list.get(0);
    }

    public <T> List<Object> zBatchIncrDouble(List<String> keys, List<T> vals, List<Double> incs) {
        return redisTemplate.executePipelined((RedisConnection connection) -> {
            for (int index = 0; index < keys.size(); index++) {
                connection.zIncrBy(keys.get(index).getBytes(), incs.get(index), vals.get(index).toString().getBytes());
            }
            return null;
        });
    }

    public <T> List<Object> zBatchIncr(List<String> keys, List<T> vals, List<Long> incs) {
        return redisTemplate.executePipelined((RedisConnection connection) -> {
            for (int index = 0; index < keys.size(); index++) {
                connection.zIncrBy(keys.get(index).getBytes(), incs.get(index), vals.get(index).toString().getBytes());
            }
            return null;
        });
    }

    public <T> List<Object> zBatchIncrWithTime(List<String> keys, List<T> rows, List<Long> incs) {
        List<String> argv = new ArrayList<>();
        for (int i = 0; i < keys.size(); i++) {
            argv.add(rows.get(i).toString());
            argv.add(incs.get(i).toString());
        }
        argv.add(getTime());
        return executeLua(RedisSupport.ZBATCHINCR_WITH_TIME, List.class, keys, argv);
    }

    public String get(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public long incrValue(final String key, final long step) {
        return redisTemplate.opsForValue().increment(key, step);
    }

    public void set(final String key, final String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(final String key, final String value, long expireSec) {
        redisTemplate.opsForValue().set(key, value, expireSec, TimeUnit.SECONDS);
    }

    public boolean setNX(final String key, String val) {
        return redisTemplate.opsForValue().setIfAbsent(key, val);
    }

    public boolean setNX(final String key, String val, final long sec) {
        Boolean succ = redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            Boolean setNX = connection.setNX(key.getBytes(), val.getBytes());
            if (Boolean.TRUE.equals(setNX)) {
                connection.expire(key.getBytes(), sec);
            }
            return setNX;
        });
        return succ != null && succ;
    }

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public void del(final String key) {
        redisTemplate.delete(key);
    }

    public void delKeys(Set<String> keys) {
        redisTemplate.delete(keys);
    }

    public String hget(final String key, final String hashKey) {
        Object obj = redisTemplate.opsForHash().get(key, hashKey);
        if (obj == null) {
            return null;
        } else {
            return obj.toString();
        }
    }

    public Map<Object, Object> hGetAll(final String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public List<Object> hmGet(final String key, List<Object> hashKeys) {
        return redisTemplate.opsForHash().multiGet(key, hashKeys);
    }

    public void hset(final String key, final String hashKey, final String value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public long hIncrByKey(final String key, final String hashKey, final long value) {
        return redisTemplate.boundHashOps(key).increment(hashKey, value);
    }

    public boolean hsetnx(final String key, final String hashKey, final String value) {
        return redisTemplate.boundHashOps(key).putIfAbsent(hashKey, value);
    }

    public Double zScore(String key, String obj) {
        return redisTemplate.opsForZSet().score(key, obj);
    }

    public long zscore(String key, String row){
        return Convert.toLong(redisTemplate.opsForZSet().score(key, row));
    }

    /**
     * 只支持分数为正整数的zset
     * @param key
     * @param obj
     * @return
     */
    public List<Long> zScoreAndRank(String key, String obj) {
        List<Object> list = redisTemplate.execute(new RedisCallback<List<Object>>() {
            @Override
            public List<Object> doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                connection.zScore(key.getBytes(), obj.getBytes());
                connection.zRevRank(key.getBytes(), obj.getBytes());
                return connection.closePipeline();
            }
        });

        // 表示 zset key 或 member 不存在
        if(list.get(0) == null) {
            return Arrays.asList(0L, 0L);
        } else {
            long score = Convert.toLong(list.get(0));
            long rank = Convert.toLong(list.get(1)) + 1;
            return Arrays.asList(score, rank);
        }
    }

    public Long zRevRank(String key, String obj) {
        return redisTemplate.opsForZSet().reverseRank(key, obj);
    }

    public boolean zAdd(String key, String obj, double score) {
        return redisTemplate.opsForZSet().add(key, obj, score);
    }

    public long zIncr(String key, String obj, long score) {
        return redisTemplate.opsForZSet().incrementScore(key, obj, score).longValue();
    }

    public Set<TypedTuple<String>> zrevRange(String key, long num) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, num - 1);
    }

    public Set<TypedTuple<String>> zrevRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    public Set<String> zrevRangeByScore(String key, long min, long max) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    public Set<TypedTuple<String>> zrevRangeByScoreWithScore(String key, long min, long max) {
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max);
    }

    public Set<String> zrevRangeNoScores(String key, long num) {
        return redisTemplate.opsForZSet().reverseRange(key, 0, num - 1);
    }

    public Set<TypedTuple<String>> zrange(String key, long num) {
        return redisTemplate.opsForZSet().rangeWithScores(key, 0, num - 1);
    }

    public long zcard(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    public long zcount(String key, double min, double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    public boolean sIsMember(String key, String member) {
        return redisTemplate.opsForSet().isMember(key, member);
    }

    public <T> T executeLua(String scriptLuaName, Class<T> returnClz, List<String> keys, List<String> argv) {
        DefaultRedisScript<T> script = new DefaultRedisScript<>();
        script.setScriptText(RedisSupport.getScript(scriptLuaName));
        script.setResultType(returnClz);
        return redisTemplate.execute(script, keys, argv.toArray());
    }

    public boolean hExists(String key, String row) {
        return redisTemplate.opsForHash().hasKey(key, row);
    }

    public Set<String> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public boolean sAdd(String key, String member) {
        return redisTemplate.opsForSet().add(key, member) > 0;
    }

    /**
     * 带上时间戳的zset, 同分下, 后到的排后面
     *
     * @param key zset key
     * @param rowkey zset rowkey
     * @param incr zset incr score
     * @return
     */
    public long zincrWithTime(String key, String rowkey, long incr){
        Long result = executeLua(RedisSupport.ZINCR_WITH_TIME, Long.class, Collections.singletonList(key),
                Arrays.asList(rowkey, incr + "", getTime()));
        return result == null ? 0 : result.longValue();
    }

    /**
     * 带上后缀的zincr
     *
     * @param key
     * @param rowkey
     * @param incr
     * @param suffix 小于1的小数字符串
     * @return
     */
    public long zincrWithSuffix(String key, String rowkey, long incr, String suffix){
        Long result = executeLua(RedisSupport.ZINCR_WITH_TIME, Long.class, Collections.singletonList(key),
                Arrays.asList(rowkey, incr + "", suffix));
        return result == null ? 0 : result.longValue();
    }

    /**
     * FIXME: 本代码支持到 2034/11/19 01:27:27
     * @return
     */
    private String getTime() {
        return "0." + (Integer.MAX_VALUE - System.currentTimeMillis() / 1000);
    }

    //等待重试锁
    public String tryLock(String name) {
        return tryLock(name, 60, 250, 40);
    }

    /**
     * 锁定name ttl 秒， 尝试 times次， 每次失败睡眠 mills
     * @date 2019年11月13日 下午7:53:57
     */
    public String tryLock(String name, int ttl, int mills, int times) {
        while (times-- > 0) {
            String secret = lock(name, ttl);
            if (secret != null) {
                return secret;
            }
            waiting(mills);
        }
        return null;
    }

    private static void waiting(long mills) {
        try {
            Thread.sleep(mills);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }

    public String lock(String name, int ttl) {
        name = REDIS_WAIT_RETRY_LOCK_PREFIX + name;
        String secret = java.util.UUID.randomUUID().toString();
        try {
            List<String> keys = Arrays.asList(name);
            DefaultRedisScript<String> script = new DefaultRedisScript<String>();
            script.setScriptText(LUA_LOCK);
            script.setResultType(String.class);
            String ret = redisTemplate.execute(script, keys, String.valueOf(ttl), secret);
            log.info("lock done@name:{}, secret:{}", name, secret);
            return "OK".equalsIgnoreCase(ret) ? secret : null;
        } catch (Throwable t) {
            log.error("lock exception@name:{}, secret:{}, err:{}", name, secret, t.getMessage(), t);
            // 发生异常，也认为锁定失败
            return null;
        }
    }

    public boolean unlock(String name, String secret) {
        if (StringUtil.isBlank(secret)) {
            return true;
        }
        name = REDIS_WAIT_RETRY_LOCK_PREFIX + name;
        List<String> keys = Arrays.asList(name);
        DefaultRedisScript<Long> script = new DefaultRedisScript<Long>();
        script.setScriptText(LUA_UNLOCK);
        script.setResultType(Long.class);
        Long ret = redisTemplate.execute(script, keys, secret.toString());
        log.info("unlock ok@delete lock:{}, secret:{}, result:{}", name, secret, ret);
        return (ret != null) && (ret == 1);
    }

    /**
     * 操作计时器并设置过期时间
     */
    public long incrWithExpire(String key, long step, long expireSeconds) {
        List<String> keys = Lists.newArrayList(key);
        List<String> argv = Lists.newArrayList(String.valueOf(step), String.valueOf(expireSeconds));
        long count = this.executeLua("incr_with_expire.lua", Long.class, keys, argv);
        return count;
    }

    public long listPush(String listName, String data){
        return redisTemplate.opsForList().leftPush(listName, data);
    }

    public String listPop(String listName){
        return redisTemplate.opsForList().rightPop(listName);
    }


}
