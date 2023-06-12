package com.glp.common.locker;

import com.glp.common.consts.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractLocker implements Locker {
    private static final Logger log = LoggerFactory.getLogger(AbstractLocker.class);

    private AtomicLong seq = new AtomicLong(1);

    @Override
    public Secret lock(String name) {
        return lock(name, 30);
    }

    @Override
    public Secret lock(String name, int ttl) {
        return lock(name, ttl, "", 1);

    }

    @Override
    public Secret lock(String name, int ttl, String extdat, int count) {
        if (!isValid(name)) {
            return null;
        }

        int inx = 0;
        try {
            count = (count <= 0) ? 1 : count;
            ttl = (ttl <= 0) ? Integer.MAX_VALUE : ttl;

            while (++inx <= count) {
                Secret secret = new Secret(seq, ttl, extdat);
                if (doLock(name, secret, inx)) {
                    log.info("lock ok@try {}/{}, name:{}, secret:'{}'", inx, count, name, secret);
                    return secret;
                }

                if (inx < count) {
                    log.warn("lock fail@try {}/{}, name:{}, ttl:{}, retry after 1s", inx, count, name, ttl);
                    sleep2(200);
                } else {
                    log.warn("lock fail@can't lock {} by try {} times", name, count);
                }
            }
        } catch (Throwable t) {
            log.error("lock exception@inx:{}, name:{}, extdat:{}, err:{}", inx, name, extdat, t.getMessage(), t);
        }
        return null;
    }

    @Override
    public boolean unlock(String name, Secret secret) {
        return unlock(name, secret, 1);
    }

    @Override
    public boolean unlock(String name, Secret secret, int count) {
        if (!isValid(name) || secret == null) {
            return false;
        }

        int inx = 0;
        count = count <= 0 ? 1 : count;

        while (++inx <= count) {
            try {
                boolean flag = doUnlock(name, secret, inx);
                String ret = flag ? "ok" : "fail";
                log.info("unlock {}@try {}/{}, name:{}, secret:'{}'", ret, inx, count, name, secret);
                return flag;
            } catch (Throwable t) { // 一般是网络等外部异常，需要尝试继续解锁
                if (inx < count) {
                    log.error("unlock exception@try {}/{}, name:{}, secret:'{}', err:{}, retry after 1s", inx, count,
                        name, secret, t.getMessage(), t);
                    sleep(1);
                } else {
                    log.error("unlock exception@can't unlock name:{}, secret:'{}' by try {} times", name, secret,
                        count);
                }
            }
        }

        return false;
    }

    @Override
    public boolean review(String name, Secret secret, long ttl) {
        if (secret == null) {
            return false;
        }
        Date date = secret.obtainDate();
        if (date == null) {
            log.error("wait fail@no secret time, name:{}, secret:'{}'", name, secret);
            return false;
        }

        long cost = (System.currentTimeMillis() - date.getTime()) / 1000;
        boolean safe = ttl >= cost;
        if (!safe) {
            log.error("wait fail@expire, name:{}, secret:'{}', cost:{}, ttl:{}", name, secret, cost, ttl);
        }
        return safe;
    }

    @Override
    public boolean expire(String name, Secret secret, int ttl) {
        if (!isValid(name) || secret == null || ttl < 1) {
            return false;
        }
        boolean flag = doExpire(name, secret, ttl);
        String ret = flag ? "ok" : "fail";
        log.info("expire {}@{}, secret:'{}', ttl:{}", ret, name, secret, ttl);
        return flag;
    }

    @Override
    public Secret replace(String name, Secret csecret, int ttl) {
        return replace(name, csecret, ttl, "");
    }

    @Override
    public Secret replace(String name, Secret csecret, int ttl, String extdat) {
        if (!isValid(name) || ttl < 1) {
            return null;
        }

        try {
            // 1. 老钥匙无效，直接尝试锁定
            if (csecret == null) {
                return lock(name, ttl, extdat, 1);
            }

            // 2. 合法老钥匙，采取验证钥匙的方式来重新锁定，返回新钥匙
            Secret nsecret = new Secret(seq, ttl, extdat);
            if (doReplace(name, csecret, nsecret)) {
                log.info("replace ok@{}, new:'{}', old:'{}'", name, nsecret, csecret);
                return nsecret;
            }

            log.warn("replace fail@{}, old:'{}'", name, csecret);
        } catch (Throwable t) {
            log.error("replace exception@{}, old:'{}', extdat:{}, err:{}", name, csecret, extdat, t.getMessage(), t);
        }
        return null;
    }

    @Override
    public boolean stick(String name, Secret secret, int period) {
        if (period < 1) {
            return false;
        }

        int ttl = getStickTtl(period);

        return this.expire(name, secret, ttl);
    }

    @Override
    public boolean turn(String name, Secret secret, int period, int probability) {
        if (period < 1) {
            return false;
        }

        int ttl = getTurnTtl(secret, period, probability);

        boolean flag = this.expire(name, secret, ttl);
        if (flag && ttl <= period) {
            int milliseconds = Math.min(MACHINE_TIME_DIFFERENCE, period * 1000 / 3);
            //睡眠不超过 1/3 个周期，给其它执行者获取锁的机会
            sleep2(milliseconds);
        }
        return flag;
    }

    /**
     * 验证名字是否有效
     */
    protected boolean isValid(String name) {
        return name != null && name.trim().length() > 0;
    }

    /**
     * 获取粘滞所需要的合适 ttl
     */
    protected int getStickTtl(int period) {
        // 1. 小于机器误差时固定取1秒； 大于机器误差时取 3 ~ period ~ 10 之间的一个值
        int diff = period < MACHINE_TIME_DIFFERENCE ? 1 : Math.min(10, Math.max(period, MACHINE_TIME_DIFFERENCE));

        // 2. 一个执行周期稍长一点的 ttl，保证当前执行线程使用 replaceOldLocker() 更有机会在一个  period 内就提前得到锁
        return period + diff;
    }

    /**
     * 获取达到概率转移所需要的合适 ttl
     */
    protected int getTurnTtl(Secret secret, int period, int probability) {
        // 1. secret空且概率未命中，返回 stick 模式下的 ttl
        if (secret == null || probability < 1 || Const.RD.nextInt(probability) != 0) {
            return getStickTtl(period);
        }

        // 2. 得到执行周期剩余时间
        int milliseconds = secret.getPeriodLeftMilliseconds(period);
        int seconds = Math.round((float)milliseconds / 1000);

        // 3. 将剩余时间作为过期时间(保证执行节奏用的)，让所有参与者都有机会等待锁过期，从而能平等的抢到锁
        return Math.max(1, seconds);
    }

    /**
     * 当前线程睡眠指定秒数
     */
    protected void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 当前线程睡眠指定毫秒数
     */
    protected void sleep2(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected abstract boolean doReplace(String name, Secret csecret, Secret nsecret);

    protected abstract boolean doExpire(String name, Secret secret, int ttl);

    protected abstract boolean doLock(String name, Secret secret, int inx);

    protected abstract boolean doUnlock(String name, Secret secret, int inx);
}
