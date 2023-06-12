/*
 * @(#)Locker.java 2015-08-29
 */
package com.glp.common.locker;

/**
 * @功能说明
 * <pre>
 * 加解锁接口定义, 锁的稳定性 及 全局程度依赖于存储、访问的实现方案。
 * </pre>
 * 
 * @版本更新
 * <pre>
 * 修改说明：形成初始版本
 * 复审人：
 * </pre>
 */
public interface Locker {
    /** 假设公司生产机器之间3秒误差（由公司运维保证）*/
    public static final int MACHINE_TIME_DIFFERENCE = 3;

    /**
     * 加锁：
     * 1）本函数只会做1次加锁操作，成功加上的锁的存活时间默认为 30 秒（合理缺省值防止误用）
     * 2）若不主动解锁，则 30 秒后锁自动失效
     * 
     * @param name - 锁的名称（命名规则由具体实现类决定）
     * @return 解锁钥匙（解锁时必须提供，防止误解别人的锁），加锁失败返回null，加锁成功返回锁对象（锁在网内全局唯一）
     */
    public Secret lock(String name);

    /**
     * 加锁：
     * 1）本函数只会做1次加锁操作，成功加上的锁的存活时间为 ttl 秒（合理缺省值防止误用）
     * 2）若不主动解锁，则 ttl 秒后，锁自动失效
     * 
     * @param name - 锁的名称（命名规则由具体实现类决定）
     * @param ttl  - 成功加的锁的存活时间（单位：秒），若小于或等于 0, 则默认为 1
     * @return 解锁钥匙（解锁时必须提供，防止误解别人的锁），加锁失败返回null，加锁成功返回锁对象（锁在网内全局唯一）
     */
    public Secret lock(String name, int ttl);

    /**
     * 加锁：
     * 1）尝试 count 次加锁，每次加锁失败后等待 1 秒再重试，成功加上的锁的存活时间为 ttl 秒
     * 2）若不主动解锁，则 ttl 秒后，锁自动失效
     * 
     * @param name - 锁的名称（命名规则由具体实现类决定）
     * @param ttl  - 成功加的锁的存活时间（单位：秒），若小于或等于 0, 则默认为 1
     * @param extdat - 任意的扩展数据（不可含 | 钥匙信息分隔符号）
     * @param count- 加锁尝试次数，若小于或等于 0, 则默认为 1
     * @return 解锁钥匙（解锁时必须提供，防止误解别人的锁），加锁失败返回null，加锁成功返回锁对象（锁在网内全局唯一）
     */
    public Secret lock(String name, int ttl, String extdat, int count);

    /**
     * 解锁：
     * 1）只有 name、secret 都匹配了，才能解除 匹配 的锁定（发生实际的解锁操作）
     * 2）若  name、secret 任一不匹配，则是解别人持有的锁，或不存在的锁（过期锁/已被解除的锁/从未存在的锁）
     * 3）本函数只会做1次解锁操作（合理缺省值防止误用）
     * 4）若name/secret 为 null 或空白串，则解锁必返回失败（因为是操作一个无意义的锁！ 后果由调用者自己负责）
     * 5）从业务安全角度理解，使用者必须解锁成功，才能让业务安全，否则可能带来死锁
     * 
     * @param name - 锁的名字
     * @param secret - 解锁钥匙
     * 
     * @return true ：成功解锁（真实删除过一个存在的锁，业务可认为期间是锁定独占的）
     *         false：解锁失败（钥匙不对 或 锁不存在 或 超过尝试次数，不能断定锁被自己一直独占）
     */
    public boolean unlock(String name, Secret secret);

    /**
     * 解锁：
     * 1）只有 name、secret 都匹配了，才能解除 匹配 的锁定（发生实际的解锁操作）
     * 2）若  name、secret 任一不匹配，则是解别人持有的锁，或不存在的锁（过期锁/已被解除的锁/从未存在的锁）
     * 3）本函数会尝试 count次解锁（如网络导致解锁异常情况才会尝试，钥匙不对 或 锁不存在情况不尝试，直接返回结果），每次尝试失败后等待 1 秒再重试，直到解锁成功
     * 4）若name/secret 为 null 或空白串，则解锁必返回失败（因为是操作一个无意义的锁！ 后果由调用者自己负责）
     * 5）从业务安全角度理解，使用者必须解锁成功，才能让业务安全，否则可能带来死锁
     * 
     * @param name - 锁的名字
     * @param secret - 解锁钥匙
     * @param count- 解锁尝试次数，若小于或等于 0, 则默认为 1
     * 
     * @return true ：成功解锁（真实删除过一个存在的锁，业务可认为期间是锁定独占的）
     *         false：解锁失败（钥匙不对 或 锁不存在 或 超过尝试次数，不能断定锁被自己一直独占）
     */
    public boolean unlock(String name, Secret secret, int count);

    /**
     * 看下锁是否超时过期
     * 
     * @param name - 锁的名字
     * @param secret - 锁当前的钥匙
     * @param ttl - 锁之前设置过期时长，用于比对（单位为秒）
     * 
     * @return true :当前花费的时间小于等于锁定时长
     *         false:没有找到秘钥上的时间或当前花费的时间大于锁定时长
     */
    public boolean review(String name, Secret secret, long ttl);

    /**
     * 核验锁name 和 钥匙 secret 匹配后，设置过期时间为  ttl 秒
     * 
     * @param name - 锁的名字
     * @param secret - 锁当前的钥匙
     * @param ttl - 锁要新设置的过期时间（单位为秒）
     * 
     * @return true :成功设置到过期时间（key存在，且secret核验正确）, 
     *         false:没有实际更新到过期时间（key不存在 或者 secret核验不正确）
     *         
     * @date 2019年7月16日 上午9:33:52
     */
    public boolean expire(String name, Secret secret, int ttl);

    /**
     * 粘附抢锁设置，让抢到锁的执行机器一直优先得到锁，尽量保持任务稳定的在同一台机器上执行
     * 优点是：任务能稳定的在同一台机器上执行，防止在不同机器之间漂移引起处理节奏不稳定
     * 缺点是：任务分配不均衡，除非发生锁过期，其它机器才有机会抢到锁去执行任务
     * 
     * 【建议使用场景】：轻型任务（如执行时0.1秒）、执行频度高（如每3秒运行一次）、对执行频度动态小幅变化也敏感（如前端倒计时更新不及时会导致跳秒）
     * 
     * @param name - 锁的名字
     * @param secret - 锁当前的钥匙
     * @param period - 结合业务的 执行周期（单位为秒，如 30秒一次， 3600秒一次）
     * 
     * @return true :成功设置到过期时间（key存在，且secret核验正确）, 
     *         false:未实际更新到过期时间（key不存在 或者 secret核验不正确）
     * 
     * @date 2019年7月17日 下午6:11:27
     */
    public boolean stick(String name, Secret secret, int period);

    /**
     * 概率转移抢锁设置，使得 1:probability 的几率让锁提前过期并让自己睡眠一小段时间，让其它机器有更大几率碰到锁过期而抢锁成功，
     * （至于其它机器在这种便利条件下能否抢锁成功，仍然是不确定的，但只要次数够多，其它机器总有机会抢到锁的）
     * 优点是：可让任务执行负载均衡
     * 缺点是：任务在不同机器之间漂移时，引起处理节奏不稳定，设置合适的  probability 值可缓解漂移频率，降低影响
     * 
     * 【建议使用场景】：重型任务（如执行时长超过1秒）、执行频度低（如每30秒运行一次）、对执行频度动态小幅变化不敏感
     * 
     * @param name - 锁的名字
     * @param secret - 锁当前的钥匙
     * @param period - 结合业务的 执行周期（单位为秒，如 30秒一次， 3600秒一次）
     * @param probability - 提前过期锁概率，1:probability，比如 4 表示 1/4=25%, 1/10000 = 0.01% 等, 若小于1按1算
     * 
     * @return true :成功设置到过期时间（key存在，且secret核验正确）, 
     *         false:未实际更新到过期时间（key不存在 或者 secret核验不正确）
     * 
     * @date 2019年7月17日 下午6:15:29
     */
    public boolean turn(String name, Secret secret, int period, int probability);

    /**
     * 换钥匙 - 验证 name 锁的当前钥匙 csecret 匹配后，产生新的钥匙并将过期时间设置为 ttl（注意还是原来的锁）
     * 
     * @param name - 锁的名字
     * @param csecret - 锁当前的钥匙
     * @param ttl - 替换钥匙成功时给 锁新设置的过期时间（单位为秒）
     * 
     * @return 成功：返回新锁对象， 失败：返回 null 
     * 
     * @date 2019年7月16日 下午11:32:36
     */
    public Secret replace(String name, Secret csecret, int ttl);

    /**
     * 换钥匙 - 验证 name 锁的当前钥匙 csecret 匹配后，产生新的钥匙并将过期时间设置为 ttl（注意还是原来的锁）
     * 
     * @param name - 锁的名字
     * @param csecret - 锁当前的钥匙
     * @param ttl - 替换钥匙成功时给 锁新设置的过期时间（单位为秒）
     * @param extdat - 任意的扩展数据（不可含 | 钥匙信息分隔符号）
     * 
     * @return 成功：返回新锁对象， 失败：返回 null 
     * 
     * @date 2019年7月16日 下午11:32:36
     */
    public Secret replace(String name, Secret csecret, int ttl, String extdat);
}
