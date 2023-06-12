/*
 * @(#)Clock.java 2013-07-11
 * 
 */

package com.glp.common.utils;

import java.util.Date;

/**
 * @功能说明
 * <pre>
 * 计时器 - 可返回单次计时结果，也可汇总输出多次计时结果
 * </pre>
 * 
 * @版本更新
 * <pre>
 * 修改说明：形成初始版本
 * 复审人：
 * </pre>
 */
public class Clock {
    private long begin = 0;

    private StringBuffer buf = new StringBuffer();

    private long last = 0;


    private long nanoBegin = 0;

    @SuppressWarnings("unused")
    private long nanoLast = 0;

    /**
     * 临时方法，后续补充
     * @return
     */
    public Clock nanoTag() {
        long curr = System.nanoTime();
        long span = curr - this.nanoBegin;
        this.nanoLast = curr;
        buf.append("|").append(span / 1000).append("us");
        return this;
    }

    public static long time(long start) {
        return System.currentTimeMillis() - start;
    }

    public Clock() {
        this.begin = System.currentTimeMillis();
        this.last = this.begin;
        this.nanoBegin = System.nanoTime();
        this.nanoLast = this.nanoBegin;
    }

    public Clock(long now) {
        this.begin = now < 0 ? 0 : now;
        this.last = this.begin;
    }

    public Clock(Date now) {
        this.begin = now == null ? 0 : now.getTime();
        this.last = this.begin;

    }

    public Clock tag() {
        long curr = System.currentTimeMillis();
        long span = curr - this.last;
        this.last = curr;
        buf.append("|").append(span).append("ms");
        return this;
    }

    public long millis() {
        long curr = System.currentTimeMillis();
        long span = curr - this.last;
        this.last = curr;
        buf.append("|").append(span).append("ms");
        return span;
    }

    public long seconds() {
        long curr = System.currentTimeMillis();
        long span = (curr - this.last) / 1000;
        this.last = curr;
        buf.append("|").append(span).append("s");
        return span;
    }

    public long minutes() {
        long curr = System.currentTimeMillis();
        long span = (curr - this.last) / 60000;
        this.last = curr;
        buf.append("|").append(span).append("m");
        return span;
    }

    public long hours() {
        long curr = System.currentTimeMillis();
        long span = (curr - this.last) / 3600000;
        this.last = curr;
        buf.append("|").append(span).append("h");
        return span;
    }

    public long days() {
        long curr = System.currentTimeMillis();
        long span = (curr - this.last) / 86400000;
        this.last = curr;
        buf.append("|").append(span).append("d");
        return span;
    }

    public long elapse() {
        return System.currentTimeMillis() - begin;
    }

    public String toString() {
        return String.format("@[clock:%sms%s]", last - begin, buf);
    }

}
