/**
 * 
 */
package com.netfinworks.basis.inf.ucs.enhanced.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netfinworks.basis.inf.ucs.client.CacheRespone;
import com.netfinworks.basis.inf.ucs.client.UCache;
import com.netfinworks.basis.inf.ucs.local.LocalUCache;
import com.netfinworks.basis.inf.ucs.memcached.XUCache;

/**
 * @author bigknife
 * 
 */
public class UCacheSwitcher<T> implements IUCacheSwitcher<T> {
	private LocalUCache<T> localUCache;
	private XUCache<T> xuCache;

	private Logger log = LoggerFactory.getLogger(getClass());

	class UCacheProxy implements UCache<T> {

		public boolean init() {
			boolean b1 = localUCache.init();
			boolean b2 = xuCache.init();
			return b1 && b2;
		}

		public CacheRespone<T> get(String key) {
			synchronized (key) {
				CacheRespone<T> t = getProperUCache().get(key);
				if (log.isDebugEnabled()) {
					log.debug("getCache from memcached,return: " + t);
				}
				if (t.isSuccess()) {
					if (t.get() != null) {
						// 如果memcache中有，则更新本地缓存
						localUCache.updateValue(key, t.get());
					} else {
						localUCache.delete(key);
					}
				} else {
					t = localUCache.get(key);
					if (log.isDebugEnabled()) {
						log.debug("getCache from localUCache,return: " + t);
					}
				}
				return t;
			}
		}

		@Override
		public boolean add(String key, T t, int expireSecond) {
			boolean ret = getProperUCache().add(key, t, expireSecond);
			if (log.isDebugEnabled()) {
				log.debug("addCache to memcached, key = " + key + ", t = " + t
						+ " , exprireSecond = " + expireSecond + "; return "
						+ ret);
			}
			if (ret) {
				// 如果set成功，因为memcached可用，本地无需缓存，就算缓存了，在memcached不可用时也是不可信的
				localUCache.delete(key);
			} else {
				ret = localUCache.add(key, t, expireSecond);
				if (log.isDebugEnabled()) {
					log.debug("addCache to localUCache, key = " + key
							+ ", t = " + t + " , exprireSecond = "
							+ expireSecond + "; return " + ret);
				}
			}
			return ret;
		}

		public boolean set(String key, T t, int expireSecond) {
			boolean ret = getProperUCache().set(key, t, expireSecond);
			if (log.isDebugEnabled()) {
				log.debug("setCache to memcached, key = " + key + ", t = " + t
						+ " , exprireSecond = " + expireSecond + "; return "
						+ ret);
			}
			if (ret) {
				// 如果set成功，因为memcached可用，本地无需缓存，就算缓存了，在memcached不可用时也是不可信的
				localUCache.delete(key);
			} else {
				ret = localUCache.set(key, t, expireSecond);
				if (log.isDebugEnabled()) {
					log.debug("setCache to localUCache, key = " + key
							+ ", t = " + t + " , exprireSecond = "
							+ expireSecond + "; return " + ret);
				}
			}
			return ret;
		}

		public boolean touch(String key, int newExpireSecond) {
			boolean ret = getProperUCache().touch(key, newExpireSecond);
			if (!ret) {
				ret = localUCache.touch(key, newExpireSecond);
			} else {
				// 原因同set成功后本地缓存的处理
				localUCache.delete(key);
			}
			return ret;
		}

		public CacheRespone<T> getAndTouch(String key, int newExpireSecond) {
			CacheRespone<T> t = getProperUCache().getAndTouch(key,
					newExpireSecond);
			if (t.isSuccess()) {
				if (t.get() != null) {
					t = localUCache.getAndTouch(key, newExpireSecond);
				} else {
					localUCache.delete(key);
				}
			} else {
				// 原因同set成功后本地缓存的处理
				localUCache.delete(key);
			}
			return t;
		}

		public boolean delete(String key) {
			boolean ret = getProperUCache().delete(key);
			if (log.isDebugEnabled()) {
				log.debug("deleteCache from memcached, key = " + key
						+ "; return " + ret);
			}
			// 无论如何本地一起删除
			ret = localUCache.delete(key);
			return ret;
		}

		public void flush() {
			xuCache.flush();
			localUCache.flush();
		}

		public boolean destroy() {
			boolean b1 = xuCache.destroy();
			boolean b2 = localUCache.destroy();
			return b1 && b2;
		}

		@Override
		public boolean replace(String key, T t, int expireSecond) {
			boolean b1 = xuCache.replace(key, t, expireSecond);
			boolean b2 = localUCache.replace(key, t, expireSecond);
			return b1 && b2;
		}

		@Override
		public CacheRespone<Long> incr(String key, long delta) {
			// 不支持本地
			return xuCache.incr(key, delta);
		}

		@Override
		public CacheRespone<Long> decr(String key, long delta) {
			// 不支持本地
			return xuCache.decr(key, delta);
		}
	}

	private UCacheProxy instance = new UCacheProxy();

	/**
	 * @param localUCache
	 *            the localUCache to set
	 */
	public void setLocalUCache(LocalUCache<T> localUCache) {
		this.localUCache = localUCache;
	}

	/**
	 * @param xuCache
	 *            the xuCache to set
	 */
	public void setXuCache(XUCache<T> xuCache) {
		this.xuCache = xuCache;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.netfinworks.basis.inf.ucs.enhanced.support.IUCacheSwitcher#switchUCache
	 * ()
	 */
	public UCache<T> switchUCache() {
		return instance;
	}

	public boolean init() {
		return instance.init();
	}

	public boolean destroy() {
		return instance.destroy();
	}

	private boolean memcachedOk = true;

	private UCache<T> getProperUCache() {
		return memcachedOk ? xuCache : localUCache;
	}

	@Override
	public XUCache<T> getXUCache() {
		return xuCache;
	}

	@Override
	public LocalUCache<T> getLocalUCache() {
		return localUCache;
	}

}
