/*
 * @(#)BeanUtils.java
 *
 */

package com.glp.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.collections.CollectionUtils;


/**
 * <pre>
 * &#64;date 2011-07-24
 *
 * &#64;功能说明：
 * Bean工具类.
 * 
 * &#64;版本更新列表：
 * 修改版本：1.0.0
 * 修改日期：2011-07-24
 * 修改说明：初始版本
 * 复审人：
 * </pre>
 */

public class BeanUtil {
    /**
     * 将 java bean 中的值以map方式返回， map的key是 bean 的属性名，map的value是bean的值
     * 
     * @param javaBean - 要转换的 java bean
     * @return
     */
    public static Map<String, Object> javaBean2Map(Object javaBean) {
        return javaBean2Map(javaBean, null);
    }

    /**
     * 将 java bean 中的值以map方式返回， map 的 key = 前缀 + bean属性名，map的value是bean的值
     * 
     * @param javaBean - 要转换的 java bean
     * @param prefix - 换成map时候的key前缀
     * @return
     */
    public static Map<String, Object> javaBean2Map(Object javaBean, String prefix) {
        return javaBean2Map(javaBean, prefix, null, false);
    }

    /**
     * 排除/选中 指定属性名后，将 java bean 中的值以 map 方式返回 map 的 key = 前缀 + bean属性名，map的value是bean的值
     * 
     * @param javaBean - 要转换的 java bean
     * @param prefix - 换成map时候的key前缀
     * @param attrs - 属性集合 ：警告，由于代码生成器历史问题，此处原参数为数据库字段， 20160224 modified by zhangjia
     *        为了兼容之前的代码，此处通过调用getFieldAttrName方法将attrs中的数据库字段转换为Bean属性（驼峰命名法）
     * @param bIn - ture: 选择 attrs 中的属性，false：排除 attrs 中的属性
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, Object> javaBean2Map(Object javaBean, String prefix, String[] attrs,
            boolean bIn) {

        if (javaBean == null) {
            return new HashMap<String, Object>(128);
        }

        if (attrs == null) {
            attrs = new String[0];
        }
        prefix = prefix == null ? "" : prefix.trim();
        Map beanMap = new BeanMap(javaBean);
        Map<String, Object> result = new HashMap<String, Object>(128);
        Set keys = beanMap.keySet();
        Iterator keyIterator = keys.iterator();
        while (keyIterator.hasNext()) {
            String name = (String) keyIterator.next();
            Object value = beanMap.get(name);
            if (value instanceof Class) {
                continue;
            }
            String key = prefix + name;
            result.put(key, value);
        }

        if (bIn) {
            Map<String, Object> map = new HashMap<String, Object>(128);
            for (String attr : attrs) {
                // 将数据库字段转换为Javabean 属性
                String key = prefix + getFieldAttrName(attr);
                Object val = result.get(key);
                if (val == null) {
                    continue;
                }
                map.put(key, val);
            }
            result = map;
        } else {
            for (String attr : attrs) {
                // 将数据库字段转换为Javabean 属性
                String key = prefix + getFieldAttrName(attr);
                result.remove(key);
            }
        }

        return result;
    }

    /**
     * 移除空值属性
     * 
     * @param map
     */
    public static void removeNullAttr(Map<String, Object> map) {
        List<String> attrs = new ArrayList<String>();
        for (Iterator<String> itr = map.keySet().iterator(); itr.hasNext();) {
            String attr = itr.next();
            Object val = map.get(attr);
            if (val == null) {
                attrs.add(attr);
            }
        }
        for (String attr : attrs) {
            map.remove(attr);
        }
    }

    /**
     * 是否有 null 属性
     * 
     * @param map
     * @return
     */
    public static boolean hasNullAttr(Map<String, Object> map) {
        for (Iterator<String> itr = map.keySet().iterator(); itr.hasNext();) {
            String attr = itr.next();
            if (map.get(attr) == null) {
                return true;
            }
        }
        return false;
    }


    /**
     * 
     * @desc: 将数据库字段（带下划线）转化为Java属性
     * 
     * @param @param fldname
     * @param @return
     * @param @throws Exception
     * 
     */
    public static String getFieldAttrName(String fldname) {
        String flagChar = "_";
        if (!fldname.contains(flagChar)) {
            return fldname;
        }
        fldname = StringUtil.trim(fldname).toLowerCase();
        String[] strs = fldname.split(flagChar);
        String attrName = "";

        for (String str : strs) {
            attrName += StringUtil.upperCaseInitial(str);
        }

        if (attrName.length() == 0) {
            throw new RuntimeException("为字段产生的属性名为空 -> " + attrName);
        }

        attrName = StringUtil.lowerCaseInitial(attrName);

        if (attrName.length() > 1) {
            String s1 = attrName.substring(0, 1);
            String s2 = attrName.substring(1, 2);
            String s3 = attrName.substring(2);
            attrName = s1 + s2.toLowerCase() + s3;
        }
        return attrName;
    }


    /**
     * 
     * @desc: TODO
     * 
     * @param beanList
     * @param fieldName
     * @return
     * @throws Exception
     * @throws SecurityException
     * 
     * @date 2017年4月7日
     */
    @SuppressWarnings("unchecked")
    public static <K, T> List<T> getBeanFieldValues(List<K> beanList, String fieldName) {
        if (CollectionUtils.isEmpty(beanList)) {
            return new ArrayList<>();
        }
        try {
            Class<?> clazz = beanList.get(0).getClass();
            Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            List<T> fieldValues = new ArrayList<>();
            for (K k : beanList) {
                fieldValues.add((T) f.get(k));
            }
            return fieldValues;
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }

}
