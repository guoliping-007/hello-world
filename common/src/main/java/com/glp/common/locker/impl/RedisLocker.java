package com.glp.common.locker.impl;

import com.glp.common.locker.AbstractLocker;
import com.glp.common.locker.Secret;
import com.glp.common.support.SpringBeanAwareFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class RedisLocker extends AbstractLocker {

    private static final Logger log = LoggerFactory.getLogger(RedisLocker.class);

    /* 加锁 lua 脚本，可保证原子性，加锁成功返回OK，否则加锁失败 */
    public static final String LUA_LOCK = "" +
    /**/ " if redis.call('exists', KEYS[1])==0 then " +
    /**/ "	 return redis.call('setex', KEYS[1], ARGV[1], ARGV[2]) " +
    /**/ " else " +
    /**/ "	 return 'nil' " +
    /**/ " end ";

    /* 解锁 lua 脚本，可保证原子性，返回 0：锁已不存在或密码不匹配，1：删除锁成功 */
    public static final String LUA_UNLOCK = "" +
    /**/ " if redis.call('get', KEYS[1])==ARGV[1] then " +
    /**/ "     return redis.call('del', KEYS[1]) " +
    /**/ " else" +
    /**/ "     return 0 " +
    /**/ " end ";

    /* 设置过期 lua 脚本，当检测 key 下 value 相等时， 才进行  expire 设置， 1：设置新值成功，0：设置新值失败 */
    public static final String LUA_EXPIRE = "" +
    /**/ " if redis.call('get', KEYS[1])==ARGV[1] then " +
    /**/ "     return redis.call('expire', KEYS[1], ARGV[2]) " +
    /**/ " else " +
    /**/ "     return 0 " +
    /**/ " end ";

    /* 换锁  lua 脚本，原锁检测匹配，才进行换新锁的操作  */
    public static final String LUA_REPLACE = "" +
    /**/ " if redis.call('exists', KEYS[1])==0 or redis.call('get', KEYS[1])==ARGV[1] then " +
    /**/ "     return redis.call('setex', KEYS[1], ARGV[2], ARGV[3]) " +
    /**/ " else " +
    /**/ "     return 'nil' " +
    /**/ " end ";

    @Autowired
    private RedisTemplate<String, String> redisTemplateLock;

    @Override
    protected boolean doLock(String name, Secret secret, int inx) {
        try {
            int ttl = secret.getTtl();
            List<String> keys = Arrays.asList(name);
            DefaultRedisScript<String> script = new DefaultRedisScript<String>();
            script.setScriptText(LUA_LOCK);
            script.setResultType(String.class);
            String ret = redisTemplateLock.execute(script, keys, String.valueOf(ttl), secret.toString());
            //log.info("[{}]-doLock done@name:{}, secret:{}", inx, name, secret);
            return "OK".equalsIgnoreCase(ret);
        } catch (Throwable t) {
            log.error("[{}]-doLock exception@name:{}, secret:{}, err:{}", inx, name, secret, t.getMessage(), t);
            // 发生异常，也认为锁定失败
            return false;
        }
    }

    @Override
    protected boolean doUnlock(String name, Secret secret, int inx) {
        List<String> keys = Arrays.asList(name);
        DefaultRedisScript<Long> script = new DefaultRedisScript<Long>();
        script.setScriptText(LUA_UNLOCK);
        script.setResultType(Long.class);
        Long ret = redisTemplateLock.execute(script, keys, secret.toString());
        //log.info("[{}]-doUnlock ok@delete lock:{}, secret:{}, result:{}", inx, name, secret, ret);
        return (ret != null) && (ret == 1);
    }

    @Override
    protected boolean doExpire(String name, Secret secret, int ttl) {
        try {
            List<String> keys = Arrays.asList(name);
            DefaultRedisScript<Long> script = new DefaultRedisScript<Long>();
            script.setScriptText(LUA_EXPIRE);
            script.setResultType(Long.class);
            Long ret = redisTemplateLock.execute(script, keys, secret.toString(), String.valueOf(ttl));
            //log.info("doExpire done@name:{}, secret:{}, ttl:{}, ret:{}", name, secret, ttl, ret);
            return (ret != null) && (ret == 1);
        } catch (Throwable t) {
            log.error("doExpire exception@name:{}, secret:{}, ttl:{}, err:{}", name, secret, ttl, t.getMessage(), t);
            // 发生异常，也认过期设置失败
            return false;
        }
    }

    @Override
    protected boolean doReplace(String name, Secret osecret, Secret nsecret) {
        int ttl = nsecret.getTtl();
        String oldContent = osecret.toString();
        String newContent = nsecret.toString();
        try {
            List<String> keys = Arrays.asList(name);
            DefaultRedisScript<String> script = new DefaultRedisScript<String>();
            script.setScriptText(LUA_REPLACE);
            script.setResultType(String.class);
            String ret = redisTemplateLock.execute(script, keys, oldContent, String.valueOf(ttl), newContent);
            //log.info("doReplace done@name:{}, old:{}, new:{}", name, oldContent, newContent);
            return "OK".equalsIgnoreCase(ret);
        } catch (Throwable t) {
            log.error("doReplace exception@name:{}, old:{}, new:{}, err:{}", name, oldContent, newContent, t.getMessage(), t);
            // 发生异常，也认为替换失败
            return false;
        }
    }

    public void test() {
        try {
            String name = "20190717_locker_test_by_glp";
            //Secret s1 = new Secret(new AtomicLong(1), 20, null);
            Secret s2 = new Secret(new AtomicLong(2), 30, "");
            String s3 = s2.toString();
            Secret s4 = Secret.toSecret(s3);
            System.out.println("s4 = " + s4);
            int ttl = 600;
            ApplicationContext context = SpringBeanAwareFactory.getBeanFactory();
            RedisLocker locker = context.getBean(RedisLocker.class);

            Secret secret0 = locker.replace(name, new Secret(), ttl);
            boolean b0 = locker.unlock(name, secret0);
            System.out.println("b0 = " + b0);
            Secret secret1 = locker.lock(name, ttl);

            String content = secret1.toString();
            Secret object = Secret.toSecret(content);
            System.out.println("object = " + object);

            Secret secret2 = locker.lock(name, ttl);
            Secret secret3 = locker.replace(name, secret1, ttl);
            boolean b1 = locker.unlock(name, secret1);
            boolean b2 = locker.unlock(name, secret2);
            boolean b3 = locker.unlock(name, secret3);
            Secret secret4 = locker.lock(name, ttl);
            boolean b4 = locker.expire(name, secret4, 3600);
            boolean b5 = locker.review(name, secret4, 3600);
            boolean b6 = locker.expire(name, new Secret(), 1000);
            boolean b7 = locker.expire(name, null, 1000);
            Secret secret5 = locker.replace(name, new Secret(), 1000);
            Secret secret6 = locker.replace(name, null, 1000);
            boolean b8 = locker.unlock(name, secret4);
            boolean b9 = locker.expire(name, secret4, 3000);
            Secret secret7 = locker.replace(name, secret4, 1000);
            System.out.println("b6 = " + b6);
            System.out.println("b7 = " + b7);
            System.out.println("secret5 = " + secret5);
            System.out.println("secret6 = " + secret6);
            System.out.println("b8 = " + b8);
            System.out.println("b9 = " + b9);
            System.out.println("secret7 = " + secret7);

            System.out.println("secret1 = " + secret1);
            System.out.println("secret2 = " + secret2);
            System.out.println("secret3 = " + secret3);
            System.out.println("b1 = " + b1);
            System.out.println("b2 = " + b2);
            System.out.println("b3 = " + b3);
            System.out.println("secret4 = " + secret4);
            System.out.println("b4 = " + b4);
            System.out.println("b5 = " + b5);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
