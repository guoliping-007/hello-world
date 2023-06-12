
package com.glp.common.support;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RedisSupport {

	private static Logger log = Logger.getLogger(RedisSupport.class);

	private static final Map<String, String> SCRIPT_MAP = new ConcurrentHashMap<String, String>();

    public static final String ZINCR_WITH_TIME = "zIncrWithTime.lua";

    public static final String ZBATCHINCR_WITH_TIME = "zBatchincrWithTime.lua";

	public static String getScript(String name) {
		String script = SCRIPT_MAP.get(name);
		if (script == null) {
			PathMatchingResourcePatternResolver prpr = new PathMatchingResourcePatternResolver();
			String scriptLocation = "classpath:/lua/";
			Resource resource = prpr.getResource(scriptLocation + name);
			InputStream is = null;
			try {
				if (resource != null) {
					is = resource.getInputStream();
					script = IOUtils.toString(is);
					SCRIPT_MAP.put(name, script);
					return script;
				}
			} catch (IOException e) {
				log.error("read script file error,script file name:" + name, e);
			} finally {
				IOUtils.closeQuietly(is);
			}
		}
		return script;
	}
}
