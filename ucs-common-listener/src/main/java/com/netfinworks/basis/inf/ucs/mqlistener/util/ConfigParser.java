/**
 * 
 */
package com.netfinworks.basis.inf.ucs.mqlistener.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * prefix.name.attrName 格式的配置解析
 * @author bigknife
 *
 */
public class ConfigParser {
	private String prefix;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Map<String, Map<String, String>> parse(String properties){
		Properties p = new Properties();
		try {
			p.load(new FileInputStream(properties));
			//find all name
			Map<String, Map<String, String>> retMap = new HashMap<String, Map<String,String>>();
			
			for(Entry<Object, Object> entry : p.entrySet()){
				String key = (String) entry.getKey();
				if(key.startsWith(prefix)){
					String names = key.substring(prefix.length() + 1);
					String [] nameArr = names.split("[.]");
					if(nameArr.length == 2){
						Map<String,String> map = retMap.get(nameArr[0]);
						if(map == null){
							map = new HashMap<String, String>();
							retMap.put(nameArr[0], map);
						}
						map.put(nameArr[1], (String) entry.getValue());
					}
				}
			}
			logger.info("加载配置信息：{}",retMap);
			return retMap;
		} catch (FileNotFoundException e) {
			logger.error("配置属性文件不存在",e);
		} catch (IOException e) {
			logger.error("配置属性文件IO异常",e);
		}
		return null;
	}
}
