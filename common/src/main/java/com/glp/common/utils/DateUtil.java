/*
 * @(#)DateUtil.java
 */

package com.glp.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <pre>
 * @date 2008-11-24
 *
 * @功能说明：
 * 时间日期处理
 *
 * @版本更新列表
 * 修改版本: 1.0.0
 * 修改日期：2008-11-24
 * 修改说明：形成初始版本
 * 复审人：
 * </pre>
 */

public class DateUtil {
    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(DateUtil.class);

    /**
     * 默认的日期格式（yyyy-MM-dd HH:mm:ss）
     */
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * mysql数据库默认的日期格式（yyyy-MM-dd HH:mm:ss）
     */
    public static final String DB_DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss.S";

    /**
     * 日期格式1（yyyyMMddHHmmss）
     */
    public static final String PATTERN_TYPE1 = "yyyyMMddHHmmss";

    /**
     * 日期格式2（yyyyMMdd）
     */
    public static final String PATTERN_TYPE2 = "yyyyMMdd";

    /**
     * 日期格式3（HH:mm）
     */
    public static final String PATTERN_TYPE3 = "HH:mm";

    /**
     * 日期格式4（yyyyMM）
     */
    public static final String PATTERN_TYPE4 = "yyyyMM";

    /**
     * 日期格式5（yyyy-MM-dd）
     */
    public static final String PATTERN_TYPE5 = "yyyy-MM-dd";

    /**
     * 日期格式6（HH:mm:ss）
     */
    public static final String PATTERN_TYPE6 = "HH:mm:ss";

    /**
     * 日期格式7（HH:mm:ss）
     */
    public static final String PATTERN_TYPE7 = "yyyyMMddHH";

    /**
     * 默认的最小年
     */
    private static final Integer MAX_YEAR = 9999;

    /**
     * 默认的最大年
     */
    private static final Integer MIN_YEAR = 1000;


    /**
     * 一天秒
     */
    public static long DAY_SEC = 3600 * 24;

    public final static DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_PATTERN);

    public final static DateTimeFormatter DB_DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(DB_DEFAULT_PATTERN);

    public final static DateTimeFormatter NO_SPACE_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_TYPE1);

    public final static DateTimeFormatter DEFAULT_DAY_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_TYPE5);

    public final static DateTimeFormatter YYYYMM = DateTimeFormatter.ofPattern(PATTERN_TYPE4);

    public final static DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern(PATTERN_TYPE2);

    /**
     * 添加周数（就是每周加7天）
     * @param date
     * @param weeks
     * @return
     */
    public static Date addWeeks(Date date, long weeks) {
        if (null == date) {
            return null;
        }
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.DATE, (int) weeks * 7);
        return rightNow.getTime();
    }

    /**
     * 添加分钟数
     *
     * @param date
     * @param minutes
     * @return
     */
    public static Date addMinutes(Date date, int minutes) {
        if (null == date) {
            return null;
        }
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.MINUTE, minutes);
        return rightNow.getTime();
    }

    /**
     * 添加小时数
     * @param date
     * @param hours
     * @return
     */
    public static Date addHours(Date date, int hours) {
        if (null == date) {
            return null;
        }
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.HOUR, hours);
        return rightNow.getTime();
    }

    /**
     * @param @param  date
     * @param @param  seconds
     * @param @return
     * @desc: TODO 增加seconds秒
     * @date 2016年1月27日
     */
    public static Date addSeconds(Date date, int seconds) {
        if (null == date) {
            return null;
        }
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.SECOND, seconds);
        return rightNow.getTime();
    }

    /**
     * 起始日期加减指定的月数
     *
     * @param date   起始日期
     * @param months 月数(可以为负数)
     * @return 日期
     */
    public static Date addMonths(Date date, long months) {
        if (null == date) {
            return null;
        }
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.MONTH, (int) months);
        return rightNow.getTime();
    }

    /**
     * 起始日期加减指定的天数
     *
     * @param date 起始日期
     * @param days 天数(可以为负数)
     * @return 日期
     */
    public static Date add(Date date, int days) {
        if (null == date) {
            return null;
        }
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.DATE, days);
        return rightNow.getTime();
    }

    /**
     * 起始日期加减指定的天数
     *
     * @param date 起始日期
     * @param days 天数(可以为负数)
     * @return 日期
     */
    public static String add(String date, String pattern, int days) {
        if (null == date) {
            return null;
        }
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(getDate(date, pattern));
        rightNow.add(Calendar.DATE, days);
        return format(rightNow.getTime(), pattern);
    }

    /**
     * 根据毫秒获取当前时间字符串
     *
     * @param curTime
     * @param pattern
     * @return
     */
    public static String getPattenStrFromTime(long curTime, String pattern) {
        LocalDateTime date = LocalDateTime.ofEpochSecond(curTime / 1000, 0, ZoneOffset.ofHours(8));
        DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
        String dateStr = date.format(format);
        return dateStr;

    }

    /**
     * 计算两个日期相差的天数(结束日期-开始日期)
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 天数
     */
    public static long getDays(Date begin, Date end) {
        return (end.getTime() - begin.getTime()) / (3600 * 24 * 1000);
    }

    /**
     * 获取1970-01-01 到现在的天数
     *
     * @return
     */
    public static int getDays() {
        return (int) (System.currentTimeMillis() / 86400000);
    }

    /**
     * 获取1970-01-01 到现在的秒数
     *
     * @return
     */
    public static long getSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取指定时间代表的秒数
     *
     * @return
     */
    public static long getSeconds(Date date) {
        return date.getTime() / 1000;
    }

    /**
     * 获取当前的小时计数
     *
     * @return
     */
    public static int getHours() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.MINUTE);
    }

    /**
     * 获取今日的时间串,格式由 DEFAULT_PATTERN 决定（yyyy-MM-dd HH:mm:ss）
     *
     * @return
     */
    public static String today() {
        return format(new Date(), null);
    }

    public static String today(String pattern) {
        return format(new Date(), pattern);
    }

    public static Date prev() {
        return new Date(System.currentTimeMillis() - 86400000);
    }

    public static Date next() {
        return new Date(System.currentTimeMillis() + 86400000);
    }

    public static Date skip(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    // 当天凌晨
    public static Date first() {
        Date now = new Date();
        return first(now);
    }

    public static Date first(Date now) {
        String date = DateUtil.format(now, "yyyy-MM-dd");
        return getDate(date + " 00:00:00");
    }

    // 当天最后
    public static Date last() {
        Date now = new Date();
        return last(now);
    }

    public static Date last(Date now) {
        String date = DateUtil.format(now, "yyyy-MM-dd");
        return getDate(date + " 23:59:59");
    }

    // 当天最后
    public static Date getTodayLast() {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 23);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.SECOND, 59);
        return now.getTime();
    }

    /**
     * 得到指日期格式的字符串
     *
     * @param pattern 日期格式,比如：yyyy-MM-dd HH:mm:ss
     * @return 日期字符串
     */
    public static String format(String pattern) {
        Date date = new Date();
        // 如果pattern为空，则返回默认的格式
        if (StringUtils.isEmpty(pattern)) {
            pattern = DateUtil.DEFAULT_PATTERN;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug(e);
            }
            return "";
        }
    }

    /**
     * 得到指日期格式的字符串
     *
     * @param date    日期
     * @param pattern 日期格式,比如：yyyy-MM-dd HH:mm:ss
     * @return 日期字符串
     */
    public static String format(Date date, String pattern) {
        if (null == date) {
            return "";
        }
        // 如果pattern为空，则返回默认的格式
        if (StringUtils.isEmpty(pattern)) {
            pattern = DateUtil.DEFAULT_PATTERN;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug(e);
            }
            return "";
        }
    }

    /**
     * 得到指日期格式(yyyy-MM-dd HH:mm:ss)的字符串
     *
     * @param date 日期
     * @return 日期字符串
     */
    public static String format(Date date) {
        if (null == date) {
            return "";
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(DateUtil.DEFAULT_PATTERN);
            return df.format(date);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug(e);
            }
            return "";
        }
    }

    //格式化数据库默认格式 yyyy-MM-dd HH:mm:ss.S ---> yyyy-MM-dd HH:mm:ss
    public static String formatDbDate(String dateStr){
        Date date = getDate(dateStr,DB_DEFAULT_PATTERN);
        return format(date);
    }

    /**
     * 得到日期的最大值
     *
     * @return date
     */
    public static Date getMaxDateValue() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, MAX_YEAR);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 得到日期的最小值
     *
     * @return date
     */
    public static Date getMinDateValue() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, MIN_YEAR);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 得到日期对象
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */
    public static Date getDate(String strDate, String pattern) {
        if (null == strDate || "".equals(strDate.trim())) {
            // log.debug("日期为空");
            return null;
        }
        try {
            java.text.DateFormat df = new SimpleDateFormat(pattern);
            return df.parse(strDate);
        } catch (ParseException e) {
            if (log.isDebugEnabled()) {
                log.debug(e);
            }
            return null;
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug(e);
            }
            return null;
        }
    }

    /**
     * 得到日期对象(日期格式默认为yyyy-MM-dd HH:mm:ss)
     *
     * @param strDate 日期字符串
     * @return
     */
    public static Date getDate(String strDate) {
        return getDate(strDate, DEFAULT_PATTERN);
    }

    public static Date getDate(long seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(seconds * 1000);
        return calendar.getTime();
    }


    /**
     * 得到日期对应的星期几，以中文名显示
     *
     * @param date 日期
     * @return
     */
    public static String getDayOfWeekChinese(Date date) {
        int dayOfWeek = getNubmerDayOfWeek(date);
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "星期日";
            case Calendar.MONDAY:
                return "星期一";
            case Calendar.TUESDAY:
                return "星期二";
            case Calendar.WEDNESDAY:
                return "星期三";
            case Calendar.THURSDAY:
                return "星期四";
            case Calendar.FRIDAY:
                return "星期五";
            case Calendar.SATURDAY:
                return "星期六";
            default:
                break;
        }
        return "";
    }

    /**
     * 得到日期对应的星期几，以中文名显示
     *
     * @param date 日期
     * @return
     */
    public static String getDayOfWeekEnglish(Date date) {
        int dayOfWeek = getNubmerDayOfWeek(date);
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "SUNDAY";
            case Calendar.MONDAY:
                return "MONDAY";
            case Calendar.TUESDAY:
                return "TUESDAY";
            case Calendar.WEDNESDAY:
                return "WEDNESDAY";
            case Calendar.THURSDAY:
                return "THURSDAY";
            case Calendar.FRIDAY:
                return "FRIDAY";
            case Calendar.SATURDAY:
                return "SATURDAY";
            default:
                break;
        }
        return "";
    }

    /**
     * 得到日期对应的星期几，以数字显示
     *
     * @param date 日期
     * @return
     */
    public static int getNubmerDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 将时间串转换成新的格式输出
     *
     * @param ofs - 时间串的原格式
     * @param ds  - 要被转换的时间串R
     * @param ofs - 时间串的新格式
     * @return
     */
    public static String changeDateTimeFormat(String ofs, String ds, String nfs) {
        try {
            ofs = ofs == null || ofs.trim().length() == 0 ? "yyyy-MM-dd HH:mm:ss" : ofs.trim();
            Date date = (new SimpleDateFormat(ofs)).parse(ds);
            return (new SimpleDateFormat(nfs)).format(date);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 返回一个Map, key为属性名, value为指定格式的时间串
     *
     * @param patterns - key为属性名, value为时间格式
     * @return
     */
    public static Map<String, String> getSysDatetimeEntries(Map<String, String> patterns) {
        Date date = new Date();
        Map<String, String> entries = new HashMap<String, String>(128);
        for (Iterator<String> itr = patterns.keySet().iterator(); itr.hasNext(); ) {
            String key = itr.next();
            String pattern = StringUtils.trim(patterns.get(key));
            if (pattern.length() == 0) {
                entries.put(key, null);
                continue;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            entries.put(key, sdf.format(date));
        }
        return entries;
    }

    /**
     * 当天剩余秒数
     *
     * @return
     */
    public static int getTodayLeftSeconds() {
        Date now = new Date();
        Date after = getDate(format(now, "yyyy-MM-dd") + " 23:59:59");
        return (int) ((after.getTime() - now.getTime()) / 1000 + 1);
    }

    /**
     * 1小时内剩余秒数
     *
     * @return
     */
    public static int getHourLeftSeconds() {
        return getTodayLeftSeconds() % 3600;
    }

    /**
     * 3小时内剩余秒数
     *
     * @return
     */
    public static int get3HoursLeftSeconds() {
        return getTodayLeftSeconds() % 10800;
    }

    /**
     * 获取本月第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        calendar.set(year, month, 1, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 获取本月最后一秒
     *
     * @param date
     * @return
     */
    public static Date getLastDay(Date date) {
        return addSeconds(getNextMonthFirstDay(date), -1);
    }



    /**
     * 获取下个月第一天
     *
     * @param date
     * @return
     */
    public static Date getNextMonthFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 加一个月
        calendar.add(Calendar.MONTH, 1);
        return getFirstDay(calendar.getTime());
    }

    /**
     * 获取上个月第一天
     *
     * @param date
     * @return
     */
    public static Date getPreMonthFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 减一个月
        calendar.add(Calendar.MONTH, -1);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        calendar.set(year, month, 1, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 返回当前日的周一日期 （按中国传统：周一为首，周日为尾）
     *
     * @return
     */
    public static String getMondayDate(Date now) {
        return getMondayDate(now, DateUtil.PATTERN_TYPE2);
    }

    public static String getMondayDate(Date now, String pattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return DateUtil.format(calendar.getTime(), pattern);
    }

    public static String getSundayDate(Date now, String pattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return DateUtil.format(calendar.getTime(), pattern);
    }


    /**
     * 返回当前日的周一日期
     *
     * @return
     */
    public static String getMondayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return DateUtil.format(calendar.getTime(), DateUtil.PATTERN_TYPE2);
    }



    /**
     * 返回当前日的周二日期
     *
     * @return
     */
    public static String getTuesdayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        return DateUtil.format(calendar.getTime(), DateUtil.PATTERN_TYPE2);
    }

    /**
     * @return
     * @desc: 查询上周一日期
     */
    public static String getPreMondayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return DateUtil.format(calendar.getTime(), DateUtil.PATTERN_TYPE2);
    }

    /**
     * @return
     * @desc: 查询上周二日期
     */
    public static String getPreTuesdayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        return DateUtil.format(calendar.getTime(), DateUtil.PATTERN_TYPE2);
    }

    /**
     * 获取传入日期的季度第一天
     * 2018年7月9日
     */
    public static String getFriDayOfQuarter(Date date) {
        // return DateTime.now().AddMonths(0 - ((DateTime.now().getMonthOfYear() - 1) % 3)).ToString("yyyy-MM-01");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        long months = (long) (0 - ((month - 1) % 3));
        Date strDate = addMonths(date, months);
        return format(getFirstDay(strDate), "yyyy-MM-dd");

    }


    public static Date getDayBeginTime(Date date) {
        Date first = getDate(format(date, "yyyy-MM-dd") + " 00:00:00");
        return first;
    }

    public static Date getDayEndTime(Date date) {
        Date first = getDate(format(date, "yyyy-MM-dd") + " 23:59:59");
        return first;
    }

    /**
     * 得到日期对应的日份，以数字显示
     *
     * @param date 日期
     * @return
     */
    public static int getDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 计算有多少天，忽略小时
     */
    public static long getDays(long sec) {
        long days = sec / (60 * 60 * 24);
        return days;
    }

    //diamond\vip2_dao\VipPkCommonFunc.cpp#355
    public static long getTimeTypeInt(int act_time_type) {
        long ext_int = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        switch (act_time_type) {
            case 1:
                ext_int = calendar.get(Calendar.YEAR) * 10000 + (calendar.get(Calendar.MONTH) + 1) * 100 + calendar.get(Calendar.DATE);
                break;
            case 2:
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                calendar.add(Calendar.DATE, 1);
                ext_int = calendar.get(Calendar.YEAR) * 10000 + (calendar.get(Calendar.MONTH) + 1) * 100 + calendar.get(Calendar.DATE);
                break;
            case 3:
                ext_int = calendar.get(Calendar.YEAR) * 1000000 + (calendar.get(Calendar.MONTH) + 1) * 10000 + calendar.get(Calendar.DATE) * 100 + calendar.get(Calendar.HOUR_OF_DAY);
                break;
            default:
                break;
        }
        return ext_int;
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.from(date.toInstant().atZone(ZoneId.systemDefault()));
    }

    public static int getDayInt(Date time) {
        LocalDateTime dateTime = DateUtil.toLocalDateTime(time);
        return dateTime.getYear() * 10000 + dateTime.getMonthValue() * 100 + dateTime.getDayOfMonth();
    }

    public static int getHours(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MINUTE);
    }

    public static boolean isSecondEqual(Date d1, Date d2) {
        return d1.getTime()/1000 == d2.getTime() / 1000;
    }

    /** 获取当前小时的第1秒 */
    public static Date getHourFirstSecond(Date now) {
        String firstSecond = DateUtil.format(now, "yyyy-MM-dd HH:00:00");
        return DateUtil.getDate(firstSecond);
    }

    /** 获取当前小时的最后1秒 */
    public static Date getHourLastSecond(Date now) {
        String lastSecond = DateUtil.format(now, "yyyy-MM-dd HH:59:59");
        return DateUtil.getDate(lastSecond);
    }

    /** 获取当前天的第1秒 */
    public static Date getDayFirstSecond(Date now) {
        String firstSecond = DateUtil.format(now, "yyyy-MM-dd 00:00:00");
        return DateUtil.getDate(firstSecond);
    }

    /** 获取当前天的最后1秒 */
    public static Date getDayLastSecond(Date now) {
        String lastSecond = DateUtil.format(now, "yyyy-MM-dd 23:59:59");
        return DateUtil.getDate(lastSecond);
    }

    /** 获取当前周的第1秒 */
    public static Date getWeekFirstSecond(Date now) {
        String firstSecond = DateUtil.getMondayDate(now, "yyyy-MM-dd 00:00:00");
        return DateUtil.getDate(firstSecond);
    }

    /** 获取当前周的最后1秒 */
    public static Date getWeekLastSecond(Date now) {
        String lastSecond = DateUtil.getSundayDate(now, "yyyy-MM-dd 23:59:59");
        return DateUtil.getDate(lastSecond);
    }

    /** 获取当前月的第1秒 */
    public static Date getMonthFirstSecond(Date now) {
        return DateUtil.getFirstDay(now);
    }

    /** 获取当前月的最后1秒 */
    public static Date getMonthLastSecond(Date now) {
        Date first = DateUtil.getFirstDay(now);
        Date nextFirst = DateUtil.addMonths(first, 1);
        return DateUtil.addSeconds(nextFirst, -1);
    }

    /** 获取当前季的第1秒 */
    public static Date getSeasonFirstSecond(Date now) {
        String friDayOfQuarter = DateUtil.getFriDayOfQuarter(now);
        return DateUtil.getDate(friDayOfQuarter + " 00:00:00");
    }

    /** 获取当前季的最后1秒 */
    public static Date getSeasonLastSecond(Date now) {
        String friDayOfQuarter = DateUtil.getFriDayOfQuarter(now);
        Date date = DateUtil.getDate(friDayOfQuarter + " 00:00:00");
        Date nextFirst = DateUtil.addMonths(date, 3);
        return DateUtil.addSeconds(nextFirst, -1);
    }

    /** 获取当前年的第1秒 */
    public static Date getYearFirstSecond(Date now) {
        return DateUtil.getDate(DateUtil.format(now, "yyyy-01-01 00:00:00"));
    }

    /** 获取当前年的最后1秒 */
    public static Date getYearLastSecond(Date now) {
        return DateUtil.getDate(DateUtil.format(now, "yyyy-12-31 23:59:59"));
    }


    /** 获取上一小时的第1秒 */
    public static Date getPrevHourFirstSecond(Date now) {
        return getHourFirstSecond(DateUtil.addHours(now, -1));
    }

    /** 获取上一小时的最后1秒 */
    public static Date getPrevHourLastSecond(Date now) {
        return getHourLastSecond(DateUtil.addHours(now, -1));
    }

    /** 获取上一天的第1秒 */
    public static Date getPrevDayFirstSecond(Date now) {
        return getDayFirstSecond(DateUtil.add(now, -1));
    }

    /** 获取上一天的最后1秒 */
    public static Date getPrevDayLastSecond(Date now) {
        return getDayLastSecond(DateUtil.add(now, -1));
    }

    /** 获取上一周的第1秒 */
    public static Date getPrevWeekFirstSecond(Date now) {
        return getWeekFirstSecond(DateUtil.addWeeks(now, -1));
    }

    /** 获取上一周的最后1秒 */
    public static Date getPrevWeekLastSecond(Date now) {
        return getWeekLastSecond(DateUtil.addWeeks(now, -1));
    }

    /** 获取上一月的第1秒 */
    public static Date getPrevMonthFirstSecond(Date now) {
        return DateUtil.addMonths(getMonthFirstSecond(now), -1);
    }

    /** 获取上一月的最后1秒 */
    public static Date getPrevMonthLastSecond(Date now) {
        return DateUtil.addSeconds(getMonthFirstSecond(now), -1);
    }

    /** 获取上一季的第1秒 */
    public static Date getPrevSeasonFirstSecond(Date now) {
        return DateUtil.addMonths(getSeasonFirstSecond(now), -3);
    }

    /** 获取上一季的最后1秒 */
    public static Date getPrevSeasonLastSecond(Date now) {
        return DateUtil.addSeconds(getSeasonFirstSecond(now), -1);
    }

    /** 获取上一年的第1秒 */
    public static Date getPrevYearFirstSecond(Date now) {
        return DateUtil.addMonths(getYearFirstSecond(now), -12);
    }

    /** 获取上一年的最后1秒 */
    public static Date getPrevYearLastSecond(Date now) {
        return DateUtil.addSeconds(getYearFirstSecond(now), -1);
    }


    /** 获取下一小时的第1秒 */
    public static Date getNextHourFirstSecond(Date now) {
        return getHourFirstSecond(DateUtil.addHours(now, 1));
    }

    /** 获取下一小时的最后1秒 */
    public static Date getNextHourLastSecond(Date now) {
        return getHourLastSecond(DateUtil.addHours(now, 1));
    }

    /** 获取下一天的第1秒 */
    public static Date getNextDayFirstSecond(Date now) {
        return getDayFirstSecond(DateUtil.add(now, 1));
    }

    /** 获取下一天的最后1秒 */
    public static Date getNextDayLastSecond(Date now) {
        return getDayLastSecond(DateUtil.add(now, 1));
    }

    /** 获取下一周的第1秒 */
    public static Date getNextWeekFirstSecond(Date now) {
        return getWeekFirstSecond(DateUtil.addWeeks(now, 1));
    }

    /** 获取下一周的最后1秒 */
    public static Date getNextWeekLastSecond(Date now) {
        return getWeekLastSecond(DateUtil.addWeeks(now, 1));
    }

    /** 获取下一月的第1秒 */
    public static Date getNextMonthFirstSecond(Date now) {
        return DateUtil.addSeconds(getMonthLastSecond(now), 1);
    }

    /** 获取下一月的最后1秒 */
    public static Date getNextMonthLastSecond(Date now) {
        Date nowMonthFirstSecond = getMonthFirstSecond(now);
        return DateUtil.addSeconds(DateUtil.addMonths(nowMonthFirstSecond, 2), -1);
    }

    /** 获取下一季的第1秒 */
    public static Date getNextSeasonFirstSecond(Date now) {
        return DateUtil.addMonths(getSeasonFirstSecond(now), 3);
    }

    /** 获取下一季的最后1秒 */
    public static Date getNextSeasonLastSecond(Date now) {
        Date nowSeasonFirstSecond = getSeasonFirstSecond(now);
        return DateUtil.addSeconds(DateUtil.addMonths(nowSeasonFirstSecond, 6), -1);
    }

    /** 获取下一年的第1秒 */
    public static Date getNextYearFirstSecond(Date now) {
        return DateUtil.addMonths(getYearFirstSecond(now), 12);
    }

    /** 获取下一年的最后1秒 */
    public static Date getNextYearLastSecond(Date now) {
        return DateUtil.addSeconds(getYearLastSecond(now), 12);
    }
}
