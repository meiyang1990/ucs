/**
 * 
 */
package com.netfinworks.basis.inf.ucs.support.annotation;

import java.util.List;

/**
 * @author bigknife
 *
 */
public interface ITestService {
	String heavyGet(String key , String key2);
	String heavyGetCache10s(String key , String key2);
	void reHeavyGet(String key);
	void reHeavyGet(List<String> keys);
	
	String reHeavyGet(KeyGen kg);
}
