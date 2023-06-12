package com.glp.common.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReadFileUtil {
    private static final Logger log = LoggerFactory.getLogger(ReadFileUtil.class);
    
    private static Map<String, String> scriptMap = new ConcurrentHashMap<String, String>();

    public static List<String> readFromFile(String filename, String encoding) {
        PathMatchingResourcePatternResolver prpr = new PathMatchingResourcePatternResolver();
        Resource resource = prpr.getResource(filename);
        InputStream is = null;
        try {
            if (resource != null) {
                is = resource.getInputStream();
                @SuppressWarnings("unchecked")
				List<String> lines = IOUtils.readLines(is, encoding);
                List<String> list = new ArrayList<String>();
                for (String line : lines) {
					if(line==null) {
					    continue;
                    }
					line = line.trim();
					if(line.startsWith("#")) {
					    continue;
                    }
					list.add(line);
				}
                
                log.info("read file by name:{}", filename);
                return list;
            }
        } catch (IOException e) {
            log.error("read file {} error", filename);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return null;
    }
    
    
    /**
     * 获取本项目的lua脚本
     * @param name
     * @return
     */
	public static String getLuaScript(String name) {
        String script = scriptMap.get(name);
        if (script == null) {
            PathMatchingResourcePatternResolver prpr = new PathMatchingResourcePatternResolver();
            String scriptLocation = "classpath:/lua/";
			Resource resource = prpr.getResource(scriptLocation  + name);
            InputStream is = null;
            try {
                if (resource != null) {
                    is = resource.getInputStream();
                    script = IOUtils.toString(is);
                    scriptMap.put(name, script);
                    return script;
                }
            } catch (IOException e) {
                log.error("read script file {} error", name);
            } finally {
                IOUtils.closeQuietly(is);
            }
        }
        return script;
    }
}
