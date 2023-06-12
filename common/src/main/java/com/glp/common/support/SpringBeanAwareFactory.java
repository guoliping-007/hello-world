
package com.glp.common.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * sprring 容器的ApplicationContext感知工厂 ，启动web系统，初始化spring ioc容器将被该类感知
 * @date 2018年8月28日 下午8:27:41
 */
public class SpringBeanAwareFactory implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringBeanAwareFactory.applicationContext = applicationContext;
    }

    public static ApplicationContext getBeanFactory() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        if (applicationContext == null) {
            throw new NullPointerException(SpringBeanAwareFactory.class.getName()
                    + "exception:must asign value to the property beanFactory. name:" + name);
        }
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> classType) {
        if (applicationContext == null) {
            throw new NullPointerException(SpringBeanAwareFactory.class.getName()
                    + "exception:must asign value to the property beanFactory. classType:" + classType.getName());
        }
        return applicationContext.getBean(classType);
    }
}
