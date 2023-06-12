/*
 * @(#)Convert.java
 */

package com.glp.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * &#64;date 2008-06-19
 *
 * &#64;功能说明：
 * 提供丰富的类型转换函数，比如 toInt、toLong、toFloat、toString 等
 * 
 * &#64;版本更新列表
 * 修改版本: 1.0.0
 * 修改日期：2008-06-19
 * 修改说明：形成初始版本
 * 复审人：
 * </pre>
 */

public class Convert {
    private static final Logger log = LoggerFactory.getLogger(Convert.class);

    /**
     * 将 data 中数据集转为 List<Long> 输出
     * 
     * @param data - 数据集
     * @param sep - 数据分隔符
     * @return
     */
    public static List<Long> toList(String data, String sep) {
        List<Long> list = new ArrayList<Long>();
        String[] strs = data.split(sep);
        for (String s : strs) {
            if (StringUtil.isEmpty(s)) {
                continue;
            }
            list.add(new Long(StringUtil.trim(s)));
        }

        return list;
    }

    public static List<Long> toList(String data) {
        return toList(data, ",");
    }

    /**
     * 转换成int
     * 
     * @param value
     * @param defaultValue 默认值
     * @return
     */
    public static int toInt(Object value, int defaultValue) {
        try {
            if (value == null) {
                return defaultValue;
            }
            return toInt(value);
        } catch (Exception e) {
        	 return defaultValue;
        } 
    }

    /**
     * 转换成int. 如果转换不成功,抛出NumberFormatException
     * 
     * @param value
     * @return
     */
    public static int toInt(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            String text = (String) value;
            return Integer.parseInt(text);
        }
        throw new NumberFormatException(toString(value) + " 不是可转换成int型的对象");
    }

    /**
     * 转换成long
     * 
     * @param value
     * @param defaultValue
     * @return
     */
    public static long toLong(Object value, long defaultValue) {
        try {

            if (value == null) {
                return defaultValue;
            }
            return toLong(value);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return defaultValue;
    }

    /**
     * 转换成long. 如果转换不成功,抛出NumberFormatException
     * 
     * @param value
     * @return
     */
    public static long toLong(Object value) {
        if (value == null || StringUtil.isBlank(value.toString())) {
            return 0L;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            String text = (String) value;
            return Long.parseLong(text.trim());
        }
        throw new NumberFormatException(toString(value) + " 不是可转换成long型的对象");
    }

    /**
     * 转换成float
     * 
     * @param value
     * @param defaultValue
     * @return
     */
    public static float toFloat(Object value, float defaultValue) {
        try {
            return toFloat(value);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return defaultValue;
    }

    /**
     * 转换成float, 如果转换不成功,抛出NumberFormatException
     * 
     * @param value
     * @return
     */
    public static float toFloat(Object value) {
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        if (value instanceof String) {
            String text = (String) value;
            return Float.parseFloat(text);
        }
        throw new NumberFormatException(toString(value) + " 不是可转换成float型的对象");
    }

    /**
     * 转换成double
     * 
     * @param value
     * @param defaultValue
     * @return
     */
    public static double toDouble(Object value, double defaultValue) {
        try {
        	if(value == null) {
        		return defaultValue;
        	}
            return toDouble(value);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return defaultValue;
    }

    /**
     * 转换成double. 如果转换不成功,抛出NumberFormatException
     * 
     * @param value
     * @return
     */
    public static double toDouble(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        if (value instanceof String) {
            String text = (String) value;
            return Double.parseDouble(text);
        }
        throw new NumberFormatException(toString(value) + " 不是可转换成double型的对象");
    }

    /**
     * 转换成boolean
     * 
     * @param value
     * @param defaultValue
     * @return
     */
    public static boolean toBoolean(Object value, boolean defaultValue) {
        try {
        	if(value == null) {
        		return defaultValue;
        	}
            return toBoolean(value);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return defaultValue;
    }

    /**
     * 转换成boolean. 如果转换不成功,抛出IllegalArgumentException
     * 
     * @param value
     * @return
     */
    public static boolean toBoolean(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            String text = (String) value;
            return "true".equalsIgnoreCase(text) || "1".equals(text) || "yes".equalsIgnoreCase(text)
                    || "y".equalsIgnoreCase(text);
        }
        throw new IllegalArgumentException(toString(value) + " 不是可转换成boolean型的对象");
    }


    /**
     * 转换成Sring
     * 
     * @param value
     * @return
     */
    public static String toString(Object value) {
        return toString(value, "");
    }

    /**
     * 将Map转成List返回
     * 
     * @param map
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List mapToList(Map map) {
        List list = new ArrayList();
        for (Iterator itr = map.keySet().iterator(); itr.hasNext();) {
            list.add(map.get(itr.next()));
        }
        return list;
    }



    /**
     * 转换成Sring
     * 
     * @param value
     * @return
     */
    public static String toString(Object value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof String) {
            return (String) value;
        } else if (value instanceof Date) {
            return DateUtil.format((Date) value);
        }else if (value instanceof byte[]){
            return new String((byte[]) value);
        }
        return String.valueOf(value);
    }

    /**
     * 得到统一格式的完整的时间串
     * 
     * @param fs - 时间串 ds 的格式
     * @param ds - 时间串
     * @return
     * @throws Exception
     */
    public static String toWholeFormatDateTime(String fs, String ds) throws Exception {
        try {
            fs = fs == null || fs.trim().length() == 0 ? "yyyy-MM-dd HH:mm:ss" : fs.trim();
            SimpleDateFormat f = new SimpleDateFormat(fs);
            Date date = f.parse(ds);
            ds = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
            return ds;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将 Map 中的值全部转换成串并返回一个新Map
     * 
     * @param map
     * @return
     */
    public static Map<String, String> toStringValueMap(Map<String, Long> map) {
        Map<String, String> result = new HashMap<String, String>(128);
        for (Iterator<String> itr = map.keySet().iterator(); itr.hasNext();) {
            String key = itr.next();
            String val = toString(map.get(key));
            result.put(key, val);
        }
        return result;
    }
}
