/**
 * 
 */
package com.netfinworks.basis.inf.ucs.client;

/**
 * 统一缓存接口
 * @author bigknife
 *
 */
public interface UCache<T> {

    boolean init();

    CacheRespone<T> get(String key);

    boolean add(String key, T t, int expireSecond);

    boolean set(String key, T t, int expireSecond);

    boolean replace(String key, T t, int expireSecond);

    boolean touch(String key, int newExpireSecond);

    CacheRespone<T> getAndTouch(String key, int newExpireSecond);

    boolean delete(String key);

    void flush();

    boolean destroy();

    CacheRespone<Long> incr(String key, long delta);

    CacheRespone<Long> decr(String key, long delta);
}
