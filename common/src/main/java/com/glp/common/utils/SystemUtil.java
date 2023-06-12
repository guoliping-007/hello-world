/**
 * @(#)LogUtil.java V0.0.1 2010-7-16
 */

package com.glp.common.utils;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SystemUtil(功能未确定).
 * <p>
 * 
 * @author 阿海
 * 
 */
public final class SystemUtil {
    private static final Logger log = LoggerFactory.getLogger(SystemUtil.class);

    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private SystemUtil() {

    }

    public static void defer(final long millis) {

        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 执行Shell程序
     * <p>
     * .
     * 
     * @param cmd
     * @return 输出信息.
     */
    public static String execShell(final String cmd) {
        return execShell(cmd, true);
    }

    public static String getMessage(final InputStream is) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
                br = null;
            }
        }
        return sb.toString();
    }

    public static String execShell(final String cmd, final boolean wait) {
        String msg = "";
        try {
            // cmd = "/bin/sh " + cmd;
            Process pro = Runtime.getRuntime().exec(cmd);
            if (wait) {

                InputStream es = pro.getErrorStream();
                msg = getMessage(es);
                // System.err.println(msg);
            }
        } catch (Exception e) {
            msg = e.getMessage();
            log.error(e.getMessage(), e);
        }
        return msg;
    }

    public static String getStackMessage(final StackTraceElement[] stack) {
        if (stack == null) {
            return null;
        }
        int size = stack.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < size; i++) {
            StackTraceElement ste = stack[i];
            if (i > 1) {
                sb.append("\t");
            }
            sb.append(ste.toString()).append("\n");
        }
        return sb.toString();
    }

    public static Object getField(final String className, final String fieldName) {
        @SuppressWarnings("rawtypes")
        Class c = null;
        Field field = null;
        try {
            c = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class(" + className + ")不存在.");
        }

        try {
            field = c.getField(fieldName);
            return field.get(null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Field(" + fieldName + ")不存在.");
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 从系统参数或环境变量中获取配置值
     * 
     * @param key
     * @return
     */
    public static String getPropertiesFromSystem(String key) {
        return getPropertiesFromSystem(key, "");
    }

    public static String getPropertiesFromSystem(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (StringUtils.isEmpty(value)) {
            value = System.getenv(key);
            if (StringUtils.isEmpty(value)) {
                value = defaultValue;
            }
        }
        return value.trim();
    }

    public static List<String> getHostAddress() {
        Enumeration<NetworkInterface> netInterfaces;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            return null;
        }
        List<String> list = new ArrayList<String>();
        while (netInterfaces.hasMoreElements()) {
            NetworkInterface ni = netInterfaces.nextElement();
            String displayName = ni.getDisplayName();
            log.info("getDisplayName:" + ni.getDisplayName());
            Enumeration<InetAddress> ips = ni.getInetAddresses();

            List<String> subList = null;
            if ("eth0".equalsIgnoreCase(displayName)) {
                Enumeration<NetworkInterface> subInterfaces = ni.getSubInterfaces();
                subList = getHostAddress(subInterfaces);
            }
            String currentIp = null;
            while (ips.hasMoreElements()) {
                InetAddress inet = ips.nextElement();
                String ip = inet.getHostAddress();
                if (ip.indexOf(":") == -1) {
                    // 不要使用IPv6
                    if (currentIp == null) {
                        currentIp = ip;
                    } else if (subList != null && !subList.contains(ip)) {
                        currentIp = ip;
                    } else {
                        // 忽略
                    }
                }
            }
            if (currentIp != null) {
                list.add(currentIp);
            }
        }
        return list;
    }

    private static List<String> getHostAddress(Enumeration<NetworkInterface> netInterfaces) {
        List<String> list = new ArrayList<String>();
        while (netInterfaces.hasMoreElements()) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> ips = ni.getInetAddresses();
            while (ips.hasMoreElements()) {
                InetAddress inet = ips.nextElement();
                String ip = inet.getHostAddress();
                if (ip.indexOf(":") == -1) {
                    // 不要使用IPv6
                    list.add(ip);
                }
            }
        }
        return list;
    }

    private static String myip = null;

    public static String getIp() {
        if (myip != null) {
            return myip;
        }

        List<String> iplist = getHostAddress();
        StringBuffer sb = new StringBuffer();
        for (String ip : iplist) {
            sb.append(" " + ip + " ");
        }
        log.info("this machine all ip are:" + sb);

        int size = iplist == null ? 0 : iplist.size();
        if (size == 0) {
            return null;
        }
        if (size == 1) {
            return myip = iplist.get(0);
        }
        // 尽量忽略 127.0.0.x
        for (String ip : iplist) {
            if (ip.startsWith("127.0.0.")) {
                continue;
            }
            if (ip.startsWith("10.")) {
                continue;
            }
            return myip = ip;
        }

        return myip = iplist.get(0);
    }

    private static String pid = null;

    public static String getPid() {
        if (pid != null) {
            return pid;
        }

        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_PATTERN);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        log.info(df.format(new Date()) + " ######## jvm pid:" + name + ", my ip:" + getIp() + " ####### ");
        return pid = name.split("@")[0];
    }

    public static String getTidDetail() {
        Thread ct = Thread.currentThread();
        return String.format("[%s]%s", ct.getId(), ct.getName());
    }

    public static String getTid() {
        Thread ct = Thread.currentThread();
        return String.valueOf(ct.getId());
    }

    public static String getWorkerInfo() {
        return String.format("ip=%s,pid=%s,tid=%s", getIp(), getPid(), getTidDetail());
    }

    public static String getWorkerBrief() {
        return String.format("%s/%s/%s", getIp(), getPid(), getTid());
    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
        log.info(getIp());
        log.info(getPid());
        log.info(getWorkerInfo());
        log.info(getWorkerBrief());
    }

}
