
package com.glp.common.dao;

import com.glp.common.utils.DynamicSqlHelper;

/**
 * 分表逻辑处理类
 */
public class SplitTableHelper {

    /**
     * 判断是否要做分表
     */
    public static boolean isSplitTable() {
        return true;
    }




    /**
     * <p>
     * 计算表名，用于分表等场合确定分表名
     *    优先考虑用户制定的表名 tableName，
     *    其次从 me 中提取（可根据字段值决定最后的表名）
     *    最后就是 clazz 关联的默认表名 
     * </p>
     */
    public static String toTableName(String tableName, Object me, Class<?> clazz) {

        if (tableName != null && tableName.trim().length() > 0) {
            return tableName;
        }

        tableName = DynamicSqlHelper.getTABLE_NAME(clazz);



        return tableName;
    }

}
