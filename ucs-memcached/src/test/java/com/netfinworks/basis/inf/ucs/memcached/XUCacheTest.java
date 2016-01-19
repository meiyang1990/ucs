/**
 * 
 */
package com.netfinworks.basis.inf.ucs.memcached;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.netfinworks.basis.inf.ucs.client.UCache;
import com.netfinworks.basis.inf.ucs.memcached.ns.TTNamingService;

/**
 * @author bigknife
 * 
 */
public class XUCacheTest {
	private UCache<Object> ucache;

	@Before
	public void init() {
		XUCache<Object> c = new XUCache<Object>();
		c.setName("index.com.netfinworks.cache.cc");
		TTNamingService ns = new TTNamingService();
		ns.setTtServerAddress("tcp://10.65.178.11:1978");
		c.setNamingService(ns);
		c.init();
		ucache = c;

	}

	@After
	public void destroy() {
		ucache.destroy();
	}

	@Test
	public void test() throws Exception {
		String key = "test.me";
		String value = "hello,world";
		ucache.set(key, value, 2);
		String v = (String) ucache.get(key).get();
		Assert.assertEquals(value, v);

		Assert.assertEquals("缓存中已存在test.me", false, ucache.add(key, value, 2));

		Thread.sleep(2000);
		v = (String) ucache.get(key).get();
		Assert.assertNull(v);

		Assert.assertEquals("缓存中不存在test.me，add 成功", true,
				ucache.add(key, value, 2));
		v = (String) ucache.get(key).get();
		Assert.assertEquals(value, v);
		Assert.assertEquals("缓存中已存在test.me，add 失败", false,
				ucache.add(key, value, 2));

		Thread.sleep(2000);

		ucache.set(key, value, 2);
		ucache.set(key, value, 4);
		Thread.sleep(3000);
		v = (String) ucache.get(key).get();
		Assert.assertEquals(value, v);

		boolean b = ucache.delete(key);
		Assert.assertEquals(b, true);

		v = (String) ucache.get(key).get();
		Assert.assertNull(v);

	}

	@Test
	public void testArray() throws Exception {
		String key = "test.array";
		String[] value = new String[] { "1", "2", "3" };

		ucache.set(key, value, 2);
		value = (String[]) ucache.get(key).get();
		Assert.assertEquals(3, value.length);
		Assert.assertEquals("1", value[0]);
		Assert.assertEquals("2", value[1]);
		Assert.assertEquals("3", value[2]);

		Thread.sleep(2000);
		value = (String[]) ucache.get(key).get();
		Assert.assertNull(value);

	}

}
