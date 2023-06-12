
package com.glp.common.exception;

import com.glp.common.Code;

@SuppressWarnings("serial")
public class SuperException extends RuntimeException {
    // 未知错误
    public static final int E_UNKNOWN = 99999;
    // 标识生成失败
    public static final int E_FAIL_GEN_ID = 99998;
    // 签名不正确
    public static final int E_WRONG_MAC = 99997;
    // 无效时间戳
    public static final int E_WRONG_TIMESTAMP = 99996;
    // 参数不正确
    public static final int E_WRONG_PARAM = 99994;
    // 数据库操作失败
    public static final int E_DB_OPER = -2;
    // 参数不能为空
    public static final int E_PARAM_NOTNULL = -3;
    // 数据错误
    public static final int E_DATA_ERROR = -4;
    // 不在IP白名单中
    public static final int E_IP_ALLOW = 99995;
    // 参数非法
    public static final int E_PARAM_ILLEGAL = 99994;
    // 系统配置出错
    public static final int E_CONF_ILLEGAL = 99993;
    // 请求失败，用来提示交易错误信息
    public static final int E_FAIL = 8888;

    protected int code = -1;

    public SuperException(String message) {
        super(message);
        this.code = 9999;
    }

    public SuperException(String message, int code) {
        super(message);
        this.code = code;
    }

    public SuperException(Code code) {
        super(code.getReason());
        this.code = code.getCode();
    }

    public int getCode() {
        return code;
    }

    public static int getErrCode(Throwable e) {
        return (e instanceof SuperException) ? ((SuperException)e).getCode() : E_UNKNOWN;
    }

    public static String getErrMessage(Throwable e) {
        return (e instanceof SuperException) ? ((SuperException)e).getMessage() : "E9999#网络错误,稍后重试";
    }
}
