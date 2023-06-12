/**
 * Secret.java / 2019年7月17日 上午9:25:23
 */
package com.glp.common.locker;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glp.common.utils.DateUtil;
import com.glp.common.utils.StringUtil;
import com.glp.common.utils.SystemUtil;

/** 
 * @date 2019年7月17日 上午9:25:23
 */
public class Secret {
    private static final Logger log = LoggerFactory.getLogger(Secret.class);

    public static final String PATTERN = "yyyyMMddHHmmss.SSS";
    // 锁创建时间，精确到毫秒，格式为： yyyy-MM-dd HH:mm:ss.SSS
    private String time;
    // 操作序号，当前进程内唯一
    private long seq;
    // 工作者， ip+port+进程号+线程号
    private String worker;
    // 加锁的过期时间
    private int ttl;
    // 扩展数据, 不可含有  | （|用来隔开秘钥各字段的）
    private String extdat = "";

    public Secret() {

    }

    public Secret(AtomicLong seq, int ttl, String extdat) throws Exception {
        String worker = SystemUtil.getWorkerBrief();
        verify(worker, extdat);
        this.time = DateUtil.today(PATTERN);
        this.seq = seq.getAndIncrement();
        this.worker = StringUtil.trim(worker);
        this.ttl = ttl;
        this.extdat = StringUtil.trim(extdat);
    }

    private Secret(String time, long seq, String worker, int ttl, String extdat) throws Exception {
        verify(worker, extdat);
        this.time = time;
        this.seq = seq;
        this.worker = StringUtil.trim(worker);
        this.ttl = ttl;
        this.extdat = StringUtil.trim(extdat);
    }

    public String getTime() {
        return time;
    }

    public long getSeq() {
        return seq;
    }

    public String getWorker() {
        return worker;
    }

    public int getTtl() {
        return ttl;
    }

    public String getExtdat() {
        return extdat;
    }

    /**
     * 将 time 以 Date 对象的方式提取出来
     * @date 2019年7月17日 上午11:14:47
     */
    public Date obtainDate() {
        return DateUtil.getDate(time, PATTERN);
    }

    /**
     * 获取执行周期的剩余秒数 
     * @date 2019年7月17日 下午5:27:03
     */
    public int getPeriodLeftMilliseconds(int period) {
        Date date = this.obtainDate();
        long diff = System.currentTimeMillis() - date.getTime();
        return (int)(period * 1000 - diff);
    }

    /**
     * 获取执行周期的剩余秒数 
     * @date 2019年7月17日 下午5:27:03
     */
    public int getTtlLeftMilliseconds() {
        Date date = this.obtainDate();
        long diff = System.currentTimeMillis() - date.getTime();
        return (int)(ttl * 1000 - diff);
    }

    /**
     * 检查2个内容的合法性
     * @date 2019年7月17日 上午10:11:52
     */
    private void verify(String worker, String extdat) throws Exception {
        check(worker);
        check(extdat);
    }

    /**
     * 内容不能包含秘钥字段分隔符 |
     * @throws Exception
     * @date 2019年7月17日 上午10:09:19 
     */
    private void check(String content) throws Exception {
        String flagChar = "#";
        if (content != null && content.contains(flagChar)) {
            throw new Exception(content + " can't contain '#'");
        }
    }

    /**
     * 按固定顺序生产锁的钥匙，
     * 这里不能使用 json， 因为不同语言、不同库的 json 转换格式不一样，会导致验证钥匙时字符串比较失败！
     * @date 2019年7月17日 上午10:18:17
     */
    public String toString() {
        return time + "#" + seq + "#" + worker + "#" + ttl + "#" + extdat;
    }

    /**
     * 按固定顺序解析锁的钥匙，构造钥匙对象
     * @date 2019年7月17日 上午10:20:47
     */
    public static Secret toSecret(String secret) {
        try {
            if (StringUtil.isBlank(secret)) {
                return null;
            }
            String[] strs = secret.split("#", 5);
            String time = strs[0];
            long seq = Long.parseLong(strs[1]);
            String worker = strs[2];
            int ttl = Integer.parseInt(strs[3]);
            String extdat = strs[4];
            return new Secret(time, seq, worker, ttl, extdat);
        } catch (Throwable t) {
            log.error("toSecret exception@secret:{}, err:{}", secret, t, t);
            return null;
        }
    }

    public static void main(String[] args) {
        String secret = "20190718000224.001#20#172.25.55.13/13680/70#30#";
        Secret object = Secret.toSecret(secret);
        System.out.println(object);
    }
}
