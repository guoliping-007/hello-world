
package com.glp.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Maps;

public class DynamicSqlHelper {
    private static final Logger log = LoggerFactory.getLogger(DynamicSqlHelper.class);

    /**
     * 存储类的相关静态属性值，用于访问加速
     */
    private static final Map<String, Object> CLASS_STATIC_PROPERTY_MAP = Maps.newConcurrentMap();

    /**
     * <p>获取表名 </p>
     *
     * @date 2017年6月30日 下午5:40:27
     * 
     * @param clazz
     * @return
     */
    public static String getTABLE_NAME(Class<?> clazz) {
        return getStaticPropertyValue(clazz, "TABLE_NAME");
    }

    /**
     * <p>获取表字段Map</p>
     *
     * @date 2017年6月30日 下午5:39:38
     * 
     * @param clazz
     * @return
     */
    public static Map<String, String> getTableFields(Class<?> clazz) {
        return getStaticPropertyValue(clazz, "TABLE_FIELDS");
    }

    /**
     * <p>获取表主键数组</p>
     *
     * @date 2017年6月30日 下午5:39:56
     * 
     * @param clazz
     * @return
     */
    public static String[] getTablePks(Class<?> clazz) {
        return getStaticPropertyValue(clazz, "TABLE_PKS");
    }

    @SuppressWarnings("unchecked")
    public static <T> T getStaticPropertyValue(Class<?> clazz, String property) {
        String key = clazz.getName() + "|" + property;
        Object value = CLASS_STATIC_PROPERTY_MAP.get(key);
        if (value == null) {
            value = getStaticPropertyObject(clazz, property);
            CLASS_STATIC_PROPERTY_MAP.put(key, value);
        }
        return (T)value;
    }

    /**
     * <p>提取静态属性值</p>
     *
     * @date 2017年6月30日 下午3:41:37
     * 
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Object getStaticPropertyObject(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getField(fieldName);
            if (!Modifier.isStatic(field.getModifiers())) {
                String clazzName = clazz.getName();
                throw new RuntimeException(clazzName + " has't static field:" + fieldName);
            }
            return field.get(clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**<p>获取select语句的投射字段</p>
    *
    * @date 2017年6月30日 下午3:01:43
    * 
    * @return
    */
    public static String getProjects(Class<?> clazz) {
        Map<String, String> fields = DynamicSqlHelper.getTableFields(clazz);
        StringBuffer where = new StringBuffer(" ");
        for (Iterator<String> itr = fields.keySet().iterator(); itr.hasNext();) {
            String key = itr.next();
            String value = fields.get(key);
            where.append(value + " AS " + key).append(itr.hasNext() ? ", " : " ");
        }
        return where.toString();
    }

    /**
    * 提取  ON DUPLICATE KEY UPDATE 的条件
    * @param map
    * @param values
    * @return
    */
    public static String getDuplicateKeyWhere(Map<String, Object> map, List<Object> values, Class<?> clazz) {
        Map<String, String> fields = DynamicSqlHelper.getTableFields(clazz);
        StringBuffer where = new StringBuffer("");
        if (map == null || map.size() == 0) {
            return where.toString();
        }
        for (Iterator<String> itr = map.keySet().iterator(); itr.hasNext();) {
            String attr = itr.next();
            String field = fields.get(attr);
            if (field == null) {
                throw new RuntimeException(attr + " 对应的表字段配置不存在！");
            }
            where.append(", " + field + "=? ");
            values.add(map.get(attr));
        }
        return where.substring(1);
    }

    /**
     * 提取  where 的条件 - for old， need del
     * @param map
     * @param values
     * @return
     */
    public static String getWhere(Map<String, Object> map, List<Object> values, Map<String, String> fields) {
        StringBuffer where = new StringBuffer(" 1=1 ");
        if (map == null || map.size() == 0) {
            return where.toString();
        }
        for (Iterator<String> itr = map.keySet().iterator(); itr.hasNext();) {
            String attr = itr.next();
            String field = fields.get(attr);
            if (field == null) {
                throw new RuntimeException(attr + " 对应的表字段配置不存在！");
            }
            where.append(" and " + field + "=?");
            values.add(map.get(attr));
        }
        return where.toString();
    }

    /**
     * 提取  where 的条件
     * @param map
     * @param values
     * @return
     */
    public static String getWhere(Map<String, Object> map, List<Object> values, Class<?> clazz) {
        Map<String, String> fields = DynamicSqlHelper.getTableFields(clazz);
        StringBuffer where = new StringBuffer(" 1=1 ");
        if (map == null || map.size() == 0) {
            return where.toString();
        }
        for (Iterator<String> itr = map.keySet().iterator(); itr.hasNext();) {
            String attr = itr.next();
            String field = fields.get(attr);
            if (field == null) {
                throw new RuntimeException(attr + " 对应的表字段配置不存在！");
            }
            where.append(" AND " + field + "=? ");
            values.add(map.get(attr));
        }
        return where.toString();
    }

    /**
     * 提取 set 的字段 - for old， need del
     * @param map
     * @param values
     * @return
     */
    public static String getSet(Map<String, Object> map, List<Object> values, Map<String, String> fields) {
        StringBuffer set = new StringBuffer();
        if (map == null || map.size() == 0) {
            return set.toString();
        }
        for (Iterator<String> itr = map.keySet().iterator(); itr.hasNext();) {
            String attr = itr.next();
            String field = fields.get(attr);
            if (field == null) {
                throw new RuntimeException(attr + " 对应的表字段配置不存在！");
            }
            set.append(", " + field + "=?");
            values.add(map.get(attr));
        }

        return set.substring(",".length());
    }

    /**
     * 提取 set 的字段
     * @param map
     * @param values
     * @return
     */
    public static String getSet(Map<String, Object> map, List<Object> values, Class<?> clazz) {
        Map<String, String> fields = DynamicSqlHelper.getTableFields(clazz);
        StringBuffer set = new StringBuffer();
        if (map == null || map.size() == 0) {
            return set.toString();
        }
        for (Iterator<String> itr = map.keySet().iterator(); itr.hasNext();) {
            String attr = itr.next();
            String field = fields.get(attr);
            if (field == null) {
                throw new RuntimeException(attr + " 对应的表字段配置不存在！");
            }
            set.append(", " + field + "=? ");
            values.add(map.get(attr));
        }

        return set.substring(",".length());
    }

    /**
     * 打印sql，捕获异常，以防万一 
     * @param sql
     * @param values
     */
    public static void print(String sql, Object[] values) {
        try {
            log.info("sql:[{}], args:{}", sql, StringUtil.toString(values));
        } catch (Throwable t) {
            log.error("sql:[{}], err:{}", sql, t.getMessage(), t);
        }
    }

    public static void print(String sql, int ret, Object[] values) {
        try {
            log.info("sql:[{}], args:{}, ret:{}", sql, StringUtil.toString(values), ret);
        } catch (Throwable t) {
            log.error("sql:[{}], ret:{}, err:{}", sql, ret, t.getMessage(), t);
        }
    }

    public static void print(String sql, List<Object> values) {
        print(sql, values.toArray());
    }

    public static void print(String sql, int ret, List<Object> values) {
        print(sql, ret, values.toArray());
    }

    /**<p>Description: </p>
     *
     * @date 2017年7月11日 下午9:00:54
     * 
     * @param clazz
     * @return
     */
    public static <T> RowMapper<T> getRowMapper(Class<T> clazz) {
        return getStaticPropertyValue(clazz, "ROW_MAPPER");
    }

    /**
     * 批量执行更新性的语句，暂时不限制批量大小（太大肯定会有问题），后面看实际使用情况调整
     * @date 2018年5月31日 上午11:32:24
     * 
     * @param hint - 提示性内容，方便日志搜索
     * @param sql - 语句模板，应该是 insert 或 update 语句
     * @param params - 批量参数内容
     */
    public static int excuteSqlBatchUpdate(JdbcTemplate jdbcTemplate, String hint, String sql, List<Object[]> params) {
        if (StringUtil.isBlank(sql) || CollectionUtils.isEmpty(params)) {
            return -1;
        }

        // params 最好不要超过1万个， 后面看需要改成分段批量执行
        int[] results = jdbcTemplate.batchUpdate(sql, params);
        for (int i = 0; i < params.size(); i++) {
            log.info("ExcuteSqlBatchUpdate ok@hint:{}, sql:{}, params:{} -> {}", hint, sql,
                StringUtil.toString(params.get(i)), results[i]);
        }

        return results == null ? -1 : results.length;
    }

    /**
     * 批量执行任何语句
     * @date 2018年5月31日 上午11:35:09
     * 
     * @param hint - 提示性内容，方便日志搜索
     * @param sqls - 组装好的完整sql语句集合
     * @param num - 每批执行的sql条数
     */
    public static void excuteSqlBatchCommand(JdbcTemplate jdbcTemplate, String hint, List<String> sqls, int num) {
        if (CollectionUtils.isEmpty(sqls)) {
            return;
        }

        num = Math.max(1, num);
        int count = 0;
        String sqlstr = "";
        for (String sql : sqls) {
            sql = sql.trim();
            if (sql.isEmpty()) {
                continue;
            }

            count++;
            // 补上 ; ，确保多条语句被分开
            if (!sql.endsWith(";")) {
                sql += ";";
            }
            // 让sql语句直接分行隔开
            sqlstr += sql + "\n";

            if ((count % num) == 0) {
                log.info("ExcuteSqlBatchCommand@hint:{} -> sql:{}", hint, sqlstr);
                jdbcTemplate.execute(sqlstr);
                sqlstr = "";
            }
        }

        if (!StringUtil.isBlank(sqlstr)) {
            log.info("ExcuteSqlBatchCommand@hint:{} -> sql:{}", hint, sqlstr);
            jdbcTemplate.execute(sqlstr);
        }
    }

    /**
     * 执行sql语句（可能包括多条）
     * @date 2018年5月31日 上午11:41:42
     * 
     * @param hint - 提示性内容，方便日志搜索
     * @param sql - 1~n 条混合sql
     */
    public static void excuteSqlBatchCommand(JdbcTemplate jdbcTemplate, String hint, String sql) {
        if (StringUtil.isBlank(sql)) {
            return;
        }
        log.info("ExcuteSqlBatchCommand@hint:{} -> sql:{}", hint, sql);
        jdbcTemplate.execute(sql);
    }
}
