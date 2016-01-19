/**
 * Copyright 2014 netfinworks.com, Inc. All rights reserved.
 */
package com.netfinworks.basis.inf.ucs.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>远程缓存计数减</p>
 * @author huipeng
 * @version $Id: CacheDecrease.java, v 0.1 Jan 8, 2014 1:59:11 PM knico Exp $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheDecrease {
    /**
     * 缓存命名空间
     * @return
     */
    public abstract String namespace();

    /**
     * 指定的缓存key，如果方法签名中指定了KeyProvider标签，则优先使用KeyProvider，忽略assignedKey.
     * @return
     */
    public abstract String assignedKey() default "";
}
