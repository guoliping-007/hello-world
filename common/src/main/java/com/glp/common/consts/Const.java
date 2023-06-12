/**
 * Const.java / 2017年11月1日 上午10:48:17
 */

package com.glp.common.consts;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 常量定义类-全局就这一个类（除和业务相关但数量众多含义单一的常量，可另起一个文件专门存放，其余常量值应该都放到这里来。
 * 对于数量较少但表示同一类属性值的的多个常量，应该启用内部类的方式限定起来，比如 SYS 就是系统级的公用常量
 * 
 * @date 2017年11月1日 上午10:53:56
 */
public class Const {
    ////////////////////////////////////////////////////////////////////////////
    // 构造一个全局随机数发生器
    public static Random RD = new Random(System.currentTimeMillis());


    /**
     * 制作线程池 的方法
     */
    public static ExecutorService makeExecutorService(int corePoolSize) {
        return makeExecutorService(corePoolSize, 0);
    }
    public static ExecutorService makeExecutorService(int corePoolSize, long keepAliveTime) {
        ThreadFactory namedThreadFactory = Executors.defaultThreadFactory();
        LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        return new ThreadPoolExecutor(corePoolSize, corePoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue, namedThreadFactory);
    }

    //////////////////////////////////////////////////////////////////////////
    // 公用线程池，用来异步执行一些非重要操作，这里只给100个线程，对于需要及时完成的任务应该使用专用线程池
    public static final ExecutorService EXECUTOR_GENERAL = makeExecutorService(100);


    // 环境类型
    public static class ENV {
        public static final String LOCAL = "local"; // 本地调测环境

        public static final String DEV = "dev"; // 开发测试环境

        public static final String DEPLOY = "deploy"; // 生产部署环境
    }

}
