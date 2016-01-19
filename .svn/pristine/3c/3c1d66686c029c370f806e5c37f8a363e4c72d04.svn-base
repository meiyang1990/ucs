/**
 * 
 */
package com.netfinworks.basis.inf.ucs.memcached;

import com.netfinworks.basis.inf.ucs.client.UCache;
import com.netfinworks.basis.inf.ucs.memcached.ns.NamingService;

/**
 * @author bigknife
 *
 */
public abstract class BaseMemcachedClient<T> implements UCache<T> {
	private String name;
	private NamingService namingService;
	
	public static final String PROTOCOL_TEXT = "text";
	public static final String PROTOCOL_BIN = "bin";
	
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param namingService the namingService to set
	 */
	public void setNamingService(NamingService namingService) {
		this.namingService = namingService;
	}

	public String getMemcachedNodeAddress(){
		assertNotNull(namingService, "namingService must not be null!");
		assertNotNull(name, "name must not be null!");
		
		return namingService.resove(name);
	}
	
	protected void assertNotNull(Object obj, String msg){
		if(obj == null){
			throw new IllegalArgumentException(msg);
		}
	}

}
