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
public class CacheResult10sTest extends AOPTest {
	@Test
	public void test() throws InterruptedException{
		ITestService ts = (ITestService) getBean("testService");
		String v9 = ts.heavyGetCache10s("key9","1");
		
		Thread.sleep(8000);
		v9 = ts.heavyGetCache10s("key9","1");
		Assert.assertEquals(v9, "hello,world9");
		
		Thread.sleep(1000);
		v9 = ts.heavyGetCache10s("key9","1");
		Assert.assertEquals(v9, "hello,world9");
		
		
		Thread.sleep(12000);
		v9 = ts.heavyGetCache10s("key9","1");
		Assert.assertEquals(v9, "hello,world9");
		
		KeyGen kg = new KeyGen();
		kg.setKey1("key3");
		kg.setKey2("key4");
		ts.reHeavyGet(kg);
		
	}
}
