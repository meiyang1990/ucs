/**
 * 
 */
package com.netfinworks.basis.inf.ucs.support.listener;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.netfinworks.basis.inf.ucs.client.CacheEventType;
import com.netfinworks.basis.inf.ucs.client.CacheListener;
import com.netfinworks.basis.inf.ucs.enhanced.EUCache;
import com.netfinworks.basis.inf.ucs.memcached.XUCache;
import com.netfinworks.basis.inf.ucs.support.annotation.advice.EUCacheBuilder;
import com.netfinworks.mq.constant.MessageFormat;
import com.netfinworks.mq.core.MQException;
import com.netfinworks.mq.jms.impl.JmsService;
import com.netfinworks.mq.request.notify.DefaultNotifyRequest;
import com.netfinworks.mq.request.notify.DefaultTransacatedNotifyRequest;

/**
 * @author bigknife
 * 
 */
public class CommonCacheListener<T> implements CacheListener<T>,
		ApplicationContextAware {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private ApplicationContext applicationContext;
	private static final String euCacheBeanName = "ucs.support.annotation.euCache";
	private static final String cacheBuilderBeanName = "ucs.support.annotation.cacheBuilder";
	private String description;
	private String queueName;
	private JmsService mqService;

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param queueName
	 *            the queueName to set
	 */
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	/**
	 * @param mqService
	 *            the mqService to set
	 */
	public void setMqService(JmsService mqService) {
		this.mqService = mqService;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onErroEvent(Event<T> event) throws Exception {
		CacheEventType type = event.getType();
		EUCache<T> euCache = (EUCache<T>) applicationContext
				.getBean(euCacheBeanName);
		XUCache<T> cache = euCache.getXUCache();
		String key = event.getKey();
		switch (type) {
		case DEL_EXCEPTION:
		case DEL_FAIL:
		case REPLACE_EXCEPTION:
		case REPLACE_FAIL:
		case SET_FAIL:
		case SET_EXCEPTION:
			handleError(cache,key,event,type);
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void handleError(XUCache<T> cache,String key, Event<T> event,CacheEventType type) throws MQException {
		// 如果缓存的delete replace
		// set操作出错，尝试该key，确保缓存数据要么是一致的，要么是空的，如果删除失败，则发警告MQ
		boolean ret = false;
		try {
			ret = cache.getMemcachedClient().delete(key);
		} catch (Exception ex) {
			logger.error("listener 再次删除缓存异常", ex);
			ret = false;
		}
		if (!ret) {
			// 如果再一次删除失败，通知另外的mq listener
			logger.warn("尝试删除，失败！memcacheNodes = {},key = {}", new Object[] {
					cache.getMemcachedNodeAddress(), event.getKey() });

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("memcachedNodes", cache.getMemcachedNodeAddress());
			map.put("key", event.getKey());
			// map.put("value", event.getValue());
			map.put("description", description);
			map.put("type", type.toString());

			EUCacheBuilder<T> builder = (EUCacheBuilder<T>) applicationContext
					.getBean(cacheBuilderBeanName);
			map.put("indexName", builder.getName());
			map.put("nameServerAddress", builder.getNameServerAddress());

			sendMq(map);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	private void sendMq(Map<String, Object> map) throws MQException {
		DefaultNotifyRequest<Map<String, Object>> notifyRequest = new DefaultTransacatedNotifyRequest<Map<String, Object>>();
		notifyRequest.setDestination(queueName);
		notifyRequest.setMessageFormat(MessageFormat.OBJECT);
		notifyRequest.setContent(map);

		mqService.sendMessage(notifyRequest);
	}

}
