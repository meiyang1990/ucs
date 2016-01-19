/**
 * 
 */
package com.netfinworks.basis.inf.ucs.enhanced;

import com.netfinworks.basis.inf.ucs.client.CacheEventType;
import com.netfinworks.basis.inf.ucs.client.CacheListener;
import com.netfinworks.basis.inf.ucs.client.CacheRespone;
import com.netfinworks.basis.inf.ucs.client.UCache;
import com.netfinworks.basis.inf.ucs.enhanced.support.IUCacheSwitcher;
import com.netfinworks.basis.inf.ucs.local.LocalUCache;
import com.netfinworks.basis.inf.ucs.memcached.XUCache;

/**
 * 增强型UCache，当连接memcached连接不上的时候，使用本地缓存，避免雪崩效应，一旦memcached缓存可用，则切换到memcached
 * 
 * @author bigknife
 * 
 */
public class EUCache<T> implements UCache<T> {
	private IUCacheSwitcher<T> ucacheSwitcher;

	public XUCache<T> getXUCache() {
		return ucacheSwitcher.getXUCache();
	}

	public LocalUCache<T> getLocalUCache() {
		return ucacheSwitcher.getLocalUCache();
	}

	/**
	 * @param ucacheSwitcher
	 *            the ucacheSwitcher to set
	 */
	public void setUcacheSwitcher(IUCacheSwitcher<T> ucacheSwitcher) {
		this.ucacheSwitcher = ucacheSwitcher;
	}

	public boolean init() {
		boolean ret = ucacheSwitcher.init();
		// 添加一个默认的监听器，处理档get正常时，返回了空值，要删除本地的缓存，防止用了本地缓存老的数据
		CacheListener<T> listener = new CacheListener<T>() {
			@Override
			public void onErroEvent(Event<T> event) throws Exception {
				CacheEventType type = event.getType();
				// 如果缓存返回空，并且memcached没有出错，则删除本地缓存
				if (CacheEventType.GET_OK.equals(type)
						&& event.getValue() == null) {
					ucacheSwitcher.getLocalUCache().delete(event.getKey());
				}
			}

		};
		ucacheSwitcher.getXUCache().addListener(listener);
		return ret;
	}

	public CacheRespone<T> get(String key) {
		return ucacheSwitcher.switchUCache().get(key);
	}

	@Override
	public boolean add(String key, T t, int expireSecond) {
		return ucacheSwitcher.switchUCache().add(key, t, expireSecond);
	}

	public boolean set(String key, T t, int expireSecond) {
		return ucacheSwitcher.switchUCache().set(key, t, expireSecond);
	}

	public boolean touch(String key, int newExpireSecond) {
		return ucacheSwitcher.switchUCache().touch(key, newExpireSecond);
	}

	public CacheRespone<T> getAndTouch(String key, int newExpireSecond) {
		return ucacheSwitcher.switchUCache().getAndTouch(key, newExpireSecond);
	}

	public boolean delete(String key) {
		return ucacheSwitcher.switchUCache().delete(key);
	}

	public void flush() {
		ucacheSwitcher.switchUCache().flush();
	}

	public boolean destroy() {
		return ucacheSwitcher.destroy();
	}

	@Override
	public boolean replace(String key, T t, int expireSecond) {
		return ucacheSwitcher.switchUCache().replace(key, t, expireSecond);
	}

	@Override
	public CacheRespone<Long> incr(String key, long delta) {
		return ucacheSwitcher.switchUCache().incr(key, delta);
	}

	@Override
	public CacheRespone<Long> decr(String key, long delta) {
		return ucacheSwitcher.switchUCache().decr(key, delta);
	}
}
