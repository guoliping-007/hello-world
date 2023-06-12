package com.glp.common.dao;

import java.util.List;

/**
 * 通用 mysql库业务操作类
 */
public class BaseMysqlService {
    private BaseMysqlDao baseMysqlDao;

    public BaseMysqlDao getBaseMysqlDao() {
        return baseMysqlDao;
    }

    public void setBaseMysqlDao(BaseMysqlDao baseMysqlDao) {
        this.baseMysqlDao = baseMysqlDao;
    }

    public <T> void insert(Class<T> clazz, T me) {
        baseMysqlDao.insert(clazz, me, null);
    }
    public <T> void insertIgnore(Class<T> clazz, T me) {
        baseMysqlDao.insertIgnore(clazz, me, null);
    }


    /**
     * 将 me 插入到表中
     *
     * @param clazz - 描述表的 字段、pk键、RowMapper 等关系的 model bean
     * @param me - 被插入的记录，值为null的被忽略，由数据库插入相应的 零值
     * @param tableName - 被插入的表指示，为空或null时将从 clazz 中推导出表名
     * @return 插入记录的数目，插入成功时为 1，失败为 0
     */
    public <T> void insert(Class<T> clazz, T me, String tableName) {
        baseMysqlDao.insert(clazz, me, tableName);
    }

    public <T> void insertOrUpdate(Class<T> clazz, T me) {
        baseMysqlDao.insertOrUpdate(clazz, me, null);
    }

    public <T> void batchInsert(Class<T> clazz, List<T> list, String tableName) {
        baseMysqlDao.batchInsert(clazz, list, tableName);
    }

    /**
     * 和 insert 类似，但对主键记录存在的会做 更新操作（而不是插入）
     *
     * @param clazz
     * @param me
     * @param tableName
     */
    public <T> void insertOrUpdate(Class<T> clazz, T me, String tableName) {
        baseMysqlDao.insertOrUpdate(clazz, me, tableName);
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
     * @param clazz - 描述表的 字段、pk键、RowMapper 等关系的 model bean
     * @param me - 删除条件，为 null 的属性不计为条件，比较操作符为等号
     * @param afterWhere - 跟在  where 条件后的任意合法sql，正确性由调用者保证，空或null被忽略
     * @param tableName - 被插入的表指示，为空或null时将从 clazz 中推导出表名
     * @return 被删除的记录数
     */
    public <T> int delete(Class<T> clazz, T me, String afterWhere, String tableName) {
        return baseMysqlDao.delete(clazz, me, afterWhere, tableName);
    }

    public <T> int update(Class<T> clazz, T me, T to) {
        return update(clazz, me, to, null, null, null);
    }

    public <T> int update(Class<T> clazz, T me, T to, String beforeWhere) {
        return update(clazz, me, to, beforeWhere, null, null);
    }

    public <T> int update(Class<T> clazz, T me, T to, String beforeWhere, String afterWhere) {
        return update(clazz, me, to, beforeWhere, afterWhere, null);
    }

    /**
     * 更新表中的记录
     *
     * @param clazz - 描述表的 字段、pk键、RowMapper 等关系的 model bean
     * @param me - 更新条件，为 null 的属性不计为条件，比较操作符为等号
     * @param to - 更新值，不包括主键属性和 null 值属性
     * @param beforeWhere - 排在  where 条件前的任意合法sql，正确性由调用者保证，空或null被忽略
     * @param afterWhere - 跟在  where 条件后的任意合法sql，正确性由调用者保证，空或null被忽略
     * @param tableName - 被插入的表指示，为空或null时将从 clazz 中推导出表名
     * @return 更新记录的数目
     */
    public <T> int update(Class<T> clazz, T me, T to, String beforeWhere, String afterWhere,
            String tableName) {
        return baseMysqlDao.update(clazz, me, to, beforeWhere, afterWhere, tableName);
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

    public <T> List<T> select(Class<T> clazz, T me, String afterWhere, boolean bForUpdate) {
        return select(clazz, me, afterWhere, bForUpdate, null);
    }

    /**
     * 从表中查询以 me 为条件记录
     *
     * @param clazz - 描述表的 字段、pk键、RowMapper 等关系的 model bean
     * @param me - 查询条件， 为 null 的属性不计为条件，比较操作符为等号
     * @param afterWhere - 跟在  where 条件后的任意合法sql，正确性由调用者保证，空或null被忽略
     * @param bForUpdate - 是否对选择的记录加行锁, true-加锁， false-不加锁
     * @param tableName - 被插入的表指示，为空或null时将从 clazz 中推导出表名
     * @return 查询得到的记录集合，集合不为null，但可空
     */
    public <T> List<T> select(Class<T> clazz, T me, String afterWhere, boolean bForUpdate, String tableName) {
        return baseMysqlDao.select(clazz, me, afterWhere, bForUpdate, tableName);
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
     * @param clazz - 描述表的 字段、pk键、RowMapper 等关系的 model bean
     * @param me - 查询条件，只取对应的pk属性，pk属性不可 null，否则失败，比较操作符为等号
     * @param bForUpdate - 是否对选择的记录加行锁, true-加锁， false-不加锁
     * @param tableName - 被插入的表指示，为空或null时将从 clazz 中推导出表名
     * @return 若找到，为对应的主键记录，否则为null
     */
    public <T> T selectByKey(Class<T> clazz, T me, boolean bForUpdate, String tableName) {
        return baseMysqlDao.selectByKey(clazz, me, bForUpdate, tableName);
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
     * @param clazz - 描述表的 字段、pk键、RowMapper 等关系的 model bean
     * @param me - 查询条件， 为 null 的属性不计为条件，比较操作符为等号
     * @param afterWhere - 跟在  where 条件后的任意合法sql，正确性由调用者保证，空或null被忽略
     * @param tableName - 被插入的表指示，为空或null时将从 clazz 中推导出表名
     * @return 记录数量
     */
    public <T> long count(Class<T> clazz, T me, String afterWhere, String tableName) {
        return baseMysqlDao.count(clazz, me, afterWhere, tableName);
    }

    public <T> int updateByKey(Class<T> clazz, T to) {
        return updateByKey(clazz, to, null);
    }

    /**
     * 按主键更新表中记录，主键值在  to 中，若表无主键，函数执行失败
     *
     * @param clazz - 描述表的 字段、pk键、RowMapper 等关系的 model bean
     * @param to - 更新条件，只取对应的pk属性，pk属性不可 null，否则失败，比较操作符为等号
     *             除主键外的所有非null值属性都将被更新
     * @param tableName - 被插入的表指示，为空或null时将从 clazz 中推导出表名
     * @return 更新记录的数目，为0或 1
     */
    public <T> int updateByKey(Class<T> clazz, T to, String tableName) {
        return baseMysqlDao.updateByKey(clazz, to, tableName);
    }

}
