/**
 * 
 */
package com.netfinworks.basis.inf.ucs.support.annotation.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import net.rubyeye.xmemcached.MemcachedClient;

import com.netfinworks.basis.inf.ucs.client.CacheListener;
import com.netfinworks.basis.inf.ucs.enhanced.EUCache;
import com.netfinworks.basis.inf.ucs.enhanced.support.UCacheSwitcher;
import com.netfinworks.basis.inf.ucs.local.LocalUCache;
import com.netfinworks.basis.inf.ucs.memcached.XUCache;
import com.netfinworks.basis.inf.ucs.memcached.ns.TTNamingService;

/**
 * @author bigknife
 *
 */
public class EUCacheBuilder<T> implements ApplicationContextAware {
	private String name;
	private String nameServerAddress;
	private int localCacheMaxSize;
	private int memcachedConnectionPoolSize = MemcachedClient.DEFAULT_CONNECTION_POOL_SIZE;
	private long opTimeoutMs = 1000L;//1秒
	private long connectTimeoutMs = 60000L;//1分钟
	private String listeners;
	private Logger logger = LoggerFactory.getLogger(getClass());
	private ApplicationContext applicationContext;
	/**
	 * @param listeners the listeners to set
	 */
	public void setListeners(String listeners) {
		this.listeners = listeners;
	}

	/**
	 * @param opTimeoutMs the opTimeoutMs to set
	 */
	public void setOpTimeoutMs(long opTimeoutMs) {
		this.opTimeoutMs = opTimeoutMs;
	}

	/**
	 * @param connectTimeoutMs the connectTimeoutMs to set
	 */
	public void setConnectTimeoutMs(long connectTimeoutMs) {
		this.connectTimeoutMs = connectTimeoutMs;
	}

	/**
	 * @param memcachedConnectionPoolSize the memcachedConnectionPoolSize to set
	 */
	public void setMemcachedConnectionPoolSize(int memcachedConnectionPoolSize) {
		this.memcachedConnectionPoolSize = memcachedConnectionPoolSize;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}



	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}



	/**
	 * @return the nameServerAddress
	 */
	public String getNameServerAddress() {
		return nameServerAddress;
	}



	/**
	 * @param nameServerAddress the nameServerAddress to set
	 */
	public void setNameServerAddress(String nameServerAddress) {
		this.nameServerAddress = nameServerAddress;
	}



	/**
	 * @return the localCacheMaxSize
	 */
	public int getLocalCacheMaxSize() {
		return localCacheMaxSize;
	}



	/**
	 * @param localCacheMaxSize the localCacheMaxSize to set
	 */
	public void setLocalCacheMaxSize(int localCacheMaxSize) {
		this.localCacheMaxSize = localCacheMaxSize;
	}



	@SuppressWarnings("unchecked")
	public EUCache<T> build(){
		EUCache<T> euCache = new EUCache<T>();
		UCacheSwitcher<T> ucacheSwitcher = new UCacheSwitcher<T>();
		euCache.setUcacheSwitcher(ucacheSwitcher);
		
		LocalUCache<T> luc = new LocalUCache<T>();
		luc.setMaxSize(localCacheMaxSize);
		ucacheSwitcher.setLocalUCache(luc);
		
		XUCache<T> xuc = new XUCache<T>();
		xuc.setConnectTimeout(1000);
		xuc.setName(name);
		xuc.setConnectionPoolSize(memcachedConnectionPoolSize);
		xuc.setOpTimeout(opTimeoutMs);
		xuc.setConnectTimeout(connectTimeoutMs);
		
		try {
			if(listeners != null && listeners.trim().length() > 0){
				String [] listenerArray = listeners.split("[,]");
				for(String listener : listenerArray){
					CacheListener<T> lsn = applicationContext.getBean(listener.trim(), CacheListener.class);
					xuc.addListener(lsn);
				}
			}
		} catch (Exception e) {
			logger.error("初始化XUCache 监听器异常",e);
			throw new RuntimeException(e);
		} 
		
		TTNamingService namingService = new TTNamingService();
		namingService.setTtServerAddress(nameServerAddress);
		xuc.setNamingService(namingService);
		ucacheSwitcher.setXuCache(xuc);
		
		euCache.init();
		return euCache;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
}
