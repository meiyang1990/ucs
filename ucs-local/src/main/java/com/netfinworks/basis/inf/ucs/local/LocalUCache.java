/**
 * 
 */
package com.netfinworks.basis.inf.ucs.local;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netfinworks.basis.inf.ucs.client.CacheRespone;
import com.netfinworks.basis.inf.ucs.client.UCache;
import com.netfinworks.basis.inf.ucs.local.discardpolicy.LRUDiscardPolicy;
import com.netfinworks.basis.inf.ucs.local.support.CachedItem;

/**
 * @author bigknife
 * 
 */
public class LocalUCache<T> implements UCache<T> {
	private Map<String, CachedItem<T>> map = new ConcurrentHashMap<String, CachedItem<T>>();
	private int maxSize = 10000;
	private DiscardingPolicy<T> discardingPolicy = new LRUDiscardPolicy<T>();
	private Logger logger = LoggerFactory.getLogger(getClass());

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public void setDiscardingPolicy(DiscardingPolicy<T> discardingPolicy) {
		this.discardingPolicy = discardingPolicy;
	}

	public CacheRespone<T> get(String key) {
		CachedItem<T> _item = map.get(key);
		if (_item != null && !_item.isExpired()) {
			_item.getHint().addAndGet(1);
			return new CacheRespone<T>(_item.getItem());
		}
		if (_item != null && _item.isExpired()) {
			map.remove(key);
		}
		return new CacheRespone<T>(null);
	}

	public void updateValue(String key, T t) {
		CachedItem<T> item = map.get(key);
		if (item != null) {
			item.setItem(t);
		}
	}

	@Override
	public boolean add(String key, T t, int expireSecond) {
		if (!map.containsKey(key)) {
			return set(key, t, expireSecond);
		}
		return false;
	}

	public boolean set(String key, T t, int expireSecond) {
		int _expireSecond = correctExpireSecond(expireSecond);
		CachedItem<T> _item = map.get(key);
		if (_item != null) {
			synchronized (_item) {
				_item.setItem(t);
				_item.setUpdateTime(System.nanoTime());
				_item.setExpireSecond(_expireSecond);
			}
			return true;
		}

		if (map.size() < maxSize) {
			_item = new CachedItem<T>(key, t, _expireSecond);
			map.put(key, _item);
			logger.debug("cached:[key={},value={}]", key, _item.getItem());
			return true;
		} else {
			// 尝试找是否有过期的对象，如果有直接丢弃过期的
			for (String _key : map.keySet()) {
				_item = map.get(_key);
				if (_item.isExpired()) {
					map.remove(_key);
					logger.debug("remove expired item : [key={},value={}]",
							_key, _item.getItem());
					_item = new CachedItem<T>(key, t, _expireSecond);
					map.put(key, _item);
					logger.debug("cached:[key={},value={}]", key,
							_item.getItem());
					return true;
				}
			}
			// 超出最大数量,根据丢弃策略,丢弃一个对象，然后set一个新对象
			if (discardingPolicy != null) {
				String _key = discardingPolicy.discard(map);
				_item = map.get(_key);
				if (_item != null) {
					logger.debug(
							"discard cached item : [ke={},value={}], by discarding policy:{}",
							new Object[] { _key, _item.getItem().toString(),
									discardingPolicy.toString() });
					map.remove(_key);
					_item = new CachedItem<T>(key, t, _expireSecond);
					map.put(key, _item);
					logger.debug("cached:[key={},value={}]", key,
							_item.getItem());
					return true;
				}
			}

		}
		return false;
	}

	public boolean touch(String key, int newExpireSecond) {
		int _newExpireSecond = correctExpireSecond(newExpireSecond);
		CachedItem<T> item = map.get(key);
		if (item != null) {
			item.setExpireSecond(_newExpireSecond);
			return true;
		}
		return false;
	}

	public CacheRespone<T> getAndTouch(String key, int newExpireSecond) {
		if (touch(key, newExpireSecond)) {
			return get(key);
		}
		return new CacheRespone<T>(null);
	}

	public boolean delete(String key) {
		if (map.containsKey(key)) {
			map.remove(key);
			return true;
		}
		return false;
	}

	public void flush() {
		if (map != null) {
			map.clear();
		}
	}

	public boolean init() {
		return true;
	}

	public boolean destroy() {
		map.clear();
		return true;
	}

	private int correctExpireSecond(int expireSecond) {
		return expireSecond <= 0 ? Integer.MAX_VALUE : expireSecond;
	}

	@Override
	public boolean replace(String key, T t, int expireSecond) {
		synchronized (this) {
			if (get(key) != null) {
				set(key, t, expireSecond);
				return true;
			}
		}
		return false;
	}

	@Override
	public CacheRespone<Long> incr(String key, long delta) {
		logger.warn("Local cache not support the operation.");
		return CacheRespone.failResult();
	}

	@Override
	public CacheRespone<Long> decr(String key, long delta) {
		logger.warn("Local cache not support the operation.");
		return CacheRespone.failResult();
	}
}
