/**
 * Copyright 2014 netfinworks.com, Inc. All rights reserved.
 */
package com.netfinworks.basis.inf.ucs.client;

/**
 * <p>缓存结果</p>
 * @author huipeng
 * @version $Id: CacheResult.java, v 0.1 Jan 8, 2014 10:30:30 AM knico Exp $
 */
public class CacheRespone<T> {
    private boolean success;
    private T       data;

    /**
     * @param data
     */
    public CacheRespone(T data) {
        this.data = data;
        this.success = true;
    }

    public static <T> CacheRespone<T> failResult() {
        CacheRespone<T> ret = new CacheRespone<T>(null);
        ret.success = false;
        return ret;

    }

    /**
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @return the data
     */
    public T get() {
        return data;
    }
}
