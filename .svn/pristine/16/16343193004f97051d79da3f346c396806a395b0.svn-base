/**
 * 
 */
package com.netfinworks.basis.inf.ucs.support.annotation;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author bigknife
 *
 */
public class CacheResultTest extends AOPTest {
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
		
		Thread.sleep(10000);
		v1 = ts.heavyGet("key1","2");
		v2 = ts.heavyGet("key2","2");
		Assert.assertNotSame(v1,v2);
	}
}
