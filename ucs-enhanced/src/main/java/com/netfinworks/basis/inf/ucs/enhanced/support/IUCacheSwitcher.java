package com.netfinworks.basis.inf.ucs.enhanced.support;

import com.netfinworks.basis.inf.ucs.client.UCache;
import com.netfinworks.basis.inf.ucs.local.LocalUCache;
import com.netfinworks.basis.inf.ucs.memcached.XUCache;

/**
 * ucache 实例切换
 * @author bigknife
 *
 * @param <T>
 */
public interface IUCacheSwitcher<T> {
	public XUCache<T> getXUCache();
	public LocalUCache<T> getLocalUCache();
	public abstract UCache<T> switchUCache();
	public abstract boolean init();
	public abstract boolean destroy();

}
