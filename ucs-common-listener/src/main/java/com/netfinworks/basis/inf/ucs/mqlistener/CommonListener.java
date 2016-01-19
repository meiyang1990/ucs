/**
 * 
 */
package com.netfinworks.basis.inf.ucs.mqlistener;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.netfinworks.basis.inf.ucs.mqlistener.util.ConfigParser;
import com.netfinworks.basis.inf.ucs.mqlistener.util.MNSUtil;
import com.netfinworks.mq.handler.notify.AbstractNotifyMessageHandler;

/**
 * @author bigknife
 * 
 */
public class CommonListener extends AbstractNotifyMessageHandler {
	private MNSUtil mnsUtil;
	private String alertConfig;
	private ConfigParser configParser;
	private Map<String,Map<String,String>> configs;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * @param configParser the configParser to set
	 */
	public void setConfigParser(ConfigParser configParser) {
		this.configParser = configParser;
	}

	/**
	 * @param mnsUtil
	 *            the mnsUtil to set
	 */
	public void setMnsUtil(MNSUtil mnsUtil) {
		this.mnsUtil = mnsUtil;
	}
	
	/**
	 * @param alertConfig the alertConfig to set
	 */
	public void setAlertConfig(String alertConfig) {
		this.alertConfig = alertConfig;
	}
	
	public void init(){
		configs = configParser.parse(alertConfig);
	}



	@SuppressWarnings("unchecked")
	@Override
	public void handleMessage(Object msg) throws Exception {
		HashMap<String, String> map = (HashMap<String, String>) msg;
		if(isIgnored(map)){
			logger.warn("ignore warning, no email sended for this msg: {}", new Object[]{msg});
			return ;
		}
		//buildMsg
		String emails = buildEmails(map);
		if(emails != null){
			String emailMsg = buildMsg(map);
			mnsUtil.sendEmail(emails, emailMsg);
		}
		
		
		System.out.println(map);
	}
	
	private boolean isIgnored(HashMap<String, String> map){
		String indexName = map.get("indexName");
		for(Entry<String,Map<String,String>> entry : configs.entrySet()){
			Map<String, String> value = entry.getValue();
			if( indexName.equals(value.get("indexName"))){
				String alertType = value.get("alertType");
				String type = map.get("type");
				return (StringUtils.hasText(alertType) && alertType.indexOf(type) < 0);
			}
		}
		return false;
	}
	
	private String buildEmails(HashMap<String, String> map) {
		String indexName = map.get("indexName");
		for(Entry<String,Map<String,String>> entry : configs.entrySet()){
			Map<String, String> value = entry.getValue();
			if( indexName.equals(value.get("indexName"))){
				return value.get("receiver");
			}
		}
		return null;
	}

	private String buildMsg(HashMap<String, String> map){
		String description = map.get("description");
		String nameServerAddress = map.get("nameServerAddress");
		String indexName = map.get("indexName");
		String memcachedNodes = map.get("memcachedNodes");
		String key = map.get("key");
//		String value = map.get("key");
		String type = map.get("type");
		
		StringBuffer buf = new StringBuffer();
		buf.append("<ul>UCS Memcached Warning:</ul>");
		buf.append("<li>description:").append(description).append("</li>");
		buf.append("<li>indexName:").append(indexName).append("</li>");
		buf.append("<li>nameServerAddress:").append(nameServerAddress).append("</li>");
		buf.append("<li>memcachedNodes:").append(memcachedNodes).append("</li>");
		buf.append("<li>key:").append(key).append("</li>");
//		buf.append("<li>value:").append(value).append("</li>");
		buf.append("<li>type:").append(type).append("</li></ul>");
		
		return buf.toString();
	}
}
