/**
 * 
 */
package com.netfinworks.basis.inf.ucs.memcached;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.transcoders.Transcoder;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netfinworks.basis.inf.ucs.client.CacheEventType;
import com.netfinworks.basis.inf.ucs.client.CacheListener;
import com.netfinworks.basis.inf.ucs.client.CacheListener.Event;
import com.netfinworks.basis.inf.ucs.client.CacheRespone;

/**
 * @author bigknife
 * 
 */
public class XUCache<T> extends BaseMemcachedClient<T> {
	private MemcachedClient memcachedClient;

	private long connectTimeout = MemcachedClient.DEFAULT_CONNECT_TIMEOUT;
	private long opTimeout = MemcachedClient.DEFAULT_OP_TIMEOUT;
	private int connectionPoolSize = MemcachedClient.DEFAULT_CONNECTION_POOL_SIZE;

	private Transcoder<T> transcoder;

	private String protocol = PROTOCOL_TEXT;
	private Logger log = LoggerFactory.getLogger(getClass());

	private List<CacheListener<T>> listeners;

	public void addListener(CacheListener<T> listener) {
		if (listeners == null) {
			listeners = new ArrayList<CacheListener<T>>();
		}
		listeners.add(listener);
	}

	/**
	 * @return the memcachedClient
	 */
	public MemcachedClient getMemcachedClient() {
		return memcachedClient;
	}

	/**
	 * @param listeners
	 *            the listener to set
	 */
	public void setListener(List<CacheListener<T>> listeners) {
		this.listeners = listeners;
	}

	/**
	 * @param transcoder
	 *            the transcoder to set
	 */
	public void setTranscoder(Transcoder<T> transcoder) {
		this.transcoder = transcoder;
	}

	/**
	 * @param opTimeout
	 *            the opTimeout to set
	 */
	public void setOpTimeout(long opTimeout) {
		this.opTimeout = opTimeout;
	}

	/**
	 * @param protocol
	 *            the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @param connectTimeout
	 *            the connectTimeout to set
	 */
	public void setConnectTimeout(long connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	/**
	 * @param connectionPoolSize
	 *            the connectionPoolSize to set
	 */
	public void setConnectionPoolSize(int connectionPoolSize) {
		this.connectionPoolSize = connectionPoolSize;
	}

	public boolean init() {
		String address = getMemcachedNodeAddress();
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(
				AddrUtil.getAddresses(address));
		builder.setConnectionPoolSize(connectionPoolSize);
		builder.setConnectTimeout(connectTimeout);

		if (transcoder != null) {
			builder.setTranscoder(transcoder);
		}

		if (PROTOCOL_BIN.equals(this.protocol)) {
			builder.setCommandFactory(new BinaryCommandFactory());
		}
		try {
			memcachedClient = builder.build();
			memcachedClient.setOpTimeout(opTimeout);
			log.debug(
					"XUCache successfully initialized! memcached node info : {}",
					address);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.error("XUCache initializing failed! memcached node info : {}",
				address);
		return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netfinworks.basis.inf.ucs.client.UCache#get(java.lang.String)
	 */
	public CacheRespone<T> get(String key) {
		try {
			T t = memcachedClient.get(key);
			// 出发GET_OK事件
			fireEvent(new Event<T>(CacheEventType.GET_OK, key, t));
			return new CacheRespone<T>(t);
		} catch (TimeoutException e) {
			log.error("xmemcache timeout", e);
			fireEvent(new Event<T>(CacheEventType.GET_EXCEPTION, key, e));
		} catch (InterruptedException e) {
			log.error("xmemcache interrupted", e);
			fireEvent(new Event<T>(CacheEventType.GET_EXCEPTION, key, e));
		} catch (MemcachedException e) {
			log.error("xmemcache exception", e);
			fireEvent(new Event<T>(CacheEventType.GET_EXCEPTION, key, e));
		}
		return CacheRespone.failResult();
	}

	@Override
	public boolean add(String key, T t, int expireSecond) {
		try {
			boolean ret = memcachedClient.add(key, expireSecond, t);
			log.debug("add [key={},object={},exp={}] {}", new Object[] { key,
					t, expireSecond, ret ? "successfully" : "failed" });
			if (!ret) {
				fireEvent(new Event<T>(CacheEventType.ADD_FAIL, key, t));
			} else {
				fireEvent(new Event<T>(CacheEventType.ADD_OK, key, t));
			}
			return ret;
		} catch (TimeoutException e) {
			log.error("xmemcache timeout", e);
			fireEvent(new Event<T>(CacheEventType.ADD_EXCEPTION, key, t, e));
		} catch (InterruptedException e) {
			log.error("xmemcache interrupted", e);
			fireEvent(new Event<T>(CacheEventType.ADD_EXCEPTION, key, t, e));
		} catch (MemcachedException e) {
			log.error("xmemcache exception", e);
			fireEvent(new Event<T>(CacheEventType.ADD_EXCEPTION, key, t, e));
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netfinworks.basis.inf.ucs.client.UCache#set(java.lang.String,
	 * java.lang.Object, int)
	 */
	public boolean set(String key, T t, int expireSecond) {
		try {
			boolean ret = memcachedClient.set(key, expireSecond, t);
			log.debug("set [key={},object={},exp={}] {}", new Object[] { key,
					t, expireSecond, ret ? "successfully" : "failed" });
			if (!ret) {
				fireEvent(new Event<T>(CacheEventType.SET_FAIL, key, t));
			} else {
				fireEvent(new Event<T>(CacheEventType.SET_OK, key, t));
			}
			return ret;
		} catch (TimeoutException e) {
			log.error("xmemcache timeout", e);
			fireEvent(new Event<T>(CacheEventType.SET_EXCEPTION, key, t, e));
		} catch (InterruptedException e) {
			log.error("xmemcache interrupted", e);
			fireEvent(new Event<T>(CacheEventType.SET_EXCEPTION, key, t, e));
		} catch (MemcachedException e) {
			log.error("xmemcache exception", e);
			fireEvent(new Event<T>(CacheEventType.SET_EXCEPTION, key, t, e));
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netfinworks.basis.inf.ucs.client.UCache#touch(java.lang.String,
	 * int)
	 */
	public boolean touch(String key, int newExpireSecond) {
		try {
			boolean ret = memcachedClient.touch(key, newExpireSecond);
			fireEvent(new Event<T>(ret ? CacheEventType.TOUCH_OK
					: CacheEventType.TOUCH_FAIL, key));
			return ret;
		} catch (TimeoutException e) {
			log.error("xmemcache timeout", e);
			fireEvent(new Event<T>(CacheEventType.TOUCH_EXCEPTION, key, e));
		} catch (InterruptedException e) {
			log.error("xmemcache interrupted", e);
			fireEvent(new Event<T>(CacheEventType.TOUCH_EXCEPTION, key, e));
		} catch (MemcachedException e) {
			log.error("xmemcache exception", e);
			fireEvent(new Event<T>(CacheEventType.TOUCH_EXCEPTION, key, e));
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.netfinworks.basis.inf.ucs.client.UCache#getAndTouch(java.lang.String,
	 * int)
	 */
	public CacheRespone<T> getAndTouch(String key, int newExpireSecond) {
		try {
			T t = memcachedClient.getAndTouch(key, newExpireSecond);
			fireEvent(new Event<T>(CacheEventType.GAT_OK, key));
			return new CacheRespone<T>(t);
		} catch (TimeoutException e) {
			log.error("xmemcache timeout", e);
			fireEvent(new Event<T>(CacheEventType.GAT_EXCEPTION, key, e));
		} catch (InterruptedException e) {
			log.error("xmemcache interrupted", e);
			fireEvent(new Event<T>(CacheEventType.GAT_EXCEPTION, key, e));
		} catch (MemcachedException e) {
			log.error("xmemcache exception", e);
			fireEvent(new Event<T>(CacheEventType.GAT_EXCEPTION, key, e));
		}
		return CacheRespone.failResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netfinworks.basis.inf.ucs.client.UCache#delete(java.lang.String)
	 */
	public boolean delete(String key) {
		try {
			boolean ret = memcachedClient.delete(key);
			fireEvent(new Event<T>(ret ? CacheEventType.DEL_OK
					: CacheEventType.DEL_FAIL, key));
			return ret;
		} catch (TimeoutException e) {
			log.error("xmemcache timeout", e);
			fireEvent(new Event<T>(CacheEventType.DEL_EXCEPTION, key, e));
		} catch (InterruptedException e) {
			log.error("xmemcache interrupted", e);
			fireEvent(new Event<T>(CacheEventType.DEL_EXCEPTION, key, e));
		} catch (MemcachedException e) {
			log.error("xmemcache exception", e);
			fireEvent(new Event<T>(CacheEventType.DEL_EXCEPTION, key, e));
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netfinworks.basis.inf.ucs.client.UCache#flush()
	 */
	public void flush() {
		try {
			memcachedClient.flushAll();
			fireEvent(new Event<T>(CacheEventType.FLUSH_OK));
		} catch (TimeoutException e) {
			log.error("xmemcache timeout", e);
			fireEvent(new Event<T>(CacheEventType.DEL_EXCEPTION, e));
		} catch (InterruptedException e) {
			log.error("xmemcache interrupted", e);
			fireEvent(new Event<T>(CacheEventType.DEL_EXCEPTION, e));
		} catch (MemcachedException e) {
			log.error("xmemcache exception", e);
			fireEvent(new Event<T>(CacheEventType.DEL_EXCEPTION, e));
		}
	}

	public boolean destroy() {
		try {
			memcachedClient.shutdown();
			return true;
		} catch (IOException e) {
			log.error("xmemcache io exception", e);
		}
		return false;
	}

	@Override
	public boolean replace(String key, T t, int expireSecond) {
		try {
			boolean ret = memcachedClient.replace(key, expireSecond, t);
			fireEvent(new Event<T>(ret ? CacheEventType.REPLACE_OK
					: CacheEventType.REPLACE_FAIL, key));
			return ret;
		} catch (TimeoutException e) {
			log.error("xmemcache timeout", e);
			fireEvent(new Event<T>(CacheEventType.REPLACE_EXCEPTION, key, t, e));
		} catch (InterruptedException e) {
			log.error("xmemcache interrupted", e);
			fireEvent(new Event<T>(CacheEventType.REPLACE_EXCEPTION, key, t, e));
		} catch (MemcachedException e) {
			log.error("xmemcache exception", e);
			fireEvent(new Event<T>(CacheEventType.REPLACE_EXCEPTION, key, t, e));
		}
		return false;
	}

	@Override
	public CacheRespone<Long> incr(String key, long delta) {
		try {
			long ret = memcachedClient.incr(key, delta);
			return new CacheRespone<Long>(ret);
		} catch (TimeoutException e) {
			log.error("xmemcache increase timeout", e);
		} catch (InterruptedException e) {
			log.error("xmemcache increase interrupted", e);
		} catch (MemcachedException e) {
			log.error("xmemcache increase exception", e);
		}
		return CacheRespone.failResult();
	}

	@Override
	public CacheRespone<Long> decr(String key, long delta) {
		try {
			long ret = memcachedClient.decr(key, delta);
			return new CacheRespone<Long>(ret);
		} catch (TimeoutException e) {
			log.error("xmemcache decrease timeout", e);
		} catch (InterruptedException e) {
			log.error("xmemcache decrease interrupted", e);
		} catch (MemcachedException e) {
			log.error("xmemcache decrease exception", e);
		}
		return CacheRespone.failResult();
	}

	private void fireEvent(Event<T> event) {
		if (listeners != null && !listeners.isEmpty()) {
			for (CacheListener<T> listener : listeners) {
				try {
					listener.onErroEvent(event);
				} catch (Exception ex) {
					log.error(
							"缓存事件处理异常：listener = {}, event = {}/{}/{}",
							new Object[] { listener, event.getKey(),
									event.getType(), event.getValue() });
					log.error("缓存事件处理异常", ex);
				}
			}
		}
	}
}
