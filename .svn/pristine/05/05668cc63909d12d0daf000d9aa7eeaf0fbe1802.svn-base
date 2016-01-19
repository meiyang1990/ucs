/**
 * 
 */
package com.netfinworks.basis.inf.ucs.memcached.ns;

import org.junit.Test;

/**
 * @author bigknife
 *
 */
public class TTNamingServiceTest {
	@Test
	public void testResolve(){
		String name = "index.com.netfinworks.inf.ucs.test";
		TTNamingService tts = new TTNamingService();
		tts.setTtServerAddress("tcp://10.65.178.11:1978");
		String serverInfo = tts.resove(name);
		System.out.println(serverInfo);
	}
}
