/**
 * 
 */
package com.netfinworks.basis.inf.ucs.support.annotation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bigknife
 *
 */
public class TestService implements ITestService {
    private Logger                     log = LoggerFactory.getLogger(getClass());
    private static Map<String, String> kv  = new HashMap<String, String>();
    static {
        for (int i = 0; i < 10; i++) {
            kv.put("key" + i, "hello,world" + i);
        }

    }

    /* (non-Javadoc)
     * @see com.netfinworks.basis.inf.ucs.support.annotation.ITestService#heavyGetSth(java.lang.String)
     */
    @CacheResult(namespace = "ucs.support.test")
    public String heavyGet(@KeyGenerator(keyMethod = "toString") String key, String key2) {
        return innerGet(key);
    }

    private String innerGet(String key) {
        for (int i = 0; i < 5; i++) {
            log.info("Getting .... {}", i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return kv.get(key);
    }

    @InvalidateCache(mode = InvalidateCacheMode.MODE_SINGE, namespace = "ucs.support.test", executeOrder = InvalidateExecuteOrder.AFTER)
    public void reHeavyGet(@KeyGenerator String key) {

    }

    @InvalidateCache(mode = InvalidateCacheMode.MODE_MULTI, namespace = "ucs.support.test")
    public void reHeavyGet(@KeyGenerator List<String> keys) {

    }

    @CacheResult(namespace = "ucs.support.test")
    public String heavyGetCache10s(@KeyGenerator String key, String key2) {
        return innerGet(key);
    }

    @Override
    @CacheResult(namespace = "ucs.support.test")
    public String reHeavyGet(@KeyGenerator(keyMethod = { "getKey1", "getKey2" }) KeyGen kg) {
        return innerGet(kg.getKey1());
    }

    @UpdateCache(namespace = "ucs.support.test.top", expireSecond = 1800)
    public String setTop(long top) {
        return top+"";
    }

    @CacheDecrease(namespace = "ucs.support.test.top")
    public Long decreaseTop(@DeltaValue long delta, @DeltaResult long result) {
        return result;
    }
}
