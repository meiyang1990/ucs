package com.netfinworks.basis.inf.ucs.enhanced;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.netfinworks.basis.inf.ucs.enhanced.support.UCacheSwitcher;
import com.netfinworks.basis.inf.ucs.local.LocalUCache;
import com.netfinworks.basis.inf.ucs.memcached.XUCache;
import com.netfinworks.basis.inf.ucs.memcached.ns.TTNamingService;

/**
 * @author bigknife
 *
 */
public class EUCachedTest {
	private EUCache<Object> euCache;
	
	@Before
	public void init(){
		euCache = new EUCache<Object>();
		UCacheSwitcher<Object> ucacheSwitcher = new UCacheSwitcher<Object>();
		euCache.setUcacheSwitcher(ucacheSwitcher);
		
		LocalUCache<Object> luc = new LocalUCache<Object>();
		luc.setMaxSize(10000);
		ucacheSwitcher.setLocalUCache(luc);
		
		XUCache<Object> xuc = new XUCache<Object>();
		xuc.setConnectTimeout(1000);
		xuc.setName("index.com.netfinworks.inf.ucs.test");
		TTNamingService namingService = new TTNamingService();
		namingService.setTtServerAddress("tcp://10.65.178.11:1978");
		xuc.setNamingService(namingService);
		ucacheSwitcher.setXuCache(xuc);
		
		euCache.init();
	}
	@After
	public void destroy(){
		euCache.destroy();
	}
	
	@Test
	public void test() throws Exception{
		for(int i = 0; i < 10; i ++){
			euCache.set("test.eucache", "hello,world", 2);
			Assert.assertEquals(euCache.get("test.eucache"), "hello,world");
			Thread.sleep(2000);
			Assert.assertNull(euCache.get("test.eucache"));
		}
	}
}
