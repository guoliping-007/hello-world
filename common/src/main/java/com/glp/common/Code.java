/**
 * Code.java
 */

package com.glp.common;

/**
 * 定义系统中的各种返回编码，部分模仿  http 协议中的状态码定义：
 * 
 * 	0 (表示成功，通用场景，大多数情况下可返回这个值）
 * 	1~99 (表示成功，特定场景，如有多种结果都能表示成功）
 * 	4xxx(客户端错误，请求包含语法错误或无法完成请求)
 * 	5xxx(服务器错误，服务器在处理请求的过程中发生了错误)
 *
 * @date 2017年7月28日 下午5:13:40
 */
public enum Code {
    // 0 (表示成功，通用场景，大多数情况下可返回这个值）
    OK(0, "ok"),

    // 1~99 (表示成功，特定场景，如有多种结果都能表示成功）
    //OK_GOOD(1, "成功"),

    DUP(1001,"重复设置"),

    // 4xxx(客户端错误，请求包含语法错误或无法完成请求)
    E_DATA_ERROR(4000, "E4000#参数错误"),
    E_WRONG_MAC(4001, "签名不正确"),
    E_IP_ALLOW(4002, "不在IP白名单中"),
    E_PARAM_NOTNULL(4003, "参数不能为空"),
    E_WRONG_PARAM(4004, " 参数不正确"),
    E_PARAM_ILLEGAL(4005, " 砸龙蛋功能正在系统维护中，开放时间请等待官方通知！"),
    E_OP_ILLEGAL(4006, " 非法操作"),
    E_NOT_LOGIN(4007, " 未登录状态"),
    E_FAIL_VERIFY_USER(4008, "用户验证失败！"),
    E_NO_ACTIVITY(4009, "活动不存在！"),
    E_NO_ACTIVITY_OR_END(4010, "活动不存在或已结束！"),
    E_OBTAINED_AWARD(4011, "奖品已经领取！"),
    E_NOT_OBTAIN_ALL_AWARD(4012, "未成功领取所有奖品！"),
    E_NOT_ENTER_CHANNEL(4013, "未进入频道！"),
    E_ILLEGAL_ORDER_PARAM(4014, "非法的订单参数"),
    E_OBTAINED_LOTTERY_FAIL(4015, "未中奖！"),
    E_OBTAINED_LOTTERY_ERROR(4017, "中奖异常请重试！"),
    
    E_ORDER_NOT_FOUND(4015, "订单不存在"),
    E_ORDER_PAYING(4016, "订单支付中"),
    E_ORDER_FAIL(4017, "订单已失败"),

    E_FAIL(4999, "请求失败，用来提示交易错误信息"),
    
    // 5xxx(服务器错误，服务器在处理请求的过程中发生了错误)
    E_DATA(5000, "E5000#系统维护中"),
    E_DB_OPER(5001, "数据库操作失败"),
    E_REDIS_OPER(5002, "数据库操作失败"),
    E_FAIL_GEN_ID(5003, "标识生成失败"),
    E_WRONG_TIMESTAMP(5004, "无效时间戳"),
    E_MONITOR_EXCEPTION(5101, "系统异常监控警告"),
    E_MONITOR_ELAPSE(5102, "系统耗时监控警告"),
    E_CONFIG_ERRO(5103, "[Conf]网络错误,请重试"),
    E_NO_BET(5104, "当前没有正在进行的游戏"),
    E_NO_BET_CONF(5105, "系统维护中"),

    
    // 表示所有原因未明的错误
    E_UNKNOWN(9999, "E9999#网络错误,请重试");

    /** 编码  */
    private int code;

    /** 编码对应的说明 */
    private String reason;

    private Code(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String toString() {
        return "[" + this.code + "]" + this.reason;
    }
}
