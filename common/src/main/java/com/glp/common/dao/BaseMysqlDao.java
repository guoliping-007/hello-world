/**
 * BaseMysqlDao.java / 2017年11月1日 上午10:48:17
 */

package com.glp.common.dao;

import com.glp.common.bean.Page;
import com.glp.common.exception.SuperException;
import com.glp.common.utils.BeanUtil;
import com.glp.common.utils.Convert;
import com.glp.common.utils.DateUtil;
import com.glp.common.utils.DynamicSqlHelper;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * 通用 mysql库数据操作类
 */
public class BaseMysqlDao implements InitializingBean {
    public static final Logger log = LoggerFactory.getLogger(BaseMysqlDao.class);
    // 空sql参数，用开占位的
    private static final Object[] EMPTY_SQL_PARAM = new Object[]{};

    protected JdbcTemplate jdbcTemplate;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public int update(String sql) {
        int ret = this.jdbcTemplate.update(sql);
        DynamicSqlHelper.print(sql, ret, EMPTY_SQL_PARAM);
        return ret;
    }

    public int update(String sql, Object... args) {
        int ret = this.jdbcTemplate.update(sql, args);
        DynamicSqlHelper.print(sql, ret, args);
        return ret;
    }

    /////////////////////////////////////////////////////////////////////////

    public List<Map<String, Object>> queryForList(String sql) {
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        return result == null ? Lists.newArrayList() : result;
    }

    public List<Map<String, Object>> queryForList(String sql, Object... args) {
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, args);
        return result == null ? Lists.newArrayList() : result;
    }

    public Map<String, Object> queryForMap(String sql) {
        return jdbcTemplate.queryForMap(sql);
    }

    public Map<String, Object> queryForMap(String sql, Object... args) {
        return jdbcTemplate.queryForMap(sql, args);
    }

    /////////////////////////////////////////////////////////////////////////

    private static <T> boolean isPrimitiveClass(Class<T> clazz) {
        return Number.class.isAssignableFrom(clazz) || String.class.isAssignableFrom(clazz)
                || Boolean.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz);
    }

    public <T> List<T> queryForList(String sql, Class<T> clazz, Boolean primitivesDefaultedForNullValue,
                                    Object... args) {
        List<T> result = null;
        if (isPrimitiveClass(clazz)) {
            result = this.jdbcTemplate.queryForList(sql, clazz, args);
        } else {
            BeanPropertyRowMapper<T> mapper = new BeanPropertyRowMapper<T>(clazz);
            mapper.setPrimitivesDefaultedForNullValue(primitivesDefaultedForNullValue);
            result = this.jdbcTemplate.query(sql, mapper, args);
        }
        return result == null ? Lists.newArrayList() : result;
    }

    public <T> List<T> queryForList(String sql, Class<T> clazz, Object... args) {
        return queryForList(sql, clazz, false, args);
    }

    public <T> List<T> queryForList(String sql, Class<T> clazz) {
        return queryForList(sql, clazz, EMPTY_SQL_PARAM);
    }

    public <T> List<T> queryForList(String sql, Class<T> clazz, Boolean primitivesDefaultedForNullValue) {
        return queryForList(sql, clazz, primitivesDefaultedForNullValue, EMPTY_SQL_PARAM);
    }

    public <T> T queryForObject(String sql, Class<T> clazz) {
        return this.queryForObject(sql, clazz, EMPTY_SQL_PARAM);
    }

    public <T> T queryForObject(String sql, Class<T> clazz, Object... args) {
        return this.queryForObject(sql, clazz, false, args);
    }

    public <T> T queryForObject(String sql, Class<T> clazz, Boolean primitivesDefaultedForNullValue) {
        return this.queryForObject(sql, clazz, primitivesDefaultedForNullValue, EMPTY_SQL_PARAM);
    }

    public <T> T queryForObject(String sql, Class<T> clazz, Boolean primitivesDefaultedForNullValue, Object... args) {
        List<T> result = this.queryForList(sql, clazz, primitivesDefaultedForNullValue, args);
        int size = result.size();
        return size == 0 ? null : result.get(0);
    }

    public <T> int insert(Class<T> clazz, T me) {
        return insert(clazz, me, null);
    }

    public <T> long insertWithReturnAutoId(Class<T> clazz, T me) {
        return insertWithReturnAutoId(clazz, me, null);
    }

    /**
     * 将 me 插入到表中，返回自增ID的值
     *
     * @param clazz     - 描述表的 字段、pk键、RowMapper 等关系的 model bean
     * @param me        - 被插入的记录，值为null的被忽略，由数据库插入相应的 零值
     * @param tableName - 被插入的表指示，为空或null时将从 clazz 中推导出表名
     *
     * @return 插入记录，返回自增ID的值（由调用者自己保证存在自增字段）
     */
    public <T> long insertWithReturnAutoId(Class<T> clazz, T me, String tableName) {
        if (clazz == null) {
            throw new RuntimeException("类对象不能为null");
        }

        tableName = SplitTableHelper.toTableName(tableName, me, clazz);
        Map<String, Object> map = BeanUtil.javaBean2Map(me);
        BeanUtil.removeNullAttr(map);

        int pos = 0;
        StringBuffer fields = new StringBuffer();
        StringBuffer holders = new StringBuffer();
        Object[] params = new Object[map.size()];
        for (Iterator<String> itr = map.keySet().iterator(); itr.hasNext(); ) {
            String attr = itr.next();
            String field = DynamicSqlHelper.getTableFields(clazz).get(attr);
            if (field == null) {
                throw new RuntimeException(attr + " 对应的表字段配置不存在！");
            }

            fields.append(field).append(itr.hasNext() ? ", " : "");
            holders.append("?").append(itr.hasNext() ? ", " : "");
            params[pos++] = map.get(attr);
        }

        String sql = "INSERT INTO " + tableName + "(" + fields + ") VALUES (  " + holders + "  )";
        DynamicSqlHelper.print(sql, params);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                for(int i=0; i<params.length; i++) {
                    ps.setObject(i+1, params[i]);
                }
                return ps;
            }
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public <T> int[] batchInsert(Class<T> clazz, List<T> list) {
        return batchInsert(clazz, list, null);
    }

    public <T> int[] batchInsertEx(Class<T> clazz, List<T> list) {
        return batchInsertEx(clazz, list, null);
    }

    public <T> int[] batchInsertNew(Class<T> clazz, List<T> list) {
        return batchInsertNew(clazz, list, null);
    }

    /**
     * <p>Description:批量插入 </p>
     */
    public <T> int[] batchInsert(Class<T> clazz, List<T> list, String tableName) {

        StringBuffer sqls = new StringBuffer();
        List<Object[]> batchArgs = Lists.newArrayList();

        boolean isFirst = true;

        for (T me : list) {
            tableName = SplitTableHelper.toTableName(tableName, me, clazz);
            Map<String, Object> map = BeanUtil.javaBean2Map(me);
            BeanUtil.removeNullAttr(map);

            int pos = 0;
            StringBuffer fields = new StringBuffer();
            StringBuffer holders = new StringBuffer();
            Object[] params = new Object[map.size()];
            for (Iterator<String> itr = map.keySet().iterator(); itr.hasNext(); ) {
                String attr = itr.next();
                String field = DynamicSqlHelper.getTableFields(clazz).get(attr);
                if (field == null) {
                    throw new RuntimeException(attr + " 对应的表字段配置不存在！");
                }

                fields.append(field).append(itr.hasNext() ? ", " : "");
                holders.append("?").append(itr.hasNext() ? ", " : "");
                params[pos++] = map.get(attr);
            }

            String sql = "INSERT INTO " + tableName + "(" + fields + ") VALUES (  " + holders + "  );";
            if (isFirst) {
                sqls.append(sql);
                isFirst = false;
            }

            batchArgs.add(params);
            DynamicSqlHelper.print(sql, params);
        }

        return jdbcTemplate.batchUpdate(sqls.toString(), batchArgs);
    }

    public <T> int[] batchInsertEx(Class<T> clazz, List<T> list, String tableName) {

        StringBuffer sqls = new StringBuffer();
        List<Object[]> batchArgs = Lists.newArrayList();

        boolean isFirst = true;

        for (T me : list) {
            tableName = SplitTableHelper.toTableName(tableName, me, clazz);
            Map<String, Object> map = BeanUtil.javaBean2Map(me);
            BeanUtil.removeNullAttr(map);

            int pos = 0;
            StringBuffer fields = new StringBuffer();
            StringBuffer holders = new StringBuffer();
            Object[] params = new Object[map.size()];
            for (Iterator<String> itr = map.keySet().iterator(); itr.hasNext(); ) {
                String attr = itr.next();
                String field = DynamicSqlHelper.getTableFields(clazz).get(attr);
                if (field == null) {
                    throw new RuntimeException(attr + " 对应的表字段配置不存在！");
                }

                fields.append(field).append(itr.hasNext() ? ", " : "");
                holders.append("?").append(itr.hasNext() ? ", " : "");
                params[pos++] = map.get(attr);
            }

            String sql = "INSERT INTO " + tableName + "(" + fields + ") VALUES (  " + holders + "  ) ON DUPLICATE KEY UPDATE consumed=consumed+1, utime=now();";
            if (isFirst) {
                sqls.append(sql);
                isFirst = false;
            }

            batchArgs.add(params);
            DynamicSqlHelper.print(sql, params);
        }

        return jdbcTemplate.batchUpdate(sqls.toString(), batchArgs);
    }

    public <T> int[] batchInsertNew(Class<T> clazz, List<T> list, String tableName) {

        StringBuffer sqls = new StringBuffer();
        List<Object[]> batchArgs = Lists.newArrayList();

        boolean isFirst = true;

        for (T me : list) {
            tableName = SplitTableHelper.toTableName(tableName, me, clazz);
            Map<String, Object> map = BeanUtil.javaBean2Map(me);
            BeanUtil.removeNullAttr(map);

            int pos = 0;
            StringBuffer fields = new StringBuffer();
            StringBuffer holders = new StringBuffer();
            Object[] params = new Object[map.size()];
            for (Iterator<String> itr = map.keySet().iterator(); itr.hasNext(); ) {
                String attr = itr.next();
                String field = DynamicSqlHelper.getTableFields(clazz).get(attr);
                if (field == null) {
                    throw new RuntimeException(attr + " 对应的表字段配置不存在！");
                }

                fields.append(field).append(itr.hasNext() ? ", " : "");
                holders.append("?").append(itr.hasNext() ? ", " : "");
                params[pos++] = map.get(attr);
            }

            Long addConsumed = Convert.toLong(map.get("consumed"));
            Date updateTime = (Date) map.get("utime");
            String date = DateUtil.format(updateTime, "yyyy-MM-dd HH:mm:ss");
            String afterUpdate = String.format("ON DUPLICATE KEY UPDATE consumed=consumed+%s, utime=now();", addConsumed);

            String sql = "INSERT INTO " + tableName + "(" + fields + ") VALUES (  " + holders + "  ) "+ afterUpdate;
            if (isFirst) {
                sqls.append(sql);
                isFirst = false;
            }

            batchArgs.add(params);
            DynamicSqlHelper.print(sql, params);
        }

        return jdbcTemplate.batchUpdate(sqls.toString(), batchArgs);
    }

    /**
     * 将 me 插入到表中
     *
     * @param clazz     - 描述表的 字段、pk键、RowMapper 等关系的 model bean
     * @param me        - 被插入的记录，值为null的被忽略，由数据库插入相应的 零值
     * @param tableName - 被插入的表指示，为空或null时将从 clazz 中推导出表名
     * @return 插入记录的数目，插入成功时为 1，失败为 0
     */
    public <T> int insert(Class<T> clazz, T me, String tableName) {
        if (clazz == null) {
            throw new RuntimeException("类对象不能为null");
        }

        tableName = SplitTableHelper.toTableName(tableName, me, clazz);
        Map<String, Object> map = BeanUtil.javaBean2Map(me);
        BeanUtil.removeNullAttr(map);

        int pos = 0;
        StringBuffer fields = new StringBuffer();
        StringBuffer holders = new StringBuffer();
        Object[] params = new Object[map.size()];
        for (Iterator<String> itr = map.keySet().iterator(); itr.hasNext(); ) {
            String attr = itr.next();
            String field = DynamicSqlHelper.getTableFields(clazz).get(attr);
            if (field == null) {
                throw new RuntimeException(attr + " 对应的表字段配置不存在！");
            }

            fields.append(field).append(itr.hasNext() ? ", " : "");
            holders.append("?").append(itr.hasNext() ? ", " : "");
            params[pos++] = map.get(attr);
        }

        String sql = "INSERT INTO " + tableName + "(" + fields + ") VALUES (  " + holders + "  )";
        DynamicSqlHelper.print(sql, params);
        return jdbcTemplate.update(sql, params);
    }

    /**
     * 将 me 插入到表中-重复唯一键不插入，不会报错
     *
     * @param clazz     - 描述表的 字段、pk键、RowMapper 等关系的 model bean
     * @param me        - 被插入的记录，值为null的被忽略，由数据库插入相应的 零值
     * @param tableName - 被插入的表指示，为空或null时将从 clazz 中推导出表名
     * @return 插入记录的数目，插入成功时为 1，失败为 0
     */
    public <T> int insertIgnore(Class<T> clazz, T me, String tableName) {
        if (clazz == null) {
            throw new RuntimeException("类对象不能为null");
        }

        tableName = SplitTableHelper.toTableName(tableName, me, clazz);
        Map<String, Object> map = BeanUtil.javaBean2Map(me);
        BeanUtil.removeNullAttr(map);

        int pos = 0;
        StringBuffer fields = new StringBuffer();
        StringBuffer holders = new StringBuffer();
        Object[] params = new Object[map.size()];
        for (Iterator<String> itr = map.keySet().iterator(); itr.hasNext(); ) {
            String attr = itr.next();
            String field = DynamicSqlHelper.getTableFields(clazz).get(attr);
            if (field == null) {
                throw new RuntimeException(attr + " 对应的表字段配置不存在！");
            }

            fields.append(field).append(itr.hasNext() ? ", " : "");
            holders.append("?").append(itr.hasNext() ? ", " : "");
            params[pos++] = map.get(attr);
        }

        String sql = "INSERT IGNORE INTO " + tableName + "(" + fields + ") VALUES (  " + holders + "  )";
        DynamicSqlHelper.print(sql, params);
        return jdbcTemplate.update(sql, params);
    }

    public <T> int insertOrUpdate(Class<T> clazz, T me) {
        return insertOrUpdate(clazz, me, null);
    }

    /**
     * 和 insert 类似，但对主键记录存在的会做 更新操作（而不是插入）
     */
    public <T> int insertOrUpdate(Class<T> clazz, T me, String tableName) {
        if (clazz == null) {
            throw new RuntimeException("类对象不能为null");
        }

        tableName = SplitTableHelper.toTableName(tableName, me, clazz);
        String[] tablePks = DynamicSqlHelper.getTablePks(clazz);
        if (tablePks.length == 0) {
            throw new RuntimeException(tableName + " 不存在主键!");
        }

        Map<String, Object> map = BeanUtil.javaBean2Map(me);
        BeanUtil.removeNullAttr(map);

        StringBuffer fields = new StringBuffer();
        StringBuffer holders = new StringBuffer();
        List<Object> params = new ArrayList<Object>();
        for (Iterator<String> itr = map.keySet().iterator(); itr.hasNext(); ) {
            String attr = itr.next();
            String field = DynamicSqlHelper.getTableFields(clazz).get(attr);
            if (field == null) {
                throw new RuntimeException(attr + " 对应的表字段配置不存在！");
            }

            fields.append(field).append(itr.hasNext() ? ", " : "");
            holders.append("?").append(itr.hasNext() ? ", " : "");
            params.add(map.get(attr));
        }

        // 检查 pk 情况，构建 set 子句
        Map<String, Object> map4where = BeanUtil.javaBean2Map(me, "", tablePks, true);
        if (BeanUtil.hasNullAttr(map4where)) {
            throw new RuntimeException(tableName + " 主键有 null 值!");
        }

        Map<String, Object> map4set = BeanUtil.javaBean2Map(me, "", tablePks, false);
        BeanUtil.removeNullAttr(map4set);
        String set = DynamicSqlHelper.getSet(map4set, params, clazz);
        /////////////////////////

        String sql = "INSERT INTO " + tableName + "(" + fields + ") VALUES (  " + holders
                + "  ) ON DUPLICATE KEY UPDATE " + set;

        DynamicSqlHelper.print(sql, params);
        return jdbcTemplate.update(sql, params.toArray());
    }


    /**
     * @param clazz
     * @param me        插入信息(包含主键)
     * @param update    更新信息
     * @param tableName void
     * @return <p>Description:插入或者更新其他信息 </p>
     * @throws
     */
    public <T> int insertOrUpdate(Class<T> clazz, T me, T update, String tableName) {
        if (clazz == null) {
            throw new RuntimeException("类对象不能为null");
        }

        tableName = SplitTableHelper.toTableName(tableName, me, clazz);
        String[] tablePks = DynamicSqlHelper.getTablePks(clazz);
        if (tablePks.length == 0) {
            throw new RuntimeException(tableName + " 不存在主键!");
        }

        Map<String, Object> map = BeanUtil.javaBean2Map(me);
        BeanUtil.removeNullAttr(map);

        StringBuffer fields = new StringBuffer();
        StringBuffer holders = new StringBuffer();
        List<Object> params = new ArrayList<Object>();
        for (Iterator<String> itr = map.keySet().iterator(); itr.hasNext(); ) {
            String attr = itr.next();
            String field = DynamicSqlHelper.getTableFields(clazz).get(attr);
            if (field == null) {
                throw new RuntimeException(attr + " 对应的表字段配置不存在！");
            }

            fields.append(field).append(itr.hasNext() ? ", " : "");
            holders.append("?").append(itr.hasNext() ? ", " : "");
            params.add(map.get(attr));
        }

        // 检查 pk 情况，构建 set 子句
        Map<String, Object> map4where = BeanUtil.javaBean2Map(me, "", tablePks, true);
        if (BeanUtil.hasNullAttr(map4where)) {
            throw new RuntimeException(tableName + " 主键有 null 值!");
        }

        Map<String, Object> map4set = BeanUtil.javaBean2Map(update, "", tablePks, false);
        BeanUtil.removeNullAttr(map4set);
        String set = DynamicSqlHelper.getSet(map4set, params, clazz);
        /////////////////////////

        String sql = "INSERT INTO " + tableName + "(" + fields + ") VALUES (  " + holders
                + "  ) ON DUPLICATE KEY UPDATE " + set;

        DynamicSqlHelper.print(sql, params);
        return jdbcTemplate.update(sql, params.toArray());
    }


    public <T> int delete(Class<T> clazz) {
        return delete(clazz, null, null, null);
    }

    public <T> int delete(Class<T> clazz, T me) {
        return delete(clazz, me, null, null);
    }

    public <T> int delete(Class<T> clazz, T me, String afterWhere) {
        return delete(clazz, me, afterWhere, null);
    }

    /**
     * 从表中删除以 me 为条件的记录
     *
     * @param clazz      - 描述表的 字段、pk键、RowMapper 等关系的 model bean
     * @param me         - 删除条件，为 null 的属性不计为条件，比较操作符为等号
     * @param afterWhere - 跟在  where 条件后的任意合法sql，正确性由调用者保证，空或null被忽略
     * @param tableName  - 被插入的表指示，为空或null时将从 clazz 中推导出表名
     * @return 被删除的记录数
     */
    public <T> int delete(Class<T> clazz, T me, String afterWhere, String tableName) {
        if (clazz == null) {
            throw new RuntimeException("类对象不能为null");
        }

        tableName = SplitTableHelper.toTableName(tableName, me, clazz);
        Map<String, Object> map4where = BeanUtil.javaBean2Map(me);
        BeanUtil.removeNullAttr(map4where);

        List<Object> params = new ArrayList<Object>();
        String where = DynamicSqlHelper.getWhere(map4where, params, clazz);

        afterWhere = afterWhere == null ? "" : " " + afterWhere;
        String sql = " DELETE FROM " + tableName + " WHERE " + where + afterWhere;
        DynamicSqlHelper.print(sql, params);
        return jdbcTemplate.update(sql, params.toArray());
    }

    public <T> int update(Class<T> clazz, T me, T to, boolean updateKey) {
        return update(clazz, me, to, null, null, null, updateKey);
    }

    public <T> int update(Class<T> clazz, T me, T to) {
        return update(clazz, me, to, null, null, null, false);
    }

    public <T> int update(Class<T> clazz, T me, T to, String beforeWhere) {
        return update(clazz, me, to, beforeWhere, null, null, false);
    }

    public <T> int update(Class<T> clazz, T me, T to, String beforeWhere, String afterWhere) {
        return update(clazz, me, to, beforeWhere, afterWhere, null, false);
    }

    public <T> int update(Class<T> clazz, T me, T to, String beforeWhere, String afterWhere,
                          String tableName) {
        return update(clazz, me, to, beforeWhere, afterWhere, tableName, false);
    }

    /**
     * 更新表中的记录
     *
     * @param clazz       - 描述表的 字段、pk键、RowMapper 等关系的 model bean
     * @param me          - 更新条件，为 null 的属性不计为条件，比较操作符为等号
     * @param to          - 更新值，不包括主键属性和 null 值属性
     * @param beforeWhere - 排在  where 条件前的任意合法sql，正确性由调用者保证，空或null被忽略
     * @param afterWhere  - 跟在  where 条件后的任意合法sql，正确性由调用者保证，空或null被忽略
     * @param tableName   - 被插入的表指示，为空或null时将从 clazz 中推导出表名
     * @param updateKey   - true 允许更新主键  false反之
     * @return 更新记录的数目
     */
    public <T> int update(Class<T> clazz, T me, T to, String beforeWhere, String afterWhere, String tableName,
                          boolean updateKey) {
        if (clazz == null) {
            throw new RuntimeException("类对象不能为null");
        }

        tableName = SplitTableHelper.toTableName(tableName, me, clazz);
        Map<String, Object> map4where = BeanUtil.javaBean2Map(me);
        BeanUtil.removeNullAttr(map4where);

        Map<String, Object> map4set =
                BeanUtil.javaBean2Map(to, "", DynamicSqlHelper.getTablePks(clazz), updateKey);
        BeanUtil.removeNullAttr(map4set);

        List<Object> params = new ArrayList<Object>();
        String set = DynamicSqlHelper.getSet(map4set, params, clazz);
        String where = DynamicSqlHelper.getWhere(map4where, params, clazz);

        afterWhere = afterWhere == null ? "" : " " + afterWhere;
        beforeWhere = beforeWhere == null ? "" : " " + beforeWhere + " ";
        String sql = " UPDATE " + tableName + " SET " + set + beforeWhere + " WHERE " + where + afterWhere;
        DynamicSqlHelper.print(sql, params);
        return jdbcTemplate.update(sql, params.toArray());
    }

    public <T> List<T> select(Class<T> clazz) {
        return select(clazz, null, null, false, null);
    }

    public <T> List<T> select(Class<T> clazz, T me) {
        return select(clazz, me, null, false, null);
    }

    public <T> List<T> select(Class<T> clazz, T me, String afterWhere) {
        return select(clazz, me, afterWhere, false, null);
    }

    public <T> T selectOne(Class<T> clazz, T me, String afterWhere) {
        List<T> list = select(clazz, me, afterWhere, false, null);
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public <T> List<T> select(Class<T> clazz, T me, String afterWhere, boolean bForUpdate) {
        return select(clazz, me, afterWhere, bForUpdate, null);
    }

    /**
     * 从表中查询以 me 为条件记录
     *
     * @param clazz      - 描述表的 字段、pk键、RowMapper 等关系的 model bean
     * @param me         - 查询条件， 为 null 的属性不计为条件，比较操作符为等号
     * @param afterWhere - 跟在  where 条件后的任意合法sql，正确性由调用者保证，空或null被忽略
     * @param bForUpdate - 是否对选择的记录加行锁, true-加锁， false-不加锁
     * @param tableName  - 被插入的表指示，为空或null时将从 clazz 中推导出表名
     * @return 查询得到的记录集合，集合不为null，但可空
     */
    public <T> List<T> select(Class<T> clazz, T me, String afterWhere, boolean bForUpdate, String tableName) {
        if (clazz == null) {
            throw new RuntimeException("类对象不能为null");
        }

        tableName = SplitTableHelper.toTableName(tableName, me, clazz);
        Map<String, Object> map4where = BeanUtil.javaBean2Map(me);
        BeanUtil.removeNullAttr(map4where);

        List<Object> params = new ArrayList<Object>();
        String where = DynamicSqlHelper.getWhere(map4where, params, clazz);
        String forUpdate = bForUpdate ? " FOR UPDATE " : " ";

        afterWhere = afterWhere == null ? "" : " " + afterWhere;
        String sql = " SELECT * FROM " + tableName + " WHERE " + where + afterWhere + forUpdate;
        //DynamicSqlHelper.print(sql, params);
        RowMapper<T> rowMapper = DynamicSqlHelper.getRowMapper(clazz);

        List<T> list = null;

        if (rowMapper == null) {
            list = jdbcTemplate.query(sql, params.toArray(), new BeanPropertyRowMapper<T>(clazz));
        } else {
            list = jdbcTemplate.query(sql, params.toArray(), rowMapper);
        }
        return list == null ? new ArrayList<T>(0) : list;
    }

    public <T> T selectByKey(Class<T> clazz, T me) {
        return selectByKey(clazz, me, false, null);
    }

    public <T> T selectByKey(Class<T> clazz, T me, boolean bForUpdate) {
        return selectByKey(clazz, me, bForUpdate, null);
    }

    /**
     * 按主键查找表中记录，主键值在 me 中，若表无主键，函数执行失败
     *
     * @param clazz      - 描述表的 字段、pk键、RowMapper 等关系的 model bean
     * @param me         - 查询条件，只取对应的pk属性，pk属性不可 null，否则失败，比较操作符为等号
     * @param bForUpdate - 是否对选择的记录加行锁, true-加锁， false-不加锁
     * @param tableName  - 被插入的表指示，为空或null时将从 clazz 中推导出表名
     * @return 若找到，为对应的主键记录，否则为null
     */
    public <T> T selectByKey(Class<T> clazz, T me, boolean bForUpdate, String tableName) {
        if (clazz == null) {
            throw new RuntimeException("类对象不能为null");
        }

        tableName = SplitTableHelper.toTableName(tableName, me, clazz);
        String[] tablePks = DynamicSqlHelper.getTablePks(clazz);
        if (tablePks.length == 0) {
            throw new RuntimeException(tableName + " 不存在主键!");
        }

        Map<String, Object> map4where = BeanUtil.javaBean2Map(me, "", tablePks, true);
        if (BeanUtil.hasNullAttr(map4where)) {
            return null;
        }
        List<Object> params = new ArrayList<Object>();
        String where = DynamicSqlHelper.getWhere(map4where, params, clazz);
        String forUpdate = bForUpdate ? " FOR UPDATE " : " ";

        String sql = " SELECT * FROM " + tableName + " WHERE " + where + forUpdate;
        //DynamicSqlHelper.print(sql, params);
        RowMapper<T> rowMapper = DynamicSqlHelper.getRowMapper(clazz);
        List<T> list = null;
        if (rowMapper == null) {
            list = jdbcTemplate.query(sql, params.toArray(),  new BeanPropertyRowMapper<T>(clazz));

        } else {
            list = jdbcTemplate.query(sql, params.toArray(), rowMapper);
        }
        int size = CollectionUtils.isEmpty(list) ? 0 : list.size();
        if (size == 0) {
            return null;
        } else if (size == 1) {
            return list.get(0);
        } else {
            throw new SuperException("query by pk return more than one result!", 9999);
        }
    }

    public <T> long count(Class<T> clazz) {
        return count(clazz, null, null, null);
    }

    public <T> long count(Class<T> clazz, T me) {
        return count(clazz, me, null, null);
    }

    public <T> long count(Class<T> clazz, T me, String afterWhere) {
        return count(clazz, me, afterWhere, null);
    }

    /**
     * 从表中查询以 me 为条件的记录数
     *
     * @param clazz      - 描述表的 字段、pk键、RowMapper 等关系的 model bean
     * @param me         - 查询条件， 为 null 的属性不计为条件，比较操作符为等号
     * @param afterWhere - 跟在  where 条件后的任意合法sql，正确性由调用者保证，空或null被忽略
     * @param tableName  - 被插入的表指示，为空或null时将从 clazz 中推导出表名
     * @return 记录数量
     */
    public <T> long count(Class<T> clazz, T me, String afterWhere, String tableName) {
        if (clazz == null) {
            throw new RuntimeException("类对象不能为null");
        }

        tableName = SplitTableHelper.toTableName(tableName, me, clazz);
        //log.info("count@tableName:" + tableName);
        Map<String, Object> map4where = BeanUtil.javaBean2Map(me);
        BeanUtil.removeNullAttr(map4where);

        List<Object> params = new ArrayList<Object>();
        String where = DynamicSqlHelper.getWhere(map4where, params, clazz);

        afterWhere = afterWhere == null ? "" : " " + afterWhere;
        String sql = " SELECT count(*) FROM " + tableName + " WHERE " + where + afterWhere;
        //DynamicSqlHelper.print(sql, params);
        //log.info("count@sql -> " + sql);
        return jdbcTemplate.queryForObject(sql, params.toArray(), Long.class);
    }

    public <T> int updateByKey(Class<T> clazz, T to) {
        return updateByKey(clazz, to, null);
    }

    /**
     * 按主键更新表中记录，主键值在  to 中，若表无主键，函数执行失败
     *
     * @param clazz     - 描述表的 字段、pk键、RowMapper 等关系的 model bean
     * @param to        - 更新条件，只取对应的pk属性，pk属性不可 null，否则失败，比较操作符为等号
     *                  除主键外的所有非null值属性都将被更新
     * @param tableName - 被插入的表指示，为空或null时将从 clazz 中推导出表名
     * @return 更新记录的数目，为0或 1
     */
    public <T> int updateByKey(Class<T> clazz, T to, String tableName) {
        if (clazz == null) {
            throw new RuntimeException("类对象不能为null");
        }

        tableName = SplitTableHelper.toTableName(tableName, to, clazz);
        String[] tablePks = DynamicSqlHelper.getTablePks(clazz);
        if (tablePks.length == 0) {
            throw new RuntimeException(tableName + " 不存在主键!");
        }

        Map<String, Object> map4where = BeanUtil.javaBean2Map(to, "", tablePks, true);
        if (BeanUtil.hasNullAttr(map4where)) {
            return 0;
        }
        Map<String, Object> map4set = BeanUtil.javaBean2Map(to, "", tablePks, false);
        BeanUtil.removeNullAttr(map4set);
        if (map4set.size() == 0) {
            return 0;
        }
        List<Object> params = new ArrayList<Object>();
        String set = DynamicSqlHelper.getSet(map4set, params, clazz);
        String where = DynamicSqlHelper.getWhere(map4where, params, clazz);

        String sql = " UPDATE " + tableName + " SET " + set + " WHERE " + where;
        DynamicSqlHelper.print(sql, params);
        return jdbcTemplate.update(sql, params.toArray());
    }

    public <T> Page<T> queryForPage(Class<T> clazz, String sql, int page, int pageSize, Object... params) {

        Page<T> ret = new Page<>();
        ret.setPage(page);
        ret.setPageSize(pageSize);

        String countSql = String.format(" select count(1) from ( %s ) t1  ", sql);
        long count = this.queryForObject(countSql, Long.class, params);
        if (count == 0) {
            ret.setTotal(0L);
            return ret;
        }

        int start = (page - 1) * pageSize;
        String selectSql = appendLimitSql(sql, start, pageSize);

        ret.setList(this.queryForList(selectSql, clazz, params));
        ret.setTotal(count);
        return ret;
    }


    private String appendLimitSql(String sql, int start, int size) {
        String endChar = ";";
        if (sql.endsWith(endChar)) {
            sql = sql.substring(0, sql.length() - 1);
        }
        return sql + " LIMIT " + start + "," + size + endChar;
    }

    public int[] batchUpdate(String sql, List<? extends Serializable> updates) {
        SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(updates);
        return namedParameterJdbcTemplate.batchUpdate(sql, params);
    }

    public int[] batchUpdateEx(String sql, List<Object[]> batchArgs) {
        return jdbcTemplate.batchUpdate(sql, batchArgs);
    }

}
