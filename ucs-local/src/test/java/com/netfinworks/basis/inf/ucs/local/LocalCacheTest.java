/**
 * 
 */
package com.netfinworks.basis.inf.ucs.local;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bigknife
 * 
 */
public class LocalCacheTest {
	private Logger log = LoggerFactory.getLogger(getClass());
	LocalUCache<Object> lc = new LocalUCache<Object>();
	
	@Before
	public void init(){
		lc.setMaxSize(100);
	}
	@Test
	public void testGet() throws Exception{
		lc.set("test", "hello,world", 2);
		String v = (String) lc.get("test").get();
		Assert.assertEquals("hello,world", v);
		log.info("sleep 2000");
		Thread.sleep(2000);
		v =  (String) lc.get("test").get();
		Assert.assertNull(v);
		
		lc.set("test", "hello,world", 2);
		Thread.sleep(1000);
		lc.touch("test", 5);
		Thread.sleep(3000);
		v = (String) lc.get("test").get();
		Assert.assertEquals("hello,world", v);
		Thread.sleep(2000);
		v =  (String) lc.get("test").get();
		Assert.assertNull(v);
		
		log.info("testGet ok");
	}
	
	//@Test
	public void test() {
		final LocalUCache<Object> lc = new LocalUCache<Object>();
		lc.setMaxSize(10);

		Thread t2 = new Thread(new Runnable() {

			public void run() {
				log.info("t2 start...");
				for (int i = 0; i < 20; i++) {
					Object o = new Object();

					try {
						Thread.sleep((long) (Math.random() * 3000));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					long start = System.nanoTime();
					lc.set("key" + i, o, 2);
					log.info("set 耗费时间：{} ns", System.nanoTime() - start);
				}
			}
		});
		t2.start();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Thread t1 = new Thread(new Runnable() {

			public void run() {
				log.info("t1 start...");
				for (;;) {
					for (int i = 0; i < 20; i++) {

						long start = System.nanoTime();
						Object o = lc.get("key" + i);
						log.info("get 耗费时间：{} ns, 从缓存中取得：[key={},value={}]",
								new Object[] { System.nanoTime() - start,
										"key" + i, o });
					}
					try {
						Thread.sleep((long) (Math.random() * 1000));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t1.start();

		try {
			Thread.sleep(Integer.MAX_VALUE);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
