/**
 * 
 */
package com.netfinworks.basis.inf.ucs.support.annotation;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bigknife
 *
 */
public class InvalidateMultiCacheTest extends AOPTest {
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Test
	public void test() throws InterruptedException{
		ITestService ts = (ITestService) getBean("testService");
		String v1 = ts.heavyGet("key1","1");
		String v2 = ts.heavyGet("key2","1");
		String v3 = ts.heavyGet("key3","1");
		String v4 = ts.heavyGet("key4","1");
		String v5 = ts.heavyGet("key5","1");
		String v6 = ts.heavyGet("key6","1");
		
		log.info("v1,v2,v3,v4,v5,v6 has cached........");
		
		List<String> list = new ArrayList<String>();
		list.add("key1");
		list.add("key3");
		list.add("key5");
		ts.reHeavyGet(list);
		log.info("v1,v3,v5 cache invalidated");
		
		v1 = ts.heavyGet("key1","1");
		log.info("v1 getted");
		v2 = ts.heavyGet("key2","1");
		log.info("v2 getted");
		v3 = ts.heavyGet("key3","1");
		log.info("v3 getted");
		v4 = ts.heavyGet("key4","1");
		log.info("v4 getted");
		v5 = ts.heavyGet("key5","1");
		log.info("v5 getted");
		v6 = ts.heavyGet("key6","1");
		log.info("v6 getted");
		
		
		
	}
}
