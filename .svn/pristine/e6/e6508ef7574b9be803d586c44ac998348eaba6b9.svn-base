/**
 * 
 */
package com.netfinworks.basis.inf.ucs.support.annotation;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bigknife
 *
 */
public class InvalidateCacheTest extends AOPTest {
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Test
	public void test() throws InterruptedException{
		ITestService ts = (ITestService) getBean("testService");
		String v1 = ts.heavyGet("key1","1");
		String v2 = ts.heavyGet("key2","1");
		
		Assert.assertEquals(v1, "hello,world1");
		Assert.assertEquals(v2, "hello,world2");
		
		v1 = ts.heavyGet("key1","2");
		v2 = ts.heavyGet("key2","2");
		
		Assert.assertEquals(v1, "hello,world1");
		Assert.assertEquals(v2, "hello,world2");
		
		v1 = ts.heavyGet("key1","2");
		v2 = ts.heavyGet("key2","2");
		Assert.assertNotSame(v1,v2);
		
		ts.reHeavyGet("key1");
		log.info("has reHeavyGet");
		v1 = ts.heavyGet("key1","1");
		log.info("get key1 completes");
		v2 = ts.heavyGet("key2","1");
		log.info("get key2 completes");
		Assert.assertEquals(v1, "hello,world1");
		Assert.assertEquals(v2, "hello,world2");
		
		
	}
}
