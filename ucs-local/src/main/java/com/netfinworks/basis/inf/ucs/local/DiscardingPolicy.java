/**
 * 
 */
package com.netfinworks.basis.inf.ucs.local;

import java.util.Map;

import com.netfinworks.basis.inf.ucs.local.support.CachedItem;

/**
 * CachedItem 丢弃策略
 * @author bigknife
 *
 */
public interface DiscardingPolicy<T> {
	String discard(Map<String, CachedItem<T>> map);
}
