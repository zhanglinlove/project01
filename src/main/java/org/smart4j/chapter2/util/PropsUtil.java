package org.smart4j.chapter2.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 属性文件工具类
 * @author zhanglin
 *
 * @date 2018年8月26日
 */
public final class PropsUtil {

	private static final Logger logger = LoggerFactory.getLogger(PropsUtil.class);
	
	public static Properties loadProperties(String fileName) {
		Properties p = null;
		InputStream input = null;
		input = Thread.currentThread().getContextClassLoader().getResourceAsStream("/" + fileName);
		p = new Properties();
		try {
			p.load(input);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return p;
	}
	
	public static String getString(Properties p, String key) {
		
		return getString(p, key, "");
	}
	
	public static String getString(Properties p, String key, String defaultValue) {
		String value = p.getProperty(key);
		if (value == null)
			return defaultValue;
		return value;
	}
	
	public static int getInt(Properties p, String key) {
		return getInt(p, key, 0);
	}
	
	public static int getInt(Properties p, String key, int defaultValue) {
		Object obj = p.get(key);
		if (obj == null || !(obj instanceof Integer))
			return defaultValue;
		return (int) obj;
	}
	
	public static boolean getBoolean(Properties p, String key) {
		
		return getBoolean(p, key, false);
	}
	
	public static boolean getBoolean(Properties p, String key, boolean defaultValue) {
		
		String value = p.getProperty(key);
		if (StringUtils.isEmpty(value)) {
			return defaultValue;
		}
		if ("false".equals(value))
			return false;
		if ("true".equals(value)) 
			return true;
		return defaultValue;
	}
}
